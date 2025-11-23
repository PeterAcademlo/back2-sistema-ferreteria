FROM maven:3.9-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
# El shade-plugin crea un JAR con "-shaded" en el nombre
COPY --from=builder /app/target/back2-ferreteria-1.0.0-shaded.jar app.jar
RUN mkdir -p data

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]