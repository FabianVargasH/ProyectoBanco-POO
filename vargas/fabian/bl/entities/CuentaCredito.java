package vargas.fabian.bl.entities;

import vargas.fabian.dl.Conector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaCredito extends Cuenta {

    public CuentaCredito(double saldoInicial, double tasaInteres) throws SQLException, IOException, ClassNotFoundException {
        super(generarId(), tasaInteres, saldoInicial);
    }

    public CuentaCredito(String id, double saldoInicial, double tasaInteres){
        super(id, tasaInteres, saldoInicial, true);
    }

    private static int obtenerUltimoNumero() throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT id FROM t_cuenta_credito ORDER BY id DESC LIMIT 1;";
        ResultSet resultado = Conector.getConexion().ejecutarQuery(query);
        if(!resultado.next()) return 0;
        String id = resultado.getString("id");
        return Integer.parseInt(id.substring(3));
    }

    private static String generarId() throws SQLException, IOException, ClassNotFoundException {
        int ultimoNumero = obtenerUltimoNumero();
        return "CR-" + (ultimoNumero + 1);
    }


    protected void validarSaldo(double saldo){
        if(saldo>0){
            System.out.println("El saldo de la cuenta de credito debe ser $0 o negativo");
        }
    }

    //Retirar
    @Override
    public void retirar(double monto) throws Exception {
        if(monto <= 0){
            throw new Exception("El monto debe ser positivo");
        }
        setSaldo(getSaldo() + monto);
        System.out.println("Cargo de " + monto + " aplicado. \nSaldo (deuda): " + getSaldo());
    }

    //Abono
    public void abonar(double monto){
        if(monto <= 0){
            System.out.println("El monto debe ser mayor a 0");
            return;
        }
        if(monto > getSaldo()){
            System.out.println("Error: El abono de $" + monto + " excede la deuda actual de $" + getSaldo());
            System.out.println("El monto máximo a abonar es $" + getSaldo());
            return;
        }
        double nuevoSaldo = getSaldo() - monto;
        setSaldo(nuevoSaldo);
        System.out.println("Abono de $" + monto + " exitoso");
        System.out.println("Deuda restante: $" + getSaldo());
    }

    public String getOperacionesDisponibles(){
        return "1. Retirar\n3. Abonar\n5. Generar intereses";
    }

    public void generarIntereses(){
        double intereses = getSaldo() * (getTasaInteres()/100);
        setSaldo(getSaldo()+intereses);
        System.out.println("Intereses de: "  +intereses + "\nNuevo Saldo: " + getSaldo());
    }

    public String getTipoCuenta(){
        return "CREDITO";
    }
}
