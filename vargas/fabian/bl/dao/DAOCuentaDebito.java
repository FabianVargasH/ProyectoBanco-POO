package vargas.fabian.bl.dao;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaAhorro;
import vargas.fabian.bl.entities.CuentaDebito;
import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCuentaDebito {
    private static String statement;
    private static String query;

    //Crear nueva cuenta debito
    public static String insertarCuentaDebito(Cliente cliente, CuentaDebito cuenta) throws SQLException, IOException, ClassNotFoundException {
        statement = "INSERT INTO t_cuenta_debito VALUES ('" + cuenta.getId() + "', '" +
                cliente.getCedula() + "', " +
                cuenta.getSaldo() + ", " +
                cuenta.getTasaInteres() + ");";
        Conector.getConexion().ejecutarStatement(statement);
        return "Cuenta de débito abierta exitosamente. ID: " + cuenta.getId();
    }

    //Retiro de monto de cuenta
    public static String actualizarRetiro(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "El cliente no tiene una cuenta de débito con ese ID.";
        }

        CuentaDebito cuentaDebito = new CuentaDebito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes"));
        try{
            cuentaDebito.retirar(monto);
        }catch (Exception e){
            return "Transacción fallida. Volviendo al menú de cliente...";
        }

        statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuentaDebito.getSaldo(), id, cliente.getCedula());
        return "El retiro se realizó exitosamente. Nuevo saldo: " + cuentaDebito.getSaldo();
    }

    //Deposito
    public static String actualizarDeposito(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        CuentaDebito cuenta = new CuentaDebito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );
        cuenta.depositar(monto);
        statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());
        return "Depósito realizado exitosamente. Nuevo saldo: " + cuenta.getSaldo();
    }

    // Generar intereses en cuenta de débito
    public static String actualizarIntereses(Cliente cliente, String id)
            throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());

        if(!resultado.next()){
            return "No se encuentra una cuenta de débito con ese ID asociada al cliente";
        }

        CuentaDebito cuenta = new CuentaDebito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );

        double saldoAnterior = cuenta.getSaldo();
        cuenta.generarIntereses();
        double interesesGenerados = cuenta.getSaldo() - saldoAnterior;

        statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());

        return "Intereses generados exitosamente. Intereses: " + interesesGenerados + " | Saldo anterior: " + saldoAnterior + " | Nuevo saldo: " + cuenta.getSaldo();
    }

    //Transferencia
    public static String actualizarTransferencia(Cliente cliente, String idOrigen, String idDestino, double monto) throws Exception {
        query = "SELECT * FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, idOrigen, cliente.getCedula());
        if(!resultado.next()){
            return "El cliente emisor no tiene una cuenta de débito con ese ID.";
        }
        CuentaDebito cuentaOrigen = new CuentaDebito(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );
        if(idDestino.charAt(0) == 'A') {
            query = "SELECT * FROM t_cuenta_ahorro WHERE id = ?;";
        } else {
            query = "SELECT * FROM t_cuenta_debito WHERE id = ?;";
        }
        resultado = Conector.getConexion().ejecutarQuery(query, idDestino);

        if(!resultado.next()){
            return "No se encuentra una cuenta receptora con ese ID";
        }

        cuentaOrigen.retirar(monto);
        if(idDestino.charAt(0) == 'A'){
            CuentaAhorro cuentaDestinoA = new CuentaAhorro(
                    resultado.getString("id"),
                    resultado.getDouble("saldo"),
                    resultado.getDouble("tasa_interes")
            );
            cuentaDestinoA.depositarTransferencia(monto);

            statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaOrigen.getSaldo(), idOrigen, cliente.getCedula());

            statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaDestinoA.getSaldo(), idDestino);
        } else {
            CuentaDebito cuentaDestinoD = new CuentaDebito(
                    resultado.getString("id"),
                    resultado.getDouble("saldo"),
                    resultado.getDouble("tasa_interes")
            );
            cuentaDestinoD.depositarTransferencia(monto);

            statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaOrigen.getSaldo(), idOrigen, cliente.getCedula());

            statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaDestinoD.getSaldo(), idDestino);
        }
        return "La transferencia se realizó exitosamente. Nuevo saldo origen: " + cuentaOrigen.getSaldo();
    }

    //Ver cuentas
    public static void leerCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_debito WHERE cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query,cliente.getCedula());
        if(!resultado.next()){
            System.out.println("El cliente no tiene una cuenta de debito");
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

    //Eliminar cuenta
    public static String eliminarCuenta(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        statement = "DELETE FROM t_cuenta_debito WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, id, cliente.getCedula());
        return "Cuenta eliminada exitosamente";
    }
}