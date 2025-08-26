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
    private LocalDate dateBirth;
    private String address;
    private String phone;

    @NotNull(message = "El correo no puede ser nulo")
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    private int idRol;

    @NotNull(message = "El salario base no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario base debe ser al menos 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "El salario base no puede ser mayor a 15000000")
    private BigDecimal baseSalary;
}
