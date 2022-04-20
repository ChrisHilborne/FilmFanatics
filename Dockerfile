FROM openjdk:11
RUN mkdir ~/jar
WORKDIR ~/jar
COPY /target/film-fanatic-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "film-fanatic-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=dev"]
