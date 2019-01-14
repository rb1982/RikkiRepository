FROM openjdk:11
COPY ./target/servicediscovery-1.0.0.jar /usr/src/eureka/
WORKDIR /usr/src/eureka
ENTRYPOINT exec java $JAVA_OPTS -jar servicediscovery-1.0.0.jar
