FROM openjdk:17
WORKDIR /application
ENV PORT 8000
EXPOSE 8000
COPY target/discovery-server-0.0.1-SNAPSHOT.jar discovery-service.jar

ENTRYPOINT ["java", "-jar", "discovery-service.jar"]