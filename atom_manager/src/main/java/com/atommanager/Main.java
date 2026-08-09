package com.atommanager;

/**
 * Punto de entrada de la aplicación AtomManager.
 *
 * <p>Es la única clase del proyecto que debe conocer las implementaciones
 * concretas del paquete {@code repository} (SOLID-D): arma el grafo de
 * dependencias e inicia la interfaz.</p>
 *
 * <p>Qué va acá:</p>
 * <ul>
 *   <li>Instanciar {@code UsuarioRepositoryMemoria} y {@code TareaRepositoryMemoria}.</li>
 *   <li>Inyectarlos en {@code UsuarioService} y {@code TareaService}.</li>
 *   <li>Instanciar y arrancar {@code MenuPrincipal} con esos services.</li>
 * </ul>
 *
 * <p>Qué NO va acá: reglas de negocio (van en {@code service}) ni código de
 * presentación (va en {@code ui}).</p>
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}