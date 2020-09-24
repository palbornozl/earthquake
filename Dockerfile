FROM adoptopenjdk:11-jre-openj9
COPY . .
COPY ./target/earthquake-1.0.0.jar /usr/app/
WORKDIR /usr/app
RUN java -version
CMD ["java", "-XX:+UseG1GC", "-jar", "earthquake-1.0.0.jar", "10", "-Dspring.profiles.active=default"]
