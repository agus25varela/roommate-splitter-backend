# AGENTS.md

Roommate Splitter backend: API REST en **Spring Boot 4.1.0** + **Java 21** + **PostgreSQL** que alimenta la app de gastos del frontend Vue (`roommate-splitter-frontend`). Comentarios y mensajes en español (es-AR).

## Commands

- `.\mvnw.cmd compile` — **verificación obligatoria**; debe terminar sin errores tras cualquier cambio.
- `.\mvnw.cmd test` — solo existe un test de contexto (`RoommateSplitterApplicationTests`).
- `.\mvnw.cmd spring-boot:run` — levanta la API en `http://localhost:8080`.
- No hay lint/format ni CI configurados.

## Gotchas

- **Spring Boot 4 / Spring Framework 7**: el starter es `spring-boot-starter-webmvc` (ya no `starter-web`).
- **Sin migraciones**: el esquema lo genera Hibernate (`spring.jpa.hibernate.ddl-auto=update`). No hay Flyway/Liquibase ni archivos SQL, aunque `spring.sql.init.mode=always` esté seteado. `application-dev.properties` usa `ddl-auto=create-drop`.
- **Auth NO está enforced**: `SecurityConfig` deja todo `permitAll`, sin JWT. Login valida la contraseña BCrypt y devuelve el `Usuario`. La sesión la persiste el frontend en `localStorage` (`usuario`, `usuarioId`).
- **Lombok está en el pom pero no se usa** — el código usa getters/setters manuales. Seguir ese estilo.
- CORS solo permite `http://localhost:5173` y `http://localhost:3000`.

## Arquitectura

- Paquetes bajo `com.example.roommatesplitter`: `controller`, `service`, `repository`, `model`, `dto`, `exception`, `config`.
- Controllers: `GastoController` (`/api/gasto`) y `UsuarioController` (`/api/usuario`).
- La lógica de gastos Y balances vive en `GastoService.calcularBalances` (participantes dinámicos + minimización de transferencias).

## Contrato con el frontend (verificado; no "arreglar")

- `GastoResponseDTO` = `{id, descripcion, monto, quienPago, fecha, usuarioId}` (usuarioId numérico).
- Balances son **planos**: `BalanceDTO[] {deudor, acreedor, monto}` — el frontend renderiza exactamente esa forma.
- `Usuario` = `{id, email, nombre}` (nunca expone password).
- `quienPago` es string libre en el backend; el frontend lo restringe a `'yo' | 'roommate_a' | 'roommate_b'`.
- `POST /api/gasto` usa `GastoDTO` completo (incluye `usuarioId`, validado con `@Valid`). `PUT /api/gasto/{id}` usa `UpdateGastoDTO` con campos opcionales — **solo se aplican los no-nulos**.

## Endpoints reales

| Método | Ruta | Body | Respuesta |
|--------|------|------|-----------|
| POST | `/api/usuario/signup` | `UsuarioSignupDTO {email, nombre, password}` | 201 `UsuarioResponseDTO` |
| POST | `/api/usuario/login` | `UsuarioLoginDTO {email, password}` | 200 `UsuarioResponseDTO` |
| GET | `/api/usuario/{id}` | — | 200 `UsuarioResponseDTO` |
| GET | `/api/gasto?usuarioId=` | — | 200 `GastoResponseDTO[]` |
| POST | `/api/gasto` | `GastoDTO {descripcion, monto, quienPago, fecha, usuarioId}` | 201 `GastoResponseDTO` |
| GET | `/api/gasto/{id}` | — | 200 `GastoResponseDTO` |
| PUT | `/api/gasto/{id}` | `UpdateGastoDTO {descripcion?, monto?, quienPago?, fecha?}` | 200 `GastoResponseDTO` |
| DELETE | `/api/gasto/{id}` | — | 204 |
| GET | `/api/gasto/balances?usuarioId=` | — | 200 `BalanceDTO[]` |

## Errores

`exception/GlobalExceptionHandler` (`@RestControllerAdvice`) devuelve `{mensaje}` único:
- `IllegalArgumentException` → 400
- `CredencialesInvalidasException` → 401 (login inválido)
- `RecursoNoEncontradoException` → 404
- `MethodArgumentNotValidException` → 400 (primer mensaje de validación)
- `Exception` → 500 genérico

Usar esas excepciones en vez de `IllegalArgumentException` a secas para los casos "no encontrado" / credenciales.

## No resucitar (removido)

- **JWT**: `security/JwtUtil` y `JwtAuthenticationFilter` fueron eliminados (estaban comentados y no había dependencia jjwt). Si se quiere auth real, hay que agregar jjwt y un filtro desde cero.
- **Deuda**: entidad, repo, service y `DeudaDTO` fueron eliminados. El cálculo de deudas ahora es `GastoService.calcularBalances`. Reconstruir contra el modelo actual si se necesita persistir deudas.