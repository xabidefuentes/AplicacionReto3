package conexiones;
import java.sql.Connection;
import java.sql.Date;
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
        Io.sop("********************  DE LA BIBLIOTECA MUNICIPAL ********************");
        Io.sop("***************************   DE MUSKIZ  ***************************");
        Io.sop("***********************************************************************");
        Io.sop("1. AGREGAR AUTOR");
        Io.sop("2. BORRAR AUTOR");
        Io.sop("3. MODIFICAR AUTOR");
        Io.sop("4. SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                AutorCN.insertarAutor();
                break;
            case 2:
                    
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

    //INSERTAR Autor//

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
        //Comprobacion para ver si el autor esta repetido
        do {
            vDni = Io.leerString("Dime el dni del usuario: ");
            if (Io.comprobarExistencia(conn, "usuarios", "dni", vDni)) {
                Io.sop("Dni ya existe, vuelve a introducirlo");
            }
        } while (Io.comprobarExistencia(conn, "usuarios", "dni", vDni));
        
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

}
