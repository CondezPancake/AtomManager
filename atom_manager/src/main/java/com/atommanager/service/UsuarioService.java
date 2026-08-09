package com.atommanager.service;

import com.atommanager.model.Usuario;
import com.atommanager.repository.UsuarioRepository;

import java.util.List;

/**
 * Reglas de negocio de la Épica 1 (HU-01, HU-02). Recibe su repositorio
 * por constructor (SOLID-D): nunca instancia {@code UsuarioRepositoryMemoria}
 * directamente.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** HU-01: registra un usuario; el id debe ser único. */
    public Usuario registrarUsuario(String id, String nombre) {
        if (usuarioRepository.existe(id)) {
            throw new IllegalArgumentException("Ya existe un usuario con el id \"" + id + "\".");
        }
        Usuario usuario = new Usuario(id, nombre);
        usuarioRepository.guardar(usuario);
        return usuario;
    }

    /** HU-02: lista todos los usuarios registrados. */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }
}
