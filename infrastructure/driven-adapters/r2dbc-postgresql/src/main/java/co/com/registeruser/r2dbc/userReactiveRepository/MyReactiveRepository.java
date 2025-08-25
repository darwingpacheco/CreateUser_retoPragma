package co.com.registeruser.r2dbc.userReactiveRepository;

import co.com.registeruser.r2dbc.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("SELECT * FROM user_entity WHERE correo_electronico = :correoElectronico")
    Mono<UserEntity> findByCorreoElectronico(String correoElectronico);
}
