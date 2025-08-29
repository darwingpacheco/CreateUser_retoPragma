package co.com.registeruser.model.util;

public interface PasswordEncrypter {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
