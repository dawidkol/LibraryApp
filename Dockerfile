# 1. Build
FROM --platform=linux/amd64 maven:3.9.6-amazoncorretto-21-debian  AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline

# 2. Run tests
RUN mvn test -Dspring.profiles.active=dev

# 3. Build app
RUN mvn clean package -DskipTests

# 4. Run app
FROM --platform=linux/amd64 openjdk:26-ea-21-jdk-oracle
WORKDIR /libraryApp
EXPOSE 8080
COPY --from=builder app/target/LibraryApp-0.0.1-SNAPSHOT.jar LibraryApp.jar
ENTRYPOINT ["java","-jar","LibraryApp.jar","--spring.profiles.active=dev"]