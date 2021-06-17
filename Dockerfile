FROM openjdk:15-slim as builder
WORKDIR employee
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} employee.jar
RUN java -Djarmode=layertools -jar employee.jar extract

FROM adoptopenjdk:13-jre-hotspot
WORKDIR employee

COPY --from=builder employee/dependencies/ ./
COPY --from=builder employee/spring-boot-loader/ ./
COPY --from=builder employee/snapshot-dependencies/ ./
COPY --from=builder employee/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

