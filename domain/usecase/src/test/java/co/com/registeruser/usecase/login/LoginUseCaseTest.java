package co.com.registeruser.usecase.login;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.jwtUtil.JwtGateway;
import co.com.registeruser.model.jwtUtil.LoggerGateway;
import co.com.registeruser.model.jwtUtil.PasswordEncrypter;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
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

import static co.com.registeruser.model.constants.Constants.ROLE_NOT_EXISTS;
import static co.com.registeruser.model.constants.Constants.USER_NOT_FOUND;
import static co.com.registeruser.model.constants.ResponseCodesError.INVALID_ACCESS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private JwtGateway jwtGateway;

    @Mock
    private LoggerGateway log;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncrypter passwordEncrypter;

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
                "123456", // contraseña en BD
                "1234567890",
                "3224291874",
                LocalDate.of(2003, 1, 10),
                "Carrera 1 # 15 - 43",
                1,
                BigDecimal.valueOf(5000000)
        );
    }

    @Test
    void login_userNotFound() {
        when(userRepository.getUserByEmail(authRequest.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals(USER_NOT_FOUND))
                .verify();
    }

    @Test
    void login_wrongPassword() {
        when(userRepository.getUserByEmail(authRequest.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncrypter.matches(authRequest.getPassword(), user.getPassword()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals(INVALID_ACCESS))
                .verify();
    }

    @Test
    void login_roleNotFound() {
        when(userRepository.getUserByEmail(authRequest.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncrypter.matches(authRequest.getPassword(), user.getPassword()))
                .thenReturn(Mono.just(true));
        when(userUseCase.getRolUserByEmail(authRequest.getEmail()))
                .thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals(ROLE_NOT_EXISTS))
                .verify();
    }

    @Test
    void login_success() {
        when(userRepository.getUserByEmail(authRequest.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncrypter.matches(authRequest.getPassword(), user.getPassword()))
                .thenReturn(Mono.just(true));
        when(userUseCase.getRolUserByEmail(authRequest.getEmail()))
                .thenReturn(Mono.just("ADMIN"));
        when(jwtGateway.generateToken(user, "ADMIN"))
                .thenReturn("TOKEN_darwin@gmail.com_ADMIN");

        StepVerifier.create(loginUseCase.login(authRequest))
                .expectNextMatches(response ->
                        response.getEmail().equals(authRequest.getEmail()) &&
                                response.getToken().equals("TOKEN_darwin@gmail.com_ADMIN"))
                .verifyComplete();
    }
}
