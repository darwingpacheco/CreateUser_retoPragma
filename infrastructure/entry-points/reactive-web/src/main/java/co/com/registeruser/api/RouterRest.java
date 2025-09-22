package co.com.registeruser.api;

import co.com.registeruser.api.dto.LoginRequestDTO;
import co.com.registeruser.api.dto.UserRequestDTO;
import co.com.registeruser.api.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "createUser",
                    operation = @Operation(
                            operationId = "CreateNewUser",
                            summary = "Crear un nuevo usuario",
                            description = "Permite crear un usuario nuevo. Requiere token de administrador.",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Objeto con los datos del usuario",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserRequestDTO.class),
                                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = "{\"nombre\":\"Darwin\",\"email\":\"darwin@example.com\",\"password\":\"123456\",\"idRol\":1}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario creado correctamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserRequestDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación de datos",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"error\": \"Campo 'email' es obligatorio\"}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Token faltante o inválido",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"error\": \"No se envió token de autorización\"}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Rol no autorizado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"error\": \"No tiene permisos para crear usuarios\"}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Conflicto al crear usuario",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"error\": \"El email ya está registrado\"}"
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/email/{email}",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "loanByEmailUser",
                    produces = {"application/json"},
                    operation = @Operation(
                            operationId = "GetUserByEmail",
                            summary = "Consultar usuario por correo",
                            description = "Verifica si un usuario existe por su email.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserResponseDTO.class),
                                                    examples = @ExampleObject(value = "{ \"message\": \"Usuario encontrado\", \"data\": { ... } }")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Usuario no encontrado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{ \"error\": \"Usuario con email darwin@example.com no encontrado\" }")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Rol no autorizado para consultar usuarios",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{ \"error\": \"Acceso denegado: rol no autorizado\" }")
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    produces = {"application/json"},
                    operation = @Operation(
                            operationId = "LoginUser",
                            summary = "Login de usuario",
                            description = "Permite iniciar sesión con email y contraseña. Retorna token JWT.",
                            requestBody = @RequestBody(
                                    description = "Datos de login",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRequestDTO.class),
                                            examples = @ExampleObject(value = "{ \"email\": \"darwin@example.com\", \"password\": \"123456\" }")
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login exitoso. Retorna AuthResponse con token",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiResponse.class),
                                                    examples = @ExampleObject(value = "{ \"message\": \"Login exitoso\", \"token\": \"eyJhbGci...\" }")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Correo no existe o contraseña incorrecta",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{ \"error\": \"Correo o contraseña incorrecta\" }")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error en la validación de datos",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{ \"error\": \"Formato de email inválido\" }")
                                            )
                                    )
                            }
                    )
            )
    })

    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::createUser)
                .andRoute(GET("/api/v1/usuarios/create/email/{email}"), handler::loanByEmailUser)
                .andRoute(POST("/api/v1/login"), handler::login)
                .andRoute(GET("/api/v1/usuarios/all/{email}"), handler::getAllUsers)
                .andRoute(GET("/api/v1/usuarios/autoValidate/{email}"), handler::getAllUsers)
                .andRoute(GET("/api/v1/validateToken/reports"), handler::validateToken)
                .andRoute(GET("/api/v1/usuarios/updateLoan/email/{email}"), handler::validateToken);
    }
}
