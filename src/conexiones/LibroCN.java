package conexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Io.*;


public class LibroCN {
    //FUNCIONES
    //Menu usuario
    public static void menuLibro(){
        int opcion;
        do{
            Io.sop("***********************************************************************");
            Io.sop("********************  GESTION DE LIBROS  ****************");
            Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
            Io.sop("**************************  DE MUSKIZ  ************************************");
            Io.sop("1. AGREGAR LIBRO");
            Io.sop("2. BORRAR LIBRO");
            Io.sop("3. MODIFICAR LIBRO");
            Io.sop("4. SALIR");
             opcion = Io.leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    insertarLibro();
                    break;
                case 2:
                   borrarLibro();
                    break;
                case 3:
                    modificarLibro();
                    break;
                case 4:
                Io.sop("Saliendo.. Hasta pronto");
                return;
                default:
                    Io.sop("Opción no válida. Intenta otra vez.");
            }
    
        }while (opcion>0 );
    }

        public static void borrarLibro(){
            Connection conn = Io.getConexion();
            if (conn==null) { Io.sop("sin conexión");return;}
            Io.sop("Conexión correcta");
            consultaTablaDeleteLibro(conn, 9, 1);
            Io.cerrarConexion(conn);
        }
    
    
        public static void modificarLibro(){
            Connection conn = Io.getConexion();
             if (conn==null) { 
            Io.sop("sin conexión");
            return;
        }
            modificarLibroConTabla(conn, 9, 1);
            Io.cerrarConexion(conn);
        }
        //Funciones
        public static String validarLibro (Connection conn){
            String vTitulo;
            do {
                vTitulo = Io.leerString("Introduce el nuevo título del libro:");
                 if (vTitulo.equals("")) {
                     Io.sop("El título no puede estar vacío.");
                 } else if (Io.comprobarExistencia(conn, "libros", "titulo", vTitulo)) {
                     Io.sop("Ese título ya existe. Intenta con otro.");
                     vTitulo = "";
                 }
            } while (vTitulo.equals("")|| Io.comprobarExistencia(conn, "libros", "titulo", vTitulo));
            return vTitulo;
        }
        public static String validarISBN(Connection conn) {
            String vISBN;
            boolean valido;
        
            do {
                vISBN = Io.leerString("Dime el ISBN del libro:");
                valido = true;
        
                if (vISBN.equals("")) {
                    Io.sop("El ISBN no puede estar vacío.");
                    valido = false;
                } else if (!vISBN.matches("\\d{4}")) {
                    Io.sop("El ISBN debe tener exactamente 4 números.");
                    valido = false;
                } else if (Io.comprobarExistencia(conn, "libros", "isbn", vISBN)) {
                    Io.sop("El libro ya existe, vuelve a introducirlo.");
                    valido = false;
                }
        
            } while (!valido);
        
            return vISBN;
        }

        public static boolean validarAnioPublicacion (int ano){
            if (ano <= 1850 || ano > 2025) {
                return false;
            }
            return true;
        }
       
        public static int ejecutarAno(Connection conn){
            int ano = 0;
            boolean valido = false;
            do {
                String entrada = Io.leerString("Dime el año de publicación del libro:");
                
                if (entrada.trim().isEmpty()) {
                    Io.sop("El año no puede estar vacío.");
                } else {
                    try {
                        ano = Integer.parseInt(entrada);
                        if (!validarAnioPublicacion(ano)) {
                            Io.sop("Introduce un año válido.");
                        } else {
                            valido = true;
                        }
                    } catch (NumberFormatException e) {
                        Io.sop("Debes introducir un número válido.");
                    }
                }
            } while (!valido);
        
            return ano;
        }
        public static int validarNumEjemplares() {
            String entrada;
            int numEjemplares = -1;
            boolean valido;
        
            do {
                entrada = Io.leerString("¿Cuántos ejemplares tiene este libro?:");
                valido = true;
        
                if (entrada.trim().isEmpty()) {
                    Io.sop("El número de ejemplares no puede estar vacío.");
                    valido = false;
                } else {
                    try {
                        numEjemplares = Integer.parseInt(entrada);
                        if (numEjemplares < 0) {
                            Io.sop("El número de ejemplares no puede ser negativo.");
                            valido = false;
                        }
                    } catch (NumberFormatException e) {
                        Io.sop("Debes introducir un número válido.");
                        valido = false;
                    }
                }
            } while (!valido);
        
            return numEjemplares;
        }
            //INSERTAR LIBROS


    public static void insertarLibro(){

        Connection conn =Io.getConexion();
        if (conn==null) {
            Io.sop("Sin Conexión");
            return;
        }
        int ncambios = 0,numEjemplares=0,vAnioPublicacion;
        String vTitulo,vGenero,vEditorial,vISBN;

        
        System.out.println("Comenzamos a introducir los datos");
        vTitulo = validarLibro(conn);

        do {
            vGenero = Io.leerString("Dime el género del libro: ");
            if (vGenero.equals("")) {
                Io.sop("El género no puede estar vacío.");
            }
        } while (vGenero.equals(""));  

        do {
            vEditorial = Io.leerString("Dime la editorial del libro: ");
            if (vEditorial.equals("")) {
                Io.sop("La editorial no puede estar vacía.");
            }
        } while (vEditorial.equals(""));

        vISBN=validarISBN(conn);
        vAnioPublicacion=ejecutarAno(conn);
        numEjemplares=validarNumEjemplares();
        // Generar fk_id_autor único (del 1 al 10)
        int fk_id_autor;
        boolean existe;
        do {
            fk_id_autor = (int)(Math.random() * 30) + 1; // valores del 1 al 30
            existe = Io.comprobarExistenciaInt(conn, "autores", "id_autor", fk_id_autor);
        } while (!existe); // seguimos buscando hasta que exista en la tabla autores
        String sql = "insert into libros (isbn,titulo,genero,editorial,ano,fk_id_autor) values ('"+vISBN+"','"+vTitulo+"','"+vGenero+"','"+ vEditorial+"','"+vAnioPublicacion+"','" + fk_id_autor+ "')";  


        try{
            Statement st = conn.createStatement();   
            ncambios = st.executeUpdate(sql);
            if (ncambios > 0) {
                System.out.println("Libro registrado correctamente");
    
                int insertados = 0;
                while (insertados < numEjemplares) {
                    int idAleatorio = (int) (Math.random() * 1000); 
    
                    // Comprobamos si ya existe ese id
                    if (!Io.comprobarExistenciaInt(conn, "ejemplares", "id_ejemplar", idAleatorio)) {
                        String sqlEjemplar = "INSERT INTO ejemplares (id_ejemplar, estado, fk_isbn) " +
                                             "VALUES (" + idAleatorio + ", 'DISPONIBLE', '" + vISBN + "')";
                        st.executeUpdate(sqlEjemplar);
                        insertados++;
                    }
                }
    
                System.out.println(numEjemplares + " ejemplares añadidos correctamente.");
            } else {
                System.out.println("No se ha añadido el libro.");
            }
        }
        catch(SQLException e){
            System.out.println("Problema al insertar la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }

    }


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
        Io.sop("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗");
        Io.sop(  "║                            Listado LIBROS  |  PÁGINA: " + nPag + "                                 ║");
        Io.sop("╠═══════════════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦═══════════════╣");
        Io.sop("║    Titulo     ║     Genero     ║      Editorial   ║AÑO PUBLICACION║      ISBN     ║    ID AUTOR   ║ ");
        Io.sop("╚═══════════════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩═══════════════╝");
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
            case '+':
                    nPag++;
                    break;
                case '-':
                    nPag--;
                    if (nPag <= 0) {
                        nPag = 1;
                    }
                    break;
                case 'x':
                    salir = true;
                    menuLibro();
                    break;
                default:
                    salir = true;
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
            Io.sop("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║                            Listado LIBROS  |  PÁGINA: " + npag + "                                ║");
            Io.sop("╠═══════════════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦═══════════════╣");
            Io.sop("║    Titulo     ║     Genero     ║      Editorial   ║AÑO PUBLICACION║      ISBN     ║    ID AUTOR   ║ ");
            Io.sop("╚═══════════════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩═══════════════╝");



            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vTitulo = rs.getString("titulo");
                    vTitulo= pad2(vTitulo,30);
                    vGenero = rs.getString("genero");
                    vGenero = pad2 ( vGenero,20);
                    vEditorial = rs.getString("editorial");
                    vEditorial = pad2 ( vEditorial,20);
                    vAnioPublicacion = rs.getInt("ano");
                    vISBN = rs.getInt("isbn");
                    vidAutor = rs.getInt("fk_id_autor");
                    System.out.println(vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"|"+vISBN+"| "+vidAutor);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Io.sop("╔════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop("║ [+] Página Siguiente                 [-] Página Anterior                    [X] Salir  ║");
            Io.sop("╚════════════════════════════════════════════════════════════════════════════════════════╝");
            Io.sop("Muevete por la tabla y selecciona el isbn del libro que deseas eliminar: ");
            char opc = Io.leerCaracter();
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
        Io.sop("Voy a borrar el "+swisbnSeleccionado);
        if (LibroCN.borrarISBN(conn,swisbnSeleccionado)){
            System.out.println("Libro borrado correctamente");
        }else{
            System.out.println("El libro no se ha podido borrar");
        }
    }
    //Metodo para borrar el isbn
    public static boolean borrarISBN(Connection conn,int swisbnSeleccionado){
        Statement st;
        int borradosLibro = 0;
    try {
        st = conn.createStatement();

        // Borrar ejemplares relacionados con el ISBN
        String sqlEjemplares = "DELETE FROM ejemplares WHERE fk_isbn = " + swisbnSeleccionado;
        int ejemplaresBorrados = st.executeUpdate(sqlEjemplares);
        System.out.println("Ejemplares borrados: " + ejemplaresBorrados);

        //Borrar el libro
        String sqlLibro = "DELETE FROM libros WHERE isbn = " + swisbnSeleccionado;
        borradosLibro = st.executeUpdate(sqlLibro);
        System.out.println("Libros borrados: " + borradosLibro);

        return borradosLibro > 0;

    } catch (SQLException e) {
        System.out.println("Problema al borrar: " + e.getErrorCode() + " " + e.getMessage());
        return false;
    }
    }
    

    public static void modificarLibroConTabla(Connection conn, int nRegPag, int nPag) {
        Statement stm = null;
        ResultSet rs = null;
        boolean salir = false;
        int offset,vISBN,vAnioPublicacion,vidAutor;
        String vTitulo,vGenero,vEditorial,sql;
        while (!salir) {
            offset = ( nPag -1)* nRegPag;
            sql = " select * from libros limit " +nRegPag+ " offset "+ offset + " ";
            Io.sop("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗");
            Io.sop(  "║                            Modificacion LIBROS  |  PÁGINA: " + nPag + "                                 ║");
            Io.sop("╠═══════════════╦════════════════╦══════════════════╦═══════════════╦═══════════════╦═══════════════╣");
            Io.sop("║    Titulo     ║     Genero     ║      Editorial   ║AÑO PUBLICACION║      ISBN     ║    ID AUTOR   ║ ");
            Io.sop("╚═══════════════╩════════════════╩══════════════════╩═══════════════╩═══════════════╩═══════════════╝");
            try {
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    vTitulo = rs.getString("titulo");
                    vTitulo= pad2(vTitulo,30);
                    vGenero = rs.getString("genero");
                    vGenero = pad2 ( vGenero,20);
                    vEditorial = rs.getString("editorial");
                    vEditorial = pad2 ( vEditorial,20);
                    vAnioPublicacion = rs.getInt("ano");
                    vISBN = rs.getInt("isbn");
                    vidAutor = rs.getInt("fk_id_autor");
                    System.out.println(vTitulo +" | "+ vGenero +" | "+ vEditorial+"| "+vAnioPublicacion+"|"+vISBN+"| "+vidAutor);
                    
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
         int IsbnAntiguo = Io.leerInt("¿Estas seguro que quieres modificarlo? Introduce de nuevo isbn del libro:  ");
         if (!Io.comprobarExistenciaInt(conn, "libros", "isbn", IsbnAntiguo)) {
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
                    campo = "isbn";
                    nuevoValor = validarISBN(conn);
                    break;
                case '2':
                    campo = "titulo";
                    nuevoValor = validarLibro(conn);
                    break;
                case '3':
                    campo = "genero";
                    do {
                        nuevoValor = Io.leerString("Dime el género del libro: ");
                        if (nuevoValor.equals("")) {
                            Io.sop("El género no puede estar vacío.");
                        }
                    } while (nuevoValor.equals(""));
                    break;
                case '4':
                    campo = "editorial";
                    do {
                        nuevoValor = Io.leerString("Dime la editorial del libro: ");
                        if (nuevoValor.equals("")) {
                            Io.sop("La editorial no puede estar vacía.");
                        }
                    } while (nuevoValor.equals(""));
                    break;
                case '5':
                    campo = "ano";
                    nuevoValor = String.valueOf(ejecutarAno(conn)); 
                                       
                     break;
                case '6':
                    salir = true;
                    break;
                default:
                    Io.sop("Opción no válida.");
            }
    
            if (!campo.equals("")) {
                if (ejecutarUpdate(conn, IsbnAntiguo, campo, nuevoValor)) {
                    Io.sop(" Libro  modificado correctamente.");
                } else {
                    Io.sop(" Error al modificar el libro.");
                }
            }
        }
       public static boolean ejecutarUpdate(Connection conn, int IsbnAntiguo, String campo, String  nuevoValor) {

            String sql = "UPDATE libros SET " + campo + " = '" + nuevoValor + "' WHERE isbn = '" + IsbnAntiguo + "'";
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
    
            menuLibro();
        
        
        }
       
    
}
