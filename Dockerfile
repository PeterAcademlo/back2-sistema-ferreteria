FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir -p data
COPY --from=builder /app/target/back2-ferreteria-1.0.0.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]