package vargas.fabian.bl;
import java.util.ArrayList;

public class Banco {
    private String nombre;
    private ArrayList<Cliente> clientes;

    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
    }
    //Metodo para el registro de clientes
    public boolean registrarCliente(Cliente cliente) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cliente.getCedula())) {
                System.out.println("  Ya existe un cliente con la cédula: " + cliente.getCedula());
                return false;
            }
        }
        clientes.add(cliente);
        System.out.println("  Cliente " + cliente.getNombreCompleto() + " registrado exitosamente.");
        return true;
    }

    public Cliente buscarCliente(String cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                return c;
            }
        }
        return null;
    }

    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("  No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println("  " + c);
        }
    }
    public String getNombre() { 
        return nombre; }
}
