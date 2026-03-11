package vargas.fabian.bl;
public class CuentaCredito extends Cuenta {
  
    public CuentaCredito(double saldoInicial, double tasaInteres){
        super("CR", saldoInicial, tasaInteres);
    }

    protected void validarSaldo(double saldo){
        if(saldo>0){
            System.out.println("El saldo de la cuenta de credito debe ser $0 o negativo");
            return;
        }
    }

    //Retirar
    public void retirar(double monto){
        if(monto <= 0){
            System.out.println("El monto debe ser positivo");
            return;
        }
        setSaldo(getSaldo()-monto);
        System.out.println("Cargo de $ "  + monto + " aplicado. \n Saldo: $" + getSaldo());
    }

    //Abono
    public void abonar(double monto){
        if(monto<= 0){
            System.out.println("El monto debe ser mayor a 0 ");
            return;
        }
        double nuevoSaldo = getSaldo() + monto;
        if(nuevoSaldo >0){
            System.out.println("El abono de $: "  + monto + " excede la deuda actual de $: "  + getSaldo());
        }
        setSaldo(nuevoSaldo);
        System.out.println("Abono de: $ " + monto + " exitoso \nSaldo: $" + getSaldo());
    }

    public String getOperacionesDisponibles(){
        return "1. Retirar\n3. Abonar\n5Generar intereses";
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
