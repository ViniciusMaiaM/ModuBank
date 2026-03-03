#!/bin/bash

# ModuBank Build Script
# This script builds the entire ModuBank project

set -e

echo "🔨 Building ModuBank..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Check if we're in the right directory
check_directory() {
    if [ ! -f "account/build.gradle.kts" ]; then
        print_error "Please run this script from the ModuBank root directory"
        exit 1
    fi
}

# Clean previous builds
clean_build() {
    echo "🧹 Cleaning previous builds..."
    cd account
    
    ./gradlew clean
    
    print_status "Build cleaned!"
    cd ..
}

# Build the account service
build_account_service() {
    echo "🏦 Building account service..."
    cd account
    
    ./gradlew build -x test
    
    if [ $? -eq 0 ]; then
        print_status "Account service built successfully!"
    else
        print_error "Account service build failed!"
        exit 1
    fi
    
    cd ..
}

# Build Docker images
build_docker_images() {
    echo "🐳 Building Docker images..."
    
    # Build account service image
    docker build -t modubank/account:latest ./account
    
    if [ $? -eq 0 ]; then
        print_status "Docker image built successfully!"
    else
        print_error "Docker build failed!"
        exit 1
    fi
}

# Create distribution packages
create_packages() {
    echo "📦 Creating distribution packages..."
    cd account
    
    ./gradlew distTar distZip
    
    if [ $? -eq 0 ]; then
        print_status "Distribution packages created!"
        print_info "Packages available at: account/build/distributions/"
    else
        print_warning "Distribution package creation failed!"
    fi
    
    cd ..
}

# Generate build information
generate_build_info() {
    echo "📋 Generating build information..."
    
    BUILD_INFO_FILE="account/build/build-info.json"
    
    cat > "$BUILD_INFO_FILE" << EOF
{
  "buildTime": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")",
  "gitCommit": "$(git rev-parse HEAD)",
  "gitBranch": "$(git rev-parse --abbrev-ref HEAD)",
  "version": "$(cd account && ./gradlew properties -q | grep '^version:' | awk '{print $2}')",
  "javaVersion": "$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)",
  "gradleVersion": "$(cd account && ./gradlew --version | grep 'Gradle' | awk '{print $2}')"
}
EOF
    
    print_status "Build information generated!"
}

# Verify build artifacts
verify_build() {
    echo "🔍 Verifying build artifacts..."
    cd account
    
    # Check if JAR was created
    if [ -f "build/libs/*.jar" ]; then
        print_status "JAR artifact verified!"
    else
        print_error "JAR artifact not found!"
        exit 1
    fi
    
    # Check if Docker image exists
    if docker images | grep -q "modubank/account"; then
        print_status "Docker image verified!"
    else
        print_warning "Docker image not found!"
    fi
    
    cd ..
}

# Main build flow
main() {
    echo "🚀 Starting ModuBank build process..."
    echo ""
    
    check_directory
    
    # Parse command line arguments
    CLEAN_BUILD=false
    BUILD_DOCKER=false
    CREATE_PACKAGES=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --clean)
                CLEAN_BUILD=true
                shift
                ;;
            --docker)
                BUILD_DOCKER=true
                shift
                ;;
            --package)
                CREATE_PACKAGES=true
                shift
                ;;
            --all)
                CLEAN_BUILD=true
                BUILD_DOCKER=true
                CREATE_PACKAGES=true
                shift
                ;;
            *)
                print_error "Unknown option: $1"
                echo "Usage: $0 [--clean] [--docker] [--package] [--all]"
                exit 1
                ;;
        esac
    done
    
    # Execute build steps
    if [ "$CLEAN_BUILD" = true ]; then
        clean_build
    fi
    
    build_account_service
    generate_build_info
    
    if [ "$BUILD_DOCKER" = true ]; then
        build_docker_images
    fi
    
    if [ "$CREATE_PACKAGES" = true ]; then
        create_packages
    fi
    
    verify_build
    
    echo ""
    echo "🎉 ModuBank build completed successfully!"
    echo ""
    echo "📦 Build artifacts:"
    echo "  - JAR file: account/build/libs/"
    echo "  - Build info: account/build/build-info.json"
    
    if [ "$BUILD_DOCKER" = true ]; then
        echo "  - Docker image: modubank/account:latest"
    fi
    
    if [ "$CREATE_PACKAGES" = true ]; then
        echo "  - Distribution packages: account/build/distributions/"
    fi
    
    echo ""
    echo "🚀 To run the application:"
    echo "  - Local: cd account && ./gradlew bootRun"
    echo "  - Docker: docker-compose up account"
}

# Run main function with all arguments
main "$@"
