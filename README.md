# EduPulse Frontend

Professional React dashboard for the EduPulse Spring Boot microservices project.

## Included
- Login page connected to `POST /auth/login`
- JWT bearer token attached to protected API requests
- Role-aware navigation for ADMIN, FACULTY, and STUDENT
- Dashboard with service-backed summary cards and recent results
- Student directory with create/read/update/delete UI (ADMIN/FACULTY)
- Attendance records, attendance entry, and percentage lookup
- Results table, result entry, and percentage lookup
- Responsive sidebar, mobile layout, tables, status badges, and charts

## Requirements
- Node.js 20+ recommended
- EduPulse services and API Gateway running
- API Gateway reachable at `https://localhost:8445`

## Run locally
```bash
npm install
cp .env.example .env
npm run dev
```
Open the URL printed by Vite, normally `http://localhost:5173`.

On Windows Command Prompt, use `copy .env.example .env` instead of `cp`.

## Important: local HTTPS certificate
The API Gateway uses a local/self-signed HTTPS certificate. Browsers may block frontend API calls until the certificate is trusted. Open `https://localhost:8445` directly in the browser and, only for your own local development environment, accept/trust the development certificate if your browser offers that option. Do not disable TLS verification in application code or use this self-signed setup in production.

## API base URL
Default: `https://localhost:8445`. To change it, edit `.env`:
```env
VITE_API_BASE_URL=https://localhost:8445
```
Restart Vite after changing environment variables.

## Expected API shapes
- Login response should be a JWT string or an object containing `token`, `accessToken`, or `jwt`.
- The frontend reads `role` and `studentId` from JWT claims.
- Student endpoints use `/api/students` through the Gateway.
- Attendance and Result endpoints use the routes configured in the current Gateway project.

If your Auth Service returns a differently named token field, update `src/context/AuthContext.jsx` in the `login` function. The existing backend is not modified by this frontend.
