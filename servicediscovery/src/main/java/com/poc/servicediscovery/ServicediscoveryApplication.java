package com.poc.servicediscovery;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServicediscoveryApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ServicediscoveryApplication.class).web(WebApplicationType.SERVLET).run(args);
		//SpringApplication.run(ServicediscoveryApplication.class, args);
		
	}

}

