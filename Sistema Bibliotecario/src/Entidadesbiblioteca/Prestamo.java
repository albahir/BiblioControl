/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidadesbiblioteca;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Prestamo {
    
   //Atributo
    private String idPrestamo;
    private Estudiante estudiante; 
    private Libro libro;           
    private Date fechaPrestamo;
    private Date fechaLimite;    
    private boolean activo;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Constructor Vacio
    public Prestamo(){
        
    }
    
    public Prestamo(String idPrestamo,Estudiante estudiante,Libro libro,Date fechaPrestamo,Date fechaLimite){
        this.idPrestamo= idPrestamo;
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite= fechaLimite;
        this.activo = true;
        
    }
    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    public Date getFechaLimite() { 
        return fechaLimite; 
    }
    public void setFechaLimite(Date fechaLimite) { 
        this.fechaLimite = fechaLimite; 
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public String getEstado() {
        if (!activo) {
            return "Devuelto";
        }
        // Si la fecha actual es mayor a la fecha limite, esta Retrasado
        if (new Date().after(fechaLimite)) {
            return "Retrasado";
        }
        return "Pendiente";
    }
    //Metodos
    public String getFechaLimpia() {
        return sdf.format(fechaPrestamo);
    }
    public String getFechaLimiteLimpia() {
        return sdf.format(fechaLimite);
    }
    public void finalizarPrestamo() {
        this.activo = false;
    }
}
