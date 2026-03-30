# 🛒 E-commerce Microservice Platform

Plataforma de e-commerce baseada em **microserviços com Spring Boot**, com descoberta de serviços via **Eureka**, roteamento por **API Gateway**, comunicação assíncrona com **Kafka** e persistência híbrida com **PostgreSQL** + **MongoDB**.

---

## 📌 Sobre o projeto

Este repositório organiza um ecossistema completo para operações de e-commerce, com serviços isolados por domínio (autenticação, usuários, produtos, pedidos, pagamentos, carrinho, estoque, envio e notificações).

A arquitetura foi pensada para:

- escalar serviços de forma independente;
- separar responsabilidades de negócio;
- facilitar observabilidade e manutenção;
- permitir evolução incremental de cada domínio.

---

## 🧩 Arquitetura da solução

![Arquitetura dos microserviços](docs/services-architecture.svg)

> O diagrama acima mostra o fluxo principal: **Frontend → API Gateway → Serviços**, com registro no **Eureka**, eventos em **Kafka** e dependências de infraestrutura.

---

## 🧱 Módulos do monorepo

| Camada | Serviço/Módulo | Porta (container) | Tecnologia principal |
|---|---|---:|---|
| Entrada | `frontend-sakai-ng` | `4200` (host) | Angular |
| Entrada | `api-gateway` | `8080` (host) | Spring Cloud Gateway |
| Descoberta | `discovery-server` | `8761` (host) | Netflix Eureka |
| Segurança | `auth-service` | `8081` (interna) | Spring Boot + PostgreSQL + Kafka |
| Usuários | `user-service` | `8082` (interna) | Spring Boot + PostgreSQL |
| Catálogo | `product-service` | `8083` (interna) | Spring Boot + PostgreSQL |
| Pedidos | `order-service` | `8084` (interna) | Spring Boot + PostgreSQL + Kafka |
| Pagamentos | `payment-service` | `8085` (interna) | Spring Boot + PostgreSQL |
| Notificações | `notification-service` | `8086` (interna) | Spring Boot + MailHog + Kafka |
| Carrinho | `cart-service` | `8087` (interna) | Spring Boot + MongoDB |
| Estoque | `inventory-service` | `8088` (interna) | Spring Boot + PostgreSQL |
| Entregas | `shipping-service` | `8089` (interna) | Spring Boot + PostgreSQL |
| Configuração | `config-server` | — | Placeholder (não ativo no compose) |

---

## 🛠️ Infraestrutura e ferramentas

### Portas expostas no host

| Recurso | URL/Porta |
|---|---|
| Frontend | http://localhost:4200 |
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| PostgreSQL | `localhost:5432` |
| MongoDB | `localhost:27017` |
| Kafka (externo) | `localhost:9092` |
| Kafka Controller | `localhost:9093` |
| MailHog (SMTP) | `localhost:1025` |
| MailHog (UI) | http://localhost:8025 |
| pgAdmin | http://localhost:8090 |

### Serviços internos na rede Docker

`auth-service:8081`, `user-service:8082`, `product-service:8083`, `order-service:8084`, `payment-service:8085`, `notification-service:8086`, `cart-service:8087`, `inventory-service:8088`, `shipping-service:8089`.

---

## ▶️ Como executar

### 1) Pré-requisitos

- Docker + Docker Compose

### 2) Subir o ambiente completo

```bash
docker compose up --build
```

### 3) Verificar containers

```bash
docker compose ps
```

### 4) Parar o ambiente

```bash
docker compose down
```

> Para limpar volumes persistidos (Postgres/Mongo), use `docker compose down -v`.

---

## 🌐 API e documentação

### Swagger UI (agregado no gateway)

- http://localhost:8080/swagger-ui.html

### Endpoints OpenAPI por domínio

- `/v3/api-docs/auth`
- `/v3/api-docs/user`
- `/v3/api-docs/product`
- `/v3/api-docs/order`
- `/v3/api-docs/payment`
- `/v3/api-docs/inventory`
- `/v3/api-docs/shipping`
- `/v3/api-docs/notification`
- `/v3/api-docs/cart`

### Rotas principais no Gateway

- `/auth/**` → `auth-service`
- `/users/**` → `user-service`
- `/products/**` → `product-service`
- `/categories/**` → `product-service`
- `/product-images/**` → `product-service`
- `/orders/**` → `order-service`
- `/payments/**` → `payment-service`
- `/notifications/**` → `notification-service`
- `/cart/**` → `cart-service`

---

## 🔄 Fluxos de integração (resumo)

- **Service Discovery:** Gateway e microsserviços registram-se no Eureka.
- **Comunicação síncrona:** chamadas REST entre domínios (ex.: pedido consulta produto).
- **Comunicação assíncrona:** eventos em Kafka para notificações e processos desacoplados.
- **Persistência:** PostgreSQL para dados relacionais e MongoDB para carrinho.

---

## 🚧 Roadmap sugerido

- [ ] Finalizar a construção do frontend.
- [ ] Implementar Saga Pattern na criação do pedido (order).
- [ ] Adicionar observabilidade completa (tracing, métricas e logs centralizados).
- [ ] Implementar testes de contrato entre serviços.
- [ ] Habilitar CI/CD com validação automatizada por serviço.
- [ ] Evoluir `config-server` para configuração centralizada por ambiente.

---

## 👥 Colaboração

1. Faça um fork (ou branch).
2. Implemente sua melhoria.
3. Valide localmente.
4. Abra um PR descrevendo impacto técnico e funcional.

---

Feito para estudos e evolução contínua de arquitetura distribuída com Spring Cloud.
