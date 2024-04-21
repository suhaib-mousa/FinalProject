FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./src/main/java/com/example/node/databases/Documents.json /src/main/java/com/example/node/databases/Documents.json
COPY ./src/main/java/com/example/node/databases/DocCounter.txt /src/main/java/com/example/node/databases/DocCounter.txt
COPY ./src/main/java/com/example/node/affinity/affinity.json /src/main/java/com/example/node/affinity/affinity.json
COPY ./target/node-0.0.1-SNAPSHOT.jar node.jar
ENTRYPOINT ["java","-jar","/node.jar"]