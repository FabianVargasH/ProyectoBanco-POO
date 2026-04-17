package vargas.fabian.bl.logic;

import vargas.fabian.bl.dao.DAOCuentaAhorro;
import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaAhorro;

import java.io.IOException;
import java.sql.SQLException;

public class GestorCuentaAhorro {

    public static String abrirCuentaAhorro(Cliente cliente, double saldo, double porcentajeInteres) throws SQLException, IOException, ClassNotFoundException {
        CuentaAhorro cuenta = new CuentaAhorro(saldo, porcentajeInteres);
        return DAOCuentaAhorro.insertarCuentaAhorro(cliente, cuenta);
    }

    public static String retirar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaAhorro.actualizarRetiro(cliente, id, monto);
    }

    public static String depositar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaAhorro.actualizarDeposito(cliente, id, monto);
    }

    public static String generarIntereses(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaAhorro.actualizarIntereses(cliente, id);
    }

    public static String transferir(Cliente cliente, String idOrigen, String idDestino, double monto) throws Exception {
        return DAOCuentaAhorro.actualizarTransferencia(cliente, idOrigen, idDestino, monto);
    }

    public static void verCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        DAOCuentaAhorro.leerCuentas(cliente);
    }

    public static String eliminar(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaAhorro.eliminarCuenta(cliente, id);
    }

}
