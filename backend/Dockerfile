FROM openjdk:17-jdk-alpine

EXPOSE 8080

COPY build/libs/backend.jar transfer-service.jar

CMD ["java", "-jar", "transfer-service.jar"]
