# java image
FROM openjdk:17-alpine

# working directory in container
WORKDIR /app

# copy from host to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# run this inside the image
RUN ./mvnw dependency:go-offline
COPY src ./src

# run this inside container
CMD ["./mvnw", "spring-boot:run"]