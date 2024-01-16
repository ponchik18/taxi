FROM openjdk:17
WORKDIR /application
ENV PORT 8006
EXPOSE 8006
COPY target/promo-code-service-0.0.1-SNAPSHOT.jar promo-code-service.jar

ENTRYPOINT ["java", "-jar", "promo-code-service.jar"]