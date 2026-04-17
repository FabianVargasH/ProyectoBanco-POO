package vargas.fabian.tl;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.logic.GestorCliente;
import vargas.fabian.bl.logic.GestorCuentaAhorro;
import vargas.fabian.bl.logic.GestorCuentaCredito;
import vargas.fabian.bl.logic.GestorCuentaDebito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;

public class Controlador {
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

    //Metodo para registar clientes
    public static void registrarCliente() throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n--Registro de cliente--");
        System.out.print("Nombre completo: ");
        String nombre = entrada.readLine();
        System.out.print("Cedula:");
        String cedula = entrada.readLine();
        LocalDate fecha = null;
        try{
            System.out.print("Fecha de nacimiento (formato yyyy-MM-dd):");
            fecha = LocalDate.parse(entrada.readLine());
        }catch(Exception e){
            System.out.println("Formato de fecha incorrecto.");
            return;
        }
        System.out.print("Ingrese su ocupacion: ");
        String ocupacion = entrada.readLine();
        System.out.print("Ingrese su residencia: ");
        String residencia = entrada.readLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = entrada.readLine();
        System.out.println(GestorCliente.registrarCliente(nombre, cedula, fecha, ocupacion, residencia, contrasena));
    }

    //Ingresar como cliente registrado a su cuenta
    public static Cliente ingresarCliente() throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n-- Ingreso como cliente --");
        System.out.print("Ingrese su cedula: ");
        String cedula = entrada.readLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = entrada.readLine();
        return GestorCliente.ingresarCliente(cedula, contrasena);
    }

    //Abrir cuenta especifica
    public static void abrirCuenta(Cliente cliente) throws IOException, SQLException, ClassNotFoundException {
        byte opcion;
        System.out.println("\n-- Apertura de Cuenta --");
        System.out.println("1. Ahorro");
        System.out.println("2. Crédito");
        System.out.println("3. Débito");
        System.out.print("Ingrese su elección: ");
        try{
            opcion = Byte.parseByte(entrada.readLine());
        }catch(Exception e){
            System.out.println("Sucedió un error, intente nuevamente");
            return;
        }
        if(opcion == 1){
            System.out.println("\n-- Apertura de cuenta de ahorro --");
            System.out.print("Ingrese el saldo inicial de la cuenta: ");
            double saldo = Double.parseDouble(entrada.readLine());
            System.out.print("Ingrese el porcentaje de interes de la cuenta: ");
            double porcentajeInteres = Double.parseDouble(entrada.readLine());
            System.out.println(GestorCuentaAhorro.abrirCuentaAhorro(cliente, saldo, porcentajeInteres));
        }else if(opcion == 2){
            System.out.println("-- Apertura de cuenta de crédito");
            System.out.print("Ingrese el saldo inicial de la cuenta: ");
            double saldo = Double.parseDouble(entrada.readLine());
            System.out.print("Ingrese la tasa de interes: ");
            double tasaInteres = Double.parseDouble(entrada.readLine());
            System.out.println(GestorCuentaCredito.abrirCuentaCredito(cliente,saldo,tasaInteres));
        }else if(opcion == 3){
            System.out.println("-- Apertura cuenta de débito --");
            System.out.print("Ingrese el saldo inicial de la cuenta: ");
            double saldo = Double.parseDouble(entrada.readLine());
            System.out.print("Ingrese el porcentaje de interes de la cuenta: ");
            double porcentajeInteres = Double.parseDouble(entrada.readLine());
            System.out.println(GestorCuentaDebito.abrirCuentaDebito(cliente,saldo,porcentajeInteres));
        }else{
            System.out.println("Opción inválida");
        }
    }

    //Retirar monto
    public static void retirar(Cliente cliente) throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n-- Retiro --");
        System.out.print("\nIngrese el ID de la cuenta: ");
        String id = entrada.readLine().toUpperCase();
        char tipo = id.charAt(0);
        if(tipo == 'C'){
            System.out.print("Ingrese el monto del cargo: ");
        }else{
            System.out.print("Ingrese el monto que desea retirar de la cuenta: ");
        }
        double monto = Double.parseDouble(entrada.readLine());
        switch(tipo){
            case 'A':
                System.out.println(GestorCuentaAhorro.retirar(cliente,id,monto));
                break;
            case 'C':
                System.out.println(GestorCuentaCredito.retirar(cliente,id,monto));
                break;
            case 'D':
                System.out.println(GestorCuentaDebito.retirar(cliente,id,monto));
                break;
            default:
                System.out.println("El ID es incorrecto. Debe comenzar con A, C ó D.");
                break;
        }
    }

    //Depositar o abonar monto en una cuenta
    public static void depositarAbonar(Cliente cliente) throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n--Deposito--");
        System.out.print("\nIngrese el ID de la cuenta a depositar: ");
        String idCuenta = entrada.readLine();
        char tipoCuenta = idCuenta.charAt(0);
        if(tipoCuenta != 'A' && tipoCuenta != 'D' && tipoCuenta != 'C'){
            System.out.println("El formato del ID de la cuenta a realizar la operación es inválido. Debe comenzar con A, C ó D");
            return;
        }
        if(tipoCuenta == 'C'){
            System.out.print("Ingrese el monto que desea abonar: ");
        }else{
            System.out.print("Ingrese el monto que desea depositar: ");
        }
        double monto = 0;
        try{
            monto = Double.parseDouble(entrada.readLine());
        }catch (NumberFormatException e){
            System.out.print("Formato de numero incorrecto, intente de nuevo: ");
        }
        switch(tipoCuenta){
            case 'A':
                System.out.println(GestorCuentaAhorro.depositar(cliente, idCuenta, monto));
                break;
            case 'D':
                System.out.println(GestorCuentaDebito.depositar(cliente, idCuenta, monto));
                break;
            case 'C':
                System.out.println(GestorCuentaCredito.abonar(cliente, idCuenta, monto));
                break;
            default:
                System.out.println("El formato del ID es invalido, debe comenzar con A, D ó C");
                break;
        }
    }

    //Generación de intereses
    public static void generarIntereses(Cliente cliente) throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n-- Generar Intereses --");
        System.out.print("Ingrese el ID de la cuenta: ");
        String id = entrada.readLine().toUpperCase();

        if(id.length() < 2){
            System.out.println("ID inválido");
            return;
        }

        char tipo = id.charAt(0);

        // Confirmar la operación
        System.out.print("¿Está seguro que desea generar intereses en la cuenta " + id + "? (S/N): ");
        String confirmacion = entrada.readLine();

        if(!confirmacion.equalsIgnoreCase("S")){
            System.out.println("Operación cancelada");
            return;
        }

        switch(tipo){
            case 'A':
                System.out.println(GestorCuentaAhorro.generarIntereses(cliente, id));
                break;
            case 'C':
                System.out.println(GestorCuentaCredito.generarIntereses(cliente, id));
                break;
            case 'D':
                System.out.println(GestorCuentaDebito.generarIntereses(cliente, id));
                break;
            default:
                System.out.println("ID inválido. Debe comenzar con A (Ahorro), C (Crédito) o D (Débito)");
                break;
        }
    }

    //Transferencia
    public static void transferir(Cliente cliente) throws Exception {
        System.out.println("\n--Transferencia--");
        System.out.print("\nIngrese el ID de la cuenta de origen: ");
        String idOrigen = entrada.readLine();
        char tipoOrigen = idOrigen.charAt(0);
        System.out.print("Ingrese el ID de la cuenta de destino: ");
        String idDestino = entrada.readLine().toUpperCase();
        char tipoDestino = idDestino.charAt(0);
        if(tipoDestino != 'A' && tipoDestino != 'D'){
            System.out.println("El formato del ID de la cuenta destino es inválido. Debe comenzar con A ó D");
            return;
        }
        System.out.print("\nIngrese el monto que desea transferir entre las cuentas: ");
        double monto = Double.parseDouble(entrada.readLine());
        switch (tipoOrigen){
            case 'A':
                System.out.println(GestorCuentaAhorro.transferir(cliente,idOrigen,idDestino, monto));
                break;
            case 'D':
                System.out.println(GestorCuentaDebito.transferir(cliente,idOrigen,idDestino, monto));
                break;
            default:
                System.out.println("El formato del ID de origen es inválido.  Debe comenzar con A ó D");
                break;
        }

    }

    //Ver cuentas del usuario
    public static void verCuentas(Cliente cliente) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("\n--Visualización de cuentas");
        GestorCuentaAhorro.verCuentas(cliente);
        GestorCuentaCredito.verCuentas(cliente);
        GestorCuentaDebito.verCuentas(cliente);
    }

    //Eliminar cuenta
    public static void eliminarCuenta(Cliente cliente) throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n--Eliminación de cuenta--");
        System.out.print("Ingrese el ID de la cuenta: ");
        String id = entrada.readLine().toUpperCase();
        char tipo = id.charAt(0);
        switch (tipo){
            case 'A':
                System.out.println(GestorCuentaAhorro.eliminar(cliente, id));
                break;
            case 'D':
                System.out.println(GestorCuentaDebito.eliminar(cliente, id));
                break;
            case 'C':
                System.out.println(GestorCuentaCredito.eliminar(cliente,id));
                break;
            default:
                System.out.println("El formato del ID es inválido. Debe comenzar con A, D ó C");
                break;
        }
    }
}
