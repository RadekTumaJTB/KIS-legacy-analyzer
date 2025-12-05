# Use Eclipse Temurin JRE 17 (Ubuntu-based, multi-platform)
FROM eclipse-temurin:17-jre

# Metadata
LABEL maintainer="KIS Application Team"
LABEL description="KIS Docker Test - Java 17 on Linux"
LABEL version="1.0"

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create application directory
WORKDIR /app

# Create non-root user for running the application
RUN groupadd -r kisapp && useradd -r -g kisapp kisapp

# Create directories for logs
RUN mkdir -p /app/logs && chown -R kisapp:kisapp /app

# Copy JAR file
COPY --chown=kisapp:kisapp target/kis-application.jar /app/kis-application.jar

# Expose application port
EXPOSE 8080

# Switch to non-root user
USER kisapp

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java"]
CMD ["-jar", "/app/kis-application.jar"]
