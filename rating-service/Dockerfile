FROM openjdk:17
WORKDIR /application
ENV PORT 8004
EXPOSE 8004
COPY target/rating-service-0.0.1-SNAPSHOT.jar rating-service.jar

ENTRYPOINT ["java", "-jar", "rating-service.jar"]