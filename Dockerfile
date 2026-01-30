# First Stage: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Second Stage: Run
# Substituindo openjdk:17 por amazoncorretto:21 (mais estável e compatível com seu build)
FROM amazoncorretto:21-alpine
WORKDIR /app
# Certifique-se de que o nome do .jar no target está correto
COPY --from=build /app/target/bet-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]