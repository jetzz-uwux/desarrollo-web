**Herramienta:** Gemini (Modelo de Google)

**Respuesta obtenida:** 
Gemini me explicó que en Spring Boot las relaciones `@ManyToOne` se mapean en el JSON enviando un objeto anidado que contenga la llave primaria, por ejemplo: `"user": {"id": 1}`. 

Me proporcionó los siguientes comandos CURL estructurados para realizar las pruebas:

Para insertar una reseña:
`curl -i -X POST "http://localhost:8081/api/reviews" -H "Content-Type: application/json" -d '{"user": {"id": 1}, "book": {"id": 1}, "content": "Excelente libro"}'`

Para insertar una calificación:
`curl -i -X POST "http://localhost:8081/api/stats/rating" -H "Content-Type: application/json" -d '{"user": {"id": 1}, "book": {"id": 1}, "score": 5}'`

También me generó el script SQL con los `INSERT` iniciales de prueba para evitar violaciones de llave foránea:

```sql
INSERT INTO users (username, email, password_hash) VALUES 
('camila', 'camila@booknest.com', 'hash_falso');

INSERT INTO books (main_title, total_pages) VALUES 
('El nombre del viento', 600);