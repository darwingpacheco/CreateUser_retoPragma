package co.com.registeruser.api.mapper;

import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.dto.UserResponseDTO;
import co.com.registeruser.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperDTO {

    User toUser(UserRequestDTO dto);

    UserResponseDTO toDto(User user);
}
