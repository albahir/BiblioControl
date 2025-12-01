package AccesoDatosBiblioteca;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Clase de utilidad genérica para realizar búsquedas en cualquier tipo de lista.
 * Implementa Polimorfismo Paramétrico para reutilizar la lógica de búsqueda.
 */
public class Busqueda {

   public static <T> T buscar(ArrayList<T> lista, Predicate<T> condicion) {
        // Recorremos la lista elemento por elemento
        for (T elemento : lista) {
            // Evaluamos la condición (Lambda) enviada desde el otro archivo
            if (condicion.test(elemento)) {
                return elemento; // Si cumple, devolvemos el objeto y terminamos
            }
        }
        return null; // Si termina el ciclo y no encontró nada
    }
}