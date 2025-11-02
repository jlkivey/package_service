# Package Intake Service - Build Instructions

## Prerequisites

- **Java 17** (OpenJDK 17.0.15 or compatible)
- **Maven 3.9.11** or compatible
- **macOS/Linux** environment

## Java Version Issue

**IMPORTANT**: This project requires Java 17, but Maven may default to a different Java version (like Java 24). You must explicitly set the Java version for Maven to use.

### Check Your Java Versions

```bash
# Check system Java version
java -version

# Check Maven's Java version
mvn -version
```

If Maven shows a different Java version than Java 17, you need to set `JAVA_HOME`.

## Build Commands

### 1. Set Java 17 Environment (Required)

```bash
# For macOS with Homebrew
export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.15/libexec/openjdk.jdk/Contents/Home

# For Linux (adjust path as needed)
# export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

### 2. Build for Production

```bash
# Navigate to the project directory
cd /Users/jeffivey/dev/package_service/packageintake

# Build with production profile (skipping tests)
mvn clean package -Pprod -DskipTests -Dmaven.test.skip=true
```

### 3. Copy to Production Directory

```bash
# Copy the built JAR to production deployment directory
cp target/packageintake-0.0.1-SNAPSHOT.jar ../production-deploy/
```

## Complete Build Script

Here's a complete script that handles everything:

```bash
#!/bin/bash

# Set Java 17 environment
export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.15/libexec/openjdk.jdk/Contents/Home

# Navigate to project directory
cd /Users/jeffivey/dev/package_service/packageintake

# Build for production
echo "Building for production..."
mvn clean package -Pprod -DskipTests -Dmaven.test.skip=true

# Copy to production directory
echo "Copying JAR to production directory..."
cp target/packageintake-0.0.1-SNAPSHOT.jar ../production-deploy/

echo "Build complete! JAR file ready for deployment."
```

## Troubleshooting

### Issue: "Fatal error compiling: java.lang.ExceptionInInitializerError"

**Cause**: Maven is using the wrong Java version (likely Java 24 instead of Java 17).

**Solution**: Set `JAVA_HOME` to point to Java 17 before running Maven commands.

### Issue: "cannot find symbol" errors for Lombok methods

**Cause**: Lombok annotation processing isn't working.

**Solution**: The `pom.xml` has been updated with proper Lombok annotation processor configuration. Make sure you're using Java 17.

### Issue: "The requested profile 'prod' could not be activated"

**Cause**: Running Maven from the wrong directory.

**Solution**: Make sure you're in the `packageintake` directory (where `pom.xml` is located), not the parent `package_service` directory.

## Production Deployment

After building, the JAR file is ready for deployment:

1. **JAR Location**: `production-deploy/packageintake-0.0.1-SNAPSHOT.jar`
2. **Configuration**: `production-deploy/application-prod.properties`
3. **Deployment Script**: `production-deploy/deploy/deploy.sh`

See `production-deploy/DEPLOYMENT_GUIDE.md` for complete deployment instructions.

## Build Output

Successful build will create:
- `target/packageintake-0.0.1-SNAPSHOT.jar` - Production-ready JAR file
- `target/packageintake-0.0.1-SNAPSHOT.jar.original` - Original JAR before Spring Boot repackaging

## Notes

- Tests are skipped during production builds (`-DskipTests -Dmaven.test.skip=true`)
- The production profile (`-Pprod`) activates production-specific configuration
- Lombok generates getters, setters, and builders at compile time
- The Spring Boot Maven plugin repackages the JAR with all dependencies included
