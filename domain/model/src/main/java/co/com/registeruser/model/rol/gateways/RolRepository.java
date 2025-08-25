package co.com.registeruser.model.rol.gateways;

import co.com.registeruser.model.rol.Rol;
import co.com.registeruser.model.user.User;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> findById(int idRol);
}
