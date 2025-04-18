package principal;

public class Usuario extends Persona {
    private String penalizacion;
    private int fechaInicioPenalizacion ;
    private int fechaFinPenalizacion;



    //CONSTRUCTOR
public Usuario (String pDni, String pNombre, int pTtfn, String pEmail, String pContraseña,
    String pPenalizacion, int pFechaInicioPen, int pFechaFinPen){
    super(pDni,pNombre,pTtfn,pEmail,pContraseña);
    this.penalizacion = pPenalizacion;
    this.fechaInicioPenalizacion = pFechaInicioPen;
    this.fechaFinPenalizacion = pFechaFinPen;
    
}


//CONSTRUCTOR VACIO

public Usuario (){
    super();
    this.penalizacion = "no";
    this.fechaInicioPenalizacion = 00/00/0000;
    this.fechaFinPenalizacion = 00/00/0000;
}

///GETTERS Y SETTERS

public String getPenalizacion(){
    return this.penalizacion;
}
public void setPenalizacion (String pen){
    this.penalizacion = pen;
}

public int getFechaInicioPen (){
    return this.fechaInicioPenalizacion ;
}
public void setFechaInicioPen ( int fechainicio){
    this.fechaInicioPenalizacion = fechainicio;
}

public int getFechaFinPen(){
    return this.fechaFinPenalizacion;
}
public void setFechaFinPen(int fechaFin){
    this.fechaFinPenalizacion = fechaFin;
}









}
