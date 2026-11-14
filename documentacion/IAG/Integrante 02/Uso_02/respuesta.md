**Herramienta:** Gemini
**Respuesta obtenida:**
Comandos listos para ejecutar en secuencia
1. Subida válida de PDF (POST /api/files/upload → 201 Created):   Bashcurl -i -X POST http://localhost:8081/api/files/upload \
  -F "file=@muestra_libro.pdf" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "editionIsbn=9788401352836" \
  -F "isAuthorized=true"
(Fíjate en el "id" que te devuelva en el JSON de respuesta —será "id": 1— para usarlo en la descarga).
2. Rechazo de duplicado por checksum SHA-256 (POST /api/files/upload → 409 Conflict):   Bashcurl -i -X POST http://localhost:8081/api/files/upload \
  -F "file=@muestra_libro.pdf" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "isAuthorized=true"
3. Rechazo de archivo inválido que no es PDF (POST /api/files/upload → 400 Bad Request):   Bashecho "Archivo de texto falso" > falso.txt
curl -i -X POST http://localhost:8081/api/files/upload \
  -F "file=@falso.txt" \
  -F "bookId=1" \
  -F "uploaderUserId=3" \
  -F "language=es" \
  -F "isAuthorized=true"
4. Consultar los archivos del libro 1 (GET /api/files/book/1 → 200 OK):Bashcurl -i -X GET http://localhost:8081/api/files/book/1
5. Descargar el PDF autorizado (GET /api/files/download/1 → 200 OK):   Bashcurl -i -X GET http://localhost:8081/api/files/download/1 --output descargado.pdf
ls -lh descargado.pdf
6. Registrar y consultar título alternativo (POST y GET /api/alternative-titles):   Bashcurl -i -X POST http://localhost:8081/api/alternative-titles \
  -H "Content-Type: application/json" \
  -d '{"book": {"id": 1}, "contributedByUser": {"id": 3}, "title": "The Name of the Wind", "language": "en"}'

curl -i -X GET http://localhost:8081/api/alternative-titles/book/1