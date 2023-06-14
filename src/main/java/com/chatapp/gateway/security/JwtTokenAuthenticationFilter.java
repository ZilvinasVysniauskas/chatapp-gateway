package com.chatapp.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

    private final TokenValidator tokenValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String jwt = getJwtFromRequest(request);

        if (jwt != null && tokenValidator.validateToken(jwt)) {
            String userId = tokenValidator.getUserIdFromToken(jwt);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());


            request.mutate().header("User-Id", userId).build();
            exchange.mutate().request(request).build();

            return chain.filter(exchange).contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(authentication)
            );
        } else {
            return chain.filter(exchange);
        }
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
