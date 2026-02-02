# First Stage: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Second Stage: Run
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/bet-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:+$JAVA_OPTS} -jar /app/app.jar"]