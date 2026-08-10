package com.atommanager.ui;

import com.atommanager.model.Estado;
import com.atommanager.model.Prioridad;
import com.atommanager.model.Tarea;
import com.atommanager.model.Usuario;
import com.atommanager.service.TareaService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Apartado de tareas (HU-03 a HU-10): formulario de alta, tabla con todas
 * las tareas, acciones sobre la tarea seleccionada y consultas, como una
 * pestaña de JavaFX.
 *
 * <p>No decide nada por su cuenta: pide datos, se los pasa a
 * {@link TareaService} y muestra el resultado (los errores los atrapa
 * {@link Dialogos}).</p>
 */
public class VistaTareas {

    private final TareaService tareaService;
    private final TableView<Tarea> tabla = new TableView<>();

    public VistaTareas(TareaService tareaService) {
        this.tareaService = tareaService;
        configurarTabla();
    }

    public Tab crearPestana() {
        Label titulo = new Label("Tareas");
        titulo.getStyleClass().add("titulo-seccion");

        VBox contenido = new VBox(12,
                titulo,
                formularioCrear(),
                barraTabla(),
                tabla,
                panelAccionesSobreSeleccion(),
                panelConsultas());
        contenido.setPadding(new Insets(16));

        actualizarTabla();

        Tab pestana = new Tab("Tareas", contenido);
        pestana.setClosable(false);
        return pestana;
    }

    private HBox barraTabla() {
        Button botonRecargar = new Button("🔄 Recargar tabla");
        botonRecargar.setOnAction(e -> actualizarTabla());

        HBox barra = new HBox(botonRecargar);
        barra.setAlignment(Pos.CENTER_RIGHT);
        return barra;
    }

    private HBox formularioCrear() {
        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");
        TextField campoDescripcion = new TextField();
        campoDescripcion.setPromptText("Descripción");
        ComboBox<Prioridad> comboPrioridad = comboPrioridad();

        Button botonCrear = new Button("Crear tarea");
        botonCrear.setOnAction(e -> Dialogos.ejecutar(() -> {
            tareaService.crearTarea(campoTitulo.getText(), campoDescripcion.getText(), comboPrioridad.getValue());
            campoTitulo.clear();
            campoDescripcion.clear();
            actualizarTabla();
        }));

        HBox formulario = new HBox(10,
                new Label("Título:"), campoTitulo,
                new Label("Descripción:"), campoDescripcion,
                new Label("Prioridad:"), comboPrioridad,
                botonCrear);
        formulario.setAlignment(Pos.CENTER_LEFT);
        formulario.getStyleClass().add("form-panel");
        return formulario;
    }

    private HBox panelAccionesSobreSeleccion() {
        TextField campoUsuarioId = new TextField();
        campoUsuarioId.setPromptText("ID de usuario");
        Button botonAsignar = new Button("Asignar");
        botonAsignar.setOnAction(e -> Dialogos.ejecutar(() -> {
            Tarea tarea = tareaSeleccionadaOLanzar();
            tareaService.asignarTarea(tarea.getId(), campoUsuarioId.getText());
            actualizarTabla();
        }));

        ComboBox<Prioridad> comboPrioridad = comboPrioridad();
        Button botonPrioridad = new Button("Cambiar prioridad");
        botonPrioridad.setOnAction(e -> Dialogos.ejecutar(() -> {
            Tarea tarea = tareaSeleccionadaOLanzar();
            tareaService.cambiarPrioridad(tarea.getId(), comboPrioridad.getValue());
            actualizarTabla();
        }));

        ComboBox<Estado> comboEstado = comboEstado();
        Button botonEstado = new Button("Cambiar estado");
        botonEstado.setOnAction(e -> Dialogos.ejecutar(() -> {
            Tarea tarea = tareaSeleccionadaOLanzar();
            tareaService.cambiarEstado(tarea.getId(), comboEstado.getValue());
            actualizarTabla();
        }));

        HBox panel = new HBox(10,
                new Label("Sobre la tarea seleccionada en la tabla —"),
                new Label("Usuario:"), campoUsuarioId, botonAsignar,
                new Label("Prioridad:"), comboPrioridad, botonPrioridad,
                new Label("Estado:"), comboEstado, botonEstado);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.getStyleClass().add("form-panel");
        return panel;
    }

    private HBox panelConsultas() {
        TextField campoUsuarioId = new TextField();
        campoUsuarioId.setPromptText("ID de usuario");
        Button botonPorUsuario = new Button("Ver tareas de este usuario (por prioridad)");
        botonPorUsuario.setOnAction(e -> Dialogos.ejecutar(() ->
                Dialogos.mostrarInfo("Tareas de " + campoUsuarioId.getText(),
                        formatearListado(tareaService.tareasPorPrioridad(campoUsuarioId.getText())))));

        Button botonTablero = new Button("Ver tablero por estado");
        botonTablero.setOnAction(e -> Dialogos.ejecutar(() ->
                Dialogos.mostrarInfo("Tareas por estado", formatearTablero(tareaService.tareasPorEstado()))));

        Button botonTodasPorPrioridad = new Button("Ver todas por prioridad");
        botonTodasPorPrioridad.setOnAction(e -> Dialogos.ejecutar(() ->
                Dialogos.mostrarInfo("Todas las tareas (por prioridad)",
                        formatearListado(tareaService.tareasPorPrioridad()))));

        HBox panel = new HBox(10, new Label("Consultas —"), new Label("Usuario:"), campoUsuarioId,
                botonPorUsuario, botonTablero, botonTodasPorPrioridad);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.getStyleClass().add("form-panel");
        return panel;
    }

    private Tarea tareaSeleccionadaOLanzar() {
        Tarea tarea = tabla.getSelectionModel().getSelectedItem();
        if (tarea == null) {
            throw new IllegalArgumentException("Seleccioná una tarea de la tabla primero.");
        }
        return tarea;
    }

    private ComboBox<Prioridad> comboPrioridad() {
        ComboBox<Prioridad> combo = new ComboBox<>(FXCollections.observableArrayList(Prioridad.values()));
        combo.getSelectionModel().selectFirst();
        return combo;
    }

    private ComboBox<Estado> comboEstado() {
        ComboBox<Estado> combo = new ComboBox<>(FXCollections.observableArrayList(Estado.values()));
        combo.getSelectionModel().selectFirst();
        return combo;
    }

    private void configurarTabla() {
        TableColumn<Tarea, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Tarea, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Tarea, Prioridad> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        TableColumn<Tarea, Estado> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Tarea, String> colResponsable = new TableColumn<>("Responsable");
        colResponsable.setCellValueFactory(fila -> {
            Usuario responsable = fila.getValue().getResponsable();
            return new SimpleStringProperty(responsable == null ? "sin asignar" : responsable.getNombre());
        });

        tabla.getColumns().addAll(colId, colTitulo, colPrioridad, colEstado, colResponsable);
        tabla.setPlaceholder(new Label("Todavía no hay tareas creadas."));
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void actualizarTabla() {
        List<Tarea> tareas = tareaService.listarTareas();
        ObservableList<Tarea> datos = FXCollections.observableArrayList(tareas);
        tabla.setItems(datos);
    }

    private String formatearListado(List<Tarea> tareas) {
        if (tareas.isEmpty()) {
            return "No hay tareas para mostrar.";
        }
        StringBuilder texto = new StringBuilder();
        for (Tarea tarea : tareas) {
            texto.append(tarea).append("\n");
        }
        return texto.toString();
    }

    private String formatearTablero(Map<Estado, List<Tarea>> tablero) {
        StringBuilder texto = new StringBuilder();
        for (Estado estado : Estado.values()) {
            List<Tarea> tareas = tablero.get(estado);
            texto.append(estado.getEtiqueta()).append(" (").append(tareas.size()).append("):\n");
            if (tareas.isEmpty()) {
                texto.append("  (sin tareas)\n");
            }
            for (Tarea tarea : tareas) {
                texto.append("  ").append(tarea).append("\n");
            }
        }
        return texto.toString();
    }
}
