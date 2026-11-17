# Reporte de IA - Sesión 03 (Depuración y Validación de Pruebas HTTP/Persistencia)

| Concepto | Descripción |
| :--- | :--- |
| **Herramienta** | Gemini |
| **Modelo/temperatura** | Gemini / 0.2 |
| **Prompt original** | Ya implementé los métodos PUT, DELETE y los filtros con `@RequestParam` en mi módulo de Reseñas y Estadísticas (Spring Boot), pero tengo un par de problemas al compilar y probar: 1. En `StatsController` y `RatingService` me marca error de sintaxis con `List>`... 2. Al probar con `curl` en WSL me falla la autenticación y al reiniciar el servidor se borran los registros... 3. ¿Qué casos `curl` me recomiendas agregar para validar `400`, `404` y `409`? |
| **Respuesta de IA** | Gemini diagnosticó el error de tipos genéricos en `StatsController`/`RatingService`, indicó cambiar `spring.jpa.hibernate.ddl-auto` a `validate` para mantener la persistencia en PostgreSQL y sugirió comandos `curl` específicos para comprobar errores `400`, `404` y `409`. |
| **Código anterior** | Commit (`50c7ea3`) |
| **Código posterior** | Commit (`a4a3899`) |
| **Diferencias entre versiones** | `git diff --numstat 50c7ea3 a4a3899` (Ajustes en `RatingService.java`, `StatsController.java`, `ReviewService.java` y `ReviewController.java`) |
| **Prueba de funcionamiento** | Batería de pruebas `curl` (201, 200, 204, 400, 404, 409) y captura de reinicio del servidor en `documentacion/pruebas/Integrante 04/curl-resenias/` (Commit `3780806`) |
| **Código de uso** | Depuración / Pruebas |
| **Reflexión del estudiante** | Consultar con la IA me ayudó a resolver rápido el error de tipos genéricos en el controlador y a detectar por qué no se conservaban los datos tras reiniciar Spring Boot (`ddl-auto=create` estaba recreando el esquema en cada arranque). Además, revisar los casos borde con `curl` me permitió confirmar que mis excepciones devolvieran exactamente `400`, `404` y `409`. |