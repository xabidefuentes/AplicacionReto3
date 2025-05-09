package principal;

import Io.Io;
import conexiones.AutorCN;
import conexiones.LibroCN;
import conexiones.PrestamoCN;
import conexiones.UsuarioCN;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Io.Io.*;

public class Busquedas {
    public static void menuBusquedas() {
        Connection conn = Io.getConexion();
        int opcion;
        do {
            Io.sop("╔═══════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                BÚSQUEDAS                             ║");
            Io.sop("╠═══════════════════════════════════════════════════════════════════════╣");
            Io.sop("║  1. Buscar Préstamos                                                  ║");
            Io.sop("║  2. Buscar Libros                                                     ║");
            Io.sop("║  3. Buscar Autores                                                    ║");
            Io.sop("║  4. Buscar Usuarios                                                   ║");
            Io.sop("║  5. Volver al Menú Principal                                          ║");
            Io.sop("╚═══════════════════════════════════════════════════════════════════════╝");
            opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    Busquedas.buscarPrestamos(conn, 5, 1);
                    break;
                case 2:
                    LibroCN.consultarLibroPaginado(conn, 5,1);
                    break;
                case 3:
                    UsuarioCN.consultarUsuarioPaginado(conn,5,1);
                    break;
                case 4:
                   /* UsuarioCN.consultarUsuarioPaginado(conn, 5, 1);*/
                    break;
                case 5:
                    System.out.println("Saliendo del menú de préstamos.");
                    main.menuPrincipal();
                default:
                    System.out.println("Opción no válida. Intenta otra vez.");
            }
        } while (opcion > 0 && opcion > 5);
    }

    public static void buscarPrestamos (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String idPrestamo = "", FechaPrestamo = "", FechaDevolucion = "", dniUsuario = "", idEjemplar = "", dniEmpleado = "", swIdSeleccionado = null;
        int offset, posicion = 0;
        String[] aId = new String[totalRegistros];
        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT * FROM prestamos LIMIT " + totalRegistros + " OFFSET " + offset;
            sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║                         MODIFICACIÓN DE PRÉSTAMOS  |  PÁGINA: " + pagina + "                        ║");
            sop("╠═════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦══════════════╣");
            sop("║  ID ║ FECHA PRÉSTAMO ║ FECHA DEVOLUCIÓN ║  DNI USUARIO  ║  ID EJEMPLAR  ║ DNI EMPLEADO ║");
            sop("╚═════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩══════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);

                int cont = 0; // Aquí empieza desde el número correcto
                while (rs.next()) {
                    idPrestamo = PADL(rs.getString("id_prestamo"), 5);
                    FechaPrestamo = PADL(rs.getString("fecha_prestamo"), 14);
                    FechaDevolucion = PADL(rs.getString("fecha_devolucion"), 16);
                    dniUsuario = PADL(rs.getString("fk_dni_usuario"), 13);
                    idEjemplar = PADL(rs.getString("fk_id_ejemplar"), 13);
                    dniEmpleado = PADL(rs.getString("fk_dni_empleado"), 9);
                    aId[cont] = idPrestamo;
                    sop(idPrestamo + " | " + FechaPrestamo + " | " + FechaDevolucion + " | " + dniUsuario + " | " + idEjemplar + " | " + dniEmpleado);
                    cont++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
            sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
            char opc = leerCaracter();
            switch (opc) {
                case '+':
                    pagina++;
                    break;
                case '-':
                    if (pagina > 1) {
                        pagina--;
                    } else {
                        pagina = 1;
                    }
                    break;
                case 'x' | 'X':
                    salir = true;
                    Prestamo.menuPrestamo();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }


}
