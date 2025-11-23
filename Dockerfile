FROM maven:3.9.6-openjdk-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
RUN mkdir -p data
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]