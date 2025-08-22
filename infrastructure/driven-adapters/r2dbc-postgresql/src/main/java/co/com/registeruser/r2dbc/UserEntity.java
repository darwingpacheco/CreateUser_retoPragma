package co.com.registeruser.r2dbc;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user_entity")
@Data
public class UserEntity {
    private String nombres;
    private String apellidos;
    private LocalDate fecha_nacimiento;
    private String direccion;
    private String telefono;
    private String correo_electronico;
    private BigDecimal salario_base;
}