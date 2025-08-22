package co.com.registeruser.usecase.user;

import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.findByEmail(user.getCorreoElectronico())
                .flatMap(existingUser -> {
                    if (existingUser != null) {
                        return Mono.error(new IllegalArgumentException("El email ya está registrado"));
                    }
                    return userRepository.createUser(user);
                });
    }
}
