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
    int randomPassword = (int)(Math.random() * 9000) + 1000; // genera número aleatorio entre 1000 y 9999
    int cambios = 0,vTelefono;
    String vNombre,vDni,vEmail;
    vNombre = Io.leerString("Dime el nombre y apellido del usuario: ");//Leemos del teclado
    //Comprobacion para ver si el dni esta repetido
    do {
        vDni = Io.leerString("Dime el dni del usuario: ");
        if (Io.comprobarExistencia(conn, "usuarios", "dni", vDni)) {
            Io.sop("Dni ya existe, vuelve a introducirlo");
        }
    } while (Io.comprobarExistencia(conn, "usuarios", "dni", vDni));

    vEmail = Io.leerString("Dime el email del usuario: ");
    vTelefono = Io.leerInt("Dime el telefono del usuario: ");
    String sql = "INSERT INTO usuarios (dni, nombre, telefono, email, password, penalizacion, fecha_inicio_penalizacion, fecha_fin_penalizacion) " +
    "VALUES ('" + vDni + "', '" + vNombre + "', '" + vTelefono + "', '" + vEmail + "', '" + randomPassword + "', 'No', '1111-11-11', '1111-11-11')";       
    System.out.println("Contraseña generada para el usuario: " + randomPassword);  //Para mostrar la contraseña generada a el  usuario
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


