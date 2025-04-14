# Imagen base con JDK 8
FROM eclipse-temurin:8-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por Maven
COPY target/*.jar app.jar

# Exponemos el puerto 8080 (usado por Spring Boot)
EXPOSE 8080

# Comando que inicia la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
