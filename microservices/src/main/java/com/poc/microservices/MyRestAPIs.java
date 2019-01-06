package com.poc.microservices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("${spring.application.name}")
@RibbonClient("${spring.application.name}")
public interface MyRestAPIs {

	@RequestMapping("/greet")
	String getGreeting();
	@RequestMapping("/time")
	public String getTime();
}
