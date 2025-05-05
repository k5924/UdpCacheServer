FROM eclipse-temurin:21-alpine
LABEL authors="tr1x"
RUN mkdir /opt/app
COPY build/libs/client.jar /opt/app
CMD ["java", "-jar", "/opt/app/client.jar"]
