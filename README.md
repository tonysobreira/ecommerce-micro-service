# E-commerce Microservices (Spring Boot + Eureka + Gateway)

## Serviços no diretório raiz

- api-gateway
- discovery-server
- config-server
- auth-service
- user-service
- product-service
- inventory-service
- cart-service
- order-service
- payment-service
- shipping-service
- notification-service

## Services & Ports

### Host-exposed ports (Docker)
- api-gateway: http://localhost:8080
- discovery-server: http://localhost:8761
- postgres: localhost:5432
- mailhog ui: http://localhost:8025
- mongodb: localhost:27017
- pgadmin: http://localhost:8090

### Internal-only (Docker network)
- auth-service: 8081
- user-service: 8082
- product-service: 8083
- order-service: 8084
- payment-service: 8085
- notification-service: 8086
- cart-service: 8087
- inventory-service: 8088
- shipping-service: 8089

## Run with Docker Compose

```bash
docker compose up --build
```

## Swagger / OpenAPI (via gateway)

- `http://localhost:8080/swagger-ui.html`
- `/v3/api-docs/auth`
- `/v3/api-docs/user`
- `/v3/api-docs/product`
- `/v3/api-docs/order`
- `/v3/api-docs/payment`
- `/v3/api-docs/inventory`
- `/v3/api-docs/shipping`
- `/v3/api-docs/notification`
- `/v3/api-docs/cart`
