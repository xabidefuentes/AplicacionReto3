package principal;
import Io.*;
import conexiones.*;

public class Ejemplar {
    private int id_ejemplar;
    private String estado;
    private int codLibro;

    public Ejemplar(int pid_ejemplar, String pestado, int pcodLibro){
        this.id_ejemplar=pid_ejemplar;
        this.estado=pestado;
        this.codLibro=pcodLibro;
    }
     
    public int getid_ejemplar(){
        return this.id_ejemplar;
    }
    public String getestado(){
        return this.estado;
    }
    public int getcodLibro(){
        return this.codLibro;
    }
    public void setid_libro(int id_ejemplar){
        this.id_ejemplar=id_ejemplar;
    }
    public void settitulo(String estado){
        this.estado=estado;
    }
    public void setcodLibro (int codLibro){
        this.codLibro=codLibro;
    }
}
