FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml pom.xml
COPY src/ src/

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
