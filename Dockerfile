# Builder
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app

COPY . /app
COPY src/resources/schema /app/schema

# Compile the project
RUN mvn -f /app/pom.xml clean package

#--------------------------------------------------

# Runner
FROM openjdk:21-jdk-slim AS runner

WORKDIR /app

# Move compiled jar file from the builder to the runner
COPY --from=builder /app/target/pushserver-1.0.0-jar-with-dependencies.jar /app/pushserver.jar

# Move schema files to the container
COPY src/resources/schema /app/schema

# Port Argument
EXPOSE 5222
EXPOSE 5223

CMD ["java", "-jar", "pushserver.jar"]