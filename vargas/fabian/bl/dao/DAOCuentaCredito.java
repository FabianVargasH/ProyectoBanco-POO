package vargas.fabian.bl.dao;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaCredito;
import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCuentaCredito {
    private static String statement;
    private static String query;

    //Crear cuenta credito nueva
    public static String insertarCuentaCredito(Cliente cliente, CuentaCredito cuenta) throws SQLException, IOException, ClassNotFoundException, SQLException, IOException {
        statement = "INSERT INTO t_cuenta_credito VALUES ('" + cuenta.getId() + "', '" +
                cliente.getCedula() + "', " +
                cuenta.getSaldo() + ", " +
                cuenta.getTasaInteres() + ");";
        Conector.getConexion().ejecutarStatement(statement);
        return "Cuenta de crédito abierta exitosamente. ID: " + cuenta.getId();
    }

    //Realizar retiro de monto
    public static String actualizarRetiro(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_credito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "El cliente no tiene una cuenta de crédito con ese ID.";
        }

        CuentaCredito cuentaCredito = new CuentaCredito(resultado.getString("id"), resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes"));
        try{
            cuentaCredito.retirar(monto);
        }catch (Exception e){
            return "Transacción fallida. Volviendo al menú de cliente...";
        }

        statement = "UPDATE t_cuenta_credito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuentaCredito.getSaldo(), id, cliente.getCedula());
        return "Cargo realizado exitosamente. Nuevo saldo (deuda): " + cuentaCredito.getSaldo();
    }

    public static String actualizarAbono(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_credito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        CuentaCredito cuenta = new CuentaCredito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );
        cuenta.abonar(monto);
        statement = "UPDATE t_cuenta_credito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());
        return "Abono realizado exitosamente. Nuevo saldo: " + cuenta.getSaldo();
    }

    // Generar intereses
    public static String actualizarIntereses(Cliente cliente, String id)
            throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_credito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());

        if(!resultado.next()){
            return "No se encuentra una cuenta de crédito con ese ID asociada al cliente";
        }

        CuentaCredito cuenta = new CuentaCredito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );

        double deudaAnterior = cuenta.getSaldo();
        cuenta.generarIntereses();
        double interesesGenerados = cuenta.getSaldo() - deudaAnterior;

        statement = "UPDATE t_cuenta_credito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());

        return "Intereses generados exitosamente. Intereses aplicados: " + interesesGenerados + " | Deuda anterior: " + deudaAnterior + " | Deuda actual: " + cuenta.getSaldo();
    }

    public static void leerCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_credito WHERE cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query,cliente.getCedula());
        if(!resultado.next()){
            System.out.println("El cliente no tiene una cuenta de crédito");
            return;
        }
        do{
            System.out.println("─────────────────────────────────────");
            System.out.println("ID: " + resultado.getString("id"));
            System.out.println("Saldo: " +  resultado.getDouble("saldo"));
            System.out.println("Tasa de interés: " + resultado.getDouble("tasa_interes") + "%");
            System.out.println("─────────────────────────────────────");
        }while(resultado.next());
    }

    public static String eliminarCuenta(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_credito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        statement = "DELETE FROM t_cuenta_credito WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, id, cliente.getCedula());
        return "Cuenta eliminada exitosamente";
    }
}
