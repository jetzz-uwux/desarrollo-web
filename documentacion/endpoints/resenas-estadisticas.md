# Módulo: Reseñas y estadísticas
**Responsable:** Camila Mariné Crespo Zurita (Integrante 04)

## Tareas asignadas
- **Frontend:** Reseñas, calificaciones, rankings y paneles estadísticos.
- **Backend:** Publicar/editar reseñas, calcular promedios, conteos y estadísticas colectivas.
- **Base de datos:** Reseñas, calificaciones y consultas agregadas.
- **Pruebas:** Promedios, modificaciones, libros sin calificar y rankings.

## Endpoints propuestos
- `POST /api/reviews` : Publicar o actualizar la reseña y calificación (1-5 estrellas) de un usuario para una obra.
- `GET /api/stats/global` : Obtener clasificaciones públicas (libros mejor valorados, más leídos, géneros populares) para mostrar los promedios de la comunidad.