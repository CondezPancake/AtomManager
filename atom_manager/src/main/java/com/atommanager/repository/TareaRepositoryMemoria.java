package com.atommanager.repository;

import com.atommanager.model.Tarea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación en memoria de {@link TareaRepository} con
 * {@code HashMap} como almacén principal (sección 4.1).
 */
public class TareaRepositoryMemoria implements TareaRepository {

    private final Map<String, Tarea> tareas = new HashMap<>();

    @Override
    public void guardar(Tarea tarea) {
        tareas.put(tarea.getId(), tarea);
    }

    @Override
    public Tarea buscarPorId(String id) {
        return tareas.get(id);
    }

    @Override
    public List<Tarea> listarTodas() {
        return new ArrayList<>(tareas.values());
    }

    @Override
    public List<Tarea> buscarPorUsuario(String usuarioId) {
        return tareas.values().stream()
                .filter(tarea -> tarea.getResponsable() != null
                        && tarea.getResponsable().getId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existe(String id) {
        return tareas.containsKey(id);
    }
}
