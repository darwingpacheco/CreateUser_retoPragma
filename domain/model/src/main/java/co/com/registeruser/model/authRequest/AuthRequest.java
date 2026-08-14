package co.com.registeruser.model.authRequest;
import lombok.*;
//import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthRequest {
    private String email;
    private String password;
    private String token;
}
