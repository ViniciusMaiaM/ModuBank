# ModuBank — account (WIP)

<p align="center">
  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/spring%20boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white"/>
  <img src="https://img.shields.io/badge/testcontainers-384351?style=for-the-badge&logo=testcontainers&logoColor=white"/>
  <img src="https://img.shields.io/badge/postgresql-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
</p>

- [📖 Documentação](#-documentação)
- [📐 Arquitetura](#-arquitetura)
- [📑 Sobre](#-sobre)
- [🧭 Responsabilidades](#-responsabilidades)
- [🔌 Endpoints](#-endpoints)
- [📂 Estrutura](#-estrutura)
- [⚙️ Configuração](#️-configuração)
- [🗂️ Migrations (Flyway)](#️-migrations-flyway)
- [🧪 Testes](#-testes)
- [🚀 Como rodar](#-como-rodar)
- [📌 Status (WIP)](#-status-wip)

## 📖 Documentação
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs


## 📐 Arquitetura
- [ER Diagram](docs/erd.md)
- [Use Cases](docs/usecases.md)
- [Diagrama de sequência](docs/sequence.md)
- [Componentes](docs/components.md)

## 📑 Sobre
Serviço responsável por cadastro e consulta de contas. Segue Clean Architecture (domain, application, interfaces, infrastructure).

## 🧭 Responsabilidades
- Criar conta (vinculada a um usuário) e consultar conta por ID.
- Expor metadados (currency, status, createdAt).
- Saldo real NÃO é aqui — será derivado do ledger no transaction-service.

## 🔌 Endpoints
- POST /v1/accounts
- GET /v1/accounts/{id}
- GET /v1/accounts/{id}/balance (501 – a implementar quando integrar com ledger)

## 📂 Estrutura
```
src/main/kotlin/com/modubank/account
├── AccountServiceApplication.kt
├── domain/
├── application/
│   ├── repositories/          # contratos de persistência (ports)
│   └── usecases/              # casos de uso
├── interfaces/api/
│   └── dto/
└── infrastructure/
    ├── config/                # segurança, exception handlers
    └── persistence/jpa/       # adapters JPA
```

## ⚙️ Configuração
- src/main/resources/application.yml (datasource, JPA, Flyway, Actuator)
- Variáveis recomendadas:
    - DB_URL=jdbc:postgresql://localhost:5432/modubank
    - DB_USER=modubank
    - DB_PASSWORD=modubank

## 🗂️ Migrations (Flyway)
- Local: `src/main/resources/db/migration`
- Criar nova migration:
    - Nome: `V{N}__descricao.sql` (ex.: `V2__add_unique_email.sql`)
    - Conteúdo: SQL DDL (CREATE/ALTER/DROP…)
- Aplicar migrations:
    - Ao iniciar o app (`./gradlew bootRun`) o Flyway executa automaticamente
- Exemplo:
```sql
-- V2__add_unique_email.sql
ALTER TABLE users ADD CONSTRAINT uq_users_email UNIQUE (email);
```
- Observação: rollback é feito com nova migration (ex.: `V3__rollback_uq_email.sql`).

## 🧪 Testes
- Dependências:
    - spring-boot-starter-test
    - org.testcontainers:junit-jupiter + org.testcontainers:postgresql
    - (opcional) spring-security-test, mockk
- Tipos:
    - Web (MockMvc) para validações/contrato HTTP
    - Integração com Testcontainers (PostgreSQL) para JPA/Flyway
- Executar: `./gradlew test`

## 🚀 Como rodar
1) Infra:
```bash
docker compose up -d
```
2) Aplicação:
```bash
./gradlew bootRun
```
3) Testar:
- POST /v1/accounts
- GET /v1/accounts/{id}

## 📌 Status (WIP)
- [x] Endpoints: criar/consultar conta
- [x] Flyway V1 (tabelas users/accounts)
- [x] Exception handling (ProblemDetail)
- [ ] JWT/RBAC
- [ ] DTOs anotados no OpenAPI
- [ ] Testes de repositório JPA (CRUD completo)
- [ ] Observabilidade (Actuator + OpenTelemetry)
