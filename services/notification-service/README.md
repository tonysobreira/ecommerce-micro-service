# notification-service

Centralized microservice responsible for sending emails used by other services.

## Endpoints
- `POST /emails/activation` - sends account activation email.
- `POST /emails/orders/status` - sends order status update email (created, paid, shipped, delivered, cancelled).

## Run locally
```bash
mvn spring-boot:run
```

Default SMTP config points to MailHog:
- `MAIL_HOST=mailhog`
- `MAIL_PORT=1025`


## Kafka when running locally
If `notification-service` runs on your host while Kafka runs in Docker, use:

```bash
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

The default config also includes `kafka:29092` as a fallback for Docker-network execution.

