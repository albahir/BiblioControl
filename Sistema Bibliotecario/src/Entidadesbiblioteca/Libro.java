/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidadesbiblioteca;

/**
 *
 * @author lenovo
 */
public class Libro {
    //Atributo
    private String idLibro;    
    private String titulo;
    private String autor;
    private String genero;  
    private int stock;
    
    //constructro vacio
    public Libro(){
        
        
    }
    public Libro(String idLibro, String titulo, String autor, String genero, int stock) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.stock = stock;
    } 
    
    public static final String[] CATEGORIAS = {
        "Seleccione una opción...",
        // Informática
        "Programación y Desarrollo",
        "Bases de Datos",
        "Redes y Seguridad",
        "Inteligencia Artificial",
        // Civil
        "Materiales y Construcción",
        "Hidráulica y Geotecnia",
        "Diseño Estructural",
        // Contaduría / Admin
        "Contabilidad y Auditoría",
        "Finanzas y Tributación",
        "Marketing y Estrategia",
        "Recursos Humanos"
            
    };
    // get y set 
    public String getidLibro() {
        return idLibro;
    }

    public void setidLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("Error: No se puede asignar stock negativo.");
            
        } else {
        this.stock = stock;
        }
    }
    
    //Metodos 
    public boolean hayStock() {
        return stock > 0;
    }
    public void reducirStock() {
        if (stock > 0) {
            stock--;
        }
    }
    public void aumentarStock() {
        stock++;
    }
    @Override
    public String toString() {
        return titulo + " - Stock: " + stock;
    }
}
