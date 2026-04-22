FROM eclipse-temurin:21-jre-alpine

ARG APP_JAR=target/*.jar
COPY ${APP_JAR} /app/application.jar
COPY rest-demo.mv.db /app/

WORKDIR /app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]