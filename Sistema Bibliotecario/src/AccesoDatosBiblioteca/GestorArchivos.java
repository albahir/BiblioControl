
package AccesoDatosBiblioteca;

import Entidadesbiblioteca.Estudiante;
import Entidadesbiblioteca.Libro;
import Entidadesbiblioteca.Prestamo;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class GestorArchivos {

    // Nombres de los archivos
    private static final String ARCHIVO_ESTUDIANTES = "estudiantes.txt";
    private static final String ARCHIVO_LIBROS = "libros.txt";
    private static final String ARCHIVO_PRESTAMOS = "prestamos.txt";

    // --- 1. GUARDAR (ESCRITURA) ---

    public static void guardarEstudiantes(ArrayList<Estudiante> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_ESTUDIANTES))) {
            for (Estudiante e : lista) {
                // Formato: cedula;nombre;carrera;telefono;correo
                String linea = e.getCedula() + ";" + e.getNombre() + ";" + 
                               e.getCarrera() + ";" + e.getTelefono() + ";" + e.getCorreo();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando estudiantes: " + e.getMessage());
        }
    }

    public static void guardarLibros(ArrayList<Libro> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS))) {
            for (Libro l : lista) {
                // Formato: id;titulo;autor;genero;stock
                String linea = l.getidLibro() + ";" + l.getTitulo() + ";" + 
                               l.getAutor() + ";" + l.getGenero() + ";" + l.getStock();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando libros: " + e.getMessage());
        }
    }

    public static void guardarPrestamos(ArrayList<Prestamo> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PRESTAMOS))) {
            for (Prestamo p : lista) {
                // Guardamos IDs y la fecha como número (Timestamp) para no tener problemas de formato
                // Formato: idPrestamo;cedulaEstudiante;idLibro;fechaPrestamo(long);fechaLimite;activo(boolean)
                String linea = p.getIdPrestamo() + ";" + 
                               p.getEstudiante().getCedula() + ";" + 
                               p.getLibro().getidLibro() + ";" + 
                               p.getFechaPrestamo().getTime() + ";" + 
                               p.getFechaLimite().getTime() + ";" +
                               p.isActivo();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando préstamos: " + e.getMessage());
        }
    }

    // 2. CARGAR (LECTURA)

    public static ArrayList<Estudiante> cargarEstudiantes() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_ESTUDIANTES);
        if (!archivo.exists()) return lista; // Si no existe, devolvemos lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    lista.add(new Estudiante(datos[0], datos[1], datos[2], datos[3], datos[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public static ArrayList<Libro> cargarLibros() {
        ArrayList<Libro> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_LIBROS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    int stock = Integer.parseInt(datos[4]);
                    lista.add(new Libro(datos[0], datos[1], datos[2], datos[3], stock));
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando libros: " + e.getMessage());
        }
        return lista;
    }

    // Cargar préstamos requiere tener las listas de Estudiantes y Libros ya cargadas en memoria
    public static ArrayList<Prestamo> cargarPrestamos(ArrayList<Estudiante> ests, ArrayList<Libro> libs) {
        ArrayList<Prestamo> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_PRESTAMOS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 6) {
                    String idPrestamo = datos[0];
                    String cedulaEst = datos[1];
                    String idLibro = datos[2];
                    long fechaInicio = Long.parseLong(datos[3]);
                    long fechaLimite = Long.parseLong(datos[4]);
                    boolean activo = Boolean.parseBoolean(datos[5]);

                    // Reconstruir objetos buscando en las listas
                    Estudiante est = Busqueda.buscar(ests, e -> e.getCedula().equals(cedulaEst));
                    Libro lib = Busqueda.buscar(libs, l -> l.getidLibro().equals(idLibro));

                    if (est != null && lib != null) {
                        Prestamo p = new Prestamo(idPrestamo, est, lib, new Date(fechaInicio),new Date(fechaLimite));
                        p.setActivo(activo);
                        lista.add(p);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando préstamos: " + e.getMessage());
        }
        return lista;
    }
}
