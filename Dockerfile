FROM openjdk:11
VOLUME /tmp
COPY target/my-spring-app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
