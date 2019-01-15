FROM openjdk:11
COPY ./target/configserver-1.0.0.jar /usr/src/configserver/
WORKDIR /usr/src/configserver
ENTRYPOINT exec java $JAVA_OPTS -jar configserver-1.0.0.jar
