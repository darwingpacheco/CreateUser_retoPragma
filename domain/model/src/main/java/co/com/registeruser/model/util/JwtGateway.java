package co.com.registeruser.model.util;

public interface JwtGateway {

    String generateToken(String email, String role);
    boolean isValid(String token);
    String getEmailFromToken(String token);
    String getRolFromToken(String token);
}
