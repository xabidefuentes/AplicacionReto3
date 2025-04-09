package principal;



public class Empleado extends Persona{
    private int seguridadSocial;


    /////CONSTRUCTOR
public Empleado(String pDni, String pNombre, int pTtfn, String pEmail, String pContraseña,int seguridadSocial){
    super(pDni,pNombre,pTtfn,pEmail,pContraseña);
    this.seguridadSocial = seguridadSocial;
}

/////CONSTRUCTOR VACIO
public Empleado (){
    super();
    this.seguridadSocial = 000000000000;
}

///GETTERS Y SETTERS

public int getSeguridadSocial(){
    return this.seguridadSocial;
}
public void setSeguridadSocial(int seguridadSocial){
    this.seguridadSocial = seguridadSocial;
}



    
}
