# E-commerce Microservices (Spring Boot + Eureka + Gateway)

## Project layout

```text
infra/
 ├─ api-gateway
 ├─ discovery-server
 └─ config-server

services/
 ├─ auth-service
 ├─ user-service
 ├─ product-service
 ├─ inventory-service
 ├─ cart-service
 ├─ order-service
 ├─ payment-service
 ├─ shipping-service
 └─ notification-service
```

See full responsibilities and database recommendations in [`ARCHITECTURE.md`](ARCHITECTURE.md).

## Services & Ports

### Host-exposed ports (Docker)
- api-gateway: http://localhost:8080
- discovery-server: http://localhost:8761
- postgres: localhost:5432 (optional for debugging)
- mailhog ui: http://localhost:8025
- mongodb: localhost:27017 (notification templates/logs)
- pgadmin: http://localhost:8090

### Internal-only (Docker network)
- auth-service: 8081
- user-service: 8082
- product-service: 8083
- order-service: 8084
- payment-service: 8085
- notification-service: 8086
- cart-service: 8087

> Internal endpoints like `/internal/**` are NOT routed by the gateway.

---

## Profiles / URLs

### Local (run with `mvn spring-boot:run`)
Each service default `application.yml` uses:
- Eureka: `http://localhost:8761/eureka`
- Postgres: `localhost:5432/<db>`

### Docker
Compose sets:
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_URL=http://discovery-server:8761/eureka`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/<db>`
- Mail SMTP: `smtp.gmail.com:587` (configure `MAIL_USERNAME`, `MAIL_PASSWORD`, and optionally `MAIL_FROM`)

---

## Run with Docker Compose

From repo root:

```bash
docker compose up --build
```

## Architecture Diagram

![Services architecture](docs/services-architecture.svg)

## Swagger / OpenAPI

Swagger is centralized in the API Gateway.

- Unified UI: `http://localhost:8080/swagger-ui.html`
- Aggregated docs endpoints exposed by gateway:
  - `/v3/api-docs/auth`
  - `/v3/api-docs/user`
  - `/v3/api-docs/product`
  - `/v3/api-docs/order`
  - `/v3/api-docs/payment`
  - `/v3/api-docs/notification`
  - `/v3/api-docs/cart`
