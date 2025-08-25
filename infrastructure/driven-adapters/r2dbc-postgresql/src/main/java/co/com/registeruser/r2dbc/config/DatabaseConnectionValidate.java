package co.com.registeruser.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class DatabaseConnectionValidate {

    @Bean
    public ApplicationRunner checkDatabase(ConnectionPool pool) {
        return args -> Mono.from(pool.create())
                .flatMap(conn -> Mono.from(conn.createStatement("SELECT 1").execute())
                        .doFinally(signal -> conn.close()))
                .doOnSuccess(r -> System.out.println("Conexión a BD OK"))
                .doOnError(e -> {
                    System.err.println("Error de conexión: " + e.getMessage());
                    System.exit(1);
                })
                .subscribe();
    }
}
