/**
 * Acá van las pantallas del programa: lo que ve y con lo que interactúa la
 * persona que usa AtomManager, hechas con JavaFX (una ventana con pestañas,
 * tablas y formularios — no cuadros de diálogo sueltos).
 *
 * <p>Qué hay en esta carpeta:</p>
 * <ul>
 *   <li>{@code MenuPrincipal}: arranca JavaFX y arma la ventana principal,
 *   con una pestaña por cada apartado.</li>
 *   <li>{@code VistaUsuarios}: el apartado de usuarios (formulario de alta
 *   + tabla con los registrados).</li>
 *   <li>{@code VistaTareas}: el apartado de tareas (formulario de alta,
 *   tabla, acciones sobre la tarea seleccionada y consultas).</li>
 *   <li>{@code Dialogos}: ayudas compartidas para mostrar errores y
 *   listados, así {@code VistaUsuarios}/{@code VistaTareas} no repiten ese
 *   código.</li>
 * </ul>
 *
 * <p>Regla importante: las pantallas de acá no deciden nada por su cuenta.
 * Solo le piden datos a la persona, se los pasan a la carpeta
 * {@code service}, y muestran lo que {@code service} les devuelve (el
 * resultado, o el mensaje de error si algo salió mal). Toda la parte
 * "inteligente" ya está resuelta en {@code service}.</p>
 */
package com.atommanager.ui;
