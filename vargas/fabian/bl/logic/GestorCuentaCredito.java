package vargas.fabian.bl.logic;

import vargas.fabian.bl.dao.DAOCuentaAhorro;
import vargas.fabian.bl.dao.DAOCuentaCredito;
import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.CuentaCredito;

import java.io.IOException;
import java.sql.SQLException;

public class GestorCuentaCredito {

    //Crear cuenta credito
    public static String abrirCuentaCredito(Cliente cliente, double saldo, double tasaInteres) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaCredito.insertarCuentaCredito(cliente, new CuentaCredito(saldo, tasaInteres));
    }

    //Retiro
    public static String retirar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaCredito.actualizarRetiro(cliente, id, monto);
    }

    //Abono
    public static String abonar(Cliente cliente, String id, double monto) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaCredito.actualizarAbono(cliente, id, monto);
    }

    //Generar intereses
    public static String generarIntereses(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaCredito.actualizarIntereses(cliente, id);
    }

    //Ver cuentas creadas
    public static void verCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        DAOCuentaCredito.leerCuentas(cliente);
    }

    //Eliminar cuenta
    public static String eliminar(Cliente cliente, String id) throws SQLException, IOException, ClassNotFoundException {
        return DAOCuentaCredito.eliminarCuenta(cliente, id);
    }
}