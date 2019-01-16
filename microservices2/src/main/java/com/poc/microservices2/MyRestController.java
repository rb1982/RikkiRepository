package com.poc.microservices2;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MyRestController {
	
	@Autowired
	private Environment env;
	@Value ("${test.value}")
	private String value;
	
	@RequestMapping("/random")
	public String random() {
		long sleepTime = 10 * 1000;
		System.out.println("Processing for " + sleepTime + " millis " + LocalDateTime.now());
		try {
			Thread.sleep(sleepTime);
			System.out.println("Processing done at " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Test value is " + value + " & Random number generated is " + Math.random() + " on PORT " + env.getProperty("local.server.port");
	}

}
