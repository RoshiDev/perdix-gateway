package com.dvara.gateway.filter;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import com.dvara.gateway.filter.ConvertPostToGetGatewayFilter.Config;
import org.bouncycastle.util.Strings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class ConvertPostToGetGatewayFilter extends AbstractGatewayFilterFactory<ConvertPostToGetGatewayFilter.Config> {

	private static final String REQUEST_BODY_OBJECT = "requestBodyObject";

	public ConvertPostToGetGatewayFilter(){
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		System.out.println("convertPostToGetGatewayFilter apply" );
		return (exchange, chain) -> {
			System.out.println("convertPostToGetGatewayFilter exchange, chain");
			if (exchange.getRequest().getHeaders().getContentType() == null) {
				return chain.filter(exchange);
			} else {
				return DataBufferUtils.join(exchange.getRequest().getBody())
						.flatMap(dataBuffer -> {

							DataBufferUtils.retain(dataBuffer);
							Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

							if(exchange.getRequest().getMethod().equals(HttpMethod.GET)) {
								return OnError(exchange,"Method Not supported",HttpStatus.METHOD_NOT_ALLOWED); 
							}
							ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {


								@Override
								public String getMethodValue(){
									return HttpMethod.GET.name();
								}


								@Override
								public URI getURI(){
									return UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
											.queryParams(convertJsonToQueryParamMap(toRaw(cachedFlux))).build().toUri();
								}


								@Override
								public Flux<DataBuffer> getBody() {
									return Flux.empty();
								}

							};

							return chain.filter(exchange.mutate().request(mutatedRequest).build());
						});
			}

		};
	}


	private static String toRaw(Flux<DataBuffer> body) {
		AtomicReference<String> rawRef = new AtomicReference<>();
		body.subscribe(buffer -> {
			byte[] bytes = new byte[buffer.readableByteCount()];
			buffer.read(bytes);
			DataBufferUtils.release(buffer);
			rawRef.set(Strings.fromUTF8ByteArray(bytes));
		});
		return rawRef.get();
	}

	public static class Config {
		public Config() {
		}
	}
	
	public static MultiValueMap<String, String> convertJsonToQueryParamMap( String json ) {

		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(json);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();

		while ( fields.hasNext() ){
			Map.Entry<String, JsonNode> entry = fields.next();
			multiValueMap.add(entry.getKey(), entry.getValue().asText());
		}

		return multiValueMap;
	}
	
	private Mono<Void> OnError(ServerWebExchange exchange, String errorDescription, HttpStatus httpStatus) {
		// TODO Auto-generated method stub
		ServerHttpResponse serverHttpResposne=exchange.getResponse();
		serverHttpResposne.setStatusCode(httpStatus);
		return serverHttpResposne.setComplete();
	}

}