/**
 * Acá viven las reglas del negocio: qué se puede hacer y qué no.
 *
 * <p>Por ejemplo: "no se puede registrar dos usuarios con el mismo id", "no
 * se puede asignar una tarea a alguien que no existe", "las tareas de un
 * usuario se muestran ordenadas de la más urgente a la menos urgente".</p>
 *
 * <p>Qué hay en esta carpeta:</p>
 * <ul>
 *   <li>{@code UsuarioService}: registrar y listar usuarios, validando que
 *   el id no esté repetido.</li>
 *   <li>{@code TareaService}: crear y asignar tareas, cambiar su prioridad,
 *   listarlas, y ordenarlas por prioridad (de más a menos urgente) usando
 *   una {@code PriorityQueue}.</li>
 * </ul>
 *
 * <p>Cómo funciona por dentro: cada clase de acá recibe, al crearse, el
 * repositorio que necesita (por ejemplo, {@code UsuarioService} recibe un
 * {@code UsuarioRepository}) — nunca crea uno por su cuenta. Así, si el día
 * de mañana cambia cómo se guardan los datos, esta carpeta no se entera ni
 * hay que tocarla.</p>
 *
 * <p>Qué NO va en esta carpeta: nada de pantallas ni de {@code JOptionPane}
 * — eso lo pide y lo muestra la carpeta {@code ui}; acá solo se deciden las
 * reglas.</p>
 */
package com.atommanager.service;
