# Reporte de Avance del Proyecto

**Equipo:** 06  
**Fecha:** 16 de noviembre de 2026  
**Número de reporte:** 03 - Endpoints completos (PUT, DELETE, filtros y persistencia)  
**Repositorio GitHub:** desarrollo-web

# 1. Actividades realizadas
### Nuevo
- Implementación de métodos `PUT` y `DELETE` para actualización y eliminación controlada de registros en los módulos del sistema.
- Incorporación de filtros y parámetros de consulta (`@RequestParam`) para búsquedas y estadísticas.
- Validación estricta de códigos de respuesta HTTP (`200`, `201`, `204`, `400`, `404`, `409`).
- Pruebas de persistencia en PostgreSQL tras detener y reiniciar el servidor Spring Boot.

### Modificaciones
- Configuración de `spring.jpa.hibernate.ddl-auto=validate` para preservar la integridad y persistencia de los datos entre ejecuciones.

# 2. Evidencias de trabajo
| Actividad | Integrante responsable | Enlace a commit |
| :--- | :--- | :--- |
| Implementar PUT y DELETE - Catálogo | Jetzaly Josmery Tello Campos | https://github.com/jetzz-uwux/desarrollo-web/commit/fab0486|
| Incorporar filtros - Catálogo | Jetzaly Josmery Tello Campos | https://github.com/jetzz-uwux/desarrollo-web/commit/beda1aa|
| Implementar PUT y DELETE - Biblioteca | Juan Luis Gaona Aguirre | https://github.com/jetzz-uwux/desarrollo-web/commit/b25a808 |
| PUT, DELETE, filtros y validación HTTP (400, 404, 409) - Reseñas/Stats | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/a4a3899 |
| Pruebas CURL, persistencia y documentación IA - Reseñas/Stats | Camila Mariné Crespo Zurita | https://github.com/jetzz-uwux/desarrollo-web/commit/3780806 |
| Implementar PUT, DELETE, filtros y validaciones HTTP | Martínez Romero Hans Arturo | https://github.com/jetzz-uwux/desarrollo-web/commit/f6e4687 |

# 3. Problemas encontrados
- **Módulo de Reseñas y Estadísticas:** Al realizar las pruebas iniciales tras reiniciar el entorno, el servidor devolvió `404 Not Found ("El usuario especificado no existe")` debido a que `ddl-auto=create` recreaba las tablas vacías. Se solucionó ejecutando `database/datosPruebas.sql` y cambiando la propiedad a `spring.jpa.hibernate.ddl-auto=validate` para garantizar la persistencia real en PostgreSQL.

# 4. Avance funcional
- **Operativas:** Operaciones CRUD completas (`GET`, `POST`, `PUT`, `DELETE`), filtros de consulta, validación de reglas de negocio (`400`, `404`, `409`) y persistencia verificada en PostgreSQL.
- **Pendientes:** Integración con la interfaz Frontend (Fetch / JSON) y despliegue final.

# 5. Uso de IA generativa
- **Herramienta utilizada:** Gemini
- **Actividad para la que se utilizó:** Implementación de PUT, DELETE, filtros, validación HTTP y pruebas de persistencia.
- **Código o contenido generado:** Métodos de servicio y controlador con `ResponseStatusException` (400, 404, 409), consultas nativas de estadísticas y comandos CURL de validación.
- **Modificaciones realizadas por el equipo:** Ajuste de genéricos en `RatingService`/`StatsController`, configuración de `ddl-auto=validate` y ejecución en terminal WSL.
- **Forma en que se verificó el resultado:** Ejecución de comandos `curl` verificando cada código HTTP y reinicio del servidor comprobando persistencia en PostgreSQL (evidencias en `documentacion/pruebas/Integrante 04/curl-resenias/` y reporte en `documentacion/IAG/Integrante 04/Uso_03/r-ia-dev-s03.md`).

# 6. Participación individual
- **Jetzaly Josmery Tello Campos:** Implementación de métodos `PUT` y `DELETE` e incorporación de filtros en el módulo de catálogo.
- **Hans Arturo Martínez Romero:** IMplementación de métodos `PUT` y `DELETE` e incorporaciíon de filtros en el módulo de Archivos.
- **Juan Luis Gaona Aguirre:** Implementación de métodos `PUT` y `DELETE` en el módulo de biblioteca personal.
- **Camila Mariné Crespo Zurita:** Implementación de métodos `PUT` y `DELETE` para reseñas y calificaciones con recálculo automático de promedios.

# 7. Próximas actividades
- Integración del Frontend (HTML, CSS, JavaScript con Fetch) consumiendo los endpoints REST.
- Pruebas integrales de extremo a extremo.

## Observaciones adicionales
Se conserva `spring.jpa.hibernate.ddl-auto=validate` en `application.properties` para asegurar la persistencia de datos en PostgreSQL entre reinicios del servidor.