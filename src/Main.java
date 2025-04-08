import Io.*;
import conexiones.*;
import principal.Prestamo;

public class Main {
    public static void main(String[] args) {
        Io.sop("***********************************************************************");
        Io.sop("***************  BIENVENIDO A LA BIBLIOTECA MUNICIPAL  ****************");
        Io.sop("***************************  DE MUSKIZ  *******************************");
        Io.sop("***********************************************************************");
        Io.sop("1. GESTIONAR PRESTAMOS");
        Io.sop("2. GESTIONAR LIBROS");
        Io.sop("3. GESTIONAR AUTORES");
        Io.sop("4. GESTIONAR USUARIOS");
        Io.sop("5. BUSQUEDAS");
        Io.sop("6. SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                Prestamo.menuPrestamo();
                break;
            case 2:
                // Llamar a la función de gestionar libros
                break;
            case 3:
                // Llamar a la función de gestionar autores
                break;
            case 4:
                // Llamar a la función de gestionar usuarios
                break;
            case 5:
                // Llamar a la función de búsquedas
                break;
            case 6:
                Io.sop("Saliendo...");
                break;
            default:
                Io.sop("Opción no válida. Intenta otra vez.");
        }


    }
}
