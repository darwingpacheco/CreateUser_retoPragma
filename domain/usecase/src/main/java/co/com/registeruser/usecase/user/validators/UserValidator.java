package co.com.registeruser.usecase.user.validators;

import co.com.registeruser.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserValidator {

    public static void validate(User user) {
        if (user.getNombres() == null || user.getNombres().trim().isEmpty())
            throw new IllegalArgumentException("El campo 'nombres' es obligatorio");

        if (user.getApellidos() == null || user.getApellidos().trim().isEmpty())
            throw new IllegalArgumentException("El campo 'apellidos' es obligatorio");

        if (user.getCorreoElectronico() == null || user.getCorreoElectronico().trim().isEmpty())
            throw new IllegalArgumentException("El campo 'correo_electronico' es obligatorio");

        if (!user.getCorreoElectronico().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Formato del correo es inválido");

        if (user.getSalarioBase() == null || user.getSalarioBase().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El campo 'salario_base' es obligatorio y debe ser mayor a 0");
    }
}
