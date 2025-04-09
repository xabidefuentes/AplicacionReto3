package principal;


public abstract  class Persona {
    protected String dni;
    protected String nombre;
    protected int telefono;
    protected String email;
    protected String contraseña;


    //CONSTRUCTOR
public Persona (String pDni, String pNombre, int pTtfn, String pEmail, String pContraseña){
    this.dni = pDni;
    this.nombre = pNombre;
    this.telefono = pTtfn;
    this.email = pEmail;
    this.contraseña = pContraseña;

}
    ////CONSTRUCTOR VACIO
public Persona (){
    this.dni = "000000000";
    this.nombre = "*******";
    this.telefono = 00000000;
    this.email ="*********@******.com";
    this.contraseña = "*********";
}

///GETTERS Y SETTERS

public String getDni(){
    return this.dni;
}
public void setDni( String dni){
    this.dni = dni;
}

public String getNombre (){
    return this.nombre;
}
public void setNombre ( String nombre){
    this.nombre = nombre;
}

public int getTelefono (){
    return this.telefono;
}
public void setTelefono ( int telefono){
    this.telefono= telefono;
}

public String getEmail (){
    return this.email;
}
public void setEmail ( String email){
    this.email = email;
}

public String getContraseña (){
    return this.contraseña;
}
public void setContraseña (String contraseña){
    this.contraseña = contraseña;
}




    
}
