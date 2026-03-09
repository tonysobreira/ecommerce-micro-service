# cart-service

MongoDB-backed cart microservice for practicing document database patterns alongside existing relational services.

## Endpoints
- `GET /cart`
- `POST /cart/items`
- `PATCH /cart/items/{productId}`
- `DELETE /cart/items/{productId}`
- `DELETE /cart`
- `POST /cart/checkout`

## Run locally
- Ensure MongoDB is running on `mongodb://localhost:27017/cartdb`
- Ensure Eureka is running on `http://localhost:8761`
- Set JWT_SECRET (32+ chars)

```bash
export JWT_SECRET="dev-jwt-secret-32-bytes-long-123456"
mvn spring-boot:run
```
