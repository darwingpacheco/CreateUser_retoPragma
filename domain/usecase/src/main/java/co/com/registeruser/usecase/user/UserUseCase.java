package co.com.registeruser.usecase.user;

import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.user.User;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public Mono<User> createUser(User user) {
        return userRepository.userEmailExist(user.getCorreoElectronico())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("El email ya está registrado"));
                    }
                    return rolRepository.findById(user.getIdRol())
                            .switchIfEmpty(Mono.error(new ConflictException("El rol no existe")))
                            .flatMap(rol -> userRepository.createUser(user));
                });
    }
}
