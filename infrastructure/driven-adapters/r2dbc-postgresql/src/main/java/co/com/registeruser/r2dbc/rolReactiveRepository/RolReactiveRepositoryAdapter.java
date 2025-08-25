package co.com.registeruser.r2dbc.rolReactiveRepository;

import co.com.registeruser.model.rol.Rol;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.r2dbc.entities.RolEntity;
import co.com.registeruser.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Integer,
        RolReactiveRepository> implements RolRepository {

    public RolReactiveRepositoryAdapter(RolReactiveRepository repository,
                                        ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));
    }
}
