package co.com.registeruser.usecasetest.user.user;

import co.com.registeruser.model.rol.Rol;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;
    private Rol rol;

    @BeforeEach
    void setUp() {
        user = new User(
                "Darwin",
                "Gomez",
                LocalDate.of(2003, 1, 10),
                "Carrera 1 # 15 - 43",
                "3224291874",
                "darwin@gmail.com",
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
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("El email ya está registrado"))
                .verify();
    }

    @Test
    void saveUser_roleNotFound(){
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(e -> e instanceof ConflictException &&
                        e.getMessage().equals("El rol no existe"))
                .verify();
    }

    @Test
    void saveUser_success() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.findRoleById(user.getIdRol())).thenReturn(Mono.just(rol));
        when(userRepository.createUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.createUser(user))
                .expectNextMatches(savedUser -> savedUser.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }
}