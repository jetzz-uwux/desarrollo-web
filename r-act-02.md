# Reporte de Avance del Proyecto

**Equipo:**  06
**Fecha:**  13 de noviembre de 2026
**Número de reporte:**  02 - Backend básico
**Repositorio GitHub:**  desarrollo-web


---

# 1. Actividades realizadas

### Nuevo
- Spring boot: Se descargó desde la pagina de spring el cuerpo del proyecto, con su archivo `pom.xml` configurado con las dependencias necesarias para el proyecto
- Configuración del `application.properties` tanto para la base como para que el servidor spring boot corra en el puerto `8081`
- Entidades y repositorios: Se configuraron clases `@Entity` e interfaz implementada: `JpaRepository`
- POST y PUT implementados
- Documentación de funcionamiento con `CURL` 

### Modificaciones
- Configuración de límites de carga (`spring.servlet.multipart.max-file-size=50MB` y `max-request-size=50MB`) en `application.properties` para permitir la subida de libros en formato PDF

---

# 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
|------------|------------------------|-----------------|
| Archivos base genericos - Catálogo| Jetzaly Josmery Tello Campos| https://github.com/jetzz-uwux/desarrollo-web/commit/7ab4aed25a7b8439e028a9739f3d58ff1cf1d9ef|
| Implementación de base de Spring| Jetzaly Josmery Tello Campos|https://github.com/jetzz-uwux/desarrollo-web/commit/99f61b9557bc9f8f3996b96429b6ed4ad6c333ad|
| Crear entidades y repositorios - Catálogo|Jetzaly Josmery Tello Campos|https://github.com/jetzz-uwux/desarrollo-web/commit/9a342dfbbe6bda6589dd5cd0de347797226c765d|
| Implementar POST y GET - Catalogo|Jetzaly Josmery Tello Campos|https://github.com/jetzz-uwux/desarrollo-web/commit/dd91bb467b31ca28ef31981ae5a603332873cd63|
| Documentacion de resultados CURL - catálogo|Jetzaly Josmery Tello Campos|https://github.com/jetzz-uwux/desarrollo-web/commit/5c19d8fd8e30768fe40eabff94cc42efa6eba392|
|Documentación IA |Jetzaly Josmery Tello Campos |https://github.com/jetzz-uwux/desarrollo-web/commit/11871182f34f13d85134bd8fc380b869817454c6|
|Crear entidades y repositorios - Biblioteca |Juan Luis Gaona Aguirre |https://github.com/jetzz-uwux/desarrollo-web/commit/2600575a1eb3dacf192b98e2648021b5f4032a92 |
| Crear entidades y repositorios - Reseñas/Stats | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/b9d0510 |
| Implementar POST y GET - Reseñas/Stats | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/b9d0510 |
| Script de base de datos para pruebas locales | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/b9d0510 |
| Documentación CURL y uso de IA - Reseñas | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/50c7ea3 |
| Crear entidades y repositorios - Archivo colaborativo | Hans Arturo Martínez Romero | https://github.com/jetzz-uwux/desarrollo-web/commits/main |
| Implementar POST y GET - Archivo colaborativo | Hans Arturo Martínez Romero | https://github.com/jetzz-uwux/desarrollo-web/commits/main |
| Documentación CURL y uso de IA - Archivo colaborativo | Hans Arturo Martínez Romero | https://github.com/jetzz-uwux/desarrollo-web/commits/main |


---

# 3. Problemas encontrados

Describir los problemas técnicos encontrados y cómo fueron solucionados.
- **Módulo de Reseñas:** Al realizar las pruebas CURL (POST), el servidor devolvía un error 500 debido a una violación de restricción NOT NULL en las columnas `created_at` y `updated_at`. Se solucionó configurando estas columnas con `@Column(insertable = false, updatable = false)` para que PostgreSQL pueda asignar los timestamps automáticamente mediante la base de datos.
- **Módulo de Archivos Colaborativos:** 
  1. Al compilar con `./mvnw spring-boot:run` en WSL, se presentó un error por falta de definición de la variable `JAVA_HOME` en el entorno Linux, solucionándose al instalar `openjdk-17-jdk` y exportar la ruta en `~/.bashrc`.
  2. Durante las pruebas iniciales `POST /api/files/upload`, el servidor devolvió `404 Not Found ("El libro especificado no existe")` porque la base de datos local solo tenía ejecutado el esquema sin los registros base; se resolvió cargando `datosPruebas.sql` en PostgreSQL.
  3. Se presentó un conflicto de fusión (`merge conflict`) en `application.properties` al sincronizar cambios remotos, el cual se resolvió integrando tanto las credenciales del equipo como los parámetros `multipart` para carga de archivos.
---

# 4. Avance funcional

Indicar qué funcionalidades ya se encuentran operativas y cuáles están pendientes.
- **Operativas:** Entidades, repositorios y endpoints básicos (POST y GET) para los módulos de Catálogo, Reseñas y Estadísticas. Configuración de base de datos y script de pruebas locales.
- **Pendientes:** Endpoints de los módulos de Archivos Colaborativos y Biblioteca Personal. Implementación de métodos PUT, DELETE, búsquedas complejas (filtros) e integración con el Frontend.
---

# 5. Uso de IA generativa

Documentar el uso de herramientas de inteligencia artificial durante el desarrollo del proyecto.

- **Herramienta utilizada:** Claude
- **Actividad para la que se utilizó:** Pruebas CURL
- **Código o contenido generado:** Tabla de pruebas
- **Modificaciones realizadas por el equipo:** Separación de algunas tareas
- **Forma en que se verificó el resultado:** Revisando detalladamente la tabla

---

- **Herramienta utilizada:** Gemini 
- **Actividad para la que se utilizó:** Revisión de mensaje de error en BD
- **Código o contenido generado:** Corrección en application.properties
- **Modificaciones realizadas por el equipo:** Corrección de credenciales
- **Forma en que se verificó el resultado:** Ejecución del programa (sin error)

---

- **Herramienta utilizada:** Gemini
- **Actividad para la que se utilizó:** Generación de pruebas CURL y Script de Base de Datos.
- **Código o contenido generado:** Comandos CURL anidados para probar relaciones `@ManyToOne` y script SQL (`datosPruebas.sql`).
- **Modificaciones realizadas por el equipo:** Ajuste de los JSON en los comandos para coincidir con los IDs del script SQL y estructuración en Markdown.
- **Forma en que se verificó el resultado:** Ejecutando las pruebas CURL en la terminal WSL y verificando que el servidor devolviera los códigos HTTP 201 y 200.

---

- **Herramienta utilizada:** Gemini
- **Actividad para la que se utilizó:** Diseño de batería de pruebas CURL (casos de éxito y fallos controlados) para el módulo de Archivos Colaborativos.
- **Código o contenido generado:** Matriz de pruebas funcionales y negativas (`multipart/form-data` para subida de PDFs, validación de duplicados SHA-256, descarga binaria y títulos alternativos).
- **Modificaciones realizadas por el equipo:** Adaptación de las rutas de archivos locales en WSL y parámetros de prueba (`bookId=1`, `uploaderUserId=3`).
- **Forma en que se verificó el resultado:** Ejecución directa de los comandos `curl` contra el puerto `8081` y verificación de los códigos `201 Created`, `200 OK`, `400 Bad Request`, `404 Not Found` y `409 Conflict`, documentados en `curl-archivos.md`.

---

# 6. Participación individual

Describir brevemente la contribución de cada integrante.

- **Jetzaly Josmery Tello Campos:** Configuración de Spring, Métodos GET y POST del módulo de catálogo, que abarca clases `Book`, `Author`, `LiterarySeries` y `Genre`, con sus respectivos service, repository y controllers. 
- **Integrante 2:** 
- **Juan Luis Gaona Aguirre:** Configuración de Spring, Métodos GET y POST del módulo de biblioteca, que abarca clases `CollectionBooks`, `UserCollection` y `UserReadingProgress`, con sus respectivos service, repository y controllers. 
- **Camila Mariné Crespo Zurita:** Desarrollo del módulo de Reseñas y Estadísticas (`Review` y `Rating`). Configuración de persistencia y relaciones `@ManyToOne` con `User` y `Book`. Proyección nativa en JPA para calcular promedios globales (para el RF-16). Creación del archivo `datosPruebas.sql` para homologar el entorno del equipo y documentación de pruebas CURL.
- **Hans Arturo Martínez Romero:** Desarrollo del módulo de Archivos Colaborativos (`BookFile` y `AlternativeTitle`), implementando sus entidades, repositorios, servicios y controladores REST (`POST /api/files/upload`, `GET /api/files/download/{id}`, `GET /api/files/book/{bookId}` y endpoints de títulos alternativos). Validación de integridad y seguridad de archivos PDF (cabecera binaria real, límite de peso, detección de duplicados mediante SHA-256 e ISBN) y documentación de pruebas CURL y uso de IA.

---

# 7. Próximas actividades

Indicar las tareas que se realizarán antes del siguiente reporte.

- Implementación de PUT y DELETE
- Incorporación de filtros (q) 
- Validación respuestas HTTP
- Integrar observaciones del profesor si es que hay retroalimentación al respecto
- Probar persistencia de datos

---

## Observaciones adicionales

Agregar cualquier comentario, acuerdo, decisión técnica o evidencia complementaria relevante para el proyecto.
