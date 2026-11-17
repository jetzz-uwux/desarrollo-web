**Herramienta:** Gemini
**Respuesta obtenida:**
Se actualizaron las capas `Repository`, `Service` y `Controller` de `BookFile` y `AlternativeTitle` para soportar actualización (`PUT`), eliminación física y en base de datos (`DELETE` con `204 No Content`) y filtrado dinámico en `GET`. Asimismo, se ajustó `spring.jpa.hibernate.ddl-auto=update` en `application.properties` para evitar que Hibernate recree las tablas y borre los registros al reiniciar el servidor.

Matriz de pruebas `curl` ejecutadas para la Tercera Entrega:

| # | Prueba | Endpoint / Método | Código HTTP esperado |
|---|---|---|---|
| 1 | Filtrar archivos por libro e idioma | `GET /api/files?bookId=1&language=es` | 200 OK |
| 2 | Filtrar títulos alternativos por palabra clave | `GET /api/alternative-titles?q=Wind` | 200 OK |
| 3 | Validar filtro con idioma inválido | `GET /api/files?language=fr` | 400 Bad Request |
| 4 | Actualizar metadatos de archivo existente | `PUT /api/files/1` | 200 OK |
| 5 | Validar actualización de archivo inexistente | `PUT /api/files/999` | 404 Not Found |
| 6 | Actualizar título alternativo existente | `PUT /api/alternative-titles/1` | 200 OK |
| 7 | Eliminar título alternativo y verificar borrado | `DELETE /api/alternative-titles/2` (dos ejecuciones) | 204 No Content / 404 Not Found |
| 8 | Comprobar persistencia tras reiniciar servidor | `GET /api/files/1` (antes y después de reiniciar Spring Boot) | 200 OK |