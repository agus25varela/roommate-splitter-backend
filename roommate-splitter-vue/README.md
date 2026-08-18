# Roommate Splitter - Frontend

Dashboard interactivo para dividir gastos entre compañeros de departamento.

## Stack
- **Frontend:** Vue 3 + Vite
- **Backend:** Spring Boot (Java 21) + PostgreSQL
- **API:** REST

## Cómo correr localmente

### Requisitos
- Node.js 20+
- Backend levantado en `http://localhost:8080`

### Frontend
```bash
npm install
npm run dev
```

La app abre en `http://localhost:5173`

### Backend (necesario)
El backend corre en un repo separado: [roommate-splitter-backend](https://github.com/agus25varela/roommate-splitter-backend)

**Instrucciones:**
1. Clona el repo del backend
2. Configura PostgreSQL (crea BD `roommate_db`)
3. Levantá con Maven:
```bash
mvn clean spring-boot:run
```

Backend escucha en `http://localhost:8080`

## Demo en Vivo
Frontend desplegado en Vercel: [roommate-splitter-frontend.vercel.app](https://roommate-splitter-frontend.vercel.app)

*Nota: Para funcionalidad completa, necesita backend corriendo localmente*

## Autor
Agustina Varela - [@agus25varela](https://github.com/agus25varela)