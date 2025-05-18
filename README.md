# Servicio de Autenticación

## Descripción

Este proyecto es un servicio de autenticación robusto desarrollado con Spring Boot que proporciona funcionalidades completas de gestión de usuarios y autenticación mediante tokens JWT (JSON Web Tokens). Está diseñado para ser utilizado como un microservicio independiente que puede integrarse fácilmente con otras aplicaciones.

## Características

- Registro y autenticación de usuarios
- Gestión de sesiones mediante tokens JWT
- Renovación de tokens mediante refresh tokens
- Control de roles y permisos
- Limitación de tasa (rate limiting) para prevenir ataques de fuerza bruta
- Configuración CORS para seguridad en aplicaciones web
- Documentación API con Swagger/OpenAPI
- Sistema de recuperación de contraseña mediante correo electrónico
- Verificación de códigos de recuperación con límite de intentos
- Plantillas de correo electrónico personalizables
- Gestión de tokens de restablecimiento de contraseña

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Security** - Para la gestión de autenticación y autorización
- **Spring Data JPA** - Para la persistencia de datos
- **Hibernate 6.5.2** - Como implementación de JPA
- **PostgreSQL** - Como base de datos relacional
- **JWT (JSON Web Tokens)** - Para la gestión de tokens de autenticación
- **Spring Mail** - Para el envío de correos electrónicos
- **Thymeleaf** - Para plantillas de correo electrónico
- **Lombok** - Para reducir código boilerplate
- **Gradle** - Como sistema de construcción
- **Docker** - Para la contenerización de la aplicación
- **Swagger/OpenAPI** - Para la documentación de la API

## Requisitos Previos

Para ejecutar este proyecto, necesitarás:

- Java 21 o superior
- Docker y Docker Compose (para entorno de desarrollo)
- PostgreSQL (si no utilizas Docker)

## Configuración y Ejecución

### Usando Docker (Recomendado)

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/AuthService.git
   cd AuthService
   ```

2. Configura las variables de entorno en el archivo `.env` (basado en `.env.example`):

   ```
   POSTGRES_DB=auth_db
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=your_password
   JWT_SECRET_KEY=your_secret_key
   ```

3. Inicia los contenedores con Docker Compose:

   ```bash
   docker-compose up -d
   ```

4. La aplicación estará disponible en: `http://localhost:8080/api/v1`

### Ejecución Local

1. Asegúrate de tener PostgreSQL instalado y en ejecución

2. Configura las variables de entorno o modifica `application.properties`

3. Ejecuta la aplicación con Gradle:
   ```bash
   ./gradlew bootRun
   ```

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI:

```
http://localhost:8080/api/v1/swagger-ui.html
```

## Endpoints Principales

- **POST /auth/register** - Registro de nuevos usuarios
- **POST /auth/login** - Autenticación y obtención de tokens
- **POST /auth/refresh** - Renovación de tokens de acceso
- **POST /auth/logout** - Cierre de sesión (revocación de tokens)
- **POST /auth/forgot-password** - Solicitud de recuperación de contraseña
- **POST /auth/verify-reset-code** - Verificación del código de recuperación
- **POST /auth/recover-password** - Establecer nueva contraseña con token de recuperación
- **POST /auth/reset-password** - Cambio de contraseña (usuario autenticado)
- **GET /users/me** - Obtener información del usuario actual

## Seguridad

El servicio implementa varias capas de seguridad:

- Autenticación basada en JWT
- Almacenamiento seguro de contraseñas con BCrypt
- Limitación de tasa para prevenir ataques
- Validación de entradas
- Configuración CORS para aplicaciones web
- Límite de intentos para códigos de recuperación
- Tokens de recuperación con tiempo de expiración
- Validación de tokens en múltiples niveles
- Control de sesiones activas por usuario

## Desarrollo

### Compilación

```bash
./gradlew build
```

### Pruebas

```bash
./gradlew test
```
