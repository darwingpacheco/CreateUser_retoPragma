package co.com.registeruser.model.user.gateways;

import co.com.registeruser.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> createUser(User user);
    Mono<User> findByEmail(String email);
    Mono<Boolean> existUserByEmail(String email);
    Mono<User> getUserByEmail(String email);
    Mono<String> getRolUserByEmail(String email);
    Mono<User> getAllUser(String email);
}
