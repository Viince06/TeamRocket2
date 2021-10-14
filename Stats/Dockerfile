FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} stats.jar
RUN mkdir -p /Stats
ENTRYPOINT java -jar /stats.jar