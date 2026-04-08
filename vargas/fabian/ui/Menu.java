package vargas.fabian.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import vargas.fabian.bl.*;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.entities.Cuenta;
import vargas.fabian.tl.Controlador;

public class Menu {
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static void menuPrincipal() throws IOException, SQLException, ClassNotFoundException {
        boolean continuar = true;
        while(continuar){
            System.out.println("\n--Menu Principal--");
            System.out.println("1.Registrar cliente");
            System.out.println("2. Acceder como cliente");
            System.out.println("0. Salir");
            System.out.println("Seleccione una opcion");
            int opcion = Integer.parseInt(entrada.readLine());
            switch(opcion){
                case 1:
                    Controlador.registrarCliente();
                    break;
                case 2:
                    Cliente cliente = Controlador.ingresarCliente();
                    if(Controlador.ingresarCliente() == null){
                        System.out.println("Auntentificacion inválida");
                        continue;
                    }
                    break;
                case 0:
                    System.out.println("Saliendo del menú...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }
    }
    private static void registrarCliente()throws IOException{
        System.out.println("\n--Registro de cliente--");
        System.out.print("Nombre completo: ");
        String nombre = entrada.readLine();
        System.out.print("\nCedula:");
        String cedula = entrada.readLine();
        LocalDate fecha = LocalDate.parse(entrada.readLine());
        System.out.println("\nIngrese su ocupacion");
        String ocupacion = entrada.readLine();
        System.out.print("\nIngrese su residencia");
        String residencia = entrada.readLine();
        System.out.print("Ingrese su contraseña:");
        String contrasena = entrada.readLine();
        Cliente cliente = new Cliente(nombre, cedula, fecha, ocupacion, residencia, contrasena);
    }
    private static void menuCliente(Cliente cliente)throws IOException{
        int opcion;
        System.out.println("-- Menu de cliente --");
        System.out.println("1. Abrir una cuenta");
        System.out.println("2. Hacer un retiro");
        System.out.println("3. Hacer un depósito o abono");
        System.out.println("4. Generar intereses");
        System.out.println("5. Hacer una transferencia");
        System.out.println("6. Ver todas las cuentas");
        System.out.println("7. Eliminar una cuenta");
        System.out.println("0. Salir");
        System.out.print("Ingrese su elección: ");
        opcion = Integer.parseInt(entrada.readLine());
        switch (opcion){
            case 1:
                abrirCuenta(cliente);
                break;
            case 2:
                hacerRetiro
        }
    }

    private static void abrirCuenta(Cliente cliente)throws IOException{
        System.out.println("\n--Abrir Cuenta--");
        System.out.print("\n1. Ahorros (saldo > 100");
        System.out.print("\n2. Credito (saldo 0 o negativo");
        System.out.print("\n3. Debito (Saldo 0 o positivo");
        int tipo = Integer.parseInt(entrada.readLine());
        System.out.print("\nTasa de interes (%): ");
        double tasa = Double.parseDouble(entrada.readLine());
        double saldo = 0.0;
        try{
            switch(tipo){
                case 1:
                    System.out.print("\nSaldo inicial (>100): ");
                    saldo = Double.parseDouble(entrada.readLine());
                    cliente.abrirCuentaAhorros(saldo, tasa);
                    break;
                case 2:
                    cliente.abrirCuentaCredito(tasa);
                    break;
                case 3:
                    System.out.println("Saldo inicial (0 o positivo)");
                    saldo = Double.parseDouble(entrada.readLine());
                    cliente.abrirCuentaDebito(saldo, tasa);
                    break;
                default:
                    System.out.println("Tipo invalido");
            }
        }catch(IllegalArgumentException e){
            System.out.println("Error, argumento ingresado invalido");
        }
    }

    private void realizarTransaccion(Cliente cliente)throws IOException{
        cliente.mostrarCuentas();
        System.out.println("\nIngrese el ID de la cuenta: ");
        String id = entrada.readLine();
        Cuenta cuenta = cliente.buscarCuentaPorID(id);
        if(cuenta == null){
            System.out.println("\nCuenta no encontrada");
            return;
        }
        System.out.println("\nCuenta seleccionada: " + cuenta);
        mostrarOperacionesDisponibles(cuenta);
        System.out.print("Operacion: ");
        int opcion = Integer.parseInt(entrada.readLine());

        try{
            ejecutarOperacion(cuenta, opcion, cliente);
        }catch (IllegalArgumentException e){
            System.out.println("Error, argumento ingresado invalido");
        }
    }

    private void mostrarOperacionesDisponibles(Cuenta cuenta) {
        System.out.println("\n--- Operaciones---");
        System.out.println(cuenta.getOperacionesDisponibles());
    }

    private void ejecutarOperacion(Cuenta cuenta, int opcion, Cliente cliente)throws IOException{
        double monto = 0;
        switch(opcion){
            case 1:
                System.out.print("Monto a retirar: ");
                monto = Double.parseDouble(entrada.readLine());
                cuenta.retirar(monto);
                break;
            case 2:
                System.out.println("Monto a depositar: ");
                monto =Double.parseDouble(entrada.readLine());
                cuenta.depositar(monto);
                break;
            case 3:
                System.out.println("Monto a abonar: ");
                monto = Double.parseDouble(entrada.readLine());
                break;
            case 4:
                System.out.println("Transferencias aun no implementadas");
                break;
            case 5: 
                cuenta.generarIntereses();
                break;
            default:
                System.out.println("Operacion invalida");
                break;
        }
    }
}
