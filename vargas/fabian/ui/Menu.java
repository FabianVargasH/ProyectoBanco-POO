package vargas.fabian.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import vargas.fabian.bl.*;

public class Menu {
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    private Banco banco;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public Menu(Banco banco) {
        this.banco = banco;
    }

    public void iniciar() throws IOException{
        System.out.println("= = = = = = = = = = = ");
        System.out.println("Bienvenido al sistema del " + banco.getNombre());
        System.out.println("= = = = = = = = = = = ");

        boolean continuar = true;
        while(continuar){
            mostrarMenuPrincipal();
            System.out.println("Seleccione una opcion");
            int opcion = Integer.parseInt(entrada.readLine());
            switch(opcion){
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    menuCliente();
                    break;
                case 3:
                    banco.listarClientes();
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

    public void mostrarMenuPrincipal(){
        System.out.println("\n--Menu Principal--");
        System.out.println("1.Registrar cliente");
        System.out.println("2. Acceder como cliente");
        System.out.println("3. Listar clientes");
        System.out.println("0. Salir");
    }

    //metodo para leer fechas de forma validada
    private LocalDate leerFecha(String dato) {
        while (true) {
            try {
                System.out.print(dato);
                return LocalDate.parse(entrada.readLine().trim(), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use dd/MM/yyyy.");
            } catch (IOException e) {
                System.out.println("  Error de lectura: " + e.getMessage());
            }
        }
    }

    private void registrarCliente()throws IOException{
        System.out.println("\n--Registro de cliente--");
        System.out.print("Nombre completo: ");
        String nombre = entrada.readLine();
        System.out.print("\nCedula:");
        String cedula = entrada.readLine();
        LocalDate fecha = leerFecha("Fecha de nacimiento (yyyy/MM/dd): ");
        System.out.println("\nIngrese su ocupacion");
        String ocupacion = entrada.readLine();
        System.out.print("\nIngrese su residencia");
        String residencia = entrada.readLine();
        System.out.print("Ingrese su contraseña:");
        String contrasena = entrada.readLine();

        Cliente cliente = new Cliente(nombre, cedula, fecha, ocupacion, residencia, contrasena);
        banco.registrarCliente(cliente);
    }

    private void menuCliente()throws IOException{
        String cedula = entrada.readLine();
        Cliente cliente = banco.buscarCliente(cedula);
        if(cliente == null){
            System.out.println("Cliente no encontrado");
            return;
        }
        String contrasena = entrada.readLine();
        if(!cliente.autenticar(contrasena)){
            System.out.println("Contraseña incorrecta");
            return;
        }
        System.out.println("Bienvenido " + cliente.getNombreCompleto());

        boolean continuar = true;
        while(continuar){
            System.out.println("\n--Menu cliente--");
            System.out.println("1. Abrir cuenta");
            System.out.println("2. Realizar transacción");
            System.out.println("3. Ver mis cuentas");
            System.out.println("0. Volver");
            int opcion = Integer.parseInt(entrada.readLine());
            switch(opcion){
                case 1:
                    abrirCuenta(cliente);
                    break;
                case 2:
                    realizarTransaccion(cliente);
                    break;
                case 3:
                    cliente.mostrarCuentas();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }
    }

    private void abrirCuenta(Cliente cliente)throws IOException{
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
