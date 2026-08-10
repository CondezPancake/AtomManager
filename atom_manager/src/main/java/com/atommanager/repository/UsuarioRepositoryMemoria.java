package com.atommanager.repository;

import com.atommanager.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación en memoria de {@link UsuarioRepository} con
 * {@code HashMap} como almacén principal (sección 4.1).
 */
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarios.get(id);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public boolean existe(String id) {
        return usuarios.containsKey(id);
    }

    @Override
    public void eliminar(String id) {
        usuarios.remove(id);
    }
}
