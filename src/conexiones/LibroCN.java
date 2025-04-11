package conexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import Io.*;
import conexiones.*;

public class LibroCN {
    //FUNCIONES
    //Menu usuario
    public static void menuLibro(){
    Io.sop("***********************************************************************");
            Io.sop("********************  GESTION DE LIBROS  ****************");
            Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
            Io.sop("**************************  DE MUSKIZ  ************************************");
            Io.sop("1. AGREGAR LIBRO");
            Io.sop("2. BORRAR LIBRO");
            Io.sop("3. MODIFICAR LIBRO");
            Io.sop("4. SALIR");
            int opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    LibroCN.insertarLibro();
                    break;
                case 2:
                    /*LibroCN.borrarLibro();*/
                    break;
                case 3:
                    /*LibroCN.modificarLibro();*/
                    break;
                case 4:
                Io.sop("Saliendo...");
                    break;
                default:
                    Io.sop("Opción no válida. Intenta otra vez.");
            }
        }


    //INSERTAR LIBROS

    public static int insertarLibro(){
        Connection conn =Io.getConexion();
        int ncambios = 0,vId_libro,vISBN,vNumcopias,vAnioPublicacion;
        String vTitulo,vGenero,vEditorial;
        /*Date vAnioPublicacion;*/
        System.out.println("Comenzamos a introducir los datos");
        vTitulo = Io.leerString("Dime el titulo del libro: ");
        vGenero = Io.leerString("Dime el genero del libro: ");
        vEditorial = Io.leerString("Dime la editorial del libro: ");
        vISBN = Io.leerInt("Dime el ISBN del libro: ");
        vNumcopias = Io.leerInt("Dime el número de copias del libro: ");
        vId_libro = Io.leerInt("Dime el ID del libro: ");
        vAnioPublicacion=Io.leerInt("Dime el año de publicacion del libro");     //////////////////////ARREGLARLO
        String sql = "insert into libros (titulo,genero,editorial,isbn,numcopias,id_libro) values ('"+vTitulo+"','"+vGenero+"','"+vEditorial+"','"+ vISBN+"','"+vNumcopias+"','"+vId_libro+"','"+vAnioPublicacion+"')";  /*,'"+vAnioPublicacion+"' */
        try{
            Statement st = conn.createStatement();   
            ncambios = st.executeUpdate(sql);
        }
        catch(SQLException e){
            System.out.println("Problema al insertar la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }
        return ncambios;

    }

    public static boolean borrarLibro(Connection conn, int id_libro){
            PreparedStatement st;
            int borrados;
            String sql="DELETE FROM libros WHERE USUCOD='"+id_libro+"'";
            try{
                st= conn.prepareStatement(sql);
                borrados=st.executeUpdate();
                return borrados>0;
            }catch (SQLException e){
                System.out.println("PROBLEMA AL BORRAR:"+sql+e.getErrorCode()+e.getMessage());
                return false;
            }
        
    }


    /*public static int modificarLibro(){

    }*/



    ////
    /// 
    /*public static void main(String[] args) {
        Io.sop("***********************************************************************");
            Io.sop("********************  GESTION DE LIBROS  ****************");
            Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
            Io.sop("**************************   DE MUSKIZ  ************************************");
            Io.sop("1. AGREGAR LIBRO");
            Io.sop("2. BORRAR LIBRO");
            Io.sop("3. MODIFICAR LIBRO");
            Io.sop("4. SALIR");
            int opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    LibroCN.insertarLibro();
                    break;
                case 2:
                    LibroCN.borrarLibro();
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

*/
       
}
