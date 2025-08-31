package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.authResponse.AuthResponse;
import co.com.registeruser.model.util.PasswordEncrypter;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserUseCase userUseCase;
    private final java.util.function.BiFunction<String, String, String> tokenGenerator;
    private final long expirationSeconds;

    public Mono<AuthResponse> login(AuthRequest login) {
        return userUseCase.validateUser(login)
                .flatMap(status -> {
                    switch (status) {
                        case USER_NOT_FOUND:
                            return Mono.error(new ConflictException("El correo no existe"));
                        case WRONG_PASSWORD:
                            return Mono.error(new ConflictException("La contraseña es incorrecta"));
                        case SUCCESS:
                            return userUseCase.getRolUserByEmail(login.getEmail())
                                    .map(rol -> {
                                        String token = tokenGenerator.apply(login.getEmail(), rol);
                                        return new AuthResponse(login.getEmail(), token, expirationSeconds);
                                    });
                        default:
                            return Mono.error(new ConflictException("Error desconocido"));
                    }
                });
    }
}
