Actúa como un desarrollador backend experto en Spring Boot. Necesito probar mis endpoints REST para crear reseñas y calificaciones usando CURL en la terminal de WSL. 

Mis entidades `Review` y `Rating` tienen relaciones `@ManyToOne` con `User` y `Book`. ¿Cómo se estructura el JSON en el comando CURL para pasar los IDs al hacer un POST y no obtener un error 400 (Bad Request)? 

También, dame un pequeño script SQL para insertar un par de usuarios y libros de prueba directamente en PostgreSQL, así mis peticiones no fallarán por falta de registros.