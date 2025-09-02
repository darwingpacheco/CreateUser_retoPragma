package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.authResponse.AuthResponse;
import co.com.registeruser.model.statusCode.LoginStatus;
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

    public Mono<AuthRequest> login(AuthRequest login) {
        log.info("iniciando validación de credenciales");
        return userUseCase.validateUser(login)
                .switchIfEmpty(Mono.error(new ConflictException(USER_NOT_FOUND)))
                .flatMap(status -> switch (status) {
                    case USER_NOT_FOUND -> Mono.error(new ConflictException(EMAIL_NOT_FOUND));
                    case WRONG_PASSWORD -> Mono.error(new ConflictException(PASSWORD_INVALID));
                    case SUCCESS -> userUseCase.getRolUserByEmail(login.getEmail())
                            .switchIfEmpty(Mono.error(new ConflictException(ROLE_NOT_EXISTS)))
                            .map(rol -> {
                                String token = jwtGateway.generateToken(login.getEmail(), rol);
                                login.setToken(token);
                                return login;
                            }); default -> Mono.error(new ConflictException("Error desconocido"));
                });
    }
}
