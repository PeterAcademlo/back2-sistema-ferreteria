FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .

# Crear directorio para la base de datos SQLite
RUN mkdir -p data

# Compilar el proyecto
RUN find . -name "*.java" > sources.txt
RUN javac -cp "lib/*" -d target @sources.txt

EXPOSE 8081

CMD ["java", "-cp", "target:lib/*", "gferre.Main"]