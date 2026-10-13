# ==============================================================================
# Etapa 1: Build (Compilación con Maven Wrapper y Temurin 25)
# ==============================================================================
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

# Copiamos archivos de Maven wrapper y pom.xml
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargamos dependencias en caché
RUN ./mvnw dependency:go-offline -B

# Copiamos código fuente y compilamos
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==============================================================================
# Etapa 2: Runtime (Imagen final liviana y segura)
# ==============================================================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Crear un usuario sin privilegios root por seguridad (Best practice)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiamos solo el archivo .jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto estándar 8080
EXPOSE 8080

# Variable de entorno de configuración de memoria óptima para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
