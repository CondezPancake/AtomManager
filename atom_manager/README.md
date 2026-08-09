# AtomManager

Gestor de tareas de escritorio inspirado en herramientas como Jira y Trello, desarrollado en **Java 17** aplicando Programación Orientada a Objetos y los principios **SOLID**.

> ⚠️ **Estado actual: en desarrollo (esqueleto inicial).** La estructura de paquetes está definida pero la lógica de negocio y la interfaz gráfica todavía no están implementadas. Consultá [`docs/cambios.md`](docs/cambios.md) para ver el registro de avances.

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Objetivo](#objetivo)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Principios SOLID aplicados](#principios-solid-aplicados)
- [Estructuras de datos](#estructuras-de-datos)
- [Prioridades y estados de una tarea](#prioridades-y-estados-de-una-tarea)
- [Alcance del proyecto](#alcance-del-proyecto)
- [Cómo compilar y ejecutar](#cómo-compilar-y-ejecutar)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Documentación](#documentación)
- [Flujo de trabajo colaborativo](#flujo-de-trabajo-colaborativo)

---

## Descripción

AtomManager permite crear usuarios, crear tareas, asignarlas a un responsable, establecer su prioridad y controlar su estado a lo largo del desarrollo de un proyecto.

La interfaz gráfica se implementa con **Swing / JOptionPane**, opcionalmente apoyada en **FlatLaf** para mejorar la apariencia. Los datos se manejan en memoria mediante `Map` / `HashMap`, y el ordenamiento por prioridad se resuelve con la interfaz `Queue` (`PriorityQueue`).

## Objetivo

Desarrollar un gestor de tareas que permita a un equipo de trabajo organizar, asignar, priorizar y hacer seguimiento de las actividades de un proyecto.

**Objetivos específicos:**

- Crear usuarios dentro del sistema.
- Crear y administrar tareas.
- Asignar tareas a usuarios.
- Definir prioridades para las tareas (Alta, Moderada, Baja).
- Gestionar el estado de las tareas (Por realizar, En proceso, Finalizada).
- Consultar las tareas asignadas a cada usuario **ordenadas por prioridad**.
- Visualizar las tareas agrupadas por estado.
- Utilizar `Map` y `HashMap` como almacén principal de datos en memoria.
- Utilizar la interfaz `Queue` (`PriorityQueue`) para el ordenamiento por prioridad.
- Modelar el sistema aplicando POO (encapsulamiento, abstracción, enums, interfaces).
- Aplicar los principios SOLID en la separación de responsabilidades.
- Desarrollar la interfaz gráfica con Swing / JOptionPane.
- Utilizar GitHub para el trabajo colaborativo con Conventional Commits.

## Tecnologías

| Categoría | Tecnología |
| --- | --- |
| Lenguaje | Java 17 |
| Build tool | Maven |
| Interfaz gráfica | Swing / JOptionPane (opcionalmente [FlatLaf](https://www.formdev.com/flatlaf/)) |
| Estructuras de datos | `HashMap`, `Queue` (`PriorityQueue`) |
| Control de versiones | Git + GitHub, [Conventional Commits](https://www.conventionalcommits.org/) |

## Arquitectura

Organización por paquetes según responsabilidad (separación en capas: modelo, persistencia, lógica de negocio e interfaz). La dependencia entre capas va en una sola dirección: `ui → service → repository → model`; ninguna capa inferior conoce a las superiores.

```text
com.atommanager
│
├── model/            → Entidades del dominio: solo datos, sin lógica ni dependencias de otros paquetes.
│   ├── Usuario        → id, nombre.
│   ├── Tarea           → id, titulo, descripcion, prioridad, estado, responsable (Usuario).
│   ├── Prioridad (enum) → ALTA(1), MODERADA(2), BAJA(3), con peso, etiqueta e ícono.
│   └── Estado (enum)    → POR_REALIZAR, EN_PROCESO, FINALIZADA.
│
├── repository/       → Acceso a datos: interfaces + implementación en memoria (HashMap).
│   ├── UsuarioRepository (interfaz)        → contrato de acceso a Usuario.
│   ├── TareaRepository (interfaz)          → contrato de acceso a Tarea.
│   ├── UsuarioRepositoryMemoria (clase)    → implementa UsuarioRepository con Map<String, Usuario>.
│   └── TareaRepositoryMemoria (clase)      → implementa TareaRepository con Map<String, Tarea>.
│
├── service/          → Reglas de negocio: validaciones, asignación, PriorityQueue por prioridad.
│   ├── UsuarioService  → registrar/listar usuarios (depende de UsuarioRepository).
│   └── TareaService    → crear/asignar/cambiar prioridad-estado, cola de prioridad (depende de TareaRepository).
│
├── ui/                → Presentación (Swing / JOptionPane), sin lógica de negocio.
│   ├── MenuPrincipal   → menú de arranque, navega a las demás vistas.
│   ├── VistaUsuarios   → alta y listado de usuarios.
│   └── VistaTareas     → alta, asignación y consultas de tareas.
│
└── Main.java          → único punto de entrada (método main). Es la única clase que conoce las
                          implementaciones concretas: instancia los *Repository*Memoria, los inyecta
                          en los *Service* correspondientes y arranca MenuPrincipal. No hay otros
                          módulos ni clases fuera de este árbol de paquetes.
```

### Responsabilidad de cada paquete

El proyecto separa el código en 4 paquetes según su responsabilidad, más el punto de entrada `Main.java`. La dependencia entre capas va siempre en una sola dirección: `ui → service → repository → model`; ninguna capa inferior conoce a las superiores.

#### `com.atommanager.model`

Contiene únicamente las clases de datos del dominio: no tienen lógica de negocio ni dependen de ningún otro paquete (SOLID-S). `Tarea` compone a `Usuario` a través del atributo `responsable`.

| Tipo | Kind | Responsabilidad |
| --- | --- | --- |
| `Usuario` | Clase | Representa un integrante del proyecto: `id`, `nombre`. |
| `Tarea` | Clase | Representa una actividad: `id`, `titulo`, `descripcion`, `prioridad` (`Prioridad`), `estado` (`Estado`), `responsable` (`Usuario`). |
| `Prioridad` | Enum | Niveles de prioridad de una tarea: `ALTA(1)`, `MODERADA(2)`, `BAJA(3)`, cada constante con `peso`, `etiqueta` e `ícono`. El peso numérico es el que ordena la `PriorityQueue`. |
| `Estado` | Enum | Ciclo de vida de una tarea: `POR_REALIZAR`, `EN_PROCESO`, `FINALIZADA`. |

#### `com.atommanager.repository`

Encapsula el acceso a los datos en memoria. Cada entidad tiene una interfaz pequeña y específica (SOLID-I) y una implementación concreta con `HashMap`, de modo que el resto del sistema depende del contrato, no de cómo se almacenan los datos (SOLID-D). Una futura implementación (por ejemplo, en archivo) podría reemplazar a la de memoria sin romper nada (SOLID-L).

| Tipo | Kind | Responsabilidad |
| --- | --- | --- |
| `UsuarioRepository` | Interfaz | Contrato de acceso a datos de `Usuario` (guardar, buscar por ID, listar, verificar existencia). |
| `TareaRepository` | Interfaz | Contrato de acceso a datos de `Tarea` (guardar, buscar por ID, listar, filtrar por usuario/estado). |
| `UsuarioRepositoryMemoria` | Clase | Implementa `UsuarioRepository` con `Map<String, Usuario>`. |
| `TareaRepositoryMemoria` | Clase | Implementa `TareaRepository` con `Map<String, Tarea>` y los índices auxiliares por usuario y por estado. |

#### `com.atommanager.service`

Contiene las reglas de negocio: validaciones, asignación de tareas, cambios de prioridad/estado y el ordenamiento por prioridad mediante `PriorityQueue`. Cada servicio recibe su repositorio correspondiente por constructor (inyección de dependencias, SOLID-D), nunca instancia la implementación concreta directamente.

| Tipo | Kind | Responsabilidad |
| --- | --- | --- |
| `UsuarioService` | Clase | Registrar y listar usuarios; valida que el ID sea único antes de guardar. |
| `TareaService` | Clase | Crear/editar tareas, asignar responsable, cambiar prioridad y estado, y construir la `PriorityQueue` de tareas por usuario. |

#### `com.atommanager.ui`

Capa de presentación con Swing / JOptionPane. No contiene reglas de negocio: solo pide datos al usuario, invoca al `service` correspondiente y muestra el resultado (SOLID-S).

| Tipo | Kind | Responsabilidad |
| --- | --- | --- |
| `MenuPrincipal` | Clase | Menú de arranque de la aplicación; navega hacia las vistas de usuarios y tareas. |
| `VistaUsuarios` | Clase | Pantallas para registrar y consultar usuarios. |
| `VistaTareas` | Clase | Pantallas para crear, asignar tareas y consultarlas por usuario, estado o prioridad. |

#### `com.atommanager.Main`

Punto de entrada de la aplicación (`main`). Es la única clase que conoce las implementaciones concretas: instancia los repositorios en memoria, los inyecta en los servicios correspondientes y arranca `MenuPrincipal`.

## Principios SOLID aplicados

| Principio | Aplicación en AtomManager |
| --- | --- |
| **S** — Responsabilidad única | `Tarea` solo representa datos; `TareaService` contiene las reglas; `TareaRepository` almacena; `ui` solo presenta. |
| **O** — Abierto/cerrado | Nuevas formas de ordenar tareas se agregan con un `Comparator<Tarea>` nuevo, sin tocar `TareaService`. |
| **L** — Sustitución de Liskov | Cualquier implementación de `TareaRepository` (memoria hoy, archivo mañana) reemplaza a otra sin romper el servicio. |
| **I** — Segregación de interfaces | `UsuarioRepository` y `TareaRepository` son interfaces pequeñas y separadas. |
| **D** — Inversión de dependencias | `TareaService` depende de la interfaz `TareaRepository`; la implementación concreta se inyecta desde `Main`. |

## Estructuras de datos

### `Map` / `HashMap` — almacén principal

| Estructura | Uso |
| --- | --- |
| `Map<String, Usuario>` | Acceso a un usuario por su ID en O(1). |
| `Map<String, Tarea>` | Acceso a una tarea por su ID en O(1). |
| `Map<String, List<String>>` | Índice de tareas asignadas por usuario. |
| `Map<Estado, List<Tarea>>` | Vista tipo tablero por estado. |

### `Queue` — ordenamiento por prioridad

Se utiliza `PriorityQueue` para construir la cola de atención de un usuario, de modo que sus tareas salgan ordenadas de mayor a menor prioridad:

```java
Queue<Tarea> colaAtencion = new PriorityQueue<>(
    Comparator.comparingInt(t -> t.getPrioridad().getPeso())
);
colaAtencion.addAll(tareasDelUsuario);
```

> `PriorityQueue` solo garantiza el orden al extraer con `poll()`; para listar las tareas ordenadas hay que vaciar la cola en una lista, no recorrerla directamente.

## Prioridades y estados de una tarea

**Prioridades** (cada tarea tiene exactamente una):

| Prioridad | Peso | Descripción |
| --- | --- | --- |
| 🔴 Alta | 1 | Actividad crítica, se atiende primero. |
| 🟡 Moderada | 2 | Importante, pero no urgente. |
| 🟢 Baja | 3 | Puede realizarse más adelante. |

**Estados** (ciclo de vida habitual `POR_REALIZAR → EN_PROCESO → FINALIZADA`, con posibilidad de retroceder):

| Estado | Constante enum | Descripción |
| --- | --- | --- |
| Por realizar | `POR_REALIZAR` | Registrada, aún no iniciada. |
| En proceso | `EN_PROCESO` | En desarrollo por su responsable. |
| Finalizada | `FINALIZADA` | Completada. |

## Alcance del proyecto

**Dentro del alcance:** usuarios (registrar y consultar); tareas (crear, editar, asignar responsable, prioridad y estado); consultas por usuario ordenadas por prioridad, por estado (tipo tablero) y por prioridad; e interfaz Swing / JOptionPane.

**Fuera del alcance (por ahora):** JavaFX, persistencia obligatoria en JSON, integración con Jira/Trello, app móvil, chat interno, notificaciones, calendario avanzado, autenticación externa, roles avanzados, API REST, bases de datos SQL y despliegue en la nube.

La persistencia de datos (guardar/cargar al cerrar la app) queda como funcionalidad **opcional**, a abordar solo si el resto del sistema está terminado y probado.

## Cómo compilar y ejecutar

Requisitos previos: **JDK 17** y **Maven** instalados.

```bash
# Compilar el proyecto
mvn compile

# Ejecutar la aplicación (punto de entrada: com.atommanager.Main)
mvn exec:java -Dexec.mainClass="com.atommanager.Main"

# Empaquetar en un .jar
mvn package
```

## Estructura del repositorio

> El código listado como "stub" ya existe como clase/interfaz vacía con Javadoc (documenta la responsabilidad y los miembros a implementar), pero todavía no tiene lógica. Ver estado real y detallado en [`docs/cambios.md`](docs/cambios.md).

```text
atom_manager/
├── docs/
│   ├── AtomManager2.1.md          → documento de diseño: épicas, historias de usuario, requisitos
│   └── cambios.md                 → registro histórico de cambios del proyecto
├── src/main/java/com/atommanager/
│   ├── Main.java                  → punto de entrada
│   ├── model/
│   │   ├── Usuario.java           → (stub) integrante del proyecto
│   │   ├── Tarea.java             → (stub) actividad del proyecto
│   │   ├── Prioridad.java         → (stub, enum) ALTA, MODERADA, BAJA
│   │   └── Estado.java            → (stub, enum) POR_REALIZAR, EN_PROCESO, FINALIZADA
│   ├── repository/
│   │   ├── UsuarioRepository.java         → (stub, interfaz) contrato de acceso a usuarios
│   │   ├── TareaRepository.java           → (stub, interfaz) contrato de acceso a tareas
│   │   ├── UsuarioRepositoryMemoria.java  → (stub) implementación en memoria con HashMap
│   │   └── TareaRepositoryMemoria.java    → (stub) implementación en memoria con HashMap
│   ├── service/
│   │   ├── UsuarioService.java    → (stub) reglas de negocio de usuarios
│   │   └── TareaService.java      → (stub) reglas de negocio de tareas y PriorityQueue
│   └── ui/
│       ├── MenuPrincipal.java     → (stub) menú principal (Swing / JOptionPane)
│       ├── VistaUsuarios.java     → (stub) pantallas de usuarios
│       └── VistaTareas.java       → (stub) pantallas de tareas
├── pom.xml
└── README.md
```

## Documentación

- [`docs/AtomManager2.1.md`](docs/AtomManager2.1.md): documento de diseño completo (épicas, historias de usuario, requisitos funcionales y no funcionales).
- [`docs/cambios.md`](docs/cambios.md): registro cronológico de los cambios realizados en el proyecto.

## Flujo de trabajo colaborativo

- Desarrollo mediante **ramas independientes** por funcionalidad (rama principal: `main`, rama de trabajo: `developer`).
- Historial de cambios siguiendo **Conventional Commits** (`feat:`, `fix:`, `docs:`, etc.).
- Repositorio alojado en GitHub: [CondezPancake/AtomManager](https://github.com/CondezPancake/AtomManager).
