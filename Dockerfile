FROM ubuntu:latest
LABEL authors="HP"

ENTRYPOINT ["top", "-b"]

#FROM maven:3.8.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#FROM openjdk:17.0.2-jdk-slim
#COPY --from=build /target/flatmate-0.0.1-SNAPSHOT.war flatmate.war
#EXPOSE 8080
#ENTRYPOINT[]

FROM tomcat:10.1.24-jdk21-temurin-jammy

WORKDIR /usr/local/tomcat

COPY /target/flatmate-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/flatmate.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

