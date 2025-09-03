FROM openjdk:17-jdk-slim

# Create a non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/electronic-store-1.0.0-SNAPSHOT.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]
