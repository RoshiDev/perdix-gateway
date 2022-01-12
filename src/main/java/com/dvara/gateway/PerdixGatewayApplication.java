package com.dvara.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class PerdixGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerdixGatewayApplication.class, args);
	}

}
