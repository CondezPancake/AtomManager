package com.atommanager.service;

import com.atommanager.model.Estado;
import com.atommanager.model.Prioridad;
import com.atommanager.model.Tarea;
import com.atommanager.repository.TareaRepository;
import com.atommanager.repository.TareaRepositoryMemoria;
import com.atommanager.repository.UsuarioRepository;
import com.atommanager.repository.UsuarioRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Épica 2 — Gestión de tareas (HU-04 a HU-07), HU-03 (Épica 1) y
 * HU-08 (Épica 3).
 */
class TareaServiceTest {

    private TareaRepository tareaRepository;
    private UsuarioRepository usuarioRepository;
    private TareaService tareaService;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        tareaRepository = new TareaRepositoryMemoria();
        usuarioRepository = new UsuarioRepositoryMemoria();
        tareaService = new TareaService(tareaRepository, usuarioRepository);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void crearTarea_generaIdUnicoAutomaticamente() {
        Tarea t1 = tareaService.crearTarea("Corregir bug", "desc", Prioridad.ALTA);
        Tarea t2 = tareaService.crearTarea("Otra tarea", "desc", Prioridad.BAJA);

        assertNotNull(t1.getId());
        assertNotNull(t2.getId());
        assertNotEquals(t1.getId(), t2.getId());
        assertEquals(Estado.POR_REALIZAR, t1.getEstado());
    }

    @Test
    void crearTarea_sinTitulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> tareaService.crearTarea("  ", "desc", Prioridad.ALTA));
    }

    @Test
    void asignarTarea_aUsuarioExistente_laAsigna() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.MODERADA);
        usuarioService.registrarUsuario("u1", "Ana");

        tareaService.asignarTarea(tarea.getId(), "u1");

        assertEquals("u1", tarea.getResponsable().getId());
    }

    @Test
    void asignarTarea_aUsuarioInexistente_lanzaExcepcion() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.MODERADA);

        assertThrows(IllegalArgumentException.class,
                () -> tareaService.asignarTarea(tarea.getId(), "no-existe"));
    }

    @Test
    void asignarTarea_conIdDeTareaInexistente_lanzaExcepcion() {
        usuarioService.registrarUsuario("u1", "Ana");

        assertThrows(IllegalArgumentException.class,
                () -> tareaService.asignarTarea("no-existe", "u1"));
    }

    @Test
    void cambiarPrioridad_actualizaLaPrioridadDeLaTarea() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.BAJA);

        tareaService.cambiarPrioridad(tarea.getId(), Prioridad.ALTA);

        assertEquals(Prioridad.ALTA, tarea.getPrioridad());
    }

    @Test
    void cambiarEstado_actualizaElEstadoDeLaTarea() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.BAJA);

        tareaService.cambiarEstado(tarea.getId(), Estado.EN_PROCESO);
        tareaService.cambiarEstado(tarea.getId(), Estado.FINALIZADA);

        assertEquals(Estado.FINALIZADA, tarea.getEstado());
    }

    @Test
    void cambiarEstado_conIdDeTareaInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> tareaService.cambiarEstado("no-existe", Estado.EN_PROCESO));
    }

    @Test
    void cambiarEstado_conEstadoNulo_lanzaExcepcion() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.BAJA);

        assertThrows(IllegalArgumentException.class,
                () -> tareaService.cambiarEstado(tarea.getId(), null));
    }

    @Test
    void listarTareas_devuelveTodasLasCreadas() {
        tareaService.crearTarea("Tarea 1", "desc", Prioridad.ALTA);
        tareaService.crearTarea("Tarea 2", "desc", Prioridad.BAJA);

        assertEquals(2, tareaService.listarTareas().size());
    }

    @Test
    void tareasPorPrioridad_lasOrdenaDeAltaABaja() {
        usuarioService.registrarUsuario("u1", "Ana");
        Tarea baja = tareaService.crearTarea("Baja", "desc", Prioridad.BAJA);
        Tarea alta = tareaService.crearTarea("Alta", "desc", Prioridad.ALTA);
        Tarea moderada = tareaService.crearTarea("Moderada", "desc", Prioridad.MODERADA);
        tareaService.asignarTarea(baja.getId(), "u1");
        tareaService.asignarTarea(alta.getId(), "u1");
        tareaService.asignarTarea(moderada.getId(), "u1");

        List<Tarea> ordenadas = tareaService.tareasPorPrioridad("u1");

        assertEquals(List.of(alta, moderada, baja), ordenadas);
    }

    @Test
    void tareasPorPrioridad_deUsuarioSinTareas_devuelveListaVacia() {
        assertTrue(tareaService.tareasPorPrioridad("sin-tareas").isEmpty());
    }
}
