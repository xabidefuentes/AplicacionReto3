package conexiones;

import Io.Io;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Io.Io.sop;

public class UsuarioCN {
    // FUNCIONES
    // Menu usuario
    public static void menuUsuario() {
        int opcion;
        do {
            Io.sop("╔═══════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                          GESTIÓN DE USUARIOS                          ║");
            Io.sop("╠═══════════════════════════════════════════════════════════════════════╣");
            Io.sop("║  1. Añadir Usuario                                                    ║");
            Io.sop("║  2. Eliminar Usuario                                                  ║");
            Io.sop("║  3. Modificar Usuario                                                 ║");
            Io.sop("║  4. Volver al Menú Principal                                          ║");
            Io.sop("╚═══════════════════════════════════════════════════════════════════════╝");
            opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    insertarUsuario();
                    break;
                case 2:
                    borrarUsuario();
                    break;
                case 3:
                    modificarUsuario();
                    break;
                case 4:
                    Io.sop("Saliendo.. Hasta pronto");
                    return;
                default:
                    Io.sop("Opción no válida. Intenta otra vez.");
            }
        } while (opcion>0 );
    }

    // Metodo para borrar usuario
    public static void borrarUsuario() {
        Connection conn = Io.getConexion();
        if (conn==null) { 
            Io.sop("Sin Conexión");
            return;
        }
        Io.sop("Conexión Correcta");
        borrarUsuarioConsultandoTabla(conn, 5, 1);
        Io.cerrarConexion(conn);
    }

    public static void modificarUsuario() {
        Connection conn = Io.getConexion();
        if (conn==null) { 
            Io.sop("Sin Conexión");
            return;
        }
        modificarUsuarioConTabla(conn, 5, 1);
        Io.cerrarConexion(conn);
    }

    /// Metodo para validar el email
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // Metodo para ejecutar la validacion del email
    public static String validarEmail(Connection conn) {
        String vEmail;
        do {
            vEmail = Io.leerString("Dime el email del usuario: ");
            if (!esCorreoValido(vEmail)) {
                Io.sop("Correo no válido. Asegúrate de que tenga formato correcto (ej: usuario@hotmail.com)");
            }
        } while (!esCorreoValido(vEmail));
        return vEmail;
    }

    // INSERTAR USUARIO
    public static void insertarUsuario() {
        Connection conn = Io.getConexion();
        if (conn==null) { 
             Io.sop("Sin Conexión");
             return;
        }
        int randomPassword = (int) (Math.random() * 9000) + 1000; // genera número aleatorio entre 1000 y 9999
        int cambios = 0, vTelefono;
        String vNombre, vDni, vEmail;

        vNombre = Io.leerString("Ingresa el nombre y apellido del usuario: ");

        // Comprobación para ver si el DNI está repetido y si es correcto
        vDni = Io.ejecutarValidarDni(conn);
        // Validación del email
        vEmail = validarEmail(conn);
        vTelefono = Io.leerInt("Ingresa el teléfono del usuario: ");
        String sql = "INSERT INTO usuarios (dni, nombre, telefono, email, password, penalizacion, fecha_inicio_penalizacion, fecha_fin_penalizacion) "
                +
                "VALUES ('" + vDni + "', '" + vNombre + "', '" + vTelefono + "', '" + vEmail + "', '" + randomPassword
                + "', 'No', '1111-11-11', '1111-11-11')";

        System.out.println("Contraseña generada para el usuario: " + randomPassword); // Mostrar contraseña generada

        try {
            Statement st = conn.createStatement();
            cambios = st.executeUpdate(sql);
            if (cambios == 0) {
                System.out.println("Error al añadir el usuario.");
            } else {
                System.out.println("Usuario añadido correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Problema al insertar en la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }

       
    }


    // Metodo para borrar el usuario consultandolo en la tabla
    public static void borrarUsuarioConsultandoTabla(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vDni, vNom, vEmail, sql, vPasword, vPen, vFechaIni, vFechaFin, vTelefono;
        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            sql = " select * from usuarios limit " + nRegPag + " offset " + offset + " ";
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                              ELIMINAR USUARIOS  |  PÁGINA: " + nPag + "                                                        ║");
            Io.sop("╠═════════╦════════════════════════╦═══════════════════════════════╦═════════════════╦═════════════════╦════════════════╦════════════════╦════════════╣");
            Io.sop("║   DNI   ║         NOMBRE         ║             EMAIL             ║     TELÉFONO    ║    PASSWORD     ║  PENALIZACIÓN  ║  FECHA INICIO  ║ FECHA FIN  ║");
            Io.sop("╚═════════╩════════════════════════╩═══════════════════════════════╩═════════════════╩═════════════════╩════════════════╩════════════════╩════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vDni = rs.getString("dni");
                    vDni = padl(vDni, 9);
                    vNom = rs.getString("nombre");
                    vNom = padl(vNom, 22);// Para que quede bien en columna del mismo tamaño
                    vTelefono = rs.getString("telefono");
                    vTelefono = padl(vTelefono, 16);
                    vEmail = rs.getString("email");
                    vEmail = padl(vEmail, 30);
                    vPasword = rs.getString("password");
                    vPasword = padl(vPasword, 15);
                    vPen = rs.getString("penalizacion");
                    vPen = padl(vPen, 16);
                    vFechaIni = rs.getString("fecha_inicio_penalizacion");
                    vFechaIni = padl(vFechaIni, 16);
                    vFechaFin = rs.getString("fecha_fin_penalizacion");
                    vFechaFin = padl(vFechaFin, 20);
                    System.out.println(vDni + " | " + vNom + " | " + vEmail + "| " + vTelefono + "| " + vPasword + " |"
                            + vPen + "|"
                            + vFechaIni + "|" + vFechaFin);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                                              [-] Página Anterior                                                    [X] Salir  ║");
            Io.sop("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el dni del usuario que deseas eliminar: ");
            char opc = Io.leerCaracter();
            switch (opc) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) {
                        nPag--;
                    } else {
                        nPag = 1;
                    }
                    break;
                case 'x' | 'X':
                    salir = true;
                    menuUsuario();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
        String vDniBorrado = Io
                .leerString("¿Estas seguro que quieres eliminarlo? Introduce de nuevo dni del usuario:  ");
        Io.sop("Voy a borrar el "+vDniBorrado);
        if (UsuarioCN.borrarDato(conn, vDniBorrado)) {
            System.out.println("Usuario borrado correctamente");
        } else {
            System.out.println("Usuario no se ha podido borrar");
        }
    }

    // Metodo para borrar el dato
    public static boolean borrarDato(Connection conn, String vDni) {// Borrar un campo en el que nos pasan el codigo
        Statement st;
        int borrados;
        String sql = "delete from usuarios where dni = '" + vDni + "'";
        try {
            st = conn.prepareStatement(sql);
            borrados = st.executeUpdate(sql);
            return borrados > 0;
        } catch (SQLException e) {
            System.out.println("Problema al borrar: " + sql + e.getErrorCode() + " " + e.getMessage());
            return false;
        }
    }

    //// USUARIO PAGINADO

    public static void consultarUsuarioPaginado(Connection conn, int nRegPag, int nPag) {
        /*
         * Realizamos la consulta sql para mostrar todos los datos de la tabla, Se
         * mostraran estudiante de
         * 10 en 10 (nRegPag), los que correspondean a la pagina nPag
         * offset es el desplazamiento dentro del fichero
         * limit : es el numero de registros que voy a leer
         * con + y - avanzare una pagina. La primera pagina sera la 1
         * al pulsar ESC saldre de la consulta
         */
        Statement stm = null;
        ResultSet rs = null;
        int offset, cont;
        String vDni, vNom, vEmail, sql, vTel, vPasword, vPen, vFechaFin, vFechaIni, vTelefono;
        boolean salir = false;
        while (!salir) {// Control de las teclas +, -, ESC
            offset = (nPag - 1) * nRegPag;
            sql = " select * from usuarios limit " + nRegPag + " offset " + offset + " ";
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                           LISTADO DE USUARIOS  |  PÁGINA: " + nPag + "                                                    ║");
            Io.sop("╠═════════╦════════════════════════╦═══════════════════════════════╦═════════════════╦═════════════════╦════════════════╦════════════════╦════════════╣");
            Io.sop("║   DNI   ║         NOMBRE         ║             EMAIL             ║     TELÉFONO    ║    PASSWORD     ║  PENALIZACIÓN  ║  FECHA INICIO  ║ FECHA FIN  ║");
            Io.sop("╚═════════╩════════════════════════╩═══════════════════════════════╩═════════════════╩═════════════════╩════════════════╩════════════════╩════════════╝");
            try {
                while (rs.next()) {
                    vDni = rs.getString("dni");
                    vDni = padl(vDni, 9);
                    vNom = rs.getString("nombre");
                    vNom = padl(vNom, 22);// Para que quede bien en columna del mismo tamaño
                    vTelefono = rs.getString("telefono");
                    vTelefono = padl(vTelefono, 16);
                    vEmail = rs.getString("email");
                    vEmail = padl(vEmail, 30);
                    vPasword = rs.getString("password");
                    vPasword = padl(vPasword, 15);
                    vPen = rs.getString("penalizacion");
                    vPen = padl(vPen, 16);
                    vFechaIni = rs.getString("fecha_inicio_penalizacion");
                    vFechaIni = padl(vFechaIni, 16);
                    vFechaFin = rs.getString("fecha_fin_penalizacion");
                    vFechaFin = padl(vFechaFin, 20);
                    System.out.println(vDni + " | " + vNom + " | " + vEmail + "| " + vTelefono + "| " + vPasword + " |"
                            + vPen + "|"
                            + vFechaIni + "|" + vFechaFin);

                }
            } catch (SQLException e) {
                System.out.println("Problema al ejecutar sql " + sql + e.getErrorCode() + " " + e.getMessage());
            }
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                                              [-] Página Anterior                                                    [X] Salir  ║");
            Io.sop("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            int opc = Io.leerInt("Muevete por la tabla.");
            switch (opc) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    nPag--;
                    if (nPag <= 0) {
                        nPag = 1;
                    }
                    break;
                case 'x' | 'X':
                    salir = true;
                    menuUsuario();
                    break;
                default:
                    salir = true;
                    break;
            }
        }

    }

    //// EJECUTAR SELECT

    public static ResultSet ejecutarSelect(Connection conn, String sql) {
        PreparedStatement st;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            return (rs);
        } catch (SQLException e) {
            System.out.println("Problema al ejecutar sql : " + sql + " " + e.getErrorCode() + " " + e.getMessage());
        }
        return (null);
    }

    // Este metodo sirve para que quede todo en columna ( visualmente mas bonito y
    // ordenado)

    public static String padl(String texto, int longitud) {
        if (texto.length() > longitud) {
            return texto.substring(0, longitud);
        } else {
            while (texto.length() < longitud) {
                texto += " ";
            }
            return texto;
        }
    }

    public static void modificarUsuarioConTabla(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vDni, vNom, vEmail, sql, vPasword, vPen, vFechaIni, vFechaFin, vTelefono;
        while (!salir) {
            offset = (nPag - 1) * nRegPag;
            sql = " select * from usuarios limit " + nRegPag + " offset " + offset + " ";
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                                                           MODIFICACIÓN DE USUARIOS  |  PÁGINA: " + nPag + "                                                    ║");
            Io.sop("╠═════════╦════════════════════════╦═══════════════════════════════╦═════════════════╦═════════════════╦════════════════╦════════════════╦════════════╣");
            Io.sop("║   DNI   ║         NOMBRE         ║             EMAIL             ║     TELÉFONO    ║    PASSWORD     ║  PENALIZACIÓN  ║  FECHA INICIO  ║ FECHA FIN  ║");
            Io.sop("╚═════════╩════════════════════════╩═══════════════════════════════╩═════════════════╩═════════════════╩════════════════╩════════════════╩════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vDni = rs.getString("dni");
                    vDni = padl(vDni, 9);
                    vNom = rs.getString("nombre");
                    vNom = padl(vNom, 22);// Para que quede bien en columna del mismo tamaño
                    vTelefono = rs.getString("telefono");
                    vTelefono = padl(vTelefono, 16);
                    vEmail = rs.getString("email");
                    vEmail = padl(vEmail, 30);
                    vPasword = rs.getString("password");
                    vPasword = padl(vPasword, 15);
                    vPen = rs.getString("penalizacion");
                    vPen = padl(vPen, 16);
                    vFechaIni = rs.getString("fecha_inicio_penalizacion");
                    vFechaIni = padl(vFechaIni, 16);
                    vFechaFin = rs.getString("fecha_fin_penalizacion");
                    vFechaFin = padl(vFechaFin, 20);
                    System.out.println(vDni + " | " + vNom + " | " + vEmail + "| " + vTelefono + "| " + vPasword + " |"
                            + vPen + "|"
                            + vFechaIni + "|" + vFechaFin);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                                              [-] Página Anterior                                                    [X] Salir  ║");
            Io.sop("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el DNI del usuario que deseas modificar: ");
            char opc = Io.leerCaracter();
            switch (opc) {
                case '+':
                    nPag++;
                    break;
                case '-':
                    if (nPag > 1) {
                        nPag--;
                    } else {
                        nPag = 1;
                    }
                    break;
                case 'x' | 'X':
                    salir = true;
                    menuUsuario();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
        String vModificar = Io
                .leerString("¿Estas seguro que quieres modificarlo? Introduce de nuevo dni del usuario:  ");
        if (!Io.comprobarExistencia(conn, "usuarios", "dni", vModificar)) {
            Io.sop(" No existe ningún usuario con ese dni.");
            return;
        }
        Io.sop("¿Qué campo del usuario  deseas modificar?");
        Io.sop("1. Dni");
        Io.sop("2. Nombre");
        Io.sop("3. Telefono");
        Io.sop("4. Email");
        Io.sop("5. Penalizacion");
        Io.sop("6. Fecha inicio Penalizacion");
        Io.sop("7.Fecha fin de penalizacion");
        Io.sop("8. Terminar");

        char opc = Io.leerCaracter();
        String campo = "", nuevoValor = "";

        switch (opc) {
            case '1':
                campo = "dni";
                nuevoValor = Io.ejecutarValidarDni(conn);
                break;
            case '2':
                campo = "nombre";
                nuevoValor = Io.leerString("Introduce el nuevo nombre del usuario");
                break;
            case '3':
                campo = "Telefono";
                nuevoValor = Io.leerString("Introduce el nuevo telefono");
                break;
            case '4':
                campo = "email";
                nuevoValor = validarEmail(conn);
                break;
            case '5':
                campo = "penalizacion";
                nuevoValor = Io.leerString("Introduce la nueva penalizacion Si/No: ");
                break;
            case '6':
                campo = "fecha_inicio_penalizacion";
                nuevoValor = Io.leerString("Introduce la nueva fecha de inicio de la penalizacion ");
                break;
            case '7':
                campo = "fecha_fin_penalizacion";
                nuevoValor = Io.leerString("Introduce la nueva fecha de fin de la penalizacion ");
                break;
            case '8':
                salir = true;
                break;
            default:
                Io.sop("Opción no válida.");
        }

        if (!campo.equals("")) {
            if (ejecutarUpdateCampo(conn, vModificar, campo, nuevoValor)) {
                Io.sop(" Usuario  modificado correctamente.");
            } else {
                Io.sop(" Error al modificar el usuario.");
            }
        }

    }

    //// Modificar dato
    public static boolean ejecutarUpdateCampo(Connection conn, String idAntiguo, String campo, String nuevoValor) {
        String sql = "UPDATE usuarios SET " + campo + " = '" + nuevoValor + "' WHERE dni = '" + idAntiguo + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.sop(" Error con la query: " + sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    

    public static void main(String[] args) {

        menuUsuario();

    }
}



