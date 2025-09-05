package co.com.registeruser.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(String name,
                              String lastName,
                              String email,
                              String numberDocument,
                              BigDecimal baseSalary
                            ) {

}
