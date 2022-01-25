package com.dvara.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@Component
public class AuthorizationHeaderGatewayFilterFactory  implements GatewayFilterFactory<AuthorizationHeaderGatewayFilterFactory.Config>{

	public static final String BASE_MSG = "baseMessage";
    public static final String PRE_LOGGER = "preLogger";
	public static final String POST_LOGGER = "postLogger";

	public AuthorizationHeaderGatewayFilterFactory() {
		//super(Config.class);
	}
	
	@Override
	public Class<Config> getConfigClass() {
		return Config.class;
	}

	
	
	@Override
	public Config newConfig() {
		Config c = new Config();
		return c;
	}

	public static class Config {
		
	}
	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		System.out.println("apply () ");
		return (exchange,chain) ->{
			System.out.println("exchange jsm jsm()");
			ServerHttpRequest serverReuest = exchange.getRequest();
			HttpHeaders httpHeaders= serverReuest.getHeaders();
			if(!httpHeaders.containsKey(HttpHeaders.AUTHORIZATION)){
				return OnError(exchange,"No Auhorization Header",HttpStatus.UNAUTHORIZED); 
			}
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				System.out.println("exchange jsm jsm completed()");
			}));
	};
	}
	private Mono<Void> OnError(ServerWebExchange exchange, String errorDescription, HttpStatus httpStatus) {
		// TODO Auto-generated method stub
		ServerHttpResponse serverHttpResposne=exchange.getResponse();
		serverHttpResposne.setStatusCode(httpStatus);
		return serverHttpResposne.setComplete();
	}
	
	private boolean isTokenValid(String token){
		return true;
	}
	
}