package co.com.registeruser.model.jwtUtil;

import reactor.core.publisher.Mono;

public interface PasswordEncrypter {
    String encode(String rawPassword);

    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
