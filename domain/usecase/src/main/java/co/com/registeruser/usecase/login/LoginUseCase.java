package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.authResponse.AuthResponse;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.statusCode.LoginStatus;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.model.util.JwtGateway;
import co.com.registeruser.model.util.LoggerGateway;
import co.com.registeruser.model.util.PasswordEncrypter;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.registeruser.model.user.util.Constants.*;

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
                                return Mono.error(new ConflictException("Credenciales incorrectas"));
                            }

                            return userUseCase.getRolUserByEmail(user.getEmail())
                                    .switchIfEmpty(Mono.error(new ConflictException("Rol no encontrado")))
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
