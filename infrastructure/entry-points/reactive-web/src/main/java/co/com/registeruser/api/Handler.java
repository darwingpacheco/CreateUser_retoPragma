package co.com.registeruser.api;

import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;

import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDTO userMapperDTO;
    private final jakarta.validation.Validator validator;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .flatMap(dto -> {
                    var validateFields = validator.validate(dto);
                    if (!validateFields.isEmpty()) {
                        String errorMsg = validateFields.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));
                        return Mono.error(new IllegalArgumentException(errorMsg));
                    }

                    return userUseCase.createUser(userMapperDTO.toUser(dto))
                            .flatMap(user -> ServerResponse.ok().bodyValue(userMapperDTO.toDto(user)));
                });
    }
}