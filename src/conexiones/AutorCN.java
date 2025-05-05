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
        String sql="DELETE FROM autores WHERE USUCOD='"+pAutor+"'";
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
    String vID, vNom, vApe, vFN, vNac;
    while (!salir) {
        offset = ( nPag -1)* nRegPag;
        String sql = " select * from autores limit " +nRegPag+ " offset "+ offset + " ";
        Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        Io.sop(  "║                                         LISTADO DE AUTORES  |  PÁGINA: " + nPag + "                                                                 ║");
        Io.sop("╠══════════════╦════════════════════════╦═══════════════════════════╦═══════════════════════════╦═════════════════════════════════════════════════════╣");
        Io.sop("║ ID_Autor     ║       NOMBRE           ║        APELLIDOS          ║     FECHA NACIMIENTO      ║              NACIONALIDAD                           ║");
        Io.sop("╚══════════════╩════════════════════════╩═══════════════════════════╩═══════════════════════════╩═════════════════════════════════════════════════════╝");
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                vID = rs.getString("id_autor");
                vID = Io.PADL(vID,10);
                vNom = rs.getString("nombre");
                vNom = Io.PADL( vNom,20);
                vApe = rs.getString("apellidos");
                vApe = Io.PADL(vApe,25);
                vFN = rs.getString("fecha_nacimiento");
                vFN = Io.PADL(vFN, 10);
                vNac = rs.getString("nacionalidad");
                vNac = Io.PADL(vNac,20);
                System.out.println( vID +" | "+ vNom +" | "+ vApe+"| "+vFN+"| "+vNac);
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
     String vModificar = Io.leerString("¿Estas seguro que quieres modificarlo? Introduce de nuevo el id_autor:  ");
     if (!Io.comprobarExistencia(conn, "autores", "id_autor", vModificar)) {
            Io.sop(" No existe ningún autor con ese ID.");
            return;
        }
        Io.sop("¿Qué campo del usuario  deseas modificar?");
        Io.sop("1. ID_Autor");
        Io.sop("2. Nombre");
        Io.sop("3. Apellidos");
        Io.sop("4. Fecha Nacimiento");
        Io.sop("5. Nacionalidad");

        char opc = Io.leerCaracter();
        String campo = "", nuevoValor = "";

        switch (opc) {
            case '1':
                campo = "id_autor";
                nuevoValor = Io.leerString("Introduce el nuevo id_autor: ");
                break;
            case '2':
                campo = "nombre";
                nuevoValor = Io.leerString("Introduce el nuevo nombre");
                break;
            case '3':
                campo = "apellidos";
                nuevoValor =Io.leerString("Introduce el nuevo apellido");
                break;
            case '4':
                campo = "fecha_nacimiento";
                nuevoValor = Io.leerString("Introduce la nuevo fecha nacimiento");
                break;
            case '5':
                campo = "nacionalidad";
                nuevoValor = Io.leerString("Introduce la nueva nacionalidad");
                break;
            case '6':
                salir = true;
                break;
            default:
                Io.sop("Opción no válida.");
        }

        if (!campo.equals("")) {
            if (ejecutarUpdateCampo(conn, vModificar, campo, nuevoValor)) {
                Io.sop(" Usuario  modificado correctamente.");
            } else {
                Io.sop(" Error al modificar el autor.");
            }
        }
        UsuarioCN.menuUsuario();

    }

    public static boolean ejecutarUpdateCampo(Connection conn, String idAntiguo, String campo, String nuevoValor) {
        String sql = "UPDATE autores SET " + campo + " = '" + nuevoValor + "' WHERE id_autor = '" + idAntiguo + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.sop("❌ Error con la query: " + sql);
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
