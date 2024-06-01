FROM openjdk:17-jdk-alpine
ARG JAR_FILE=*.jar
COPY ./target/car-reset-application-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]