DROP TABLE IF EXISTS MENU;
CREATE TABLE MENU (
    ID_MENU BIGINT NOT NULL,
    NOMBRE VARCHAR(500) NOT NULL,
    CONSTRAINT PK_MENU PRIMARY KEY (ID_MENU)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

DROP TABLE IF EXISTS ROL;
CREATE TABLE ROL (
    ID_ROL BIGINT NOT NULL,
    NOMBRE VARCHAR(500) NOT NULL,
    ACTIVO INTEGER NOT NULL,
    FECHA_HORA TIMESTAMP NOT NULL,
    CONSTRAINT PK_ROL PRIMARY KEY (ID_ROL)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

DROP TABLE IF EXISTS ROL_MENU;
CREATE TABLE ROL_MENU (
    id_rol BIGINT NOT NULL,
    id_menu BIGINT NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    CONSTRAINT PK_ROL_MENU PRIMARY KEY (ID_ROL, ID_MENU),
    CONSTRAINT FK_ROL_MENU_1 FOREIGN KEY (ID_ROL) REFERENCES ROL (ID_ROL),
    CONSTRAINT FK_ROL_MENU_2 FOREIGN KEY (ID_MENU) REFERENCES MENU (ID_MENU)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

DROP TABLE IF EXISTS USUARIO;
CREATE TABLE USUARIO (
    ID_USUARIO BIGINT NOT NULL,
    NOMBRE_COMPLETO VARCHAR(500) NOT NULL,
    NOMBRE_USUARIO VARCHAR(50) NOT NULL,
    CONTRASENA VARCHAR(255) NOT NULL,
    CORREO_ELECTRONICO VARCHAR(200),
    ACTIVO INTEGER NOT NULL,
    DESCRIPCION TEXT,
    ID_ROL BIGINT NOT NULL,
    FECHA_HORA TIMESTAMP NOT NULL,
    CONSTRAINT PK_USUARIO PRIMARY KEY (ID_USUARIO),
    CONSTRAINT FK_USUARIO_1 FOREIGN KEY (ID_ROL) REFERENCES ROL (ID_ROL)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (1, 'menu-Archivo');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (2, 'menuItem-Inicio');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (3, 'menuItem-Roles');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (4, 'menuItem-Usuarios');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (5, 'menu-Felcr');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (6, 'menuItem-Felcr');
INSERT INTO MENU (ID_MENU, NOMBRE) VALUES (7, 'menuItem-Felcr-Config');

INSERT INTO ROL (ID_ROL, NOMBRE, ACTIVO, FECHA_HORA) VALUES (1, 'Administrador', 1, CURRENT_TIMESTAMP);
INSERT INTO ROL (ID_ROL, NOMBRE, ACTIVO, FECHA_HORA) VALUES (2, 'FELCR-OPER', 1, CURRENT_TIMESTAMP);

INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 1, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 2, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 3, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 4, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 5, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 6, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (1, 7, CURRENT_TIMESTAMP);

INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (2, 1, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (2, 2, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (2, 5, CURRENT_TIMESTAMP);
INSERT INTO ROL_MENU (ID_ROL, ID_MENU, FECHA_HORA) VALUES (2, 6, CURRENT_TIMESTAMP);

INSERT INTO USUARIO (ID_USUARIO, NOMBRE_COMPLETO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ACTIVO, DESCRIPCION, ID_ROL, FECHA_HORA) 
VALUES (1, 'Administrador', 'admin', SHA2('admin2023', 512), 'enavas@servicioscompartidos.com', 1, 'Usuario admnistrador del sitema.', 1, CURRENT_TIMESTAMP);
INSERT INTO USUARIO (ID_USUARIO, NOMBRE_COMPLETO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ACTIVO, DESCRIPCION, ID_ROL, FECHA_HORA) 
VALUES (2, 'Jesus Solano', 'jesus.solano', SHA2('admin2023', 512), 'jsolanoc@uno-ca.com', 1, 'Usuario operador FELCR.', 2, CURRENT_TIMESTAMP);
INSERT INTO USUARIO (ID_USUARIO, NOMBRE_COMPLETO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ACTIVO, DESCRIPCION, ID_ROL, FECHA_HORA) 
VALUES (3, 'Daniela Ruiz Arroyo', 'druiza', SHA2('admin2023', 512), 'druiza@uno-ca.com', 1, 'Usuario operador FELCR.', 2, CURRENT_TIMESTAMP);
INSERT INTO USUARIO (ID_USUARIO, NOMBRE_COMPLETO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ACTIVO, DESCRIPCION, ID_ROL, FECHA_HORA) 
VALUES (4, 'Carlos Reynaldo Lopez Gonzalez', 'crlopez', SHA2('admin2023', 512), 'crlopez@servicioscompartidos.com', 1, 'Usuario operador FELCR.', 2, CURRENT_TIMESTAMP);
INSERT INTO USUARIO (ID_USUARIO, NOMBRE_COMPLETO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ACTIVO, DESCRIPCION, ID_ROL, FECHA_HORA) 
VALUES (5, 'Facturación SFC', 'facturacion', SHA2('admin2023', 512), 'facturacion@servicioscompartidos.com', 1, 'Usuario operador FELCR.', 2, CURRENT_TIMESTAMP);

SELECT A.* FROM MENU A;
SELECT A.* FROM ROL A;
SELECT A.* FROM ROL_MENU A;
SELECT A.* FROM USUARIO A;

UPDATE USUARIO SET ID_ROL=1 WHERE ID_USUARIO=1;
UPDATE USUARIO SET NOMBRE_USUARIO='jesus.solano' WHERE ID_USUARIO=2;
DELETE FROM ROL_MENU WHERE ID_ROL=2 AND ID_MENU=7;