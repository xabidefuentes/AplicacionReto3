package conexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Io.*;
import conexiones.*;

public class LibroCN {
 private static final Connection conn = null;

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
                    LibroCN.consultaTablaDeleteLibro(conn, opcion, opcion);
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

        int ncambios = 0,vISBN,vNumcopias,vAnioPublicacion;
        String vTitulo,vGenero,vEditorial;

        /*Date vAnioPublicacion;*/
        System.out.println("Comenzamos a introducir los datos");
        vTitulo = Io.leerString("Dime el titulo del libro: ");
        vGenero = Io.leerString("Dime el genero del libro: ");
        vEditorial = Io.leerString("Dime la editorial del libro: ");
        vISBN = Io.leerInt("Dime el ISBN del libro: ");
        vNumcopias = Io.leerInt("Dime el número de copias del libro: ");
        vAnioPublicacion=Io.leerInt("Dime el año de publicacion del libro");     //////////////////////ARREGLARLO
        String sql = "insert into libros (titulo,genero,editorial,isbn,numcopias) values ('"+vTitulo+"','"+vGenero+"','"+vEditorial+"','"+ vISBN+"','"+vNumcopias+"','"+vAnioPublicacion+"')";  /*,'"+vAnioPublicacion+"' */

        //comprobar que no exista el libro
        do{
            vISBN = Io.leerInt("Dime el ISBN del libro:");
            if (Io.comprobarExistenciaInt(conn, "libros", "isbn", vISBN)) {
                Io.sop("El libro ya existe, vuelve a introducirlo");
            }
        } while (Io.comprobarExistenciaInt(conn, "libros", "isbn", vISBN));


        try{
            Statement st = conn.createStatement();   
            ncambios = st.executeUpdate(sql);
            if (ncambios == 0) {
                System.out.println("No se ha añadido el registro");
            } else {
                System.out.println("Registro añadido");
            }
        }
        catch(SQLException e){
            System.out.println("Problema al insertar la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }

        menuLibro();
        return ncambios;

    }

    public static boolean borrarLibro(){
        Connection conn =Io.getConexion();
        Statement st;
        int vISBN= Io.leerInt("Dime el ISBN del libro que deseas borrar :");
        int borrados;
        String sql = "delete from libros where isbn = '"+vISBN+"'";
        try{
            st = conn.prepareStatement(sql);
            borrados = st.executeUpdate(sql);
            return borrados>0;
        }
        catch(SQLException e){
            System.out.println("Problema al borrar: "+sql+e.getErrorCode()+" "+e.getMessage());
            return false;
        }
        
    }


    /*public static int modificarLibro(){

    }*/

    public static void consultarLibroPaginado (Connection conn,int nRegPag,int nPag){
    /*Realizamos la consulta sql para mostrar todos los datos de la tabla, Se mostraran estudiante de 
     * 10 en 10 (nRegPag), los que correspondean a la pagina nPag
     * offset es el desplazamiento dentro del fichero
     * limit : es el numero de registros que voy a leer
     * con + y - avanzare una pagina. La primera pagina sera la 1
     * al pulsar ESC saldre de la consulta */
    Statement stm = null;
    ResultSet rs = null;
    int offset,cont,vISBN,vAnioPublicacion;
    String vTitulo,vGenero,vEditorial,sql;
    boolean salir = false;
    while ( ! salir){//Control de las teclas +, -, ESC
        offset = ( nPag -1)* nRegPag;
        sql = " select * from libros limit " +nRegPag+ " offset "+ offset + " ";
        rs = ejecutarSelect (conn,sql);
        System.out.println(" TABLA DE libros Pag : "+ nPag);
        System.out.println("-------------------------------");
        cont = 0;
        try {
            while ( rs.next()){
                vTitulo = rs.getString("titulo");
                vTitulo= pad2(vTitulo,50);
                vGenero = rs.getString("genero");
                vGenero = pad2 ( vGenero,50);
                vEditorial = rs.getString("editorial");
                vEditorial = pad2 ( vGenero,50);
                vAnioPublicacion = rs.getInt("anioPublicacion");
                vISBN = rs.getInt("ISBN");
                System.out.println(pad2(cont++ +".-",5)+ vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"|"+vISBN);
                cont++;
            }   
        }catch (SQLException  e){
            System.out.println("Problema al ejecutar sql "+ sql+ e.getErrorCode()+ " "+e.getMessage());
        }
        System.out.println("[+] Pag.Sig, [-] Pag. Ant, [x] Abandonar, [Num] seleccionar");
        int opc = Io.leerInt("Elige una opcion");
        switch (opc){
            case '+' : nPag++;
                break;
            case '-' : nPag--;
                if(nPag <=0){
                    nPag = 1;
                }
                break;
            case 'x' : salir=true;
                break;
        }
    }
    
    }

    ////EJECUTAR SELECT

    public static ResultSet ejecutarSelect (Connection conn,String sql){
        PreparedStatement st;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            return (rs);
        }catch (SQLException e){
            System.out.println("Problema al ejecutar sql : "+sql +" "+e.getErrorCode()+ " "+ e.getMessage());
        }
        return (null);
    }

    //Este metodo sirve para que quede todo en columna ( visualmente mas bonito y ordenado)

    public static String pad2 (String texto, int longitud){
        if( texto.length() >longitud){
            return texto.substring(0,longitud);
        }else{
            while(texto.length()< longitud){
                texto += " ";
            }
            return texto;
        }
    }

    public static void consultaTablaDeleteLibro (Connection conn, int totalRegistros, int pagina) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        String Titulo = "", ISBN = "", Genero = "", Aniopublicacion = "", editorial = "", swisbnSeleccionado = null;
        int offset, posicion = 0;
        String[] aISBN = new String[totalRegistros];
        while (!salir) {
            offset = (pagina - 1) * totalRegistros;
            String sql = "SELECT * FROM libros LIMIT " + totalRegistros + " OFFSET " + offset;
            Io.sop("╔═════════════════════════════════════════════════════════════════════════╗");
            Io.sop(  "║                    ELIMINAR LIBROS  |  PÁGINA: " + pagina + "                        ║");
            Io.sop("╠═════╦════════════════╦══════════════════╦═══════════════╦═══════════════╣");
            Io.sop("║ISBN ║     Titulo     ║      Genero      ║AÑO PUBLICACION║   Editorial   ║");
            Io.sop("╚═════╩════════════════╩══════════════════╩═══════════════╩═══════════════╝");



            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);

                int cont = 0; // Aquí empieza desde el número correcto
                while (rs.next()) {
                    ISBN = pad2(rs.getString("ISBN"), 20);
                    Titulo = pad2(rs.getString("titulo"), 50);
                    Genero = pad2(rs.getString("genero"), 50);
                    editorial = pad2(rs.getString("editorial"), 50);
                    Aniopublicacion = pad2(rs.getString("aniopublicacion"), 13);
                    aISBN[cont] = ISBN;
                    Io.sop(ISBN + " | " +Titulo + " | " + Genero + " | " + Aniopublicacion);
                    cont++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
            Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el ID del préstamo que deseas eliminar: ");
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
                    break;
                default:
                    salir = true;
                    break;
            }
        }
        swisbnSeleccionado = Io.leerString("¿Estas seguro que quieres eliminarlo? Introduce de nuevo ISBN del libro:");
        ejecutarDelete(conn, swisbnSeleccionado);
        Io.sop("Libro con ISBN: " + swisbnSeleccionado + " eliminado correctamente");
        LibroCN.menuLibro();
    }

    public static boolean ejecutarDelete (Connection conn, String ISBN) {
        String sql = "DELETE FROM libros WHERE isbn = " + ISBN;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Io.Sop("Problema: "+sql);
            e.printStackTrace();
            return false;
        }
        return true;
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
                    LibroCN.consultaTablaDeleteLibro(conn, 10,1);
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
