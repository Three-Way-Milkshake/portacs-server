FROM openjdk:15-alpine

ARG version=1.0.0

COPY build/libs/portacs-${version}-SNAPSHOT.jar /portacs-server.jar
EXPOSE 1723

CMD [ "java", "-jar", "/portacs-server.jar" ]
# CMD [ "sleep", "120" ]