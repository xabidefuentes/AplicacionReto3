package conexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import Io.*;
import conexiones.*;

public class AutorCN {
    //FUNCIONES//

    //MENU AUTORES//

    public static void menuAutores(){
        Io.sop("***********************************************************************");
        Io.sop("************************  GESTION DE AUTORES  ************************");
        Io.sop("*********************  DE LA BIBLIOTECA MUNICIPAL *********************");
        Io.sop("****************************   DE MUSKIZ  ****************************");
        Io.sop("***********************************************************************");
        Io.sop("1. ➕AGREGAR AUTOR");
        Io.sop("2. ➖BORRAR AUTOR");
        Io.sop("3. ✏️MODIFICAR AUTOR");
        Io.sop("4. 🚶‍♂️SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                AutorCN.insertarAutor();
                break;
            case 2:
                AutorCN.borrarAutor(null, opcion);        
                break;
            case 3:
                
                break;
            case 4:
            Io.sop("Saliendo...");
                break;
            default:
                Io.sop("Opción no válida. Intenta otra vez.");
        }
    }

    //INSERTAR AUTOR//

    public static int insertarAutor(){
        Connection conn = Io.getConexion();
        int cambios = 0;
        int id_autor;
        String nombre,apellido,nacionalidad;
        LocalDate fechaNac;
        nombre = Io.leerString("Dime el nombre del autor: ");//Leemos del teclado
        apellido = Io.leerString("Dime el apellido del autor: ");//Leemos del teclado
        nacionalidad = Io.leerString("Dime de donde es/era el autor: ");//Leemos del teclado
        fechaNac = Io.leerDate("Dime cuando nació el autor: ");//Leemos del teclado
        //Comprobacion para ver si el id_autor esta repetido
        do {
            id_autor = Io.leerInt("Dime el ID del autor que quieras ponerle: ");
            if (Io.comprobarExistenciaInt(conn, "autores", "id_autor", id_autor)) {
                Io.sop("ID ya existente, introduce otro");
            }
        } while (Io.comprobarExistenciaInt(conn, "autores", "id_autor", id_autor));
        String sql = "INSERT INTO usuarios (id_autor, nombre, apellido, nacionalidad, fechaNac) " +
        "VALUES ('" + id_autor + "', '" + nombre + "', '" + apellido + "', '" + nacionalidad + "', '" + fechaNac + "')";       
        try{
            Statement st = conn.createStatement();   // es para que se agrege a la bbdd
            cambios = st.executeUpdate(sql);
            if(cambios == 0){
                System.out.println("No se ha añadido el registro");
            }
                else{
                    System.out.println("Registro añadido");
                }
        }
        catch(SQLException e){
            System.out.println("Problema al insertar la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }
        return cambios;

    }

    //BORRAR AUTOR//

    public static boolean borrarAutor(Connection conn, int pAutor){
        PreparedStatement st;
        int borrados;
        String sql="DELETE FROM libros WHERE USUCOD='"+pAutor+"'";
        try{
            st= conn.prepareStatement(sql);
            borrados=st.executeUpdate();
            return borrados>0;
        }catch (SQLException e){
            System.out.println("PROBLEMA AL BORRAR:"+sql+e.getErrorCode()+e.getMessage());
            return false;
        }

    }

    //MODIFICAR AUTOR//

    public static void modificarUsuarioConTabla(Connection conn, int nRegPag, int nPag) {
    Statement stm = null;
    ResultSet rs = null;
    boolean salir = false;
    int offset;
    String vDni,vNom,vEmail,sql,vPasword,vPen,vFechaIni,vFechaFin,vTelefono;
    while (!salir) {
        offset = ( nPag -1)* nRegPag;
        sql = " select * from usuarios limit " +nRegPag+ " offset "+ offset + " ";
        Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        Io.sop(  "║                                         LISTADO DE AUTORES  |  PÁGINA: " + nPag + "                                                                   ║");
        Io.sop("╠═════════╦════════════════════════╦══════════════════════════╦══════════════════════╦═════════════════╦════════════════╦════════════════╦════════════╣");
        Io.sop("║ DNI     ║       NOMBRE           ║        EMAIL             ║        TELEFONO      ║     PASSWORD    ║ PENALIZACIÓN   ║ FECHA INICIO   ║ FECHA FIN  ║");
        Io.sop("╚═════════╩════════════════════════╩══════════════════════════╩══════════════════════╩═════════════════╩════════════════╩════════════════╩════════════╝");
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                vDni = rs.getString("dni");
                vDni= padl(vDni,10);
                vNom = rs.getString("nombre");
                vNom = padl ( vNom,20);// Para que quede bien en columna del mismo tamaño
                vTelefono = rs.getString("telefono");
                vTelefono = padl(vTelefono,23);
                vEmail = rs.getString("email");
                vEmail = padl(vEmail, 27);
                vPasword = rs.getString("password");
                vPasword= padl(vPasword,15);
                vPen =rs.getString("penalizacion");
                vPen = padl(vPen,15);
                vFechaIni =rs.getString("fecha_inicio_penalizacion");
                vFechaIni = padl(vFechaIni, 15);
                vFechaFin = rs.getString("fecha_fin_penalizacion");
                vFechaFin = padl(vFechaFin,20);
                System.out.println( vDni +" | "+ vNom +" | "+ vEmail+"| "+vTelefono+"| "+vPasword+" |"+vPen+"|"
                +vFechaIni+"|"+vFechaFin);
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
        Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
        Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
        Io.sop("Muevete por la tabla y selecciona el dni del usuario que deseas modificar: ");
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
                break;
            default:
                salir = true;
                break;
        }
    }
     String vModificar = Io.leerString("¿Estas seguro que quieres modificarlo? Introduce de nuevo dni del usuario:  ");
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
                nuevoValor = Io.leerString("Introduce el nuevo dni del usuario: ");
                break;
            case '2':
                campo = "nombre";
                nuevoValor = Io.leerString("Introduce el nuevo nombre del usuario");
                break;
            case '3':
                campo = "Telefono";
                nuevoValor =Io.leerString("Introduce el nuevo telefono");
                break;
            case '4':
                campo = "email";
                nuevoValor = Io.leerString("Introduce el nuevo email ");
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
        UsuarioCN.menuUsuario();

    }

}
