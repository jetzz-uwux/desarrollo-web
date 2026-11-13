# Reporte de IA - Sesión 02 (Generación de Pruebas y Script DB)

| Concepto | Descripción |
| :--- | :--- |
| **Herramienta** | Gemini |
| **Modelo/temperatura** | Gemini / 0.2 |
| **Prompt original** | Actúa como un desarrollador backend experto en Spring Boot. Necesito probar mis endpoints REST para crear reseñas y calificaciones usando CURL en la terminal de WSL. Mis entidades `Review` y `Rating` tienen relaciones `@ManyToOne` con `User` y `Book`. ¿Cómo debo estructurar el JSON en el comando CURL para pasar los IDs relacionales correctamente al hacer un POST y no obtener un error 400 (Bad Request)? Adicionalmente, dame un pequeño script SQL para insertar un par de usuarios y libros de prueba directamente en PostgreSQL. |
| **Respuesta de IA** | Gemini explicó que las relaciones `@ManyToOne` se mapean en el JSON enviando un objeto anidado con la llave primaria (ej. `"user": {"id": 1}`). Generó los comandos CURL estructurados para POST y GET de reseñas y calificaciones. También generó un script SQL (`datosPruebas.sql`) con los `INSERT` iniciales para evitar violaciones de llave foránea al probar la API. |
| **Código anterior** | Commit (`2600575`) |
| **Código posterior** | Commit (`b9d0510`) |
| **Diferencias entre versiones** | `git diff --numstat 2600575 b9d0510`<br><br>49 0 backend/src/main/java/mx/uv/booknest/controllers/ReviewController.java<br>39 0 backend/src/main/java/mx/uv/booknest/controllers/StatsController.java<br>94 0 backend/src/main/java/mx/uv/booknest/models/Rating.java<br>94 0 backend/src/main/java/mx/uv/booknest/models/Review.java<br>24 0 backend/src/main/java/mx/uv/booknest/repository/RatingRepository.java<br>14 0 backend/src/main/java/mx/uv/booknest/repository/ReviewRepository.java<br>31 0 backend/src/main/java/mx/uv/booknest/service/RatingService.java<br>52 0 backend/src/main/java/mx/uv/booknest/service/ReviewService.java<br>16 0 database/datosPruebas.sql |
| **Prueba de funcionamiento** | CURL y capturas de imagen (ver carpeta `documentacion/pruebas/Integrante 04/curl-resenas/`) |
| **Código de uso** | Generación / Pruebas |
| **Reflexión del estudiante** | La IA me permitió comprender cómo Spring Boot procesa los JSON anidados (ej. `{"user": {"id": 1}}`) para resolver relaciones `@ManyToOne` sin generar errores de tipo `400 Bad Request`. Además, la creación del script de datos de prueba (`datosPruebas.sql`) homologó el entorno para que el equipo pueda probar sus endpoints sin insertar registros manualmente en PostgreSQL, cumpliendo con el uso mínimo de IA requerido para pruebas. |