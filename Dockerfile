# STAGE 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build

#Working Directory for the API
WORKDIR /app/eventbooking

# Copy the necessary project files
COPY ./pom.xml .
COPY ./src ./src
#COPY ./event-booking.iml ./event-booking.iml
COPY ./mvnw ./mvnw

# Build the application
RUN mvn clean install
#RUN ./mvnw clean install

# STAGE 2: Create the Docker image
FROM openjdk:17-jdk-slim

WORKDIR /app/eventbooking
# Copy the built JAR file from the previous stage
COPY --from=build /app/eventbooking/target/event-booking.jar ./event-booking.jar

# Expose the desired port(s)
EXPOSE 8086

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "event-booking.jar"]
