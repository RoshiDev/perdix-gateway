package com.dvara.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
//@EnableEurekaServer
@ComponentScan({"com.dvara.gateway.config","com.dvara.gateway.filter"})
@SpringBootApplication //(exclude = {SecurityAutoConfiguration.class,ReactiveUserDetailsServiceAutoConfiguration.class })
@EnableDiscoveryClient
public class PerdixGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerdixGatewayApplication.class, args);
	}

}
