# Roommate Splitter - Backend

API REST para dividir gastos entre compañeros de departamento. Aplicación desarrollada con **Spring Boot 4.1.0**, **Java 21** y **PostgreSQL**.

## Arquitectura de Capas

El proyecto está organizado en una **arquitectura limpia de capas**, siguiendo principios SOLID y separación de responsabilidades:

```
src/main/java/com/example/roommatesplitter/
│
├── model/                          [ENTIDADES JPA]
│   ├── Usuario.java              → Entidad de usuario
│   └── Gasto.java                → Entidad de gasto
│
├── dto/                            [DATA TRANSFER OBJECTS]
│   ├── GastoDTO.java             → Recibe datos de creación del cliente
│   ├── UpdateGastoDTO.java       → Recibe datos de actualización (campos opcionales)
│   ├── GastoResponseDTO.java     → Responde al cliente
│   ├── UsuarioSignupDTO.java     → Registro de usuario
│   ├── UsuarioLoginDTO.java      → Login de usuario
│   ├── UsuarioResponseDTO.java   → Responde al cliente (sin password)
│   └── BalanceDTO.java           → DTO de balances calculados
│
├── repository/                     [ACCESO A DATOS - JPA]
│   ├── UsuarioRepository.java
│   └── GastoRepository.java
│
├── service/                        [LÓGICA DE NEGOCIO]
│   ├── GastoService.java         → CRUD, validaciones y cálculo de balances
│   └── UsuarioService.java       → Registro, login y autenticación
│
├── controller/                     [ENDPOINTS REST]
│   ├── GastoController.java      → /api/gasto
│   └── UsuarioController.java    → /api/usuario
│
├── exception/                      [MANEJO DE ERRORES]
│   ├── GlobalExceptionHandler.java  → @RestControllerAdvice
│   ├── RecursoNoEncontradoException.java
│   └── CredencialesInvalidasException.java
│
├── config/                         [CONFIGURACIÓN]
│   └── SecurityConfig.java       → CORS, Seguridad, PasswordEncoder
│
└── RoommateSplitterApplication.java [MAIN]
```

### Responsabilidades por Capa

| Capa | Responsabilidad | Regla |
|------|-----------------|-------|
| **Controller** | Maneja peticiones HTTP, valida DTOs, delega | SIN lógica de negocio |
| **Service** | Contiene lógica de negocio, orquesta procesos, valida reglas | SIN acceso directo a HTTP |
| **Repository** | Acceso a datos mediante JPA/Hibernate | SIN lógica de negocio |
| **Model** | Entidades JPA simples con @Entity | SIN métodos complejos |
| **DTO** | Aisla entidades de cambios en API | Validación con Jakarta annotations |
| **Exception** | Manejo global de errores con respuestas `{mensaje}` consistentes | Centralizar en el advice |
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
mvn spring-boot:run
```

API escucha en `http://localhost:8080`

## Endpoints

### Usuarios

```bash
POST   /api/usuario/signup   # Crear usuario (email, nombre, password) → 201
POST   /api/usuario/login    # Login (email, password) → 200 / 401
GET    /api/usuario/{id}     # Obtener usuario por ID → 200 / 404
```

### Gastos

```bash
GET    /api/gasto?usuarioId=1           # Listar gastos del usuario
POST   /api/gasto                       # Crear nuevo gasto
GET    /api/gasto/{id}                  # Obtener gasto por ID
PUT    /api/gasto/{id}                  # Actualizar gasto (campos opcionales)
DELETE /api/gasto/{id}                  # Eliminar gasto
GET    /api/gasto/balances?usuarioId=1  # Calcular balances/deudas
```

**Ejemplo - Obtener balances:**
```bash
curl -X GET "http://localhost:8080/api/gasto/balances?usuarioId=1"
```

Los balances se calculan dinámicamente: se detectan los participantes a partir de los gastos del usuario y se minimizan las transferencias.

---

## CORS Configurado

El backend permite peticiones desde:
- `http://localhost:5173` (Vue dev server)
- `http://localhost:3000` (alternativa)

Configurado en `SecurityConfig.java`. La autenticación NO está enforced: todos los endpoints son `permitAll` y el login solo valida la contraseña (BCrypt) sin emitir token.

---

## Validación

Los DTOs de escritura incluyen validación con **Jakarta Validation**:

```java
@NotBlank(message = "Descripción no puede estar vacía")
@Size(min = 3, max = 200)
private String descripcion;

@NotNull(message = "Monto no puede ser nulo")
@DecimalMin(value = "0.01")
private BigDecimal monto;
```

`UpdateGastoDTO` no impone restricciones: los campos son opcionales y solo se aplican los no-nulos.

Los errores de validación y de negocio devuelven **HTTP 400/401/404** con el cuerpo `{mensaje}` (manejados por `GlobalExceptionHandler`).

---

## Stack Tecnológico

- **Framework:** Spring Boot 4.1.0 (starter `spring-boot-starter-webmvc`)
- **Lenguaje:** Java 21
- **Base de Datos:** PostgreSQL 14+
- **Build:** Maven 3.9+
- **ORM:** JPA/Hibernate (`ddl-auto=update`, sin migraciones)
- **Seguridad:** Spring Security (endpoints públicos, BCrypt para passwords)
- **Validación:** Jakarta Bean Validation
- **Pool de Conexiones:** HikariCP (automático con Spring Boot)

---

## Próximas Features

- [ ] Autenticación con JWT
- [ ] Testing (JUnit 5 + Mockito)
- [ ] Filtros avanzados por rango de fechas
- [ ] Despliegue en plataforma gratuita compatible con Java

---

## Frontend
Repo separado: [roommate-splitter-frontend](https://github.com/agus25varela/roommate-splitter-frontend)

---

## Autor
Agustina Varela - [@agus25varela](https://github.com/agus25varela)

**Junior Full Stack Developer | Buenos Aires, Argentina**