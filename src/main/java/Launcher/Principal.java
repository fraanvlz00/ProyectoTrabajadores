package Launcher;

import Dominio.TrabajadorManager;
import java.io.IOException;

public class Principal {
    public static void main(String[] args) throws IOException {
        new TrabajadorManager("src/main/java/Datos/trabajador.json", "src/main/java/Datos/isapre.json", "src/main/java/Datos/afp.json").mostrarMenu();
    }
}
