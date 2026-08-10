package com.atommanager.service;

import com.atommanager.model.Estado;
import com.atommanager.model.Prioridad;
import com.atommanager.model.Tarea;
import com.atommanager.model.Usuario;
import com.atommanager.repository.TareaRepository;
import com.atommanager.repository.TareaRepositoryMemoria;
import com.atommanager.repository.UsuarioRepository;
import com.atommanager.repository.UsuarioRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Épica 2 — Gestión de tareas (HU-04 a HU-07), HU-03 (Épica 1) y
 * HU-08 a HU-10 (Épica 3).
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
    void crearTarea_generaIdsConFormatoNumericoConsecutivo() {
        Tarea t1 = tareaService.crearTarea("Tarea 1", "desc", Prioridad.ALTA);
        Tarea t2 = tareaService.crearTarea("Tarea 2", "desc", Prioridad.ALTA);
        Tarea t3 = tareaService.crearTarea("Tarea 3", "desc", Prioridad.ALTA);

        assertEquals("01", t1.getId());
        assertEquals("02", t2.getId());
        assertEquals("03", t3.getId());
    }

    @Test
    void eliminarTarea_despuesDeBorrarLaUltima_laSiguienteIdReocupaEseLugar() {
        tareaService.crearTarea("Tarea 1", "desc", Prioridad.ALTA);
        Tarea t2 = tareaService.crearTarea("Tarea 2", "desc", Prioridad.ALTA);

        tareaService.eliminarTarea(t2.getId());
        Tarea t3 = tareaService.crearTarea("Tarea 3", "desc", Prioridad.ALTA);

        assertEquals("02", t3.getId());
    }

    @Test
    void crearTarea_sinTitulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> tareaService.crearTarea("  ", "desc", Prioridad.ALTA));
    }

    @Test
    void asignarTarea_aUsuarioExistente_laAsigna() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.MODERADA);
        Usuario ana = usuarioService.registrarUsuario("Ana");

        tareaService.asignarTarea(tarea.getId(), ana.getId());

        assertEquals(ana.getId(), tarea.getResponsable().getId());
    }

    @Test
    void asignarTarea_aUsuarioInexistente_lanzaExcepcion() {
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.MODERADA);

        assertThrows(IllegalArgumentException.class,
                () -> tareaService.asignarTarea(tarea.getId(), "no-existe"));
    }

    @Test
    void asignarTarea_conIdDeTareaInexistente_lanzaExcepcion() {
        Usuario ana = usuarioService.registrarUsuario("Ana");

        assertThrows(IllegalArgumentException.class,
                () -> tareaService.asignarTarea("no-existe", ana.getId()));
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
    void listarTareas_lasDevuelveOrdenadasPorId() {
        Tarea t1 = tareaService.crearTarea("Tarea 1", "desc", Prioridad.BAJA);
        Tarea t2 = tareaService.crearTarea("Tarea 2", "desc", Prioridad.ALTA);

        tareaService.eliminarTarea(t1.getId());
        Tarea t3 = tareaService.crearTarea("Tarea 3", "desc", Prioridad.MODERADA);

        assertEquals(List.of(t2, t3), tareaService.listarTareas());
    }

    @Test
    void tareasPorEstado_lasAgrupaEnLasTresColumnas() {
        Tarea porRealizar = tareaService.crearTarea("Por realizar", "desc", Prioridad.BAJA);
        Tarea enProceso = tareaService.crearTarea("En proceso", "desc", Prioridad.MODERADA);
        Tarea finalizada = tareaService.crearTarea("Finalizada", "desc", Prioridad.ALTA);
        tareaService.cambiarEstado(enProceso.getId(), Estado.EN_PROCESO);
        tareaService.cambiarEstado(finalizada.getId(), Estado.FINALIZADA);

        Map<Estado, List<Tarea>> agrupadas = tareaService.tareasPorEstado();

        assertEquals(List.of(porRealizar), agrupadas.get(Estado.POR_REALIZAR));
        assertEquals(List.of(enProceso), agrupadas.get(Estado.EN_PROCESO));
        assertEquals(List.of(finalizada), agrupadas.get(Estado.FINALIZADA));
    }

    @Test
    void tareasPorEstado_sinTareas_devuelveLasTresColumnasVacias() {
        Map<Estado, List<Tarea>> agrupadas = tareaService.tareasPorEstado();

        assertEquals(3, agrupadas.size());
        assertTrue(agrupadas.containsKey(Estado.POR_REALIZAR));
        assertTrue(agrupadas.containsKey(Estado.EN_PROCESO));
        assertTrue(agrupadas.containsKey(Estado.FINALIZADA));
        assertTrue(agrupadas.values().stream().allMatch(List::isEmpty));
    }

    @Test
    void tareasPorPrioridad_lasOrdenaDeAltaABaja() {
        Usuario ana = usuarioService.registrarUsuario("Ana");
        Tarea baja = tareaService.crearTarea("Baja", "desc", Prioridad.BAJA);
        Tarea alta = tareaService.crearTarea("Alta", "desc", Prioridad.ALTA);
        Tarea moderada = tareaService.crearTarea("Moderada", "desc", Prioridad.MODERADA);
        tareaService.asignarTarea(baja.getId(), ana.getId());
        tareaService.asignarTarea(alta.getId(), ana.getId());
        tareaService.asignarTarea(moderada.getId(), ana.getId());

        List<Tarea> ordenadas = tareaService.tareasPorPrioridad(ana.getId());

        assertEquals(List.of(alta, moderada, baja), ordenadas);
    }

    @Test
    void tareasPorPrioridad_deUsuarioSinTareas_devuelveListaVacia() {
        assertTrue(tareaService.tareasPorPrioridad("sin-tareas").isEmpty());
    }

    @Test
    void tareasPorPrioridad_general_lasOrdenaDeAltaABaja() {
        Tarea baja = tareaService.crearTarea("Baja", "desc", Prioridad.BAJA);
        Tarea alta = tareaService.crearTarea("Alta", "desc", Prioridad.ALTA);
        Tarea moderada = tareaService.crearTarea("Moderada", "desc", Prioridad.MODERADA);

        List<Tarea> ordenadas = tareaService.tareasPorPrioridad();

        assertEquals(List.of(alta, moderada, baja), ordenadas);
    }

    @Test
    void tareasPorPrioridad_general_entreMismaPrioridad_desempataPorId() {
        Tarea primeraAlta = tareaService.crearTarea("Primera alta", "desc", Prioridad.ALTA);
        Tarea segundaAlta = tareaService.crearTarea("Segunda alta", "desc", Prioridad.ALTA);

        List<Tarea> ordenadas = tareaService.tareasPorPrioridad();

        assertEquals(List.of(primeraAlta, segundaAlta), ordenadas);
    }

    @Test
    void tareasPorPrioridad_general_sinTareas_devuelveListaVacia() {
        assertTrue(tareaService.tareasPorPrioridad().isEmpty());
    }

    @Test
    void asignarTarea_reasignarAOtroUsuario_dejaSoloAlNuevoResponsable() {
        Usuario ana = usuarioService.registrarUsuario("Ana");
        Usuario beto = usuarioService.registrarUsuario("Beto");
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.ALTA);

        tareaService.asignarTarea(tarea.getId(), ana.getId());
        tareaService.asignarTarea(tarea.getId(), beto.getId());

        assertEquals(beto.getId(), tarea.getResponsable().getId());
        assertTrue(tareaService.tareasPorPrioridad(ana.getId()).isEmpty());
        assertEquals(List.of(tarea), tareaService.tareasPorPrioridad(beto.getId()));
    }

    @Test
    void eliminarTarea_laSacaDelListadoYDeLasConsultas() {
        Usuario ana = usuarioService.registrarUsuario("Ana");
        Tarea tarea = tareaService.crearTarea("Tarea 1", "desc", Prioridad.ALTA);
        tareaService.asignarTarea(tarea.getId(), ana.getId());

        tareaService.eliminarTarea(tarea.getId());

        assertTrue(tareaService.listarTareas().isEmpty());
        assertTrue(tareaService.tareasPorPrioridad(ana.getId()).isEmpty());
        assertTrue(tareaService.tareasPorEstado().get(Estado.POR_REALIZAR).isEmpty());
    }

    @Test
    void eliminarTarea_noAfectaOtrasTareas() {
        Tarea aBorrar = tareaService.crearTarea("Borrar", "desc", Prioridad.BAJA);
        Tarea aConservar = tareaService.crearTarea("Conservar", "desc", Prioridad.ALTA);

        tareaService.eliminarTarea(aBorrar.getId());

        assertEquals(List.of(aConservar), tareaService.listarTareas());
    }

    @Test
    void eliminarTarea_conIdInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> tareaService.eliminarTarea("no-existe"));
    }
}
