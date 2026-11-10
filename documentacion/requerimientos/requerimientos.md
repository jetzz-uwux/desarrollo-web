# Requisitos del sistema — BookNest

## Módulos y responsables

| Integrante | Módulo |
|---|---|
| Jetzaly | Catálogo, búsqueda y series |
| Hans | Archivos colaborativos |
| Juan Luis | Usuarios y biblioteca personal |
| Camila | Reseñas y estadísticas |

## Requisitos funcionales

Prioridad: **Alta** (indispensable), **Media** (después de lo indispensable, según avance) u **Opcional**.

| ID | Requisito | Descripción | Prioridad | Responsable |
|---|---|---|---|---|
| RF-01 | Buscar libros | Buscar libros mediante palabras clave, título o autor. | Alta | Jetzaly |
| RF-02 | Filtrar resultados | Filtrar por género, categoría, autor, idioma (español o inglés), extensión y tipo de serie, combinando filtros. | Alta | Jetzaly |
| RF-03 | Consultar información del libro | Mostrar una ficha compartida por obra con título principal, títulos alternativos, autor, sinopsis, género, datos de serie, reseñas, calificación y archivos disponibles. | Alta | Jetzaly |
| RF-04 | Descargar libros | Descargar los PDF autorizados asociados a la ficha, sin lector PDF integrado. La carpeta de destino depende de la configuración del navegador. | Alta | Hans |
| RF-05 | Agregar libros a colecciones | Incorporar libros a las colecciones personales del usuario. | Alta | Juan Luis |
| RF-06 | Crear colecciones personalizadas | Crear y nombrar colecciones propias. | Alta | Juan Luis |
| RF-07 | Eliminar libros de colecciones | Quitar un libro de una colección sin eliminarlo del catálogo general. | Alta | Juan Luis |
| RF-08 | Publicar reseñas | El usuario autenticado redacta y publica reseñas visibles para otros usuarios. | Alta | Camila |
| RF-09 | Calificar libros | Cada usuario asigna de 1 a 5 estrellas a cada libro, con una única calificación activa por libro, modificable después. | Alta | Camila |
| RF-10 | Crear perfil de usuario | Registrar una cuenta y configurar un perfil personal. No hay rol de administrador. | Alta | Juan Luis |
| RF-11 | Acceder a la cuenta | Iniciar y cerrar sesión para acceder a las funciones personales. | Alta | Juan Luis |
| RF-12 | Guardar información | Almacenar y recuperar usuarios, libros, colecciones, reseñas y calificaciones en la base de datos. | Alta | Todos |
| RF-13 | Mostrar estadísticas personales | Mostrar a cada usuario libros terminados, en curso y pendientes, géneros y autores más leídos, y actividad mensual. | Media | Camila |
| RF-14 | Subir libros | Cualquier usuario autenticado aporta PDF autorizados en español o inglés al catálogo compartido. | Alta | Hans |
| RF-15 | Gestionar archivos subidos | Validar los archivos aportados y asociar uno o varios PDF, con idioma y edición cuando se conozcan, a una ficha de obra. | Alta | Hans |
| RF-16 | Promedio comunitario | Calcular la media de las calificaciones activas de distintos usuarios por libro (4, 5, 4 y 3 dan 4.0). | Alta | Camila |
| RF-17 | Actualizar promedio | Recalcular el promedio al registrar, modificar o eliminar una calificación. | Alta | Camila |
| RF-18 | Mostrar valoraciones | Mostrar promedio y cantidad de valoraciones; si no hay, mostrar «Sin calificar», nunca cero estrellas. | Alta | Camila |
| RF-19 | Ordenar catálogo | Ordenar por mejor valorados, más leídos, número de páginas y, opcionalmente, título o publicación. | Media | Jetzaly |
| RF-20 | Popularidad de lecturas | Calcular «más leídos» por número de usuarios distintos que marcaron el libro como terminado, no por visitas. | Media | Camila |
| RF-21 | Series literarias | Fichas de series con nombre, tipo (bilogía, trilogía, saga), volumen y orden de lectura, y consulta de las obras de cada serie. | Media | Jetzaly |
| RF-22 | Estadísticas colectivas | Datos agregados de la comunidad para rankings públicos (mejor valorados, más leídos, géneros populares), sin exponer estadísticas privadas. | Media | Camila |
| RF-23 | Seguimiento personal | Marcar cada obra como pendiente, leyendo o terminada y registrar manualmente la última página leída. | Alta | Juan Luis |
| RF-24 | Catálogo colaborativo | Los usuarios autenticados crean fichas para obras no registradas, compartidas por toda la comunidad. | Alta | Hans |
| RF-25 | Detectar posibles duplicados | Antes de crear una ficha, buscar coincidencias por ISBN, título principal, títulos alternativos y autor, y mostrarlas para confirmación. | Media | Jetzaly |
| RF-26 | Títulos alternativos | Guardar títulos alternativos en español e inglés y permitir que usuarios autenticados aporten los faltantes. | Media | Hans |
| RF-27 | Reutilizar ficha existente | Si el usuario confirma que la obra ya existe, añadir un PDF a esa ficha sin duplicar libro, reseñas ni calificaciones. | Media | Jetzaly |
| RF-28 | Varios archivos por obra | Mostrar y descargar varios PDF de una misma ficha, identificados por idioma y, si se conoce, edición. | Alta | Hans |
| RF-29 | Progreso calculado | Calcular el porcentaje de lectura (páginas registradas entre páginas totales); impedir valores negativos o mayores al total. | Alta | Juan Luis |
| RF-30 | Perfiles públicos básicos | Consultar aportaciones y reseñas públicas de otros usuarios, sin seguidores, mensajería ni datos privados. | Opcional | Juan Luis |
| RF-31 | Editar y eliminar reseñas propias *(nuevo)* | El autor de una reseña puede modificarla o eliminarla. | Alta | Camila |
| RF-32 | Editar perfil *(nuevo)* | El usuario puede modificar los datos de su perfil. | Alta | Juan Luis |
| RF-33 | Renombrar y eliminar colecciones *(nuevo)* | El usuario puede renombrar o eliminar sus colecciones. | Alta | Juan Luis |
| RF-34 | Editar y eliminar fichas *(nuevo)* | El usuario que creó una ficha puede modificarla y, si no tiene contenido asociado, eliminarla. | Alta | Hans |

## Requisitos no funcionales

| ID | Requisito | Descripción | Responsable |
|---|---|---|---|
| RNF-01 | Interfaz y presentación | Interfaz con CSS, diseño consistente, legible y adaptable a distintos tamaños de pantalla. | Todos (sus pantallas) |
| RNF-02 | Comunicación segura | Usar HTTPS entre navegador y servidor. | Juan Luis |
| RNF-03 | Seguridad de consultas | Consultas parametrizadas o mecanismos equivalentes; nunca concatenar datos de entrada. | Todos |
| RNF-04 | Framework de desarrollo | Backend con Spring Boot. | Todos |
| RNF-05 | Persistencia | Base de datos relacional (PostgreSQL) con Hibernate/JPA; esquema en `database/schema.sql`. | Todos |
| RNF-06 | Control de acceso | Operaciones sobre colecciones, progreso y perfil requieren autenticación y autorización del propietario. Sin rol administrador. | Juan Luis |
| RNF-07 | Protección de credenciales | Contraseñas con hash seguro, nunca en texto plano. | Juan Luis |
| RNF-08 | Validación de entradas | Validar los datos recibidos y tratar de forma segura el contenido de reseñas y formularios. | Todos |
| RNF-09 | Seguridad de archivos | Validar extensión, tipo real y tamaño máximo del PDF; almacenar de forma segura y evitar rutas manipuladas. Solo español o inglés. | Hans |
| RNF-10 | Usabilidad y accesibilidad | Formularios y mensajes comprensibles, navegables con teclado, con contraste y etiquetas adecuados. | Todos |
| RNF-11 | Mantenibilidad | Código en capas claramente separadas (frontend, controlador, servicio, repositorio). | Todos |
| RNF-12 | Disponibilidad de contenido | Aceptar solo contenido de dominio público, propio o con autorización; los archivos se marcan como autorizados al subirse. | Hans |
| RNF-13 | Arquitectura de tres capas *(nuevo)* | Frontend, backend y base de datos separados; el frontend consume la API REST con Fetch y JSON. | Todos |
| RNF-14 | Respuestas HTTP y mensajes *(nuevo)* | La API usa códigos HTTP correctos y la interfaz muestra mensajes claros de éxito o error. | Todos |
| RNF-15 | Ejecución reproducible en WSL *(nuevo)* | El proyecto se desarrolla y ejecuta desde WSL; el README permite clonarlo y correrlo en otro entorno. | Todos |
