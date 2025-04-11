package Io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;
import principal.Prestamo;

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

    public static boolean comprobarExistenciaInt(Connection conn, String tabla, String columna, int valor) {
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
       /* Prestamo.menuPrestamo();*/
    }
    public static String DTOC() {
        Calendar c1 = Calendar.getInstance();
        int dia = c1.get(Calendar.DAY_OF_MONTH);
        int mes = c1.get(Calendar.MONTH) + 1; // Enero es 0
        int anio = c1.get(Calendar.YEAR);

        // Obtenemos el nombre del mes
        String nombreMes = "";
        switch (mes) {
            case 1:  nombreMes = "Enero"; break;
            case 2:  nombreMes = "Febrero"; break;
            case 3:  nombreMes = "Marzo"; break;
            case 4:  nombreMes = "Abril"; break;
            case 5:  nombreMes = "Mayo"; break;
            case 6:  nombreMes = "Junio"; break;
            case 7:  nombreMes = "Julio"; break;
            case 8:  nombreMes = "Agosto"; break;
            case 9:  nombreMes = "Septiembre"; break;
            case 10: nombreMes = "Octubre"; break;
            case 11: nombreMes = "Noviembre"; break;
            case 12: nombreMes = "Diciembre"; break;
        }

        // Devolvemos la fecha en el formato adecuado
        return dia + " de " + nombreMes + " de " + anio;
    }

    public static String PADL(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        } else {
            while (str.length() < len) {
                str += " ";
            }
            return str;
        }
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
