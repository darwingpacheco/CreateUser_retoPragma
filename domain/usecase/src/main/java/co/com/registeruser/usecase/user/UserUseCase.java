package co.com.registeruser.usecase.user;

import co.com.registeruser.model.jwtUtil.LoggerGateway;
import co.com.registeruser.model.jwtUtil.PasswordEncrypter;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.registeruser.model.constants.Constants.VALID_EMAIL_DUPLICATE;
import static co.com.registeruser.model.constants.Constants.VALID_ROLE_EXISTS;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncrypter passwordEncrypter;
    private final LoggerGateway log;

    public Mono<User> createUser(User user) {
        return userRepository.existUserByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        log.error("El email {} ya está registrado", user.getEmail());
                        return Mono.error(new ConflictException(VALID_EMAIL_DUPLICATE));
                    }
                    return rolRepository.findRoleById(user.getIdRol());
                })
                .flatMap(rolExist -> {
                    if (!rolExist) {
                        log.error("El rol con id {} no existe", user.getIdRol());
                        return Mono.error(new ConflictException(VALID_ROLE_EXISTS));
                    }

                    user.setPassword(passwordEncrypter.encode(user.getPassword()));

                    log.info("Usuario creado: ", user.getEmail());
                    return userRepository.createUser(user);
                });
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<String> getRolUserByEmail(String email) {
        return userRepository.getRolUserByEmail(email);
    }

    public Mono<User> getUserToReport(String email) {
        return userRepository.getAllUser(email);
    }
}
