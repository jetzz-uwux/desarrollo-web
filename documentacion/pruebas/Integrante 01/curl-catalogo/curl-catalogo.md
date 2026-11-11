# CURL - Segunda Entrega
## POST y GET Géneros
_**Responsable**: Jetzaly Josmery Tello Campos_

### Crear género
```bash
curl -i -X POST "http://localhost:8081/api/genres" -H "Content-Type: application/json" -d '{"name":"Nombre"}'
```
![Crear género](image.png)

### Crear género sin nombre
```bash
curl -i -X POST "http://localhost:8081/api/genres" -H "Content-Type: application/json" -d '{}'
```
![Crear género sin nombre](image-1.png)

### Listar géneros
```bash
curl -i -X GET "http://localhost:8081/api/genres"
```
![Listar géneros](image-2.png)

### Género que existe
```bash
curl -i -X GET "http://localhost:8081/api/genres/1"
```
![Género que existe](image-3.png)

### Género que no existe
```bash
curl -i -X GET "http://localhost:8081/api/genres/5"
```
![Género que no existe](image-4.png)

## POST y GET Autor

### Crear autor
```bash
curl -i -X POST "http://localhost:8081/api/authors" -H "Content-Type: application/json" -d '{"name":"Yo"}'
```
![Crear autor](image-5.png)

### Crear autor sin nombre
```bash
curl -i -X POST "http://localhost:8081/api/authors" -H "Content-Type: application/json" -d '{}'
```
![Crear autor sin nombre](image-6.png)

### Listar autores
```bash
curl -i -X GET "http://localhost:8081/api/authors"
```
![Listar autores](image-7.png)

### Pedir un autor
```bash
curl -i -X GET "http://localhost:8081/api/authors/1"
```
![Pedir un autor](image-8.png)

### Autor que no existe
```bash
curl -i -X GET "http://localhost:8081/api/authors/3"
```
![Autor que no existe](image-9.png)

## Series

### Crear serie
```bash
curl -i -X POST "http://localhost:8081/api/series" -H "Content-Type: application/json" -d '{"name":"Harry Potter", "seriesType":"saga"}'
```
![Crear serie](image-10.png)

### Serie con tipo inválido
```bash
curl -i -X POST "http://localhost:8081/api/series" -H "Content-Type: application/json" -d '{"name":"Harry Potter", "seriesType":"novela"}'
```
![Serie con tipo inválido](image-11.png)

### Serie sin tipo
```bash
curl -i -X POST "http://localhost:8081/api/series" -H "Content-Type: application/json" -d '{"name":"Harry Potter"}'
```
![Serie sin tipo](image-12.png)

### Listar series
```bash
curl -i -X GET "http://localhost:8081/api/series"
```
![Listar series](image-13.png)

### Pedir una serie
```bash
curl -i -X GET "http://localhost:8081/api/series/1"
```
![Pedir una serie](image-14.png)

### Serie que no existe
```bash
curl -i -X GET "http://localhost:8081/api/series/3"
```
![Serie que no existe](image-15.png)

## POST y GET Libros

### Crear libro
```bash
curl -i -X POST "http://localhost:8081/api/books" -H "Content-Type: application/json" -d '{"mainTitle":"Harry Potter", "totalPages":"300"}'
```
![Crear libro](image-16.png)

### Libro con 0 páginas
```bash
curl -i -X POST "http://localhost:8081/api/books" -H "Content-Type: application/json" -d '{"mainTitle":"Harry Potter", "totalPages":"0"}'
```
![Libro con 0 páginas](image-17.png)

### Libro sin páginas
```bash
curl -i -X POST "http://localhost:8081/api/books" -H "Content-Type: application/json" -d '{"mainTitle":"Harry Potter"}'
```
![Libro sin páginas](image-18.png)

### Libro sin título
```bash
curl -i -X POST "http://localhost:8081/api/books" -H "Content-Type: application/json" -d '{"totalPages":"3"}'
```
![Libro sin título](image-19.png)

### Listar libros
```bash
curl -i -X GET "http://localhost:8081/api/books"
```
![Listar libros](image-20.png)

### Pedir un libro
```bash
curl -i -X GET "http://localhost:8081/api/books/1"
```
![Pedir un libro](image-21.png)

### Libro que no existe
```bash
curl -i -X GET "http://localhost:8081/api/books/3"
```
![Libro que no existe](image-22.png)