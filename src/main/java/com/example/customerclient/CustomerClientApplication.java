package com.example.customerclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CustomerClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerClientApplication.class, args);
	}
}

@Configuration
class CustomerClientConfiguration {

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	CustomerClient client(RestTemplate restTemplate,
						  @Value("${customer-service.host:http://localhost:8080}") String uri){
		return new CustomerClient(restTemplate, uri);
	}
}
