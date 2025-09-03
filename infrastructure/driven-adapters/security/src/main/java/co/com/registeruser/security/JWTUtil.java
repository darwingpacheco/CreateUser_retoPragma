package co.com.registeruser.security;

import co.com.registeruser.model.util.JwtGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JWTUtil implements JwtGateway {
    private long expiration;

    SecretKey key = Jwts.SIG.HS512.key().build();

    @Override
    public String generateToken(String email, String rol) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim("role", rol)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiration)))
                .signWith(key)
                .compact();
    }

    public Claims validateTokenAndGetClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}