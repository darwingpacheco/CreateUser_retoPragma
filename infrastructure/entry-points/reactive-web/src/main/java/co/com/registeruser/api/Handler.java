package co.com.registeruser.api;

import co.com.registeruser.api.dto.LoginRequestDTO;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.mapper.UserMapperDTO;
import co.com.registeruser.api.utils.ValidatorsUtils;
import co.com.registeruser.security.JWTUtil;
import co.com.registeruser.usecase.login.LoginUseCase;
import co.com.registeruser.usecase.user.UserUseCase;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
            return ServerResponse.status(HttpStatus.CONFLICT)
                    .bodyValue("USER_NOT_MATCH");

        return userUseCase.existsUserByEmail(email)
                .flatMap(exists -> {
                    if (exists) {
                        return ServerResponse.status(HttpStatus.OK)
                                .bodyValue("USER_OK");
                    } else {
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue("USER_NOTFOUND");
                    }
                });
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return validatorsUtils.validateRequestBody(request, LoginRequestDTO.class)
                .doOnNext(loginRequest -> log.info("Request login: {}", loginRequest))
                .flatMap(body -> loginUseCase.login(userMapperDTO.toLogin(body)))
                .flatMap(response -> ServerResponse.status(HttpStatus.OK).bodyValue(userMapperDTO.toDtoLogin(response))
                );
    }
}
