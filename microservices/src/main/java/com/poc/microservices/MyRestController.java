package com.poc.microservices;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class MyRestController implements MyRestAPIs {
	
	@Autowired
	private Environment env;
	@Autowired
	private MyRestAPIs myApis;
	@Autowired
	private MyRestAPIs2 myApis2;
	
	public String getGreeting() {
		return "Hello World from port " + env.getProperty("local.server.port");
	}
	
	public String getTime() {
		return LocalDateTime.now() + " from port " + env.getProperty("local.server.port");
	}
	@RequestMapping("/greetAndTime")
	public String greetAndTime() {
		String message = myApis.getGreeting() + " ~ " + myApis.getTime();
		return message;
	}
	
	/*
	 * Below shows how to configure timeout either in annotation directly or externalize in properties file through value of "commandKey"
	 * In properties file it will be configured as "hystrix.command.getRandom.execution.isolation.thread.timeoutInMilliseconds=30000"
	 */
	@RequestMapping("/getRandom")
	@HystrixCommand(fallbackMethod = "reliable" /*, commandProperties = {
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="30000")
	}*/, commandKey="getRandom"
	
	)
	public String getRandom() {
		return myApis2.random();
	}
	
	public String reliable() {
		
		return "Random service is down, so here is a fallback message!";
	}

}
