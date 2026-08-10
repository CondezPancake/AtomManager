package com.atommanager;

import com.atommanager.repository.TareaRepository;
import com.atommanager.repository.TareaRepositoryMemoria;
import com.atommanager.repository.UsuarioRepository;
import com.atommanager.repository.UsuarioRepositoryMemoria;
import com.atommanager.service.TareaService;
import com.atommanager.service.UsuarioService;
import com.atommanager.ui.MenuPrincipal;

/**
 * Punto de entrada de la aplicación AtomManager.
 *
 * <p>Fase actual: ventana principal con JavaFX (pestañas de "Usuarios" y
 * "Tareas"). Antes fue una ventana de Swing con FlatLaf, antes de eso un
 * menú con {@code JOptionPane}, y antes de eso un menú por terminal —
 * mismo comportamiento en las cuatro, solo cambió cómo se pide y se
 * muestra la información.</p>
 *
 * <p>Es la única clase del proyecto que instancia las implementaciones
 * concretas de {@code repository} (las que guardan en memoria) y las
 * conecta con los {@code service} correspondientes.</p>
 */
public class Main {

    public static void main(String[] args) {
        UsuarioRepository usuarioRepository = new UsuarioRepositoryMemoria();
        TareaRepository tareaRepository = new TareaRepositoryMemoria();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        TareaService tareaService = new TareaService(tareaRepository, usuarioRepository);

        new MenuPrincipal(usuarioService, tareaService).mostrar();
    }
}
