package com.atommanager.ui;

import com.atommanager.service.TareaService;
import com.atommanager.service.UsuarioService;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Ventana principal, con JavaFX: dos apartados bien separados (pestañas),
 * uno para usuarios y otro para tareas, en vez de un menú con una lista de
 * opciones en texto plano.
 *
 * <p>No contiene reglas de negocio: cada pestaña la arma
 * {@link VistaUsuarios} o {@link VistaTareas}, que a su vez delegan en los
 * {@code service} correspondientes.</p>
 */
public class MenuPrincipal {

    private final UsuarioService usuarioService;
    private final TareaService tareaService;

    public MenuPrincipal(UsuarioService usuarioService, TareaService tareaService) {
        this.usuarioService = usuarioService;
        this.tareaService = tareaService;
    }

    /**
     * Arranca el motor gráfico de JavaFX y muestra la ventana principal.
     *
     * <p>Se usa {@code Platform.startup(...)} en vez de extender
     * {@code Application}: así {@code MenuPrincipal} sigue recibiendo los
     * {@code service} por constructor, igual que en las versiones
     * anteriores (terminal, JOptionPane, Swing).</p>
     */
    public void mostrar() {
        Platform.startup(this::construirVentana);
    }

    private void construirVentana() {
        VistaUsuarios vistaUsuarios = new VistaUsuarios(usuarioService);
        VistaTareas vistaTareas = new VistaTareas(tareaService);

        TabPane pestanas = new TabPane(vistaUsuarios.crearPestana(), vistaTareas.crearPestana());
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene escena = new Scene(pestanas, 960, 620);
        escena.getStylesheets().add(getClass().getResource("/atommanager.css").toExternalForm());

        Stage ventana = new Stage();
        ventana.setTitle("AtomManager");
        ventana.setScene(escena);
        ventana.show();
    }
}
