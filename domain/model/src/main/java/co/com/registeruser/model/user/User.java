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
    private Long userID;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String numberDocument;
    private String phone;
    private LocalDate dateBirth;
    private String address;
    private int idRol;
    private BigDecimal baseSalary;
}
