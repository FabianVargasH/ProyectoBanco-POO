package vargas.fabian.tl;

import vargas.fabian.bl.entities.Cliente;
import vargas.fabian.bl.logic.GestorCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;

public class Controlador {
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

    public static void registrarCliente()throws IOException {
        System.out.println("\n--Registro de cliente--");
        System.out.print("Nombre completo: ");
        String nombre = entrada.readLine();
        System.out.print("\nCedula:");
        String cedula = entrada.readLine();
        System.out.print("\nFecha de nacimiento (formato yyyy-MM-dd):");
        LocalDate fecha = LocalDate.parse(entrada.readLine());
        System.out.print("\nIngrese su ocupacion");
        String ocupacion = entrada.readLine();
        System.out.print("\nIngrese su residencia");
        String residencia = entrada.readLine();
        System.out.print("Ingrese su contraseña:");
        String contrasena = entrada.readLine();
        System.out.println(GestorCliente.registrarCliente(nombre, cedula, fecha, ocupacion, residencia, contrasena));
    }

    public static Cliente ingresarCliente() throws IOException, SQLException, ClassNotFoundException {
        System.out.println("\n-- Ingreso como cliente --");
        System.out.print("\nIngrese su cedula: ");
        String cedula = entrada.readLine();
        System.out.print("\nIngrese su contraseña: ");
        String contrasena = entrada.readLine();
        return GestorCliente.ingresarCliente(cedula, contrasena);
    }
}
