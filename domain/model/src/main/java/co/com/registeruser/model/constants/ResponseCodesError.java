package co.com.registeruser.model.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseCodesError {

    public static final String USER_NOT_MATCH = "El usuario autenticado no es el mismo de la solicitud.";
    public static final String EMAIL_NOT_EXIST = "El correo electrónico no existe.";
    public static final String INVALID_ACCESS = "Credenciales incorrectas";

}
