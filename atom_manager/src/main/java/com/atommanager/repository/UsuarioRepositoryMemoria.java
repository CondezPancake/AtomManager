package com.atommanager.repository;

import com.atommanager.model.Usuario;

/**
 * Implementación en memoria de {@link UsuarioRepository} usando un
 * {@code HashMap} como almacén principal (sección 4.1).
 *
 * <p>SOLID-L (sustitución de Liskov): cualquier otra implementación de
 * {@code UsuarioRepository} (por ejemplo, un futuro repositorio en archivo)
 * puede reemplazar a esta sin romper {@code UsuarioService}.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code Map<String, Usuario> usuarios = new HashMap<>();}</li>
 * </ul>
 */
public class UsuarioRepositoryMemoria implements UsuarioRepository {
}
