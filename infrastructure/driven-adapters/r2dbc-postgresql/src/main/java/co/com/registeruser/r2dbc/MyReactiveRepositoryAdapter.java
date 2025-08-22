package co.com.registeruser.r2dbc;

import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
    UserEntity,
    String,
    MyReactiveRepository
> implements UserRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> createUser(User user) {
        return this.repository.save(this.toData(user))
                .map(this::toEntity);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByCorreoElectronico(email)
                .map(this::toEntity);
    }
}
