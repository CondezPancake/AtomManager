package com.atommanager.model;

/**
 * Estados posibles del ciclo de vida de una tarea (RF-09, RF-10).
 *
 * <p>Transición habitual: {@code POR_REALIZAR → EN_PROCESO → FINALIZADA},
 * permitiendo retroceder si el usuario lo requiere.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code POR_REALIZAR} — tarea registrada, aún no iniciada.</li>
 *   <li>{@code EN_PROCESO} — tarea en desarrollo por su responsable.</li>
 *   <li>{@code FINALIZADA} — tarea completada.</li>
 * </ul>
 */
public enum Estado {
}
