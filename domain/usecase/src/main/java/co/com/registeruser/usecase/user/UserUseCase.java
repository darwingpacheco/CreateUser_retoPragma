package co.com.registeruser.usecase.user;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.statusCode.LoginStatus;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.model.util.PasswordEncrypter;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncrypter passwordEncrypter;

    public Mono<User> createUser(User user) {
        return userRepository.existUserByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("El email ya está registrado"));
                    }
                    return rolRepository.findRoleById(user.getIdRol())
                            .switchIfEmpty(Mono.error(new ConflictException("El rol no existe")))
                            .flatMap(rol -> {
                                user.setPassword(passwordEncrypter.encode(user.getPassword()));
                                return userRepository.createUser(user);
                            });
                });
    }

    public Mono<Boolean> existsUserByEmail(String email) {
        return userRepository.existUserByEmail(email);
    }

    public Mono<String> getRolUserByEmail(String email) {
        return userRepository.getRolUserByEmail(email);
    }

    public Mono<LoginStatus> validateUser(AuthRequest request) {
        return userRepository.getUserByEmail(request.getEmail())
                .map(dbUser -> passwordEncrypter.matches(request.getPassword(), dbUser.getPassword())
                        ? LoginStatus.SUCCESS
                        : LoginStatus.WRONG_PASSWORD)
                .defaultIfEmpty(LoginStatus.USER_NOT_FOUND);
    }
}
