# Registro de Uso de IA Generativa - Sesión 04 (Integrante 04 - Camila)

| Concepto | Descripción |
| :--- | :--- |
| **Herramienta** | Google Gemini |
| **Modelo / Temperatura** | Gemini |
| **Prompt original** | Consulta sobre el error `Whitelabel Error Page (404)` en `localhost:8081`, el fallo de conexión `Failed to fetch` / `CORS error` al enlazar `frontend/js/api.js` con los endpoints de Spring Boot, y verificación de la estructura JSON esperada por los modelos `Review` y `Rating` para corregir errores de enlace entre capas. |
| **Respuesta de IA** | Explicación de la separación de puertos entre el servidor estático del frontend (`5500`) y Tomcat/Spring Boot (`8081`), indicación de agregar `@CrossOrigin(origins = "*")` en `BookController.java`, `ReviewController.java` y `StatsController.java`, y revisión del mapeo de objetos anidados (`{ user: { id }, book: { id } }`) en `api.js` y `app.js`. |
| **Código anterior** | Commit `6f453ac` |
| **Código posterior** | Commit `7a722bb` |
| **Diferencias entre versiones** | `git diff --numstat 6f453ac 7a722bb` (ver salida abajo) |
| **Prueba de funcionamiento** | Reinicio del backend en WSL (`./mvnw spring-boot:run`), recarga de `http://localhost:5500`, verificación de estado HTTP `200` en *Network* para `/api/books` y `/api/reviews`, y prueba funcional de inserción, consulta, edición y eliminación de reseñas y calificaciones. |
| **Código de uso** | Depuración / Explicación / Revisión |
| **Reflexión del estudiante** | Ver archivo `reflexion.md` en esta carpeta. |

## Salida de `git diff --numstat 6f453ac 7a722bb`

```text
3	2	backend/src/main/java/mx/uv/booknest/controllers/BookController.java
2	0	backend/src/main/java/mx/uv/booknest/controllers/ReviewController.java
2	0	backend/src/main/java/mx/uv/booknest/controllers/StatsController.java
1	1	backend/src/main/resources/application.properties
234	0	frontend/css/styles.css
228	0	frontend/index.html
77	0	frontend/js/api.js
297	0	frontend/js/app.js