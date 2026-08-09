/**
 * Capa de presentación de AtomManager, implementada con Swing / JOptionPane
 * (RF-17).
 *
 * <p>Qué va acá:</p>
 * <ul>
 *   <li>{@code MenuPrincipal} — menú de arranque, navega hacia las demás vistas.</li>
 *   <li>{@code VistaUsuarios} — pantallas de alta y listado de usuarios.</li>
 *   <li>{@code VistaTareas} — pantallas de alta, asignación y consulta de tareas.</li>
 * </ul>
 *
 * <p>Reglas de diseño:</p>
 * <ul>
 *   <li>Sin lógica de negocio ni validaciones de dominio: solo pide datos, invoca al {@code service} correspondiente y muestra el resultado (SOLID-S).</li>
 *   <li>Depende de {@code UsuarioService} / {@code TareaService} (paquete {@code service}), nunca de {@code repository} directamente.</li>
 * </ul>
 */
package com.atommanager.ui;
