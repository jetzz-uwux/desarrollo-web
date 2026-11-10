# Módulo: Catálogo, búsqueda y sagas

**Responsable:** Jetzaly Josmery Tello Campos

## Tareas asignadas

- **Frontend:** Página principal, catálogo, buscador, filtros, fichas de libros y sagas.
- **Backend:** Endpoints GET, POST, PUT y DELETE para consulta, búsqueda, filtros y ordenamiento.
- **Base de datos:** Libros, autores, géneros y sagas.
- **Pruebas:** Consultas GET, filtros combinados y búsquedas sin resultados.

## Endpoints propuestos

| Método | Ruta | Descripción | Códigos |
|---|---|---|---|
| GET | `/api/books` | Lista con búsqueda, filtros y orden | 200 |
| GET | `/api/books/{id}` | Ficha completa | 200, 404 |
| GET | `/api/genres` | Lista de géneros | 200 |
| GET | `/api/authors` | Lista de autores | 200 |
| GET | `/api/series` | Lista de series | 200 |
| GET | `/api/series/{id}` | Serie con sus libros en orden de lectura | 200, 404 |
| POST | `/api/series` | Crea una serie | 201, 400 |
| PUT | `/api/series/{id}` | Modifica una serie | 200, 400, 404 |
| DELETE | `/api/series/{id}` | Elimina una serie | 204, 404, 409 |
| POST | `/api/series/{id}/books` | Agrega un libro a la serie | 201, 400, 404, 409 |
| DELETE | `/api/series/{id}/books/{bookId}` | Quita un libro de la serie | 204, 404 |