FROM openjdk:21-jdk
COPY . .
RUN ./mvnw clean install -DskipTests
ENTRYPOINT ["java", "-jar", "target/producer_service-0.0.1-SNAPSHOT.jar"]
