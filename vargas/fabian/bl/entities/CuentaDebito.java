package vargas.fabian.bl.entities;

public class CuentaDebito extends Cuenta {

    public CuentaDebito(double saldoInicial, double tasaInterese){
        super("DB", saldoInicial, tasaInterese);
    }

    @Override
    protected void validarSaldo(double saldo){
        if(saldo<0){
            System.out.println("ERROR | Cuenta Debito: el saldo debe ser igual o mayor 0. \nSaldo: " + saldo);
            return;
        }
    }

    @Override
    public void retirar(double monto){
        if(monto<= 0){
            System.out.println("ERROR, el monto debe ser mayor a 0;");
            return;
        }
        double nuevoSaldo = getSaldo() - monto;
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