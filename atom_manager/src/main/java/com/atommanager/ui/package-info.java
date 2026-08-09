/**
 * Acá van las pantallas del programa: lo que ve y con lo que interactúa la
 * persona que usa AtomManager (hechas con ventanas simples de Java,
 * {@code JOptionPane}).
 *
 * <p>Qué hay en esta carpeta:</p>
 * <ul>
 *   <li>{@code MenuPrincipal}: el menú de arranque, desde donde se llega a
 *   las demás pantallas.</li>
 *   <li>{@code VistaUsuarios}: las pantallas para registrar y ver usuarios.</li>
 *   <li>{@code VistaTareas}: las pantallas para crear, asignar y consultar
 *   tareas.</li>
 * </ul>
 *
 * <p>Regla importante: las pantallas de acá no deciden nada por su cuenta.
 * Solo le piden datos a la persona, se los pasan a la carpeta
 * {@code service}, y muestran lo que {@code service} les devuelve (el
 * resultado, o el mensaje de error si algo salió mal). Toda la parte
 * "inteligente" ya está resuelta en {@code service}.</p>
 */
package com.atommanager.ui;
