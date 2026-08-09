package com.atommanager.service;

import com.atommanager.repository.UsuarioRepository;

/**
 * Reglas de negocio para usuarios.
 *
 * <p>SOLID-D (inversión de dependencias): depende de la interfaz
 * {@link UsuarioRepository}; la implementación concreta se inyecta desde
 * {@code Main}. SOLID-S (responsabilidad única): la capa {@code ui} no
 * contiene esta lógica, solo la invoca.</p>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>{@code UsuarioRepository usuarioRepository} (inyectado por constructor).</li>
 *   <li>{@code registrarUsuario(String id, String nombre)} — valida ID único (RF-01, HU-01).</li>
 *   <li>{@code listarUsuarios()} (RF-02, HU-02).</li>
 * </ul>
 */
public class UsuarioService {
}
