# Módulo: Archivo colaborativo
**Responsable:** Hans Arturo Martínez Romero (Integrante 02)

## Tareas asignadas
- **Frontend:** Formularios de aportación, adjuntar PDF y descargar archivos.
- **Backend:** Registro, subida y descarga de PDF, validación y coincidencias por ISBN/títulos/autor.
- **Base de datos:** Archivos, idiomas, títulos alternativos y aportaciones.
- **Pruebas:** POST, archivos válidos/inválidos, duplicados y descargas.

## Endpoints propuestos
- `POST /api/files/upload` : Subir y validar un archivo PDF (validando peso y extensión real) asociado a una ficha bibliográfica.
- `GET /api/files/download/{id}` : Descargar un archivo PDF autorizado específico.