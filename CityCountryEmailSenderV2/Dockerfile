# Use Maven 3.8.4 with OpenJDK 17 as the build stage
FROM maven:3.8.4-openjdk-17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the environment file to the working directory
COPY .env /app/.env

# Copy the entire project to the working directory
COPY . /app/.

# Build the project and skip tests
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# Use Eclipse Temurin JRE 17 for the runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the environment file from the builder stage to the runtime stage
COPY --from=builder /app/.env /app/.env

# Copy the built JAR file from the builder stage to the runtime stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose port
EXPOSE 8080

# Set the entrypoint for the container to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]