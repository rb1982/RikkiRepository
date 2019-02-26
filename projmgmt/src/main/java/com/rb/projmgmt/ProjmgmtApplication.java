package com.rb.projmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rb.projmgmt.pojo.Properties;

@SpringBootApplication
public class ProjmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjmgmtApplication.class, args);
	}

@Bean
public ProjectRule maTraxRule() {
	
	return new GenericRules(props());
}

@Bean Properties props() {
	
	return new Properties();
}

}
