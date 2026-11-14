# CURL - Segunda Entrega
## POST y GET Géneros
_**Responsable**: Juan Luis Gaona Aguirre_

### Prueba 1:
curl -X POST http://localhost:8081/api/users/register -H "Content-Type: application/json" -d "{\"username\":\"JuanLuis\",\"email\":\"juan@gmail.com\",\"passwordHash\":\"user123\",\"bio\":\"Estudiante\",\"avatarUrl\":\"https://avatars.githubusercontent.com/u/260497492?v=4\"}"
![Respuesta](Diapositiva1.PNG)

### Prueba 2:
curl -X POST http://localhost:8081/api/users/login -H "Content-Type: application/json" -d "{\"email\":\"juan@gmail.com\",\"passwordHash\":\"user123\"}"
![Respuesta](Diapositiva2.PNG)

### Prueba 3:
curl -X GET http://localhost:8081/api/collections -H "Content-Type: application/json"
![Respuesta](Diapositiva3.PNG)

### Prueba 4:
curl -X POST http://localhost:8081/api/progress/user/1/book/1 -H "Content-Type: application/json" -d "{\"currentPage\": 45, \"status\": \"leyendo\"}"
![Respuesta](Diapositiva4.PNG)
