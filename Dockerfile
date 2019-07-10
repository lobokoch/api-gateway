FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]