package com.atommanager.service;

import com.atommanager.model.Usuario;
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
 * Épica 1 — Gestión de usuarios (HU-01, HU-02).
 */
class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryMemoria();
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void registrarUsuario_generaIdUnicoAutomaticamente() {
        Usuario u1 = usuarioService.registrarUsuario("Ana");
        Usuario u2 = usuarioService.registrarUsuario("Beto");

        assertNotNull(u1.getId());
        assertNotNull(u2.getId());
        assertNotEquals(u1.getId(), u2.getId());
        assertTrue(usuarioRepository.existe(u1.getId()));
    }

    @Test
    void registrarUsuario_generaIdsConFormatoNumericoConsecutivo() {
        Usuario u1 = usuarioService.registrarUsuario("Ana");
        Usuario u2 = usuarioService.registrarUsuario("Beto");
        Usuario u3 = usuarioService.registrarUsuario("Cami");

        assertEquals("01", u1.getId());
        assertEquals("02", u2.getId());
        assertEquals("03", u3.getId());
    }

    @Test
    void registrarUsuario_conIdManualLetrasOSimbolos_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario("U-1", "Ana"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario("abc", "Ana"));
    }

    @Test
    void eliminarUsuario_despuesDeBorrarElUltimo_elSiguienteIdReocupaEseLugar() {
        usuarioService.registrarUsuario("Ana");
        Usuario u2 = usuarioService.registrarUsuario("Beto");

        usuarioService.eliminarUsuario(u2.getId());
        Usuario u3 = usuarioService.registrarUsuario("Cami");

        assertEquals("02", u3.getId());
    }

    @Test
    void eliminarUsuario_delMedio_noProvocaSaltosArtificialesNiDuplicados() {
        usuarioService.registrarUsuario("Ana");
        Usuario u2 = usuarioService.registrarUsuario("Beto");
        Usuario u3 = usuarioService.registrarUsuario("Cami");

        usuarioService.eliminarUsuario(u2.getId());
        Usuario u4 = usuarioService.registrarUsuario("Dani");

        assertEquals("04", u4.getId());
        assertNotEquals(u3.getId(), u4.getId());
    }

    @Test
    void registrarUsuario_conNombreValido_loGuarda() {
        Usuario usuario = usuarioService.registrarUsuario("Ana María");

        assertEquals("Ana María", usuario.getNombre());
    }

    @Test
    void registrarUsuario_conNumerosEnElNombre_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario("Ana123"));
    }

    @Test
    void registrarUsuario_conSimbolosInvalidosEnElNombre_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario("Ana@Beto"));
    }

    @Test
    void registrarUsuario_sinNombre_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario("  "));
    }

    @Test
    void listarUsuarios_devuelveTodosLosRegistrados() {
        usuarioService.registrarUsuario("Ana");
        usuarioService.registrarUsuario("Beto");

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertEquals(2, usuarios.size());
    }

    @Test
    void listarUsuarios_sinRegistrosDevuelveListaVacia() {
        assertTrue(usuarioService.listarUsuarios().isEmpty());
    }

    @Test
    void listarUsuarios_losDevuelveOrdenadosPorId() {
        Usuario ana = usuarioService.registrarUsuario("Ana");
        Usuario beto = usuarioService.registrarUsuario("Beto");
        Usuario cami = usuarioService.registrarUsuario("Cami");

        usuarioService.eliminarUsuario(beto.getId());
        Usuario dani = usuarioService.registrarUsuario("Dani");

        assertEquals(List.of(ana, cami, dani), usuarioService.listarUsuarios());
    }

    @Test
    void eliminarUsuario_loSacaDelListadoYNoReapareceAlConsultar() {
        Usuario ana = usuarioService.registrarUsuario("Ana");
        usuarioService.registrarUsuario("Beto");

        usuarioService.eliminarUsuario(ana.getId());

        assertEquals(1, usuarioService.listarUsuarios().size());
        assertTrue(usuarioService.listarUsuarios().stream().noneMatch(u -> u.getId().equals(ana.getId())));
        // se consulta de nuevo para confirmar que no "reaparece" en un refresco posterior
        assertTrue(usuarioService.listarUsuarios().stream().noneMatch(u -> u.getId().equals(ana.getId())));
    }

    @Test
    void eliminarUsuario_conIdInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.eliminarUsuario("no-existe"));
    }
}
