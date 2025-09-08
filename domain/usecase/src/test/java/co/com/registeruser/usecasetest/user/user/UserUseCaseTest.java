package co.com.registeruser.usecasetest.user.user;

import co.com.registeruser.model.jwtUtil.LoggerGateway;
import co.com.registeruser.model.jwtUtil.PasswordEncrypter;
import co.com.registeruser.model.rol.gateways.RolRepository;
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

import static co.com.registeruser.model.constants.Constants.VALID_EMAIL_DUPLICATE;
import static co.com.registeruser.model.constants.Constants.VALID_ROLE_EXISTS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncrypter passwordEncrypter;

    @Mock
    private LoggerGateway log;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                1L,
                "Darwin",
                "Gomez",
                "darwin@gmail.com",
                "123456",
                "1234567890",
                "3224291874",
                LocalDate.of(2003, 1, 10),
                "Carrera 1 # 15 - 43",
                1,
                BigDecimal.valueOf(5000000)
        );
    }

    @Test
    void createUser_emailAlreadyExists() {
        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals(VALID_EMAIL_DUPLICATE))
                .verify();
    }

    @Test
    void createUser_roleNotFound() {
        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals(VALID_ROLE_EXISTS))
                .verify();
    }

    @Test
    void createUser_success() {
        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.just(true));
        when(passwordEncrypter.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.createUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.createUser(user))
                .expectNextMatches(u -> u.getEmail().equals("darwin@gmail.com") &&
                        u.getPassword().equals("hashedPassword"))
                .verifyComplete();
    }

    @Test
    void findByEmail_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.findByEmail(user.getEmail()))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getRolUserByEmail_success() {
        when(userRepository.getRolUserByEmail(user.getEmail())).thenReturn(Mono.just("ADMIN"));

        StepVerifier.create(userUseCase.getRolUserByEmail(user.getEmail()))
                .expectNext("ADMIN")
                .verifyComplete();
    }

    @Test
    void getUserToReport_success() {
        when(userRepository.getAllUser(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.getUserToReport(user.getEmail()))
                .expectNext(user)
                .verifyComplete();
    }
}
