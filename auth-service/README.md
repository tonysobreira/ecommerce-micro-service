# auth-service

## Requirements
- Postgres database authdb
- JWT_SECRET must be 32+ chars (HS256)
- SMTP server for activation mail (for Docker Compose, MailHog UI is at `http://localhost:8025`)

Example:
export JWT_SECRET="dev-jwt-secret-32-bytes-long-123456"

## Run locally
1) Create DB:
   createdb authdb

2) Run:
   mvn spring-boot:run

## Endpoints
- POST /auth/register
- GET  /auth/activate?token=...
- POST /auth/login
- GET  /auth/me
- POST /auth/refresh
- POST /auth/logout

## Example curl
Register:
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

When running in Docker Compose, call registration through the gateway:

curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

Then open MailHog (`http://localhost:8025`) and use the activation link from the email.

Activate (from email link):
curl "http://localhost:8081/auth/activate?token=<TOKEN_FROM_EMAIL>"

Login:
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

Me:
curl http://localhost:8081/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
