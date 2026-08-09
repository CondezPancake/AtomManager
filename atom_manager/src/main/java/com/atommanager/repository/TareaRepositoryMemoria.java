package com.atommanager.repository;

import com.atommanager.model.Tarea;

/**
 * Implementación en memoria de {@link TareaRepository} usando un
 * {@code HashMap} como almacén principal (sección 4.1).
 *
 * <p>SOLID-L (sustitución de Liskov): cualquier otra implementación de
 * {@code TareaRepository} puede reemplazar a esta sin romper
 * {@code TareaService}.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code Map<String, Tarea> tareas = new HashMap<>();}</li>
 *   <li>{@code Map<String, List<String>> tareasPorUsuario = new HashMap<>();}</li>
 *   <li>{@code Map<Estado, List<Tarea>> tareasPorEstado = new HashMap<>();}</li>
 * </ul>
 */
public class TareaRepositoryMemoria implements TareaRepository {
}
