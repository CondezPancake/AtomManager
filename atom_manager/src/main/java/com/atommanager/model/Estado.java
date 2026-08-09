package com.atommanager.model;

/**
 * Ciclo de vida de una tarea (Épica 3, HU-08).
 *
 * <p>Toda {@link Tarea} necesita un estado válido desde que se crea, por
 * eso el enum ya existe: nace en {@code POR_REALIZAR} (ver constructor de
 * {@code Tarea}). HU-08 permite cambiarlo; la consulta agrupada de HU-09
 * todavía no está implementada.</p>
 */
public enum Estado {

    POR_REALIZAR("Por realizar"),
    EN_PROCESO("En proceso"),
    FINALIZADA("Finalizada");

    private final String etiqueta;

    Estado(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
