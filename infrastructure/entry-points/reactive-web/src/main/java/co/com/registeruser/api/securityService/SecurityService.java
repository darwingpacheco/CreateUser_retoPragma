package co.com.registeruser.api.securityService;

import co.com.registeruser.api.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final JWTUtil jwtUtil;

    public Mono<String> extractRole(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.error(new RuntimeException("Token inválido o ausente"));
        }
        String token = authHeader.replace("Bearer ", "");
        return Mono.just(jwtUtil.getRol(token));
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                String cleanToken = token.startsWith("Bearer ")
                        ? token.substring(7)
                        : token;

                return jwtUtil.isValid(cleanToken);
            } catch (Exception e) {
                return false;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> extractEmail(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new RuntimeException("Token inválido o ausente"));
        }
        String token = authHeader.replace("Bearer ", "");
        return Mono.fromCallable(() -> jwtUtil.getEmail(token))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
