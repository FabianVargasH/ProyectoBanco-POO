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

    //Usado en eliminacion de cuenta
    public void ejecutarStatement(String statement, String s1, String s2) throws SQLException {
        preparedStatement = conexion.prepareStatement(statement);
        preparedStatement.setString(1, s1);
        preparedStatement.setString(2, s2);
        preparedStatement.executeUpdate();
    }

    public void ejecutarStatement (String statement, double d, String s1, String s2) throws SQLException {
        preparedStatement = conexion.prepareStatement(statement);
        preparedStatement.setDouble(1, d);
        preparedStatement.setString(2, s1);
        preparedStatement.setString(3, s2);
        preparedStatement.executeUpdate();
    }

    public void ejecutarStatement (String statement, double d, String s1) throws SQLException {
        preparedStatement = conexion.prepareStatement(statement);
        preparedStatement.setDouble(1, d);
        preparedStatement.setString(2, s1);
        preparedStatement.executeUpdate();
    }

    public ResultSet ejecutarQuery(String query, String s1, String s2) throws SQLException {
        preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, s1);
        preparedStatement.setString(2, s2);
        return preparedStatement.executeQuery();
    }
    public ResultSet ejecutarQuery(String query, String s) throws SQLException {
        preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1,s);
        return preparedStatement.executeQuery();
    }


    public ResultSet ejecutarQuery(String query) throws SQLException {
        preparedStatement = conexion.prepareStatement(query);
        return preparedStatement.executeQuery();
    }
}
