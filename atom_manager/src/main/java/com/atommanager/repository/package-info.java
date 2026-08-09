/**
 * Acceso a datos de AtomManager: interfaces de repositorio y su
 * implementación en memoria con {@code HashMap} (sección 4.1 del documento
 * de diseño).
 *
 * <p>Qué va acá:</p>
 * <ul>
 *   <li>{@code UsuarioRepository} / {@code TareaRepository} — interfaces (contrato de acceso a datos).</li>
 *   <li>{@code UsuarioRepositoryMemoria} / {@code TareaRepositoryMemoria} — implementaciones en memoria con {@code HashMap}.</li>
 * </ul>
 *
 * <p>Reglas de diseño:</p>
 * <ul>
 *   <li>El paquete {@code service} depende siempre de la interfaz, nunca de la implementación concreta (SOLID-D).</li>
 *   <li>Una futura implementación (archivo, base de datos) debe poder reemplazar a la de memoria sin romper nada (SOLID-L).</li>
 *   <li>Sin lógica de negocio ni validaciones de reglas del dominio: solo guardar / buscar / listar.</li>
 * </ul>
 */
package com.atommanager.repository;
