# Use Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose application port
EXPOSE 8087

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
