package conexiones;
import Io.*;
import static Io.Io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import principal.Prestamo;

public class PrestamoCN {
    public static void añadirPrestamo() {
        Connection conn = getConexion();
        String dni;
        String idEjemplar;
        String dniEmpleado;

        // Comprobar si el usuario existe
        do {
            dni = leerString("Ingresa el DNI del usuario: ");
            if (!comprobarExistencia(conn, "usuarios", "dni", dni)) {
                sop("❌ No existe ningún usuario con ese DNI.");
            }
        } while (!comprobarExistencia(conn, "usuarios", "dni", dni));

        LocalDate fechaPrestamo = leerDate("Ingresa la fecha de préstamo (YYYY-MM-DD): ");

       // Comprobar si el ejemplar existe
        do {
            idEjemplar = leerString("Ingresa el ID del ejemplar: ");
            if (!comprobarExistencia(conn, "ejemplares", "id_ejemplar", idEjemplar)) {
                sop("❌ No existe ningún ejemplar con ese ID.");
            }
        } while (!comprobarExistencia(conn, "ejemplares", "id_ejemplar", idEjemplar));

        // Comprobar si el empleado existe
        do {
            dniEmpleado = leerString("Ingresa el DNI del empleado: ");
            if (!comprobarExistencia(conn, "empleados", "dni", dniEmpleado)) {
                sop("❌ No existe ningún empleado con ese DNI.");
            }
        } while (!comprobarExistencia(conn, "empleados", "dni", dniEmpleado));

        // Comprobar si el ejemplar ya está prestado
        if (ejecutarInsert(conn, dni, fechaPrestamo, idEjemplar, dniEmpleado)) {
            sop("✅ Préstamo añadido correctamente.");
        } else {
            sop("❌ Error al añadir el préstamo.");
        }
        Prestamo.menuPrestamo();
    }
    public static boolean ejecutarInsert(Connection conn, String dni, LocalDate fechaPrestamo, String idEjemplar, String dniEmpleado) {
        String sql = "INSERT INTO prestamos (id_prestamo, fecha_prestamo, fecha_devolucion, fk_dni_usuario, fk_id_ejemplar, fk_dni_empleado) " +
                "VALUES ('" + (int) (Math.random() * 1000) + "', '" + fechaPrestamo + "', '1000-10-10', '" + dni + "', '" + idEjemplar + "', '" + dniEmpleado + "')";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Sop("Problema: "+sql);
            e.printStackTrace();
            Prestamo.menuPrestamo();
            return false;
        }
        return true;
    }
    public static void borrarPrestamo(){
        Connection conn = getConexion();
        consultaTablaDelete(conn, 5, 1);
        cerrarConexion(conn);
    }
    public static void modificarPrestamo(){
        Connection conn = getConexion();
        consultaTablaPrestamo(conn, 5, 1);
        cerrarConexion(conn);
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
    public static void consultaTablaPrestamo (Connection conn, int totalRegistros, int pagina) {
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
            sop("Muevete por la tabla y selecciona el ID del préstamo que deseas modificar: ");
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
                default:
                    salir = true;
                    break;
            }
        }
        String idAntiguo = leerString("Introduce de nuevo el ID del préstamo que quieres modificar: ");
        if (!comprobarExistencia(conn, "prestamos", "id_prestamo", idAntiguo)) {
            sop("❌ No existe ningún préstamo con ese ID.");
            return;
        }
        sop("¿Qué campo del préstamo " + idAntiguo + " deseas modificar?");
        sop("1. ID");
        sop("2. Fecha de préstamo");
        sop("3. Fecha de devolución");
        sop("4. DNI del usuario");
        sop("5. ID del ejemplar");
        sop("6. DNI del empleado");
        sop("7. Terminar");

        char opc = leerCaracter();
        String campo = "", nuevoValor = "";

        switch (opc) {
            case '1':
                campo = "id_prestamo";
                nuevoValor = leerString("Introduce el nuevo ID del préstamo: ");
                break;
            case '2':
                campo = "fecha_prestamo";
                nuevoValor = leerDate("Introduce la nueva fecha de préstamo (YYYY-MM-DD): ").toString();
                break;
            case '3':
                campo = "fecha_devolucion";
                nuevoValor = leerDate("Introduce la nueva fecha de devolución (YYYY-MM-DD): ").toString();
                break;
            case '4':
                campo = "fk_dni_usuario";
                nuevoValor = leerString("Introduce el nuevo DNI del usuario: ");
                break;
            case '5':
                campo = "fk_id_ejemplar";
                nuevoValor = leerString("Introduce el nuevo ID del ejemplar: ");
                break;
            case '6':
                campo = "fk_dni_empleado";
                nuevoValor = leerString("Introduce el nuevo DNI del empleado: ");
                break;
            case '7':
                salir = true;
                break;
            default:
                sop("Opción no válida.");
        }

        if (!campo.equals("")) {
            if (ejecutarUpdateCampo(conn, idAntiguo, campo, nuevoValor)) {
                sop("✅ Préstamo con ID " + idAntiguo + " modificado correctamente.");
            } else {
                sop("❌ Error al modificar el préstamo.");
            }
        }
        Prestamo.menuPrestamo();

    }
    public static boolean ejecutarUpdateCampo(Connection conn, String idAntiguo, String campo, String nuevoValor) {
        String sql = "UPDATE prestamos SET " + campo + " = '" + nuevoValor + "' WHERE id_prestamo = '" + idAntiguo + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            sop("❌ Error con la query: " + sql);
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
            sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║                             ELIMINAR PRÉSTAMOS  |  PÁGINA: " + pagina + "                           ║");
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
            Io.sop("Muevete por la tabla y selecciona el ID del préstamo que deseas eliminar: ");
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
                default:
                    salir = true;
                    break;
            }
        }
        swIdSeleccionado = leerString("¿Estas seguro que quieres eliminarlo? Introduce de nuevo ID del préstamo: ");
        ejecutarDelete(conn, swIdSeleccionado);
        sop("✅ Préstamo con ID: " + swIdSeleccionado + " eliminado correctamente.");
        Prestamo.menuPrestamo();
    }

    public static boolean ejecutarDelete (Connection conn, String idPrestamo) {
        String sql = "DELETE FROM prestamos WHERE id_prestamo = " + idPrestamo;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Sop("Problema: "+sql);
            e.printStackTrace();
            return false;
        }
        return true;
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

   

    public static LocalDate sumar30dias(LocalDate FechaPrestamo) {
        return FechaPrestamo.plusDays(30);
    }




}
