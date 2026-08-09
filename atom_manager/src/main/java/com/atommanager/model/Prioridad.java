package com.atommanager.model;

/**
 * Niveles de prioridad de una tarea (HU-06, RF-07, RF-08).
 *
 * <p>Menor peso = mayor urgencia; el peso es el que usa
 * {@code TareaService} para ordenar la {@code PriorityQueue} (HU-03).</p>
 */
public enum Prioridad {

    ALTA(1, "Alta", "🔴"),
    MODERADA(2, "Moderada", "🟡"),
    BAJA(3, "Baja", "🟢");

    private final int peso;
    private final String etiqueta;
    private final String icono;

    Prioridad(int peso, String etiqueta, String icono) {
        this.peso = peso;
        this.etiqueta = etiqueta;
        this.icono = icono;
    }

    public int getPeso() {
        return peso;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getIcono() {
        return icono;
    }

    @Override
    public String toString() {
        return icono + " " + etiqueta;
    }
}
