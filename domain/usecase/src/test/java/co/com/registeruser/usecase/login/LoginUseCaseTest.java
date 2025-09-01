package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.statusCode.LoginStatus;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserUseCase userUseCase;

    private LoginUseCase loginUseCase;

    private AuthRequest authRequest;
    private final long expiration = 3600;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("darwin@gmail.com", "123456");

        loginUseCase = new LoginUseCase(
                userUseCase,
                (email, rol) -> "TOKEN_" + email + "_" + rol,
                expiration);
    }

    @Test
    void login_userNotFound() {
        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.USER_NOT_FOUND));

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("El correo no existe"))
                .verify();
    }

    @Test
    void login_wrongPassword() {
        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.WRONG_PASSWORD));

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("La contraseña es incorrecta"))
                .verify();
    }

    @Test
    void login_success() {
        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.SUCCESS));
        when(userUseCase.getRolUserByEmail(authRequest.getEmail())).thenReturn(Mono.just("ADMIN"));

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectNextMatches(response ->
                        response.getEmail().equals(authRequest.getEmail()) &&
                                response.getToken().equals("TOKEN_" + authRequest.getEmail() + "_ADMIN") &&
                                response.getExpiresIn() == expiration
                )
                .verifyComplete();
    }

    @Test
    void login_defaultError() {
        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("Error desconocido"))
                .verify();
    }
}