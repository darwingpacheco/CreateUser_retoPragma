package co.com.registeruser.usecase.user;

import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.createUser(user);
    }
}
