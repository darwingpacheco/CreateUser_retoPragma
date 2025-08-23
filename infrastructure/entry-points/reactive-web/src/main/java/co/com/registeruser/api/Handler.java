package co.com.registeruser.api;

import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.model.user.gateways.UserRepository;

import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDTO userMapperDTO;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .flatMap(dto ->
                        userUseCase.createUser(userMapperDTO.toUser(dto))
                                .flatMap(user -> ServerResponse.ok().bodyValue(userMapperDTO.toDto(user)))
                                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()))
                );
    }
}