package com.poc.microservices2;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {
	
	@Autowired
	private Environment env;
	
	
	@RequestMapping("/random")
	public String random() {
		long sleepTime = 10 * 1000;
		System.out.println("Processing for " + sleepTime + " millis " + LocalDateTime.now());
		try {
			Thread.sleep(sleepTime);
			System.out.println("Processing done at " + LocalDateTime.now());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Random number generated is " + Math.random() + " on PORT " + env.getProperty("local.server.port");
	}

}
