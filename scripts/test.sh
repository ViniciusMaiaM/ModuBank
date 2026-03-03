#!/bin/bash

# ModuBank Test Script
# This script runs all tests and generates reports

set -e

echo "🧪 Running ModuBank test suite..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
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

# Check if we're in the right directory
check_directory() {
    if [ ! -f "account/build.gradle.kts" ]; then
        print_error "Please run this script from the ModuBank root directory"
        exit 1
    fi
}

# Run unit tests
run_unit_tests() {
    echo "🔬 Running unit tests..."
    cd account
    
    ./gradlew test --console=plain
    
    if [ $? -eq 0 ]; then
        print_status "Unit tests passed!"
    else
        print_error "Unit tests failed!"
        exit 1
    fi
    
    cd ..
}

# Run integration tests
run_integration_tests() {
    echo "🔗 Running integration tests..."
    cd account
    
    ./gradlew test --tests "*IT" --console=plain
    
    if [ $? -eq 0 ]; then
        print_status "Integration tests passed!"
    else
        print_error "Integration tests failed!"
        exit 1
    fi
    
    cd ..
}

# Generate test reports
generate_reports() {
    echo "📊 Generating test reports..."
    cd account
    
    # Generate test coverage report if JaCoCo is available
    if ./gradlew tasks | grep -q jacoco; then
        ./gradlew jacocoTestReport
        print_status "JaCoCo coverage report generated!"
        print_warning "Coverage report available at: account/build/reports/jacoco/test/html/index.html"
    fi
    
    # Generate test results summary
    ./gradlew test --continue
    
    cd ..
}

# Run code quality checks
run_quality_checks() {
    echo "🔍 Running code quality checks..."
    cd account
    
    # Run ktlint
    ./gradlew ktlintCheck
    
    if [ $? -eq 0 ]; then
        print_status "Code style checks passed!"
    else
        print_warning "Code style issues found. Run './gradlew ktlintFormat' to fix."
    fi
    
    cd ..
}

# Check for vulnerabilities
check_vulnerabilities() {
    echo "🔒 Checking for vulnerabilities..."
    cd account
    
    # Check dependency updates
    if ./gradlew tasks | grep -q dependencyUpdates; then
        ./gradlew dependencyUpdates
        print_status "Dependency check completed!"
    fi
    
    cd ..
}

# Main test flow
main() {
    echo "🚀 Starting ModuBank test suite..."
    echo ""
    
    check_directory
    run_unit_tests
    run_integration_tests
    generate_reports
    run_quality_checks
    check_vulnerabilities
    
    echo ""
    echo "🎉 All tests completed successfully!"
    echo ""
    echo "📊 Test reports available:"
    echo "  - Unit tests: account/build/test-results/test/"
    echo "  - Integration tests: account/build/test-results/integrationTest/"
    if [ -f "account/build/reports/jacoco/test/html/index.html" ]; then
        echo "  - Coverage report: account/build/reports/jacoco/test/html/index.html"
    fi
}

# Handle script arguments
case "${1:-all}" in
    "unit")
        check_directory
        run_unit_tests
        ;;
    "integration")
        check_directory
        run_integration_tests
        ;;
    "quality")
        check_directory
        run_quality_checks
        ;;
    "security")
        check_directory
        check_vulnerabilities
        ;;
    "all"|*)
        main
        ;;
esac
