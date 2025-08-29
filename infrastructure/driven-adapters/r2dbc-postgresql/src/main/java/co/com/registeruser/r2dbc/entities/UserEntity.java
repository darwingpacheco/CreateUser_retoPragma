package co.com.registeruser.r2dbc.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("user_entity")
@Data
public class UserEntity {
    @Id
    private Long id;

    @Column("nombres")
    private String name;

    @Column("apellidos")
    private String lastName;

    @Column("correo_electronico")
    private String email;

    @Column("clave")
    private String password;

    @Column("documento_identidad")
    private String numberDocument;

    @Column("telefono")
    private String phone;

    @Column("fecha_nacimiento")
    private LocalDate dateBirth;

    @Column("direccion")
    private String address;

    @Column("id_rol")
    private int idRol;

    @Column("salario_base")
    private BigDecimal baseSalary;
}
