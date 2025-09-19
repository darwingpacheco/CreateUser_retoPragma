package co.com.registeruser.api.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(
                                "/api/auth/**",
                                "/api/v1/login",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/proxy/**",
                                "/actuator/**"
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").hasAnyRole("ADMIN", "ASESOR")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/create/email/*").hasAnyRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/updateLoan/email/*").hasAnyRole("ASESOR")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/autoValidate/email/*").hasAnyRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/all/*").hasAnyRole("ASESOR")
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            var response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            String body = "{\"error\":\"Unauthorized\",\"message\":\"Token inválido o no proporcionado\"}";
                            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                            return response.writeWith(Mono.just(buffer));
                        })
                        .accessDeniedHandler((exchange, denied) -> {
                            var response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.FORBIDDEN); // 403
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            String body = "{\"error\":\"Forbidden\",\"message\":\"No tiene permisos para acceder a este recurso\"}";
                            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                            return response.writeWith(Mono.just(buffer));
                        })
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}