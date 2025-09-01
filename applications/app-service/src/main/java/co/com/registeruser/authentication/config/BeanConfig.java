package co.com.registeruser.authentication.config;

import co.com.registeruser.api.jwt.JWTUtil;
import co.com.registeruser.model.rol.gateways.RolRepository;
import co.com.registeruser.model.user.gateways.UserRepository;
import co.com.registeruser.model.util.PasswordEncrypter;
import co.com.registeruser.usecase.login.LoginUseCase;
import co.com.registeruser.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository,
                                   RolRepository rolRepository,
                                   PasswordEncrypter passwordEncrypter) {
        return new UserUseCase(userRepository, rolRepository, passwordEncrypter);
    }

    @Bean
    public LoginUseCase loginUseCase(UserUseCase userUseCase,
                                     JWTUtil jwt) {
        return new LoginUseCase(
                userUseCase,
                (email, rol) -> jwt.generateToken(email, rol),
                jwt.getExpirationSeconds()
        );
    }
}