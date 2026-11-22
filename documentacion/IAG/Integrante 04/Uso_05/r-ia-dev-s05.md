# Registro de Uso de IA Generativa - Sesión 05

| Concepto | Descripción |
| :--- | :--- |
| **Herramienta** | Gemini |
| **Modelo / Temperatura** | Gemini |
| **Código de uso** | Pruebas / Generación |
| **Prompt original** | Solicitud de datos SQL de prueba (`INSERT`) para poblar las tablas `literary_series`, `books`, `authors`, `genres` y sus tablas pivote con 7 sagas/trilogías literarias compatibles con `schema.sql` en PostgreSQL. |
| **Respuesta de IA** | Bloque SQL con registros de prueba para géneros, autores, series literarias y libros con sus relaciones y ajuste de secuencias `setval`. |
| **Código anterior** | Commit `6b5e61a` |
| **Código posterior** | Commit `3dc19bc` |
| **Diferencias entre versiones** | `git diff --numstat 6b5e61a 3dc19bc` -> Modificación en `backend/src/main/resources/datosPruebas.sql` y evidencias en `documentacion/pruebas/Integrante 04/prueba-cruzadda-Jetzaly/` |
| **Prueba de funcionamiento** | Ejecución limpia en PostgreSQL con `psql -h localhost -U booknest_user -d booknest -f backend/src/main/resources/datosPruebas.sql` finalizando en `COMMIT`, y capturas guardadas en `documentacion/pruebas/Integrante 04/prueba-cruzadda-Jetzaly/`. |
| **Reflexión del estudiante** | Permitir que la IA generara los datos de prueba ahorró tiempo en la preparación del entorno, pero verificar el archivo `schema.sql` fue indispensable para adaptar la sintaxis y restricciones propias de PostgreSQL y evitar conflictos de llaves primarias con los datos iniciales. |