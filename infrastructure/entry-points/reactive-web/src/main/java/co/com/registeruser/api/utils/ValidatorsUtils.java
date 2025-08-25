package co.com.registeruser.api.utils;

import co.com.registeruser.api.GlobalExceptions.ValidateExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class ValidatorsUtils {


    private final SmartValidator validator;

    public <T> Mono<T> validateRequestBody(ServerRequest request, Class<T> userRequestDTOClass) {
        return request.bodyToMono(userRequestDTOClass)
                .flatMap(body -> {
                            var errors = new BeanPropertyBindingResult(body, userRequestDTOClass.getName());
                            validator.validate(body, errors);

                            if (errors.hasErrors()) {
                                return Mono.error(new ValidateExceptionHandler(errors));
                            }

                            return Mono.just(body);
                        }
                );
    }
}
