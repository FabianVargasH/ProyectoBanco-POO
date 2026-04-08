package vargas.fabian.bl.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente{
    private String nombreCompleto;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String residencia;
    private String contrasena;

    //Cuentas que puede tener el cliente
    private ArrayList<CuentaAhorro> cuentaAhorros;
    private ArrayList<CuentaCredito> cuentaCredito;
    private ArrayList<CuentaDebito> cuentaDebito;

    public Cliente(){}

    public Cliente(String nombreCompleto, String cedula, LocalDate fechaNacimiento, String ocupacion, String residencia, String contrasena){
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.ocupacion = ocupacion;
        this.residencia = residencia;
        this.contrasena = contrasena;
        this.cuentaAhorros = new ArrayList<>();
        this.cuentaDebito = new ArrayList<>();
        this.cuentaCredito = new ArrayList<>();
    }

    public CuentaAhorro abrirCuentaAhorros(double saldoInicial, double tasaInteres){
        CuentaAhorro cuenta = new CuentaAhorro(saldoInicial, tasaInteres);
        cuentaAhorros.add(cuenta);
        System.out.println("Cuenta de ahorros abierta: " + cuenta.getId());
        return cuenta;
    }

    public CuentaCredito abrirCuentaCredito(double tasaInteres){
        CuentaCredito cuenta = new CuentaCredito(0.0, tasaInteres);
        cuentaCredito.add(cuenta);
        System.out.println("Cuenta de ahorros abierta: " + cuenta.getId());
        return cuenta;
    }

    public CuentaDebito abrirCuentaDebito(double saldoInicial, double tasaInteres){
        CuentaDebito cuenta = new CuentaDebito(saldoInicial, tasaInteres);
        cuentaDebito.add(cuenta);
        System.out.println("Cuenta Debito abierta: " + cuenta.getId());
        return cuenta;
    }

    public Cuenta buscarCuentaPorID(String id){
        for(CuentaAhorro c: cuentaAhorros){
            if(c.getId().equals(id)){
                return c;
            }
        }
        for(CuentaCredito c: cuentaCredito){
            if(c.getId().equals(id)){
                return c;
            }
        }
        for(CuentaDebito c: cuentaDebito){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public boolean autenticar (String contrasena){
        return this.contrasena.equals(contrasena);
    }

    //Getters
    public String getNombreCompleto() {
         return nombreCompleto; }
    public String getCedula() {
         return cedula; }
    public LocalDate getFechaNacimiento() {
         return fechaNacimiento; }
    public String getOcupacion() {
         return ocupacion; }
    public String getResidencia() {
         return residencia; }
    public String getContrasena() {
        return contrasena; }
    public List<CuentaAhorro> getCuentasAhorros() {
         return cuentaAhorros; }
    public List<CuentaCredito> getCuentasCredito() {
         return cuentaCredito; }
    public List<CuentaDebito> getCuentasDebito() {
         return cuentaDebito; }

    public void mostrarCuentas(){
        System.out.println("\n---Cuenta de " + nombreCompleto + "---");
        if(cuentaAhorros.isEmpty() && cuentaCredito.isEmpty() && cuentaDebito.isEmpty()){
            System.out.println("Sin cuentas registradas;");
            return;
        }
        for (CuentaAhorro c : cuentaAhorros) {
            System.out.println("  " + c);
        }

        for (CuentaCredito c : cuentaCredito) {
            System.out.println("  " + c);
        }

        for (CuentaDebito c : cuentaDebito) {
            System.out.println("  " + c);
        }
    }

    @Override
    public String toString(){
        return "\nCliente: " + nombreCompleto + 
               "\nCedula: " + cedula + 
               "\nNacimiento: " + fechaNacimiento + 
               "\nOcupacion: " + ocupacion +
               "\nResidencia: " + residencia;
    }
}