FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --configuration runtimeClasspath
COPY src ./src
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S modubank && adduser -S modubank -G modubank
COPY --from=build --chown=modubank:modubank /app/build/libs/*.jar app.jar
USER modubank
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
