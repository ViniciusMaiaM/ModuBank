# Development Setup

## 🚀 Quick Start

This guide will help you set up the ModuBank development environment on your local machine.

## 📋 Prerequisites

### Required Software
- **Java 21+**: OpenJDK or Oracle JDK
- **Docker**: Latest stable version
- **Docker Compose**: Version 2.0+
- **Git**: For version control

### Recommended Tools
- **IntelliJ IDEA**: Kotlin/Java development
- **VS Code**: Lightweight alternative
- **Postman**: API testing
- **DBeaver**: Database management

## 🛠️ Environment Setup

### 1. Clone Repository
```bash
git clone https://github.com/your-org/modubank.git
cd modubank
```

### 2. Run Setup Script
```bash
chmod +x scripts/setup.sh
./scripts/setup.sh
```

The setup script will:
- ✅ Verify prerequisites
- 🐳 Start PostgreSQL database
- 🔨 Build the project
- 🧪 Run initial tests

### 3. Manual Setup (Alternative)

#### Start Infrastructure
```bash
docker-compose up -d postgres
```

#### Build Project
```bash
cd account
./gradlew build
```

#### Run Tests
```bash
./gradlew test
```

## 🏗️ Project Structure

```
ModuBank/
├── account/                 # Account service (Kotlin/Spring Boot)
│   ├── src/
│   │   ├── main/kotlin/     # Source code
│   │   ├── test/kotlin/     # Test code
│   │   └── resources/      # Configuration files
│   ├── build.gradle.kts      # Gradle build configuration
│   └── Dockerfile          # Docker configuration
├── scripts/                # Utility scripts
│   ├── setup.sh           # Environment setup
│   ├── build.sh           # Build automation
│   ├── test.sh            # Test automation
│   ├── deploy.sh          # Deployment automation
│   ├── format.sh          # Code formatting
│   └── lint.sh           # Code linting
├── docs/                  # Documentation
├── docker-compose.yml      # Development infrastructure
└── README.md             # Project overview
```

## 🔧 Development Workflow

### 1. Start Services
```bash
# Start database only
docker-compose up -d postgres

# Start all services (when available)
docker-compose up -d
```

### 2. Run Application
```bash
cd account
./gradlew bootRun
```

The application will be available at:
- **API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui/index.html
- **Health Check**: http://localhost:8081/actuator/health

### 3. Development Commands

#### Build & Test
```bash
# Build project
./scripts/build.sh

# Run all tests
./scripts/test.sh

# Run specific test types
./scripts/test.sh unit        # Unit tests only
./scripts/test.sh integration # Integration tests only
./scripts/test.sh quality    # Code quality checks
```

#### Code Quality
```bash
# Format code
./scripts/format.sh

# Run linting
./scripts/lint.sh

# Check style only
./scripts/format.sh check
```

#### Database Operations
```bash
# View database logs
docker-compose logs -f postgres

# Connect to database
docker-compose exec postgres psql -U modubank -d modubank

# Reset database
docker-compose down -v
docker-compose up -d postgres
```

## 🐳 Docker Services

### PostgreSQL
- **Port**: 5432
- **Database**: modubank
- **User**: modubank
- **Password**: modubank
- **Connection URL**: `jdbc:postgresql://localhost:5432/modubank`

### Environment Variables
Create a `.env` file in the project root:
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/modubank
DB_USER=modubank
DB_PASSWORD=modubank
DB_NAME=modubank
DB_PORT=5432

# Application Configuration
ACCOUNT_PORT=8081
SPRING_PROFILES_ACTIVE=development
LOG_LEVEL=INFO
```

## 🧪 Testing

### Test Structure
```
src/test/kotlin/
├── unit/                   # Unit tests
├── integration/            # Integration tests
└── e2e/                  # End-to-end tests
```

### Running Tests
```bash
# All tests
./gradlew test

# Unit tests only
./gradlew test --tests "*UnitTest"

# Integration tests only
./gradlew test --tests "*IT"

# Specific test class
./gradlew test --tests RegisterUserTest

# Test with coverage
./gradlew test jacocoTestReport
```

### Test Database
Tests use Testcontainers with PostgreSQL:
- Automatically started for integration tests
- Isolated from development database
- Clean state for each test run

## 📊 Monitoring & Debugging

### Application Logs
```bash
# View logs in real-time
tail -f account/build/logs/application.log

# View logs with Docker
docker-compose logs -f account
```

### Health Checks
```bash
# Application health
curl http://localhost:8081/actuator/health

# Database health
curl http://localhost:8081/actuator/health/db

# Detailed health info
curl http://localhost:8081/actuator/health/detailed
```

### Metrics
```bash
# All metrics
curl http://localhost:8081/actuator/metrics

# Specific metric
curl http://localhost:8081/actuator/metrics/account.user.registration.total
```

### Debugging
```bash
# Run with debug port
./gradlew bootRun --debug-jvm

# Remote debug configuration
# Host: localhost
# Port: 5005
```

## 🔌 IDE Configuration

### IntelliJ IDEA
1. **Import Project**: File → Open → Select `build.gradle.kts`
2. **SDK Setup**: File → Project Structure → SDK → JDK 21
3. **Code Style**: Import `.editorconfig` settings
4. **Plugins**: Install Kotlin, Docker, and Database Navigator

### VS Code
1. **Extensions**:
   - Kotlin Language
   - Docker
   - Gradle for Java
   - REST Client
   - Database Client

2. **Settings**:
   - Java Home: Set to JDK 21
   - Gradle: Use wrapper
   - Format: Enable editorconfig

## 🚨 Common Issues

### Port Conflicts
```bash
# Check what's using port 8081
lsof -i :8081

# Kill process
kill -9 <PID>
```

### Database Connection Issues
```bash
# Reset database
docker-compose down -v
docker-compose up -d postgres

# Check database status
docker-compose ps postgres
```

### Build Issues
```bash
# Clean build
./gradlew clean build

# Refresh dependencies
./gradlew build --refresh-dependencies

# Clear Gradle cache
rm -rf ~/.gradle/caches
```

### Permission Issues
```bash
# Fix script permissions
chmod +x scripts/*.sh

# Fix Docker permissions
sudo usermod -aG docker $USER
# Then logout and login again
```

## 📚 Development Resources

### API Documentation
- **Swagger UI**: http://localhost:8081/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8081/v3/api-docs

### Database Schema
- **Migrations**: `account/src/main/resources/db/migration/`
- **Schema Documentation**: See [Data Model](../architecture/data-model.md)

### Code Examples
- **Sample Requests**: `docs/api/examples/`
- **Test Cases**: `account/src/test/kotlin/`

## 🔄 Daily Development Workflow

### 1. Start Day
```bash
# Pull latest changes
git pull origin main

# Start infrastructure
docker-compose up -d postgres

# Build project
./scripts/build.sh
```

### 2. Development
```bash
# Run application
./gradlew bootRun

# In another terminal, run tests
./scripts/test.sh unit
```

### 3. Before Commit
```bash
# Format code
./scripts/format.sh

# Run linting
./scripts/lint.sh

# Run all tests
./scripts/test.sh
```

### 4. End Day
```bash
# Stop services
docker-compose down

# Commit changes
git add .
git commit -m "feat: implement new feature"
git push origin feature-branch
```

## 🆘 Getting Help

### Documentation
- [Architecture Overview](../architecture/system-overview.md)
- [API Documentation](../api/account-service.md)
- [Troubleshooting](../troubleshooting/common-issues.md)

### Community
- **GitHub Issues**: Report bugs and request features
- **Discussions**: Ask questions and share ideas
- **Wiki**: Additional documentation and guides

### Support
- **Email**: dev-team@modubank.com
- **Slack**: #modubank-dev channel

---

**Next Steps**:
- Read [Coding Standards](../development/coding-standards.md)
- Check [Testing Guidelines](../development/testing.md)
- Review [API Design](../architecture/api-design.md)
