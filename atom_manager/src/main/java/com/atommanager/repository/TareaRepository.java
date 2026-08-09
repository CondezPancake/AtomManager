package com.atommanager.repository;

import com.atommanager.model.Estado;
import com.atommanager.model.Tarea;

import java.util.List;

/**
 * Contrato de acceso a datos de {@link Tarea} (HU-03 a HU-09).
 * SOLID-D: {@code TareaService} depende de esta interfaz, no de la
 * implementación concreta.
 */
public interface TareaRepository {

    void guardar(Tarea tarea);

    Tarea buscarPorId(String id);

    List<Tarea> listarTodas();

    /** Tareas asignadas a un usuario (base de HU-03). */
    List<Tarea> buscarPorUsuario(String usuarioId);

    /** Tareas con un estado determinado (base de HU-09). */
    List<Tarea> buscarPorEstado(Estado estado);

    boolean existe(String id);
}
