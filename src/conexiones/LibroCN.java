package conexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.naming.NoPermissionException;

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
                    LibroCN.borrarLibro();
                    break;
                case 3:
                    LibroCN.modificarLibro();
                    break;
                case 4:
                Io.sop("Saliendo...");
                    break;
                default:
                    Io.sop("Opción no válida. Intenta otra vez.");
            }
        }

        //Metodo para borrar usuario
        public static void borrarLibro(){
            Connection conn = Io.getConexion();
            consultaTablaDeleteLibro(conn, 10, 1);
            Io.cerrarConexion(conn);
        }
    
    
        public static void modificarLibro(){
            Connection conn = Io.getConexion();
            modificarLibroConTabla(conn, 5, 1);
            Io.cerrarConexion(conn);
        }
    //INSERTAR LIBROS

    public static int insertarLibro(){
        Connection conn =Io.getConexion();

        int ncambios = 0,vISBN,vAnioPublicacion;
        String vTitulo,vGenero,vEditorial;

        
        System.out.println("Comenzamos a introducir los datos");
        vTitulo = Io.leerString("Dime el titulo del libro: ");
        vGenero = Io.leerString("Dime el genero del libro: ");
        vEditorial = Io.leerString("Dime la editorial del libro: ");
        vISBN = Io.leerInt("Dime el ISBN del libro: ");
        vAnioPublicacion=Io.leerInt("Dime el año de publicacion del libro:");     //////////////////////ARREGLARLO
        String sql = "insert into libros (isbn,titulo,genero,editorial,ano,fk_id_autor) values ('"+vISBN+"','"+vTitulo+"','"+vGenero+"','"+ vEditorial+"','"+vAnioPublicacion+"','" + (int) (Math.random() * 1000)+ "')";  

        //comprobar que no exista el libro ESTO LO TENGO QUE COMPROBAR PARA QUE NO LO PIDE CONTINUAMENTE
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

    /*public static boolean borrarLibro(){
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
*/
    public static void consultarLibroPaginado (Connection conn,int nRegPag,int nPag){
    /*Realizamos la consulta sql para mostrar todos los datos de la tabla, Se mostraran estudiante de 
     * 10 en 10 (nRegPag), los que correspondean a la pagina nPag
     * offset es el desplazamiento dentro del fichero
     * limit : es el numero de registros que voy a leer
     * con + y - avanzare una pagina. La primera pagina sera la 1
     * al pulsar ESC saldre de la consulta */
    Statement stm = null;
    ResultSet rs = null;
    int offset,cont,vISBN,vAnioPublicacion,vidAutor;
    String vTitulo,vGenero,vEditorial,sql;
    boolean salir = false;
    while ( ! salir){//Control de las teclas +, -, ESC
        offset = ( nPag -1)* nRegPag;
        sql = " select * from libros limit " +nRegPag+ " offset "+ offset + " ";
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop(  "║                            Listado LIBROS  |  PÁGINA: " + nPag + "                      ║");
            Io.sop("╠═════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦═══════════════╣");
            Io.sop("║ISBN ║     Titulo     ║      Genero      ║AÑO PUBLICACION║   Editorial   ║    ID AUTOR   ║ ");
            Io.sop("╚═════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩═══════════════╝");
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
                vidAutor = rs.getInt("ID Autor");
                System.out.println(pad2(cont++ +".-",5)+ vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"|"+vISBN+"| "+vidAutor);
                cont++;
            }   
        }catch (SQLException  e){
            System.out.println("Problema al ejecutar sql "+ sql+ e.getErrorCode()+ " "+e.getMessage());
        }
        Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
        Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
        Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
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

    public static void consultaTablaDeleteLibro (Connection conn, int nRegpag, int npag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset,vISBN,vAnioPublicacion,vidAutor;
        String vTitulo,vGenero,vEditorial,sql;
        while (!salir) {
            offset = (npag - 1) * nRegpag;
            sql = "SELECT * FROM libros LIMIT " + nRegpag + " OFFSET " + offset;
            Io.sop("╔═════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop(  "║                            Listado LIBROS  |  PÁGINA: " + nRegpag + "                      ║");
            Io.sop("╠═════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦═══════════════╣");
            Io.sop("║ISBN ║     Titulo     ║      Genero      ║AÑO PUBLICACION║   Editorial   ║    ID AUTOR   ║ ");
            Io.sop("╚═════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩═══════════════╝");



            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);

                int cont = 0; // Aquí empieza desde el número correcto
                while (rs.next()) {
                    vTitulo = rs.getString("titulo");
                    vTitulo= pad2(vTitulo,50);
                    vGenero = rs.getString("genero");
                    vGenero = pad2 ( vGenero,50);
                    vEditorial = rs.getString("editorial");
                    vEditorial = pad2 ( vGenero,50);
                    vAnioPublicacion = rs.getInt("ano");
                    vISBN = rs.getInt("ISBN");
                    vidAutor = rs.getInt("fk_id_autor");
                    System.out.println(pad2(cont++ +".-",5)+ vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"|"+vISBN+"| "+vidAutor);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
            Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el isbn del libro que deseas eliminar: ");
            char opc = leerCaracter();
            switch (opc) {
                case '+':
                    npag++;
                    break;
                case '-':
                    if (npag > 1) {
                        npag--;
                    } else {
                        npag = 1;
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
        int swisbnSeleccionado = Io.leerInt("¿Estas seguro que quieres eliminarlo? Introduce de nuevo ISBN del libro:") ;
        if (LibroCN.borrarISBN(conn,swisbnSeleccionado)){
            System.out.println("Libro borrado correctamente");
        }else{
            System.out.println("El libro no se ha podido borrar");
        }
        LibroCN.menuLibro();
    }
    //Metodo para borrar el isbn
    public static boolean borrarISBN(Connection conn,int swisbnSeleccionado){//Borrar un campo en el que nos pasan el codigo
        Statement st;
        int borrados;
        String sql = "delete from libros where isbn = '"+swisbnSeleccionado+"'";
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
    public static void modificarLibroConTabla(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset;
        String vTitulo,vGenero,vEditorial,sql,vISBN,vAnioPublicacion;
        while (!salir) {
            offset = ( nPag -1)* nRegPag;
            sql = " select * from libros limit " +nRegPag+ " offset "+ offset + " ";
            Io.sop("╔═══════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                         MODIFICACIÓN DE LIBROS  |  PÁGINA: " + nPag + "                      ║");
            Io.sop("╠═══════╦════════════════╦══════════════════╦═════════════════════╦═════════════════╣");
            Io.sop("║Titulo ║     Genero     ║      Editorial   ║    Año Publicacion  ║    ISBN         ║");
            Io.sop("╚═══════╩════════════════╩══════════════════╩═════════════════════╩═════════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vTitulo = rs.getString("titulo");
                    vTitulo= pad2(vTitulo,40);
                    vGenero = rs.getString("genero");
                    vGenero = pad2 ( vGenero,20);
                    vEditorial = rs.getString("editorial");
                    vEditorial = pad2(vEditorial,30);
                    vAnioPublicacion = rs.getString("ano");
                    vAnioPublicacion = pad2(vAnioPublicacion, 27);
                    vISBN = rs.getString("isbn");
                    vISBN= pad2(vISBN,15);
                    System.out.println( vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"| "+vISBN);
                    
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
            Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el ISBN del libro que deseas modificar: ");
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
         String vModificar = Io.leerString("¿Estas seguro que quieres modificarlo? Introduce de nuevo isbn del libro:  ");
         if (!Io.comprobarExistencia(conn, "libros", "isbn", vModificar)) {
                Io.sop(" No existe ningún libro con ese isbn.");
                return;
            }
            Io.sop("¿Qué campo del libro  deseas modificar?");
            Io.sop("1. ISBN");
            Io.sop("2. Titulo");
            Io.sop("3. Genero");
            Io.sop("4. Editorial");
            Io.sop("5. Año Publicacion");
            Io.sop("6. Terminar");
    
            char opc = Io.leerCaracter();
            String campo = "", nuevoValor = "";
    
            switch (opc) {
                case '1':
                    campo = "ISBN";
                    nuevoValor = Io.leerString("Introduce el nuevo ISBN del libro: ");
                    break;
                case '2':
                    campo = "Titulo";
                    nuevoValor = Io.leerString("Introduce el nuevo titulo del libro:");
                    break;
                case '3':
                    campo = "Genero";
                    nuevoValor =Io.leerString("Introduce el nuevo genero:");
                    break;
                case '4':
                    campo = "Editorial";
                    nuevoValor = Io.leerString("Introduce la nueva editorial: ");
                    break;
                case '5':
                    campo = "Año Publicacion";
                    nuevoValor = Io.leerString("Introduce el nuevo año de publicacion: ");
                    break;
                case '6':
                    salir = true;
                    break;
                default:
                    Io.sop("Opción no válida.");
            }
    
            if (!campo.equals("")) {
                if (ejecutarUpdateCampo(conn, vModificar, campo, nuevoValor)) {
                    Io.sop(" Libro  modificado correctamente.");
                } else {
                    Io.sop(" Error al modificar el libro.");
                }
            }
            LibroCN.menuLibro();
            
        }
        public static boolean ejecutarUpdateCampo(Connection conn, String IsbnAntiguo, String campo, String nuevoValor) {
            String sql = "UPDATE libros SET " + campo + " = '" + nuevoValor + "' WHERE dni = '" + IsbnAntiguo + "'";
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
       /*  public static void main(String[] args) {
    
            menuLibro();
        
        
        }
        */
    ////
    /// 
    public static void main(String[] args) {
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
                    LibroCN.modificarLibro();
                    break;
                case 4:
                Io.sop("Saliendo...");
                    break;
                default:
                    Io.sop("Opción no válida. Intenta otra vez.");
            }
        }

       
}
