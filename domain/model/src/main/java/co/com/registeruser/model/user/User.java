package co.com.registeruser.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private String lastName;
    private LocalDate dateBirth;
    private String address;
    private String phone;
    private String email;
    private int idRol;
    private BigDecimal baseSalary;
}
