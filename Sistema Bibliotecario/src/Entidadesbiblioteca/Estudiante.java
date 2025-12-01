/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidadesbiblioteca;

/**
 *
 * @author lenovo
 */
public class Estudiante {
    
    
    public static final String[] CARRERAS = {
        "Seleccione una opción...",
        "Ingeniería en Informática",
        "Ingeniería Civil",
        "Administración",
        "Contaduría"
    };
    //atributos
    private String cedula;   // O carnet/ID único
    private String nombre;
    private String carrera;  // O grado/curso
    private String telefono;
    private String correo;
    
    //constructor vacio
    public Estudiante(){
        
    }
    
    public Estudiante(String cedula, String nombre, String carrera, String telefono,String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.carrera = carrera;
        this.telefono = telefono;
        this.correo = correo;
    }
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
    public String getCorreo(){
        return correo;
    }
    public void setCorreo(String correo){
        this.correo=correo;
    }
    
    
    @Override
    public String toString() {
        return nombre + " (" + cedula + ")";
    }
}
