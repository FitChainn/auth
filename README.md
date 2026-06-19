# FitChain - Sistema de Gestión de Gimnasio

Sistema de gestión integral para cadena de gimnasios, desarrollado con arquitectura de microservicios usando Spring Boot.

## Integrantes

- Matias Rodriguez
- Cristofer Cifuentes
- Nicolás Olivares

## Repositorio

🔗 https://github.com/FitChainn

---

## Descripción del Proyecto

FitChain es una plataforma backend distribuida que gestiona todas las operaciones de una cadena de gimnasios. El sistema implementa una arquitectura de microservicios independientes que se comunican entre sí mediante REST (WebClient), centralizados a través de un API Gateway con autenticación JWT.

El dominio abarca: gestión de clientes, entrenadores, establecimientos, equipos, horarios, reservas, membresías, pagos, asistencias y notificaciones.

---

## Microservicios Implementados

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Cliente | 8081 | Gestión de clientes del gimnasio |
| Entrenador | 8082 | Gestión de entrenadores y especialidades |
| Equipo | 8083 | Control de equipos por establecimiento |
| Establecimiento | 8084 | Administración de sedes del gimnasio |
| Asistencia | 8085 | Registro de asistencias a horarios |
| Horario | 8086 | Gestión de horarios por establecimiento |
| Reserva | 8087 | Reservas de actividades |
| Membresía | 8088 | Membresías con cálculo automático de fechas |
| Pago | 8089 | Registro de pagos por cliente |
| Notificación | 8090 | Envío de notificaciones a clientes |
| **Gateway** | **8091** | **API Gateway centralizado con filtro JWT** |
| Auth | 8092 | Autenticación y generación de tokens JWT |

---

## Rutas Principales del Gateway

Todas las peticiones deben incluir el header:
```
Authorization: Bearer <token>
```

Obtener token: `POST http://44.197.145.9:8091/v1/auth/login`

| Ruta Gateway | Microservicio |
|-------------|---------------|
| `/v1/auth/**` | Auth (8092) |
| `/v1/clientes/**` | Cliente (8081) |
| `/api/entrenadores/**` | Entrenador (8082) |
| `/v1/equipos/**` | Equipo (8083) |
| `/v1/establecimientos/**` | Establecimiento (8084) |
| `/v1/asistencias/**` | Asistencia (8085) |
| `/v1/horarios/**` | Horario (8086) |
| `/v1/reservas/**` | Reserva (8087) |
| `/v1/membresias/**` | Membresía (8088) |
| `/v1/pagos/**` | Pago (8089) |
| `/v1/notificaciones/**` | Notificación (8090) |

---

## Documentación Swagger

| Microservicio | URL Local | URL Remota (AWS) |
|--------------|-----------|-----------------|
| Gateway (todos los servicios) | http://localhost:8091/webjars/swagger-ui/index.html | http://44.197.145.9:8091/webjars/swagger-ui/index.html |
| Cliente | http://localhost:8081/swagger-ui/index.html | http://44.197.145.9:8081/swagger-ui/index.html |
| Entrenador | http://localhost:8082/swagger-ui/index.html | http://44.197.145.9:8082/swagger-ui/index.html |
| Equipo | http://localhost:8083/swagger-ui/index.html | http://44.197.145.9:8083/swagger-ui/index.html |
| Establecimiento | http://localhost:8084/swagger-ui/index.html | http://44.197.145.9:8084/swagger-ui/index.html |
| Asistencia | http://localhost:8085/swagger-ui/index.html | http://44.197.145.9:8085/swagger-ui/index.html |
| Horario | http://localhost:8086/swagger-ui/index.html | http://44.197.145.9:8086/swagger-ui/index.html |
| Reserva | http://localhost:8087/swagger-ui/index.html | http://44.197.145.9:8087/swagger-ui/index.html |
| Membresía | http://localhost:8088/swagger-ui/index.html | http://44.197.145.9:8088/swagger-ui/index.html |
| Pago | http://localhost:8089/swagger-ui/index.html | http://44.197.145.9:8089/swagger-ui/index.html |
| Notificación | http://localhost:8090/swagger-ui/index.html | http://44.197.145.9:8090/swagger-ui/index.html |
| Auth | http://localhost:8092/swagger-ui/index.html | http://44.197.145.9:8092/swagger-ui/index.html |

---

## Arquitectura

```
Cliente / Postman
        │
        ▼
API Gateway :8091  ←── Valida JWT con Auth Service :8092
        │
        ├── /v1/auth/**             → Auth          :8092
        ├── /v1/clientes/**         → Cliente        :8081
        ├── /api/entrenadores/**    → Entrenador     :8082
        ├── /v1/equipos/**          → Equipo         :8083
        ├── /v1/establecimientos/** → Establecimiento :8084
        ├── /v1/asistencias/**      → Asistencia     :8085
        ├── /v1/horarios/**         → Horario        :8086
        ├── /v1/reservas/**         → Reserva        :8087
        ├── /v1/membresias/**       → Membresía      :8088
        ├── /v1/pagos/**            → Pago           :8089
        └── /v1/notificaciones/**   → Notificación   :8090
```

### Comunicación entre microservicios (WebClient)

```
Entrenador  ──► Cliente  (asignar entrenador)
Cliente     ──► Entrenador (verificar/obtener entrenador)
Asistencia  ──► Cliente + Horario
Reserva     ──► Cliente + Horario
Membresía   ──► Cliente
Pago        ──► Cliente
Notificación ──► Cliente
Establecimiento ──► Entrenador + Cliente + Equipo
Horario     ──► Establecimiento
Gateway     ──► Auth (validar JWT en cada request)
```

---

## Tecnologías

- Java 21
- Spring Boot 3.2.5
- Spring Cloud Gateway
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + Hibernate
- MySQL 8.0
- Docker + Docker Compose
- Lombok
- Bean Validation (JSR 380)
- WebClient (WebFlux)
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito (pruebas unitarias)
- GitHub Actions (CI/CD)

---

## Ejecución Local

### Requisitos previos
- Java 21
- Maven 3.9+
- XAMPP con MySQL activo (puerto 3306)
- IntelliJ IDEA (recomendado)

### 1. Crear la base de datos

```sql
CREATE DATABASE db_fitchain;
```

### 2. Configurar contraseña MySQL (si aplica)

En cada `application.properties`, ajustar:
```properties
spring.datasource.password=tu_contraseña
```

### 3. Orden de inicio recomendado

```
1. Auth          (puerto 8092)
2. Cliente       (puerto 8081)
3. Entrenador    (puerto 8082)
4. Establecimiento (puerto 8084)
5. Equipo        (puerto 8083)
6. Horario       (puerto 8086)
7. Asistencia    (puerto 8085)
8. Reserva       (puerto 8087)
9. Membresía     (puerto 8088)
10. Pago         (puerto 8089)
11. Notificación (puerto 8090)
12. Gateway      (puerto 8091)
```

Por terminal en cada carpeta del microservicio:
```bash
./mvnw spring-boot:run
```

---

## Despliegue Remoto (AWS con Docker)

El sistema está desplegado en una instancia EC2 de AWS usando Docker Compose.

**IP del servidor:** `44.197.145.9`

### Docker Compose (todos los servicios)

```bash
# Levantar todos los servicios
docker compose up -d

# Levantar un servicio específico
docker compose up -d --build <nombre_servicio>

# Ver estado de los contenedores
docker ps

# Ver logs de un servicio
docker logs <nombre_contenedor> -f
```

### CI/CD con GitHub Actions

Cada microservicio tiene su propio workflow en `.github/workflows/main.yml` que:
1. Sincroniza el código al servidor AWS via rsync
2. Reconstruye y reinicia el contenedor correspondiente

**Secrets requeridos por repositorio:**
```
IP_SERVER   → 44.197.145.9
USERNAME    → ubuntu
KEY         → (clave SSH privada .pem)
PORT        → 22
```

---

## Usuarios de Prueba

| Username | Password | Rol |
|----------|----------|-----|
| admin | admin123 | ADMIN |
| entrenador1 | entrenador123 | ENTRENADOR |
| cliente1 | cliente123 | CLIENTE |

### Obtener token

```bash
POST http://44.197.145.9:8091/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Usar el token

```
Authorization: Bearer <token_obtenido>
```

---

## Pruebas Unitarias

Cada microservicio cuenta con pruebas unitarias en `src/test/java` que cubren:
- **Controller tests** — con MockMvc y Mockito
- **Service tests** — con Mockito para simular repositorios y WebClients
- **Repository tests** — con @DataJpaTest y H2 en memoria

Para ejecutar las pruebas de un microservicio:

```bash
./mvnw test
```

---

## Endpoints Principales

### Auth
```
POST /v1/auth/login      → Obtener token JWT
POST /v1/auth/register   → Registrar usuario
GET  /v1/auth/validar    → Validar token (usado por Gateway)
```

### Cliente
```
GET    /v1/clientes
GET    /v1/clientes/{id}
POST   /v1/clientes
DELETE /v1/clientes/{id}
GET    /v1/clientes/entrenador/{entrenadorId}
GET    /v1/clientes/establecimiento/{establecimientoId}
```

### Entrenador
```
GET    /api/entrenadores
GET    /api/entrenadores/{id}
POST   /api/entrenadores
DELETE /api/entrenadores/{id}
PUT    /api/entrenadores/{entrenadorId}/cliente/{clienteId}
GET    /api/entrenadores/establecimiento/{establecimientoId}
```

> Ver documentación Swagger para el listado completo de endpoints de cada microservicio.
