package com.atommanager.service;

import java.util.List;

/**
 * Genera IDs numéricos consecutivos ("01", "02", "03"...) a partir de los
 * IDs ya existentes, en vez de un contador aparte que se puede desincronizar
 * de los datos reales (por ejemplo, después de eliminar registros).
 *
 * <p>Se usa igual para usuarios y para tareas ({@link UsuarioService} y
 * {@link TareaService}), así ambos siguen la misma lógica.</p>
 */
final class GeneradorId {

    private GeneradorId() {
    }

    /**
     * Calcula el siguiente id como "el mayor id existente + 1", formateado
     * con al menos 2 dígitos ("01".."09", "10", "11"...). Al recalcularse
     * siempre a partir de los ids que existen en ese momento (no de un
     * contador aparte que se pueda desincronizar), nunca repite un id ni
     * salta números que nunca se usaron. Si se borra el registro con el id
     * más alto, el próximo id generado vuelve a ocupar ese lugar; si se
     * borra uno del medio, ese hueco queda libre (como en cualquier
     * numeración autoincremental), pero no se generan saltos artificiales.
     */
    static String siguiente(List<String> idsExistentes) {
        int maximo = idsExistentes.stream()
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return String.format("%02d", maximo + 1);
    }
}
