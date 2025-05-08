package principal;

import Io.*;
import conexiones.AutorCN;
import conexiones.LibroCN;
import conexiones.UsuarioCN;

public class main {
    public static void main(String[] args) {
       menuPrincipal();
    }
    public static void menuPrincipal() {
        Io.sop("╔═══════════════════════════════════════════════════════════════════════╗");
        Io.sop("║              BIENVENIDO A LA BIBLIOTECA MUNICIPAL DE MUSKIZ           ║");
        Io.sop("║                           " + Io.DTOC() + "                           ║");
        Io.sop("╠═══════════════════════════════════════════════════════════════════════╣");
        Io.sop("║  1. GESTIONAR PRÉSTAMOS                                               ║");
        Io.sop("║  2. GESTIONAR LIBROS                                                  ║");
        Io.sop("║  3. GESTIONAR AUTORES                                                 ║");
        Io.sop("║  4. GESTIONAR USUARIOS                                                ║");
        Io.sop("║  5. BÚSQUEDAS                                                         ║");
        Io.sop("║  6. SALIR                                                             ║");
        Io.sop("╚═══════════════════════════════════════════════════════════════════════╝");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                Prestamo.menuPrestamo();
                break;
            case 2:
                LibroCN.menuLibro();
                break;
            case 3:
                AutorCN.menuAutores();
                break;
            case 4:
                UsuarioCN.menuUsuario();
                break;
            case 5:
                Busquedas.menuBusquedas();
                break;
            case 6:
                Io.sop("Saliendo...");
                return;
            default:
                Io.sop("Opción no válida. Intenta otra vez.");
        }
    }
}
