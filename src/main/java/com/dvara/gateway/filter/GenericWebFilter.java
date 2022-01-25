package com.dvara.gateway.filter;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class GenericWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		System.out.println(" Executing GenericWebFilter ");
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		if (request.getMethod().matches(HttpMethod.OPTIONS.toString())) {
			return chain.filter(exchange);
		}
		URI uri = request.getURI();
		String path = uri.getPath();
		HttpHeaders headers = exchange.getRequest().getHeaders();
		if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
			//return OnError(exchange, "errorDescription", HttpStatus.UNAUTHORIZED);
		}
		// exchange.getRequest().getHeaders().add("first-request",
		// "first-request-header");
		return chain.filter(exchange);
	}

	private Mono<Void> OnError(ServerWebExchange exchange, String errorDescription, HttpStatus httpStatus) {
		ServerHttpResponse serverHttpResposne = exchange.getResponse();
		serverHttpResposne.setStatusCode(httpStatus);
		return serverHttpResposne.setComplete();
	}

}
