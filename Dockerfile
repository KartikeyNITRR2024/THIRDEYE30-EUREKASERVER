# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Copy project files
COPY . /app
WORKDIR /app

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM openjdk:17-slim

# Copy the JAR from the build stage
COPY --from=build /app/target/THIRDEYE3.0_EUREKASERVER-0.0.1-SNAPSHOT.jar app.jar

# Expose Eureka Server port
EXPOSE 8761

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
