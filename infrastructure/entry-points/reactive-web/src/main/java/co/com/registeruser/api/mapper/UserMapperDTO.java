package co.com.registeruser.api.mapper;

import co.com.registeruser.api.dto.LoginRequestDTO;
import co.com.registeruser.api.dto.LoginResponseDTO;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.dto.UserResponseDTO;
import co.com.registeruser.model.authRequest.AuthRequest;
import co.com.registeruser.model.authResponse.AuthResponse;
import co.com.registeruser.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperDTO {

    User toUser(UserRequestDTO dto);

    UserResponseDTO toDto(User user);

    AuthRequest toLogin(LoginRequestDTO dtoLogin);

    LoginResponseDTO toDtoLogin(AuthRequest dtoLogin);
}
