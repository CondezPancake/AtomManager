package com.atommanager.model;

import java.util.Objects;

/**
 * Actividad del proyecto (HU-04 a HU-08). Clase de datos: sin lógica de
 * negocio (eso vive en {@code TareaService}), solo encapsulamiento y
 * validación de sus propios invariantes. Composición: referencia al
 * {@link Usuario} responsable.
 *
 * <p>La tarea nace en {@code Estado.POR_REALIZAR}; su estado puede cambiarse
 * mediante {@code setEstado(...)} para cumplir HU-08 (Épica 3).</p>
 */
public class Tarea {

    private final String id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;
    private Usuario responsable;

    public Tarea(String id, String titulo, String descripcion, Prioridad prioridad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la tarea es obligatorio.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio.");
        }
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad != null ? prioridad : Prioridad.BAJA;
        this.estado = Estado.POR_REALIZAR;
        this.responsable = null;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio.");
        }
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        }
        this.prioridad = prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tarea otra)) {
            return false;
        }
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String resp = responsable != null ? responsable.getNombre() : "sin asignar";
        return "[" + id + "] " + titulo + " (" + prioridad + ") - responsable: " + resp;
    }
}
