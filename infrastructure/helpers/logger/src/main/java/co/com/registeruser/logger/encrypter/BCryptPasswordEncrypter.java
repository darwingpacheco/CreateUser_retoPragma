package co.com.registeruser.logger.encrypter;

import co.com.registeruser.model.util.PasswordEncrypter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncrypter implements PasswordEncrypter {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return null;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return false;
    }
}
