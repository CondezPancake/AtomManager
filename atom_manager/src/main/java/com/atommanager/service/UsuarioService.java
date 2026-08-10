package com.atommanager.service;

import com.atommanager.model.Usuario;
import com.atommanager.repository.UsuarioRepository;

import java.util.Comparator;
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

    /** HU-01: registra un usuario con id numérico autogenerado ("01", "02"...), sin duplicados ni saltos. */
    public Usuario registrarUsuario(String nombre) {
        String id = GeneradorId.siguiente(usuarioRepository.listarTodos().stream().map(Usuario::getId).toList());
        Usuario usuario = new Usuario(id, nombre);
        usuarioRepository.guardar(usuario);
        return usuario;
    }

    /** HU-02: lista todos los usuarios registrados, ordenados por id. */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos().stream()
                .sorted(Comparator.comparingInt(u -> Integer.parseInt(u.getId())))
                .toList();
    }

    /** Elimina un usuario existente; valida que exista antes de borrarlo. */
    public void eliminarUsuario(String usuarioId) {
        if (!usuarioRepository.existe(usuarioId)) {
            throw new IllegalArgumentException("No existe un usuario con el id \"" + usuarioId + "\".");
        }
        usuarioRepository.eliminar(usuarioId);
    }
}
