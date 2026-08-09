package com.atommanager.repository;

import com.atommanager.model.Usuario;

import java.util.List;

/**
 * Contrato de acceso a datos de {@link Usuario} (HU-01, HU-02).
 * SOLID-D: {@code UsuarioService} depende de esta interfaz, no de la
 * implementación concreta.
 */
public interface UsuarioRepository {

    void guardar(Usuario usuario);

    Usuario buscarPorId(String id);

    List<Usuario> listarTodos();

    boolean existe(String id);
}
