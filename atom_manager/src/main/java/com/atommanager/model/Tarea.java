package com.atommanager.model;

/**
 * Representa una actividad del proyecto.
 *
 * <p>Clase de datos (encapsulamiento). Composición: una {@code Tarea}
 * referencia al {@link Usuario} responsable.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code id} (String) — identificador único de la tarea.</li>
 *   <li>{@code titulo} (String) — título de la tarea (obligatorio).</li>
 *   <li>{@code descripcion} (String) — descripción de la tarea.</li>
 *   <li>{@code prioridad} ({@link Prioridad}) — Alta, Moderada o Baja.</li>
 *   <li>{@code estado} ({@link Estado}) — Por realizar, En proceso o Finalizada.</li>
 *   <li>{@code responsable} ({@link Usuario}) — usuario asignado a la tarea.</li>
 *   <li>Constructores, getters y setters con validaciones (RF-05, RF-15).</li>
 * </ul>
 */
public class Tarea {
}