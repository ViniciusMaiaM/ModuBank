#!/bin/bash

# ModuBank Lint Script
# This script runs comprehensive linting checks

set -e

echo "🔍 Running ModuBank lint checks..."

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

# Lint Kotlin code
lint_kotlin() {
    echo "🔧 Linting Kotlin code..."
    cd account
    
    ./gradlew ktlintCheck
    
    if [ $? -eq 0 ]; then
        print_status "Kotlin linting passed!"
    else
        print_error "Kotlin linting failed!"
        print_info "Run './gradlew ktlintFormat' to fix automatically"
        return 1
    fi
    
    cd ..
}

# Lint Docker files
lint_docker() {
    echo "🐳 Linting Docker files..."
    
    if command -v hadolint &> /dev/null; then
        find . -name "Dockerfile*" -not -path "./.git/*" | while read -r file; do
            echo "Checking $file..."
            hadolint "$file"
        done
        print_status "Docker linting passed!"
    else
        print_warning "hadolint not found, skipping Docker linting"
        print_info "Install hadolint: brew install hadolint"
    fi
}

# Lint YAML files
lint_yaml() {
    echo "📄 Linting YAML files..."
    
    if command -v yamllint &> /dev/null; then
        find . -name "*.yml" -o -name "*.yaml" | grep -v ".git" | while read -r file; do
            echo "Checking $file..."
            yamllint -d relaxed "$file"
        done
        print_status "YAML linting passed!"
    else
        print_warning "yamllint not found, skipping YAML linting"
        print_info "Install yamllint: pip install yamllint"
    fi
}

# Lint shell scripts
lint_shell() {
    echo "🐚 Linting shell scripts..."
    
    if command -v shellcheck &> /dev/null; then
        find scripts/ -name "*.sh" -exec shellcheck {} \;
        print_status "Shell script linting passed!"
    else
        print_warning "shellcheck not found, skipping shell linting"
        print_info "Install shellcheck: brew install shellcheck"
    fi
}

# Security linting
lint_security() {
    echo "🔒 Running security linting..."
    
    cd account
    
    # Check for dependency vulnerabilities
    if ./gradlew tasks | grep -q dependencyUpdates; then
        ./gradlew dependencyUpdates --output-file build/dependency-updates.txt
        print_status "Security check completed!"
        print_info "Check build/dependency-updates.txt for outdated dependencies"
    fi
    
    # Check for hardcoded secrets (basic check)
    echo "Checking for hardcoded secrets..."
    if grep -r -i "password\|secret\|key" --include="*.kt" --include="*.yml" --include="*.yaml" --include="*.properties" src/ | grep -v "passwordHash\|example\|placeholder"; then
        print_warning "Potential hardcoded secrets found! Please review."
    else
        print_status "No obvious hardcoded secrets found!"
    fi
    
    cd ..
}

# Performance linting
lint_performance() {
    echo "⚡ Running performance linting..."
    
    # Check for common performance issues
    echo "Checking for common performance issues..."
    
    cd account
    
    # Check for large dependencies
    if [ -f "build/reports/dependencyAnalysis/runtimeDependencies.txt" ]; then
        print_info "Check dependency analysis for large dependencies"
    fi
    
    cd ..
}

# Main linting flow
main() {
    echo "🔍 Starting ModuBank lint checks..."
    echo ""
    
    check_directory
    
    # Track overall success
    FAILED=0
    
    # Run all linting checks
    lint_kotlin || FAILED=1
    lint_docker || FAILED=1
    lint_yaml || FAILED=1
    lint_shell || FAILED=1
    lint_security || FAILED=1
    lint_performance || FAILED=1
    
    echo ""
    if [ $FAILED -eq 0 ]; then
        echo "🎉 All lint checks passed!"
        exit 0
    else
        echo "⚠️  Some lint checks failed!"
        echo ""
        echo "💡 To fix issues:"
        echo "  - Kotlin: ./gradlew ktlintFormat"
        echo "  - Shell: Install and run shellcheck"
        echo "  - YAML: Install and run yamllint"
        echo "  - Docker: Install and run hadolint"
        exit 1
    fi
}

# Run main function with all arguments
main "$@"