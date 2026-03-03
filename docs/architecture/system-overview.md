# System Overview

## 🏗️ Architecture Overview

ModuBank is a modular banking platform built with microservices architecture, designed for scalability, security, and maintainability.

## 🎯 Design Principles

### 1. **Domain-Driven Design (DDD)**
- Bounded contexts for each service
- Rich domain models with business logic
- Clear separation of concerns

### 2. **Microservices Architecture**
- Single responsibility per service
- Independent deployment and scaling
- Fault isolation and resilience

### 3. **Event-Driven Communication**
- Asynchronous communication between services
- Event sourcing for critical operations
- Loose coupling and high scalability

### 4. **API-First Design**
- RESTful APIs with OpenAPI documentation
- Version-controlled interfaces
- Backward compatibility

## 🏦 Service Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │────│  Account Service│────│ Transaction Svc │
│   (Kotlin/Go)  │    │   (Kotlin)     │    │   (Kotlin)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Fraud Service  │    │ Notification   │    │ Currency Svc   │
│     (Go)        │    │   Service      │    │     (Go)       │
│                 │    │     (Go)       │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📊 Data Flow

### User Registration Flow
```
Client → API Gateway → Account Service → Database
         ↓
    Notification Service (async)
```

### Transaction Flow
```
Client → API Gateway → Transaction Service → Ledger
         ↓                    ↓
    Fraud Service      → Account Service (balance update)
         ↓
    Notification Service
```

## 🔧 Technology Stack

### Backend Services
- **Kotlin**: Spring Boot 3.x, Coroutines
- **Go**: Fiber framework, goroutines
- **Java**: OpenJDK 21

### Data Storage
- **PostgreSQL**: Primary database
- **Redis**: Caching and sessions
- **Flyway**: Database migrations

### Infrastructure
- **Docker**: Containerization
- **Kubernetes**: Orchestration (future)
- **AWS/GCP**: Cloud provider

### Communication
- **REST APIs**: Synchronous communication
- **Message Queues**: Async communication (SQS/RabbitMQ)
- **gRPC**: Internal service communication (future)

## 🔒 Security Architecture

### Authentication & Authorization
- **JWT**: Stateless authentication
- **OAuth 2.0**: Third-party integration
- **RBAC**: Role-based access control

### Data Protection
- **Encryption**: TLS 1.3 in transit, AES-256 at rest
- **PII Protection**: Data masking and tokenization
- **Audit Logging**: Comprehensive audit trails

### Network Security
- **API Gateway**: Rate limiting, WAF
- **Service Mesh**: mTLS for service communication
- **VPN**: Private network access

## 📈 Scalability Design

### Horizontal Scaling
- **Stateless Services**: Easy scaling
- **Load Balancing**: Multiple instances
- **Database Sharding**: Partitioned data

### Performance Optimization
- **Caching Strategy**: Multi-level caching
- **Connection Pooling**: Database efficiency
- **Async Processing**: Non-blocking operations

## 🔍 Observability

### Monitoring
- **Metrics**: Prometheus + Grafana
- **Tracing**: OpenTelemetry + Jaeger
- **Logging**: Structured logs with correlation IDs

### Health Checks
- **Service Health**: Custom health indicators
- **Database Health**: Connection validation
- **Dependency Health**: External service checks

## 🚀 Deployment Architecture

### Environment Strategy
- **Development**: Local Docker Compose
- **Staging**: Kubernetes cluster
- **Production**: Multi-region deployment

### CI/CD Pipeline
- **GitHub Actions**: Automated builds and tests
- **Docker Registry**: Container image management
- **Rolling Updates**: Zero-downtime deployments

## 📋 Service Responsibilities

### Account Service
- User registration and management
- Account creation and maintenance
- Customer data management
- **Port**: 8081

### Transaction Service
- Financial transaction processing
- Double-entry ledger
- Transaction history
- **Port**: 8082

### Fraud Service
- Real-time fraud detection
- Risk assessment
- Pattern analysis
- **Port**: 8083

### Notification Service
- Email and SMS notifications
- Push notifications
- Template management
- **Port**: 8084

### Currency Service
- Exchange rate management
- Currency conversion
- Rate caching
- **Port**: 8085

### API Gateway
- Request routing
- Authentication/authorization
- Rate limiting
- Load balancing
- **Port**: 8080

## 🔄 Integration Patterns

### Synchronous Communication
- **REST APIs**: External client communication
- **gRPC**: Internal service communication (future)
- **Circuit Breaker**: Fault tolerance

### Asynchronous Communication
- **Message Queues**: Event-driven architecture
- **Event Sourcing**: Audit trail and replay
- **CQRS**: Read/write separation

## 🎯 Quality Attributes

### Reliability
- **99.9% Uptime**: High availability target
- **Fault Tolerance**: Graceful degradation
- **Data Consistency**: ACID compliance

### Performance
- **Response Time**: <200ms for 95th percentile
- **Throughput**: 1000+ TPS per service
- **Scalability**: Horizontal scaling support

### Security
- **Compliance**: PCI DSS, GDPR
- **Data Protection**: Encryption and masking
- **Access Control**: Principle of least privilege

---

**Next Steps**: 
- Read [Domain Model](./domain-model.md) for detailed business logic
- Check [API Design](./api-design.md) for interface specifications
- Review [Security Architecture](./security.md) for security details
