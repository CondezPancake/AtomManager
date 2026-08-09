/**
 * Entidades del dominio de AtomManager.
 *
 * <p>Solo contiene clases de datos ({@code Usuario}, {@code Tarea}) y enums
 * ({@code Prioridad}, {@code Estado}): atributos privados con getters/setters
 * y validaciones propias. No depende de ningún otro paquete del proyecto ni
 * contiene lógica de negocio ni de acceso a datos (SOLID-S).</p>
 *
 * <p>Qué va acá:</p>
 * <ul>
 *   <li>{@code Usuario} — integrante del proyecto (id, nombre).</li>
 *   <li>{@code Tarea} — actividad del proyecto (id, titulo, descripcion, prioridad, estado, responsable).</li>
 *   <li>{@code Prioridad} (enum) — ALTA, MODERADA, BAJA.</li>
 *   <li>{@code Estado} (enum) — POR_REALIZAR, EN_PROCESO, FINALIZADA.</li>
 * </ul>
 *
 * <p>Qué NO va acá: repositorios, services ni vistas, ni ninguna clase que
 * dependa de Swing, {@code HashMap} o {@code PriorityQueue}.</p>
 */
package com.atommanager.model;
