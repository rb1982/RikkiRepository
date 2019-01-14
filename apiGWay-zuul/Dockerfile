FROM openjdk:11
COPY ./target/apiGWay-zuul-1.0.0.jar /usr/src/api-gateway-zuul/
WORKDIR /usr/src/api-gateway-zuul
ENTRYPOINT exec java $JAVA_OPTS -jar apiGWay-zuul-1.0.0.jar
