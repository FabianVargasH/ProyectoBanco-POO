package vargas.fabian.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.tl.Controlador;

public class Menu {
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

    public static void menuPrincipal() throws Exception {
        boolean continuar = true;
        while(continuar){
            System.out.println("\n--Menu Principal--");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Acceder como cliente");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion;
            try{
                opcion = Integer.parseInt(entrada.readLine());
            } catch(NumberFormatException e){
                System.out.println("Opción inválida");
                continue;
            }
            switch(opcion){
                case 1:
                    Controlador.registrarCliente();
                    break;
                case 2:
                    Cliente cliente = Controlador.ingresarCliente();
                    if(cliente == null) {
                        System.out.println("Autentificación inválida");
                        continue;
                    }
                    menuCliente(cliente);
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

    private static void menuCliente(Cliente cliente) throws Exception {
        int opcion;
        boolean continuar = true;
        while(continuar){
            System.out.println("\n-- Menu de cliente --");
            System.out.println("1. Abrir una cuenta");
            System.out.println("2. Hacer un retiro");
            System.out.println("3. Hacer un depósito o abono");
            System.out.println("4. Generar intereses");
            System.out.println("5. Hacer una transferencia");
            System.out.println("6. Ver todas las cuentas");
            System.out.println("7. Eliminar una cuenta");
            System.out.println("0. Salir");
            System.out.print("Ingrese su elección: ");

            try{
                opcion = Integer.parseInt(entrada.readLine());
            } catch(NumberFormatException e){
                System.out.println("Opción inválida, intente nuevamente");
                continue;
            }

            switch (opcion){
                case 1:
                    Controlador.abrirCuenta(cliente);
                    break;
                case 2:
                    Controlador.retirar(cliente);
                    break;
                case 3:
                    Controlador.depositarAbonar(cliente);
                    break;
                case 4:
                    Controlador.generarIntereses(cliente);
                    System.out.println("Funcionalidad en desarrollo");
                    break;
                case 5:
                    Controlador.transferir(cliente);
                    break;
                case 6:
                    Controlador.verCuentas(cliente);
                    break;
                case 7:
                    Controlador.eliminarCuenta(cliente);
                    System.out.println("Funcionalidad en desarrollo");
                    break;
                case 0:
                    System.out.println("Saliendo del menú de cliente...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }
}
