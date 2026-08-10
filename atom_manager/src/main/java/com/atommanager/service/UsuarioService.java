package com.atommanager.service;

import com.atommanager.model.Usuario;
import com.atommanager.repository.UsuarioRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reglas de negocio de la Épica 1 (HU-01, HU-02). Recibe su repositorio
 * por constructor (SOLID-D): nunca instancia {@code UsuarioRepositoryMemoria}
 * directamente.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AtomicInteger contadorId = new AtomicInteger(0);

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** HU-01: registra un usuario con id único autogenerado (mismo esquema que {@code TareaService.crearTarea}). */
    public Usuario registrarUsuario(String nombre) {
        String id = "U-" + contadorId.incrementAndGet();
        Usuario usuario = new Usuario(id, nombre);
        usuarioRepository.guardar(usuario);
        return usuario;
    }

    /** HU-02: lista todos los usuarios registrados. */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }
}
