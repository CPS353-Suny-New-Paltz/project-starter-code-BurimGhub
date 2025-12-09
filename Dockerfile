FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR
COPY build/libs/*.jar /app/server.jar

# Copy compiled classes
COPY build/classes/java/main /app/classes

# Copy all dependencies (this is what's missing!)
COPY build/libs/*.jar /app/libs/

# Create Resources directory
RUN mkdir -p /app/Resources

# Expose ports
EXPOSE 9090 9091

# Include all JARs in classpath
ENTRYPOINT ["java", "-cp", "/app/server.jar:/app/classes:/app/libs/*"]