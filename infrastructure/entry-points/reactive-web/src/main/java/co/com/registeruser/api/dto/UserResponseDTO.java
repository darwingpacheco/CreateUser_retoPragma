package co.com.registeruser.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(String name,
                              String lastName,
                              LocalDate dateBirth,
                              String address,
                              String phone,
                              String email,
                              int idRol,
                              BigDecimal baseSalary
                            ) {

}
