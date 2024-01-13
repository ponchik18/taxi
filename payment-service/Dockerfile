FROM openjdk:17
WORKDIR /application
ENV PORT 8005
EXPOSE 8005
COPY target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar

ENTRYPOINT ["java", "-jar", "payment-service.jar"]