package com.adidas.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PublicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicApplication.class, args);
	}

}
