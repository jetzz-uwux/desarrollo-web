**Herramienta:** Claude
**Respuesta obtenida:** 
La regla: por cada endpoint, una prueba que funcione y una que falle

Son cuatro recursos. Te dejo qué probar y qué debe responder cada uno:

|#|Prueba|Qué mandas|Código esperado|
|---|---|---|---|
|1|Crear género|POST /api/genres con name|201|
|2|Crear género sin nombre|POST /api/genres con {}|400|
|3|Listar géneros|GET /api/genres|200|
|4|Género que existe|GET /api/genres/1|200|
|5|Género que no existe|GET /api/genres/999|404|
|6|Crear autor|POST /api/authors con name|201|
|7|Crear autor sin nombre|POST /api/authors con {}|400|
|8|Listar autores y pedir uno|GET /api/authors y GET /api/authors/1|200|
|9|Autor que no existe|GET /api/authors/999|404|
|10|Crear serie|POST /api/series con name y seriesType: "saga"|201|
|11|Serie con tipo inválido|seriesType: "novela"|400|
|12|Serie sin tipo|solo name|400|
|13|Listar series y pedir una|GET /api/series y GET /api/series/1|200|
|14|Serie que no existe|GET /api/series/999|404|
|15|Crear libro|POST /api/books con mainTitle y totalPages: 300|201|
|16|Libro con 0 páginas|totalPages: 0|400|
|17|Libro sin páginas|solo mainTitle|400|
|18|Libro sin título|totalPages pero sin mainTitle|400|
|19|Listar libros y pedir uno|GET /api/books y GET /api/books/1|200|
|20|Libro que no existe|GET /api/books/999|404|

Hazlas en ese orden. Los POST crean los datos que los GET después consultan, así que no necesitas un archivo de datos de prueba.