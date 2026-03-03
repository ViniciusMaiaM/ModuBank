#!/bin/bash

# ModuBank Code Format Script
# This script formats and validates code style

set -e

echo "🎨 Formatting ModuBank code..."

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

# Format Kotlin code with ktlint
format_kotlin() {
    echo "🔧 Formatting Kotlin code..."
    cd account
    
    ./gradlew ktlintFormat
    
    if [ $? -eq 0 ]; then
        print_status "Kotlin code formatted!"
    else
        print_error "Kotlin formatting failed!"
        exit 1
    fi
    
    cd ..
}

# Check code style
check_style() {
    echo "🔍 Checking code style..."
    cd account
    
    ./gradlew ktlintCheck
    
    if [ $? -eq 0 ]; then
        print_status "Code style check passed!"
    else
        print_error "Code style issues found!"
        print_info "Run './gradlew ktlintFormat' to fix automatically"
        exit 1
    fi
    
    cd ..
}

# Format YAML files
format_yaml() {
    echo "📄 Formatting YAML files..."
    
    if command -v yamllint &> /dev/null; then
        # Find and lint YAML files
        find . -name "*.yml" -o -name "*.yaml" | grep -v ".git" | while read -r file; do
            echo "Checking $file..."
            yamllint -d relaxed "$file" || true
        done
        print_status "YAML files checked!"
    else
        print_warning "yamllint not found, skipping YAML formatting"
    fi
}

# Format Markdown files
format_markdown() {
    echo "📝 Formatting Markdown files..."
    
    if command -v markdownlint &> /dev/null; then
        # Find and lint Markdown files
        find . -name "*.md" | grep -v ".git" | while read -r file; do
            echo "Checking $file..."
            markdownlint "$file" || true
        done
        print_status "Markdown files checked!"
    else
        print_warning "markdownlint not found, skipping Markdown formatting"
    fi
}

# Format shell scripts
format_shell() {
    echo "🐚 Formatting shell scripts..."
    
    if command -v shfmt &> /dev/null; then
        # Find and format shell scripts
        find scripts/ -name "*.sh" -exec shfmt -w -i 4 {} \;
        print_status "Shell scripts formatted!"
    else
        print_warning "shfmt not found, skipping shell script formatting"
    fi
}

# Format all files
format_all() {
    format_kotlin
    format_shell
    
    # Optional formatters
    format_yaml
    format_markdown
    
    print_status "All formatting completed!"
}

# Main formatting flow
main() {
    echo "🎨 Starting ModuBank code formatting..."
    echo ""
    
    check_directory
    
    # Parse command line arguments
    case "${1:-all}" in
        "kotlin")
            format_kotlin
            ;;
        "check")
            check_style
            ;;
        "shell")
            format_shell
            ;;
        "yaml")
            format_yaml
            ;;
        "markdown")
            format_markdown
            ;;
        "all"|*)
            format_all
            ;;
    esac
    
    echo ""
    echo "🎉 Code formatting completed!"
    echo ""
    echo "💡 Tips:"
    echo "  - Run './scripts/format.sh check' to validate style without formatting"
    echo "  - Run './scripts/format.sh kotlin' to format only Kotlin code"
    echo "  - Install yamllint, markdownlint, and shfmt for better formatting"
}

# Run main function with all arguments
main "$@"