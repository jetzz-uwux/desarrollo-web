# Reporte de Avance del Proyecto

**Equipo:** BookNest (Equipo 06)  
**Fecha:** 23 de noviembre de 2026  
**Número de reporte:** 05 (Versión candidata - Pruebas y documentación)  
**Repositorio GitHub:** https://github.com/jetzz-uwux/desarrollo-web  

## 1. Actividades realizadas
- Validación integral de carga, filtrado, edición y descarga de archivos PDF y títulos alternativos en el entorno integrado.
- Corrección de errores de interfaz y layout en el módulo de Archivos Colaborativos (`frontend/index.html` y `frontend/css/styles.css`).
- Ajuste en el consumo de endpoints del módulo de registro de libros (`frontend/js/catalogo.js`) y recreación limpia de la base de datos local con `schema.sql`, la migración `2026_11_23_book_category.sql` y `datosPruebas.sql`.
- Preparación y grabación de los segmentos de demostración técnica del sistema (Arquitectura/tecnologías y módulo de Archivos PDF y aportaciones).
- *(Agregar actividades adicionales de Integrantes 1, 3 y 4)*

## 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
| :--- | :--- | :--- |
| Revisión y ajustes de catálogo y series | Integrante 1 (Jetzaly) | https://github.com/jetzz-uwux/desarrollo-web/commit/4680d8d4614e094f1d70abc1825f6c73447e0308, https://github.com/jetzz-uwux/desarrollo-web/commit/dfefbfc7d10e6757015dd6d8a0f71e7d2c06aaa4, https://github.com/jetzz-uwux/desarrollo-web/commit/6752675b3e8b237eaa1e01dea53723e35f70b120, https://github.com/jetzz-uwux/desarrollo-web/commit/ae1242b9ac0f90256c3d2361d578e9835463eb3b, https://github.com/jetzz-uwux/desarrollo-web/commit/e1f52654ca119d9d5b1742292c033035c34085ef |
| Validación de carga y descarga de PDFs, corrección de filtros, estilos CSS y ruta API en registro de libros | Integrante 2 (Hans) | https://github.com/jetzz-uwux/desarrollo-web/commit/8f58359 |
| Pruebas de colecciones, progreso y README | Integrante 3 (Juan Luis) | `<commit-juanluis>` |
| Datos de prueba de series, prueba cruzada de catálogo/series y evidencias IAG | Integrante 4 (Camila) | `3dc19bc` (https://github.com/jetzz-uwux/desarrollo-web/commit/3dc19bc) |

## 3. Problemas encontrados
1. **Desfase de esquema en PostgreSQL (`column b1_0.category does not exist`):** Tras incorporar el campo `category` en la entidad `Book`, la base local arrojaba error SQL al consultar libros y archivos, y `datosPruebas.sql` fallaba con `ROLLBACK` por restricciones `ON CONFLICT` faltantes.  
   - **Solución:** Se recreó el esquema `public` ejecutando en orden `database/schema.sql`, la migración `database/migrations/2026_11_23_book_category.sql` y `backend/src/main/resources/datosPruebas.sql`.
2. **Desplazamiento del formulario en CSS Grid por nombres largos de PDF:** Los nombres de archivos sin espacios empujaban y aplastaban el panel del formulario izquierdo dentro de `.grid-layout`.  
   - **Solución:** Se ajustó `grid-template-columns` a `minmax(300px, 1fr) minmax(0, 2fr)`, se añadió `min-width: 0` en `.card` y `overflow-wrap: anywhere` en `th, td` dentro de `frontend/css/styles.css`.
3. **Ocultamiento de la vista al usar el botón "Filtrar" en Archivos:** Los botones `#btn-filter-files` y `#btn-filter-alt` tenían asignada la clase `nav-btn`, lo que disparaba el evento de cambio de pestañas en `app.js` y dejaba la pantalla en blanco.  
   - **Solución:** Se restauró la clase `btn btn-outline` en ambos botones dentro de `frontend/index.html`.
4. **Error `"No se pudieron comprobar posibles duplicados"` al registrar libros:** El `fetch` en `frontend/js/catalogo.js` utilizaba una ruta relativa sin `API_BASE_URL` y bloqueaba el alta cuando la consulta previa fallaba.  
   - **Solución:** Se antepuso `${API_BASE_URL}` a las peticiones de `/api/books` y se manejó la validación previa sin interrumpir el registro del libro.

## 4. Avance funcional
- **Operativo (Versión candidata):**
  - Sistema integrado cliente-servidor (Frontend HTML/CSS/JS + Backend Spring Boot en puerto `8081` + PostgreSQL).
  - Módulo de Archivo Colaborativo (PDFs) y Títulos Alternativos operando de extremo a extremo: alta con validaciones de formato/tamaño/duplicados, edición de metadatos, filtros sin pérdida de vista, descarga y eliminación.
  - Registro de libros, asociación de autores/géneros, series, reseñas, calificaciones, estadísticas, usuarios y colecciones integrados en la misma base de datos de prueba.
- **Pendiente:**
  - Consolidación final del video demostrativo, exportación de `documentacion/proyecto.pdf` y creación del *Release* final en GitHub.

## 5. Uso de IA generativa
- **Herramienta utilizada:** Google Gemini (Integrante 2 - Hans Arturo Martínez Romero)
- **Actividad para la que se utilizó:** Depuración de errores de esquema en PostgreSQL (`category` y `datosPruebas.sql`), corrección de CSS Grid (`styles.css`), diagnóstico del conflicto de clases en botones de filtrado (`index.html`), ajuste de `catalogo.js` y estructuración del guion para el video demostrativo.
- **Código o contenido generado:** Comandos `psql` para recreación limpia de la base de datos, reglas CSS `minmax`/`overflow-wrap`, corrección de botones HTML y manejo de `API_BASE_URL` en `catalogo.js`.
- **Modificaciones realizadas por el equipo:** Se aplicaron las correcciones directamente en el repositorio local y se verificó el funcionamiento completo antes de grabar la demostración.
- **Forma en que se verificó el resultado:** Pruebas manuales en navegador (`http://localhost:8081/index.html`) validando creación de libros, filtrado en tablas, carga/descarga de PDFs y visualización responsiva sin desplazamiento.

*(Agregar registros de Uso de IA de Integrantes 1, 3 y 4 si aplica)*

## 6. Participación individual
- **Integrante 1 (Jetzaly Josmery Tello Campos):** *(Completar)*
- **Integrante 2 (Hans Arturo Martínez Romero):** Validación de carga y descarga de PDFs, recreación y sincronización de la base de datos de prueba con migraciones, corrección de estilos en `styles.css`, reparación de botones de filtrado en `index.html`, ajuste de rutas `fetch` en `catalogo.js`, actualización del reporte `r-act-05.md` y grabación de los segmentos de arquitectura y archivos colaborativos para el video demostrativo.
- **Integrante 3 (Juan Luis Gaona Aguirre):** *(Completar)*
- **Integrante 4 (Camila Mariné Crespo Zurita):** *(Completar)*

## 7. Próximas actividades
- Ensamblar y subir el video demostrativo final del sistema.
- Verificar el `README.md` y el documento consolidado `documentacion/proyecto.pdf`.
- Publicar el *Release* final de la versión candidata en GitHub.