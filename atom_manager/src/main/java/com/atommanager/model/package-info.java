/**
 * Acá van las "fichas" de datos del sistema: usuarios, tareas, prioridades y estados.
 *
 * <p>Estas clases solo guardan información y se fijan que tenga sentido (por
 * ejemplo, que una tarea no se pueda crear sin título). No saben nada de cómo
 * se guarda esa información en la memoria, ni de las reglas del negocio, ni
 * de las pantallas.</p>
 *
 * <p>Qué hay en esta carpeta:</p>
 * <ul>
 *   <li>{@code Usuario}: una persona del proyecto (tiene un id y un nombre).</li>
 *   <li>{@code Tarea}: una actividad del proyecto (tiene un id, un título, una
 *   descripción, una prioridad, un estado y, si ya se asignó, el usuario
 *   responsable).</li>
 *   <li>{@code Prioridad}: qué tan urgente es una tarea (Alta, Moderada o Baja).</li>
 *   <li>{@code Estado}: en qué punto está una tarea (Por realizar, En proceso
 *   o Finalizada).</li>
 * </ul>
 *
 * <p>Qué NO va en esta carpeta: nada que guarde información en un
 * {@code HashMap} o en un archivo, nada de pantallas, y nada que decida si
 * una acción "se puede hacer" o no — eso va en la carpeta {@code service}.</p>
 */
package com.atommanager.model;
