package vargas.fabian.dl;

import vargas.fabian.utils.Utilidades;

import java.io.IOException;
import java.sql.SQLException;

public class Conector {

    private static AccesoBD conexionBD = null;

    public static AccesoBD getConexion() throws IOException, SQLException, ClassNotFoundException {
        if(conexionBD != null) return conexionBD;
        String[] propiedades = Utilidades.getProperties();
        String direccion = propiedades[0] + "//" + propiedades[1] + "/" + propiedades[2];
        String usuario = propiedades[3];
        String contrasena = propiedades[4];
        conexionBD = new AccesoBD(direccion, usuario, contrasena);
        return conexionBD;
    }

}
