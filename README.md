PROJECTS DESCRIPTION
--------------------

*** START GROUP 1: Demonstration of Microservice Architecture showing API Gateway, Load Balancing (Ribbon), Resilient (Hystrix), Service Discovery (Eureka OR Consul), Spring Cloud Config, Spring Bus integration using RabbitMQ for Auto refreshing config properties for all subscribed services when hit endpoint "/manage/bus-refresh" for any one microservice ***
1. apiGWay-zuul: Contains API Gateway (Zuul)
2. microservices: Sample Micro services that calls another microservice in project "microservices2". Also contains client side Load Balancing (Ribbon) and Fault Tolerant/Resilient capabilities (Hystrix)
3. microservices2: Sample Micro service
4. servicediscovery: Contains Service Discovery - Eureka
5. configserver: Contains Spring cloud config server for externalization application configuration properties

*** END GROUP 1 ***

RUNNING THE APP
---------------

Steps: 
  1. Docker must be installed 
  2. Maven installed (3.6.0 or later)
  3. JDK 11 installed
  4. Clone repository to your local directory (say REPO_HOME)
  5. CD to REPO_HOME
  6. Execute: mvn clean package
  7. Ensure docker is UP & Running
  8. If you wish to use EUREKA as service discovery execute following else if you wish to use CONSUL go to step 10.

     Execute: docker-compose up --build
     
  9. You are done with EUREKA setup - ignore below steps i.e. 10 onwards.
  10. Download CONSUL and RUN in dev mode using following command
  
     consul agent -dev -client=<IP_OF_YOUR_MACHINE>
     
  11. Edit docker-compose-consul.yml to set <IP_OF_YOUR_MACHINE>
  10. Execute: docker-compose -f docker-compose-consul.yml up --build
  
TESTING
-------
EUREKA URL = http://localhost:8761

Accessing services through ZUUL API GATEWAY
  1. http://localhost/microservices-1/greet
  2. http://localhost/microservices-1/time
  3. http://localhost/microservices-1/getRandom (Calls another service i.e. microservices-2)
  4. http://localhost/microservices-2/random (Directly calls the microservices-2)

After making any configuration change and committing to Git, the configuration can be refreshed for all microservices by sending refresh request to bus (POST)

  1. METHOD: POST
  2. URL: http://localhost/microservices-1/manage/bus-refresh
     OR http://localhost/microservices-2/manage/bus-refresh
  3. REQUEST BODY: empty
  4. HTTP RESPONSE CODE: 204
