package conexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

}
