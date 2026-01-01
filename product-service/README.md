# product-service

## Run locally
- Ensure Postgres has database `productdb`
- Ensure Eureka is running on http://localhost:8761
- Set JWT_SECRET (32+ chars)

```bash
export JWT_SECRET="dev-jwt-secret-32-bytes-long-123456"
mvn spring-boot:run
