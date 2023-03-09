FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} biblioteca.jar
ENTRYPOINT ["java", "-jar", "/biblioteca.jar"]
