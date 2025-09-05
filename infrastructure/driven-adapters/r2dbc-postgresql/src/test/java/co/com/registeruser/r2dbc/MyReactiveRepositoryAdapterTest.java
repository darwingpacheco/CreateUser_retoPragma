package co.com.registeruser.r2dbc;

import co.com.registeruser.model.user.User;
import co.com.registeruser.r2dbc.entities.UserEntity;
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

//    @BeforeEach
//    void setUp() {
//        userDomain = new User();
//        userDomain.setName("Juan");
//        userDomain.setLastName("Pérez");
//        userDomain.setDateBirth(LocalDate.of(1990, 1, 1));
//        userDomain.setAddress("Calle 123");
//        userDomain.setPhone("3001234567");
//        userDomain.setEmail("juan@test.com");
//        userDomain.setIdRol(1);
//        userDomain.setBaseSalary(BigDecimal.valueOf(2000));
//
//        userEntity = new UserEntity();
//        userEntity.setId(1L);
//        userEntity.setName("Juan");
//        userEntity.setLastName("Pérez");
//        userEntity.setDateBirth(LocalDate.of(1990, 1, 1));
//        userEntity.setAddress("Calle 123");
//        userEntity.setPhone("3001234567");
//        userEntity.setEmail("juan@test.com");
//        userEntity.setIdRol(1);
//        userEntity.setBaseSalary(BigDecimal.valueOf(2000));
//    }
//
//    @Test
//    void testCreateUser_success() {
//        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
//        when(mapper.map(userEntity, User.class)).thenReturn(userDomain);
//        when(mapper.map(userDomain, UserEntity.class)).thenReturn(userEntity);
//
//        Mono<User> result = adapter.createUser(userDomain);
//
//        StepVerifier.create(result)
//                .expectNextMatches(user -> user.getEmail().equals("juan@test.com"))
//                .verifyComplete();
//    }
//
//    @Test
//    void testUserEmailExist_found() {
//        when(repository.findByEmail("juan@test.com"))
//                .thenReturn(Mono.just(userEntity));
//        when(mapper.map(userEntity, User.class)).thenReturn(userDomain);
//
//        Mono<Boolean> result = adapter.findByEmail("juan@test.com");
//
//        StepVerifier.create(result)
//                .expectNext(true)
//                .verifyComplete();
//    }
//
//    @Test
//    void testUserEmailExist_notFound() {
//        when(repository.findByEmail("notfound@test.com")).thenReturn(Mono.empty());
//
//        Mono<Boolean> result = adapter.findByEmail("notfound@test.com");
//
//        StepVerifier.create(result)
//                .expectNext(false)
//                .verifyComplete();
//    }
}