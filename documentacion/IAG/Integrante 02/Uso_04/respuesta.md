**Herramienta:** Gemini
**Respuesta obtenida:**
1. **CORS en Backend:** Se añadió `@CrossOrigin(origins = "*")` en `BookFileController.java` y `AlternativeTitleController.java` para permitir peticiones desde el servidor frontend (`127.0.0.1:5500`) hacia Spring Boot (`localhost:8081`).
2. **Funciones Fetch (`frontend/js/api.js`):** Se agregaron métodos para listar con filtros (`getFiles`, `getAlternativeTitles`), subir archivos PDF mediante `FormData` sin fijar `Content-Type` manual (`uploadFile`), actualizar (`updateFile`, `updateAlternativeTitle`), eliminar (`deleteFile`, `deleteAlternativeTitle`) y construir la URL de descarga (`getDownloadUrl`).
3. **Interfaz y lógica (`frontend/index.html` y `frontend/js/archivos.js`):** Se integró la pestaña "Archivos" con formularios de alta/edición, tablas responsivas con filtros por libro, idioma y texto, botones de descarga y alertas de confirmación o error en `#alert-box`.
4. **Diagnóstico de conexión BD:** Se identificó el error `FATAL: password authentication failed for user "booknest_user"` y se verificaron las credenciales en `application.properties` junto con la instancia activa de PostgreSQL en WSL.
5. **Plan de pruebas en navegador:**
   - Subida exitosa de PDF (`201 Created`) con confirmación visual.
   - Validación de error controlado al subir archivo duplicado o no autorizado (`409 Conflict` / `400 Bad Request`).
   - Edición de metadatos (`200 OK`), filtrado en tabla (`GET`), descarga del PDF y gestión (alta/eliminación `204 No Content`) de títulos alternativos.
   - Verificación de solicitudes y códigos HTTP en la pestaña *Network (Fetch/XHR)* de *DevTools*.