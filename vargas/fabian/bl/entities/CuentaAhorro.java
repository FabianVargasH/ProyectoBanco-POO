package vargas.fabian.bl.entities;

import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaAhorro extends Cuenta {

    public static final double  SALDO_MINIMO = 100.0;

    public CuentaAhorro(double saldoInicial, double tasaInteres) throws SQLException, IOException, ClassNotFoundException {
        super(generarId(), tasaInteres, saldoInicial);
    }

    public CuentaAhorro(String id, double saldoInicial, double tasaInteres){
        super(id, tasaInteres, saldoInicial, true);
    }

    private static int obtenerUltimoNumero() throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT id FROM t_cuenta_ahorro ORDER BY id DESC LIMIT 1;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query);
        if(!resultado.next()) return 0;
        String id = resultado.getString("id");
        return Integer.parseInt(id.substring(3));
    }

    private static String generarId() throws SQLException, IOException, ClassNotFoundException {
        int ultimoNumero = obtenerUltimoNumero();
        int nuevoNumero = ultimoNumero + 1;
        return "AH-" + nuevoNumero;
    }

    @Override
    protected void validarSaldo(double saldo){
        if(saldo <= SALDO_MINIMO){
            System.out.println("El saldo de la CUENTA DE AHORROS deber ser mayor a " + SALDO_MINIMO + "\nSaldo: " + saldo);
        }
    }

    @Override
    public void retirar(double monto) throws Exception {
        if(monto <= 0){
            throw new Exception("El monto ingresado debe ser mayor a 0");
        }
        double nuevoSaldo = getSaldo() - monto;
        if(nuevoSaldo < SALDO_MINIMO){
            throw new Exception("El saldo no puede ser menor a " + SALDO_MINIMO +
                    ". Saldo actual: " + getSaldo() + ", Intento retirar: " + monto);
        }
        setSaldo(nuevoSaldo);
        System.out.println("Retiro exitoso de " + monto + "\nNuevo saldo: " + getSaldo());
    }

    public void depositar(double monto){
        if(monto<=0){
            System.out.println("El monto ingresado debe ser mayor a 0");
            return;
        }
        setSaldo(getSaldo() + monto);
        System.out.println("Deposito de " + monto + " realizado con exito. \nNuevo Saldo: " + getSaldo());
    }

    public void depositarTransferencia(double monto){
        if(monto <= 0){
            return;
        }
        setSaldo(getSaldo() + monto);
    }

    @Override
    public void generarIntereses(){
        double intereses = getSaldo() * (getTasaInteres()/100);
        setSaldo(getSaldo() + intereses);
        System.out.println("Intereses generados: " + intereses + "Nuevo saldo " + getSaldo());
    }

    public String getOperacionesDisponibles(){
        return "1. Retirar\n2. Depositar\n5. Generar intereses";
    }

    @Override
    public String getTipoCuenta(){
        return "AHORROS";
    }
}
