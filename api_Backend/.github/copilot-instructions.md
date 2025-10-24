# TaskFlow Backend - Copilot Instructions

## Project Overview
This is a Spring Boot backend for TaskFlow, a task management application using Clean Architecture principles with JWT authentication and PostgreSQL database.

## Architecture Guidelines
- **Clean Architecture**: Separate layers (domain, application, infrastructure, config)
- **Domain Layer**: Entities, repository interfaces, business rules, exceptions
- **Application Layer**: Use cases, DTOs, service interfaces
- **Infrastructure Layer**: Controllers, repository implementations, external adapters
- **Config Layer**: Security, database, and application configuration

## Development Standards
- Use Java 17 features and modern Spring Boot 3.x practices
- Implement proper exception handling with GlobalExceptionHandler
- Follow REST API conventions and HTTP status codes
- Use JWT for stateless authentication
- Validate all inputs using Spring Validation annotations
- Write meaningful commit messages following conventional commits
- Keep methods small, focused, and testable
- Use dependency injection properly

## Security Implementation
- JWT-based authentication with access tokens
- All endpoints except /api/auth/** require authentication
- BCrypt password hashing with proper salt rounds
- CORS configuration for frontend integration
- JWT token validation in security filter chain
- Proper error responses for security violations

## Database Configuration
- PostgreSQL with schema 'taskflow'
- JPA/Hibernate with explicit schema mapping
- Proper entity relationships and cascading
- Repository pattern implementation
- Transaction management with @Transactional

## API Endpoints Structure
```
/api/auth/register - POST (public)
/api/auth/login    - POST (public)
/api/tasks         - GET, POST (authenticated)
/api/tasks/{id}    - DELETE (authenticated)
/api/tasks/{id}/status - PATCH (authenticated)
```

## Error Handling
- Centralized exception handling with @ControllerAdvice
- Custom exceptions: TaskNotFoundException, UserNotFoundException, InvalidCredentialsException, UnauthorizedException
- Standardized error response format with timestamp, status, error, message, path
- Proper HTTP status codes for different error types

## Testing Guidelines
- Unit tests for use cases and domain logic
- Integration tests for controllers and repositories
- Mock external dependencies and databases
- Test security configurations and JWT handling

## Code Quality
- Follow Spring Boot best practices
- Use meaningful variable and method names
- Implement proper logging levels
- Handle null checks and edge cases
- Document complex business logic