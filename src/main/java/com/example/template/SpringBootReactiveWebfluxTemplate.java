package com.example.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableReactiveFeignClients
@SpringBootApplication
public class SpringBootReactiveWebfluxTemplate {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactiveWebfluxTemplate.class, args);
	}

}
