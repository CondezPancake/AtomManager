package com.atommanager.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Integrante del proyecto (HU-01, HU-02). Clase de datos: sin lógica de
 * negocio, solo encapsulamiento y validación de sus propios invariantes.
 */
public class Usuario {

    private static final Pattern ID_VALIDO = Pattern.compile("^\\d+$");
    private static final Pattern NOMBRE_VALIDO = Pattern.compile("^[\\p{L} '-]+$");

    private final String id;
    private String nombre;

    public Usuario(String id, String nombre) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del usuario es obligatorio.");
        }
        if (!ID_VALIDO.matcher(id).matches()) {
            throw new IllegalArgumentException(
                    "El id del usuario no tiene un formato válido (esperado solo números, por ejemplo \"01\").");
        }
        validarNombre(nombre);
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
        validarNombre(nombre);
        this.nombre = nombre;
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (!NOMBRE_VALIDO.matcher(nombre).matches()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario solo puede tener letras, espacios, guiones y apóstrofos (sin números ni otros símbolos).");
        }
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
