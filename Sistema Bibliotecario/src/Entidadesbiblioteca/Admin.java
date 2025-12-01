/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidadesbiblioteca;

/**
 *
 * @author lenovo
 */
public class Admin {
    //Atributos
    private String usuario;
    private String password;
    private String nombre;
    
    //Constructor vacio
    public Admin(){
        
    }
    public Admin(String usuario,String password,String nombre){
        this.usuario= usuario;
        this.password= password;
        this.nombre = nombre;
    }
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreReal() {
        return nombre;
    }

    public void setNombreReal(String nombre) {
        this.nombre = nombre;
    }
    
}
