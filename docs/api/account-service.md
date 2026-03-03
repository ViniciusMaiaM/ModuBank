# Account Service API

## 🏦 Overview

The Account Service manages user accounts, customer information, and account-related operations in the ModuBank platform.

## 🌐 Base URL

- **Development**: `http://localhost:8081`
- **Production**: `https://api.modubank.com/account`

## 📋 API Versioning

All APIs are versioned using URL path versioning:
- Current version: `/v1`
- Previous versions: `/v1` (only version currently supported)

## 🔐 Authentication

All API endpoints (except registration) require authentication:

```http
Authorization: Bearer <jwt_token>
```

## 📚 Endpoints

### User Management

#### Register User
Create a new user and associated account.

```http
POST /v1/users
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!",
  "cpf": "12345678901",
  "birthDate": "1990-01-01",
  "phone": "+5511999999999",
  "street": "Rua Principal",
  "number": "123",
  "complement": "Apto 101",
  "neighborhood": "Centro",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01310-100",
  "currency": "BRL",
  "accountType": "CHECKING"
}
```

**Response (201):**
```json
{
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "cpf": "12345678901",
    "birthDate": "1990-01-01",
    "phone": "+5511999999999",
    "address": {
      "street": "Rua Principal",
      "number": "123",
      "complement": "Apto 101",
      "neighborhood": "Centro",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01310-100"
    },
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00Z",
    "updatedAt": "2024-01-01T10:00:00Z"
  },
  "account": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "currency": "BRL",
    "status": "ACTIVE",
    "accountNumber": "1234567890-12",
    "branchCode": "0001",
    "type": "CHECKING",
    "createdAt": "2024-01-01T10:00:00Z"
  }
}
```

#### Get User
Retrieve user information by ID.

```http
GET /v1/users/{userId}
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `userId` (UUID): User identifier

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "cpf": "12345678901",
  "birthDate": "1990-01-01",
  "phone": "+5511999999999",
  "address": {
    "street": "Rua Principal",
    "number": "123",
    "complement": "Apto 101",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310-100"
  },
  "status": "ACTIVE",
  "createdAt": "2024-01-01T10:00:00Z",
  "updatedAt": "2024-01-01T10:00:00Z"
}
```

#### Get User Accounts
Retrieve all accounts for a specific user.

```http
GET /v1/users/{userId}/accounts
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `userId` (UUID): User identifier

**Response (200):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "currency": "BRL",
    "status": "ACTIVE",
    "accountNumber": "1234567890-12",
    "branchCode": "0001",
    "type": "CHECKING",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

### Account Management

#### Create Account
Create a new account for an existing user.

```http
POST /v1/accounts
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "currency": "USD",
  "accountType": "SAVINGS"
}
```

**Response (201):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "currency": "USD",
  "status": "ACTIVE",
  "accountNumber": "0987654321-34",
  "branchCode": "0001",
  "type": "SAVINGS",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

#### Get Account
Retrieve account information by ID.

```http
GET /v1/accounts/{accountId}
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `accountId` (UUID): Account identifier

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "currency": "BRL",
  "status": "ACTIVE",
  "accountNumber": "1234567890-12",
  "branchCode": "0001",
  "type": "CHECKING",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

## 📊 Data Models

### User Status
- `ACTIVE`: User can perform transactions
- `BLOCKED`: User temporarily blocked
- `CLOSED`: User account closed

### Account Status
- `ACTIVE`: Account operational
- `BLOCKED`: Account temporarily blocked
- `CLOSED`: Account permanently closed

### Account Type
- `CHECKING`: Checking account
- `SAVINGS`: Savings account

### Supported Currencies
- `BRL`: Brazilian Real
- `USD`: US Dollar
- `EUR`: Euro
- `GBP`: British Pound
- `JPY`: Japanese Yen

## ❌ Error Handling

### Error Response Format
All errors follow the RFC 7807 Problem Details format:

```json
{
  "type": "https://api.modubank.com/errors/validation-error",
  "title": "Validation error",
  "status": 400,
  "detail": "Invalid request payload",
  "instance": "/v1/users",
  "timestamp": "2024-01-01T10:00:00Z",
  "correlationId": "abc123-def456",
  "errors": {
    "email": "must be a valid email",
    "cpf": "must have 11 digits"
  }
}
```

### Common Error Codes

#### 400 Bad Request
- `validation_error`: Request validation failed
- `currency_not_supported`: Currency not in supported list
- `user_not_found`: User does not exist

#### 401 Unauthorized
- `invalid_token`: JWT token is invalid or expired
- `missing_token`: Authorization header missing

#### 403 Forbidden
- `insufficient_permissions`: User lacks required permissions
- `account_blocked`: Account is blocked

#### 404 Not Found
- `user_not_found`: User does not exist
- `account_not_found`: Account does not exist

#### 409 Conflict
- `email_already_in_use`: Email already registered
- `cpf_already_in_use`: CPF already registered

#### 500 Internal Server Error
- `internal_error`: Unexpected server error
- `database_error`: Database operation failed

## 🔄 Rate Limiting

API endpoints are rate-limited to prevent abuse:

- **Anonymous endpoints**: 10 requests per minute
- **Authenticated endpoints**: 100 requests per minute
- **Admin endpoints**: 1000 requests per minute

Rate limit headers are included in responses:
```http
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1640995200
```

## 🧪 Testing

### Swagger UI
Interactive API documentation available at:
- **Development**: `http://localhost:8081/swagger-ui/index.html`
- **Production**: `https://api.modubank.com/account/swagger-ui/index.html`

### OpenAPI Specification
Complete API specification available at:
- **Development**: `http://localhost:8081/v3/api-docs`
- **Production**: `https://api.modubank.com/account/v3/api-docs`

## 📈 Monitoring

### Health Check
```http
GET /actuator/health
```

### Metrics
```http
GET /actuator/metrics
```

### Custom Metrics
- `account.user.registration.total`: Total user registrations
- `account.creation.total`: Total accounts created
- `account.user.lookup.time`: User lookup response time
- `account.creation.time`: Account creation response time

## 🔒 Security Considerations

### Data Validation
- All input is validated and sanitized
- SQL injection protection via parameterized queries
- XSS protection via input encoding

### Authentication
- JWT tokens with RS256 signing
- Token expiration: 1 hour
- Refresh token support

### Authorization
- Role-based access control (RBAC)
- Resource-level permissions
- Audit logging for all operations

---

**Related Documentation:**
- [Authentication & Authorization](./auth.md)
- [Error Handling](./error-handling.md)
- [Rate Limiting](./rate-limiting.md)
