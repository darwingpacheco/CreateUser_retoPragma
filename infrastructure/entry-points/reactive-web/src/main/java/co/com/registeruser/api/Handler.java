package co.com.registeruser.api;

import co.com.registeruser.api.GlobalExceptions.ValidateExceptionHandler;
import co.com.registeruser.api.dto.LoginRequestDTO;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.jwt.JWTUtil;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.api.securityService.SecurityService;
import co.com.registeruser.api.utils.ValidatorsUtils;
import co.com.registeruser.model.authResponse.AuthResponse;
import co.com.registeruser.usecase.login.LoginUseCase;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final LoginUseCase loginUseCase;
    private final UserMapperDTO userMapperDTO;
    private final ValidatorsUtils validatorsUtils;
    private final SecurityService securityService;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, UserRequestDTO.class)
                .doOnNext(userRequest -> log.info("Request createUser: {}", userRequest))
                .flatMap(userRequest ->
                        securityService.extractRole(request)
                                .flatMap(role -> {
                                    if ("CLIENTE".equalsIgnoreCase(role)) {
                                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                                .bodyValue("No tiene permisos para crear usuarios");
                                    }

                                    return userUseCase.createUser(userMapperDTO.toUser(userRequest))
                                            .doOnNext(user -> log.info("Usuario creado: {}", user))
                                            .flatMap(user -> ServerResponse.ok().bodyValue(userMapperDTO.toDto(user)));
                                })
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

    public Mono<ServerResponse> loanByEmailUser(ServerRequest request) {
        String email = request.pathVariable("email");
        return securityService.extractRole(request)
                .flatMap(rol -> {
                    if (!"CLIENTE".equals(rol.toUpperCase())) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                .bodyValue("No tiene permisos para solicitar un prestamo");
                    }
                    return userUseCase.existsUserByEmail(email)
                            .flatMap(exists -> {
                                if (exists) {
                                    return ServerResponse.ok().build();
                                } else {
                                    return ServerResponse.notFound().build();
                                }
                            });
                });
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, LoginRequestDTO.class)
                .doOnNext(loginRequest -> log.info("Request login: {}", loginRequest))
                .flatMap(body -> loginUseCase.login(userMapperDTO.toLogin(body)))
                .flatMap((AuthResponse responseLogin) -> ServerResponse.ok().bodyValue(responseLogin))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
    }
}
