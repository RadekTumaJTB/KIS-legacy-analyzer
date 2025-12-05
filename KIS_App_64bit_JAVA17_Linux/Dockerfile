# Use Red Hat UBI 10 minimal as base image
FROM registry.access.redhat.com/ubi10/ubi-minimal:latest

# Metadata
LABEL maintainer="KIS Application Team"
LABEL description="KIS Application - Java 17 on Linux UBI-base10 (64-bit)"
LABEL version="2.0"

# Install Java 17 JDK
RUN microdnf install -y java-17-openjdk-devel \
    && microdnf clean all

# Set Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk \
    PATH=$PATH:$JAVA_HOME/bin

# Create application directory
WORKDIR /app

# Create non-root user for running the application
RUN groupadd -r kisapp && useradd -r -g kisapp kisapp

# Create directories for logs and config
RUN mkdir -p /app/logs /app/config /app/lib && \
    chown -R kisapp:kisapp /app

# Copy application files (will be populated during migration)
COPY --chown=kisapp:kisapp . /app/

# Install Oracle JDBC driver (if needed)
# RUN curl -o /app/lib/ojdbc11.jar https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc11/23.3.0.23.09/ojdbc11-23.3.0.23.09.jar

# Expose application port
EXPOSE 8080

# Switch to non-root user
USER kisapp

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Set entry point
ENTRYPOINT ["java"]

# Default command (can be overridden in docker-compose.yml)
CMD ["-jar", "/app/kis-application.jar"]
