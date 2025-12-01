/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InterfacesBiblioteca;

import AccesoDatosBiblioteca.BaseArchivos;
import AccesoDatosBiblioteca.Busqueda; 
import AccesoDatosBiblioteca.PrestamosCRUD;
import Entidadesbiblioteca.Estudiante;
import Entidadesbiblioteca.Prestamo;
import Entidadesbiblioteca.Libro;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Panel para el Registro de Nuevos Préstamos.
 * Utiliza filtros en cascada (Carrera -> Estudiante) para facilitar la búsqueda.
 */

public class PanelPrestamos extends JPanel {

    private final Color COLOR_BOTON = new Color(0x5D4037);
    private final Color COLOR_TEXTO = new Color(0x3E2723);

   //componentes  
    private JComboBox<String> cmbCarrera;
    private JComboBox<String> cmbEstudiante;
    private JComboBox<String> cmbLibro;
    private JComboBox<String> cmbCategoria;
    private JLabel lblFecha;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JSpinner spinDias;
    
    // Lógica de Negocio
    private final PrestamosCRUD logicaPrestamos = new PrestamosCRUD();

    public PanelPrestamos() {
        BaseArchivos.cargarDatosIniciales();//cargamos las listas para que no estn vacias
        
        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        

        JLabel lblTitulo = new JLabel("REGISTRO DE PRÉSTAMOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(250, 250, 250));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        
        actualizarTabla();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(330, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BOTON, 1, true), ""));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1.0;

       // 1. Filtro Carrera
        gbc.gridy = 0; 
        panel.add(createLabel("Filtrar por Carrera:"), gbc);
        
        gbc.gridy = 1; 
        //usamos la lista estatica que creamos en la clase estudiante
        cmbCarrera = new JComboBox<>(Estudiante.CARRERAS);
        styleComboBox(cmbCarrera);
        cmbCarrera.addActionListener(e -> cargarEstudiantesPorCarrera());
        panel.add(cmbCarrera, gbc);

        // 2. Selección Estudiante
        gbc.gridy = 2; 
        panel.add(createLabel("   Seleccionar Estudiante:"), gbc);
        
        gbc.gridy = 3; 
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.addItem("Filtre primero...");
        styleComboBox(cmbEstudiante);
        panel.add(cmbEstudiante, gbc);
       
        //categoria para filtrar
       gbc.gridy = 4; 
        panel.add(createLabel("Filtrar por Categoría:"), gbc);
        
        gbc.gridy = 5; 
        // Usamos la lista estática que creamos en la clase Libro
        cmbCategoria = new JComboBox<>(Libro.CATEGORIAS);
        styleComboBox(cmbCategoria);
        cmbCategoria.addActionListener(e -> cargarLibrosPorCategoria());
        panel.add(cmbCategoria, gbc);

        // Libro
        gbc.gridy = 6; panel.add(createLabel("Libro a Prestar:"), gbc);
        gbc.gridy = 7; cmbLibro = new JComboBox<>();
        styleComboBox(cmbLibro);
        panel.add(cmbLibro, gbc);

        // Fecha
        gbc.gridy = 8; panel.add(createLabel("Fecha de Préstamo:"), gbc);
        gbc.gridy = 9;
        lblFecha = createLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        lblFecha.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5,5,5,5)));
        panel.add(lblFecha, gbc);
        
        gbc.gridy = 10;
        panel.add(createLabel("Plazo de Entrega (Días):"), gbc);
        
        gbc.gridy = 11;
        // Default 3 días, mínimo 1, máximo 30, paso 1
        spinDias = new JSpinner(new SpinnerNumberModel(3, 1, 30, 1));
        spinDias.setPreferredSize(new Dimension(0, 35));
        spinDias.setBorder(new LineBorder(COLOR_BOTON, 1));
        panel.add(spinDias, gbc);
        // Botón
        gbc.gridy = 12; gbc.insets = new Insets(25, 10, 10, 10);
        JButton btnPrestar = new JButton("REALIZAR PRÉSTAMO");
        styleButton(btnPrestar);
        
        btnPrestar.addActionListener(e -> procesarPrestamo());
        
        panel.add(btnPrestar, gbc);
        gbc.gridy = 13; gbc.weighty = 1.0; panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }
    
    //cargamos el combo de estudiante en base a la carrera que escogio
    private void cargarEstudiantesPorCarrera() {
        cmbEstudiante.removeAllItems();
        String carrera = (String) cmbCarrera.getSelectedItem();
        
        if (carrera == null || carrera.startsWith("Seleccione")) {
            cmbEstudiante.addItem("Seleccione una carrera válida...");
            return;
        }
        
        boolean hayAlumnos = false;
        cmbEstudiante.addItem("Seleccione un estudiante...");
        
        for (Estudiante e : BaseArchivos.listaEstudiantes) {
            // Filtramos por la carrera seleccionada
            if (e.getCarrera().equals(carrera)) {
                cmbEstudiante.addItem(e.getNombre() + " (" + e.getCedula() + ")");
                hayAlumnos = true;
            }
        }
        
        if (!hayAlumnos) cmbEstudiante.addItem("No hay estudiantes registrados.");
    }
    //cargo el combo libro que voy a prestar en base a la categoria de libro que selecciono
    private void cargarLibrosPorCategoria() {
        cmbLibro.removeAllItems(); // Limpiar lista anterior
        
        String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
        
        if (categoriaSeleccionada == null || categoriaSeleccionada.equals("Seleccione una opción...")) {
            cmbLibro.addItem("Seleccione una categoría válida...");
            return;
        }
        
        boolean hayLibros = false;
        cmbLibro.addItem("Seleccione un libro...");
        
        // Recorremos la base de datos buscando coincidencias
        for (Libro l : BaseArchivos.listaLibros) {
            // Filtro 1: Que sea de la categoría
            // Filtro 2: Que tenga stock
            if (l.getGenero().equals(categoriaSeleccionada) && l.hayStock()) {
                cmbLibro.addItem(l.getTitulo() + " (" + l.getidLibro() + ")");
                hayLibros = true;
            }
        }
        
        if (!hayLibros) {
            cmbLibro.addItem("No hay libros disponibles en esta categoría");
        }
    }

  // se registra el prestamo
  private void procesarPrestamo() {
        // 1. VALIDACIÓN BÁSICA DE SELECCIÓN
        if (cmbEstudiante.getSelectedIndex() <= 0 || cmbLibro.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Estudiante y un Libro válidos.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String selEst = (String) cmbEstudiante.getSelectedItem();
            String selLib = (String) cmbLibro.getSelectedItem();

            // 2. VALIDACIÓN DE PLACEHOLDERS (Evitar error si seleccionan "No hay libros...")
            if (selEst.startsWith("No hay") || selLib.startsWith("No hay")) {
                JOptionPane.showMessageDialog(this, "La selección actual no es válida.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extraer IDs
            String cedulaEst = selEst.substring(selEst.lastIndexOf("(") + 1, selEst.lastIndexOf(")"));
            String idLibro = selLib.substring(selLib.lastIndexOf("(") + 1, selLib.lastIndexOf(")"));

            // Buscar Objetos
            Estudiante objEst = Busqueda.buscar(BaseArchivos.listaEstudiantes, e -> e.getCedula().equals(cedulaEst));
            Libro objLib = Busqueda.buscar(BaseArchivos.listaLibros, l -> l.getidLibro().equals(idLibro));
            
            // Obtener Días
            int dias = (int) spinDias.getValue();
            if (dias < 1) { // Validación extra de seguridad
                JOptionPane.showMessageDialog(this, "El plazo debe ser de al menos 1 día.");
                return;
            }

            if (objEst != null && objLib != null) {
                
                // 3. VALIDACIONES DE NEGOCIO ESPECÍFICAS (Antes de llamar al CRUD)
                // Así le damos un mensaje exacto al usuario
                
                if (!objLib.hayStock()) {
                    JOptionPane.showMessageDialog(this, 
                        "¡No queda Stock!\nEl libro '" + objLib.getTitulo() + "' no tiene copias disponibles.",
                        "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                 if (logicaPrestamos.tieneDeudaPendiente(objEst)) {
                    JOptionPane.showMessageDialog(this, 
                        "🚫 ACCESO DENEGADO\n\nEl estudiante " + objEst.getNombre() + 
                        "\ntiene préstamos ATRASADOS pendientes de devolución.",
                        "Estudiante Moroso", JOptionPane.ERROR_MESSAGE);
                    return; // Detenemos el proceso aquí
                }
                if (logicaPrestamos.tieneLibroPendiente(objEst, objLib)) {
                    JOptionPane.showMessageDialog(this, 
                        "Préstamo Duplicado.\nEl estudiante " + objEst.getNombre() + "\nya tiene una copia de este libro pendiente.",
                        "Acción Denegada", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 4. EJECUCIÓN DEL PRÉSTAMO
                boolean exito = logicaPrestamos.registrarPrestamo(objEst, objLib, dias);
                
                if (exito) {
                     JOptionPane.showMessageDialog(this, "Préstamo registrado. Vence en " + dias + " días.");

                    actualizarTabla();

                    cargarLibrosPorCategoria();
                } else {
                    // Si falla aquí es por un error desconocido (base de datos, etc)
                    JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Error crítico: Los datos seleccionados ya no existen en memoria.");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error procesando los datos: " + ex.getMessage());
        }
    }
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        
        for (Prestamo p : BaseArchivos.listaPrestamos) {
            modeloTabla.addRow(new Object[]{
                p.getIdPrestamo(), 
                p.getEstudiante().getNombre(), 
                p.getLibro().getTitulo(),
                p.getFechaLimpia(),
                p.getFechaLimiteLimpia(),
                p.getEstado()
            });
        }
    }

    //Estilos visuales
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()); panel.setOpaque(false);
       
        String[] columnas = {"ID", "Estudiante", "Libro","Fecha Prestamo","Fecha Entrega", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPrestamos = new JTable(modeloTabla);
        styleTable(tablaPrestamos);
        tablaPrestamos.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablaPrestamos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Inicio
        tablaPrestamos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Límite
        JScrollPane scroll = new JScrollPane(tablaPrestamos); scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private JLabel createLabel(String t) { 
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        l.setForeground(COLOR_TEXTO); 
        return l; 
    }
    
    private void styleComboBox(JComboBox<String> c) { 
        c.setPreferredSize(new Dimension(0, 35)); 
        c.setFont(new Font("Segoe UI",Font.PLAIN,11));
        c.setBackground(Color.WHITE);
        ((JComponent)c.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5)); 
    }
    
    private void styleButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setBackground(COLOR_BOTON); 
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(0, 40)); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
    }
    
    private void styleTable(JTable t) {
         t.setRowHeight(30);
        // Fuente del contenido normal
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // ESTILO DEL ENCABEZADO (TITULOS)
        t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD, 14)); 
        t.getTableHeader().setBackground(new Color(0xE1D7C6)); 
        t.getTableHeader().setForeground(COLOR_TEXTO); 
        
        t.setSelectionBackground(new Color(0x795548)); 
        t.setSelectionForeground(Color.WHITE);
    }
}