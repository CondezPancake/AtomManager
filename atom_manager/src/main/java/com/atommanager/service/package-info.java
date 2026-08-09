/**
 * Reglas de negocio de AtomManager: validaciones, asignación de tareas,
 * cambios de prioridad/estado y ordenamiento por prioridad con
 * {@code PriorityQueue} (sección 4.2 del documento de diseño).
 *
 * <p>Qué va acá:</p>
 * <ul>
 *   <li>{@code UsuarioService} — registrar/listar usuarios, validar ID único.</li>
 *   <li>{@code TareaService} — crear/asignar tareas, cambiar prioridad/estado, construir la cola de prioridad.</li>
 * </ul>
 *
 * <p>Reglas de diseño:</p>
 * <ul>
 *   <li>Cada service recibe su(s) repositorio(s) por constructor (inyección de dependencias, SOLID-D); nunca instancia {@code UsuarioRepositoryMemoria} / {@code TareaRepositoryMemoria} directamente.</li>
 *   <li>Acá vive toda la validación de reglas del dominio (ids únicos, campos obligatorios, existencia de usuario/tarea antes de operar).</li>
 *   <li>Sin código de Swing/JOptionPane: la capa {@code ui} es la única que sabe cómo se presenta la información.</li>
 * </ul>
 */
package com.atommanager.service;
