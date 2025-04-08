package Io;

import principal.Prestamo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Io {
// SYSTEM  OUT
    public static void sop(String s){
        System.out.println(s);
    }
    public static void Sop(String s){
        sop(s);
    }
// FUNCIONES BASICAS DE CONEXIÓN
public static Connection getConexion(){
    String url = "jdbc:mysql://localhost:3306/web_reto";
    String user = "root";
    String pass = "";
    Connection conn = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return conn;
}
    public static boolean cerrarConexion (Connection conn){
        try {
            conn.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean comprobarExistencia(Connection conn, String tabla, String columna, String valor) {
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + " = '" + valor + "'";
        try (Statement stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Io.sop("⚠️ Error comprobando existencia en tabla " + tabla + ": " + e.getMessage());
        }
        return false;
    }

// FUNCIONES DE ESCANER
    private static final Scanner scanner = new Scanner(System.in);
    public static String leerString(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextLine()) {
            System.out.println("Entrada no válida. Intenta otra vez.");
            scanner.next();
        }
        return scanner.nextLine();
    }

    public static int leerInt(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada no válida. Intenta otra vez.");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static LocalDate leerDate(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextLine()) {
            System.out.println("Entrada no válida. Intenta otra vez.");
            scanner.next();
        }
        String fechaStr = scanner.nextLine();
        return LocalDate.parse(fechaStr);
    }

    public static void main(String[] args) {
        /*String texto = leerString("Introduce un texto: ");
        int numero = leerInt("Introduce un número: ");
        sop("Texto introducido: " + texto);
        sop("Número introducido: " + numero);

        Connection conn = getConexion();
        if (conn != null) {
            sop("Conexión exitosa a la base de datos.");
            cerrarConexion(conn);
        } else {
            sop("Error al conectar a la base de datos.");
        }*/

        Prestamo prestamo = new Prestamo();
        Prestamo.menuPrestamo();
    }
}
