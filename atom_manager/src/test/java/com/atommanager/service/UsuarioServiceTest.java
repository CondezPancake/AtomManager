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
}
