# FitChain - Sistema de Gestión de Gimnasio

Sistema de gestión integral para cadena de gimnasios, desarrollado con arquitectura de microservicios usando Spring Boot.

## Integrantes

- Matias Rodriguez
- Cristofer Cifuentes

## Repositorio

https://github.com/FitChainn

---

## Descripción

FitChain es una plataforma backend distribuida que gestiona todas las operaciones de una cadena de gimnasios: clientes, entrenadores, establecimientos, membresías, pagos, reservas, asistencias, horarios, equipos y notificaciones. El sistema incluye autenticación JWT y un API Gateway centralizado.

---

## Microservicios

| Servicio         | Puerto | Base de Datos                  |
|------------------|--------|-------------------------------|
| Cliente          | 8081   | db_fitchain_clientes          |
| Entrenador       | 8082   | db_fitchain_entrenadores      |
| Equipo           | 8083   | db_fitchain_equipo            |
| Establecimiento  | 8084   | db_fitchain_establecimiento   |
| Asistencia       | 8085   | db_fitchain_asistencia        |
| Horario          | 8086   | db_fitchain_horario           |
| Reserva          | 8087   | db_fitchain_reserva           |
| Membresía        | 8088   | db_fitchain_membresia         |
| Pago             | 8089   | db_fitchain_pago              |
| Notificación     | 8090   | db_fitchain_notificacion      |
| Gateway          | 8091   | (sin base de datos)           |
| Auth             | 8092   | db_fitchain_auth              |

---

## Funcionalidades implementadas

- Gestión completa de clientes con asignación a entrenador y establecimiento
- Gestión de entrenadores con especialidad y establecimiento asignado
- Control de equipos por establecimiento con resumen por tipo
- Administración de establecimientos con vista de entrenadores, clientes y equipos
- Registro de asistencias vinculadas a cliente y horario
- Gestión de horarios por establecimiento y día de la semana
- Reservas de actividades con estado (PENDIENTE, CONFIRMADA, CANCELADA)
- Membresías con tipos MENSUAL, TRIMESTRAL y ANUAL con cálculo automático de fecha fin
- Registro de pagos por cliente con distintos métodos de pago
- Envío de notificaciones a clientes por tipo y estado
- Autenticación con JWT, encriptación BCrypt y roles: ADMIN, ENTRENADOR, CLIENTE
- API Gateway con filtro JWT para todas las rutas protegidas
- Comunicación entre microservicios mediante WebClient

---

## Tecnologías

- Java 21
- Spring Boot 3.2.5
- Spring Cloud Gateway
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + Hibernate
- MySQL (XAMPP)
- Lombok
- Bean Validation (JSR 380)
- WebClient (WebFlux)

---

## Requisitos previos

- Java 21
- Maven 3.9+
- XAMPP con MySQL activo
- IntelliJ IDEA (recomendado)

---

## Pasos para ejecutar

### 1. Crear las bases de datos en MySQL

Ejecutar en phpMyAdmin o consola MySQL:

```sql
CREATE DATABASE db_fitchain_clientes;
CREATE DATABASE db_fitchain_entrenadores;
CREATE DATABASE db_fitchain_equipo;
CREATE DATABASE db_fitchain_establecimiento;
CREATE DATABASE db_fitchain_asistencia;
CREATE DATABASE db_fitchain_horario;
CREATE DATABASE db_fitchain_reserva;
CREATE DATABASE db_fitchain_membresia;
CREATE DATABASE db_fitchain_pago;
CREATE DATABASE db_fitchain_notificacion;
CREATE DATABASE db_fitchain_auth;
```

### 2. Iniciar los microservicios

Abrir cada proyecto en IntelliJ y ejecutar en este orden:

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

O por terminal en la carpeta de cada microservicio:

```bash
./mvnw spring-boot:run
```

### 3. Obtener token de acceso

```
POST http://localhost:8091/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Usuarios disponibles por defecto:

| Username     | Password        | Rol        |
|--------------|-----------------|------------|
| admin        | admin123        | ADMIN      |
| entrenador1  | entrenador123   | ENTRENADOR |
| cliente1     | cliente123      | CLIENTE    |

### 4. Usar el token en las peticiones

Agregar en el header de cada request:

```
Authorization: Bearer <token_obtenido>
```

---
## Arquitectura

```
Cliente/Postman
      │
      ▼
API Gateway (8091)  ←── Valida JWT con Auth Service
      │
      ├── /v1/clientes/**        → Cliente (8081)
      ├── /v1/entrenadores/**   → Entrenador (8082)
      ├── /v1/equipos/**         → Equipo (8083)
      ├── /v1/establecimientos/**→ Establecimiento (8084)
      ├── /v1/asistencias/**     → Asistencia (8085)
      ├── /v1/horarios/**        → Horario (8086)
      ├── /v1/reservas/**        → Reserva (8087)
      ├── /v1/membresias/**      → Membresía (8088)
      ├── /v1/pagos/**           → Pago (8089)
      ├── /v1/notificaciones/**  → Notificación (8090)
      └── /v1/auth/**            → Auth (8092)
# FitChain - Referencia de Endpoints

Todos los requests deben incluir el header:
```
Authorization: Bearer <token>
```

Obtener token: `POST http://localhost:8091/v1/auth/login`

---

## CLIENTE (8081) → Gateway: /v1/clientes/**

```
GET    /v1/clientes
GET    /v1/clientes/{id}
POST   /v1/clientes
DELETE /v1/clientes/{id}
PUT    /v1/clientes/{clienteId}/entrenador/{entrenadorId}
GET    /v1/clientes/entrenador/{entrenadorId}
GET    /v1/clientes/entrenador/{entrenadorId}/simple
GET    /v1/clientes/establecimiento/{establecimientoId}
```

### Crear cliente
```json
POST /v1/clientes
{
  "nombre": "Juan Pérez",
  "run": "12345678-k",
  "fechaNacimiento": "1990-12-15",
  "entrenadorId": 1,
  "establecimientoId": 1
}
```

---

## ENTRENADOR (8082) → Gateway: /api/entrenadores/**

```
GET    /api/entrenadores
GET    /api/entrenadores/{id}
GET    /api/entrenadores/{id}/simple
POST   /api/entrenadores
DELETE /api/entrenadores/{id}
GET    /api/entrenadores/establecimiento/{establecimientoId}
PUT    /api/entrenadores/{entrenadorId}/establecimiento/{establecimientoId}
```

### Crear entrenador
```json
POST /api/entrenadores
{
  "run": "19482021-0",
  "nombre": "Guillermo Salas",
  "especialidad": "Crossfit",
  "fechaNacimiento": "1980-12-10",
  "establecimientoId": 1
}
```

---

## EQUIPO (8083) → Gateway: /v1/equipos/**

```
GET    /v1/equipos
GET    /v1/equipos/{id}
POST   /v1/equipos
PUT    /v1/equipos/{id}
DELETE /v1/equipos/{id}
GET    /v1/equipos/establecimiento/{establecimientoId}
GET    /v1/equipos/establecimiento/{establecimientoId}/total
GET    /v1/equipos/establecimiento/{establecimientoId}/resumen
```

### Crear equipo
```json
POST /v1/equipos
{
  "tipoMaquina": "Cinta de Correr",
  "marca": "Life Fitness",
  "fechaCompra": "2022-03-15",
  "estado": "Operativo",
  "establecimientoId": 1
}
```

---

## ESTABLECIMIENTO (8084) → Gateway: /v1/establecimientos/**

```
GET    /v1/establecimientos
GET    /v1/establecimientos/{id}
POST   /v1/establecimientos
DELETE /v1/establecimientos/{id}
GET    /v1/establecimientos/{id}/entrenadores
GET    /v1/establecimientos/{id}/clientes
```

### Crear establecimiento
```json
POST /v1/establecimientos
{
  "nombre": "FitChain Santiago",
  "direccion": "Av. Providencia 1234, Santiago"
}
```

---

## ASISTENCIA (8085) → Gateway: /v1/asistencias/**

```
GET    /v1/asistencias
GET    /v1/asistencias/{id}
POST   /v1/asistencias
DELETE /v1/asistencias/{id}
GET    /v1/asistencias/cliente/{clienteId}
GET    /v1/asistencias/horario/{horarioId}
```

### Registrar asistencia
```json
POST /v1/asistencias
{
  "clienteId": 1,
  "horarioId": 1,
  "fecha": "2026-05-20"
}
```

---

## HORARIO (8086) → Gateway: /v1/horarios/**

```
GET    /v1/horarios
GET    /v1/horarios/{id}
POST   /v1/horarios
PUT    /v1/horarios/{id}
DELETE /v1/horarios/{id}
GET    /v1/horarios/establecimiento/{establecimientoId}
GET    /v1/horarios/establecimiento/{establecimientoId}/dia/{diaSemana}
GET    /v1/horarios/dia/{diaSemana}
```

### Crear horario
```json
POST /v1/horarios
{
  "establecimientoId": 1,
  "diaSemana": "LUNES",
  "horaApertura": "07:00:00",
  "horaCierre": "21:00:00",
  "abierto": true
}
```

---

## RESERVA (8087) → Gateway: /v1/reservas/**

```
GET    /v1/reservas
GET    /v1/reservas/{id}
POST   /v1/reservas
PUT    /v1/reservas/{id}
DELETE /v1/reservas/{id}
GET    /v1/reservas/cliente/{clienteId}
GET    /v1/reservas/estado/{estado}
```

### Crear reserva
```json
POST /v1/reservas
{
  "clienteId": 1,
  "horarioId": 1,
  "actividad": "SPINNING",
  "fecha": "2026-05-21",
  "hora": "10:00:00"
}
```

### Estados válidos
```
PENDIENTE | CONFIRMADA | CANCELADA
```

---

## MEMBRESÍA (8088) → Gateway: /v1/membresias/**

```
GET    /v1/membresias
GET    /v1/membresias/{id}
POST   /v1/membresias
PUT    /v1/membresias/{id}
DELETE /v1/membresias/{id}
GET    /v1/membresias/cliente/{clienteId}
GET    /v1/membresias/estado/{estado}
```

### Crear membresía
```json
POST /v1/membresias
{
  "clienteId": 1,
  "tipo": "MENSUAL",
  "fechaInicio": "2026-05-20",
  "precio": 25000
}
```

### Tipos válidos
```
MENSUAL | TRIMESTRAL | ANUAL
```

### Estados válidos
```
ACTIVA | VENCIDA | CANCELADA
```

---

## PAGO (8089) → Gateway: /v1/pagos/**

```
GET    /v1/pagos
GET    /v1/pagos/{id}
POST   /v1/pagos
PUT    /v1/pagos/{id}
DELETE /v1/pagos/{id}
GET    /v1/pagos/cliente/{clienteId}
GET    /v1/pagos/estado/{estado}
```

### Crear pago
```json
POST /v1/pagos
{
  "clienteId": 1,
  "montoPagar": 25000,
  "metodoPago": "EFECTIVO",
  "fechaPago": "2026-05-20"
}
```

### Métodos de pago válidos
```
EFECTIVO | TARJETA | TRANSFERENCIA
```

### Estados válidos
```
PENDIENTE | PAGADO | CANCELADO
```

---

## NOTIFICACIÓN (8090) → Gateway: /v1/notificaciones/**

```
GET    /v1/notificaciones
GET    /v1/notificaciones/{id}
POST   /v1/notificaciones
PUT    /v1/notificaciones/{id}
DELETE /v1/notificaciones/{id}
GET    /v1/notificaciones/cliente/{clienteId}
GET    /v1/notificaciones/estado/{estado}
```

### Crear notificación
```json
POST /v1/notificaciones
{
  "clienteId": 1,
  "tipo": "VENCIMIENTO_MEMBRESIA",
  "mensaje": "Tu membresía vence en 3 días",
  "fechaEnvio": "2026-05-20"
}
```

### Tipos de notificación
```
VENCIMIENTO_MEMBRESIA | CONFIRMACION_PAGO | RECORDATORIO_RESERVA
```

### Estados válidos
```
PENDIENTE | ENVIADA
```

---

## Usuarios de prueba

| Username     | Password        | Rol        |
|--------------|-----------------|------------|
| admin        | admin123        | ADMIN      |
| entrenador1  | entrenador123   | ENTRENADOR |
| cliente1     | cliente123      | CLIENTE    |





