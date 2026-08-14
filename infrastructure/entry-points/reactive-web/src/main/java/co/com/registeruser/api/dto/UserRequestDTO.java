package co.com.registeruser.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserRequestDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @NotNull(message = "El correo no puede ser nulo")
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotNull(message = "El número de documento no puede ser nulo")
    @NotBlank(message = "El número de documento no puede estar vacío")
    private String numberDocument;

    private String phone;

    private LocalDate dateBirth;
    private String address;
    private int idRol;

    @NotNull(message = "El salario base no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario base debe ser al menos 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "El salario base no puede ser mayor a 15000000")
    private BigDecimal baseSalary;
}
