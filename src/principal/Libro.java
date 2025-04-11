package principal;
import java.sql.Date;
import Io.*;
import conexiones.*;

public class Libro {
    private String titulo;
    private int ISBN;
    private String genero;
    private int anioPublicacion;
    private String editorial;

    public Libro (String pTitulo, int pISBN, String pGenero, int pAnioPublicacion, String pEditorial){
        this.titulo=pTitulo;
        this.ISBN=pISBN;
        this.genero=pGenero;
        this.anioPublicacion=pAnioPublicacion;
        this.editorial=pEditorial;
    }
    
    public String gettitulo(){
        return this.titulo;
    }
    public int getISBN(){
        return this.ISBN;
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

    
    public void settitulo(String titulo){
        this.titulo=titulo;
    }
    public void setISBN (int ISBN){
        this.ISBN=ISBN;
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
