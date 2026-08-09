package com.atommanager.service;

import com.atommanager.model.Usuario;
import com.atommanager.repository.UsuarioRepository;
import com.atommanager.repository.UsuarioRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void registrarUsuario_loGuardaConIdUnico() {
        Usuario usuario = usuarioService.registrarUsuario("u1", "Ana");

        assertEquals("u1", usuario.getId());
        assertEquals("Ana", usuario.getNombre());
        assertTrue(usuarioRepository.existe("u1"));
    }

    @Test
    void registrarUsuario_conIdDuplicado_lanzaExcepcion() {
        usuarioService.registrarUsuario("u1", "Ana");

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario("u1", "Otro"));
    }

    @Test
    void listarUsuarios_devuelveTodosLosRegistrados() {
        usuarioService.registrarUsuario("u1", "Ana");
        usuarioService.registrarUsuario("u2", "Beto");

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertEquals(2, usuarios.size());
    }

    @Test
    void listarUsuarios_sinRegistrosDevuelveListaVacia() {
        assertTrue(usuarioService.listarUsuarios().isEmpty());
    }
}
