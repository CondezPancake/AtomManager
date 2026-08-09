package com.atommanager.service;

import com.atommanager.repository.TareaRepository;

/**
 * Reglas de negocio para tareas: creación, asignación, prioridad, estado y
 * ordenamiento mediante {@code PriorityQueue} (sección 4.2).
 *
 * <p>SOLID-D (inversión de dependencias): depende de la interfaz
 * {@link TareaRepository}; la implementación concreta se inyecta desde
 * {@code Main}. SOLID-O (abierto/cerrado): nuevas formas de ordenar tareas
 * se agregan con un nuevo {@code Comparator<Tarea>}, sin modificar esta
 * clase.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code TareaRepository tareaRepository} (inyectado por constructor).</li>
 *   <li>{@code crearTarea(...)} (RF-03, RF-04, RF-05, RF-15, HU-04).</li>
 *   <li>{@code asignarTarea(String tareaId, String usuarioId)} (RF-06, HU-05).</li>
 *   <li>{@code cambiarPrioridad(...)}, {@code cambiarEstado(...)} (RF-07 a RF-10, HU-06, HU-08).</li>
 *   <li>{@code Queue<Tarea> tareasPorPrioridad(String usuarioId)} (RF-11, HU-03).</li>
 * </ul>
 */
public class TareaService {
}
