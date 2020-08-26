FROM openjdk:11-jre-slim-sid

LABEL maintainer="ZupBootcamp"
LABEL version="1.0"

ENV DB_NAME="postgres"
ENV DB_HOSTNAME="localhost"
ENV DB_PORT="5432"
ENV DB_USERNAME="bootcamp"
ENV DB_PWD="bootcamp"

EXPOSE 8080

COPY maven/*.jar picuacdp-api.jar

ENTRYPOINT [ "java" , "-Xms128m",  "-Xmx400m", "-jar", "picuacdp-api.jar", "--jdbc.url=jdbc:postgresql://${DB_HOSTNAME}:${DB_PORT}/${DB_NAME}", "--jdbc.password=${DB_PWD}", "--jdbc.username=${DB_USERNAME}"]