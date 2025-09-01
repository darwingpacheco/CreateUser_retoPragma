package co.com.registeruser.api.securityService;

import co.com.registeruser.api.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

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

    public Mono<Void> authorizeRole(ServerRequest request, String requiredRole) {
        return extractRole(request)
                .flatMap(role -> {
                    if (!requiredRole.equalsIgnoreCase(role)) {
                        return Mono.error(new RuntimeException("No tiene permisos"));
                    }
                    return Mono.empty(); // autorización OK
                });
    }
}
