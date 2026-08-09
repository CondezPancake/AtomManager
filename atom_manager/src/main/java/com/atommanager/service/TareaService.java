package com.atommanager.service;

import com.atommanager.model.Estado;
import com.atommanager.model.Prioridad;
import com.atommanager.model.Tarea;
import com.atommanager.model.Usuario;
import com.atommanager.repository.TareaRepository;
import com.atommanager.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reglas de negocio de la Épica 2 (HU-04 a HU-07), HU-03 (Épica 1) y
 * HU-08 a HU-10 (Épica 3).
 * Depende de {@link TareaRepository} y {@link UsuarioRepository} por
 * constructor (SOLID-D): necesita el segundo para validar que el
 * responsable de HU-05 exista.
 */
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtomicInteger contadorId = new AtomicInteger(0);

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /** HU-04: crea una tarea con id único autogenerado; el título obligatorio lo valida {@link Tarea}. */
    public Tarea crearTarea(String titulo, String descripcion, Prioridad prioridad) {
        String id = "T-" + contadorId.incrementAndGet();
        Tarea tarea = new Tarea(id, titulo, descripcion, prioridad);
        tareaRepository.guardar(tarea);
        return tarea;
    }

    /** HU-05: asigna una tarea a un usuario ya registrado. */
    public void asignarTarea(String tareaId, String usuarioId) {
        Tarea tarea = obtenerTareaOLanzar(tareaId);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("No existe un usuario con el id \"" + usuarioId + "\".");
        }
        tarea.setResponsable(usuario);
    }

    /** HU-06: cambia la prioridad de una tarea existente. */
    public void cambiarPrioridad(String tareaId, Prioridad prioridad) {
        obtenerTareaOLanzar(tareaId).setPrioridad(prioridad);
    }

    /** HU-08: cambia el estado de una tarea existente. */
    public void cambiarEstado(String tareaId, Estado estado) {
        obtenerTareaOLanzar(tareaId).setEstado(estado);
    }

    /** HU-07: lista todas las tareas registradas. */
    public List<Tarea> listarTareas() {
        return tareaRepository.listarTodas();
    }

    /** HU-09: agrupa las tareas por estado para la vista tipo tablero. */
    public Map<Estado, List<Tarea>> tareasPorEstado() {
        Map<Estado, List<Tarea>> tareasAgrupadas = new EnumMap<>(Estado.class);
        for (Estado estado : Estado.values()) {
            tareasAgrupadas.put(estado, tareaRepository.buscarPorEstado(estado));
        }
        return tareasAgrupadas;
    }

    /** HU-03: tareas de un usuario ordenadas de mayor a menor prioridad, vía PriorityQueue. */
    public List<Tarea> tareasPorPrioridad(String usuarioId) {
        return ordenarPorPrioridad(tareaRepository.buscarPorUsuario(usuarioId));
    }

    /** HU-10: todas las tareas ordenadas de mayor a menor prioridad, vía PriorityQueue. */
    public List<Tarea> tareasPorPrioridad() {
        return ordenarPorPrioridad(tareaRepository.listarTodas());
    }

    private List<Tarea> ordenarPorPrioridad(List<Tarea> tareas) {
        Queue<Tarea> cola = new PriorityQueue<>(
                Comparator.comparingInt(tarea -> tarea.getPrioridad().getPeso())
        );
        cola.addAll(tareas);

        List<Tarea> ordenadas = new ArrayList<>();
        while (!cola.isEmpty()) {
            ordenadas.add(cola.poll());
        }
        return ordenadas;
    }

    private Tarea obtenerTareaOLanzar(String tareaId) {
        Tarea tarea = tareaRepository.buscarPorId(tareaId);
        if (tarea == null) {
            throw new IllegalArgumentException("No existe una tarea con el id \"" + tareaId + "\".");
        }
        return tarea;
    }
}
