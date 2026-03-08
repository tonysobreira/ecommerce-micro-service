# auth-service

## Requirements
- Postgres database authdb
- JWT_SECRET must be 32+ chars (HS256)
- Gmail SMTP account configured with App Password

### Gmail SMTP setup
Configure these env vars before running:

```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME="your_account@gmail.com"
export MAIL_PASSWORD="<gmail-app-password>"
export MAIL_SMTP_AUTH=true
export MAIL_SMTP_STARTTLS_ENABLE=true
export MAIL_FROM="your_account@gmail.com"
```

Notes:
- `MAIL_PASSWORD` must be a **Google App Password** (not your normal Gmail password).
- `MAIL_FROM` should match `MAIL_USERNAME` for Gmail to avoid sender rejection.
- Registration creates a **short-lived signed JWT activation token** sent by email.

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
- POST /auth/activation/resend
- POST /auth/password/forgot
- POST /auth/password/reset
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

Check your Gmail inbox and use the activation link from the email.

Activate (from email link):
curl "http://localhost:8080/auth/activate?token=<TOKEN_FROM_EMAIL>"

Login:
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Passw0rd!"}'

Me:
curl http://localhost:8081/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
