package vargas.fabian.bl.entities;

public class CuentaAhorro extends Cuenta {

    private static final double  SALDO_MINIMO = 100.0;

    public CuentaAhorro(double saldoInicial, double tasaInteres){
        super("AH",saldoInicial,tasaInteres);
    }

    @Override
    protected void validarSaldo(double saldo){
        if(saldo <= SALDO_MINIMO){
            System.out.println("El saldo de la CUENTA DE AHORROS deber ser mayor a " + SALDO_MINIMO + "\nSaldo: " + saldo);
            return;
        }
    }

    @Override
    public void retirar(double monto){
        if(monto <= 0){
            System.out.println("El monto ingresado debe ser mayor a 0");
            return;
        }
        double nuevoSaldo = getSaldo() - monto;
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
