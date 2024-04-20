# Stage 1: Build the application using Maven and JDK 21
FROM eclipse-temurin:21-jdk as build
WORKDIR /workspace/app

# Copy the Maven configuration files first and download dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copy the project source code and build the executable jar
COPY src src
RUN ./mvnw package -DskipTests

# Stage 2: Setup the runtime container with JRE 21
FROM eclipse-temurin:21-jre
VOLUME /tmp

# Copy the jar from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
