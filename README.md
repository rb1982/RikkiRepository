PROJECTS DESCRIPTION
--------------------

*** START GROUP 1: Demonstration of Microservice Architecture showing API Gateway, Load Balancing (Ribbon), Resilient (Hystrix), Service Discovery (Eureka or Consul), Spring Cloud Config, Spring Bus integration using RabbitMQ for Auto refreshing config properties for all subscribed services when hit endpoint "/manage/bus-refresh" for any one microservice ***
1. apiGWay-zuul: Contains API Gateway (Zuul)
2. microservices: Sample Micro services that calls another microservice in project "microservices2". Also contains client side Load Balancing (Ribbon) and Fault Tolerant/Resilient capabilities (Hystrix)
3. microservices2: Sample Micro service
4. servicediscovery: Contains Service Discovery - Eureka
5. configserver: Contains Spring cloud config server for externalization application configuration properties

*** END GROUP 1 ***

RUNNING THE APP
---------------

Steps: 
  1. Docker must be installed and have working knowledge
  2. Maven installed (3.6.0 or later)
  3. JDK 11
  4. Clone repository to your local directory (say REPO_HOME)
  5. CD to REPO_HOME
  6. Execute: mvn clean package
  7. Ensure docker is UP & Running

<p>OPTION EUREKA: If you wish to use EUREKA for service discovery
  8. Execute: docker-compose up --build
<p>OPTION CONSUL: If you wish to use CONSUL for service discovery
  8. Download CONSUL and RUN in dev mode using following command
     consul agent -dev -client=<IP_OF_YOUR_MACHINE>
  9. Edit docker-compose-consul.yml to set <IP_OF_YOUR_MACHINE>
  10. Execute: docker-compose -f docker-compose-consul.yml up --build
  
TESTING
-------
EUREKA URL = http://localhost:8761
<p>Accessing services throught ZUUL API GATEWAY
  1. http://localhost/microservices-1/greet
  2. http://localhost/microservices-1/time
  3. http://localhost/microservices-1/getRandom (Calls anoter service i.e. microservices-2)
  4. http://localhost/microservices-2/random (Directly calls the microservices-2)

<p>After making any configuration change and committing it to git, the configuration can be refreshed for all microservices by sending refresh request to bus (POST)
  <p>METHOD: POST
  <p>URL: http://localhost/microservices-1/manage/bus-refresh
      OR http://localhost/microservices-2/manage/bus-refresh
  <p>REQUEST BODY: empty
  <p>HTTP RESPONSE CODE: 204
