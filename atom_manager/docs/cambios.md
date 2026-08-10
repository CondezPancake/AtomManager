# AtomManager — Registro de cambios

> Documento de seguimiento del proyecto. Se actualiza constantemente con cada cambio realizado.

---

## Historial de cambios

### 2026-08-09 — Reescritura de la interfaz en JavaFX

**Descripción:** Después de ver la ventana de Swing+FlatLaf, el usuario la siguió encontrando anticuada y pidió priorizar la experiencia de usuario, con JavaFX si hacía falta. Se le preguntó explícitamente si prefería quedarse en Swing (con retoques) o pasar a JavaFX (reescritura completa, fuera del alcance original de `AtomManager2.1.md`); eligió JavaFX. Esto reemplaza — no convive con — la versión de Swing/FlatLaf, siguiendo la misma regla de siempre ("un solo punto de entrada, sin versiones viejas dando vueltas").

**Cambios realizados:**
1. `pom.xml`: se sacó la dependencia de FlatLaf. Se agregó `javafx-controls:17.0.20` (misma versión mayor que el Java del proyecto) y 3 perfiles de Maven (`javafx-linux`, `javafx-mac`, `javafx-windows`) que detectan el sistema operativo de quien compila y arman la variante del `.jar` que corresponde — así el proyecto sigue funcionando clonado en cualquier sistema, sin tocar el `pom.xml` a mano.
2. `src/main/resources/atommanager.css`: hoja de estilos con colores, tipografía y botones; carpeta `resources` nueva en el proyecto (convención estándar de Maven para este tipo de archivos).
3. `ui/MenuPrincipal.java` reescrito: ya no extiende `JFrame`. Arranca JavaFX con `Platform.startup(...)` (en vez de extender `Application`, que exige un constructor vacío) y arma una ventana con `TabPane`: una pestaña "Usuarios" y una pestaña "Tareas". Sigue recibiendo `UsuarioService`/`TareaService` por constructor, igual que en todas las versiones anteriores.
4. `ui/VistaUsuarios.java` reescrito: la pestaña de usuarios pasó de una cadena de cuadros de diálogo a un formulario fijo (ID, Nombre, botón) + una `TableView` con todos los usuarios, que se refresca sola después de cada alta.
5. `ui/VistaTareas.java` reescrito: la pestaña de tareas tiene un formulario de alta, una `TableView` con todas las tareas (columnas ID, Título, Prioridad, Estado, Responsable), un panel de acciones que opera sobre la fila seleccionada (asignar, cambiar prioridad, cambiar estado) y un panel de consultas (por usuario, tablero por estado, todas por prioridad) que abre un cuadro con el resultado.
6. `ui/Dialogos.java` (nuevo, package-private): agrupa el manejo de errores (`IllegalArgumentException` → alerta) y el cuadro de listados largos, para no repetir ese código en las dos vistas.
7. `Main.java`: se sacó `FlatLightLaf.setup()`; el resto no cambió — sigue armando los repositorios/services e invocando `new MenuPrincipal(...).mostrar()`.
8. `docs/AtomManager2.1.md`: se actualizó la sección 1 (con una nota explicando la decisión) y la sección 9 (JavaFX ya no figura en "fuera de alcance"; la tecnología de interfaz en 9.1 pasa a ser JavaFX). El resto del documento (épicas, requisitos, estructuras de datos) no se tocó.
9. `README.md`: actualizado el estado del proyecto, la descripción, la tabla de tecnologías, el árbol de arquitectura, la tabla del paquete `ui`, el alcance y el árbol de "Estructura del repositorio" (agrega `resources/`, `Dialogos.java`, saca las etiquetas viejas de Swing/JOptionPane). De paso se corrigió una línea duplicada que había quedado en la sección de Documentación.
10. `docs/GUIA-PROXIMO-DESARROLLADOR.md`: actualizada — la sección "qué falta" ya no incluye las pantallas (están hechas), solo queda la persistencia opcional y, como ítem opcional nuevo, pulir más la interfaz si se quiere.

**Verificación:** `mvn clean compile` y `mvn test` — compila sin errores y las 20 pruebas de `service` siguen pasando (no dependen de la interfaz). La interfaz gráfica en sí no se puede probar de forma automática desde este entorno; queda a cargo del usuario correrla y probarla a mano, como en las etapas anteriores.

**Decisiones de diseño:**
- Se usó `Platform.startup(Runnable)` en vez de extender `javafx.application.Application`, específicamente para poder seguir inyectando `UsuarioService`/`TareaService` por constructor en `MenuPrincipal` — `Application` exige que el framework cree la instancia por reflexión con un constructor vacío, lo que hubiera roto el patrón de inyección de dependencias usado en todo el proyecto.
- Las consultas especiales (por usuario, por estado, por prioridad general) siguen llamando explícitamente a los métodos de `TareaService` que usan `PriorityQueue`/`Map<Estado, List<Tarea>>`, en vez de ordenar la tabla del lado de la ventana — así la interfaz sigue demostrando el uso real de esas estructuras de datos (requisito del enunciado), no solo un efecto visual parecido.

**Próximos pasos (pendientes):**
- Persistencia opcional (Épica 5), solo si se quiere ir más allá del alcance actual.
- (Opcional) seguir puliendo la interfaz de JavaFX si se desea.

---

### 2026-08-09 — FlatLaf para mejorar la apariencia de la ventana

**Descripción:** El usuario probó la ventana de Swing (`JFrame`) y la encontró fea — es el look & feel por defecto de Swing, conocido por eso. Se agregó FlatLaf, que es justo la mejora de apariencia "de bajo costo" que ya preveía la sección 1 y la RNF-02 de `AtomManager2.1.md` ("opcionalmente puede usarse FlatLaf"). No hizo falta salir del alcance del documento de diseño para esto.

**Cambios realizados:**
1. `pom.xml`: agregada la dependencia `com.formdev:flatlaf:3.7.2`.
2. `Main.java`: se agregó `FlatLightLaf.setup();` como primera línea de `main(...)`, antes de crear cualquier ventana.
3. No se tocó `ui/MenuPrincipal.java`, `VistaUsuarios.java` ni `VistaTareas.java`: FlatLaf cambia la apariencia de los componentes de Swing existentes (botones, diálogos, fuentes, colores) sin que el código que los crea tenga que saber nada al respecto.
4. `README.md`: actualizada la descripción y la tabla de tecnologías (FlatLaf pasa de "opcional" a "en uso").

**Verificación:** `mvn clean test` — `BUILD SUCCESS`, 20/20 pruebas (FlatLaf no afecta la lógica de negocio, solo la apariencia).

**Decisión sobre JavaFX:** antes de este cambio se le preguntó al usuario si para mejorar la apariencia prefería FlatLaf (bajo costo, ya previsto en el documento) o reescribir toda la interfaz en JavaFX (fuera de alcance, requiere módulos adicionales). Se optó por FlatLaf. La versión con JavaFX sigue como posible "cuarto paso" opcional a futuro si se necesita, pero no era necesaria para resolver el pedido de "que se vea mejor".

**Próximos pasos (pendientes):**
- (Opcional, fuera de alcance) Versión con JavaFX, si más adelante se decide explorarla.
- Persistencia opcional (Épica 5), solo si el resto del proyecto está terminado y probado.

---

### 2026-08-09 — Interfaz con Swing "de verdad" (ventana JFrame)

**Descripción:** Tercera etapa de interfaz, reemplazando el menú de `JOptionPane` (que se reabría en cada vuelta) por una ventana persistente con botones.

**Cambios realizados:**
1. `ui/MenuPrincipal.java` reescrito: ahora extiende `JFrame`, con un panel de 11 botones (uno por cada una de las 10 acciones + Salir), en vez de mostrar un cuadro de texto con la lista de opciones en cada vuelta.
2. `VistaUsuarios.java` y `VistaTareas.java`: **sin cambios**. Siguen usando `JOptionPane` para pedir datos puntuales (id, nombre, prioridad, etc.) — eso también es Swing, y reescribir cada formulario como un `JDialog` propio se dejó de lado para no complicar el proyecto sin necesidad real.
3. `Main.java`: mismo llamado a `new MenuPrincipal(usuarioService, tareaService).mostrar()`, sin cambios de código (solo se actualizó el comentario).

**Verificación:** `mvn clean test` — `BUILD SUCCESS`, 20/20 pruebas (no dependen de la interfaz).

**Próximos pasos (pendientes):**
- Mejorar la apariencia visual (ver entrada siguiente: FlatLaf).
- (Opcional, fuera de alcance) Versión con JavaFX.
- Persistencia opcional (Épica 5).

---

### 2026-08-09 — Interfaz con JOptionPane (segunda etapa)

**Descripción:** Segunda etapa de interfaz (después de la terminal): se implementó `ui/MenuPrincipal.java`, `ui/VistaUsuarios.java` y `ui/VistaTareas.java` con `JOptionPane`, siguiendo la estructura de paquetes ya documentada (en vez de tener todo el código de la interfaz metido en `Main.java`, como en la fase de terminal).

**Cambios realizados:**
1. `ui/VistaUsuarios.java`: `registrarUsuario()` y `consultarUsuarios()`, con diálogos de `JOptionPane`.
2. `ui/VistaTareas.java`: las 8 acciones de tareas (crear, asignar, cambiar prioridad, cambiar estado, y las 4 consultas), todas con diálogos de `JOptionPane`.
3. `ui/MenuPrincipal.java`: arma `VistaUsuarios`/`VistaTareas` y controla el bucle del menú (un `JOptionPane.showInputDialog` con las 10 opciones + Salir, que se repite hasta elegir 0 o cerrar la ventana).
4. `Main.java`: se simplificó a solo armar las dependencias y llamar a `new MenuPrincipal(...).mostrar()` — la lógica del menú que antes estaba ahí se movió a la carpeta `ui`, donde siempre debió estar según el documento de diseño.

**Decisiones de diseño:** ninguna de las tres clases valida ni decide nada por su cuenta — todas las excepciones de negocio (`IllegalArgumentException`) las sigue tirando `service`, y `MenuPrincipal` las atrapa en un solo lugar para mostrarlas como diálogo de error.

**Verificación:** `mvn clean test` — `BUILD SUCCESS`, 20/20 pruebas (no dependen de la interfaz; la interfaz gráfica no se puede probar de forma automática, se probó a mano).

**Próximos pasos (pendientes):**
- Reemplazar por una ventana con Swing (`JFrame`).
- (Opcional, fuera de alcance) Versión con JavaFX.
- Persistencia opcional (Épica 5).

---

### 2026-08-09 — Main.java al día con Épica 3, se elimina MainPruebas.java, se valida y se restaura el changelog

**Descripción:** El equipo dio por implementadas (casi) todas las historias de usuario y pidió dejar de usar `MainPruebas.java`, probar todo desde `Main.java`, y revisar/validar `docs/cambios.md`.

**Hallazgo al revisar `cambios.md`:** el merge de la rama con HU-08/HU-09/HU-10 (`FeatureAtomStatusTaskEpics4_5`) con la rama que traía el menú de terminal y la decisión de Java 17 tuvo un conflicto en este archivo. Se resolvió quedándose con una sola versión, y 3 entradas completas se perdieron del historial (aunque el código sí se mergeó bien): "Comentarios de paquete en lenguaje simple", "Main.java: primera interfaz de usuario (terminal)" y "Definido: el proyecto usa Java 17". Se restauraron más abajo, en el lugar donde cronológicamente correspondían, con una nota explicando qué pasó.

**Cambios realizados:**
1. `Main.java`: el menú de terminal (que solo cubría Épica 1 y 2) se completó con las 3 opciones de Épica 3: cambiar el estado de una tarea (HU-08), consultar las tareas agrupadas por estado en formato tablero (HU-09), y consultar todas las tareas ordenadas por prioridad (HU-10). El menú pasó de 7 a 10 opciones.
2. Se borró `src/main/java/com/atommanager/MainPruebas.java`: ya cumplió su función (probar el menú sin arriesgar `Main.java`) y el equipo confirmó que ya no hace falta.
3. Se restauraron las 3 entradas de `cambios.md` perdidas en el merge (ver más abajo, en su ubicación cronológica).

**Verificación:**
- `mvn clean test`: `BUILD SUCCESS`, **20/20 pruebas** (antes eran 13; las 7 nuevas son de HU-08/HU-09/HU-10).
- `Main.java` corrido de punta a punta simulando las 10 opciones del menú (registrar usuario, crear tarea, asignar, cambiar prioridad, cambiar estado, consultar todas, consultar por usuario, consultar por estado/tablero, consultar todas por prioridad, salir): todas responden correctamente, sin excepciones sin atrapar, y los datos quedan consistentes entre pasos (por ejemplo, la tarea creada en Alta y bajada a Baja se ve como Baja en las consultas posteriores).

**Próximos pasos (pendientes):**
- Reemplazar el menú de `Main.java` por una versión con `JOptionPane`.
- Después, reemplazar esa versión por una con Swing (ventanas con `JFrame`).
- Como ejercicio extra fuera de alcance: una versión con JavaFX, actualizando `AtomManager2.1.md` para reflejar esa decisión cuando se llegue a esa etapa.
- Persistencia opcional (Épica 5), solo si el resto del proyecto está terminado y probado.

---

### 2026-08-09 — Implementación de HU-10 (consulta general por prioridad)

**Descripción:** Se implementó la historia de usuario HU-10 de la Épica 3: consultar todas las tareas ordenadas de mayor a menor prioridad.

**Cambios realizados:**

1. `service/TareaService.java`: se agregó la sobrecarga `tareasPorPrioridad()` para consultar todas las tareas registradas.
2. El ordenamiento se extrajo al método privado `ordenarPorPrioridad(List<Tarea>)`, reutilizado por HU-03 y HU-10. El método vacía una `PriorityQueue` para garantizar el orden Alta → Moderada → Baja.
3. `src/test/.../TareaServiceTest.java`: se agregaron pruebas para el listado general ordenado y para el caso sin tareas.
4. `README.md` y `docs/GUIA-PROXIMO-DESARROLLADOR.md`: se actualizó el estado de avance.

**Decisiones de diseño:**
- La sobrecarga sin parámetros mantiene explícita la diferencia entre HU-03 (tareas de un usuario) y HU-10 (todas las tareas), sin crear otro servicio ni duplicar lógica.
- No se usa el orden de iteración de `PriorityQueue`; las tareas se extraen con `poll()` antes de devolver la lista.

**Próximos pasos (pendientes):**
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).
- Persistencia opcional si el resto del proyecto está terminado y probado.

---

### 2026-08-09 — Implementación de HU-09 (consulta de tareas por estado)

**Descripción:** Se implementó la historia de usuario HU-09 de la Épica 3: consultar las tareas agrupadas por estado en una vista tipo tablero.

**Cambios realizados:**

1. `repository/TareaRepository.java`: se agregó `buscarPorEstado(Estado)` al contrato de acceso a datos.
2. `repository/TareaRepositoryMemoria.java`: se implementó la consulta sobre el `HashMap` principal, filtrando por el enum `Estado` sin duplicar ni desincronizar datos.
3. `service/TareaService.java`: se agregó `tareasPorEstado()`, que devuelve un `Map<Estado, List<Tarea>>` con las tres columnas: `POR_REALIZAR`, `EN_PROCESO` y `FINALIZADA`.
4. `src/test/.../TareaServiceTest.java`: se agregaron pruebas del agrupamiento y del tablero vacío, verificando que las tres columnas siempre estén presentes.
5. `README.md` y `docs/GUIA-PROXIMO-DESARROLLADOR.md`: se actualizó el estado de avance.

**Decisiones de diseño:**
- El servicio usa `EnumMap` porque las claves son valores del enum `Estado`; expone la interfaz `Map` para no acoplar a quien consume el resultado.
- La vista incluye una lista vacía para cada estado aunque no haya tareas, lo que permite a la interfaz gráfica mostrar siempre las tres columnas requeridas.
- HU-10 no se implementó: es una historia independiente para ordenar todas las tareas por prioridad.

**Próximos pasos (pendientes):**
- HU-10: consultar todas las tareas ordenadas por prioridad.
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Implementación de HU-08 (cambio de estado de tareas)

**Descripción:** Se implementó la historia de usuario HU-08 de la Épica 3: cambiar el estado de una tarea existente.

**Cambios realizados:**

1. `model/Tarea.java`: el atributo `estado` deja de ser inmutable y se agregó `setEstado(Estado)`. El setter valida que el estado no sea nulo y lanza `IllegalArgumentException` si se intenta asignar uno inválido.
2. `service/TareaService.java`: se agregó `cambiarEstado(String tareaId, Estado estado)`. Reutiliza la validación existente de tarea por ID y delega la validación de estado al modelo.
3. `src/test/.../TareaServiceTest.java`: se agregaron pruebas para cambiar una tarea a `EN_PROCESO` y `FINALIZADA`, intentar cambiar el estado de una tarea inexistente y pasar un estado nulo.
4. `README.md` y `docs/GUIA-PROXIMO-DESARROLLADOR.md`: se actualizó el estado de avance; HU-09 (consulta agrupada por estado) sigue pendiente.

**Decisiones de diseño:**
- Se permiten cambios entre cualquiera de los tres valores del enum, incluido retroceder de estado, tal como permite el documento de diseño.
- HU-09 y HU-10 no se implementaron: son historias independientes que requieren consultas nuevas en la capa `repository` y `service`.

**Próximos pasos (pendientes):**
- HU-09: consultar tareas agrupadas por estado.
- HU-10: consultar todas las tareas ordenadas por prioridad.
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

> **Nota de recuperación:** las 3 entradas siguientes (package-info en lenguaje simple, Main.java por terminal, decisión de Java 17) se hicieron en paralelo a HU-08/HU-09/HU-10, en otra rama. Al mergear ambas ramas, el conflicto en este archivo se resolvió quedándose con una sola versión y estas 3 entradas se perdieron del changelog — aunque el código correspondiente sí siguió en el proyecto (se verificó que existe). Se restauran acá para que el registro quede completo.

### 2026-08-09 — Comentarios de paquete en lenguaje simple

**Descripción:** Se reescribieron los 4 `package-info.java` (`model`, `repository`, `service`, `ui`) porque el equipo los encontró poco claros: usaban códigos como "SOLID-S"/"SOLID-D"/"SOLID-L" y referencias a secciones del documento de diseño (RF-17, sección 4.1/4.2) sin explicar qué significaban en la práctica.

**Cambios realizados:** mismo contenido y ubicación de cada uno, pero explicado en español simple: qué hay en la carpeta, qué NO va ahí, y por qué está organizado así, sin siglas ni referencias a números de requisito. Se mantiene el mismo tono que `docs/GUIA-PROXIMO-DESARROLLADOR.md`.

**Verificación:** `mvn test` — sigue compilando y las pruebas siguen pasando (son comentarios, no cambia ningún comportamiento).

---

### 2026-08-09 — Main.java: primera interfaz de usuario (terminal)

**Descripción:** A pedido del equipo, se va a interactuar con las épicas implementadas en tres etapas progresivas: primero por terminal, después con `JOptionPane`, y por último con Swing (más una cuarta etapa opcional con JavaFX, fuera del alcance oficial del documento de diseño, como ejercicio extra). Se decidió que las etapas van a vivir todas en `Main.java`, reemplazando cada una a la anterior — no van a convivir varias interfaces sueltas en el proyecto.

**Cambios realizados:**
1. `Main.java` completado: arma las dependencias (`UsuarioRepositoryMemoria`, `TareaRepositoryMemoria`, `UsuarioService`, `TareaService`) y levanta un menú por consola con `Scanner`.
2. El menú cubre las 7 historias de usuario de Épica 1 y 2: registrar usuario, consultar usuarios, crear tarea, asignar tarea, cambiar prioridad, consultar todas las tareas, y consultar las tareas de un usuario ordenadas por prioridad.
3. Los errores de negocio (`IllegalArgumentException` que tiran los `service`, por ejemplo id duplicado o usuario inexistente) se atrapan en el menú y se muestran como mensaje, sin cortar el programa.
4. `MainPruebas.java`: copia temporal de `Main.java` para que el usuario pudiera correr y probar el menú de forma interactiva (tipeando él mismo) sin arriesgar la versión "oficial" mientras se iteraba.

**Verificación:** se corrió el programa de punta a punta simulando una sesión completa (registrar usuario → crear tarea → asignar → cambiar prioridad → consultar) y también los casos de error (id duplicado, tarea inexistente, opción de menú inválida): todo responde como se espera, sin excepciones sin atrapar.

**Próximos pasos (pendientes):**
- Reemplazar el menú de `Main.java` por una versión con `JOptionPane`.
- Después, reemplazar esa versión por una con Swing (ventanas con `JFrame`).
- Como ejercicio extra fuera de alcance: una versión con JavaFX, actualizando `AtomManager2.1.md` para reflejar esa decisión cuando se llegue a esa etapa.

---

### 2026-08-09 — Definido: el proyecto usa Java 17

**Descripción:** Al correr `MainPruebas.java` desde el IDE apareció `UnsupportedClassVersionError`: el IDE ejecuta con `java-17-openjdk` (class file version 61), pero `pom.xml` compilaba para Java 21 (class file version 65). Esto es la misma inconsistencia Java 17/README vs Java 21/`pom.xml` que quedaba pendiente desde varias entradas atrás — recién ahí se manifestó como error real, no solo como discrepancia en la documentación.

**Decisión:** el proyecto queda en **Java 17**. Motivos: (1) es lo que el IDE del usuario ejecuta por defecto — hay JDK 17, 21 y 26 instalados en la máquina, y el `java` con el que corre el botón "Run" del IDE es el 17; (2) el código no usa ninguna característica exclusiva de Java 21 o superior (se revisó: sin `record`, `sealed`, pattern matching en `switch`, etc.) — todo compila y corre igual en 17; (3) es lo que ya decía `README.md` desde que el usuario lo dejó así explícitamente en una sesión anterior.

**Cambios realizados:**
1. `pom.xml`: `maven.compiler.source`/`target` de `21` a `17`.
2. `mvn clean test`: recompilado desde cero, `.class` ahora en major version 61 (Java 17).
3. Verificado corriendo `MainPruebas` directamente con `/usr/lib/jvm/java-17-openjdk/bin/java` (el mismo binario del error original): ya no tira `UnsupportedClassVersionError`.

**Esto cierra el punto "definir si el proyecto usa Java 17 o Java 21"** que venía repitiéndose como pendiente desde hace varias entradas de este documento.

---

### 2026-08-09 — Revisión de coherencia README ↔ AtomManager2.1.md

**Descripción:** Revisión completa de `README.md` contra el documento de diseño `AtomManager2.1.md` para garantizar coherencia total.

**Cambios realizados en `README.md`:**

1. **Versión de Java alineada con el proyecto:** se corrigió "Java 21"/"JDK 21" → "Java 17"/"JDK 17" (descripción, tabla de tecnologías y requisitos previos), coincidiendo con el `pom.xml` (`maven.compiler.source/target = 17`). El documento de diseño no fija versión (RNF-01 solo indica Java).
2. **Objetivos específicos completados:** se amplió la lista a los 13 objetivos de la sección 2.1 del documento (añadidos: prioridades, estados, `Map`/`HashMap`, `Queue`/`PriorityQueue`, POO, SOLID, Swing/JOptionPane y Conventional Commits).
3. **Alcance corregido:** se sustituyó "CRUD de usuarios y tareas" por la redacción exacta del documento (sección 9.1): usuarios — registrar y consultar; tareas — crear, editar, asignar responsable, prioridad y estado.
4. **`model/Prioridad` detallado:** se añadió la mención a `peso`, `etiqueta` e `ícono` en la tabla del paquete `model` y en el árbol de arquitectura (sección 8 del documento).

**Verificación:** búsqueda de referencias residuales a "Java 21"/"CRUD" con resultado vacío; hoy no queda ninguna discrepancia entre README, `AtomManager2.1.md` y el `pom.xml`.

---

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

---

### 2026-08-09 — Documentación de paquetes para el equipo (`package-info.java`)

**Descripción:** A pedido del equipo, se documentó dentro del propio código (no solo en `README.md`) qué va y qué no va en cada paquete, para que cualquier desarrollador lo vea directamente en el IDE o al generar Javadoc, sin necesidad de implementar todavía la lógica real. No se tocó ninguna firma ni lógica existente: es documentación pura.

**Cambios realizados:**
1. Creado `model/package-info.java`: qué entidades van ahí (`Usuario`, `Tarea`, `Prioridad`, `Estado`) y qué no (repositorios, services, vistas, Swing/HashMap/PriorityQueue).
2. Creado `repository/package-info.java`: interfaces + implementaciones en memoria; regla de que `service` depende de la interfaz, nunca de la implementación (SOLID-D/L).
3. Creado `service/package-info.java`: dónde vive la validación de reglas de negocio; inyección de repositorios por constructor; sin código de Swing acá.
4. Creado `ui/package-info.java`: qué vistas van ahí; sin lógica de negocio, depende de `service`, nunca de `repository` directamente.
5. Agregado Javadoc de clase a `Main.java` explicando que es la única clase que debe conocer las implementaciones concretas de `repository`, y qué pasos le corresponde armar (instanciar repos en memoria, inyectarlos en los services, arrancar `MenuPrincipal`). El cuerpo del método `main` no se modificó (sigue como placeholder).
6. Verificada la compilación completa del proyecto con `javac` (sin errores).

**Nota para coordinación entre agentes:** esta entrada es solo documentación (`package-info.java` + Javadoc de `Main`); ningún archivo de lógica fue tocado. La implementación real de `model`, `repository`, `service`, `ui` y `Main` sigue pendiente y listada abajo.

**Próximos pasos (pendientes):**
- Implementar atributos, constructores, getters/setters y validaciones en `model` (`Usuario`, `Tarea`, `Prioridad`, `Estado`).
- Implementar los métodos de `UsuarioRepository` / `TareaRepository` y sus implementaciones en memoria.
- Implementar la lógica de negocio en `UsuarioService` / `TareaService`, incluyendo `PriorityQueue`.
- Implementar la capa `ui` (Swing / JOptionPane).
- Completar `Main.java` (inyección de dependencias e inicio de `MenuPrincipal`).

---

### 2026-08-09 — Implementación de Épica 1 y Épica 2 (model, repository, service)

**Descripción:** Implementación funcional de las historias de usuario HU-01 a HU-07 (Épica 1 — Gestión de usuarios, Épica 2 — Gestión de tareas). Alcance intencionalmente acotado a estas dos épicas: no se tocó `ui/` ni `Main.java`, y `Tarea` todavía no expone forma de cambiar su `Estado` (eso es HU-08, Épica 3).

**Cambios realizados:**
1. `model/Usuario.java`: `id` (inmutable) y `nombre`, con validación de campos obligatorios en el constructor y en `setNombre`. `equals`/`hashCode` por `id`.
2. `model/Tarea.java`: `id` (inmutable, autogenerado por el service), `titulo` (obligatorio), `descripcion`, `prioridad`, `responsable` y `estado` (inmutable, nace en `POR_REALIZAR`; sin setter todavía porque HU-08 está fuera de alcance). `equals`/`hashCode` por `id`.
3. `model/Prioridad.java` (enum): `ALTA(1)`, `MODERADA(2)`, `BAJA(3)` con `peso`, `etiqueta` e `icono`; el peso es el que ordena la `PriorityQueue`.
4. `model/Estado.java` (enum): `POR_REALIZAR`, `EN_PROCESO`, `FINALIZADA` — solo el valor por defecto que necesita `Tarea`; sin lógica de transición todavía.
5. `repository/UsuarioRepository(Memoria)`: `guardar`, `buscarPorId`, `listarTodos`, `existe`, sobre `Map<String, Usuario>`.
6. `repository/TareaRepository(Memoria)`: `guardar`, `buscarPorId`, `listarTodas`, `buscarPorUsuario` (filtra por `responsable.id`), `existe`, sobre `Map<String, Tarea>`. No se agregó `buscarPorEstado`: pertenece a Épica 3.
7. `service/UsuarioService`: `registrarUsuario` (HU-01, valida id único) y `listarUsuarios` (HU-02).
8. `service/TareaService`: `crearTarea` (HU-04, id autogenerado `"T-" + contador`), `asignarTarea` (HU-05, valida que el usuario exista), `cambiarPrioridad` (HU-06), `listarTareas` (HU-07) y `tareasPorPrioridad` (HU-03, vacía una `PriorityQueue` en una `List` para no depender del orden de iteración interno).
9. Verificada la compilación completa del proyecto con `javac` (sin errores).

**Decisiones de diseño (POO/SOLID):**
- `TareaService` depende de `TareaRepository` **y** `UsuarioRepository` (ambas interfaces) para poder validar HU-05 sin que `Tarea`/`TareaRepository` conozcan nada de usuarios fuera de la referencia ya modelada (SOLID-D, SOLID-S).
- No se agregaron mapas índice (`tareasPorUsuario`, `tareasPorEstado`) además del `Map` principal: `buscarPorUsuario` filtra sobre el único `HashMap` con un stream. Se prioriza no tener dos estructuras que puedan desincronizarse por sobre seguir al pie de la letra la sección 4.1 del documento; si el volumen de tareas lo justifica más adelante, se puede optimizar sin cambiar la interfaz `TareaRepository`.
- `Tarea` no tiene `setEstado(...)` ni `TareaRepository` tiene `buscarPorEstado(...)`: agregarlos ahora sería anticipar Épica 3 sin una historia de usuario que lo pida (YAGNI).

**Próximos pasos (pendientes):**
- Probar `UsuarioService`/`TareaService` (HU-01 a HU-07) — en curso.
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Pruebas unitarias de Épica 1 y Épica 2 (JUnit 5)

**Descripción:** Se agregó JUnit 5 como dependencia de test y se escribieron pruebas unitarias para `UsuarioService` y `TareaService`, cubriendo los criterios de aceptación de HU-01 a HU-07.

**Cambios realizados:**
1. `pom.xml`: agregada dependencia `org.junit.jupiter:junit-jupiter:5.10.2` (scope `test`) y `maven-surefire-plugin:3.2.5` para que `mvn test` las ejecute.
2. `src/test/java/com/atommanager/service/UsuarioServiceTest.java` (4 tests, HU-01/HU-02): registrar usuario válido, id duplicado lanza excepción, listar usuarios (con y sin registros).
3. `src/test/java/com/atommanager/service/TareaServiceTest.java` (9 tests, HU-03 a HU-07): id autogenerado y único, título vacío lanza excepción, asignar a usuario existente/inexistente, asignar con id de tarea inexistente, cambiar prioridad, listar todas, y el caso clave de HU-03 — `tareasPorPrioridad` devuelve las tareas de un usuario en orden Alta → Moderada → Baja usando `PriorityQueue`.
4. Cada test parte de repositorios en memoria nuevos (`@BeforeEach`) para que los casos no compartan estado.

**Cómo se corrieron (nota de entorno):** este sandbox no tiene el binario `mvn` instalado, solo `java`/`javac`. Para verificar las pruebas ahora se compiló todo con `javac` y se ejecutaron con `junit-platform-console-standalone-1.10.2.jar` (descargado de Maven Central) en vez de `mvn test`. En cualquier máquina con Maven instalado, `mvn test` debería correr esta misma suite sin pasos adicionales, ya que el `pom.xml` quedó configurado para eso.

**Resultado:** 13/13 tests pasaron (5 containers, 0 fallos).

**Próximos pasos (pendientes):**
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).
- Cuando se implemente `ui`/`Main`, correr `mvn test` en una máquina con Maven para confirmar que la config del `pom.xml` funciona igual que la verificación manual hecha acá.

---

### 2026-08-09 — Guía en lenguaje simple para el próximo desarrollador

**Descripción:** Se creó `docs/GUIA-PROXIMO-DESARROLLADOR.md`, un documento en español simple (sin jerga técnica) que resume: qué funciona hoy (Épica 1 y 2), qué falta (Épica 3, `ui`/`Main`, persistencia opcional), en qué carpeta/archivo va cada cosa pendiente, y cómo correr y agregar pruebas. Pensado para alguien que se suma al proyecto y necesita entender el panorama completo sin tener que reconstruirlo leyendo commit por commit. Se enlazó desde la sección "Documentación" de `README.md`.

**Conflicto de edición detectado (para que quede registrado):** mientras se trabajaba en esta sesión, otro proceso/agente editó `README.md` y `pom.xml` en paralelo:
- `pom.xml` quedó en **Java 21** (`maven.compiler.source/target`).
- `README.md` quedó en **Java 17** en sus tres menciones (descripción, tabla de tecnologías, requisitos previos), revertido intencionalmente después de haber sido llevado a 21 en una entrada anterior.

Esto deja una inconsistencia real entre `pom.xml` y `README.md` sobre qué versión de Java usa el proyecto. No se resolvió en esta entrada porque el cambio en `README.md` fue una edición manual explícita durante la sesión (no un error de sincronización), así que corresponde que el equipo defina cuál de las dos versiones es la correcta y alinee el otro archivo, en vez de que un agente lo decida unilateralmente.

**Próximos pasos (pendientes):**
- Definir si el proyecto usa Java 17 o Java 21, y alinear `pom.xml` y `README.md` entre sí (hoy están en desacuerdo).
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Maven instalado y `mvn test` verificado

**Descripción:** Se instaló Apache Maven 3.9.16 en la máquina de desarrollo (antes solo estaban `java`/`javac`, sin `mvn`) y se corrió `mvn test` de punta a punta para confirmar que la suite de tests de la Épica 1 y 2 funciona igual con el flujo estándar de Maven, no solo con el método manual (`junit-platform-console-standalone.jar`) usado en la entrada anterior por falta de Maven en ese momento.

**Resultado:** `BUILD SUCCESS`. `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0` (`UsuarioServiceTest` 4/4, `TareaServiceTest` 9/9). Coincide con el resultado obtenido antes a mano.

**Próximos pasos (pendientes):**
- Definir si el proyecto usa Java 17 o Java 21, y alinear `pom.xml` y `README.md` entre sí (hoy están en desacuerdo).
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Limpieza de la estructura del repositorio

**Descripción:** Auditoría completa de archivos del proyecto a pedido del equipo, porque la carpeta `target/` (generada por Maven al compilar) se había estado versionando por error desde el primer commit — nunca existió un `.gitignore` real, pese a que una entrada muy anterior de este mismo documento decía haberlo agregado.

**Hallazgo:** 20 archivos de `target/` (`.class` compilados, reportes de `surefire`, metadata interna de `maven-compiler-plugin`) estaban trackeados en git. Son artefactos 100% regenerables con `mvn compile` / `mvn test` / `mvn clean`; no aportan nada versionados y generan diffs de ruido cada vez que alguien compila. No se encontró ningún otro archivo sobrante (sin `.class` fuera de `target/`, sin archivos de IDE, sin duplicados).

**Cambios realizados:**
1. Creado `.gitignore` en la raíz del proyecto: ignora `target/`, carpetas/archivos comunes de IDE (`.idea/`, `*.iml`, `.vscode/`, `.settings/`, `.classpath`, `.project`) y `.DS_Store`.
2. `git rm -r --cached target`: se sacó `target/` del seguimiento de git. Los archivos siguen en el disco (Maven los necesita para correr), solo dejan de versionarse.
3. Actualizado el árbol de `README.md` (sección "Estructura del repositorio"): se sacaron las etiquetas `(stub)` de `model`, `repository` y `service` (ya están implementados, quedan solo en `ui`, que sigue pendiente), se agregaron los `package-info.java`, `src/test/`, `.gitignore` y `docs/GUIA-PROXIMO-DESARROLLADOR.md`, que no figuraban.
4. Verificado con `mvn clean test`: sigue compilando y las 13 pruebas siguen pasando después de la limpieza.

**Próximos pasos (pendientes):**
- Definir si el proyecto usa Java 17 o Java 21, y alinear `pom.xml` y `README.md` entre sí (hoy están en desacuerdo).
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).

---

### 2026-08-09 — Merge con `origin/developer` y push

**Descripción:** Al hacer `git push origin developer` el remoto rechazó el push porque tenía un commit que no estaba en el historial local: `2b06c46 "Feature H01 User Register Task and User"`, de `CondezPancake`, del 6 de agosto (anterior a toda esta sesión de trabajo) — nunca se había hecho `git pull` de ese commit.

**Qué tenía ese commit:** solo creaba `model/Tarea.java` y `model/Usuario.java` vacíos (`public class Tarea {}` / `public class Usuario {}`), el punto de partida exacto sobre el que se construyó después toda la implementación de Épica 1 y 2 en esta sesión.

**Resolución:** `git merge origin/developer` marcó conflicto tipo "add/add" en esos dos archivos (ambas ramas los "crearon" de forma independiente desde una base donde no existían). Se resolvió quedándose con la versión completa e implementada (la de esta sesión) y descartando el stub vacío. También el merge reintrodujo dos `.class` de `target/` que el commit remoto tenía trackeados; se sacaron de nuevo del índice con `git rm --cached -f`, coherente con la limpieza de `target/`/`.gitignore` de la entrada anterior.

**Verificación:** `mvn test` después del merge — `BUILD SUCCESS`, 13/13 pruebas, sin cambios de comportamiento.

**Nota para coordinación entre agentes/colaboradores:** este es el primer caso concreto de historias de git realmente divergentes (no solo archivos desactualizados en el mismo árbol de trabajo). Antes de cualquier `git push` a una rama compartida, conviene `git fetch` primero para detectar esto antes de que lo rechace el remoto.

**Próximos pasos (pendientes):**
- Definir si el proyecto usa Java 17 o Java 21, y alinear `pom.xml` y `README.md` entre sí (hoy están en desacuerdo).
- Épica 3 (HU-08 estado, HU-09 consulta por estado, HU-10 consulta general por prioridad).
- Implementar la capa `ui` (Swing / JOptionPane) y completar `Main.java` (inyección de dependencias).
