# CURL - Segunda Entrega
## Módulo: Archivo colaborativo
_**Responsable**: Hans Arturo Martínez Romero_

### Subir un archivo PDF válido
```bash
curl -i -X POST "http://localhost:8081/api/files/upload" \
  -F "file=@muestra_libro.pdf" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "editionIsbn=9788401352836" \
  -F "isAuthorized=true"

curl -i -X POST "http://localhost:8081/api/files/upload" \
  -F "file=@muestra_libro.pdf" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "isAuthorized=true"

curl -i -X POST "http://localhost:8081/api/files/upload" \
  -F "file=@falso.txt" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "isAuthorized=true"

curl -i -X GET "http://localhost:8081/api/files/book/1"

curl -i -X GET "http://localhost:8081/api/files/download/1" --output descargado.pdf

curl -i -X POST "http://localhost:8081/api/alternative-titles" \
  -H "Content-Type: application/json" \
  -d '{"book": {"id": 1}, "contributedByUser": {"id": 3}, "title": "The Name of the Wind", "language": "en"}'

curl -i -X GET "http://localhost:8081/api/alternative-titles/book/1"
---

# CURL - Tercera Entrega
## Módulo: Archivo colaborativo
_**Responsable**: Hans Arturo Martínez Romero_

### Filtrar archivos por libro e idioma (200 OK)
```bash
curl -i -X GET "http://localhost:8081/api/files?bookId=1&language=es"

#### Filtrar títulos alternativos por palabra clave (200 OK)
curl -i -X GET "http://localhost:8081/api/alternative-titles?q=Wind"

#### Validar error HTTP 400 con parámetro inválido
curl -i -X GET "http://localhost:8081/api/files?language=fr"

#### Actualizar metadatos de un archivo PDF con PUT (200 OK)
curl -i -X PUT "http://localhost:8081/api/files/1" \
  -H "Content-Type: application/json" \
  -d '{"language": "es", "editionIsbn": "9788401352836", "originalFilename": "el_nombre_del_viento_ed1.pdf"}'

#### Validar error HTTP 404 al actualizar archivo inexistente
curl -i -X PUT "http://localhost:8081/api/files/999" \
  -H "Content-Type: application/json" \
  -d '{"language": "en"}'

#### Actualizar un título alternativo con PUT (200 OK)
curl -i -X PUT "http://localhost:8081/api/alternative-titles/1" \
  -H "Content-Type: application/json" \
  -d '{"title": "The Name of the Wind (10th Anniversary)", "language": "en"}'

#### Eliminar registro con DELETE (204 No Content) y comprobar inexistencia (404 Not Found)
curl -i -X DELETE "http://localhost:8081/api/alternative-titles/2"

curl -i -X DELETE "http://localhost:8081/api/alternative-titles/2"

#### Prueba de persistencia (Reiniciar servidor Spring Boot y consultar datos guardados)
curl -i -X GET "http://localhost:8081/api/files/1"

