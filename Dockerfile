FROM maven:3.5-jdk-8 AS building_of_project
COPY src /projects/EmailSender/src
COPY pom.xml /projects/EmailSender/pom.xml
RUN mvn -f /projects/EmailSender/pom.xml clean package spring-boot:repackage

FROM openjdk:8-jre
COPY --from=building_of_project /projects/EmailSender/target /projects/target
EXPOSE 8091
WORKDIR /projects/EmailSender/
CMD ["java", "-jar", "/projects/target/EmailSender-0.0.jar"]
