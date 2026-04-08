package vargas.fabian.bl.logic;

import vargas.fabian.bl.dao.DAOCliente;
import vargas.fabian.bl.entities.Cliente;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class GestorCliente {

    public static String registrarCliente(String nombreCompleto, String cedula, LocalDate fechaNacimiento, String ocupacion, String residencia, String contrasena) throws SQLException, IOException, ClassNotFoundException {
        Cliente cliente = new Cliente(nombreCompleto, cedula, fechaNacimiento, ocupacion, residencia, contrasena);
        return DAOCliente.insertarCliente(new Cliente(nombreCompleto, cedula, fechaNacimiento, ocupacion, residencia, contrasena));
    }
    public static Cliente ingresarCliente(String cedula, String contrasena) throws SQLException, IOException, ClassNotFoundException {
        return DAOCliente.seleccionarCliente(cedula, contrasena);
    }
}
