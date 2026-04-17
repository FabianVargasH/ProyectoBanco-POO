package vargas.fabian.bl.entities;

import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaDebito extends Cuenta {

    public CuentaDebito(double saldoInicial, double tasaInteres)
            throws SQLException, IOException, ClassNotFoundException {
        super(generarId(), tasaInteres, saldoInicial);
    }

    public CuentaDebito(String id, double saldoInicial, double tasaInteres){
        super(id, tasaInteres, saldoInicial, true);
    }

    private static int obtenerUltimoNumero() throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT id FROM t_cuenta_debito ORDER BY id DESC LIMIT 1;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query);
        if(!resultado.next()) return 0;
        String id = resultado.getString("id");
        return Integer.parseInt(id.substring(3));
    }

    private static String generarId() throws SQLException, IOException, ClassNotFoundException {
        int ultimoNumero = obtenerUltimoNumero();
        return "DB-" + (ultimoNumero + 1);
    }

    @Override
    protected void validarSaldo(double saldo){
        if(saldo<0){
            System.out.println("ERROR | Cuenta Debito: el saldo debe ser igual o mayor 0. \nSaldo: " + saldo);
        }
    }

    @Override
    public void retirar(double monto) throws Exception {
        if(monto <= 0){
            throw new Exception("ERROR, el monto debe ser mayor a 0");
        }
        double nuevoSaldo = getSaldo() - monto;
        if(nuevoSaldo < 0){
            throw new Exception("Saldo insuficiente. Saldo actual: " + getSaldo() +
                    ", Intento retirar: " + monto);
        }
        setSaldo(nuevoSaldo);
        System.out.println("Retiro de: " + monto + "\nNuevo saldo: " + getSaldo());
    }

    public void depositar(double monto){
        if(monto <=0){
            System.out.println("ERROR, el monto debe ser positivo");
            return;
        }
        setSaldo(getSaldo() + monto);
        System.out.println("Deposito de: " + monto + " exitoso \nNuevo Saldo: " + getSaldo());
    }

    public void depositarTransferencia(double monto){
        if(monto <= 0){
            return;
        }
        setSaldo(getSaldo() + monto);
    }
    
   public String getOperacionesDisponibles(){
        return "1. Retirar\n2. Depositar\n5. Generar intereses";
    }

    @Override
    public void generarIntereses(){
        double interes = getSaldo() * (getTasaInteres() /100);
        setSaldo(getSaldo() + interes);
        System.out.println("Interes generados: " + interes + "\nNuevo saldo: " + getSaldo());
    }

    @Override
    public String getTipoCuenta(){
        return "DEBITO";
    }
}