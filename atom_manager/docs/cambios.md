# AtomManager — Registro de cambios

> Documento de seguimiento del proyecto. Se actualiza constantemente con cada cambio realizado.

---

## Historial de cambios

### 2026-08-09 — Empaquetado del proyecto (esqueleto)

**Descripción:** Reorganización del proyecto según la estructura definida en `AtomManager2.1.md` (sección 3.1). Se deja todo listo para empezar a programar: solo estructura, sin lógica de negocio.

**Cambios realizados:**

1. **Movimiento de documentación**
   - Creada la carpeta `docs/` en el proyecto.
   - `AtomManager2.1.md` y `cambios.md` movidos desde `src/main/java/com/atommanager/` a `docs/`.
   - Motivo: evitar que Maven los copie como recursos a `target/classes/`.

2. **Estructura de paquetes creada** (esqueleto, sin código dentro)
   - `model/`: `Usuario`, `Tarea`, `Prioridad` (enum), `Estado` (enum).
   - `repository/`: `UsuarioRepository`, `TareaRepository` (interfaces); `UsuarioRepositoryMemoria`, `TareaRepositoryMemoria` (implementaciones con `HashMap`).
   - `service/`: `UsuarioService`, `TareaService` (reglas de negocio y colas `PriorityQueue`).
   - `ui/`: `MenuPrincipal`, `VistaUsuarios`, `VistaTareas` (JOptionPane / Swing).
   - `Main.java`: queda como punto de entrada vacío, listo para armar las dependencias e iniciar `MenuPrincipal`.

3. **Decisiones tomadas**
   - Nomenclatura según la sección 3.1 del documento (`UsuarioRepository`, `TareaService`, etc.), por ser la organización oficial de paquetes.
   - La sección 8 (`GestorUsuarios`, `GestorTareas`, `Repositorio<T>`, `RepositorioTareaCSV`) se descarta por ser inconsistente con la sección 3.1.
   - Persistencia (RF-18 / Épica 5, `RepositorioTareaCSV`) queda fuera del empaquetado actual: es opcional y se abordará al final.
   - Identificadores de usuarios y tareas como `String` (según sección 4.1, `Map<String, Usuario>` / `Map<String, Tarea>`).

4. **Limpieza y configuración**
   - Añadido `.gitignore` con `target/` para no versionar artefactos de compilación.
   - Eliminadas las copias obsoletas de los `.md` en `target/classes/`.
   - Verificada la compilación del esqueleto con `javac` (sin errores).

**Próximos pasos (pendientes):**
- Implementar `model` (atributos, getters/setters, enums con peso/etiqueta/ícono).
- Implementar interfaces de repositorio y sus implementaciones en memoria.
- Implementar servicios con reglas de negocio y `PriorityQueue`.
- Implementar la capa de interfaz `ui`.
- Completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Validación de estructura y paquetes faltantes

**Descripción:** Revisión del empaquetado real del proyecto (filesystem) frente a lo registrado en la entrada anterior y frente a la sección 3.1 de `AtomManager2.1.md`, aplicando criterios de POO y SOLID. Se detectaron discrepancias entre este documento y el estado real del código, y se completó el esqueleto de paquetes.

**Hallazgos de la revisión:**
- La entrada anterior indicaba que los paquetes `repository`, `service` y `ui` ya existían como esqueleto. En el filesystem **solo existía `model/`**; los otros tres paquetes no se habían creado.
- `model/` ya contenía `Usuario.java`, `Tarea.java` y `Prioridad.java` (stubs con Javadoc, sin miembros aún) por trabajo en paralelo no reflejado todavía en este documento. Faltaba `Estado.java`, ya referenciado desde el Javadoc de `Tarea` (`{@link Estado}`), lo que dejaba una dependencia sin resolver.
- No se detectaron violaciones de SOLID en el código existente porque, al ser stubs vacíos, aún no hay lógica que evaluar; la nomenclatura y el Javadoc sí son consistentes con la sección 3.1/3.2 del documento de diseño.

**Cambios realizados:**
1. Creado `model/Estado.java` (enum stub: `POR_REALIZAR`, `EN_PROCESO`, `FINALIZADA`), completando el paquete `model` (sección 3.1 y 11).
2. Creado el paquete `repository/` con stubs: `UsuarioRepository`, `TareaRepository` (interfaces, SOLID-I/D) y `UsuarioRepositoryMemoria`, `TareaRepositoryMemoria` (implementaciones en memoria con `HashMap`, SOLID-L).
3. Creado el paquete `service/` con stubs: `UsuarioService`, `TareaService` (dependen de las interfaces de `repository`, SOLID-D).
4. Creado el paquete `ui/` con stubs: `MenuPrincipal`, `VistaUsuarios`, `VistaTareas` (sin lógica de negocio, SOLID-S).
5. Verificada la compilación del esqueleto completo con `javac` (sin errores).

**Nota para coordinación entre agentes:** este documento es la fuente de verdad compartida del avance del proyecto. Como hay más de un agente trabajando en paralelo sobre el mismo código, antes de asumir el estado del proyecto a partir de una entrada previa hay que verificar el filesystem directamente, ya que puede haber cambios recientes aún no volcados aquí.

**Próximos pasos (pendientes):**
- Implementar atributos, constructores, getters/setters y validaciones en `model` (`Usuario`, `Tarea`, `Prioridad`, `Estado`).
- Implementar los métodos de `UsuarioRepository` / `TareaRepository` y sus implementaciones en memoria.
- Implementar la lógica de negocio en `UsuarioService` / `TareaService`, incluyendo `PriorityQueue`.
- Implementar la capa `ui` (Swing / JOptionPane).
- Completar `Main.java` (inyección de dependencias e inicio de `MenuPrincipal`).