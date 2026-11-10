# desarrollo-web

## Base de datos (PostgreSQL)

Requisitos: WSL con Ubuntu y PostgreSQL 14 o superior.

### 1. Instalar PostgreSQL (solo la primera vez)

```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib
```

### 2. Iniciar el servicio

```bash
sudo service postgresql start
```

### 3. Crear usuario y base de datos

Cambia `tu_contraseña` por una contraseña propia.
```bash
sudo -u postgres psql -c "CREATE USER booknest_user WITH PASSWORD 'tu_contraseña';"
sudo -u postgres psql -c "CREATE DATABASE booknest OWNER booknest_user;"
```

### 4. Crear el esquema

Desde la raíz del repositorio:

```bash
PGPASSWORD='tu_contraseña' psql -h localhost -U booknest_user -d booknest -f database/schema.sql
```

Al final debe aparecer `COMMIT`. El script es transaccional: si algo falla, no se crea nada.

### 5. Verificar

```bash
PGPASSWORD='tu_contraseña' psql -h localhost -U booknest_user -d booknest -c '\dt'
```

Deben aparecer 15 tablas (y 2 vistas con `\dv`).

### Reiniciar la base de datos desde cero

```bash
sudo -u postgres dropdb booknest
sudo -u postgres psql -c "CREATE DATABASE booknest OWNER booknest_user;"
PGPASSWORD='tu_contraseña' psql -h localhost -U booknest_user -d booknest -f database/schema.sql
```

### Valores de los campos con opciones fijas

- `series_type`: `bilogia`, `trilogia`, `saga`
- `language` (archivos y títulos alternativos): `es`, `en`
- `status` (progreso de lectura): `pendiente`, `leyendo`, `terminado`

### Conexión desde Spring Boot

En `backend/.../application.properties` (no subas la contraseña real al repositorio):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booknest
spring.datasource.username=booknest_user
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
```