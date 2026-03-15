# Arquitetura de serviços

Todos os serviços ficam no diretório raiz do projeto.

## Serviços e bancos recomendados

| Service | Responsabilidade | Banco |
|---|---|---|
| auth-service | autenticação, JWT, roles/permissions | PostgreSQL |
| user-service | usuários, perfil, endereços | PostgreSQL |
| product-service | catálogo de produtos | PostgreSQL |
| inventory-service | estoque, reserva e movimentação | PostgreSQL |
| cart-service | carrinho temporário | Redis |
| order-service | pedidos e histórico | PostgreSQL |
| payment-service | pagamentos e transações | PostgreSQL |
| shipping-service | frete, envio e rastreamento | PostgreSQL |
| notification-service | envio de email e logs | MongoDB |
| api-gateway | roteamento/auth/rate limit | sem banco |
| discovery-server | service discovery (Eureka) | sem banco |
| config-server | configuração centralizada | Git |

## Eventos assíncronos (broker)

Kafka ou RabbitMQ para eventos como:
- OrderCreated
- PaymentApproved
- StockReserved
- ShipmentCreated
- EmailSent
