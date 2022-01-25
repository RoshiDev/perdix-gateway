package com.dvara.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dvara.gateway.filter.ConvertPostToGetGatewayFilter;
import com.dvara.gateway.filter.AuthorizationHeaderGatewayFilterFactory;

@Configuration
public class RoutesConfig {
	
	@Bean
	   public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthorizationHeaderGatewayFilterFactory authorizationHeaderGatewayFilterFactory,ConvertPostToGetGatewayFilter convertPostToGetGatewayFilter) {

	       return rlb
	               .routes()
	               .route(p -> p
	                   .path("/rajesh-app/jwt/**")
	                   .filters(f -> f.removeRequestHeader("Cookie")
	                           .rewritePath("/rajesh-app/(?<segment>.*)", "/$\\{segment}")
	                           .filters(authorizationHeaderGatewayFilterFactory.apply(new AuthorizationHeaderGatewayFilterFactory.Config()),
	                        		   convertPostToGetGatewayFilter.apply(new ConvertPostToGetGatewayFilter.Config())
	                        		   ))
	                .uri("lb://rajesh-app")
	            )
	            .build();
	     }

	
}
