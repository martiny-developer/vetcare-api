# 🩺VetCare API

API REST para gestionar una clínica veterinaria: dueños, mascotas, veterinarios, especialidades, citas y usuarios, con autenticación JWT y permisos por rol.

> **Demo en línea**
> - Aplicación web: https://martiny-developer.github.io/vetcare-web/
> - API: https://vetcare-api-7nmw.onrender.com
>
> La API corre en un plan gratuito que se apaga por inactividad: la primera petición puede tardar hasta un minuto en responder.

## Captura

<img width="1228" height="593" alt="image" src="https://github.com/user-attachments/assets/f365e942-7713-4bf2-a7f6-da02542a0d44" />

## Cuentas de demostración

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Vetcare2026` | ADMIN |
| `recepcion1` | `clave1234` | RECEPCIONISTA |
| `veterinario1` | `clave1234` | VETERINARIO |

Son cuentas de prueba con datos ficticios.


## Roles y permisos

| Acción | ADMIN | RECEPCIONISTA | VETERINARIO |
|---|:---:|:---:|:---:|
| Consultar dueños, mascotas, veterinarios, especialidades y citas | Sí | Sí | Sí |
| Crear y editar dueños y mascotas | Sí | Sí | No |
| Agendar citas | Sí | Sí | No |
| Atender citas | Sí | No | Sí |
| Crear veterinarios y especialidades | Sí | No | No |
| Eliminar registros | Sí | No | No |
| Crear usuarios | Sí | No | No |

## Tecnologías

- Java 21
- Spring Boot 4.1 (Web MVC, Security, Data JPA, Validation)
- JWT con la librería jjwt
- Hibernate y PostgreSQL (alojado en Neon)
- Lombok y Maven
- Docker y Render para el despliegue

## Funcionalidades

- Registro y consulta de dueños, mascotas, veterinarios y especialidades.
- Agenda de citas con estados (`PENDIENTE`, `ATENDIDA`, `CANCELADA`) y botón de atender para el veterinario.
- Inicio de sesión con token JWT y tres roles: `ADMIN`, `RECEPCIONISTA` y `VETERINARIO`.
- Contraseñas guardadas con hash BCrypt.
- Validación de datos de entrada y respuestas de error en JSON con el código HTTP correcto (400, 401, 403, 404, 409).
- CORS configurable por variable de entorno.


## Endpoints principales

| Recurso | Ruta | Operaciones |
|---|---|---|
| Autenticación | `/api/auth/login` | `POST` (pública) |
| Usuarios | `/api/usuarios` | `POST` |
| Dueños | `/api/duenos` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Mascotas | `/api/mascotas` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Veterinarios | `/api/veterinarios` | `GET`, `GET /{id}`, `POST`, `DELETE /{id}` |
| Especialidades | `/api/especialidades` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Citas | `/api/citas` | `GET`, `GET /{id}`, `POST`, `PATCH /{id}/atender`, `DELETE /{id}` |

Todas las rutas, salvo el login, exigen el encabezado `Authorization: Bearer <token>`.

## Arquitectura

```
src/main/java/com/generation/vetcare/
├── config/        seguridad (SecurityConfig) y datos iniciales
├── controller/    endpoints REST
├── dto/           objetos de entrada y salida (Request, Response y Resumen)
├── exception/     excepciones propias y manejo global de errores
├── model/         entidades JPA y enumeraciones
├── repository/    acceso a datos con Spring Data JPA
├── security/      servicio JWT y filtro de autenticación
└── service/       lógica de negocio
```

Decisiones de diseño:

- **DTO en lugar de entidades:** las entidades no salen de la capa de servicio. Los DTO `Request`, `Response` y `Resumen` evitan ciclos de serialización en las relaciones bidireccionales y exponen solo lo necesario.
- **Seguridad sin sesión:** cada petición se autentica con su token; el filtro consulta el rol vigente en la base de datos.
- **Errores centralizados:** un `@RestControllerAdvice` convierte las excepciones propias en respuestas JSON coherentes.

## Ejecutar en local

Requisitos: Java 21 y PostgreSQL.

1. Crea la base de datos:

   ```sql
   CREATE DATABASE vetcare;
   ```

2. Revisa el usuario y la contraseña de tu PostgreSQL en `src/main/resources/application.properties`.
3. Inicia la aplicación:

   ```bash
   ./mvnw spring-boot:run
   ```

La API queda en `http://localhost:8080`. Al arrancar por primera vez se crea el usuario `admin` de demostración.

## Variables de entorno en producción

Se usan con el perfil `prod`, que ya viene activado en el `Dockerfile`.

| Variable | Descripción |
|---|---|
| `DATABASE_URL` | URL JDBC, por ejemplo `jdbc:postgresql://HOST/NOMBRE_BD?sslmode=require` |
| `DATABASE_USER` | Usuario de la base de datos |
| `DATABASE_PASSWORD` | Contraseña de la base de datos |
| `JWT_SECRET` | Clave para firmar los tokens (mínimo 32 caracteres) |
| `APP_CORS_ALLOWED_ORIGINS` | Orígenes permitidos separados por comas, sin espacios |

## Despliegue

- **API:** imagen Docker en dos etapas, desplegada en Render (plan gratuito).
- **Base de datos:** PostgreSQL en Neon.
- **Frontend:** [vetcare-web](https://github.com/martiny-developer/vetcare-web), publicado en GitHub Pages.


## Autor

**Martiny** · Desarrollador Backend Java
