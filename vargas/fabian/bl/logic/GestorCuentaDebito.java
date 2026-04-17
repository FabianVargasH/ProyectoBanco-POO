package vargas.fabian.bl.logic;

import vargas.fabian.bl.dao.DAOCuentaAhorro;
import vargas.fabian.bl.dao.DAOCuentaCredito;
import vargas.fabian.bl.dao.DAOCuentaDebito;
import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaDebito;

import java.io.IOException;
import java.sql.SQLException;

public class GestorCuentaDebito {

    //Crear cuenta
    public static String abrirCuentaDebito(Cliente cliente, double saldo, double porcentajeInteres) throws SQLException, IOException, ClassNotFoundException {
        CuentaDebito cuenta = new CuentaDebito(saldo, porcentajeInteres);
        return DAOCuentaDebito.insertarCuentaDebito(cliente, cuenta);
    }

    //Retiro
    public static String retirar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaDebito.actualizarRetiro(cliente, id, monto);
    }

    //Deposito
    public static String depositar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaDebito.actualizarDeposito(cliente, id, monto);
    }

    // Generar intereses
    public static String generarIntereses(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaDebito.actualizarIntereses(cliente, id);
    }

    //Transferencia
    public static String transferir(Cliente cliente, String idOrigen, String idDestino, double monto) throws Exception {
        return DAOCuentaDebito.actualizarTransferencia(cliente,idOrigen,idDestino,monto);
    }

    public static void verCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        DAOCuentaDebito.leerCuentas(cliente);
    }

    public static String eliminar(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaDebito.eliminarCuenta(cliente, id);
    }
}