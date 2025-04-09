package conexiones;
import Io.*;
import conexiones.*;
import principal.Prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

import static Io.Io.PADL;

public class PrestamoCN {
    public static void añadirPrestamo() {
        Connection conn = Io.getConexion();
        String dni;
        String idEjemplar;
        String dniEmpleado;

        // Comprobar si el usuario existe
        do {
            dni = Io.leerString("Ingresa el DNI del usuario: ");
            if (!Io.comprobarExistencia(conn, "usuarios", "dni", dni)) {
                Io.sop("❌ No existe ningún usuario con ese DNI.");
            }
        } while (!Io.comprobarExistencia(conn, "usuarios", "dni", dni));

        LocalDate fechaPrestamo = Io.leerDate("Ingresa la fecha de préstamo (YYYY-MM-DD): ");
        String fechaDevolucion = Io.leerString("Ingresa la fecha de devolución (YYYY-MM-DD): ");

       // Comprobar si el ejemplar existe
        do {
            idEjemplar = Io.leerString("Ingresa el ID del ejemplar: ");
            if (!Io.comprobarExistencia(conn, "ejemplares", "id_ejemplar", idEjemplar)) {
                Io.sop("❌ No existe ningún ejemplar con ese ID.");
            }
        } while (!Io.comprobarExistencia(conn, "ejemplares", "id_ejemplar", idEjemplar));

        // Comprobar si el empleado existe
        do {
            dniEmpleado = Io.leerString("Ingresa el DNI del empleado: ");
            if (!Io.comprobarExistencia(conn, "empleados", "dni", dniEmpleado)) {
                Io.sop("❌ No existe ningún empleado con ese DNI.");
            }
        } while (!Io.comprobarExistencia(conn, "empleados", "dni", dniEmpleado));

        // Comprobar si el ejemplar ya está prestado
        if (ejecutarInsert(conn, dni, fechaPrestamo, fechaDevolucion, idEjemplar, dniEmpleado)) {
            Io.sop("✅ Préstamo añadido correctamente.");
        } else {
            Io.sop("❌ Error al añadir el préstamo.");
        }
    }

    public static void borrarPrestamo(){
        Connection conn = Io.getConexion();
        consultaTablaDelete(conn, 5, 1);

    }

    public static Prestamo buscarPrestamo(Connection conn, int swcod) {
        Statement stm = null;
        ResultSet rs = null;
        Prestamo swPrestamo = null;
        String sql = "SELECT * FROM prestamos WHERE id_prestamo ='"+swcod+"'";
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                swcod = rs.getInt("id_prestamo");
                String swdni = rs.getString("fk_dni_usuario");
                LocalDate swfechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();
                LocalDate swfechaDevolucion = rs.getDate("fecha_devolucion").toLocalDate();
                int swidEjemplar = rs.getInt("fk_id_ejemplar");
                String swdniEmpleado = rs.getString("fk_dni_empleado");
                swPrestamo = new Prestamo (swcod, swfechaPrestamo, swfechaDevolucion, swdni, swidEjemplar, swdniEmpleado);
                return swPrestamo;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void modificarPrestamo(){
        Connection conn = Io.getConexion();
        String dni;
        String idEjemplar;
        String dniEmpleado;

        dni = Io.leerString("Ingresa el DNI del usuario: ");
        idEjemplar = Io.leerString("Ingresa el ID del ejemplar: ");
        dniEmpleado = Io.leerString("Ingresa el DNI del empleado: ");

        LocalDate fechaPrestamo = Io.leerDate("Ingresa la nueva fecha de préstamo (YYYY-MM-DD): ");
        String fechaDevolucion = Io.leerString("Ingresa la nueva fecha de devolución (YYYY-MM-DD): ");

        if (ejecutarUpdate(conn, dni, fechaPrestamo, fechaDevolucion, idEjemplar, dniEmpleado)) {
            Io.sop("✅ Préstamo modificado correctamente.");
        } else {
            Io.sop("❌ Error al modificar el préstamo.");
        }

    }
    public static boolean ejecutarInsert(Connection conn, String dni, LocalDate fechaPrestamo, String fechaDevolucion, String idEjemplar, String dniEmpleado) {
        String sql = "INSERT INTO prestamos (id_prestamo, fecha_prestamo, fecha_devolucion, fk_dni_usuario, fk_id_ejemplar, fk_dni_empleado) VALUES ('" + (int) (Math.random() * 1000) +"', '" + fechaPrestamo + "', '" + fechaDevolucion + "', '" + dni + "', '" + idEjemplar + "', '" + dniEmpleado + "')";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.Sop("Problema: "+sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ejecutarDelete (Connection conn, String idPrestamo) {
        String sql = "DELETE FROM prestamos WHERE id_prestamo = " + idPrestamo;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.Sop("Problema: "+sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean ejecutarUpdate (Connection conn, String dni, LocalDate fechaPrestamo, String fechaDevolucion, String idEjemplar, String dniEmpleado) {
        String sql = "UPDATE prestamos SET fecha_prestamo = '" + fechaPrestamo + "', fecha_devolucion = '" + fechaDevolucion + "' WHERE fk_dni_usuario = '" + dni + "' AND fk_id_ejemplar = '" + idEjemplar + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.Sop("Problema: "+sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void consultaTablaDelete (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String idPrestamo = "", FechaPrestamo = "", FechaDevolucion = "", dniUsuario = "", idEjemplar = "", dniEmpleado = "", swIdSeleccionado = null;
        int offset, posicion = 0;
        String[] aId = new String[totalRegistros];
        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT * FROM prestamos LIMIT " + totalRegistros + " OFFSET " + offset;
            Io.sop("---------------------------------------------------------------------------------");
            Io.sop("---------------------- TABLA DE PRESTAMOS | PAGINA: " + pagina + " ---------------------------");
            Io.sop("---------------------------------------------------------------------------------");
            Io.sop("ID | FECHA PRESTAMO | FECHA DEVOLUCIÓN | DNI USUARIO | ID EJEMPLAR | DNI EMPLEADO");

            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);

                int cont = 0; // Aquí empieza desde el número correcto
                while (rs.next()) {
                    idPrestamo = PADL(rs.getString("id_prestamo"), 2);
                    FechaPrestamo = PADL(rs.getString("fecha_prestamo"), 14);
                    FechaDevolucion = PADL(rs.getString("fecha_devolucion"), 16);
                    dniUsuario = PADL(rs.getString("fk_dni_usuario"), 11);
                    idEjemplar = PADL(rs.getString("fk_id_ejemplar"), 11);
                    dniEmpleado = PADL(rs.getString("fk_dni_empleado"), 9);
                    aId[cont] = idPrestamo;
                    Io.sop(idPrestamo + " | " + FechaPrestamo + " | " + FechaDevolucion + " | " + dniUsuario + " | " + idEjemplar + " | " + dniEmpleado);
                    cont++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("-----------------------------------------------------------------------------");
            Io.sop("| [+] Pag. Siguiente   [-] Pag. Anterior   [X] Salir   [Num] Borrar |");
            Io.sop("-----------------------------------------------------------------------------");
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
                    break;
                case '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    posicion = Character.getNumericValue(opc) - 1;
                    swIdSeleccionado = aId[posicion - 1];
                    salir = true;
                    break;
                default:
                    Io.sop("Opcion no valida");
            }
        }
        ejecutarDelete(conn, swIdSeleccionado);
        Io.sop("✅ Préstamo con ID: " + swIdSeleccionado + " eliminado correctamente.");
        Prestamo.menuPrestamo();
    }
    public static ResultSet ejecutarSelect (Connection conn, String sql) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static char leerCaracter(){
        Scanner sc = new Scanner(System.in);
        char letra = ' ';
        String cadena = "";
        cadena = sc.nextLine();
        if (cadena.isEmpty()) {
            letra = 13; // ENTER
        } else {
            letra = cadena.charAt(0);
        }
        return letra;
    }




}
