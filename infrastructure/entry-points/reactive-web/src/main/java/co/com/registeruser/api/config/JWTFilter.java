package co.com.registeruser.api.config;


import co.com.registeruser.model.util.JwtGateway;
import co.com.registeruser.security.JWTUtil;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter implements WebFilter {

    private final JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (auth == null || !auth.startsWith("Bearer "))
            return chain.filter(exchange);

        String token = auth.substring(7);

        return Mono.just(token)
                .flatMap(this::validateAndCreateAuthentication)
                .doOnError(e -> log.warn("Token validation failed: {}", e.getMessage()))
                .flatMap(authentication -> {
                    SecurityContext securityContext = new SecurityContextImpl(authentication);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                });
    }

    private Mono<Authentication> validateAndCreateAuthentication(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = jwtUtil.validateTokenAndGetClaims(token);

            if (claims == null)
                throw new ConflictException("Token no valido");

            String role = (String) claims.get("role");
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        });
    }
}
