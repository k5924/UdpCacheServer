FROM eclipse-temurin:21-alpine
LABEL authors="tr1x"
RUN mkdir /opt/app
COPY build/libs/server.jar /opt/app
CMD ["java", "-jar", "/opt/app/server.jar"]
