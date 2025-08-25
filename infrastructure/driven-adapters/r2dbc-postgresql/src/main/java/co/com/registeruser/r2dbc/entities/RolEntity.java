package co.com.registeruser.r2dbc.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table(name = "rol")
public class RolEntity {

    @Id
    private Integer id;
    private String nombre;
    private String descripcion;
}
