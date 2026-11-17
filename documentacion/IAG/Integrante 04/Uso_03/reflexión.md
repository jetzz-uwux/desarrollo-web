### 3. `reflexion.md`
**Tipo de uso:** Depuración de errores de compilación/BD y diseño de pruebas de validación HTTP

**Reflexión del estudiante:**
Consultar con la IA me ayudó a resolver rápido el error de tipos genéricos en el controlador y a detectar por qué no se conservaban los datos tras reiniciar Spring Boot (`ddl-auto=create` estaba recreando el esquema en cada arranque)[cite: 1, 7]. Además, revisar los casos borde con `curl` me permitió confirmar que mis excepciones con `ResponseStatusException` devolvieran exactamente `400`, `404` y `409` según cada regla de negocio.