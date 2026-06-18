# 1. Build
FROM maven:3.9.6-amazoncorretto-21-debian  AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline

# 2. Build app
RUN mvn clean package -DskipTests

# 3. Run app
FROM ubuntu/jdk:21-24.04_stable
WORKDIR /libraryApp
EXPOSE 8080
COPY --from=builder app/target/LibraryApp*.jar LibraryApp.jar
ENTRYPOINT ["java","-jar","LibraryApp.jar","--spring.profiles.active=dev"]