**Herramienta:** ChatGPT
**Respuesta obtenida:** 


## 1. Error: columna `category` inexistente

**Error presentado:**
```text
b1_0.category does not exist
```

**Causa:** Se agregó el atributo `category` al modelo Java, pero la columna correspondiente todavía no existía en la tabla `books` de PostgreSQL.

## 2. Error: NullPointerException al crear libros

**Error presentado:**
```text
java.lang.NullPointerException
at mx.uv.booknest.controllers.BookController.crear(BookController.java:62)
```

**Causa:** El controlador intentaba obtener información de una sesión HTTP inexistente mediante `getSession(false)`.

## 3. Modificaciones innecesarias en autenticación

**Problema:** Durante la integración se introdujeron cambios relacionados con sesiones y contraseñas, aunque estas funcionalidades pertenecían al módulo de usuarios.

## 4. Ausencia del formulario para crear series

**Problema:** La interfaz permitía administrar series existentes, pero no mostraba una opción para registrar nuevas series literarias.

## 5. Flujo incompleto de administración de series

**Problema:** No era posible comenzar a gestionar series desde la interfaz cuando todavía no había ninguna registrada.

## 6. Integración incompleta con archivos PDF

**Problema:** La ficha bibliográfica no contaba con una opción para utilizar directamente la funcionalidad existente de subida de archivos PDF.


## Clasificación de los problemas

### Errores de ejecución
- Columna `category` inexistente en PostgreSQL.
- `NullPointerException` en `BookController.crear()`.

### Problemas de implementación e integración
- Modificaciones innecesarias en autenticación.
- Ausencia del formulario para crear series.
- Flujo incompleto de administración de series.
- Integración incompleta con archivos PDF.

## Uso de inteligencia artificial

Se utilizó ChatGPT para analizar los mensajes de error, revisar el código involucrado, identificar posibles causas y proponer correcciones. También se empleó como apoyo para completar funcionalidades pendientes y preparar archivos modificados para su integración en el proyecto.


Pruebas finales de tu módulo
Lista de verificación

[ ] Crear un libro y verificar que se guarde

[ ] Buscar por título, autor y título alternativo

[ ] Aplicar filtros combinados, incluida categoría

[ ] Editar un libro y comprobar los cambios

[ ] Intentar registrar un libro duplicado

[ ] Crear una serie y asociar varios libros en orden

[ ] Consultar la ficha bibliográfica y sus datos

[ ] Agregar un PDF a una ficha existente

[ ] Eliminar un libro sin dependencias y comprobar 
las restricciones
