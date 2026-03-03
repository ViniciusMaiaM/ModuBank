#!/bin/bash

# ModuBank Development Setup Script
# This script sets up the development environment for ModuBank

set -e

echo "🚀 Setting up ModuBank development environment..."

# Check prerequisites
check_prerequisites() {
    echo "📋 Checking prerequisites..."
    
    if ! command -v java &> /dev/null; then
        echo "❌ Java is not installed. Please install Java 21 or higher."
        exit 1
    fi
    
    if ! command -v docker &> /dev/null; then
        echo "❌ Docker is not installed. Please install Docker."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        echo "❌ Docker Compose is not installed. Please install Docker Compose."
        exit 1
    fi
    
    echo "✅ All prerequisites are installed!"
}

# Setup infrastructure
setup_infrastructure() {
    echo "🐳 Setting up infrastructure..."
    
    # Start PostgreSQL
    docker-compose up -d postgres
    
    # Wait for database to be ready
    echo "⏳ Waiting for database to be ready..."
    sleep 10
    
    # Check if database is ready
    until docker-compose exec -T postgres pg_isready -U modubank; do
        echo "Waiting for postgres..."
        sleep 2
    done
    
    echo "✅ Infrastructure is ready!"
}

# Build project
build_project() {
    echo "🔨 Building project..."
    
    cd account
    ./gradlew build
    
    echo "✅ Project built successfully!"
}

# Run tests
run_tests() {
    echo "🧪 Running tests..."
    
    cd account
    ./gradlew test
    
    echo "✅ All tests passed!"
}

# Main setup flow
main() {
    check_prerequisites
    setup_infrastructure
    build_project
    run_tests
    
    echo ""
    echo "🎉 ModuBank development environment is ready!"
    echo ""
    echo "📚 Next steps:"
    echo "  1. Start the application: cd account && ./gradlew bootRun"
    echo "  2. Access Swagger UI: http://localhost:8081/swagger-ui/index.html"
    echo "  3. View metrics: http://localhost:8081/actuator/metrics"
    echo "  4. Check health: http://localhost:8081/actuator/health"
    echo ""
    echo "📖 For more information, check the README.md file."
}

# Run main function
main "$@"
