FROM openjdk:17
WORKDIR /application
ENV PORT 8003
EXPOSE 8003
COPY target/rides-service-0.0.1-SNAPSHOT.jar rides-service.jar

ENTRYPOINT ["java", "-jar", "rides-service.jar"]