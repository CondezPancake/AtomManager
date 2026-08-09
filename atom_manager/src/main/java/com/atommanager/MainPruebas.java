package com.atommanager;

import com.atommanager.model.Prioridad;
import com.atommanager.model.Tarea;
import com.atommanager.model.Usuario;
import com.atommanager.repository.TareaRepository;
import com.atommanager.repository.TareaRepositoryMemoria;
import com.atommanager.repository.UsuarioRepository;
import com.atommanager.repository.UsuarioRepositoryMemoria;
import com.atommanager.service.TareaService;
import com.atommanager.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

/**
 * Copia de {@link Main} para probar a mano por terminal, corriéndola vos
 * mismo desde el IDE (clic derecho → Run) o con
 * {@code mvn exec:java -Dexec.mainClass="com.atommanager.MainPruebas"}.
 *
 * <p>Es un archivo temporal: existe solo para no arriesgar {@code Main.java}
 * mientras se prueban funcionalidades. Se borra cuando se termine de probar
 * la fase de terminal.</p>
 */
public class MainPruebas {

    public static void main(String[] args) {
        UsuarioRepository usuarioRepository = new UsuarioRepositoryMemoria();
        TareaRepository tareaRepository = new TareaRepositoryMemoria();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        TareaService tareaService = new TareaService(tareaRepository, usuarioRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerOpcion(scanner);
                ejecutar(opcion, scanner, usuarioService, tareaService);
            } while (opcion != 0);
        }

        System.out.println("Listo, ¡hasta la próxima!");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== AtomManager (PRUEBAS) =====");
        System.out.println("1. Registrar usuario");
        System.out.println("2. Consultar usuarios");
        System.out.println("3. Crear tarea");
        System.out.println("4. Asignar tarea a un usuario");
        System.out.println("5. Cambiar la prioridad de una tarea");
        System.out.println("6. Consultar todas las tareas");
        System.out.println("7. Consultar las tareas de un usuario (por prioridad)");
        System.out.println("0. Salir");
        System.out.print("Elegí una opción: ");
    }

    private static int leerOpcion(Scanner scanner) {
        String linea = scanner.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void ejecutar(int opcion, Scanner scanner, UsuarioService usuarioService, TareaService tareaService) {
        try {
            switch (opcion) {
                case 1 -> registrarUsuario(scanner, usuarioService);
                case 2 -> consultarUsuarios(usuarioService);
                case 3 -> crearTarea(scanner, tareaService);
                case 4 -> asignarTarea(scanner, tareaService);
                case 5 -> cambiarPrioridad(scanner, tareaService);
                case 6 -> consultarTareas(tareaService);
                case 7 -> consultarTareasPorUsuario(scanner, tareaService);
                case 0 -> System.out.println("Cerrando el programa...");
                default -> System.out.println("Esa opción no existe, probá de nuevo.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarUsuario(Scanner scanner, UsuarioService usuarioService) {
        System.out.print("ID del usuario: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre del usuario: ");
        String nombre = scanner.nextLine().trim();

        Usuario usuario = usuarioService.registrarUsuario(id, nombre);
        System.out.println("Usuario registrado: " + usuario);
    }

    private static void consultarUsuarios(UsuarioService usuarioService) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Todavía no hay usuarios registrados.");
            return;
        }
        System.out.println("Usuarios registrados:");
        for (Usuario usuario : usuarios) {
            System.out.println("  " + usuario);
        }
    }

    private static void crearTarea(Scanner scanner, TareaService tareaService) {
        System.out.print("Título de la tarea: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();
        Prioridad prioridad = pedirPrioridad(scanner);

        Tarea tarea = tareaService.crearTarea(titulo, descripcion, prioridad);
        System.out.println("Tarea creada: " + tarea);
    }

    private static void asignarTarea(Scanner scanner, TareaService tareaService) {
        System.out.print("ID de la tarea: ");
        String tareaId = scanner.nextLine().trim();
        System.out.print("ID del usuario responsable: ");
        String usuarioId = scanner.nextLine().trim();

        tareaService.asignarTarea(tareaId, usuarioId);
        System.out.println("Tarea asignada correctamente.");
    }

    private static void cambiarPrioridad(Scanner scanner, TareaService tareaService) {
        System.out.print("ID de la tarea: ");
        String tareaId = scanner.nextLine().trim();
        Prioridad prioridad = pedirPrioridad(scanner);

        tareaService.cambiarPrioridad(tareaId, prioridad);
        System.out.println("Prioridad actualizada.");
    }

    private static void consultarTareas(TareaService tareaService) {
        List<Tarea> tareas = tareaService.listarTareas();
        if (tareas.isEmpty()) {
            System.out.println("Todavía no hay tareas creadas.");
            return;
        }
        System.out.println("Todas las tareas:");
        for (Tarea tarea : tareas) {
            System.out.println("  " + tarea);
        }
    }

    private static void consultarTareasPorUsuario(Scanner scanner, TareaService tareaService) {
        System.out.print("ID del usuario: ");
        String usuarioId = scanner.nextLine().trim();

        List<Tarea> tareas = tareaService.tareasPorPrioridad(usuarioId);
        if (tareas.isEmpty()) {
            System.out.println("Ese usuario no tiene tareas asignadas.");
            return;
        }
        System.out.println("Tareas de " + usuarioId + ", de más a menos urgente:");
        for (Tarea tarea : tareas) {
            System.out.println("  " + tarea);
        }
    }

    private static Prioridad pedirPrioridad(Scanner scanner) {
        System.out.println("Prioridad: 1) Alta  2) Moderada  3) Baja");
        System.out.print("Elegí una opción: ");
        String opcion = scanner.nextLine().trim();
        return switch (opcion) {
            case "1" -> Prioridad.ALTA;
            case "2" -> Prioridad.MODERADA;
            case "3" -> Prioridad.BAJA;
            default -> throw new IllegalArgumentException("Opción de prioridad inválida: " + opcion);
        };
    }
}
