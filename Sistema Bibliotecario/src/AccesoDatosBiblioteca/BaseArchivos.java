package AccesoDatosBiblioteca;


import Entidadesbiblioteca.Admin;
import Entidadesbiblioteca.Estudiante;
import Entidadesbiblioteca.Libro;
import Entidadesbiblioteca.Prestamo;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que simula la Base de Datos en memoria.
 * Contiene listas estáticas que son accesibles desde todo el programa.
 */
public class BaseArchivos {

    // Listas estáticas para mantener la persistencia durante la ejecución
    public static ArrayList<Admin> listaAdmins = new ArrayList<>();
    public static ArrayList<Libro> listaLibros = new ArrayList<>();
    public static ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();
    public static ArrayList<Prestamo> listaPrestamos = new ArrayList<>();

    //metodo Principal de Carga
    public static void cargarDatosIniciales() {
        // Evitar duplicados si ya hay datos en memoria
        if (!listaEstudiantes.isEmpty()) return;

        // Cargar Admin por defecto
        if (listaAdmins.isEmpty()) {
            listaAdmins.add(new Admin("admin", "123", "Administrador Principal"));
        }

        // Intentar cargar desde archivos TXT existentes
        listaEstudiantes = GestorArchivos.cargarEstudiantes();
        listaLibros = GestorArchivos.cargarLibros();
        listaPrestamos = GestorArchivos.cargarPrestamos(listaEstudiantes, listaLibros);

        // Si no hay datos crear catálogo completo
        if (listaEstudiantes.isEmpty() && listaLibros.isEmpty()) {
            System.out.println("--> Generando catálogo extendido de prueba...");
            crearDatosPrueba();
        }
    }

    // Este metdo genera datos para presentar y que no arranque vacio
    private static void crearDatosPrueba() {
        

        //  INFORMÁTICA
        listaLibros.add(new Libro("INF-001", "Java: Cómo Programar", "Deitel & Deitel", "Programación y Desarrollo", 5));
        listaLibros.add(new Libro("INF-002", "Clean Code", "Robert C. Martin", "Programación y Desarrollo", 3));
        listaLibros.add(new Libro("INF-003", "Fundamentos de Bases de Datos", "Silberschatz", "Bases de Datos", 4));
        listaLibros.add(new Libro("INF-004", "Redes de Computadoras", "Tanenbaum", "Redes y Seguridad", 2));
        listaLibros.add(new Libro("INF-005", "Inteligencia Artificial Moderna", "Russell & Norvig", "Inteligencia Artificial", 3));

        // INGENIERÍA CIVIL 
        listaLibros.add(new Libro("CIV-010", "Mecánica de Suelos", "Juarez Badillo", "Hidráulica y Geotecnia", 3));
        listaLibros.add(new Libro("CIV-011", "Diseño de Estructuras de Acero", "McCormac", "Diseño Estructural", 4));
        listaLibros.add(new Libro("CIV-012", "Materiales de Construcción", "Mamlouk", "Materiales y Construcción", 6));
        listaLibros.add(new Libro("CIV-013", "Hidráulica General", "Gilberto Sotelo", "Hidráulica y Geotecnia", 2));

        
        //CONTADURÍA / ADMINISTRACIÓN 
        listaLibros.add(new Libro("ADM-030", "Marketing Estratégico", "Kotler", "Marketing y Estrategia", 4));
        listaLibros.add(new Libro("ADM-031", "Administración de RRHH", "Chiavenato", "Recursos Humanos", 3));
        listaLibros.add(new Libro("CON-032", "Normas NIIF Completas", "Consejo Tecnico", "Contabilidad y Auditoría", 6));
        listaLibros.add(new Libro("CON-033", "Principios de Finanzas", "Gitman", "Finanzas y Tributación", 4));
        listaLibros.add(new Libro("CON-034", "Auditoría Práctica", "Whittington", "Contabilidad y Auditoría", 2));

       
        Estudiante e1 = new Estudiante("28111222", "Ana García", "Ingeniería en Informática", "0414-1234567", "ana@mail.com");
        Estudiante e2 = new Estudiante("30555666", "María Rodríguez", "Administración", "0416-5551122", "maria@mail.com");
        Estudiante e3 = new Estudiante("31777888", "Luis González", "Ingeniería Civil", "0424-1112233", "luis@mail.com");
        Estudiante e4 = new Estudiante("32888999", "Sofía Méndez", "Contaduría", "0412-1112223", "sofia@mail.com");

        listaEstudiantes.add(e1); listaEstudiantes.add(e2); listaEstudiantes.add(e3); listaEstudiantes.add(e4);

       
        // GENERAR PRÉSTAMOS PENDIENTES
        
        
       java.util.Calendar cal = java.util.Calendar.getInstance();

    // 1. Ana tiene "Clean Code" (Préstamo normal, vence en 3 días)
    Libro l_inf = listaLibros.get(1); 
    cal.setTime(new Date()); cal.add(java.util.Calendar.DAY_OF_YEAR, 3); // Fecha límite futuro
    Prestamo p1 = new Prestamo("P-100", e1, l_inf, new Date(), cal.getTime());
    l_inf.reducirStock();
    listaPrestamos.add(p1);

    // 2. Carlos tiene "Código Civil" (RETRASADO: Lo pidió hace 10 días y venció hace 5)
    Libro l_der = listaLibros.get(10); 
    
    cal.setTime(new Date()); cal.add(java.util.Calendar.DAY_OF_YEAR, -10); // Prestado hace 10 días
    Date fechaVieja = cal.getTime();
    
    cal.setTime(new Date()); cal.add(java.util.Calendar.DAY_OF_YEAR, -5); // Venció hace 5 días
    Date fechaLimiteVencida = cal.getTime();
    
    Prestamo p2 = new Prestamo("P-101", e2, l_der, fechaVieja, fechaLimiteVencida);
    l_der.reducirStock();
    listaPrestamos.add(p2);
        

        
        GestorArchivos.guardarLibros(listaLibros);
        GestorArchivos.guardarEstudiantes(listaEstudiantes);
        GestorArchivos.guardarPrestamos(listaPrestamos);
    }
}