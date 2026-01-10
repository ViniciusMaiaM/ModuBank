# ModuBank (WIP)

<p align="center">
  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/spring%20boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/go-00ADD8?style=for-the-badge&logo=go&logoColor=white"/>
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/postgresql-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white"/>
  <img src="https://img.shields.io/badge/testcontainers-384351?style=for-the-badge&logo=testcontainers&logoColor=white"/>
</p>

- [📖 Documentação](#-documentação)
- [📑 Sobre o projeto](#-sobre-o-projeto)
- [🧭 Serviços e responsabilidades](#-serviços-e-responsabilidades)
- [📦 Gerenciadores de pacotes](#-gerenciadores-de-pacotes)
- [📂 Estrutura do repositório](#-estrutura-do-repositório)
- [🚀 Começando](#-começando)
- [🧪 Testes](#-testes)
- [📌 Status (WIP)](#-status-wip)

## 📖 Documentação
- Swagger (local, quando os serviços estiverem rodando):
    - account: http://localhost:8081/swagger-ui/index.html
    - transaction-service: http://localhost:8082/swagger-ui/index.html
- Diagramas e coleções (WIP): adicionar em DOCS/ (ERD, arquitetura, coleções de API).

## 📑 Sobre o projeto
ModuBank é uma plataforma bancária modular (microserviços) para estudo de back-end, segurança e observabilidade. O foco é consistência financeira (ledger de dupla entrada), idempotência, testes e evolução para cloud.

## 🧭 Serviços e responsabilidades
- account (Kotlin + Spring Boot)
    - Cadastro e consulta de contas (metadados).
    - Não calcula saldo real (delegado ao transaction-service).
- transaction-service (Kotlin + Spring Boot)
    - Transferências internas; ledger dupla-entrada; idempotência; base para extrato/saldo.
- fraud-service (Go + Fiber)
    - Regras de fraude (velocity, limites, horários), inicialmente em “shadow mode”.
- notification-service (Go + Fiber)
    - Envio de e-mails/SMS (confirmações, alertas); idempotência e tratamento de bounce.
- currency-service (Go + Fiber)
    - Taxas de câmbio, cache com TTL, arredondamento por moeda, fallback.
- api-gateway (Kotlin ou Go)
    - Roteamento, autenticação/ratelimiting, timeouts/circuit breakers, observabilidade.

## 📦 Gerenciadores de pacotes
- Kotlin/Java: Gradle (Kotlin DSL)
- Go: Go Modules

## 📂 Estrutura do repositório
```
modubank/
├── account/           # Kotlin + Spring Boot
├── transaction-service/       # Kotlin + Spring Boot
├── fraud-service/             # Go + Fiber
├── notification-service/      # Go + Fiber
├── currency-service/          # Go + Fiber
├── api-gateway/               # Kotlin ou Go
├── docker-compose.yml         # Infra local (Postgres, LocalStack - WIP)
└── DOCS/                      # Diagramas, coleções de API, ADRs (WIP)
```

## 🚀 Começando
1) Pré-requisitos: Java 21, Docker + Docker Compose, IDE (IntelliJ/VS Code)
2) Infra local: `docker compose up -d` (Postgres; LocalStack WIP)
3) Subir serviços:
    - account: `cd account && ./gradlew bootRun`
4) Swagger: http://localhost:8081/swagger-ui/index.html

## 🧪 Testes
- Unit/Integration: Spring Boot Test + Testcontainers (PostgreSQL)
- Contratos (futuro): Pact
- Carga (futuro): k6

## 📌 Status (WIP)
- Core
    - [x] Planejamento inicial
    - [ ] CI/CD (GitHub Actions)
    - [ ] Observabilidade (OpenTelemetry/Prometheus)
- account
    - [x] Endpoints iniciais (criar/consultar conta)
    - [x] Flyway V1 (users, accounts)
    - [x] Exception handling (ProblemDetail)
    - [ ] JWT/RBAC
    - [ ] DTOs anotados no OpenAPI
    - [ ] Testes Web/Repository completos
- transaction-service
    - [ ] Ledger dupla-entrada
    - [ ] Idempotência e regras
    - [ ] Outbox + SQS (LocalStack)
- demais serviços (fraud/notification/currency/gateway)
    - [ ] Prototipagem e contratos
