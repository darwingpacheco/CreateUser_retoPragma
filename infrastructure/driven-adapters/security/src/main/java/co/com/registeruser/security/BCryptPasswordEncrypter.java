package co.com.registeruser.security;

import co.com.registeruser.model.util.PasswordEncrypter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BCryptPasswordEncrypter implements PasswordEncrypter {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
        return Mono.just(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
