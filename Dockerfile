# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21 as builder
WORKDIR /app
# Copy the source code
COPY src src
# Copy the Maven configuration file
COPY pom.xml .
# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime container
FROM openjdk:21-slim
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose the port the application runs on
EXPOSE $PORT 5005
# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

