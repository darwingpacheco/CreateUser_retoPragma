package co.com.registeruser.api;

import co.com.registeruser.api.GlobalExceptions.ValidateExceptionHandler;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.api.utils.ValidatorsUtils;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDTO userMapperDTO;
    private final ValidatorsUtils validatorsUtils;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, UserRequestDTO.class)
                .doOnNext(userRequest -> log.info("Request createUser: {}", userRequest))
                .flatMap(userRequest -> userUseCase.createUser(userMapperDTO.toUser(userRequest))
                        .doOnNext(user -> log.info("Usuario creado: {}", user))
                        .flatMap(user -> ServerResponse.ok().bodyValue(userMapperDTO.toDto(user)))
                )
                .doOnError(error -> {
                    if (error instanceof ValidateExceptionHandler ex) {
                        ex.getError().getAllErrors().forEach(err -> {
                            log.error("Validación fallida: campo={}, mensaje={}",
                                    ((FieldError) err).getField(),
                                    err.getDefaultMessage());
                        });
                    } else {
                        log.error("Error en createUser: {}", error.getMessage(), error);
                    }
                });
    }
}
