FROM amazoncorretto:17
COPY target/*.jar Vaadin-Course.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]