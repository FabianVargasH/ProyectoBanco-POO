package vargas.fabian.bl.dao;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaAhorro;
import vargas.fabian.bl.entities.CuentaDebito;
import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCuentaAhorro {
    private static String statement;
    private static String query;


    //Crear cuenta ahorro
    public static String insertarCuentaAhorro(Cliente cliente, CuentaAhorro cuenta) throws SQLException, IOException, ClassNotFoundException {
        statement = "INSERT INTO t_cuenta_ahorro VALUES ('" + cuenta.getId() + "', '" +
                cliente.getCedula() + "', " +
                cuenta.getSaldo() + ", " +
                cuenta.getTasaInteres() + ", " +
                CuentaAhorro.SALDO_MINIMO + ");";
        Conector.getConexion().ejecutarStatement(statement);
        return "Cuenta de ahorro abierta exitosamente. ID: " + cuenta.getId();
    }

    //Retirar monto de cuenta
    public static String actualizarRetiro(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "El cliente no tiene una cuenta de ahorro con ese ID.";
        }

        CuentaAhorro cuentaAhorro = new CuentaAhorro(resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes"));
        try{
            cuentaAhorro.retirar(monto); // Solo una llamada
        }catch (Exception e){
            return "Transacción fallida: " + e.getMessage();
        }

        statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuentaAhorro.getSaldo(), id, cliente.getCedula());
        return "El retiro se realizó exitosamente. Nuevo saldo: " + cuentaAhorro.getSaldo();
    }

    //Deposito
    public static String actualizarDeposito(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        CuentaAhorro cuenta = new CuentaAhorro(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );
        cuenta.depositar(monto);
        statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());
        return "Depósito realizado exitosamente. Nuevo saldo: " + cuenta.getSaldo();
    }

    // Generar intereses
    public static String actualizarIntereses(Cliente cliente, String id)
            throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());

        if(!resultado.next()){
            return "No se encuentra una cuenta de ahorro con ese ID asociada al cliente";
        }

        CuentaAhorro cuenta = new CuentaAhorro(
                resultado.getString("id"),
                resultado.getDouble("saldo"),
                resultado.getDouble("tasa_interes")
        );

        double saldoAnterior = cuenta.getSaldo();
        cuenta.generarIntereses();
        double interesesGenerados = cuenta.getSaldo() - saldoAnterior;

        statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, cuenta.getSaldo(), id, cliente.getCedula());

        return "Intereses generados exitosamente. Intereses: " + interesesGenerados + " | Saldo anterior: " + saldoAnterior + " | Nuevo saldo: " + cuenta.getSaldo();
    }

    //Transferencia de cuenta ahorro a otra cuenta
    public static String actualizarTransferencia(Cliente cliente, String idOrigen, String idDestino, double monto) throws Exception {
        query = "SELECT * FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, idOrigen, cliente.getCedula());

        if(!resultado.next()){
            return "El cliente emisor no tiene una cuenta de ahorro con ese ID.";
        }
        CuentaAhorro cuentaOrigen = new CuentaAhorro(
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

            statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
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

            statement = "UPDATE t_cuenta_ahorro SET saldo = ? WHERE id = ? AND cliente_cedula = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaOrigen.getSaldo(), idOrigen, cliente.getCedula());

            statement = "UPDATE t_cuenta_debito SET saldo = ? WHERE id = ?;";
            Conector.getConexion().ejecutarStatement(statement, cuentaDestinoD.getSaldo(), idDestino);
        }

        return "La transferencia se realizó exitosamente. Nuevo saldo origen: " + cuentaOrigen.getSaldo();
    }

    //Ver cuentas del usuario
    public static void leerCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_ahorro WHERE cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query,cliente.getCedula());
        if(!resultado.next()){
            System.out.println("El cliente no tiene una cuenta de ahorro");
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

    //Eliminar Cuenta
    public static String eliminarCuenta(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        query = "SELECT * FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query, id, cliente.getCedula());
        if(!resultado.next()){
            return "No se encuentra una cuenta con ese ID asociada al cliente";
        }
        statement = "DELETE FROM t_cuenta_ahorro WHERE id = ? AND cliente_cedula = ?;";
        Conector.getConexion().ejecutarStatement(statement, id, cliente.getCedula());
        return "Cuenta eliminada exitosamente";
    }
}
