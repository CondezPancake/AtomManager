package com.atommanager.repository;

import com.atommanager.model.Usuario;

/**
 * Contrato de acceso a datos de {@link Usuario}.
 *
 * <p>SOLID-D (inversión de dependencias): {@code UsuarioService} depende de
 * esta interfaz, no de una implementación concreta. SOLID-I (segregación de
 * interfaces): contrato específico para usuarios, separado de
 * {@code TareaRepository}.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code void guardar(Usuario usuario)}</li>
 *   <li>{@code Usuario buscarPorId(String id)}</li>
 *   <li>{@code List<Usuario> listarTodos()}</li>
 *   <li>{@code boolean existe(String id)}</li>
 * </ul>
 */
public interface UsuarioRepository {
}
