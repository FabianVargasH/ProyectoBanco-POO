package vargas.fabian;

import java.io.IOException;
import vargas.fabian.bl.Banco;
import vargas.fabian.ui.Menu;

public class Main {
        public static void main(String[] args) throws IOException{
                Banco banco = new Banco("Banco de Java");
                Menu menu = new Menu(banco);
                menu.iniciar();       
        }
}
