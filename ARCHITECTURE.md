# Project Structure and Service Responsibilities

## Folder layout

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

## Service responsibilities and database recommendation

| Service | Responsibility | Recommended DB |
|---|---|---|
| auth-service | Authentication (login, refresh token, JWT, roles/permissions) | PostgreSQL |
| user-service | User profile and addresses | PostgreSQL |
| product-service | Product catalog, categories, prices and images | PostgreSQL |
| inventory-service | Stock quantities, reservations and stock movements | PostgreSQL |
| cart-service | Temporary shopping cart and subtotal | Redis |
| order-service | Order lifecycle and order history | PostgreSQL |
| payment-service | Payment processing and transaction records | PostgreSQL |
| shipping-service | Freight calculation, shipment and tracking | PostgreSQL |
| notification-service | Email notifications, templates and send logs | MongoDB |
| api-gateway | Routing, auth and rate limit | No DB |
| discovery-server | Service registry (Eureka) | No DB |
| config-server | Centralized configuration | Git-backed |

## Async communication

Use a message broker (Kafka or RabbitMQ) for events such as:
- `OrderCreated`
- `PaymentApproved`
- `StockReserved`
- `ShipmentCreated`
- `EmailSent`
