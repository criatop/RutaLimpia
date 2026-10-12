package com.rutalimpia.gateway.filter;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.rutalimpia.gateway.dto.TokenInfo;

import reactor.core.publisher.Mono;

/**
 * Authorizer del API Gateway: valida el JWT contra ms-auth antes de enrutar.
 * Las rutas publicas (registro/login/validacion) no requieren token.
 * Al validar, propaga X-Usuario-Id y X-Rol a los microservicios.
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private final WebClient webClient;
    private final boolean enabled;
    private final List<String> publicPaths;

    public JwtAuthFilter(WebClient.Builder loadBalancedWebClientBuilder,
                         @Value("${rutalimpia.security.enabled:true}") boolean enabled,
                         @Value("${rutalimpia.security.public-paths:/auth/register,/auth/login,/auth/validate}") String publicPaths) {
        this.webClient = loadBalancedWebClientBuilder.clone().baseUrl("http://ms-auth").build();
        this.enabled = enabled;
        this.publicPaths = Arrays.stream(publicPaths.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!enabled) {
            return chain.filter(exchange);
        }
        String path = exchange.getRequest().getPath().value();
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod()) || esPublica(path)) {
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return noAutorizado(exchange);
        }

        return webClient.get()
                .uri("/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .bodyToMono(TokenInfo.class)
                .flatMap(info -> info != null && info.valido()
                        ? chain.filter(conIdentidad(exchange, info))
                        : noAutorizado(exchange))
                .onErrorResume(e -> {
                    log.warn("[AUTH] Token invalido para {}: {}", path, e.getMessage());
                    return noAutorizado(exchange);
                });
    }

    private ServerWebExchange conIdentidad(ServerWebExchange exchange, TokenInfo info) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-Usuario-Id", info.usuarioId() == null ? "" : String.valueOf(info.usuarioId()))
                .header("X-Rol", info.rol() == null ? "" : info.rol())
                .build();
        return exchange.mutate().request(request).build();
    }

    private boolean esPublica(String path) {
        return publicPaths.stream().anyMatch(p -> MATCHER.match(p, path));
    }

    private Mono<Void> noAutorizado(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
