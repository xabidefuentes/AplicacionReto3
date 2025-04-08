package conexiones;
import Io.*;
import conexiones.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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
        String dni;
        String idEjemplar;
        String dniEmpleado;

        dni = Io.leerString("Ingresa el DNI del usuario: ");
        idEjemplar = Io.leerString("Ingresa el ID del ejemplar: ");
        dniEmpleado = Io.leerString("Ingresa el DNI del empleado: ");

        if (ejecutarDelete(conn, dni, null, null, idEjemplar, dniEmpleado)) {
            Io.sop("✅ Préstamo eliminado correctamente.");
        } else {
            Io.sop("❌ Error al eliminar el préstamo.");
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

    public static boolean ejecutarDelete (Connection conn, String dni, LocalDate fechaPrestamo, String fechaDevolucion, String idEjemplar, String dniEmpleado) {
        String sql = "DELETE FROM prestamos WHERE fk_dni_usuario = '" + dni + "' AND fk_id_ejemplar = '" + idEjemplar + "'";
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
}
