# Base image with JDK 11
FROM openjdk:11

# Copy the compiled jar file to the container
ADD AdvancedWeb_Backend.jar /app/

# Set the working directory
WORKDIR /app

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "AdvancedWeb_Backend.jar"]
