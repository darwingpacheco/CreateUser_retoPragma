package co.com.registeruser.model.util;

public interface JwtGateway {

    String generateToken(String email, String role);

}
