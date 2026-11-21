**Herramienta:** Claude
**Respuesta obtenida:** 
Se señalaron errores en el código con respecto al ordenamiento y filtrado de datos
## 1. Diferencia entre frontend y template en BookNest

| Característica | Frontend con Fetch | Template con Thymeleaf |
|---|---|---|
| ¿Qué es? | Una interfaz HTML que utiliza JavaScript para consultar el backend | Una página HTML que Spring Boot procesa antes de enviarla |
| ¿Dónde está? | `frontend/index.html` | `backend/src/main/resources/templates/` |
| ¿Cómo obtiene los datos? | JavaScript utiliza `fetch()` para llamar a la API | El controlador Java envía los datos mediante `Model` |
| ¿Quién genera el contenido? | JavaScript en el navegador | Thymeleaf en el servidor |
| ¿Necesita recargar la página? | Generalmente no | Normalmente sí, cuando envías un formulario |
| ¿Para qué lo usamos? | Crear libros, asociar autores y géneros, gestionar series | Consultar catálogo, fichas y series |