package co.com.registeruser.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserRequestDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombres;

    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    @NotNull(message = "El correo no puede ser nulo")
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String correoElectronico;

    private int idRol;

    @NotNull(message = "El salario base no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario base debe ser al menos 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "El salario base no puede ser mayor a 15000000")
    private BigDecimal salarioBase;
}
