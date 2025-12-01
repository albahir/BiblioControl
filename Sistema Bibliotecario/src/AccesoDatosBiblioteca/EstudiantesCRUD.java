package AccesoDatosBiblioteca;

import Entidadesbiblioteca.Estudiante;

/**
 * Clase controladora para la gestión de Estudiantes.
 * Maneja el CRUD (Crear, Leer, Actualizar, Borrar) y las validaciones de duplicados.
 */
public class EstudiantesCRUD {

    // --- 1. AGREGAR ---
    public void agregarEstudiante(Estudiante nuevoEstudiante) {
        BaseArchivos.listaEstudiantes.add(nuevoEstudiante);
        // Persistencia automática
        GestorArchivos.guardarEstudiantes(BaseArchivos.listaEstudiantes);
    }

    // --- 2. ELIMINAR ---
    public boolean eliminarEstudiante(String cedula) {
        // Buscamos el objeto
        Estudiante est = (Estudiante) Busqueda.buscar(BaseArchivos.listaEstudiantes, 
            e -> e.getCedula().equals(cedula));
        
        if (est != null) {
            BaseArchivos.listaEstudiantes.remove(est);
            GestorArchivos.guardarEstudiantes(BaseArchivos.listaEstudiantes); // Guardar cambios
            return true;
        }
        return false;
    }

    // --- 3. ACTUALIZAR (Uso de Setters) ---
    public boolean actualizarEstudiante(String cedula, String nuevoNombre, String nuevaCarrera, String nuevoTelefono, String nuevoCorreo) {
        // Buscamos por la Cédula (que es la llave inmutable)
        Estudiante est = (Estudiante) Busqueda.buscar(BaseArchivos.listaEstudiantes, 
            e -> e.getCedula().equals(cedula));

        if (est != null) {
            // USAMOS LOS MÉTODOS SET PARA MODIFICAR (ENCAPSULAMIENTO)
            est.setNombre(nuevoNombre);
            est.setCarrera(nuevaCarrera);
            est.setTelefono(nuevoTelefono);
            est.setCorreo(nuevoCorreo);
            
            // Guardamos la lista actualizada en el archivo
            GestorArchivos.guardarEstudiantes(BaseArchivos.listaEstudiantes);
            return true;
        }
        return false;
    }

    // --- 4. VALIDACIONES DE DUPLICADOS (Usadas por el Panel) ---
    
    public boolean existeCedula(String cedula) {
        return Busqueda.buscar(BaseArchivos.listaEstudiantes, e -> e.getCedula().equals(cedula)) != null;
    }

    public boolean existeCorreo(String correo) {
        // equalsIgnoreCase para que "ana@mail.com" sea igual a "ANA@MAIL.COM"
        return Busqueda.buscar(BaseArchivos.listaEstudiantes, e -> e.getCorreo().equalsIgnoreCase(correo)) != null;
    }

    public boolean existeTelefono(String telefono) {
        return Busqueda.buscar(BaseArchivos.listaEstudiantes, e -> e.getTelefono().equals(telefono)) != null;
    }
}