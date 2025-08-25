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

    private final RolReactiveRepositoryAdapter rolAdapter;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository,
                                       ObjectMapper mapper,
                                       RolReactiveRepositoryAdapter rolAdapter) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.rolAdapter = rolAdapter;
    }

    @Override
    @Transactional
    public Mono<User> createUser(User user) {
        return this.repository.save(this.toData(user))
                .map(this::toEntity);
    }

    @Override
    @Transactional
    public Mono<Boolean> userEmailExist(String email) {
        return this.repository.findByCorreoElectronico(email)
                .map(this::toEntity)
                .map(user -> true)
                .defaultIfEmpty(false);
    }
}