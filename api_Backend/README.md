# TaskFlow Backend

## 🚀 Technical Requirements Implemented

### ✅ Authentication & Security
- **JWT Authentication with Access Token**: Real JWT tokens using `io.jsonwebtoken` library
- **Protected Routes Middleware**: Custom `JwtAuthenticationFilter` for route protection
- **Password Hashing with BCrypt**: Secure password storage using BCryptPasswordEncoder
- **CORS Configuration**: Proper cross-origin resource sharing setup

### ✅ Data Validation & Error Handling
- **Input Validation**: Jakarta validation annotations on DTOs
- **HTTP Error Codes**: Proper status codes (200, 201, 400, 401, 404, 500)
- **Global Exception Handler**: Centralized error handling with `@ControllerAdvice`
- **Custom Exceptions**: Domain-specific exceptions for business logic

### ✅ Environment Configuration
- **Environment Variables (.env)**: All sensitive data configurable via environment variables
- **Configuration Profiles**: Support for different environments (dev, prod)
- **Default Values**: Fallback configurations for development

### ✅ Clean Architecture
- **Domain Layer**: Entities, repositories interfaces, business exceptions
- **Application Layer**: Use cases, DTOs, service interfaces
- **Infrastructure Layer**: Controllers, repository implementations, security
- **Configuration Layer**: Security, database, and application setup

## 🛡️ JWT Authentication Flow

### How JWT Bearer Token Validation Works:

1. **Client Login** → Server validates credentials → Returns JWT token
2. **Client Request** → Includes `Authorization: Bearer <token>` header
3. **JWT Filter** → Intercepts request and validates token:
   - ✅ Verifies JWT signature with secret key
   - ✅ Checks token expiration date
   - ✅ Extracts user ID from token claims
   - ✅ Validates user exists in database
   - ✅ Sets Spring Security authentication context

4. **Controller Access** → Request proceeds with authenticated user context

### Security Implementation Details:
- **JwtAuthenticationFilter**: Custom filter that runs before Spring Security
- **TokenService**: Handles JWT generation, validation, and parsing
- **Stateless Authentication**: No server-side sessions, only JWT tokens
- **Automatic Validation**: All protected endpoints automatically validate JWT
- **Error Handling**: Invalid/expired tokens return 401 Unauthorized

## Descripción
TaskFlow es una aplicación web de gestión de tareas personales que permite a los usuarios organizar sus tareas en un tablero tipo Kanban con estados: "Por hacer", "En progreso" y "Completada".

## Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

### Arquitectura
- **Clean Architecture** con separación de capas:
  - `domain/`: Entidades, repositorios abstractos, reglas de negocio
  - `application/`: Casos de uso, DTOs, servicios
  - `infrastructure/`: Controladores, repositorios concretos, adaptadores
  - `config/`: Configuración de seguridad, base de datos

## Funcionalidades

### Autenticación
- ✅ Registro de usuarios con email y contraseña
- ✅ Login con JWT (access token)
- ✅ Middleware de autenticación en rutas protegidas
- ✅ Hash de contraseñas con BCrypt

### Gestión de Tareas
- ✅ Crear tareas (título, descripción, prioridad: alta/media/baja)
- ✅ Listar todas las tareas del usuario
- ✅ Cambiar estado de tarea (Por hacer → En progreso → Completada)
- ✅ Eliminar tareas
- ✅ Filtrar tareas por estado

## Instalación y Configuración

### Prerrequisitos
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- VS Code (recomendado)

### Variables de Entorno
Crea un archivo `.env` basado en `.env.example`:

```bash
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

### Configuración de Base de Datos

1. **Crear el esquema en PostgreSQL:**
```sql
-- Ejecutar los scripts en el directorio /db/
-- schema.sql para crear las tablas
-- seed.sql para datos de prueba
```

2. **Configurar conexión:**
   - Host: `localhost:5432`
   - Database: `postgres`
   - Schema: `taskflow`
   - User: `postgres`
   - Password: `1234`

### Comandos para Ejecutar

```bash
# Instalar dependencias y compilar
mvn clean install

# Ejecutar en modo desarrollo
mvn spring-boot:run

# Ejecutar tests
mvn test

# Generar JAR para producción
mvn clean package
java -jar target/taskflow-backend-0.0.1-SNAPSHOT.jar
```

### Configuración en VS Code

1. **Extensiones recomendadas:**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client (para probar APIs)

2. **Tasks configuradas:**
   - `Build Project`: Compila el proyecto
   - `Run Application`: Ejecuta la aplicación
   - `Run Tests`: Ejecuta las pruebas

## 🔐 API Endpoints

### Authentication (Public)
```
POST /api/auth/register - User registration
POST /api/auth/login    - User login (returns JWT token)
```

### Tasks (Protected - Requires JWT Token)
```
POST   /api/tasks              - Create task
GET    /api/tasks              - Get all user tasks
GET    /api/tasks?status=...   - Filter tasks by status
PATCH  /api/tasks/{id}/status  - Update task status
DELETE /api/tasks/{id}         - Delete task
```

### User Profile (Protected - Requires JWT Token)
```
GET /api/users/profile - Get user profile
```

### 🧪 Testing with Insomnia/Postman

1. **Register a new user (NO returns token for security):**
```json
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
}

Response (201 Created):
{
    "userId": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "message": "User registered successfully"
}
```

2. **Login to get JWT token:**
```json
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
    "email": "john@example.com",
    "password": "password123"
}
```

3. **Use the JWT token for protected endpoints with Bearer Authentication:**

**Get all tasks:**
```json
GET http://localhost:8081/api/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIi...
```

**Create a new task:**
```json
POST http://localhost:8081/api/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIi...
Content-Type: application/json

{
    "title": "New Task",
    "description": "Task description",
    "priority": "HIGH"
}
```

**Valid Priority Values:**
- `"LOW"` - Baja prioridad
- `"MEDIUM"` - Prioridad media
- `"HIGH"` - Alta prioridad

**Update task status:**
```json
PATCH http://localhost:8081/api/tasks/1/status
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIx"i...
Content-Type: application/json

{
    "status": "IN_PROGRESS"
}
```

**Valid Task Status Values:**
- `"TODO"` - Por hacer
- `"IN_PROGRESS"` - En progreso  
- `"COMPLETED"` - Completada

**Get user profile:**
```json
GET http://localhost:8081/api/users/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIx"i...
```

### 🔐 **JWT Bearer Token Authentication:**
- **Header Required**: `Authorization: Bearer <jwt-token>`
- **Validation**: JWT signature, expiration, and user existence
- **Protected Routes**: All endpoints except `/api/auth/*`
- **Token Format**: Standard JWT with user ID, email, and name claims
- **Security**: Stateless authentication with 24-hour expiration

### Ejemplos de Request/Response

**POST /api/auth/register**
```json
{
  "name": "Juan Pérez",
  "email": "juan@email.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "juan@email.com",
  "name": "Juan Pérez"
}
```

**POST /api/tasks**
```json
{
  "title": "Implementar autenticación",
  "description": "Crear sistema de login con JWT",
  "priority": "ALTA"
}
```

## Datos de Prueba

### Usuario de Ejemplo
- **Email:** `admin@taskflow.com`
- **Password:** `admin123`

### Tareas de Ejemplo
1. Diseñar interfaz de usuario (Prioridad: MEDIA)
2. Implementar autenticación JWT (Prioridad: ALTA)
3. Crear API REST (Prioridad: ALTA)
4. Escribir documentación (Prioridad: BAJA)
5. Realizar pruebas unitarias (Prioridad: MEDIA)

## Estructura del Proyecto

```
api_Backend/
├── src/main/java/com/taskflow/
│   ├── domain/
│   │   ├── entities/          # Entidades JPA
│   │   ├── enums/            # Enumeraciones
│   │   ├── exceptions/       # Excepciones personalizadas
│   │   └── repositories/     # Interfaces de repositorio
│   ├── application/
│   │   ├── dtos/            # Data Transfer Objects
│   │   └── usecases/        # Casos de uso
│   ├── infrastructure/
│   │   ├── controllers/     # Controladores REST
│   │   ├── repositories/    # Implementaciones de repositorio
│   │   └── security/        # Configuración JWT
│   ├── config/             # Configuración general
│   └── TaskFlowApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

## Manejo de Errores

El proyecto implementa un sistema centralizado de manejo de errores con `@ControllerAdvice`:

```json
{
  "timestamp": "2025-10-24T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: 123",
  "path": "/api/tasks/123"
}
```

## Desarrollo

### Próximas Funcionalidades
- [ ] Edición completa de tareas
- [ ] Fechas de vencimiento
- [ ] Categorías de tareas
- [ ] Notificaciones
- [ ] Colaboración entre usuarios

### Contribuir
1. Fork del repositorio
2. Crear rama para nueva funcionalidad
3. Hacer commits descriptivos
4. Crear Pull Request

## Licencia
MIT License - ver archivo LICENSE para detalles.