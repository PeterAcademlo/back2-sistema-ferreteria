FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .

# Compilar
RUN find src -name "*.java" > sources.txt
RUN javac -cp "lib/*" -d target @sources.txt

EXPOSE 8081
CMD ["java", "-cp", "target:lib/*", "gferre.Main"]