package co.com.registeruser.r2dbc.userReactiveRepository;

import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.r2dbc.entities.UserEntity;
import co.com.registeruser.r2dbc.helper.ReactiveAdapterOperations;
import co.com.registeruser.r2dbc.rolReactiveRepository.RolReactiveRepositoryAdapter;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        String,
        MyReactiveRepository
        > implements UserRepository {

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository,
                                       ObjectMapper mapper,
                                       RolReactiveRepositoryAdapter rolAdapter) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    @Transactional
    public Mono<User> createUser(User user) {
        return this.repository.save(this.toData(user))
                .map(this::toEntity);
    }

    @Override
    @Transactional
    public Mono<User> findByEmail(String email) {
        return this.repository.findByEmail(email)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existUserByEmail(String email) {
        return this.repository.findByEmail(email)
                .map(this::toEntity)
                .map(user -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return this.repository.getUserByEmail(email)
                .map(this::toEntity);
    }

    @Override
    public Mono<String> getRolUserByEmail(String email) {
        return this.repository.getRolByEmail(email);
    }

    @Override
    public Mono<User> getAllUser(String email) {
        return repository.getUserByEmail(email)
                .map(this::toEntity);
    }
}