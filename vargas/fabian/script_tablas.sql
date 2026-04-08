CREATE DATABASE bd_banco;
USE bd_banco;

CREATE TABLE t_cliente(
   nombre_completo VARCHAR(150) NOT NULL,
   cedula VARCHAR(20) PRIMARY KEY,
   fecha_nacimiento DATE NOT NULL,
   residencia VARCHAR(100),
   ocupacion VARCHAR(100),
   contrasena VARCHAR(100) NOT NULL
);


CREATE TRIGGER validar_edad
    BEFORE INSERT ON t_cliente
    FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento > (CURRENT_DATE - INTERVAL 18 YEAR) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente debe ser mayor de edad.';
END IF;
END;

CREATE TABLE t_cuenta_ahorro(
     id VARCHAR(20) PRIMARY KEY,
     cliente_cedula VARCHAR(20) NOT NULL,
     saldo DOUBLE NOT NULL,
     tasa_interes DOUBLE NOT NULL,
     saldo_minimo DOUBLE NOT NULL DEFAULT 100.00,
     CONSTRAINT fk_ahorro_cliente FOREIGN KEY (cliente_cedula) REFERENCES t_cliente(cedula)
);

CREATE TABLE t_cuenta_credito(
     id VARCHAR(20) PRIMARY KEY,
     cliente_cedula VARCHAR(20) NOT NULL,
     saldo DOUBLE NOT NULL,
     tasa_interes DOUBLE NOT NULL,
     CONSTRAINT fk_credito_cliente FOREIGN KEY (cliente_cedula) REFERENCES t_cliente(cedula)
);

CREATE TABLE t_cuenta_debito(
     id VARCHAR(20) PRIMARY KEY,
     cliente_cedula VARCHAR(20) NOT NULL,
     saldo DOUBLE NOT NULL,
     tasa_interes DOUBLE NOT NULL,
     CONSTRAINT fk_debito_cliente FOREIGN KEY (cliente_cedula) REFERENCES t_cliente(cedula)
);