# Use an official OpenJDK runtime
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar to the container
COPY build/libs/hyperativa-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
