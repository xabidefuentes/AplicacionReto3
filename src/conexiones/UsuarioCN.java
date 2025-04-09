package conexiones;
import Io.Io;
import java.sql.Connection;
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


//INSERTAR USUARIO

public static int insertarUsuario(){
    Connection conn =Io.getConexion();
    int cambios = 0,vTelefono;
    String vNombre,vApellido,vDni,vEmail;
    System.out.println("Comenzamos a introducir los datos");
    vNombre = Io.leerString("Dime el nombre del usuario: ");//Leemos del teclado
    vApellido = Io.leerString("Dime el apellido del usuario: ");
    vDni = Io.leerString("Dime el dni del usuario: ");
    vEmail = Io.leerString("Dime el email del usuario: ");
    vTelefono = Io.leerInt("Dime el telefono del usuario: ");
    String sql = "insert into usuarios (nombre,apellido,dni,email,telefono) values ('"+vNombre+"','"+vApellido+"','"+vDni+"','"+ vEmail+"','"+vTelefono+"')";
    try{
        Statement st = conn.createStatement();   // es para que se agrege a la bbdd
        cambios = st.executeUpdate(sql);
    }
    catch(SQLException e){
        System.out.println("Problema al insertar la tabla");
        System.out.println(e.getErrorCode());
        System.out.println(sql);
    }
    return cambios;

    }














    public static void main(String[] args) {
        


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
                insertarUsuario();
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
        
    }


