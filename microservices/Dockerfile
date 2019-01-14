FROM openjdk:11
COPY ./target/microservices-1.0.0.jar /usr/src/microservices/
WORKDIR /usr/src/microservices
ENTRYPOINT exec java $JAVA_OPTS -jar microservices-1.0.0.jar
