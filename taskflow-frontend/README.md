# TaskFlow Frontend

SPA construida con Angular 17 que consume el backend de TaskFlow. Proporciona autenticación JWT, tablero Kanban para tareas y detección de caídas del backend con una experiencia cuidada usando Bootstrap 5.

## ✅ Características principales

- Inicio de sesión y registro con JWT; almacenamiento seguro del token en `localStorage`.
- Protección de rutas con `authGuard` y refresco del perfil al cargar la aplicación.
- Interceptor `jwtInterceptor` que inyecta el token, maneja 401 y detecta cortes del backend.
- Tablero Kanban con columnas *Por hacer*, *En progreso* y *Completada* con drag & drop básico.
- Perfil de usuario con métricas rápidas y botón para refrescar datos.
- Modal global de "Servicio no disponible" cuando la API responde 0/503 y redirección automática al login.

## 📦 Requisitos

- Node.js 20+ (recomendado 20 LTS)
- Angular CLI 17.x global (`npm install -g @angular/cli@17`)
- Backend de TaskFlow corriendo en `http://localhost:8081` (por proxy)

## 🚀 Puesta en marcha

```bash
# Instalar dependencias
npm install

# Levantar servidor de desarrollo (http://localhost:4200)


# Compilar para producción
npm run build
```

El servidor de desarrollo utiliza `proxy.conf.json` para redirigir las llamadas a `/api` hacia el backend Spring Boot. Asegúrate de que el backend esté activo para evitar el modal de indisponibilidad permanente.

## 🔧 Configuración de entornos

Las URLs base de la API se definen en:

- `src/environments/environment.ts`
- `src/environments/environment.development.ts`

Por defecto apuntan a `/api`, aprovechando el proxy. Si despliegas el frontend de forma independiente, actualiza `apiUrl` con la URL pública del backend.

## 🧱 Arquitectura del frontend

```
src/app/
├── app.component.*          # Layout, navbar y modal global
├── app.config.ts            # Bootstrap de la aplicación y providers
├── app.routes.ts            # Definición de rutas standalone
├── core/
│   ├── guards/auth.guard.ts             # Protección de rutas
│   ├── interceptors/jwt.interceptor.ts  # Manejo de token/errores
│   └── services/
│       ├── auth.service.ts              # Autenticación y perfil
│       ├── system-status.service.ts     # Estado del backend
│       ├── task.service.ts              # CRUD de tareas
│       └── token-storage.service.ts     # Persistencia de JWT
└── features/
	├── auth/                            # Login y registro
	├── dashboard/                       # Tablero Kanban
	└── profile/                         # Información del usuario
```

Los componentes usan la modalidad *standalone* de Angular 17 y estilos de Bootstrap 5 definidos en `src/styles.scss`.

## 🧪 Scripts disponibles

- `npm start`: Ejecuta `ng serve` con proxy a la API.
- `npm run build`: Genera artefactos de producción en `dist/taskflow-frontend/`.
- `npm run lint`: Ejecuta el analizador estático (si está configurado en `angular.json`).
- `npm test`: Ejecuta pruebas unitarias con Karma (pendiente de configurar en el MVP actual).

## 🔄 Manejo de indisponibilidad del backend

El interceptor marca el backend como no disponible cuando recibe códigos `0` (fallo de red) o `503`. `SystemStatusService` expone un `BehaviorSubject` que el `AppComponent` consume para mostrar un modal bloqueante con opciones de cerrar o reintentar (recarga la página). Además, se fuerza la navegación al login y se limpia el token para evitar estados inconsistentes.

## 🧭 Próximos pasos sugeridos

- Agregar pruebas unitarias para el guard, interceptor y componentes principales.
- Incorporar drag & drop real con `@angular/cdk/drag-drop`.
- Internacionalización (i18n) para soportar múltiples idiomas.
- Pipeline de CI/CD que construya y despliegue el frontend junto al backend.
