package Io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        //Sop("Problemas al conectar");
        //e.printStackTrace();
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

    // METODO PARA VALIDAR FECHA
    public static LocalDate parsearFecha(String texto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(texto, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: debes introducir una fecha completa en formato yyyy-MM-dd (por ejemplo, 2025-04-30).");
            return null;
        }
    }
    public static boolean esFechaValida(String texto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(texto, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
//Metodo para validar dni
    public static boolean validarDni(String dni) {
        if (dni == null || dni.length() != 9) {
            return false;
        }
        String numeroStr = dni.substring(0, 8);
        char letraIntroducida = dni.charAt(8);
            if (!numeroStr.matches("\\d{8}")) {
                return false;
        }
        int numero = Integer.parseInt(numeroStr);
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letraCorrecta = letras.charAt(numero % 23);
            return letraIntroducida == letraCorrecta;
    }

//Metodo para ejecutar la validacion del dni

    public static String ejecutarValidarDni(Connection conn) {
        String dni;
        boolean valido = false;
        do {
            dni = leerString("Introduce el DNI del usuario: ").toUpperCase(); // Pedimos y pasamos a mayúsculas
            if (!validarDni(dni)) {
                sop("Formato de DNI incorrecto. Debe tener 8 números y una letra correcta.");
            } else if (comprobarExistencia(conn, "usuarios", "dni", dni)) {
                sop("El DNI ya existe en la base de datos. Introduce uno diferente.");
            } else {
                valido = true; // Si el formato es correcto y no existe, lo damos como válido
            }
        } while (!valido);
        return dni;
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
