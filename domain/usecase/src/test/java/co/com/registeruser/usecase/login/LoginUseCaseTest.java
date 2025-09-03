package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.statusCode.LoginStatus;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.util.JwtGateway;
import co.com.registeruser.model.util.LoggerGateway;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.registeruser.model.user.util.Constants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private JwtGateway jwtGateway;

    @Mock
    private LoggerGateway log;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("darwin@gmail.com", "123456", null);

        user = new User(
                1L,
                "Darwin",
                "Gomez",
                "darwin@gmail.com",
                "1234567890",
                "1234567890",
                "3224291874",
                LocalDate.of(2003, 1, 10),
                "Carrera 1 # 15 - 43",
                1,
                BigDecimal.valueOf(5000000)
        );
    }
//
//    @Test
//    void login_userNotFound_fromValidate() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.USER_NOT_FOUND));
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectErrorMatches(e -> e instanceof ConflictException &&
//                        e.getMessage().equals(EMAIL_NOT_FOUND))
//                .verify();
//    }
//
//    @Test
//    void login_wrongPassword() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.WRONG_PASSWORD));
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectErrorMatches(e -> e instanceof ConflictException &&
//                        e.getMessage().equals(PASSWORD_INVALID))
//                .verify();
//    }
//
//    @Test
//    void login_success() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.SUCCESS));
//        when(userUseCase.getRolUserByEmail(authRequest.getEmail())).thenReturn(Mono.just("ADMIN"));
//        when(jwtGateway.generateToken(user, "ADMIN"))
//                .thenReturn("TOKEN_" + authRequest.getEmail() + "_ADMIN");
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectNextMatches(response ->
//                        response.getEmail().equals(authRequest.getEmail()) &&
//                                response.getToken().equals("TOKEN_" + authRequest.getEmail() + "_ADMIN"))
//                .verifyComplete();
//    }
//
//    @Test
//    void login_roleNotFound() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.SUCCESS));
//        when(userUseCase.getRolUserByEmail(authRequest.getEmail())).thenReturn(Mono.empty());
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectErrorMatches(e -> e instanceof ConflictException &&
//                        e.getMessage().equals(ROLE_NOT_EXISTS))
//                .verify();
//    }
//
//    @Test
//    void login_validateUserReturnsEmpty() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.empty());
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectErrorMatches(e -> e instanceof ConflictException &&
//                        e.getMessage().equals(USER_NOT_FOUND))
//                .verify();
//    }
//
//    @Test
//    void login_defaultError() {
//        when(userUseCase.validateUser(authRequest)).thenReturn(Mono.just(LoginStatus.valueOf("UNKNOWN")));
//
//        StepVerifier.create(loginUseCase.login(authRequest))
//                .expectErrorMatches(e -> e instanceof ConflictException &&
//                        e.getMessage().equals("Error desconocido"))
//                .verify();
//    }
}
