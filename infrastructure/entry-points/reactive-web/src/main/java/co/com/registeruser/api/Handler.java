package co.com.registeruser.api;

import co.com.registeruser.api.dto.LoginRequestDTO;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.dto.UserResponseDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.api.utils.ValidatorsUtils;
import co.com.registeruser.security.JWTUtil;
import co.com.registeruser.usecase.login.LoginUseCase;
import co.com.registeruser.usecase.user.ConflictException.ConflictException;
import co.com.registeruser.usecase.user.UserUseCase;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.registeruser.model.constants.ResponseCodesError.EMAIL_NOT_EXIST;
import static co.com.registeruser.model.constants.ResponseCodesError.USER_NOT_MATCH;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final LoginUseCase loginUseCase;
    private final UserMapperDTO userMapperDTO;
    private final ValidatorsUtils validatorsUtils;
    private final JWTUtil jwtUtil;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, UserRequestDTO.class)
                .doOnNext(userRequest -> log.info("Request createUser: {}", userRequest))
                .flatMap(userRequest -> userUseCase.createUser(userMapperDTO.toUser(userRequest)))
                .doOnNext(user -> log.info("Usuario creado: {}", user))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userMapperDTO.toDto(user))
                );
    }

    public Mono<ServerResponse> loanByEmailUser(ServerRequest request) {
        String email = request.pathVariable("email");
        String tokenHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

        String token = tokenHeader.substring(7);
        Claims claims = jwtUtil.validateTokenAndGetClaims(token);
        String emailToken = claims.getSubject();

        if (!emailToken.equals(email))
            return ServerResponse.status(HttpStatus.CONFLICT).bodyValue(USER_NOT_MATCH);

        return userUseCase.findByEmail(email)
                .switchIfEmpty(Mono.error(new ConflictException(EMAIL_NOT_EXIST)))
                .flatMap(exists -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, LoginRequestDTO.class)
                .doOnNext(loginRequest -> log.info("Request login: {}", loginRequest))
                .flatMap(body -> loginUseCase.login(userMapperDTO.toLogin(body)))
                .flatMap(response -> ServerResponse.status(HttpStatus.OK).bodyValue(userMapperDTO.toDtoLogin(response))
                );
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        log.info("Received request to get all users");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getUserToReport(email).map(userMapperDTO::toDto), UserResponseDTO.class)
                .doOnSuccess(users -> log.info("Successfully retrieved all users"))
                .doOnError(error -> log.error("Error retrieving users: {}", error.getMessage()));
    }

    public Mono<ServerResponse> validateToken(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON).build();
    }
}
