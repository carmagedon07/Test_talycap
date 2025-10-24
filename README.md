# TaskFlow

## Descripción breve
- Plataforma web para organizar tareas personales en un tablero Kanban con los estados "Por hacer", "En progreso" y "Completada".
- Backend construido en Spring Boot que expone APIs seguras con JWT, validaciones y arquitectura limpia.
- Frontend Angular 17 que consume las APIs, protege rutas, muestra métricas del usuario y detecta caídas del backend con un modal de servicio no disponible.

## Tecnologías utilizadas
- **Backend:** Java 17, Spring Boot 3.2, Spring Security (JWT), Spring Data JPA, PostgreSQL, Maven.
- **Frontend:** Angular 17 (standalone components), RxJS, Bootstrap 5, Angular Router, HttpClient.
- **Infraestructura y herramientas:** Insomnia/Postman para pruebas, VS Code, Node.js 20+, Angular CLI 17.x.

## Instrucciones de instalación
1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/carmagedon07/Test_talycap.git
   cd Test_talycap
   ```
2. **Configurar el backend**
   - Crear un archivo `.env` en `api_Backend/` siguiendo el ejemplo de variables de entorno.
   - IMPORTANTE: Antes de arrancar el backend, crea el esquema y carga los datos iniciales ejecutando los scripts SQL incluidos en el repositorio (`db/schema.sql`). Por ejemplo, usando psql:

```bash
# Conexión directa (ajusta host, user y database según tu entorno)
psql -h localhost -U postgres -d postgres -f db/schema.sql
psql -h localhost -U postgres -d postgres -f db/seed.sql

# O desde una sesión psql
# psql -h localhost -U postgres -d postgres
# \i /ruta/al/repo/db/schema.sql
# \i /ruta/al/repo/db/seed.sql
```

   - Si prefieres, crea la base y las tablas manualmente en tu cliente preferido antes de iniciar la aplicación.
3. **Instalar dependencias del backend**
   ```bash
   cd api_Backend
   mvn clean install
   ```
4. **Instalar dependencias del frontend**
   ```bash
   cd ../taskflow-frontend
   npm install
   ```

## Variables de entorno necesarias (`api_Backend/.env`)
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=1234

# JWT Configuration
JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
```
> Ajusta los valores según tu entorno. El frontend usa `proxy.conf.json` para redirigir las peticiones a `http://localhost:8081/api`, por lo que conviene alinear `SERVER_PORT` (por ejemplo, 8081) o actualizar la configuración del proxy.

## Comandos para ejecutar
- **Backend**
  ```bash
  cd api_Backend
  mvn spring-boot:run          # Ejecutar en modo desarrollo
  mvn test                     # Ejecutar pruebas unitarias
  mvn clean package            # Generar artefacto JAR
  java -jar target/taskflow-backend-0.0.1-SNAPSHOT.jar
  ```
- **Frontend**
  ```bash
  cd taskflow-frontend
  npm start                    # ng serve con proxy a la API (http://localhost:4200)
  npm run build                # Compilación para producción en dist/
  npm run lint                 # Linter (si está configurado)
  npm test                     # Pruebas unitarias con Karma (pendiente de configuración)
  ```

> Al ejecutar el frontend, asegúrate de que el backend esté disponible para evitar el modal de "Servicio no disponible". Si se usa otra URL para la API en producción, actualiza `src/environments/environment*.ts`.

## Cómo probar con Postman o Insomnia

1. **Registrar usuario (solo la primera vez)**
   ```http
   POST http://localhost:8081/api/auth/register
   Content-Type: application/json

   {
     "name": "John Doe",
     "email": "john@example.com",
     "password": "password123"
   }
   ```
   - Respuesta `201 Created` con los datos del usuario. Si ya existe, la API devolverá un error `409`.

2. **Iniciar sesión y obtener token JWT**
   ```http
   POST http://localhost:8081/api/auth/login
   Content-Type: application/json

   {
     "email": "john@example.com",
     "password": "password123"
   }
   ```
   - La respuesta incluye `token`. Copia su valor para las peticiones siguientes.

3. **Configurar variable global (opcional)**
   - En Postman/Insomnia crea una variable `TOKEN` con el valor del JWT.
   - Agrega el header `Authorization: Bearer {{TOKEN}}` en la colección.

4. **Consultar tareas del usuario autenticado**
   ```http
   GET http://localhost:8081/api/tasks
   Authorization: Bearer <token>
   ```
   - Debe devolver arreglo de tareas del usuario. Usar `status=TODO|IN_PROGRESS|COMPLETED` como query param para filtrar.

5. **Crear nueva tarea**
   ```http
   POST http://localhost:8081/api/tasks
   Authorization: Bearer <token>
   Content-Type: application/json

   {
     "title": "Implementar autenticación",
     "description": "Crear sistema de login con JWT",
     "priority": "HIGH"
   }
   ```
   - Respuesta `201 Created` con la tarea creada.

6. **Actualizar estado de una tarea**
   ```http
   PATCH http://localhost:8081/api/tasks/1/status
   Authorization: Bearer <token>
   Content-Type: application/json

   {
     "status": "IN_PROGRESS"
   }
   ```

7. **Obtener perfil del usuario**
   ```http
   GET http://localhost:8081/api/users/profile
   Authorization: Bearer <token>
   ```
   - Permite validar que la sesión está vigente y que el frontend puede refrescar el perfil exitosamente.

> Si la API responde `401`, renueva el token repitiendo el login. Errores `0` o `503` indican indisponibilidad del backend; el frontend mostrará el modal correspondiente.

## Documentación adicional incluida
- `Prueba_BACKEND_TEORICA resuelto.pdf`: respuestas completas a las preguntas teóricas solicitadas para la prueba técnica.
- `Insomnia_2025-10-24.json`: colección de requests para consumir los endpoints del backend con Insomnia/Postman.
