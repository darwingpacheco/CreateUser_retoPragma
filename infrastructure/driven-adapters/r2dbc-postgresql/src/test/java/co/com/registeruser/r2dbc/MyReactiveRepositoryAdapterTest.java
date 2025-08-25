package co.com.registeruser.r2dbc;

import co.com.registeruser.model.user.User;
import co.com.registeruser.r2dbc.entities.UserEntity;
import co.com.registeruser.r2dbc.rolReactiveRepository.RolReactiveRepositoryAdapter;
import co.com.registeruser.r2dbc.userReactiveRepository.MyReactiveRepository;
import co.com.registeruser.r2dbc.userReactiveRepository.MyReactiveRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    @Mock
    private MyReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private MyReactiveRepositoryAdapter adapter;

    private User userDomain;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userDomain = new User();
        userDomain.setNombres("Juan");
        userDomain.setApellidos("Pérez");
        userDomain.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        userDomain.setDireccion("Calle 123");
        userDomain.setTelefono("3001234567");
        userDomain.setCorreoElectronico("juan@test.com");
        userDomain.setIdRol(1);
        userDomain.setSalarioBase(BigDecimal.valueOf(2000));

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setNombres("Juan");
        userEntity.setApellidos("Pérez");
        userEntity.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        userEntity.setDireccion("Calle 123");
        userEntity.setTelefono("3001234567");
        userEntity.setCorreoElectronico("juan@test.com");
        userEntity.setIdRol(1);
        userEntity.setSalarioBase(BigDecimal.valueOf(2000));
    }

    @Test
    void testCreateUser_success() {
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(userDomain);
        when(mapper.map(userDomain, UserEntity.class)).thenReturn(userEntity);

        Mono<User> result = adapter.createUser(userDomain);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getCorreoElectronico().equals("juan@test.com"))
                .verifyComplete();
    }

    @Test
    void testUserEmailExist_found() {
        when(repository.findByCorreoElectronico("juan@test.com"))
                .thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(userDomain);

        Mono<Boolean> result = adapter.userEmailExist("juan@test.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testUserEmailExist_notFound() {
        when(repository.findByCorreoElectronico("notfound@test.com")).thenReturn(Mono.empty());

        Mono<Boolean> result = adapter.userEmailExist("notfound@test.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}