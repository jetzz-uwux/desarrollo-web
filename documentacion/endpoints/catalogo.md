# Módulo: Catálogo, búsqueda y sagas
**Responsable:** Jetzaly Josmery Tello Campos

## Tareas asignadas
- **Frontend:** Página principal, catálogo, buscador, filtros, fichas de libros y sagas.
- **Backend:** Endpoints GET para consulta, búsqueda, filtros y ordenamiento.
- **Base de datos:** Libros, autores, géneros y sagas.
- **Pruebas:** Consultas GET, filtros combinados y búsquedas sin resultados.

## Endpoints propuestos
- `GET /api/books` : Listar el catálogo completo, realizar búsquedas por palabras clave y aplicar filtros (género, autor, idioma).
- `GET /api/books/{id}` : Consultar la información detallada de una obra específica (y su saga, si aplica).