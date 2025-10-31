# --- Step 1: Build the JAR ---
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml first (to cache dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (so this layer can be cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project and build
COPY src src
RUN ./mvnw clean package -DskipTests

# --- Step 2: Run the app ---
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
