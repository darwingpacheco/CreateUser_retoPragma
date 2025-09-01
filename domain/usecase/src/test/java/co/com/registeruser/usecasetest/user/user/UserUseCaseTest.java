package co.com.registeruser.usecasetest.user.user;

import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.rol.Rol;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.statusCode.LoginStatus;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.model.util.PasswordEncrypter;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private PasswordEncrypter passwordEncrypter;

    private User user;
    private Rol rol;

    @BeforeEach
    void setUp() {
        user = new User(
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

        rol = new Rol(
                1,
                "ADMIN",
                "Encargado de registro"
        );
    }

    @Test
    void saveUser_emailIsExist(){
        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("El email ya está registrado"))
                .verify();
    }

    @Test
    void saveUser_roleNotFound(){
        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("El rol no existe"))
                .verify();
    }

    @Test
    void saveUser_success() {
        User user = new User();
        user.setEmail("juan.perez@example.com");
        user.setPassword("12345");
        user.setIdRol(1);

        when(userRepository.existUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.just(rol));
        when(passwordEncrypter.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.createUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.createUser(user))
                .expectNextMatches(u -> u.getEmail().equals("juan.perez@example.com"))
                .verifyComplete();
    }

    @Test
    void existsUserByEmail_true() {
        when(userRepository.existUserByEmail("darwin@gmail.com")).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.existsUserByEmail("darwin@gmail.com"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsUserByEmail_false() {
        when(userRepository.existUserByEmail("darwin@gmail.com")).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.existsUserByEmail("darwin@gmail.com"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void getRolUserByEmail_success() {
        when(userRepository.getRolUserByEmail("darwin@gmail.com")).thenReturn(Mono.just("ADMIN"));

        StepVerifier.create(userUseCase.getRolUserByEmail("darwin@gmail.com"))
                .expectNext("ADMIN")
                .verifyComplete();
    }

    @Test
    void getRolUserByEmail_empty() {
        when(userRepository.getRolUserByEmail("darwin@gmail.com")).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getRolUserByEmail("darwin@gmail.com"))
                .verifyComplete();
    }

    @Test
    void validateUser_success() {
        user.setPassword("hashedPassword");

        when(userRepository.getUserByEmail("darwin@gmail.com")).thenReturn(Mono.just(user));
        when(passwordEncrypter.matches("1234567890", "hashedPassword")).thenReturn(true);

        StepVerifier.create(userUseCase.validateUser(
                        new AuthRequest("darwin@gmail.com", "1234567890")))
                .expectNext(LoginStatus.SUCCESS)
                .verifyComplete();
    }

    @Test
    void validateUser_wrongPassword() {
        user.setPassword("hashedPassword");

        when(userRepository.getUserByEmail("darwin@gmail.com")).thenReturn(Mono.just(user));
        when(passwordEncrypter.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        StepVerifier.create(userUseCase.validateUser(
                        new AuthRequest("darwin@gmail.com", "wrongPassword")))
                .expectNext(LoginStatus.WRONG_PASSWORD)
                .verifyComplete();
    }

    @Test
    void validateUser_userNotFound() {
        when(userRepository.getUserByEmail("noexist@gmail.com")).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.validateUser(
                        new AuthRequest("noexist@gmail.com", "12345")))
                .expectNext(LoginStatus.USER_NOT_FOUND)
                .verifyComplete();
    }



}