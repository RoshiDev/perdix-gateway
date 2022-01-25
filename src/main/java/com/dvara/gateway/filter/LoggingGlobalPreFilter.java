package com.dvara.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalPreFilter implements GlobalFilter {

	// final Logger logger = LoggerFactory.getLogger(LoggingGlobalPreFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("Global Pre Filter executed");
		ServerHttpRequest serverReuest = exchange.getRequest();
		HttpHeaders httpHeaders = serverReuest.getHeaders();
		if (!httpHeaders.containsKey("Content-Type")) {
			return OnError(exchange, "No Content-Type Header", HttpStatus.UNAUTHORIZED);
		}
		System.out.println("exchange globalfilter compeleted");
		return chain.filter(exchange);
	}

	private Mono<Void> OnError(ServerWebExchange exchange, String errorDescription, HttpStatus httpStatus) {
		// TODO Auto-generated method stub
		ServerHttpResponse serverHttpResposne = exchange.getResponse();
		serverHttpResposne.setStatusCode(httpStatus);
		return serverHttpResposne.setComplete();
	}
}