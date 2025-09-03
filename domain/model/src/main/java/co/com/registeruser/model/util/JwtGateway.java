package co.com.registeruser.model.util;

import co.com.registeruser.model.user.User;

public interface JwtGateway {

    String generateToken(User user, String role);

}
