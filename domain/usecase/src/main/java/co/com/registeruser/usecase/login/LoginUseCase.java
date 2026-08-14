package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.jwtUtil.JwtGateway;
import co.com.registeruser.model.jwtUtil.LoggerGateway;
import co.com.registeruser.model.jwtUtil.PasswordEncrypter;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.registeruser.model.constants.Constants.ROLE_NOT_EXISTS;
import static co.com.registeruser.model.constants.Constants.USER_NOT_FOUND;
import static co.com.registeruser.model.constants.ResponseCodesError.INVALID_ACCESS;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserUseCase userUseCase;
    private final JwtGateway jwtGateway;
    private final LoggerGateway log;
    private final UserRepository userRepository;
    private final PasswordEncrypter passwordEncrypter;

    public Mono<AuthRequest> login(AuthRequest login) {
        log.info("iniciando validación de credenciales");
        return userRepository.getUserByEmail(login.getEmail())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario no encontrado para el email: " + login.getEmail());
                    return Mono.error(new ConflictException(USER_NOT_FOUND));
                }))
                .flatMap(user -> passwordEncrypter.matches(login.getPassword(), user.getPassword())
                        .flatMap(match -> {
                            if (!match) {
                                log.warn("Credenciales incorrectas para el usuario con email: {}", login.getEmail());
                                return Mono.error(new ConflictException(INVALID_ACCESS));
                            }

                            return userUseCase.getRolUserByEmail(user.getEmail())
                                    .switchIfEmpty(Mono.error(new ConflictException(ROLE_NOT_EXISTS)))
                                    .map(role -> {
                                        String token = jwtGateway.generateToken(user, role);
                                        login.setToken(token);
                                        log.info("Token generado exitosamente para el usuario: {}", login.getEmail());
                                        return login;
                                    });
                        })

                );
    }
}
