package co.com.registeruser.api.GlobalExceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Object>> handleValidationException(WebExchangeBindException ex) {
        var errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage()
                ));
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage())));
    }
}
