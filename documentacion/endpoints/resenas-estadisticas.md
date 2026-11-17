# Módulo: Reseñas y estadísticas
**Responsable:** Camila Mariné Crespo Zurita (Integrante 04)

## Tareas asignadas
- **Frontend:** Reseñas, calificaciones, rankings y paneles estadísticos.
- **Backend:** Publicar/editar reseñas, calcular promedios, conteos y estadísticas colectivas.
- **Base de datos:** Reseñas, calificaciones y consultas agregadas.
- **Pruebas:** Promedios, modificaciones, libros sin calificar y rankings.

## Endpoints implementados

| Método | Ruta | Descripción | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/reviews` | Lista reseñas (filtros opcionales: `?bookId=` y `?userId=`) | `200` |
| `GET` | `/api/reviews/{id}` | Obtiene una reseña por ID | `200`, `404` |
| `GET` | `/api/reviews/book/{bookId}` | Obtiene las reseñas de un libro (más recientes primero) | `200`, `404` |
| `POST` | `/api/reviews` | Publica una reseña nueva | `201`, `400`, `404` |
| `PUT` | `/api/reviews/{id}` | Edita una reseña existente (valida autoría) | `200`, `400`, `404`, `409` |
| `DELETE` | `/api/reviews/{id}` | Elimina una reseña (parámetro opcional `?userId=`) | `204`, `404`, `409` |
| `POST` | `/api/stats/rating` | Registra una calificación de 1 a 5 estrellas (una por usuario/libro) | `201`, `400`, `404`, `409` |
| `PUT` | `/api/stats/rating/{id}` | Modifica una calificación y recalcula promedio | `200`, `400`, `404` |
| `DELETE` | `/api/stats/rating/{id}` | Elimina una calificación y recalcula promedio | `204`, `404` |
| `GET` | `/api/stats/global` | Promedios y conteo de votos por obra (`"Sin calificar"` si no hay votos; filtro opcional `?bookId=`) | `200`, `404` |
| `GET` | `/api/stats/most-read` | Ranking de libros más leídos (usuarios distintos en estado `terminado`) | `200` |
| `GET` | `/api/stats/user/{userId}` | Estadísticas personales de lectura del usuario | `200`, `404` |