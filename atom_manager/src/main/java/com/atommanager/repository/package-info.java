/**
 * Acá vive la parte que guarda y busca la información: hoy, todo se guarda
 * en la memoria de la computadora (con {@code HashMap}), no en un archivo ni
 * en una base de datos.
 *
 * <p>Qué hay en esta carpeta:</p>
 * <ul>
 *   <li>{@code UsuarioRepository} / {@code TareaRepository}: son como un
 *   "contrato" que dice qué se puede hacer con los datos (guardar, buscar,
 *   listar), sin decir cómo se hace por dentro.</li>
 *   <li>{@code UsuarioRepositoryMemoria} / {@code TareaRepositoryMemoria}:
 *   son quienes realmente cumplen ese contrato, guardando todo en la
 *   memoria mientras el programa está abierto.</li>
 * </ul>
 *
 * <p>Por qué está separado en "contrato" y "quien lo cumple": para que, el
 * día de mañana, si se quiere guardar la información en un archivo en vez
 * de en la memoria, alcance con crear una clase nueva que cumpla el mismo
 * contrato, sin tener que tocar el resto del programa.</p>
 *
 * <p>Qué NO va en esta carpeta: reglas del negocio. Por ejemplo, "no se
 * puede crear una tarea sin título" no se valida acá — eso ya viene resuelto
 * antes de llegar. Esta carpeta solo guarda y busca, no decide nada.</p>
 */
package com.atommanager.repository;
