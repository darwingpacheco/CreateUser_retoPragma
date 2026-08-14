package co.com.registeruser.usecase.user.ConflictException;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException{

    public ConflictException(String message) {
        super(message);
    }
}
