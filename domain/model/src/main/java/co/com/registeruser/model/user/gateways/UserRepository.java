package co.com.registeruser.model.user.gateways;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> createUser(User user);
    Mono<Boolean> existUserByEmail(String email);
    Mono<User> existUserByEmailAndPassword(AuthRequest request);
    Mono<String> getRolUserByEmail(String email);
}
