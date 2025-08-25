package co.com.registeruser.api.GlobalExceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof IllegalArgumentException e) {
            status = HttpStatus.BAD_REQUEST;
            errorResponse.put("error", "Bad Request");

            if (e.getMessage() != null && e.getMessage().contains(":")) {
                var details = Arrays.stream(e.getMessage().split(","))
                        .map(String::trim)
                        .map(err -> {
                            String[] parts = err.split(":", 2);
                            return Map.of(
                                    "field", parts[0].trim(),
                                    "message", parts.length > 1 ? parts[1].trim() : "Invalid value"
                            );
                        })
                        .toList();
                errorResponse.put("details", details);
            } else {
                errorResponse.put("message", e.getMessage());
            }
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}