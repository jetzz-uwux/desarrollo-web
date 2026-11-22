# Reporte de Avance del Proyecto

**Equipo:** 06  
**Fecha:** 20 de noviembre de 2026  
**Número de reporte:** R4 (Entrega 4 - Sistema integrado, pruebas y documentación)  
**Repositorio GitHub:** https://github.com/jetzz-uwux/desarrollo-web  

## 1. Actividades realizadas
- Construcción de la interfaz web en `frontend/index.html` y `frontend/css/styles.css` con formularios, selectores dinámicos, filtros y tablas responsivas para el módulo de Reseñas, Calificaciones y Estadísticas.
- Implementación del cliente de consumo HTTP mediante API Fetch (`frontend/js/api.js`) y lógica de control de vistas (`frontend/js/app.js`) con soporte para peticiones JSON y *Query Strings*.
- Implementación de notificaciones visuales (`#alert-box`) para mostrar mensajes de confirmación y manejo controlado de errores HTTP.
- Integración del frontend con el backend Spring Boot habilitando `@CrossOrigin(origins = "*")` en los controladores REST (`BookController`, `ReviewController` y `StatsController`).
- Ejecución de pruebas funcionales de extremo a extremo desde WSL y documentación del uso de IA Generativa.

## 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
| :--- | :--- | :--- |
| Desarrollo de interfaz (`index.html`, `styles.css`), funciones Fetch (`api.js`, `app.js`), manejo de alertas y configuración `@CrossOrigin` | Integrante 04 (Camila) | `7a722bb` |
| Desarrollo y unión de interfaces | Integrante 04 (Jetzaly) | https://github.com/jetzz-uwux/desarrollo-web/commit/f8fea70957634370692c4c3dbd804368b80372d6, https://github.com/jetzz-uwux/desarrollo-web/commit/16d5b4cfc9be6ee3f9fe07801a44fd598d2ea8ec |
| frontend coleccion/progreso| Integrante 03 (Juan) | 2643047 |
| Integración frontend-backend de Archivos Colaborativos y Títulos Alternativos (`index.html`, `api.js`, `archivos.js`), `@CrossOrigin` en controladores, pruebas en navegador y documentación IAG | Integrante 02 (Hans Arturo Martínez Romero) | https://github.com/jetzz-uwux/desarrollo-web/commits/main |

## 3. Problemas encontrados
1. **Conflicto de migración automática de esquema en Hibernate:** Al ejecutar `./mvnw spring-boot:run`, PostgreSQL rechazó un `ALTER TABLE` automático sobre `user_reading_progress.status` debido a la dependencia de la vista `v_book_read_counts`.  
   - **Solución:** Se configuró `spring.jpa.hibernate.ddl-auto=none` en `application.properties`, respetando el esquema previamente creado con `database/schema.sql`.
2. **Puerto del servidor backend y ruta raíz (`404`):** El servidor Tomcat de Spring Boot inició en el puerto `8081` y al ser una API REST sin vistas en `/`, devolvía `Whitelabel Error Page` al acceder directamente.  
   - **Solución:** Se levantó un servidor HTTP independiente para `frontend/` en el puerto `5500` y se apuntó `API_BASE_URL` a `http://localhost:8081`.
3. **Bloqueo de peticiones entre orígenes (`Cross-Origin`):** El navegador bloqueaba las peticiones `fetch` realizadas desde `localhost:5500` hacia `localhost:8081`.  
   - **Solución:** Se añadió la anotación `@CrossOrigin(origins = "*")` en `BookController.java`, `ReviewController.java` y `StatsController.java`.
4. **Fallo de autenticación en PostgreSQL al iniciar el backend (`FATAL: password authentication failed for user "booknest_user"`):** Al levantar el proyecto tras sincronizar el repositorio, Hibernate no pudo establecer la conexión JDBC con la base de datos local.  
   - **Solución:** Se verificaron las credenciales en `application.properties` y se alineó la contraseña/instancia activa de PostgreSQL en el puerto `5432` en WSL.

## 4. Avance funcional
- **Operativo:**
  - Carga dinámica del catálogo de libros en formularios y filtros (`GET /api/books`).
  - CRUD completo de reseñas (`GET`, `POST`, `PUT`, `DELETE` en `/api/reviews`) con filtrado por libro y usuario mediante *Query Strings*.
  - Registro, actualización y eliminación de calificaciones (`POST`, `PUT`, `DELETE` en `/api/stats/rating`) y consulta de promedios globales (`GET /api/stats/global`).
  - Consulta de ranking de libros más leídos (`GET /api/stats/most-read`) y estadísticas personales de lectura por usuario (`GET /api/stats/user/{userId}`).
  - Gestión completa de **Archivos Colaborativos** desde la interfaz web: subida de archivos PDF mediante `FormData` (`POST /api/files/upload`), validación de duplicados y autorización, filtrado por libro/idioma/nombre (`GET /api/files`), edición de metadatos (`PUT /api/files/{id}`), descarga de archivos autorizados (`GET /api/files/download/{id}`) y eliminación (`DELETE /api/files/{id}`).
  - Visualización de alertas de confirmación y error en pantalla.
- **Pendiente:**
- Generación del documento PDF final consolidado, actualización del `README.md` y grabación del video demostrativo.

## 5. Uso de IA generativa
- **Herramienta utilizada:** Google Gemini
- **Actividad para la que se utilizó:** Consulta y depuración de errores de despliegue en Spring Boot (`Whitelabel Error Page 404`), bloqueo entre orígenes (`@CrossOrigin`) y validación de estructura JSON para las entidades JPA `Review` y `Rating`.
- **Código o contenido generado:** Diagnóstico técnico y ejemplos de uso de `@CrossOrigin(origins = "*")` y estructura de objetos JSON anidados (`{ user: { id }, book: { id } }`).
- **Modificaciones realizadas por el equipo:** Se incorporó `@CrossOrigin(origins = "*")` en los controladores de libros, reseñas y estadísticas, y se ajustaron las rutas y payloads en `frontend/js/api.js` y `frontend/js/app.js`.
- **Forma en que se verificó el resultado:** Pruebas en navegador (`http://localhost:5500`) inspeccionando códigos de estado HTTP `200`/`201`/`204` en *DevTools (Network)* y verificando la persistencia en PostgreSQL. Evidencia detallada en `documentacion/IAG/Integrante 04/Uso_04/`.

- **Herramienta utilizada:** Claude
- **Actividad para la que se utilizó:** Consultas de errores y detalles, consulta de conceptos
- **Código o contenido generado:** Diagnostico de fallos, y conceptos generados de usos de templates
- **Modificaciones realizadas por el equipo:** Se integraron los templates
- **Forma en que se verificó el resultado:** Pruebas en navegador

- **Herramienta utilizada:** Google Gemini
- **Actividad para la que se utilizó:** Integración frontend-backend del módulo de Archivos Colaborativos y Títulos Alternativos (`fetch` con `FormData` y JSON), habilitación de `@CrossOrigin`, depuración de error de autenticación con PostgreSQL y diseño del plan de pruebas en navegador.
- **Código o contenido generado:** Funciones `fetch` para subida/descarga/edición/eliminación en `api.js`, lógica de formularios, tablas, filtros y notificaciones en `archivos.js`, y sección HTML integrada en `index.html`.
- **Modificaciones realizadas por el equipo:** Se integró la pestaña "Archivos" respetando los estilos de `styles.css`, se agregó `@CrossOrigin(origins = "*")` en `BookFileController` y `AlternativeTitleController`, y se ajustaron las credenciales de PostgreSQL local.
- **Forma en que se verificó el resultado:** Ejecución de pruebas de extremo a extremo en `http://127.0.0.1:5500/frontend/index.html` verificando alertas visuales, descargas de PDF y códigos HTTP (`200`, `201`, `204`, `400`, `409`) en la pestaña *Network* de *DevTools*. Evidencia detallada en `documentacion/IAG/Integrante 02/Uso_04/`.

## 6. Participación individual
- **Integrante 1(Jetzaly Josmery Tello Campos):** Front y backend enlazados y se adicionó el uso de templates para el apartado de catálogo y series, e implementación de funciones fetch.
- **Integrante 2 (Hans Arturo Martínez Romero):** Desarrollo e integración frontend-backend del módulo de Archivos Colaborativos y Títulos Alternativos. Implementación de formularios de subida de PDF (`multipart/form-data`), tablas con filtros dinámicos, descarga, edición y eliminación de archivos y títulos alternativos en `index.html`, `api.js` y `archivos.js`; manejo de notificaciones de confirmación/error, habilitación de `@CrossOrigin` en sus controladores y ejecución de pruebas funcionales en navegador.- **Integrante 3 (Juan Luis Gaona Aguirre):** Frontend HTML/JS de login y pagina de usuario, pagina de colecciones del usuario. Implementacion de funciones fetch para recabar datos del backend creado.
- **Integrante 4 (Camila Mariné Crespo Zurita):** Desarrollo de páginas, formularios y tablas en HTML/CSS (`index.html`, `styles.css`), implementación de funciones `fetch` (`api.js`, `app.js`), notificaciones de errores y confirmaciones, habilitación de `@CrossOrigin` e integración frontend-backend del módulo de reseñas, calificaciones y estadísticas.

## 7. Próximas actividades
- Integrar los módulos restantes en la interfaz web, como mostrar las colecciones ya guardadas.
- Completar el `README.md` con instrucciones de ejecución paso a paso en WSL.
- Redactar y exportar el documento `documentacion/proyecto.pdf` con portada, problemática, arquitectura y enlaces.
- Grabar y subir el video demostrativo en YouTube (máximo 15-20 minutos) y generar el *Release* final en GitHub.