package principal;

import Io.*;
import conexiones.AutorCN;
import conexiones.LibroCN;
import conexiones.PrestamoCN;
import conexiones.UsuarioCN;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Io.Io.*;
import static conexiones.UsuarioCN.padl;

public class Busquedas {
    public static void menuBusquedas() {
        Connection conn = Io.getConexion();
        int opcion;
        do {
            Io.sop("╔═══════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                BÚSQUEDAS                              ║");
            Io.sop("╠═══════════════════════════════════════════════════════════════════════╣");
            Io.sop("║  1. Buscar Préstamos                                                  ║");
            Io.sop("║  2. Buscar Libros                                                     ║");
            Io.sop("║  3. Buscar Autores                                                    ║");
            Io.sop("║  4. Buscar Usuarios                                                   ║");
            Io.sop("║  5. Buscar Empleados                                                  ║");
            Io.sop("║  6. Volver al Menú Principal                                          ║");
            Io.sop("╚═══════════════════════════════════════════════════════════════════════╝");
            opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    buscarPrestamos(conn, 10, 1);
                    break;
                case 2:
                    buscarLibros(conn, 10, 1);
                    break;
                case 3:
                    buscarAutores(conn,10,1);
                    break;
                case 4:
                    buscarUsuarios(conn,10,1);
                case 5:
                    buscarEmpleados(conn, 10, 1);
                    break;
                case 6:
                    main.menuPrincipal();
                default:
                    System.out.println("Opción no válida. Intenta otra vez.");
            }
        } while (opcion > 0 && opcion > 6);
    }

    public static void buscarPrestamos (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String idPrestamo = "", fechaPrestamo = "", fechaDevolucion = "", dniUsuario = "", idEjemplar = "", dniEmpleado = "", swIdSeleccionado = "", tituloLibro = "", nombreUsuario = "", nombreEmpleado = "";
        int offset, posicion = 0;
        String[] aId = new String[totalRegistros];
        String orden = "id_prestamo";

        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT p.*, u.nombre AS nombre_usuario FROM prestamos p JOIN usuarios u ON p.fk_dni_usuario = u.dni ORDER BY " + orden + " LIMIT " + totalRegistros + " OFFSET " + offset;
            sop("╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║                                                                  LISTADO DE PRÉSTAMOS  |  PÁGINA: " + pagina + "                                                                  ║");
            sop("╠═════╦════════════════════════════════╦═══════════════╦════════════════╦══════════════════╦═══════════════╦══════════════════════╦═══════════════╦════════════════════╣");
            sop("║ ID  ║        TÍTULO LIBRO            ║  ID EJEMPLAR  ║ FECHA PRÉSTAMO ║ FECHA DEVOLUCIÓN ║  DNI USUARIO  ║    NOMBRE USUARIO    ║  DNI EMPLEADO ║   NOMBRE EMPLEADO  ║");
            sop("╚═════╩════════════════════════════════╩═══════════════╩════════════════╩══════════════════╩═══════════════╩══════════════════════╩═══════════════╩════════════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);

                int cont = 0; // Aquí empieza desde el número correcto
                while (rs.next()) {
                    idPrestamo = PADL(rs.getString("id_prestamo"), 5);
                    tituloLibro = PrestamoCN.buscarPrestamoPorId(conn, idPrestamo);
                    tituloLibro = PADL(tituloLibro, 30);
                    fechaPrestamo = PADL(rs.getString("fecha_prestamo"), 14);
                    fechaDevolucion = rs.getString("fecha_devolucion");
                    fechaDevolucion = PrestamoCN.mostrarPenalización(conn, fechaDevolucion, dniUsuario);
                    fechaDevolucion = PADL(fechaDevolucion, 16);

                    dniUsuario = PADL(rs.getString("fk_dni_usuario"), 13);
                    nombreUsuario = PrestamoCN.buscarUsuarioPorDni(conn, dniUsuario);
                    nombreUsuario = PADL(nombreUsuario, 20);
                    idEjemplar = PADL(rs.getString("fk_id_ejemplar"), 13);
                    dniEmpleado = PADL(rs.getString("fk_dni_empleado"), 13);
                    nombreEmpleado = PrestamoCN.buscarEmpleadoPorDni(conn, dniEmpleado);
                    nombreEmpleado = PADL(nombreEmpleado, 20);
                    aId[cont] = idPrestamo;
                    sop(idPrestamo + " | " + tituloLibro + " | "  + idEjemplar + " | " + fechaPrestamo + " | " + fechaDevolucion + " | " + dniUsuario + " | " + nombreUsuario + " | " + dniEmpleado + " | " + nombreEmpleado);
                    cont++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            sop("╔═══════════════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║ ORDENAR POR: [LIBROS (L)]        [FECHA (F)]         [USUARIO (U)]    ║  [+] Página Siguiente                 [-] Página Anterior                         [X] Salir   ║");
            sop("╚═══════════════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona una opción: ");
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
                case 'L','l':
                    orden = "fk_id_ejemplar";
                    break;
                case 'F','f':
                    orden = "fecha_prestamo";
                    break;
                case 'U','u':
                    orden = "u.nombre";
                    break;
                case 'x','X':
                    salir = true;
                    menuBusquedas();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }

    public static void buscarLibros(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset, vISBN, vAnioPublicacion, vidAutor;
        String vTitulo, vGenero, vEditorial, sql, orden = "titulo"; // orden por defecto

        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            sql = "SELECT * FROM libros ORDER BY " + orden + " LIMIT " + nRegPag + " OFFSET " + offset;

            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                            LISTADO DE LIBROS  |  PÁGINA: " + Io.PADL(nPag + "", 49) + "  ║");
            Io.sop("╠═════════════════════════════════════════╦══════════════════════╦═══════════════════════════════╦═════════════╦════════════════╦═════════════╣");
            Io.sop("║                  TÍTULO                 ║        GÉNERO        ║           EDITORIAL           ║     AÑO     ║      ISBN      ║  ID AUTOR   ║");
            Io.sop("╚═════════════════════════════════════════╩══════════════════════╩═══════════════════════════════╩═════════════╩════════════════╩═════════════╝");

            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vTitulo = LibroCN.pad2(rs.getString("titulo"), 41);
                    vGenero = LibroCN.pad2(rs.getString("genero"), 20);
                    vEditorial = LibroCN.pad2(rs.getString("editorial"), 30);
                    vAnioPublicacion = rs.getInt("ano");
                    vISBN = rs.getInt("isbn");
                    vidAutor = rs.getInt("fk_id_autor");

                    System.out.println(vTitulo + " | " + vGenero + " | " + vEditorial + "| " +
                            vAnioPublicacion + "        |" + vISBN + "            | " + vidAutor + "    ");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            sop("╔═══════════════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║ ORDENAR POR: [TITULO (T)]           [AÑO (A)]          [GÉNERO (G)]     ║  [+] Página Siguiente                 [-] Página Anterior                      [X] Salir      ║");
            sop("╚═══════════════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════╝");

            Io.sop("Muevete por la tabla y selecciona el libro o cambia orden/página: ");
            char opc = Io.leerCaracter();

            switch (Character.toUpperCase(opc)) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) nPag--;
                    break;
                case 'T', 't':
                    orden = "titulo";
                    break;
                case 'A', 'a':
                    orden = "ano";
                    break;
                case 'G', 'g':
                    orden = "genero";
                    break;
                case 'X' , 'x':
                    salir = true;
                    menuBusquedas();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }

    public static void buscarAutores(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vID, vNom, vApe, vFN, vNac;
        String orden = "id_autor";  // Orden por defecto

        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            String sql = "SELECT * FROM autores ORDER BY " + orden + " LIMIT " + nRegPag + " OFFSET " + offset;

            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                            LISTADO DE AUTORES  |  PÁGINA: " + nPag + "                                     ║");
            Io.sop("╠══════════╦══════════════════════╦══════════════════════════╦═════════════════════╦══════════════════════════════╣");
            Io.sop("║    ID    ║        NOMBRE        ║        APELLIDOS         ║ FECHA DE NACIMIENTO ║        NACIONALIDAD          ║");
            Io.sop("╚══════════╩══════════════════════╩══════════════════════════╩═════════════════════╩══════════════════════════════╝");

            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vID = Io.PADL(rs.getString("id_autor"), 10);
                    vNom = Io.PADL(rs.getString("nombre"), 20);
                    vApe = Io.PADL(rs.getString("apellidos"), 25);
                    vFN = Io.PADL(rs.getString("fecha_nacimiento"), 20);
                    vNac = Io.PADL(rs.getString("nacionalidad"), 20);
                    System.out.println(vID + " | " + vNom + " | " + vApe + " | " + vFN + " | " + vNac);
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar autores: " + e.getMessage());
            }

            Io.sop("╔═══════════════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ ORDENAR POR: [NOMBRE (N)]        [APELLIDO (A)]         [FECHA (F)]   ║  [+] Página Siguiente                 [-] Página Anterior                         [X] Salir   ║");
            Io.sop("╚═══════════════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Selecciona una opción: ");
            char opc = Io.leerCaracter();

            switch (Character.toUpperCase(opc)) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) nPag--;
                    break;
                case 'N' , 'n':
                    orden = "nombre";
                    nPag = 1; // Reiniciamos a la primera página
                    break;
                case 'A', 'a':
                    orden = "apellidos";
                    nPag = 1;
                    break;
                case 'F','f':
                    orden = "fecha_nacimiento";
                    nPag = 1;
                    break;
                case 'X','x':
                    salir = true;
                    menuBusquedas();
                    break;
                default:
                    Io.sop("Opción no válida.");
            }
        }
    }

    public static void buscarUsuarios(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vDni, vNom, vEmail, sql, vPasword, vPen, vFechaIni, vFechaFin, vTelefono;
        String orden = "dni"; // Orden por defecto

        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            sql = "SELECT * FROM usuarios ORDER BY " + orden + " LIMIT " + nRegPag + " OFFSET " + offset;

            Io.sop("╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                           LISTADO DE USUARIOS  |  PÁGINA: " + nPag + "                                                          ║");
            Io.sop("╠═════════╦════════════════════════╦════════════════════════════════╦═════════════════╦═════════════════╦════════════════╦════════════════╦════════════╣");
            Io.sop("║   DNI   ║         NOMBRE         ║              EMAIL             ║     TELÉFONO    ║    PASSWORD     ║  PENALIZACIÓN  ║  FECHA INICIO  ║ FECHA FIN  ║");
            Io.sop("╚═════════╩════════════════════════╩════════════════════════════════╩═════════════════╩═════════════════╩════════════════╩════════════════╩════════════╝");

            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vDni = padl(rs.getString("dni"), 9);
                    vNom = padl(rs.getString("nombre"), 22);
                    vEmail = padl(rs.getString("email"), 30);
                    vTelefono = padl(rs.getString("telefono"), 15);
                    vPasword = padl(rs.getString("password"), 15);
                    vPen = padl(rs.getString("penalizacion"), 14);
                    vFechaIni = padl(rs.getString("fecha_inicio_penalizacion"), 14);
                    vFechaFin = padl(rs.getString("fecha_fin_penalizacion"), 20);

                    System.out.println(vDni + " | " + vNom + " | " + vEmail + " | " + vTelefono + " | " + vPasword + " | " +
                            vPen + " | " + vFechaIni + " | " + vFechaFin);
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar usuarios: " + e.getMessage());
            }

            Io.sop("╔═══════════════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ ORDENAR POR: [NOMBRE (N)]        [DNI (D)]         [FECHA (F)]        ║  [+] Página Siguiente                 [-] Página Anterior                         [X] Salir   ║");
            Io.sop("╚═══════════════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Selecciona una opción: ");
            char opc = Character.toUpperCase(Io.leerCaracter());

            switch (opc) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) nPag--;
                    break;
                case 'N', 'n':
                    orden = "nombre";
                    nPag = 1;
                    break;
                case 'D', 'd':
                    orden = "dni";
                    nPag = 1;
                    break;
                case 'F','f':
                    orden = "fecha_inicio_penalizacion";
                    nPag = 1;
                    break;
                case 'X','x':
                    salir = true;
                    menuBusquedas();
                    break;
                default:
                    Io.sop("Opción no válida.");
            }
        }
    }

    public static void buscarEmpleados(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vDni, vNom, vEmail, sql, vPasword, vSeg, vTelefono;
        String orden = "dni"; // Orden por defecto

        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            sql = "SELECT * FROM empleados ORDER BY " + orden + " LIMIT " + nRegPag + " OFFSET " + offset;

            Io.sop("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                 LISTA DE EMPLEADOS  |  PÁGINA: " + nPag + "                                          ║");
            Io.sop("╠═════════╦════════════════════════╦═══════════════════════════════╦═════════════════╦═════════════════╦════════════════════╣");
            Io.sop("║   DNI   ║         NOMBRE         ║             EMAIL             ║     TELÉFONO    ║    PASSWORD     ║  SEGURIDAD SOCIAL  ║");
            Io.sop("╚═════════╩════════════════════════╩═══════════════════════════════╩═════════════════╩═════════════════╩════════════════════╝");

            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vDni = padl(rs.getString("dni"), 9);
                    vNom = padl(rs.getString("nombre"), 22);
                    vEmail = padl(rs.getString("email"), 30);
                    vTelefono = padl(rs.getString("telefono"), 16);
                    vPasword = padl(rs.getString("password"), 15);
                    vSeg = padl(rs.getString("seguridad_social"), 18);

                    System.out.println(vDni + " | " + vNom + " | " + vEmail + " | " + vTelefono + " | " + vPasword + " | " + vSeg);
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar empleados: " + e.getMessage());
            }

            Io.sop("╔═════════════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ ORDENAR POR: [NOMBRE (N)]          [DNI (D)]        ║  [+] Página Siguiente                 [-] Página Anterior                         [X] Salir   ║");
            Io.sop("╚═════════════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Selecciona una opción: ");
            char opc = Character.toUpperCase(Io.leerCaracter());

            switch (opc) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) nPag--;
                    break;
                case 'N', 'n':
                    orden = "nombre";
                    nPag = 1;
                    break;
                case 'D', 'd':
                    orden = "dni"; // Asegúrate de que este campo exista en la tabla
                    nPag = 1;
                    break;
                case 'X', 'x':
                    salir = true;
                    menuBusquedas();
                    break;
                default:
                    Io.sop("Opción no válida.");
            }
        }
    }


}






