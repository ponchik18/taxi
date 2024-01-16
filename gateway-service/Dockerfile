FROM openjdk:17
WORKDIR /application
ENV PORT 8003
EXPOSE 8003
COPY target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service.jar

ENTRYPOINT ["java", "-jar", "gateway-service.jar"]