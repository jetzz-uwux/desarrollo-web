Ya implementé los métodos PUT, DELETE y los filtros con `@RequestParam` en mi módulo de Reseñas y Estadísticas (Spring Boot), pero tengo un par de problemas al compilar y probar:
1. En `StatsController` y `RatingService` me marca error de sintaxis con `List>` y el tipo de retorno.
2. Al probar con `curl` en WSL me falla la autenticación con `booknest_user` y, cuando reinicio el servidor, se borran los registros de prueba que inserté en PostgreSQL.
3. ¿Qué casos de prueba `curl` me recomiendas agregar para asegurarme de que los códigos HTTP `400`, `404` y `409` estén bien validados?