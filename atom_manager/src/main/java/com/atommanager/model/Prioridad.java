package com.atommanager.model;

/**
 * Niveles de prioridad de las tareas (RF-07, RF-08).
 *
 * <p>Menor peso = mayor urgencia (usado por {@code PriorityQueue}):</p>
 * <ul>
 *   <li>{@code ALTA(1)} — 🔴 actividad crítica.</li>
 *   <li>{@code MODERADA(2)} — 🟡 actividad importante.</li>
 *   <li>{@code BAJA(3)} — 🟢 actividad menor.</li>
 * </ul>
 *
 * <p>Miembros a implementar:</p>
 * <ul>
 *   <li>Atributo {@code peso} (int), {@code etiqueta} (String) e {@code icono} (String).</li>
 *   <li>Constructor y getters ({@code getPeso()}, etc.).</li>
 * </ul>
 */
public enum Prioridad {
}