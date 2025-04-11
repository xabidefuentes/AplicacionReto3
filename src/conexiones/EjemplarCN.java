package conexiones;
import Io.*;
import conexiones.*;

public class EjemplarCN {
    //FUNCIONES
    //Menu usuario
    public static void menuEjemplar(){
        Io.sop("***********************************************************************");
                Io.sop("********************  GESTION DE EJEMPLARES  ****************");
                Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
                Io.sop("**************************  DE MUSKIZ  ************************************");
                Io.sop("1. AGREGAR EJEMPLAR");
                Io.sop("2. BORRAR EJEMPLAR");
                Io.sop("3. MODIFICAR EJEMPLAR");
                Io.sop("4. SALIR");
                int opcion = Io.leerInt("Selecciona una opción: ");
                switch (opcion) {
                    case 1:
                        /*LibroCN.insertarLibro();*/
                        break;
                    case 2:
                        /*LibroCN.borrarLibro();*/
                        break;
                    case 3:
                        /*LibroCN.modificarLibro();*/
                        break;
                    case 4:
                    Io.sop("Saliendo...");
                        break;
                    default:
                        Io.sop("Opción no válida. Intenta otra vez.");
                }
            }
    
    
        //INSERTAR LIBROS




    /*public static void main(String[] args) {
        Io.sop("***********************************************************************");
        Io.sop("********************  GESTION DE EJEMPLARES  ****************");
        Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
        Io.sop("**************************   DE MUSKIZ  ************************************");
        Io.sop("1. AGREGAR EJEMPLAR");
        Io.sop("2. BORRAR EJEMPLAR");
        Io.sop("3. MODIFICAR EJEMPLAR");
        Io.sop("4. SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                LibroCN.insertarLibro();
                break;
            case 2:
                LibroCN.borrarLibro();
                break;
            case 3:
                    
                break;
            case 4:
                Io.sop("Saliendo...");
                break;
            default:
                Io.sop("Opción no válida. Intenta otra vez.");
        }
    }

*/
}
