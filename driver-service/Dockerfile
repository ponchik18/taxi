FROM openjdk:17
WORKDIR /application
ENV PORT 8002
EXPOSE 8002
COPY target/driver-service-0.0.1-SNAPSHOT.jar driver-service.jar

ENTRYPOINT ["java", "-jar", "driver-service.jar"]