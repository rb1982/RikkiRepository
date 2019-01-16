PROJECTS DESCRIPTION

--------------------

*** START GROUP 1: Demonstration of Microservice Architecture showing API Gateway, Load Balancing (Ribbon), Resilient (Hystrix), Service Discovery (Eureka), Spring Cloud Config, Spring Bus integration using RabbitMQ for Auto refreshing config properties for all subscribed services when hit endpoint "/manage/bus-refresh" for any one microservice ***
1. apiGWay-zuul: Contains API Gateway (Zuul)
2. microservices: Sample Micro services that calls another microservice in project "microservices2". Also contains client side Load Balancing (Ribbon) and Fault Tolerant/Resilient capabilities (Hystrix)
3. microservices2: Sample Micro service
4. servicediscovery: Contains Service Discovery - Eureka
5. configserver: Contains Spring cloud config server for externalization application configuration properties

*** END GROUP 1 ***
