# Módulo: Catálogo, búsqueda y sagas

**Responsable:** Jetzaly Josmery Tello Campos

## Tareas asignadas

- **Frontend:** Página principal, catálogo, buscador, filtros, fichas de libros y sagas.
- **Backend:** Endpoints GET, POST, PUT y DELETE para consulta, búsqueda, filtros y ordenamiento.
- **Base de datos:** Libros, autores, géneros y sagas.
- **Pruebas:** Consultas GET, filtros combinados y búsquedas sin resultados.

## Endpoints propuestos

| Método | Ruta | Descripción |
| --- | --- | --- |
|GET|`/api/libros`|Lista con búsqueda, filtros y orden|
|GET|`/api/libros/{id}`|Ficha completa|
|GET|`/api/generos`|Lista de géneros|
|GET|`/autores`|Lista de autores|
|GET|`/sagas`|Lista de sagas|
|GET|`/sagas/{id}`|Saga con sus libros en orden|
|POST|`/sagas`|Crea una saga|
|PUT|`/sagas/{id}`|Modifica una saga|
|DELETE|`/sagas/{id}`|Elimina una saga|