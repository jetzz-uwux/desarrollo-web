# Prompt - Uso 04 (Integrante 04 - Camila)

Al ejecutar el proyecto BookNest en WSL me aparecen los siguientes errores al intentar probar la interfaz web con el backend:

1. Al entrar a `http://localhost:8081` sale `Whitelabel Error Page (type=Not Found, status=404) - No static resource for request '/'`.
2. Al abrir el frontend en `http://localhost:5500`, en pantalla aparece `No se pudieron cargar los libros: Failed to fetch` y en la pestaña Network del navegador las peticiones a `books` y `reviews` fallan con `CORS error`.
3. Además, necesito checar el formato de JSON que esperan `ReviewController` y `StatsController` según las clases `Review.java` y `Rating.java` para que se puedan ligar bien los formularios con `fetch`.

¿Por qué ocurren estos errores y cómo puedo corregirlos usando `@CrossOrigin` como vimos en clase?