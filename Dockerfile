FROM maven:3.6.3-adoptopenjdk-11-openj9 as builder
COPY . .
RUN ls -al
RUN rm -rf ./target
RUN mvn -v
RUN mvn clean verify compile install -DskipTests

FROM adoptopenjdk:11-jre-openj9
COPY --from=builder ./target/earthquake-1.0.0.jar /usr/app/
WORKDIR /usr/app
RUN java -version
CMD ["java", "-XX:+UseG1GC", "-jar", "earthquake-1.0.0.jar", "10", "-Dspring.profiles.active=default"]
