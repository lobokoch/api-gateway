FROM adoptopenjdk/openjdk8-openj9:latest
VOLUME /tmp
COPY target/*.jar service-discovery.jar
ENTRYPOINT ["java", "-Xshareclasses:name=kerubin", "-jar", "/service-discovery.jar"]