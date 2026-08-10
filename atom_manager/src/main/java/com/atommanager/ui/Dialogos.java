package com.atommanager.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

/**
 * Ayudas comunes para mostrar mensajes en las pantallas de JavaFX, para no
 * repetir el mismo código de manejo de errores en {@link VistaUsuarios} y
 * {@link VistaTareas}.
 */
final class Dialogos {

    private Dialogos() {
    }

    /** Corre una acción y, si tira un error de negocio, lo muestra como diálogo en vez de romper la ventana. */
    static void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText("Error");
        alerta.showAndWait();
    }

    /** Pide confirmación antes de una acción que no se puede deshacer (por ejemplo, borrar). */
    static boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText("Confirmar");
        return alerta.showAndWait().filter(boton -> boton == ButtonType.YES).isPresent();
    }

    /** Muestra un texto (potencialmente largo, como un listado de tareas) en un cuadro con scroll. */
    static void mostrarInfo(String titulo, String mensaje) {
        TextArea area = new TextArea(mensaje);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefSize(480, 320);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(titulo);
        alerta.getDialogPane().setContent(area);
        alerta.showAndWait();
    }
}
