# Use OpenJDK 21
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy jar file built by Maven
COPY target/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
