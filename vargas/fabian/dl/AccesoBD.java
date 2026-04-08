package vargas.fabian.dl;

import java.sql.*;

public class AccesoBD {

    private Connection conexion = null;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public AccesoBD(String direccion, String usuario, String contrasenia) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection(direccion, usuario, contrasenia);
    }

    public void ejecutarStatement(String statement)throws SQLException{
        this.statement = conexion.createStatement();
        this.statement.executeUpdate(statement);
    }

    public ResultSet ejecutarQuery(String query, String cedula, String contrasena) throws SQLException {
        preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, cedula);
        preparedStatement.setString(2, contrasena);
        return preparedStatement.executeQuery();
    }
}
