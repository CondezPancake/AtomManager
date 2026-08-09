# Guía para quien siga con AtomManager

Este documento es para vos, si sos la próxima persona que va a tocar este proyecto. Está escrito en criollo, sin vueltas técnicas innecesarias, para que en una lectura entiendas qué hay hecho, qué falta y dónde tenés que meter mano.

Si en algún momento algo de acá no coincide con lo que ves en el código, hacele caso al código: este documento se puede desactualizar, el código no miente.

---

## 1. Qué es este proyecto, en una frase

Un gestor de tareas de escritorio (estilo Trello/Jira, pero chiquito): se registran personas, se crean tareas, se les asigna un responsable, una prioridad y un estado, y se pueden consultar de distintas formas.

---

## 2. Qué es lo que YA funciona hoy

Ya está hecho y probado lo siguiente:

- **Registrar personas** y **ver la lista de personas registradas**.
- **Crear una tarea** (con título, descripción y prioridad).
- **Asignar una tarea a una persona ya registrada** (si la persona no existe, avisa con un error en vez de romperse).
- **Cambiarle la prioridad a una tarea** (Alta, Moderada o Baja).
- **Cambiar el estado de una tarea** (Por realizar, En proceso o Finalizada).
- **Ver todas las tareas** que existen.
- **Ver las tareas de una persona ordenadas de la más urgente a la menos urgente**.

Todo esto tiene pruebas automáticas, y el proyecto compila sin errores.

**Importante:** todo lo anterior hoy solo se puede usar escribiendo código Java (por ejemplo, desde las pruebas). Todavía **no hay ninguna pantalla** para que una persona común use el programa haciendo clicks. Eso es lo primero que falta.

---

## 3. Qué falta por hacer

### 3.1. Ver las tareas agrupadas por estado

Una tarea ya puede pasar de "Por realizar" a "En proceso" o "Finalizada". Falta mostrar las tareas agrupadas por estado, como un tablero de tres columnas.

- Poder ver las tareas agrupadas por estado, como un tablero de tres columnas.

**Dónde tocar:**

1. Carpeta `repository`, archivos `TareaRepository.java` y `TareaRepositoryMemoria.java`: agregar una forma de buscar las tareas que están en un estado determinado.
2. Carpeta `service`, archivo `TareaService.java`: agregar la función que devuelve las tareas agrupadas por estado.
3. Carpeta `src/test/.../service`: agregar pruebas para lo nuevo, copiando el estilo de los métodos que ya están en `TareaServiceTest.java`.

### 3.2. Las pantallas del programa (para que se use sin programar)

Todo lo que funciona hoy, funciona "por dentro". Falta la parte visual:

- La ventana principal con el menú de opciones.
- La ventana para registrar y ver personas.
- La ventana para crear, asignar y consultar tareas.
- Conectar todo esto en el archivo `Main.java` para que, al ejecutar el programa, se abra el menú.

**Dónde tocar:**

- Carpeta `ui`: los archivos `MenuPrincipal.java`, `VistaUsuarios.java` y `VistaTareas.java` ya existen, pero están vacíos — solo tienen un comentario arriba explicando qué deberían hacer. Ahí es donde va el código real de las pantallas (con `JOptionPane`, que son esos cuadros de diálogo simples de Java).
- Archivo `Main.java` (en la carpeta `com.atommanager`, la de más arriba): hay que completar el método `main`. También tiene un comentario explicando, paso a paso, qué arma y en qué orden.
- **Regla importante:** las pantallas no tienen que decidir nada por su cuenta (por ejemplo, no tienen que fijarse si un ID ya existe). Solo piden datos, se los pasan a las clases de la carpeta `service`, y muestran lo que esas clases devuelven o el error que devuelven. Toda la parte "inteligente" ya está resuelta en `service`.

### 3.3. Guardar los datos (opcional, solo si sobra tiempo)

Ahora mismo, si cerrás el programa, se pierde todo lo que cargaste — se guarda solo en la memoria de la computadora mientras el programa está abierto. Guardar esa información en un archivo para que no se pierda es una mejora que **no es obligatoria**: se hace solo si el resto ya está terminado y probado.

---

## 4. Mapa rápido: dónde va cada cosa

Dentro de `src/main/java/com/atommanager` hay cuatro carpetas, cada una con un propósito bien puntual:

| Carpeta | Para qué es | Ejemplo de lo que hay |
| --- | --- | --- |
| `model` | Las "cosas" del sistema. Son como fichas de datos, sin reglas de negocio. | Una persona, una tarea, una prioridad. |
| `repository` | Dónde se guardan y se buscan esas fichas (hoy, solo en la memoria de la compu). | "Guardame esta tarea", "buscame la persona con tal ID". |
| `service` | Las reglas del negocio: qué se puede hacer y qué no. | "No dejes crear una tarea sin título", "no asignes una tarea a alguien que no existe". |
| `ui` | Las pantallas, lo que ve la persona que usa el programa. | El menú, el formulario para crear una tarea. |

Y afuera de esas cuatro carpetas está `Main.java`, que es el botón de arranque: junta todas las piezas y prende el programa.

**Regla para decidir dónde poner algo nuevo:** preguntate "¿esto es un dato, una regla de negocio, un lugar donde se guarda información, o una pantalla?" y ponelo en la carpeta que corresponda. Si dudás, mirá el archivo `package-info.java` que hay dentro de cada una de esas cuatro carpetas: cada uno explica, con ejemplos, qué va ahí y qué NO va ahí.

---

## 5. Cómo correr las pruebas

1. Abrí una terminal parado en la carpeta del proyecto (`atom_manager`, donde está el archivo `pom.xml`).
2. Escribí:
   ```
   mvn test
   ```
3. Si todo anda bien, vas a ver al final un mensaje `BUILD SUCCESS` y un resumen con la cantidad de pruebas que pasaron.
4. Si algo se rompió, vas a ver `BUILD FAILURE`, con el nombre de la prueba que falló y por qué.

**Cómo agregar una prueba nueva** (por ejemplo, para lo que hagas en el punto 3.1):

- Las pruebas viven en `src/test/java/com/atommanager/service/`.
- Ya hay dos archivos de ejemplo: `UsuarioServiceTest.java` y `TareaServiceTest.java`. La forma más fácil de arrancar es copiar un método parecido a lo que necesitás probar y adaptarlo.
- La receta de una prueba siempre es la misma: preparar la situación (por ejemplo, crear una tarea), hacer la acción que querés comprobar (por ejemplo, cambiarle el estado), y verificar que pasó lo que esperabas.

> Nota: en la máquina donde se armó este proyecto no estaba instalado el comando `mvn`, así que las pruebas se corrieron a mano con una herramienta descargada de internet. Si en tu máquina tenés Maven instalado (lo normal), con `mvn test` alcanza y sobra.

---

## 6. Antes de ponerte a programar

- Leé `docs/cambios.md`: ahí está anotado, en orden, todo lo que se hizo hasta ahora y por qué. Es la fuente de verdad de en qué estado está el proyecto.
- Leé `docs/AtomManager2.1.md`: ahí está el pedido original completo, con todas las épicas y requisitos.
- Si en paralelo hay otra persona (o "agente") tocando este mismo proyecto: **revisá los archivos y `docs/cambios.md` antes de dar por hecho en qué estado está todo**. Puede haber cambios recientes que este documento todavía no refleja.
- Cuando termines algo, anotalo en `docs/cambios.md` (qué hiciste y por qué), para que la próxima persona no tenga que adivinar.

---

## 7. Resumen para pegar en el pizarrón

- ✅ Personas: registrar y consultar — **hecho y probado**.
- ✅ Tareas: crear, asignar, poner prioridad, consultar — **hecho y probado**.
- ✅ Tareas: cambiar de estado — **hecho y probado**.
- ⬜ Tareas: verlas agrupadas por estado — **falta**.
- ⬜ Pantallas del programa y arranque (`ui` + `Main.java`) — **falta**.
- ⬜ Guardar los datos en un archivo — **falta, no es obligatorio**.
