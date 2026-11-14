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