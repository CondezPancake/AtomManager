# AtomManager — Gestor de tareas

## 1. Descripción del proyecto

El proyecto consiste en desarrollar una aplicación de escritorio para la **gestión y seguimiento de tareas**, tomando como referencia herramientas de organización como Jira y Trello.

La aplicación permitirá crear usuarios, crear tareas, asignarlas, establecer prioridades y controlar su estado durante el desarrollo de un proyecto.

La interfaz se implementó en tres etapas incrementales — terminal, `JOptionPane` y Swing (con FlatLaf) — y finalmente en **JavaFX**, que quedó como la interfaz definitiva del proyecto (ver nota al final de esta sección). La lógica de negocio se implementa en **Java puro**, siguiendo el paradigma de **Programación Orientada a Objetos** y los **principios SOLID**.

> **Nota (post-alcance original):** el enunciado permitía explícitamente Swing/JOptionPane y excluía JavaFX por el costo de configuración adicional (sección 9.2 original). Una vez armado el proyecto, se decidió agregar JavaFX igual, como mejora de experiencia de usuario, ya con la lógica de negocio (Épicas 1 a 3) completamente implementada y probada — el costo de configuración ya no era un riesgo para cumplir el plazo. La sección 9.2 se actualizó para reflejar esta decisión; el resto del documento (épicas, requisitos, estructuras de datos) no cambió.

La gestión de datos en memoria se realiza con estructuras **`Map` / `HashMap`** como almacén principal, y con la interfaz **`Queue`** (implementada mediante `PriorityQueue`) como mecanismo de ordenamiento por prioridad de las tareas.

El desarrollo será realizado colaborativamente mediante **Git y GitHub**, utilizando ramas, control de versiones y **Conventional Commits**.

---

## 2. Objetivo general

Desarrollar un gestor de tareas que permita a un equipo de trabajo organizar, asignar, priorizar y realizar seguimiento de las actividades de un proyecto, aplicando POO, principios SOLID y las estructuras de datos `Map`/`HashMap` y `Queue`.

### 2.1 Objetivos específicos

* Crear usuarios dentro del sistema.
* Crear y administrar tareas.
* Asignar tareas a usuarios.
* Definir prioridades para las tareas (Alta, Moderada, Baja).
* Gestionar el estado de las tareas (Por realizar, En proceso, Finalizada).
* Consultar las tareas asignadas a cada usuario **ordenadas por prioridad**.
* Visualizar las tareas agrupadas por estado.
* Utilizar `Map` y `HashMap` como almacén principal de datos en memoria.
* Utilizar la interfaz `Queue` (`PriorityQueue`) para el ordenamiento por prioridad.
* Modelar el sistema aplicando POO (encapsulamiento, abstracción, enums, interfaces).
* Aplicar los principios SOLID en la separación de responsabilidades.
* Desarrollar la interfaz gráfica con Swing / JOptionPane.
* Utilizar GitHub para el trabajo colaborativo con Conventional Commits.

---

## 3. Arquitectura y aplicación de POO / SOLID

### 3.1 Organización por paquetes

```text
com.atommanager
├── model        → Usuario, Tarea, Prioridad (enum), Estado (enum)
├── repository   → UsuarioRepository, TareaRepository (interfaces)
│                  UsuarioRepositoryMemoria, TareaRepositoryMemoria (impl. con HashMap)
├── service      → UsuarioService, TareaService (reglas de negocio y colas de prioridad)
├── ui           → MenuPrincipal, VistaUsuarios, VistaTareas (JOptionPane / Swing)
└── Main.java    → punto de entrada, arma las dependencias
```

### 3.2 Principios SOLID aplicados

| Principio | Aplicación concreta en AtomManager |
| --------- | ---------------------------------- |
| **S** — Responsabilidad única | `Tarea` solo representa datos; `TareaService` contiene reglas; `TareaRepository` almacena; la capa `ui` solo presenta. |
| **O** — Abierto/cerrado | Agregar una nueva forma de ordenar tareas se hace con un nuevo `Comparator<Tarea>`, sin modificar `TareaService`. |
| **L** — Sustitución de Liskov | Cualquier implementación de `TareaRepository` (memoria hoy, archivo mañana) puede reemplazar a otra sin romper el servicio. |
| **I** — Segregación de interfaces | `UsuarioRepository` y `TareaRepository` son interfaces pequeñas y separadas, no una única interfaz genérica. |
| **D** — Inversión de dependencias | `TareaService` depende de la **interfaz** `TareaRepository`, y la implementación concreta se inyecta desde `Main`. |

### 3.3 Conceptos de POO usados

* **Encapsulamiento:** atributos privados con getters/setters y validaciones en los setters.
* **Abstracción:** interfaces de repositorio que ocultan cómo se almacenan los datos.
* **Enumeraciones:** `Prioridad` y `Estado` como `enum`, evitando el uso de cadenas sueltas.
* **Polimorfismo:** uso de `Comparator<Tarea>` y de distintas implementaciones de repositorio.
* **Composición:** una `Tarea` referencia al `Usuario` responsable.

---

## 4. Estructuras de datos requeridas

### 4.1 `Map` / `HashMap` — almacén principal

| Estructura | Declaración | Uso |
| ---------- | ----------- | --- |
| Usuarios | `Map<String, Usuario> usuarios = new HashMap<>();` | Acceso a un usuario por su ID en O(1). |
| Tareas | `Map<String, Tarea> tareas = new HashMap<>();` | Acceso a una tarea por su ID en O(1). |
| Tareas por usuario | `Map<String, List<String>> tareasPorUsuario = new HashMap<>();` | Índice de los IDs de tareas asignadas a cada usuario. |
| Tareas por estado | `Map<Estado, List<Tarea>> tareasPorEstado = new HashMap<>();` | Vista tipo tablero (Por realizar / En proceso / Finalizada). |

### 4.2 `Queue` — ordenamiento por prioridad

La interfaz `Queue` se utiliza mediante `PriorityQueue` para construir la **cola de atención** de un usuario: al consultar sus tareas, estas salen ordenadas de mayor a menor prioridad.

```java
public enum Prioridad {
    ALTA(1), MODERADA(2), BAJA(3);
    private final int peso;
    Prioridad(int peso) { this.peso = peso; }
    public int getPeso() { return peso; }
}

// Cola de prioridad: menor peso = mayor urgencia
Queue<Tarea> colaAtencion = new PriorityQueue<>(
    Comparator.comparingInt(t -> t.getPrioridad().getPeso())
);
colaAtencion.addAll(tareasDelUsuario);
```

**Advertencia técnica importante:** `PriorityQueue` **solo garantiza el orden al extraer** con `poll()`. Recorrerla con un `for` o imprimirla directamente **no muestra el orden correcto**. Para listar las tareas ordenadas hay que vaciar la cola:

```java
List<Tarea> ordenadas = new ArrayList<>();
while (!colaAtencion.isEmpty()) {
    ordenadas.add(colaAtencion.poll());   // sale ALTA, luego MODERADA, luego BAJA
}
```

Adicionalmente puede mantenerse una cola por prioridad para la vista general:

```java
Map<Prioridad, Queue<Tarea>> colasPorPrioridad = new HashMap<>();
```

---

## 5. Épicas e historias de usuario

### Épica 1 — Gestión de usuarios

**Objetivo:** Permitir administrar los usuarios que participan en el proyecto.

* **HU-01 — Registrar usuario:** Como administrador del proyecto, quiero registrar un usuario para poder asignarle tareas.
  * *Criterio de aceptación:* el usuario se almacena en el `HashMap` con un ID único; si el ID ya existe, se muestra un mensaje de error.
* **HU-02 — Consultar usuarios:** Como usuario, quiero consultar los usuarios registrados para identificar los integrantes disponibles del proyecto.
  * *Criterio de aceptación:* se listan todos los usuarios del `Map` con ID y nombre.
* **HU-03 — Consultar mis tareas por prioridad:** Como usuario, quiero consultar las tareas que tengo asignadas ordenadas por prioridad para saber qué debo atender primero.
  * *Criterio de aceptación:* las tareas se extraen de una `PriorityQueue` y se muestran en orden Alta → Moderada → Baja.

### Épica 2 — Gestión de tareas

**Objetivo:** Permitir crear y administrar las tareas del proyecto.

* **HU-04 — Crear tarea:** Como usuario, quiero crear una tarea indicando su título y descripción para registrar una actividad del proyecto.
  * *Criterio de aceptación:* no se permite guardar una tarea sin título; se genera un ID único automáticamente.
* **HU-05 — Asignar tarea:** Como responsable del proyecto, quiero asignar una tarea a un usuario para establecer quién será responsable de realizarla.
  * *Criterio de aceptación:* solo se permite asignar a usuarios existentes en el `Map` de usuarios.
* **HU-06 — Establecer prioridad:** Como usuario, quiero definir la prioridad de una tarea para identificar qué actividades requieren mayor atención.
  * *Prioridades disponibles:* Alta, Moderada, Baja (enum `Prioridad`).
* **HU-07 — Consultar tareas:** Como usuario, quiero consultar todas las tareas existentes para conocer las actividades registradas en el proyecto.

### Épica 3 — Gestión del estado de las tareas

**Objetivo:** Permitir controlar el progreso de las tareas.

* **HU-08 — Cambiar estado:** Como usuario, quiero cambiar el estado de una tarea para actualizar el progreso de la actividad.
  * *Estados disponibles:* Por realizar, En proceso, Finalizada (enum `Estado`).
* **HU-09 — Consultar tareas por estado:** Como usuario, quiero visualizar las tareas agrupadas por estado para conocer el progreso general del proyecto.
  * *Criterio de aceptación:* se muestra un listado tipo tablero con las tres columnas de estado.
* **HU-10 — Consultar tareas por prioridad:** Como usuario, quiero visualizar todas las tareas ordenadas por prioridad para conocer las actividades críticas del proyecto.

### Épica 4 — Colaboración y control de versiones

**Objetivo:** Organizar el desarrollo colaborativo del proyecto.

* **HU-11 — Control de versiones:** Como desarrollador, quiero utilizar Git y GitHub para mantener un historial de cambios del proyecto.
* **HU-12 — Trabajo mediante ramas:** Como desarrollador, quiero trabajar mediante ramas independientes para evitar conflictos entre los cambios de los integrantes.
* **HU-13 — Conventional Commits:** Como desarrollador, quiero utilizar Conventional Commits para identificar claramente el propósito de cada cambio realizado.

### Épica 5 — Persistencia *(opcional / solo si sobra tiempo)*

* **HU-14 — Guardar y recuperar información:** Como usuario, quiero que las tareas y usuarios se conserven al cerrar la aplicación.
  * *Nota:* esta épica **no forma parte del alcance mínimo** exigido por el enunciado y solo se aborda si el resto está terminado y probado.

---

## 6. Requisitos funcionales

| ID    | Requisito                                                    | Prioridad |
| ----- | ------------------------------------------------------------ | --------- |
| RF-01 | El sistema debe permitir registrar usuarios.                 | Alta |
| RF-02 | El sistema debe permitir consultar los usuarios registrados. | Alta |
| RF-03 | El sistema debe permitir crear tareas.                       | Alta |
| RF-04 | Cada tarea debe tener un identificador único.                | Alta |
| RF-05 | Cada tarea debe tener título y descripción.                  | Alta |
| RF-06 | El sistema debe permitir asignar una tarea a un usuario existente. | Alta |
| RF-07 | El sistema debe permitir establecer una prioridad para cada tarea. | Alta |
| RF-08 | El sistema debe manejar las prioridades Alta, Moderada y Baja mediante un `enum`. | Alta |
| RF-09 | El sistema debe permitir cambiar el estado de una tarea.     | Alta |
| RF-10 | El sistema debe manejar los estados Por realizar, En proceso y Finalizada mediante un `enum`. | Alta |
| RF-11 | El sistema debe permitir consultar las tareas asignadas a un usuario, ordenadas por prioridad. | Alta |
| RF-12 | El sistema debe permitir visualizar las tareas agrupadas por estado. | Alta |
| RF-13 | El sistema debe permitir visualizar todas las tareas ordenadas por prioridad. | Alta |
| RF-14 | El sistema debe mostrar el usuario responsable de cada tarea. | Alta |
| RF-15 | El sistema debe validar los datos obligatorios antes de crear una tarea. | Media |
| RF-16 | El sistema debe permitir actualizar la información de una tarea. | Media |
| RF-17 | El sistema debe proporcionar una interfaz gráfica basada en JOptionPane / Swing. | Alta |
| RF-18 | El sistema debe permitir guardar y cargar la información almacenada. | Opcional |

---

## 7. Requisitos no funcionales

| ID     | Requisito                                                    |
| ------ | ------------------------------------------------------------ |
| RNF-01 | El sistema debe ser desarrollado utilizando Java.            |
| RNF-02 | La interfaz gráfica debe desarrollarse con Swing / JOptionPane; opcionalmente puede usarse FlatLaf como librería de diseño. |
| RNF-03 | La aplicación debe seguir el paradigma de Programación Orientada a Objetos. |
| RNF-04 | La aplicación debe respetar los principios SOLID en la separación de capas. |
| RNF-05 | El sistema debe utilizar `Map` / `HashMap` como almacén principal de datos en memoria. |
| RNF-06 | El sistema debe utilizar la interfaz `Queue` (`PriorityQueue`) para el manejo de prioridades. |
| RNF-07 | La aplicación debe ser ejecutable como aplicación de escritorio. |
| RNF-08 | La interfaz debe ser sencilla e intuitiva.                   |
| RNF-09 | El sistema debe proporcionar mensajes de error cuando se ingresen datos inválidos. |
| RNF-10 | Los identificadores de usuarios y tareas deben ser únicos.   |
| RNF-11 | El proyecto debe utilizar Git para el control de versiones.  |
| RNF-12 | El código fuente debe mantenerse en un repositorio de GitHub. |
| RNF-13 | Los commits deben seguir la convención Conventional Commits. |
| RNF-14 | El código debe mantenerse organizado y permitir futuras modificaciones. |
| RNF-15 | El proyecto debe ser desarrollable en 2 días por un equipo con 3 semanas de experiencia en Java. |

---

### 8. Clases principales

| Clase                 | Tipo     | Responsabilidad                        | Atributos / miembros clave                                   |
| --------------------- | -------- | -------------------------------------- | ------------------------------------------------------------ |
| `Usuario`             | Clase    | Representar un integrante del proyecto | `id`, `nombre`                                               |
| `Tarea`               | Clase    | Representar una actividad del proyecto | `id`, `titulo`, `descripcion`, `prioridad`, `estado`, `responsable` |
| `Prioridad`           | `enum`   | Definir los niveles de prioridad       | `ALTA(1)`, `MODERADA(2)`, `BAJA(3)` con etiqueta e ícono     |
| `Estado`              | `enum`   | Definir el ciclo de vida de la tarea   | `POR_REALIZAR`, `EN_PROCESO`, `FINALIZADA`                   |
| `Repositorio<T>`      | Interfaz | Contrato de persistencia               | `guardar(List<T>)`, `cargar()`                               |
| `RepositorioTareaCSV` | Clase    | Implementar persistencia en archivo    | implementa `Repositorio<Tarea>`                              |
| `GestorUsuarios`      | Clase    | Lógica de negocio de usuarios          | `Map<Integer, Usuario>`                                      |
| `GestorTareas`        | Clase    | Lógica de negocio de tareas            | `Map<Integer, Tarea>`, filtros por estado, prioridad y usuario |
| `MenuPrincipal`       | Clase    | Interacción con el usuario (Swing)     | no contiene lógica de negocio                                |

### 

---

## 9. Alcance (Scope)

### 9.1 Dentro del alcance

* **Usuarios:** crear, consultar y asignar tareas.
* **Tareas:** crear, editar, asignar responsable, prioridad y estado.
* **Prioridades:** Alta, Moderada y Baja.
* **Estados:** Por realizar, En proceso y Finalizada.
* **Visualización:** listado general, tareas por usuario ordenadas por prioridad, tareas por estado y tareas por prioridad.
* **Estructuras:** `Map`, `HashMap` y `Queue` / `PriorityQueue`.
* **Tecnología:** Java, JavaFX (interfaz definitiva; se pasó por terminal, JOptionPane y Swing/FlatLaf en el camino), Git, GitHub y Conventional Commits.

### 9.2 Fuera del alcance

Para mantener el proyecto dentro de los 2 días disponibles, no se incluirán:

* **Persistencia obligatoria en JSON** (requiere librería externa tipo Gson/Jackson; queda como funcionalidad opcional).
* Integración con APIs externas de Jira o Trello.
* Aplicación móvil.
* Sistema de chat o mensajes internos.
* Notificaciones por correo electrónico o push.
* Calendario avanzado y archivos adjuntos.
* Autenticación externa (OAuth/Google) y recuperación de contraseña.
* Sistema avanzado de roles y permisos.
* Microservicios, API REST, bases de datos SQL y despliegue en cloud.

---

## 10. Prioridades

Cada tarea deberá tener exactamente una prioridad.

| Prioridad | Peso | Descripción                                                  |
| --------- | ---- | ------------------------------------------------------------ |
| Alta      | 1    | Actividad crítica que debe atenderse con prioridad.          |
| Moderada  | 2    | Actividad importante que debe realizarse, pero no es urgente. |
| Baja      | 3    | Actividad de menor prioridad que puede realizarse posteriormente. |

**Ejemplo de representación:**

```text
🔴 ALTA     - Corregir error crítico del sistema
🟡 MODERADA - Crear pantalla de usuarios
🟢 BAJA     - Mejorar diseño visual
```

---

## 11. Estados

| Estado | Constante enum | Descripción |
| ------ | -------------- | ----------- |
| Por realizar | `POR_REALIZAR` | Tarea registrada que aún no ha iniciado. |
| En proceso   | `EN_PROCESO`   | Tarea en desarrollo por su responsable. |
| Finalizada   | `FINALIZADA`   | Tarea completada. |

Transición habitual: `POR_REALIZAR → EN_PROCESO → FINALIZADA`, permitiendo retroceder si el usuario lo requiere.
