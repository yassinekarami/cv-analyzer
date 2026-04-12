# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /backoffice

# Copy Maven files first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /backoffice/target/*.jar backoffice.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backoffice.jar"]