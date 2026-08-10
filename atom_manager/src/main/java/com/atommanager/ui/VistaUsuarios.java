package com.atommanager.ui;

import com.atommanager.model.Usuario;
import com.atommanager.service.UsuarioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Apartado de usuarios (HU-01, HU-02): formulario de alta + tabla con los
 * usuarios registrados, como una pestaña de JavaFX.
 *
 * <p>No decide nada por su cuenta: pide datos, se los pasa a
 * {@link UsuarioService} y muestra el resultado (los errores los atrapa
 * {@link Dialogos}).</p>
 */
public class VistaUsuarios {

    private final UsuarioService usuarioService;
    private final TableView<Usuario> tabla = new TableView<>();

    public VistaUsuarios(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        configurarTabla();
    }

    public Tab crearPestana() {
        Label titulo = new Label("Usuarios");
        titulo.getStyleClass().add("titulo-seccion");

        VBox contenido = new VBox(12, titulo, formularioAlta(), tabla, panelAcciones());
        contenido.setPadding(new Insets(16));

        actualizarTabla();

        Tab pestana = new Tab("Usuarios", contenido);
        pestana.setClosable(false);
        return pestana;
    }

    private HBox formularioAlta() {
        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nombre");

        Button botonRegistrar = new Button("Registrar usuario");
        botonRegistrar.setOnAction(e -> Dialogos.ejecutar(() -> {
            usuarioService.registrarUsuario(campoNombre.getText());
            campoNombre.clear();
            actualizarTabla();
        }));

        HBox formulario = new HBox(10, new Label("Nombre:"), campoNombre, botonRegistrar);
        formulario.setAlignment(Pos.CENTER_LEFT);
        formulario.getStyleClass().add("form-panel");
        return formulario;
    }

    private HBox panelAcciones() {
        Button botonEliminar = new Button("Eliminar usuario seleccionado");
        botonEliminar.setOnAction(e -> Dialogos.ejecutar(() -> {
            Usuario usuario = usuarioSeleccionadoOLanzar();
            if (Dialogos.confirmar("¿Eliminar al usuario \"" + usuario.getNombre() + "\" (" + usuario.getId() + ")? Esta acción no se puede deshacer.")) {
                usuarioService.eliminarUsuario(usuario.getId());
                actualizarTabla();
            }
        }));

        HBox panel = new HBox(botonEliminar);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.getStyleClass().add("form-panel");
        return panel;
    }

    private Usuario usuarioSeleccionadoOLanzar() {
        Usuario usuario = tabla.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            throw new IllegalArgumentException("Seleccioná un usuario de la tabla primero.");
        }
        return usuario;
    }

    private void configurarTabla() {
        TableColumn<Usuario, String> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Usuario, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tabla.getColumns().addAll(columnaId, columnaNombre);
        tabla.setPlaceholder(new Label("Todavía no hay usuarios registrados."));
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void actualizarTabla() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        ObservableList<Usuario> datos = FXCollections.observableArrayList(usuarios);
        tabla.setItems(datos);
        tabla.refresh();
    }
}
