FROM adoptopenjdk/openjdk8-openj9:latest
VOLUME /tmp
COPY target/*.jar api-gateway.jar
ENTRYPOINT ["java", "-Xshareclasses:name=kerubin", "-jar", "/api-gateway.jar"]