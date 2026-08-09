package com.atommanager.ui;

/**
 * Menú principal de la aplicación (Swing / JOptionPane, RF-17).
 *
 * <p>SOLID-S (responsabilidad única): esta clase solo presenta y navega;
 * no contiene reglas de negocio, delega en {@code UsuarioService} y
 * {@code TareaService}.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>Referencias a {@code UsuarioService} y {@code TareaService} (inyectadas desde {@code Main}).</li>
 *   <li>Menú de opciones y navegación hacia {@code VistaUsuarios} / {@code VistaTareas}.</li>
 * </ul>
 */
public class MenuPrincipal {
}
