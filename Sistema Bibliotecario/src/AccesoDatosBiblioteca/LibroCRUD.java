/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatosBiblioteca;

import Entidadesbiblioteca.Libro;
import java.util.ArrayList;
public class LibroCRUD {
    // Agregar un libro a la lista
    public void agregarLibro(Libro nuevoLibro) {
        BaseArchivos.listaLibros.add(nuevoLibro);
        GestorArchivos.guardarLibros(BaseArchivos.listaLibros);
    }

    // Obtener la lista completa
    public ArrayList<Libro> obtenerLibros() {
        return BaseArchivos.listaLibros;
    }

    public Libro buscarLibro(String id) {
         return Busqueda.buscar(BaseArchivos.listaLibros, l -> l.getidLibro().equals(id));
    }

public boolean eliminarLibro(String id) {
        // 1. Usamos tu función maestra para encontrarlo
        Libro libroEncontrado = Busqueda.buscar(
            BaseArchivos.listaLibros, 
            l -> l.getidLibro().equals(id)
        );
       
        // 2. Si existe, lo borramos
        if (libroEncontrado != null) {
            BaseArchivos.listaLibros.remove(libroEncontrado);
            GestorArchivos.guardarLibros(BaseArchivos.listaLibros);
            return true;
        }
        return false;
    }
   
//editar
    
 public boolean actualizarLibro(String id, String nuevoTitulo, String nuevoAutor, String nuevoGenero, int nuevoStock) {
        // 1. Buscamos el original
        Libro libro = Busqueda.buscar(
            BaseArchivos.listaLibros, 
            l -> l.getidLibro().equals(id)
        );
        
        // 2. Si existe, actualizamos sus datos
        if (libro != null) {
            libro.setTitulo(nuevoTitulo);
            libro.setAutor(nuevoAutor);
            libro.setGenero(nuevoGenero);
            libro.setStock(nuevoStock);
           GestorArchivos.guardarLibros(BaseArchivos.listaLibros);
            return true;
        }
        return false;
    }
}
