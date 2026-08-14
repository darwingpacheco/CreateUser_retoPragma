
CREATE TABLE IF NOT EXISTS rol (
				    id SERIAL PRIMARY KEY,
				    nombre VARCHAR(255) NOT NULL UNIQUE,
				    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_entity (
                         id BIGSERIAL PRIMARY KEY,
                         nombres VARCHAR(255) NOT NULL,
                         apellidos VARCHAR(255) NOT NULL,
                         correo_electronico VARCHAR(255) NOT NULL UNIQUE,
                         clave VARCHAR(255) NOT NULL,
                         documento_identidad VARCHAR(50) NOT NULL UNIQUE,
                         telefono VARCHAR(20),
                         fecha_nacimiento DATE NOT NULL,
                         direccion VARCHAR(255) NOT NULL,
                         id_rol BIGINT NOT NULL,
                         salario_base DECIMAL(12, 2) NOT NULL,
                         FOREIGN KEY (id_rol) REFERENCES rol(id)
);

INSERT INTO rol (id, nombre, descripcion)
VALUES
    (1, 'ADMIN', 'Control total del sistema'),
    (2, 'ASESOR', 'Gestión de clientes y asesoría'),
    (3, 'CLIENTE', 'Acceso limitado a su información')
ON CONFLICT (id) DO NOTHING;

/*
   $2a$10$qEHTkA51qs0C/q2gRtWWoOTDAg5pKh65URD5qcb196Vv5rsiGElPu
   Darwin10.
*/
