package co.com.registeruser.r2dbc.rolReactiveRepository;

import co.com.registeruser.r2dbc.entities.RolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Integer>,
        ReactiveQueryByExampleExecutor<RolEntity> {
}
