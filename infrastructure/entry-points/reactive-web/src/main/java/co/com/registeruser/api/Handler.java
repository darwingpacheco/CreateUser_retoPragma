package co.com.registeruser.api;

import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.api.utils.ValidatorsUtils;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDTO userMapperDTO;
    private final jakarta.validation.Validator validator;
    private final ValidatorsUtils validatorsUtils;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, UserRequestDTO.class)
                .flatMap(userRequest -> userUseCase.createUser(userMapperDTO.toUser(userRequest))
                        .flatMap(user -> ServerResponse.ok().bodyValue(userMapperDTO.toDto(user)))
                );
    }
}