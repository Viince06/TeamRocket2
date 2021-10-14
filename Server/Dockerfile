FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} server.jar
ENTRYPOINT java -jar /server.jar