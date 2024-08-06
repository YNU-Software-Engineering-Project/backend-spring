FROM --platform=linux/arm64 gradle:8.3-jdk17-jammy AS build
WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

COPY . /app

RUN gradle clean build --no-daemon

FROM --platform=linux/arm64/v8 eclipse-temurin:17.0.10_7-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/agile.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/agile.jar"]