package conexiones;
import Io.Io;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioCN {


    //FUNCIONES
    //Menu usuario
public static void menuUsuario(){
Io.sop("***********************************************************************");
        Io.sop("********************  GESTION DE USUARIOS  ****************");
        Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
        Io.sop("**************************   DE MUSKIZ  ************************************");
        Io.sop("1. AGREGAR USUARIO");
        Io.sop("2. BORRAR USUARIO");
        Io.sop("3. MODIFICAR USUARIO");
        Io.sop("4. SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                UsuarioCN.insertarUsuario();
                break;
            case 2:
               UsuarioCN.BorrarUsuario(); 
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





    ///Metodo para validar el email 
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }


    //INSERTAR USUARIO
    public static int insertarUsuario() {
        Connection conn = Io.getConexion();
        int randomPassword = (int)(Math.random() * 9000) + 1000; // genera número aleatorio entre 1000 y 9999
        int cambios = 0, vTelefono;
        String vNombre, vDni, vEmail;
    
        vNombre = Io.leerString("Dime el nombre y apellido del usuario: ");
    
        // Comprobación para ver si el DNI está repetido
        do {
            vDni = Io.leerString("Dime el dni del usuario: ");
            if (Io.comprobarExistencia(conn, "usuarios", "dni", vDni)) {
                Io.sop("Dni ya existe, vuelve a introducirlo");
            }
        } while (Io.comprobarExistencia(conn, "usuarios", "dni", vDni));
    
        // Validación del email
        do {
            vEmail = Io.leerString("Dime el email del usuario: ");
            if (!esCorreoValido(vEmail)) {
                Io.sop("Correo no válido. Asegúrate de que tenga formato correcto (ej: usuario@dominio.com)");
            }
        } while (!esCorreoValido(vEmail));
    
        vTelefono = Io.leerInt("Dime el telefono del usuario: ");
    
        String sql = "INSERT INTO usuarios (dni, nombre, telefono, email, password, penalizacion, fecha_inicio_penalizacion, fecha_fin_penalizacion) " +
                "VALUES ('" + vDni + "', '" + vNombre + "', '" + vTelefono + "', '" + vEmail + "', '" + randomPassword + "', 'No', '1111-11-11', '1111-11-11')";
    
        System.out.println("Contraseña generada para el usuario: " + randomPassword);  // Mostrar contraseña generada
    
        try {
            Statement st = conn.createStatement();
            cambios = st.executeUpdate(sql);
            if (cambios == 0) {
                System.out.println("No se ha añadido el registro");
            } else {
                System.out.println("Registro añadido");
            }
        } catch (SQLException e) {
            System.out.println("Problema al insertar en la tabla");
            System.out.println(e.getErrorCode());
            System.out.println(sql);
        }
    
        menuUsuario();
        return cambios;
    }
   
    
    //BORRAR USUARIO
    public static boolean BorrarUsuario(){   // Pedimos el dni por teclado que queremos borrar
        Connection conn =Io.getConexion();
        Statement st;
        String vDni= Io.leerString("Dime el dni del usuario que deseas borrar : ");
        int borrados;
        String sql = "delete from usuarios where dni = '"+vDni+"'";
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


////USUARIO PAGINADO

public static void consultarUsuarioPaginado (Connection conn,int nRegPag,int nPag){
    /*Realizamos la consulta sql para mostrar todos los datos de la tabla, Se mostraran estudiante de 
     * 10 en 10 (nRegPag), los que correspondean a la pagina nPag
     * offset es el desplazamiento dentro del fichero
     * limit : es el numero de registros que voy a leer
     * con + y - avanzare una pagina. La primera pagina sera la 1
     * al pulsar ESC saldre de la consulta */
    Statement stm = null;
    ResultSet rs = null;
    int offset,cont,vTelefono;
    String vDni,vNom,vEmail,sql;
    boolean salir = false;
    while ( ! salir){//Control de las teclas +, -, ESC
        offset = ( nPag -1)* nRegPag;
        sql = " select * from usuarios limit " +nRegPag+ " offset "+ offset + " ";
        rs = ejecutarSelect (conn,sql);
        System.out.println(" TABLA DE USUARIOS Pag : "+ nPag);
        System.out.println("-------------------------------");
        cont = 0;
        try {
            while ( rs.next()){
                vDni = rs.getString("dni");
                vDni= padl(vDni,10);
                vNom = rs.getString("nombre");
                vNom = padl ( vNom,20);// Para que quede bien en columna del mismo tamaño
                vTelefono = rs.getInt("telefono");
                vEmail = rs.getString("email");
                vEmail = padl(vEmail, 20);
                System.out.println(padl(cont++ +".-",5)+ vDni +" | "+ vNom +" | "+ vEmail+"| "+vTelefono);
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

public static String padl (String texto, int longitud){
    if( texto.length() >longitud){
        return texto.substring(0,longitud);
    }else{
        while(texto.length()< longitud){
            texto += " ";
        }
        return texto;
    }
}

////CONSULTAR TABLA

public static void borrarUsuarioTabla (Connection conn, int totalRegistros){ //Visualizamos la tabla 
    Statement stm = null;
    ResultSet rs = null;//eso seria como apuntar al fichero se usa cuando quieres ver la informacion, no cuando quieres ejecutar algo
    String sql = "select * from usuarios";
    String vDni,vNom,vEmail,vTelefono;
    try{
        stm = conn.createStatement();
        rs = stm.executeQuery(sql);
        System.out.println("Todos los registros de la tabla usuarios");
        int cont = 0;
        while(rs.next()){//ESTO ES PARA QUE VAYA LEYENDO LINEAS DEL FICHERO HASTA QUE SE ACABE ( como un leer registro)
            vDni = rs.getString("dni");//Aqui coge el valor de la tabla y se lo añado a la variable
            vDni = padl(vDni,10);
            vNom = rs.getString("nombre");
            vNom = padl(vNom, 20);//sirve para que quede ordenado por columnas
            vEmail = rs.getString("email");
            vDni = padl(vDni, 20);
            vTelefono = rs.getString("telefono");
            vTelefono = padl(vTelefono,10);
            System.out.println(vDni+"|"+vNom+"|"+vEmail+"|"+vTelefono);//Visualizo los datos de la tabla metidos en la variable
            cont++;
            if(cont>totalRegistros && totalRegistros !=0){
                break;
            }

        }
    }
    catch(SQLException exc){
        System.out.println("Problema al consultar"+sql+exc.getErrorCode()+exc.getMessage());
    }
    if (BorrarUsuario()){
        System.out.println("Usuario borrado correctamente");
    }else{
        System.out.println("Usuario no se ha podido borrar");
    }
    menuUsuario();
}











    public static void main(String[] args) {
    Connection conn =Io.getConexion();    


        Io.sop("***********************************************************************");
        Io.sop("********************  GESTION DE USUARIOS  ****************");
        Io.sop("*****************  DE LA BIBLIOTECA MUNICIPAL *******************************");
        Io.sop("**************************   DE MUSKIZ  ************************************");
        Io.sop("1. AGREGAR USUARIO");
        Io.sop("2. BORRAR USUARIO");
        Io.sop("3. MODIFICAR USUARIO");
        Io.sop("4. SALIR");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                UsuarioCN.insertarUsuario();
                break;
            case 2:
                UsuarioCN.borrarUsuarioTabla(conn,10 );
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

   
        


    


   
    }



