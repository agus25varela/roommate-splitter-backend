# Roommate Splitter - Backend

API REST para dividir gastos entre compañeros de departamento. Aplicación desarrollada con **Spring Boot 4.1.0**, **Java 21** y **PostgreSQL**.

## Arquitectura de Capas

El proyecto está organizado en una **arquitectura limpia de 6 capas**, siguiendo principios SOLID y separación de responsabilidades:

```
src/main/java/com/example/roommatesplitter/
│
├── model/                          [ENTIDADES JPA]
│   ├── Usuario.java              → Entidad de usuario
│   ├── Gasto.java                → Entidad de gasto
│   └── Deuda.java                → Entidad de deuda
│
├── dto/                            [DATA TRANSFER OBJECTS]
│   ├── GastoDTO.java             → Recibe datos del cliente
│   ├── GastoResponseDTO.java     → Responde al cliente
│   ├── DeudaDTO.java             → DTO de deudas
│   └── BalanceDTO.java           → DTO de balances calculados
│
├── repository/                 
│   ├── UsuarioRepository.java
│   ├── GastoRepository.java        [ACCESO A DATOS - JPA]
│   └── DeudaRepository.java
│
├── service/                        [LÓGICA DE NEGOCIO]
│   ├── GastoService.java         → CRUD y validaciones
│   └── DeudaService.java         → Cálculo de balances y deudas
│
├── controller/                     [ENDPOINTS REST]
│   ├── GastoController.java      → /api/gastos
│   └── UsuarioController.java    → /api/usuarios
│
├── config/                         [CONFIGURACIÓN]
│   └── SecurityConfig.java       → CORS, Seguridad, Validación
│
└── RoommateApplication.java [MAIN]
```

### Responsabilidades por Capa

| Capa | Responsabilidad | Regla |
|------|-----------------|-------|
| **Controller** | Maneja peticiones HTTP, valida DTOs, delega | SIN lógica de negocio |
| **Service** | Contiene lógica de negocio, orquesta procesos, valida reglas | SIN acceso directo a HTTP |
| **Repository** | Acceso a datos mediante JPA/Hibernate | SIN lógica de negocio |
| **Model** | Entidades JPA simples con @Entity | SIN métodos complejos |
| **DTO** | Aisla entidades de cambios en API | Validación con Jakarta annotations |
| **Config** | CORS, Seguridad, Beans, Infraestructura | Configuración centralizada |
 
---

## Quick Start

### Requisitos
- **Java 21+**
- **Maven 3.9+**
- **PostgreSQL 14+**

## Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/agus25varela/roommate-splitter-backend.git
cd roommate-splitter-backend
```

### 2. Configurar BD
```sql
CREATE DATABASE roommate_db;
```

### 3. Levantar la app
```bash
mvn clean spring-boot:run
```

API escucha en `http://localhost:8080`

## Endpoints

### Usuarios

```bash
GET    /api/usuarios              # Obtener todos los usuarios
GET    /api/usuarios/{id}         # Obtener usuario por ID
POST   /api/usuarios              # Crear usuario
PUT    /api/usuarios/{id}         # Actualizar usuario
DELETE /api/usuarios/{id}         # Eliminar usuario
```

### Gastos

```bash
GET    /api/gastos?usuarioId=1           # Listar gastos del usuario
POST   /api/gastos                       # Crear nuevo gasto
GET    /api/gastos/{id}                  # Obtener gasto por ID
PUT    /api/gastos/{id}                  # Actualizar gasto
DELETE /api/gastos/{id}                  # Eliminar gasto
GET    /api/gastos/balances?usuarioId=1  # Calcular balances/deudas
```

**Ejemplo - Obtener balances:**
```bash
curl -X GET "http://localhost:8080/api/gastos/balances?usuarioId=1"
```
---

## CORS Configurado

El backend permite peticiones desde:
- `http://localhost:5173` (Vue dev server)
- `http://localhost:3000` (alternativa)
- `https://roommate-splitter-one.vercel.app` (producción)
  Configurado en `SecurityConfig.java`.

---

## Validación

Los DTOs incluyen validación con **Jakarta Validation**:

```java
@NotBlank(message = "Descripción no puede estar vacía")
@Size(min = 3, max = 200)
private String descripcion;
 
@NotNull(message = "Monto no puede ser nulo")
@DecimalMin(value = "0.01")
private BigDecimal monto;
```

Errores de validación devuelven **HTTP 400**.

---

## Stack Tecnológico

- **Framework:** Spring Boot 4.1.0
- **Lenguaje:** Java 21
- **Base de Datos:** PostgreSQL 14+
- **Build:** Maven 3.9+
- **ORM:** JPA/Hibernate
- **Seguridad:** Spring Security
- **Validación:** Jakarta Bean Validation
- **Pool de Conexiones:** HikariCP (automático con Spring Boot)
---

## Próximas Features

- [ ] Manejo centralizado de excepciones (GlobalExceptionHandler)
- [ ] Autenticación con JWT
- [ ] Testing (JUnit 5 + Mockito)
- [ ] Endpoint para marcar deudas como pagadas
- [ ] Filtros avanzados por rango de fechas
- [ ] Despliegue en plataforma gratuita compatible con Java
---

## Frontend
Repo separado: [roommate-splitter-frontend](https://github.com/agus25varela/roommate-splitter-frontend)

---
## Autor
Agustina Varela - [@agus25varela](https://github.com/agus25varela) 

**Junior Full Stack Developer | Buenos Aires, Argentina**