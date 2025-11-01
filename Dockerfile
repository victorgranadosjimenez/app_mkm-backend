# ---------------------------
# Etapa 1: Build
# ---------------------------
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias
COPY pom.xml .
COPY src ./src

# Forzamos UTF-8 y desactivamos filtrado de recursos
RUN mvn clean package -DskipTests

# ---------------------------
# Etapa 2: Runtime
# ---------------------------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el JAR construido
COPY --from=build /app/target/app_mkm-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

