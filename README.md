# 🩺VetCare

Sistema full stack para gestionar una clínica veterinaria: dueños, mascotas, veterinarios, especialidades y citas, con inicio de sesión y permisos según el rol de cada usuario.

**Demo en línea**
Aplicación web: https://martiny-developer.github.io/vetcare-web/
> La API corre en un plan gratuito que se apaga por inactividad: el primer inicio de sesión puede tardar hasta un minuto. La pantalla de login lo avisa mientras espera.

## Captura

<img width="1228" height="593" alt="image" src="https://github.com/user-attachments/assets/f365e942-7713-4bf2-a7f6-da02542a0d44" />

## Cuentas de demostración

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Vetcare2026` | ADMIN |
| `recepcion1` | `clave1234` | RECEPCIONISTA |
| `veterinario1` | `clave1234` | VETERINARIO |

Son cuentas de prueba con datos ficticios.

## Qué demuestra este proyecto

- Una API REST en capas con Spring Boot (controller, service y repository) que usa DTO en lugar de exponer las entidades.
- Autenticación y autorización con JWT y tres roles, sin sesiones en el servidor.
- Un frontend en Angular que consume la API, con interceptor HTTP, guards de rutas y formularios reactivos.
- Despliegue real en la nube: base de datos, API y frontend en servicios distintos, con CORS y configuración por variables de entorno.

## Arquitectura general

```
Navegador
   │   Angular (GitHub Pages)
   │   HTTPS + token JWT en el encabezado Authorization
   ▼
API REST con Spring Boot (Render, contenedor Docker)
   │   JDBC con SSL
   ▼
PostgreSQL (Neon)
```

## Tecnologías

| Capa | Tecnologías |
|---|---|
| Backend | Java 21, Spring Boot 4.1 (Web MVC, Security, Data JPA, Validation), Hibernate, jjwt, Lombok, Maven |
| Frontend | Angular 22 (componentes standalone y signals), TypeScript, RxJS, formularios reactivos, Bootstrap 5 y Bootstrap Icons |
| Datos e infraestructura | PostgreSQL en Neon, Docker, Render, GitHub Actions y GitHub Pages |

## Funcionalidades

- Registro y consulta de dueños, mascotas, veterinarios y especialidades.
- Agenda de citas con estados (`PENDIENTE`, `ATENDIDA`, `CANCELADA`) y acción de atender para el veterinario.
- Inicio de sesión con token JWT y cierre de sesión.
- Panel de inicio con totales de dueños, mascotas y citas pendientes, y la lista de próximas citas.
- Botones y formularios visibles según el rol del usuario.
- Contraseñas guardadas con hash BCrypt.
- Validación de datos de entrada y respuestas de error en JSON con el código HTTP adecuado (400, 401, 403, 404 y 409).

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

Los permisos se aplican en la API. El frontend solo oculta lo que el usuario no puede usar.

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

## Backend

Estructura de este repositorio:

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
- **Seguridad sin sesión:** cada petición se autentica con su token, y el filtro consulta el rol vigente en la base de datos.
- **Errores centralizados:** un `@RestControllerAdvice` convierte las excepciones propias en respuestas JSON coherentes.
- **Configuración por perfiles:** el perfil `prod` lee la base de datos, la clave JWT y los orígenes CORS desde variables de entorno.

## Frontend

El código está en otro repositorio (ver el final de este documento). Su estructura:

```
src/app/
├── core/        servicio de autenticación, interceptor del token y guards de rutas
├── features/    pantallas: login, inicio, dueños, mascotas y citas
├── models/      interfaces que reflejan los DTO de la API
└── services/    servicios que llaman a la API
```

Decisiones de diseño:

- **Interceptor HTTP:** agrega el token a cada petición hacia la API y cierra la sesión si el servidor responde 401.
- **Guards de ruta:** exigen sesión iniciada y, cuando hace falta, un rol concreto.
- **Sesión en `sessionStorage`:** se borra al cerrar la pestaña.
- **Aviso de espera:** si la API está dormida, el login muestra "Iniciando VetCare" con un mensaje explicativo.

## Despliegue

| Parte | Dónde | Cómo |
|---|---|---|
| API | Render (plan gratuito) | Imagen Docker en dos etapas definida en el `Dockerfile` |
| Base de datos | Neon | PostgreSQL con conexión SSL |
| Frontend | GitHub Pages | Flujo de GitHub Actions que compila Angular y lo publica en cada cambio de la rama `main` |

## Retos que resolví

- **Relaciones bidireccionales:** serializar dueño y mascotas a JSON producía un ciclo infinito. Lo resolví con DTO de resumen que cortan la recursión.
- **Configuración distinta en local y en producción:** separé los perfiles y dejé los secretos de producción solo en variables de entorno.
- **CORS entre dominios:** la web en GitHub Pages y la API en Render son orígenes distintos, así que los orígenes permitidos se configuran por variable de entorno.
- **Rutas de Angular en GitHub Pages:** recargar una sección daba 404; el flujo de despliegue copia `index.html` a `404.html`.
- **Servidor gratuito que se duerme:** el login avisa con claridad que el arranque puede tardar.
- **Creación de usuarios segura:** solo un administrador puede crear usuarios, para que nadie pueda registrarse a sí mismo como `ADMIN`.


## Autor

**Martiny** · Desarrollador Backend Java


## Código del frontend

El frontend en Angular vive en su propio repositorio: **[vetcare-web](https://github.com/martiny-developer/vetcare-web)**
