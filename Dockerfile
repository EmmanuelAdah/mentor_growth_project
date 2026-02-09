# =========================
# Stage 1: Build
# =========================
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# 1. Copy the wrapper files AND the pom.xml
# You need mvnw and the .mvn folder to run the wrapper
COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .

# 2. FIX FOR EXIT CODE 126: Grant execution permission to the wrapper
RUN chmod +x mvnw

# 3. Use ./mvnw instead of mvn to ensure version consistency
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar using the wrapper
RUN ./mvnw clean package -DskipTests


# =========================
# Stage 2: Runtime
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install wget for healthcheck
RUN apk add --no-cache wget

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy jar explicitly (using a wildcard can be risky, but this works)
COPY --from=build /app/target/*.jar app.jar

# Set permissions
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

# JVM container optimizations
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]