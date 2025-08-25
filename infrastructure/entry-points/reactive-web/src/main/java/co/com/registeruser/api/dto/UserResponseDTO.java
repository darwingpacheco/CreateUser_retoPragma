package co.com.registeruser.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(String nombres,
                              String apellidos,
                              LocalDate fechaNacimiento,
                              String direccion,
                              String telefono,
                              String correoElectronico,
                              BigDecimal salarioBase
                            ) {
}
