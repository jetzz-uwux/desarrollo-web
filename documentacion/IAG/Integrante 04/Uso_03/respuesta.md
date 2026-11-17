**Herramienta:** Gemini (Modelo de Google)

**Respuesta obtenida:**
Gemini identificó las causas de los errores y propuso los siguientes ajustes:

1. **Error de compilación en genéricos:** Simplificar el tipo de retorno de `obtenerEstadisticasGlobales` a `List<Object>` en `RatingService` y `StatsController` para evitar conflictos de sintaxis con tipos genéricos anidados[cite: 7].
2. **Conexión y persistencia en PostgreSQL:** Verificar el servicio de PostgreSQL en WSL (`sudo service postgresql start`), alinear la contraseña de `booknest_user` y cambiar `spring.jpa.hibernate.ddl-auto=create` por `validate` en `application.properties` para que Hibernate no recree las tablas ni borre los datos al reiniciar el servidor[cite: 1].
3. **Casos de prueba `curl` para validar códigos HTTP (`400`, `404`, `409`) y persistencia:**
```bash
# 400 Bad Request - Contenido vacío en reseña
curl -i -X POST "http://localhost:8081/api/reviews" -H "Content-Type: application/json" -d '{"user": {"id": 1}, "book": {"id": 1}, "content": ""}'

# 404 Not Found - Recurso o llave foránea inexistente
curl -i -X GET "http://localhost:8081/api/reviews/999"

# 409 Conflict - Calificación duplicada o intento de editar reseña de otro usuario
curl -i -X PUT "http://localhost:8081/api/reviews/1" -H "Content-Type: application/json" -d '{"user": {"id": 2}, "content": "Intento ajeno."}'
curl -i -X POST "http://localhost:8081/api/stats/rating" -H "Content-Type: application/json" -d '{"user": {"id": 1}, "book": {"id": 1}, "score": 4}'