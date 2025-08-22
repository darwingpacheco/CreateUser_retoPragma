package co.com.registeruser.model.user.gateways;

import co.com.registeruser.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> createUser(User user);
    Mono<User> findByEmail(String email);
}
