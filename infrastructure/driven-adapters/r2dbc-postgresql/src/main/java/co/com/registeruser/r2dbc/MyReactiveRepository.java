package co.com.registeruser.r2dbc;

import co.com.registeruser.model.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("SELECT * FROM user_entity WHERE correo_electronico = :correoElectronico")
    Mono<UserEntity> findByCorreoElectronico(String correoElectronico);
}
