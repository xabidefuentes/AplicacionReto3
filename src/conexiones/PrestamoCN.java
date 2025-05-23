package conexiones;
import Io.*;
import static Io.Io.*;
import static conexiones.UsuarioCN.menuUsuario;
import static conexiones.UsuarioCN.padl;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import principal.Busquedas;
import principal.Prestamo;
import principal.main;

public class PrestamoCN {
    public static void añadirPrestamo() {
        Connection conn = getConexion();
        if (conn == null) {
            sop("Error al conectar a la base de datos.");
            Prestamo.menuPrestamo();
        }
        String dni;
        String idEjemplar = "";
        String dniEmpleado;

        // Comprobar si el usuario existe
        do {
            UsuarioCN.consultarUsuarioPaginado(conn, 5, 1);
            dni = leerString("Ingresa de nuevo el DNI del usuario: ");
            if (dni == null || dni.isEmpty()) {
                sop("Saliendo...");
                Prestamo.menuPrestamo();
            }
            dni = dni.toUpperCase();
            if (!comprobarExistencia(conn, "usuarios", "dni", dni)) {
                sop("No existe ningún usuario con ese DNI.");
            }
            if (!validarDni(dni)) {
                sop("DNI inválido. Debe tener 8 dígitos y una letra al final.");
            }
            if (comprobarUsuarioPenalizado(conn, dni)) {
                int dias = comprobarDiasPenalizado(conn, dni);
                sop("El usuario se encuentra penalizado. Le quedan " + dias + " dias para no estar penalizado.");
                main.menuPrincipal();
            }
            if (comprobarMasDe3Prestamos(conn, dni)) {
                sop("El usuario tiene mas de 3 prestamos.");
                main.menuPrincipal();
            }
        } while (!comprobarExistencia(conn, "usuarios", "dni", dni));

        String fechaPrestamo;
        do {
            fechaPrestamo = leerString("Ingresa la fecha de préstamo (YYYY-MM-DD): ");
            if (fechaPrestamo == null || fechaPrestamo.isEmpty()) {
                sop("Saliendo...");
                Prestamo.menuPrestamo();
            }
            if (!esFechaValida(fechaPrestamo)) {
                sop("Fecha inválida. Debe ser en el formato YYYY-MM-DD.");
            }
        } while (!esFechaValida(fechaPrestamo));

        // Comprobar si el ejemplar existe
        String nombreLibro;
        do {
            LibroCN.consultarLibroPaginado(conn, 5, 1);
            nombreLibro = leerString("Ingresa de nuevo el nombre del libro: ");
            if (nombreLibro == null || nombreLibro.isEmpty()) {
                sop("Saliendo...");
                Prestamo.menuPrestamo();
            }
            if (!comprobarExistencia(conn, "libros", "titulo", nombreLibro)) {
                sop("No existe ningún libro con ese título.");
                idEjemplar = null;
                continue;
            }
            idEjemplar = obtenerIdEjemplarPorNombre(conn, nombreLibro);
            if (idEjemplar == null) {
                sop("No hay ejemplares disponibles para ese libro.");
                continue;
            }
            if (buscarPrestamoPorEjemplar(conn, idEjemplar) > 0) {
                sop("El ejemplar ya está prestado. Elige otro.");
                idEjemplar = null;
            }

        } while (idEjemplar == null);

        // Comprobar si el empleado existe
        do {
            consultaTablaEmpleados(conn, 5,1);
            dniEmpleado = leerString("Ingresa de nuevo el DNI del empleado: ");
            if (dniEmpleado == null || dniEmpleado.isEmpty()) {
                sop("Saliendo...");
                Prestamo.menuPrestamo();
            }
            if (!comprobarExistencia(conn, "empleados", "dni", dniEmpleado)) {
                sop("No existe ningún empleado con ese DNI.");
            }
        } while (!comprobarExistencia(conn, "empleados", "dni", dniEmpleado));

        // Comprobar si el ejemplar ya está prestado
        if (ejecutarInsert(conn, dni.toUpperCase(), parsearFecha(fechaPrestamo), idEjemplar, dniEmpleado.toUpperCase())) {
            sop("Préstamo añadido correctamente.");
            cambiarEstadoEjemplar(conn, idEjemplar);
        } else {
            sop("Error al añadir el préstamo.");
        }
        Prestamo.menuPrestamo();
    }

    public static boolean ejecutarInsert(Connection conn, String dni, LocalDate fechaPrestamo, String idEjemplar, String dniEmpleado) {
        String sql = "INSERT INTO prestamos (id_prestamo, fecha_prestamo, fecha_devolucion, fk_dni_usuario, fk_id_ejemplar, fk_dni_empleado) " +
                "VALUES ('" +  generarId() + "', '" + fechaPrestamo + "', '1000-10-10', '" + dni + "', '" + idEjemplar + "', '" + dniEmpleado + "')";
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

    public static boolean comprobarUsuarioPenalizado(Connection conn, String dniUsuario) {
        String query = "SELECT penalizacion FROM usuarios WHERE dni = '" + dniUsuario + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String swPenalizacion = rs.getString("penalizacion");
                return "SI".equalsIgnoreCase(swPenalizacion);
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return false;
    }
    public static int comprobarDiasPenalizado(Connection conn, String dniUsuario) {
        String query = "SELECT fecha_inicio_penalizacion, fecha_fin_penalizacion FROM usuarios WHERE dni = '" + dniUsuario + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                LocalDate fechaInicio = rs.getDate("fecha_inicio_penalizacion").toLocalDate();
                LocalDate fechaFin = rs.getDate("fecha_fin_penalizacion").toLocalDate();
                LocalDate hoy = LocalDate.now();

                if ((hoy.isEqual(fechaInicio) || hoy.isAfter(fechaInicio)) && hoy.isBefore(fechaFin)) {
                    return (int) ChronoUnit.DAYS.between(hoy, fechaFin);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return 0;
    }

    public static boolean comprobarMasDe3Prestamos(Connection conn, String dniUsuario) {
        String query = "SELECT COUNT(*) AS total FROM prestamos WHERE fk_dni_usuario = '" + dniUsuario + "' AND fecha_devolucion IS NULL";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int total = rs.getInt("total");
                return total > 3;
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return false;
    }


    public static int generarId() {
        Connection conn = getConexion();
        int id = 1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;
            while (id>0) {
                String sql = "SELECT COUNT(*) FROM prestamos WHERE id_prestamo = " + id;
                rs = stmt.executeQuery(sql);
                if (rs.next() && rs.getInt(1) == 0) {
                    return id;
                }
                id++;
            }
        } catch (SQLException e) {
            sop("Error al generar ID: " + e.getMessage());
            return -1;
        }
        return 0;
    }
    public static void borrarPrestamo(){
        Connection conn = getConexion();
        if (conn == null) {
            sop("Error al conectar a la base de datos.");
            Prestamo.menuPrestamo();
        }
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

    private static String obtenerIdEjemplarPorNombre(Connection conn, String nombreLibro) {
        String query = "SELECT id_ejemplar FROM ejemplares WHERE fk_isbn = " +
                "(SELECT isbn FROM libros WHERE titulo = '" + nombreLibro + "') AND estado = 'DISPONIBLE'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String id = rs.getString("id_ejemplar");
                return (id);
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return null;
    }

    private static int buscarPrestamoPorEjemplar(Connection conn, String idEjemplar) {
        String query = "SELECT COUNT(*) FROM prestamos WHERE fk_id_ejemplar = " + idEjemplar + " AND fecha_devolucion IS NULL";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return 0;
    }

    public static String buscarPrestamoPorId (Connection conn, String idPrestamo) {
        String query = "SELECT titulo FROM libros WHERE isbn = (SELECT fk_isbn FROM ejemplares WHERE id_ejemplar = (SELECT fk_id_ejemplar FROM prestamos WHERE id_prestamo = '" + idPrestamo + "'))";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString("titulo");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return null;
    }

    public static String mostrarPenalización(Connection conn, String fechaDevolucion, String dniUsuario) {
        if (fechaDevolucion.equals("1000-10-10")) {
            return "ACTIVO";
        } else {
            if (estaPenalizado(conn, dniUsuario)) {
                return "PENALIZADO";
            } else {
                return fechaDevolucion;
            }
        }
    }
    public static boolean estaPenalizado(Connection conn, String dniUsuario) {
        String query = "SELECT penalizacion FROM usuarios WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dniUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "SI".equalsIgnoreCase(rs.getString("penalizacion"));
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return false;
    }
    public static String buscarUsuarioPorDni (Connection conn, String dni) {
        String query = "SELECT nombre FROM usuarios WHERE dni = '" + dni + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return null;
    }

    public static String buscarEmpleadoPorDni (Connection conn, String dni) {
        String query = "SELECT nombre FROM empleados WHERE dni = '" + dni + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return null;
    }


    private static void cambiarEstadoEjemplar (Connection conn, String idEjemplar) {
        String sql = "UPDATE ejemplares SET estado = 'PRESTADO' WHERE id_ejemplar = '" + idEjemplar + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            sop("❌ Error al cambiar el estado del ejemplar: " + sql);
            e.printStackTrace();
        }
    }
    public static void consultaTablaEmpleados(Connection conn, int nRegPag, int nPag) {
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

            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                                              [-] Página Anterior                                                    [X] Salir  ║");
            Io.sop("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el DNI del empleado: ");
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
                    Prestamo.menuPrestamo();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }
    public static void consultaTablaPrestamo (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String idPrestamo = "", fechaPrestamo = "", fechaDevolucion = "", dniUsuario = "", idEjemplar = "", dniEmpleado = "", swIdSeleccionado = "", tituloLibro = "", nombreUsuario = "", nombreEmpleado = "";
        int offset, posicion = 0;
        String[] aId = new String[totalRegistros];
        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT * FROM prestamos LIMIT " + totalRegistros + " OFFSET " + offset;
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
            sop("╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║ [+] Página Siguiente                                                 [-] Página Anterior                                                                  [X] Salir  ║");
            sop("╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
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
                    Prestamo.menuPrestamo();
                    break;
                default:
                    salir = true;
                    break;
            }

        }
        String idAntiguo = leerString("Introduce de nuevo el ID del préstamo que quieres modificar: ");
        if (!comprobarExistencia(conn, "prestamos", "id_prestamo", idAntiguo)) {
            sop("No existe ningún préstamo con ese ID.");
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

        int opc = leerInt("");
        String campo = "", nuevoValor = "";

        switch (opc) {
            case 1:
                campo = "id_prestamo";
                do {
                    nuevoValor = leerString("Introduce el nuevo ID del préstamo: ");
                    if (comprobarExistencia(conn, "prestamos", "id_prestamo", nuevoValor)) {
                        sop("Ya existe un préstamo con ese ID.");
                    }
                } while (comprobarExistencia(conn, "prestamos", "id_prestamo", nuevoValor));
                break;
            case 2:
                campo = "fecha_prestamo";
                do {
                    nuevoValor = leerString("Introduce la nueva fecha de préstamo (YYYY-MM-DD): ");
                    if (!esFechaValida(nuevoValor)) {
                        sop("Fecha inválida. Debe ser en el formato YYYY-MM-DD.");
                    }
                } while (!esFechaValida(nuevoValor));
                break;
            case 3:
                campo = "fecha_devolucion";
                do {
                    nuevoValor = leerString("Introduce la nueva fecha de devolución (YYYY-MM-DD): ");
                    if (!esFechaValida(nuevoValor)) {
                        sop("Fecha inválida. Debe ser en el formato YYYY-MM-DD.");
                    }
                } while (!esFechaValida(nuevoValor));
                break;
            case 4:
                campo = "fk_dni_usuario";
                do {
                    nuevoValor = leerString("Introduce el nuevo DNI del usuario: ");
                    if (!Io.validarDni(nuevoValor)) {
                        sop("DNI inválido. Debe tener 8 dígitos y una letra al final.");
                    } else if (!comprobarExistencia(conn, "usuarios", "dni", nuevoValor)) {
                        sop("No existe ningún usuario con ese DNI.");
                    }
                } while (!Io.validarDni(nuevoValor) || !comprobarExistencia(conn, "usuarios", "dni", nuevoValor));
                break;
            case 5:
                campo = "fk_id_ejemplar";
                do {
                    nuevoValor = leerString("Introduce el nuevo ID del ejemplar: ");
                    if (!comprobarExistencia(conn, "ejemplares", "id_ejemplar", nuevoValor)) {
                        sop("No existe ningún ejemplar con ese ID.");
                    }
                } while (!comprobarExistencia(conn, "ejemplares", "id_ejemplar", nuevoValor));
                break;
            case 6:
                campo = "fk_dni_empleado";
                do {
                    nuevoValor = leerString("Introduce el nuevo DNI del empleado: ");
                    if (!Io.validarDni(nuevoValor)) {
                        sop("DNI inválido. Debe tener 8 dígitos y una letra al final.");
                    } else if (!comprobarExistencia(conn, "empleados", "dni", nuevoValor)) {
                        sop("No existe ningún empleado con ese DNI.");
                    }
                } while (!Io.validarDni(nuevoValor) || !comprobarExistencia(conn, "empleados", "dni", nuevoValor));
                break;
            case 7:
                salir = true;
                break;
            default:
                sop("Opción no válida.");
        }

        if (!campo.equals("")) {
            if (ejecutarUpdateCampo(conn, idAntiguo, campo, nuevoValor)) {
                sop("Préstamo con ID " + idAntiguo + " modificado correctamente.");
            } else {
                sop("Error al modificar el préstamo.");
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
            sop("Error con la query: " + sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void consultaTablaDelete (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String idPrestamo = "", fechaPrestamo = "", fechaDevolucion = "", dniUsuario = "", idEjemplar = "", dniEmpleado = "", swIdSeleccionado = "", tituloLibro = "", nombreUsuario = "", nombreEmpleado = "";
        int offset, posicion = 0;
        String[] aId = new String[totalRegistros];
        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT * FROM prestamos LIMIT " + totalRegistros + " OFFSET " + offset;
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
            sop("╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            sop("║ [+] Página Siguiente                                                 [-] Página Anterior                                                                  [X] Salir  ║");
            sop("╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
            sop("Muevete por la tabla y selecciona el ID del préstamo que deseas eliminar: ");
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
        swIdSeleccionado = leerString("¿Estas seguro que quieres eliminarlo? Introduce de nuevo ID del préstamo: ");
        if (!comprobarExistencia(conn, "prestamos", "id_prestamo", swIdSeleccionado)) {
            sop("No existe ningún préstamo con ese ID.");
            consultaTablaDelete(conn, 5, 1);
        }
        ejecutarDelete(conn, swIdSeleccionado);
        sop("Préstamo con ID: " + swIdSeleccionado + " eliminado correctamente.");
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
