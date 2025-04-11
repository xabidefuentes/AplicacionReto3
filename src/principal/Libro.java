package principal;
import java.sql.Date;
import Io.*;
import conexiones.*;

public class Libro {
    private int id_libro;
    private String titulo;
    private int ISBN;
    private int numCopias;
    private String genero;
    private int anioPublicacion;
    private String editorial;

    public Libro (int pId_libro, String pTitulo, int pISBN, int pNumCopias, String pGenero, int pAnioPublicacion, String pEditorial){
        this.id_libro=pId_libro;
        this.titulo=pTitulo;
        this.ISBN=pISBN;
        this.numCopias=pNumCopias;
        this.genero=pGenero;
        this.anioPublicacion=pAnioPublicacion;
        this.editorial=pEditorial;
    }
    
    public int getid_libro(){
        return this.id_libro;
    }
    public String gettitulo(){
        return this.titulo;
    }
    public int getISBN(){
        return this.ISBN;
    }
    public int getnumCopias(){
        return this.numCopias;
    }
    public String getgenero(){
        return this.genero;
    }
    public int getanioPublicacion(){
        return this.anioPublicacion;
    }
    public String geteditorial(){
        return this.editorial;
    }

    public void setid_libro(int id_libro){
        this.id_libro=id_libro;
    }
    public void settitulo(String titulo){
        this.titulo=titulo;
    }
    public void setISBN (int ISBN){
        this.ISBN=ISBN;
    }
    public void setnumCopias(int numCopias){
        this.numCopias=numCopias;
    }
    public void setgenero(String genero){
        this.genero=genero;
    }
    public void setanioPublicacion(int anioPublicacion){
        this.anioPublicacion=anioPublicacion;
    }
    public void seteditorial(String editorial){
        this.editorial=editorial;
    }

}
