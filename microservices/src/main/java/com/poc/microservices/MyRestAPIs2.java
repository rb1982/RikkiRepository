package com.poc.microservices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("microservices-2")
@RibbonClient("microservices-2")
public interface MyRestAPIs2 {

	@RequestMapping("/random")
	public String random(); 
}
