package co.com.registeruser.model.rol.gateways;

import co.com.registeruser.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> findRoleById(int idRol);
}
