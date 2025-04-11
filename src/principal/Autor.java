package principal;
import Io.*;
import conexiones.*;
import java.util.Date;

public class Autor {
    private int id_autor;
    private String nombre;
    private String apellido;
    private Date fechaNac;
    private String nacionalidad;

    //CONSTRUCTORES//

    public Autor (int pId_autor, String pNombre, String pApellido, Date pFechaNac, String pNacionalidad){
        this.id_autor=pId_autor;
        this.nombre=pNombre;
        this.apellido=pApellido;
        this.fechaNac=pFechaNac;
        this.nacionalidad=pNacionalidad;
    }
    
    //GETTERS//

    public int getId_autor(){
        return this.id_autor;
    }
    public String getNombre(){
        return this.nombre;
    }
    public String getApellido(){
        return this.apellido;
    }
    public Date getFechaNac(){
        return this.fechaNac;
    }
    public String getNacionalidad(){
        return this.nacionalidad;
    }

    //SETTERS//

    public void setId_autor(int pId_autor){
        this.id_autor=pId_autor;
    }
    public void setNombre(String pNombre){
        this.nombre=pNombre;
    }
    public void setApellido(String pApellido){
        this.apellido=pApellido;
    }
    public void setFechaNac(Date pFechaNac){
        this.fechaNac=pFechaNac;
    }
    public void setNacionalidad(String pNacionalidad){
        this.nacionalidad=pNacionalidad;
    }

    
}
