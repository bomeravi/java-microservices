# Use a base image with Java. Matching the version in your pom.xml (Java 21) is recommended.
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot application JAR file into the container
# This relies on the output of the 'mvn package' command from Step 1
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your product service listens on (default Spring Boot is 8080)
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
