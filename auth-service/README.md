# auth-service

## Requirements
- Postgres database authdb
- JWT_SECRET must be 32+ chars (HS256)

Example:
export JWT_SECRET="dev-jwt-secret-32-bytes-long-123456"

## Run locally
1) Create DB:
   createdb authdb

2) Run:
   mvn spring-boot:run

## Endpoints
- POST /auth/register
- POST /auth/login
- GET  /auth/me
- POST /auth/refresh
- POST /auth/logout

## Example curl
Register:
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

Login:
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

Me:
curl http://localhost:8081/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
