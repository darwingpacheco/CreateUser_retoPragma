DROP TABLE IF EXISTS user_entity;
DROP TABLE IF EXISTS rol;

SELECT * FROM rol;
SELECT * FROM user_entity;

CREATE TABLE rol (
				    id SERIAL PRIMARY KEY,
				    nombre VARCHAR(255) NOT NULL UNIQUE,
				    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE user_entity (
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
    (3, 'CLIENTE', 'Acceso limitado a su información');
