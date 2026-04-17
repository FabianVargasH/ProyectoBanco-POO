package vargas.fabian.bl.dao;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCliente {

    private static String statement;
    private static String query;

    public static String insertarCliente(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        statement = "INSERT INTO t_cliente VALUES ('"+cliente.getNombreCompleto()+"','"+cliente.getCedula()+
        "', '" + cliente.getFechaNacimiento()+"', '" + cliente.getOcupacion() + "', '" + cliente.getResidencia() + "', '"+ cliente.getContrasena()+"');";
        Conector.getConexion().ejecutarStatement(statement);
        return "El cliente se registró exitosamente";
    }

    public static Cliente seleccionarCliente(String cedula, String contrasena) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cliente WHERE cedula = ? AND contrasena = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, cedula, contrasena);
        if(!resultado.next()) return null;
        return new Cliente(
                resultado.getString("nombre_completo"),
                resultado.getString("cedula"),
                resultado.getDate("fecha_nacimiento").toLocalDate(),
                resultado.getString("residencia"),
                resultado.getString("ocupacion"),
                resultado.getString("contrasena")
        );
    }
}
