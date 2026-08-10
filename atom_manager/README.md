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

- Crear y consultar usuarios.
- Crear y administrar tareas (título, descripción, responsable, prioridad, estado).
- Asignar tareas a usuarios existentes.
- Consultar las tareas de un usuario ordenadas por prioridad.
- Visualizar tareas agrupadas por estado (tipo tablero).
- Aplicar POO y SOLID en el diseño del sistema.

## Tecnologías

| Categoría | Tecnología |
| --- | --- |
| Lenguaje | Java 21 |
| Build tool | Maven |
| Interfaz gráfica | Swing / JOptionPane (opcionalmente [FlatLaf](https://www.formdev.com/flatlaf/)) |
| Estructuras de datos | `HashMap`, `Queue` (`PriorityQueue`) |
| Control de versiones | Git + GitHub, [Conventional Commits](https://www.conventionalcommits.org/) |

## Arquitectura

Organización por paquetes según responsabilidad (separación en capas: modelo, persistencia, lógica de negocio e interfaz):

```text
com.atommanager
├── model        → Usuario, Tarea, Prioridad (enum), Estado (enum)
├── repository   → UsuarioRepository, TareaRepository (interfaces)
│                  UsuarioRepositoryMemoria, TareaRepositoryMemoria (impl. con HashMap)
├── service      → UsuarioService, TareaService (reglas de negocio y colas de prioridad)
├── ui           → MenuPrincipal, VistaUsuarios, VistaTareas (JOptionPane / Swing)
└── Main.java    → punto de entrada, arma las dependencias
```

### Clases principales

| Clase / interfaz | Paquete | Responsabilidad |
| --- | --- | --- |
| `Usuario` | `model` | Representar un integrante del proyecto (`id`, `nombre`). |
| `Tarea` | `model` | Representar una actividad (`id`, `titulo`, `descripcion`, `prioridad`, `estado`, `responsable`). |
| `Prioridad` | `model` (enum) | `ALTA(1)`, `MODERADA(2)`, `BAJA(3)`. |
| `Estado` | `model` (enum) | `POR_REALIZAR`, `EN_PROCESO`, `FINALIZADA`. |
| `UsuarioRepository` / `TareaRepository` | `repository` | Contrato de acceso a datos. |
| `UsuarioRepositoryMemoria` / `TareaRepositoryMemoria` | `repository` | Implementación en memoria con `HashMap`. |
| `UsuarioService` / `TareaService` | `service` | Reglas de negocio y ordenamiento por prioridad. |
| `MenuPrincipal`, `VistaUsuarios`, `VistaTareas` | `ui` | Presentación con Swing / JOptionPane, sin lógica de negocio. |

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

**Dentro del alcance:** CRUD de usuarios y tareas, asignación de responsables, prioridades, estados, vistas por usuario/estado/prioridad, e interfaz Swing / JOptionPane.

**Fuera del alcance (por ahora):** JavaFX, persistencia obligatoria en JSON, integración con Jira/Trello, app móvil, chat interno, notificaciones, calendario avanzado, autenticación externa, roles avanzados, API REST, bases de datos SQL y despliegue en la nube.

La persistencia de datos (guardar/cargar al cerrar la app) queda como funcionalidad **opcional**, a abordar solo si el resto del sistema está terminado y probado.

## Cómo compilar y ejecutar

Requisitos previos: **JDK 21** y **Maven** instalados.

```bash
# Compilar el proyecto
mvn compile

# Ejecutar la aplicación (punto de entrada: com.atommanager.Main)
mvn exec:java -Dexec.mainClass="com.atommanager.Main"

# Empaquetar en un .jar
mvn package
```

## Estructura del repositorio

```text
atom_manager/
├── docs/
│   ├── AtomManager2.1.md   → documento de diseño: épicas, historias de usuario, requisitos
│   └── cambios.md          → registro histórico de cambios del proyecto
├── src/main/java/com/atommanager/
│   └── Main.java           → punto de entrada
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
