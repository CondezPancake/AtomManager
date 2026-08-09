package com.atommanager.repository;

import com.atommanager.model.Tarea;

/**
 * Contrato de acceso a datos de {@link Tarea}.
 *
 * <p>SOLID-D (inversión de dependencias): {@code TareaService} depende de
 * esta interfaz, no de una implementación concreta. SOLID-I (segregación de
 * interfaces): contrato específico para tareas, separado de
 * {@code UsuarioRepository}.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code void guardar(Tarea tarea)}</li>
 *   <li>{@code Tarea buscarPorId(String id)}</li>
 *   <li>{@code List<Tarea> listarTodas()}</li>
 *   <li>{@code List<Tarea> buscarPorUsuario(String usuarioId)}</li>
 *   <li>{@code List<Tarea> buscarPorEstado(Estado estado)}</li>
 * </ul>
 */
public interface TareaRepository {
}
