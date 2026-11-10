# Reporte de Avance del Proyecto
**Fecha:** 9 de noviembre de 2026
**Número de reporte:** R1 - Diseño inicial
**Repositorio GitHub:** desarrollo-web

# 1. Actividades realizadas
Describir las actividades desarrolladas desde el último reporte.
- Creación del repositorio en GitHub y estructuración de carpetas del proyecto.
- Análisis de requerimientos funcionales y no funcionales del sistema BookNest.
- Diseño de la estructura de la base de datos relacional (PostgreSQL).
- Definición y documentación de los endpoints REST distribuidos por módulos en la carpeta `documentacion/endpoints/`.
- Distribución de responsabilidades y módulos por integrante.

# 2. Evidencias de trabajo
| Actividad | Integrante responsable | Enlace a commit / Ruta |
| :--- | :--- | :--- |
| Creación de repositorio y estructura inicial | Todos | [Estructura base](./) |
| Diseño de modelo de datos relacional | Todos | [`database/schema.sql`](./database/schema.sql) |
| Documentación de Endpoints GET (Catálogo) | Jetzaly Josmery Tello Campos | [`documentacion/endpoints/catalogo.md`](./documentacion/endpoints/catalogo.md) |
| Documentación de Endpoints POST/GET (Archivos) | Hans Arturo Martínez Romero | [`documentacion/endpoints/archivos.md`](./documentacion/endpoints/archivos.md) |
| Documentación de Endpoints (Usuarios/Auth) | Juan Luis Gaona Aguirre | [`documentacion/endpoints/usuarios-colecciones.md`](./documentacion/endpoints/usuarios-colecciones.md) |
| Documentación de Endpoints (Reseñas/Stats) | Camila Mariné Crespo Zurita | [`documentacion/endpoints/resenas-estadisticas.md`](./documentacion/endpoints/resenas-estadisticas.md) |

# 3. Problemas encontrados
Describir los problemas técnicos encontrados y cómo fueron solucionados.
- El único que se encontró fue descubrir los requisitos y características que se querían usar, pero una charla rápida como equipo fue lo único que se necesitó para obtenerlos.
- Más allá de eso, en esta fase de análisis y diseño inicial (R1) no se nos presentaron problemas técnicos. Las dudas sobre la estructuestructura y la documentación se resolvieron revisando y manteniéndose fiel a los lineamientos del proyecto.

# 4. Avance funcional
Indicar qué funcionalidades ya se encuentran operativas y cuáles están pendientes.
- **Operativas:** Ninguna (Fase de diseño y planeación R1 completada).
- **Pendientes:** Creación del proyecto Spring Boot, configuración de la conexión a PostgreSQL, creación de entidades/repositorios y desarrollo de los primeros endpoints funcionales (POST y GET).

# 5. Uso de IA generativa
Documentar el uso de herramientas de inteligencia artificial durante el desarrollo del proyecto.

**Herramienta utilizada:** Gemini
**Actividad para la que se utilizó:** Asistencia en la normalización y diseño del esquema DDL de la base de datos (PostgreSQL), y estructuración de reportes Markdown.
**Código o contenido generado:** Script SQL preliminar para las tablas de usuarios, catálogo, archivos, colecciones y reseñas.
**Modificaciones realizadas por el equipo:** Ajuste de tipos de datos, revisión de constraints y adaptación a la estructura de archivos solicitada para el repositorio.
**Forma en que se verificó el resultado:** Revisión manual del script SQL contra el documento de requerimientos funcionales y el modelo de datos de Spring Boot (JPA).

**Herramienta utilizada:** ChatGPT
**Actividad para la que se utilizó:** Asistencia para plasmar los requerimientos.
**Código o contenido generado:** Lista de requerimeintos y division de módulos.
**Modificaciones realizadas por el equipo:** Ajustes minimos de requerimientos. 
**Forma en que se verificó el resultado:** Revision general si los requerimientos seguian nuestro borrador inicial.

# 6. Participación individual
Describir brevemente la contribución de cada integrante.

- **Jetzaly Josmery Tello Campos:** Análisis de requerimientos y diseño del módulo de catálogo, búsqueda y sagas. Redacción del diseño de sus endpoints asignados.
- **Hans Arturo Martínez Romero:** Análisis de requerimientos y diseño del módulo de archivo colaborativo (PDFs). Redacción del diseño de sus endpoints asignados.
- **Juan Luis Gaona Aguirre:** Análisis de requerimientos y diseño del módulo de usuarios y biblioteca personal. Redacción del diseño de sus endpoints asignados.
- **Camila Mariné Crespo Zurita:** Análisis de requerimientos y diseño del módulo de reseñas y estadísticas. Redacción del diseño de sus endpoints asignados.

# 7. Próximas actividades
Indicar las tareas que se realizarán antes del siguiente reporte.
- Crear el proyecto base en Spring Boot.
- Configurar la conexión a la base de datos (PostgreSQL).
- Crear las entidades JPA y repositorios correspondientes a cada módulo.
- Implementar los primeros métodos POST y GET básicos.
- Realizar pruebas de endpoints mediante el cliente CURL.

## Observaciones adicionales
El diseño detallado de la API y los endpoints correspondientes a cada módulo no se repiten en este reporte, ya que se encuentran documentados de forma independiente en los archivos individuales dentro de la ruta `documentacion/endpoints/` del repositorio.