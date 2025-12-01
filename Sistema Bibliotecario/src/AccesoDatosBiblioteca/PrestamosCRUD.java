/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatosBiblioteca;


import Entidadesbiblioteca.Estudiante;
import Entidadesbiblioteca.Libro;
import Entidadesbiblioteca.Prestamo;
import java.util.Calendar;
import java.util.Date;

public class PrestamosCRUD {
// 1. MÉTODO PRINCIPAL: REGISTRAR EL PRÉSTAMO
    // Devuelve "true" si se pudo prestar, "false" si hubo error (sin stock, etc)
    public boolean registrarPrestamo(Estudiante estudiante, Libro libro,int diasPlazo) {
        
        // Validación A: ¿El libro existe y tiene stock?
        if (libro == null || !libro.hayStock()) {
            System.out.println("Error: Libro no disponible o inexistente.");
            return false;
        }

        // Validación B: ¿El estudiante ya tiene este libro pendiente?
        if (tieneLibroPendiente(estudiante, libro)|| tieneDeudaPendiente(estudiante)) {
            System.out.println("Error: El estudiante ya tiene una copia de este libro sin devolver.");
            return false;
        }

        // --- INICIO DE LA TRANSACCIÓN ---
        int contador = BaseArchivos.listaPrestamos.size() + 1;
        String idGenerado = "P-" + contador;
       Date fechaInicio = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicio);
        calendar.add(Calendar.DAY_OF_YEAR, diasPlazo); 
        Date fechaLimite = calendar.getTime();
        
        // 1. Crear el objeto Préstamo con la fecha de hoy
        Prestamo nuevoPrestamo = new Prestamo(idGenerado,estudiante, libro, fechaInicio,fechaLimite);

       
        BaseArchivos.listaPrestamos.add(nuevoPrestamo);
       

        // 3. ACTUALIZAR STOCK DEL LIBRO (Muy importante)
        libro.reducirStock();
        
        System.out.println("Prestamo registrado con exito. Stock restante: " + libro.getStock());
        GestorArchivos.guardarPrestamos(BaseArchivos.listaPrestamos);
    GestorArchivos.guardarLibros(BaseArchivos.listaLibros);
        return true;
    }

    // 2. MÉTODO PARA DEVOLVER UN LIBRO
    public void devolverLibro(Prestamo prestamo) {
        if (prestamo != null && prestamo.isActivo()) {
            // Cambiamos el estado del préstamo
            prestamo.finalizarPrestamo();
            
            // Recuperamos el stock del libro
            prestamo.getLibro().aumentarStock();
            GestorArchivos.guardarPrestamos(BaseArchivos.listaPrestamos);
            GestorArchivos.guardarLibros(BaseArchivos.listaLibros);
            System.out.println("Libro devuelto correctamente.");
        }
    }

    // 3. MÉTODO AUXILIAR: VERIFICAR SI YA LO TIENE
    // Recorre los préstamos para ver si este estudiante ya tiene ese libro prestado activo
 public boolean tieneLibroPendiente(Estudiante est, Libro lib) {
        // Usamos Busqueda para ver si existe un préstamo que coincida con Estudiante + Libro + Activo
      Prestamo encontrado = (Prestamo) Busqueda.buscar(
            BaseArchivos.listaPrestamos,
            p -> p.getEstudiante().getCedula().equals(est.getCedula()) && 
                 p.getLibro().getidLibro().equals(lib.getidLibro()) && 
                 p.isActivo()
        );
        
        // Si lo encontró (no es null), devolvemos true
        return encontrado != null;
    }
  
    public boolean tieneDeudaPendiente(Estudiante est) {
        // Buscamos si tiene ALGÚN préstamo activo cuyo estado sea "Retrasado"
        Prestamo moroso = (Prestamo) Busqueda.buscar(BaseArchivos.listaPrestamos, 
            p -> p.getEstudiante().getCedula().equals(est.getCedula()) && 
                 p.isActivo() && 
                 p.getEstado().equals("Retrasado")
        );
        return moroso != null; // Si encontró uno, es verdadero (tiene deuda)
    }
     
 public Prestamo buscarPrestamoPorId(String id) {
        return (Prestamo) Busqueda.buscar(BaseArchivos.listaPrestamos, 
            p -> p.getIdPrestamo().equals(id));
    }
}
