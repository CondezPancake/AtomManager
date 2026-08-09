package com.atommanager.model;

import java.util.Objects;

/**
 * Integrante del proyecto (HU-01, HU-02). Clase de datos: sin lógica de
 * negocio, solo encapsulamiento y validación de sus propios invariantes.
 */
public class Usuario {

    private final String id;
    private String nombre;

    public Usuario(String id, String nombre) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del usuario es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario otro)) {
            return false;
        }
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
