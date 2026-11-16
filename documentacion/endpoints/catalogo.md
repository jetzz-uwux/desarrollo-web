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
| GET | `/api/genres` | Lista de géneros | 200 |
| GET | `/api/genres/{id}` | Un género | 200, 404 |
| POST | `/api/genres` | Crea un género | 201, 400 |
| PUT | `/api/genres/{id}` | Modifica un género | 200, 400, 404 |
| DELETE | `/api/genres/{id}` | Elimina un género | 204, 404, 409 |
| GET | `/api/authors` | Lista de autores | 200 |
| GET | `/api/authors/{id}` | Un autor | 200, 404 |
| POST | `/api/authors` | Crea un autor | 201, 400 |
| PUT | `/api/authors/{id}` | Modifica un autor | 200, 400, 404 |
| DELETE | `/api/authors/{id}` | Elimina un autor | 204, 404, 409 |
| GET | `/api/series` | Lista de series | 200 |
| GET | `/api/series/{id}` | Serie con sus libros en orden de lectura | 200, 404 |
| POST | `/api/series` | Crea una serie | 201, 400 |
| PUT | `/api/series/{id}` | Modifica una serie | 200, 400, 404 |
| DELETE | `/api/series/{id}` | Elimina una serie | 204, 404, 409 |
| POST | `/api/series/{id}/books` | Agrega un libro a la serie | 201, 400, 404, 409 |
| DELETE | `/api/series/{id}/books/{bookId}` | Quita un libro de la serie | 204, 404 |
| GET | `/api/books` | Lista con búsqueda, filtros y orden | 200 |
| GET | `/api/books/{id}` | Ficha completa | 200, 404 |
| POST | `/api/books` | Crea un libro | 201, 400 |
| PUT | `/api/books/{id}` | Modifica un libro | 200, 400, 404 |
| DELETE | `/api/books/{id}` | Elimina un libro | 204, 404, 409 |
| GET | `/api/books/series/{seriesId}` | Lista los libros de una serie en orden de lectura | 200 |
| POST | `/api/books/{id}/authors` | Liga un autor con un libro (propuesta) | 201, 404, 409 |
| POST | `/api/books/{id}/genres` | Liga un género con un libro (propuesta) | 201, 404, 409 |