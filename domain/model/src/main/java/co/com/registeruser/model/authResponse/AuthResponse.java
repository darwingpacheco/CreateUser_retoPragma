package co.com.registeruser.model.authResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String email;
    private String token;
    private long expiresIn;
}
