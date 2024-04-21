FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/bootstrapper-0.0.1-SNAPSHOT.jar bootstrapper.jar
COPY ./src/main/java/com/example/bootstrapper/data/users.json /src/main/java/com/example/bootstrapper/data/users.json
ENTRYPOINT ["java","-jar","/bootstrapper.jar"]