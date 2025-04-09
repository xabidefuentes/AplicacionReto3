package principal;
import Io.*;
import conexiones.*;

import java.time.LocalDate;

public class Prestamo {
    // Atributos
    private int idPrestamo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private String dniUsuario;
    private int idEjemplar;
    private String dniEmpleado;

    // Constructor
    public Prestamo(int idPrestamo, LocalDate fechaPrestamo, LocalDate fechaDevolucion, String dniUsuario, int idEjemplar, String dniEmpleado) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.dniUsuario = dniUsuario;
        this.idEjemplar = idEjemplar;
        this.dniEmpleado = dniEmpleado;
    }
    public Prestamo() {
        this.idPrestamo = 0;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = LocalDate.now();
        this.dniUsuario = "";
        this.idEjemplar = 0;
        this.dniEmpleado = "";
    }

    // Getters y Setters
    public int getIdPrestamo() {
        return idPrestamo;
    }
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    public String getDniUsuario() {
        return dniUsuario;
    }
    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }
    public int getIdEjemplar() {
        return idEjemplar;
    }
    public void setIdEjemplar(int idEjemplar) {
        this.idEjemplar = idEjemplar;
    }
    public String getDniEmpleado() {
        return dniEmpleado;
    }
    public void setDniEmpleado(String dniEmpleado) {
        this.dniEmpleado = dniEmpleado;
    }

    // Funciones
    public void mostrarPrestamo() {
        System.out.println("ID Prestamo: " + idPrestamo);
        System.out.println("Fecha de Prestamo: " + fechaPrestamo);
        System.out.println("Fecha de Devolucion: " + fechaDevolucion);
        System.out.println("DNI Usuario: " + dniUsuario);
        System.out.println("ID Ejemplar: " + idEjemplar);
        System.out.println("DNI Empleado: " + dniEmpleado);
    }

    public static void menuPrestamo() {
        Io.sop("╔═══════════════════════════════════════════════════════════════════════╗");
        Io.sop("║                         GESTIÓN DE PRÉSTAMOS                          ║");
        Io.sop("╠═══════════════════════════════════════════════════════════════════════╣");
        Io.sop("║  1. 📖 Añadir Préstamo                                                ║");
        Io.sop("║  2. ❌ Eliminar Préstamo                                              ║");
        Io.sop("║  3. ✏️ Modificar Préstamo                                             ║");
        Io.sop("║  4. 🔙 Volver al Menú Principal                                       ║");
        Io.sop("╚═══════════════════════════════════════════════════════════════════════╝");
        int opcion = Io.leerInt("Selecciona una opción: ");
        switch (opcion) {
            case 1:
                PrestamoCN.añadirPrestamo();
                break;
            case 2:
                PrestamoCN.borrarPrestamo();
                break;
            case 3:
                PrestamoCN.modificarPrestamo();
                break;
            case 4:
                System.out.println("Saliendo del menú de préstamos.");;
                main.menuPrincipal();
            default:
                System.out.println("Opción no válida. Intenta otra vez.");
        }
    }



}
