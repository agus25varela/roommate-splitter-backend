# Roommate Splitter - Backend

API REST para dividir gastos entre compañeros de departamento.

## Stack
- **Framework:** Spring Boot 4.1.0
- **Lenguaje:** Java 21
- **BD:** PostgreSQL
- **Build:** Maven

## Requisitos
- Java 21
- Maven 3.9+
- PostgreSQL 14+

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

### Gastos
- `GET /api/gastos` - Listar gastos
- `POST /api/gastos` - Crear gasto
- `GET /api/gastos/debts` - Ver deudas resumidas

### Modelo
```json
{
  "id": 1,
  "descripcion": "Almuerzo",
  "monto": 500.00,
  "paidBy": "Agustina",
  "splitAmong": ["Agustina", "Otro"],
  "fecha": "2026-08-18"
}
```

## Frontend
Repo separado: [roommate-splitter-frontend](https://github.com/agus25varela/roommate-splitter-frontend)

## Autor
Agustina Varela - [@agus25varela](https://github.com/agus25varela)