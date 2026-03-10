package vargas.fabian.bl;

public abstract class Cuenta {
    protected double saldo;
    protected final String id;
    protected double tasaInteres;
    private static int contador = 1;

    public Cuenta(String prefijo, double tasaInteres, double saldoInicial){
        this.id = prefijo + "-" + contador++;
        this.tasaInteres = tasaInteres;
        this.saldo = saldoInicial;
    }

    //Metodos
    protected abstract void validarSaldo(double saldo);
    public abstract void retirar(double monto);
    public abstract void generarIntereses();
    public abstract String getTipoCuenta();


    protected void setSaldo(double saldo){
        this.saldo = saldo;
    }

    public double getSaldo(){
        return saldo;
    }

    public String getId(){
        return id;
    }

    public double getTasaInteres(){
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres){
        this.tasaInteres = tasaInteres;
    }

    public String toString(){
        return 
        "Tipo de cuenta: " + getTipoCuenta() + 
        "Id: " + id + 
        "Saldo: " + saldo + 
        "Interés: " + tasaInteres; 

    }
}