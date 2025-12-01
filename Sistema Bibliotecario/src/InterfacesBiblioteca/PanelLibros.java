/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InterfacesBiblioteca;

import AccesoDatosBiblioteca.Busqueda;
import AccesoDatosBiblioteca.BaseArchivos;
import Entidadesbiblioteca.Libro;
import AccesoDatosBiblioteca.LibroCRUD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelLibros extends JPanel {

    private final Color COLOR_BOTON = new Color(0x5D4037);
    private final Color COLOR_TEXTO = new Color(0x3E2723);
    
    private JTextField txtId, txtTitulo, txtAutor;
    private JSpinner spinStock; 
    private JComboBox<String> cmbCategoria; 
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    
    // Instancia Lógica
    private LibroCRUD logicaLibros = new LibroCRUD();

    public PanelLibros() {
        BaseArchivos.cargarDatosIniciales();
        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20)); 

        JLabel lblTitulo = new JLabel("INVENTARIO DE LIBROS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(250, 250, 250,245));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        
        llenarTabla();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BOTON, 1, true), ""));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.weightx = 1.0;

        // --- CAMPOS ---
        
        // 1. ID Libro
        gbc.gridy = 0; panel.add(createLabel("ID Libro (LIT-585):"), gbc);
        gbc.gridy = 1; txtId = createStyledTextField(); panel.add(txtId, gbc);

        // 2. Título
        gbc.gridy = 2; panel.add(createLabel("Título:"), gbc);
        gbc.gridy = 3; txtTitulo = createStyledTextField(); panel.add(txtTitulo, gbc);

        // 3. Autor
        gbc.gridy = 4; panel.add(createLabel("Autor (Solo letras):"), gbc);
        gbc.gridy = 5; txtAutor = createStyledTextField(); panel.add(txtAutor, gbc);

        // 4. Categoría
        gbc.gridy = 6; panel.add(createLabel("Categoría / Género:"), gbc);
        gbc.gridy = 7;
        try { cmbCategoria = new JComboBox<>(Libro.CATEGORIAS); } 
        catch (Exception e) { cmbCategoria = new JComboBox<>(new String[]{"General"}); }
        styleComboBox(cmbCategoria);
        panel.add(cmbCategoria, gbc);

        // 5. Stock (Spinner)
        gbc.gridy = 8; 
        panel.add(createLabel("Cantidad (Stock):"), gbc);
        gbc.gridy = 9; 
        spinStock = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        styleSpinner(spinStock);
        panel.add(spinStock, gbc);

        // --- BOTONES AGRUPADOS (IGUAL QUE EN ESTUDIANTES) ---
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 8)); // 3 filas, 8px espacio
        panelBotones.setOpaque(false);

        JButton btnGuardar = new JButton("REGISTRAR");
        styleButton(btnGuardar);
        
        JButton btnEditar = new JButton("EDITAR");
        styleButton(btnEditar);
        
        JButton btnEliminar = new JButton("ELIMINAR");
        styleButton(btnEliminar);
        JButton btnLimpiar = new JButton("LIMPIAR");
        styleButton(btnLimpiar);

    // Lógica del botón Limpiar
        btnLimpiar.addActionListener(e -> {
        limpiarFormulario(); 
        llenarTabla();
        });
        
        // --- LÓGICA ---
        
        btnGuardar.addActionListener(e -> {
            if (validarEntradas()) {
                if (existeLibro(txtId.getText())) {
                    mostrarError("El ID del libro ya existe."); return;
                }
                Libro nuevo = new Libro(
                    txtId.getText(), txtTitulo.getText(), txtAutor.getText(),
                    cmbCategoria.getSelectedItem().toString(), (int) spinStock.getValue()
                );
                logicaLibros.agregarLibro(nuevo);
                mostrarMensaje("Libro agregado exitosamente.");
                limpiarFormulario();
                llenarTabla();
            }
        });

        btnEditar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) { 
                mostrarError("Seleccione un libro de la tabla."); return; 
            }

            if (validarEntradas()) { 
                boolean exito = logicaLibros.actualizarLibro(
                    txtId.getText(), txtTitulo.getText(), txtAutor.getText(),
                    cmbCategoria.getSelectedItem().toString(), (int) spinStock.getValue()
                );
                if (exito) {
                    mostrarMensaje("Libro actualizado.");
                    limpiarFormulario();
                    llenarTabla();
                } else {
                    mostrarError("No se encontró el ID.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) { mostrarError("Ingrese el ID."); return; }
            
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar libro?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (logicaLibros.eliminarLibro(txtId.getText())) {
                    mostrarMensaje("Libro eliminado.");
                    limpiarFormulario();
                    llenarTabla();
                } else {
                    mostrarError("ID no encontrado.");
                }
            }
        });

        // AGREGAR AL SUB-PANEL
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // AGREGAR AL FORMULARIO
        gbc.gridy = 10;
        gbc.insets = new Insets(15, 10, 10, 10); // Margen superior
        panel.add(panelBotones, gbc);
        
        // EMPUJE FINAL
        gbc.gridy = 11; gbc.weighty = 1.0; 
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }
private boolean validarEntradas() {
        if (txtId.getText().trim().isEmpty() || txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty()) {
            mostrarError("Todos los campos de texto son obligatorios.");
            return false;
        }
        String id = txtId.getText().trim();
        
        if (!id.matches("[a-zA-Z0-9\\-]{3,}")) {
            mostrarError("El ID debe tener al menos 3 caracteres (Letras, números o guiones). Ej: LIB-001");
            return false;
        }
        if (cmbCategoria.getSelectedIndex() <= 0 || cmbCategoria.getSelectedItem().toString().startsWith("Seleccione")) {
            mostrarError("Seleccione una categoría válida.");
            return false;
        }
        int stock = (int) spinStock.getValue();
        if (stock < 0) {
            mostrarError("El stock no puede ser negativo.");
            return false;
        }
        if(stock ==0){
          int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de registrar el libro con Stock 0?", 
                "Advertencia de Stock", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return false;
        }
       
        String autor = txtAutor.getText().trim();
        if (autor.length() < 3) {
            mostrarError("El nombre del autor es muy corto.");
            return false;
        }
        if (!autor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ .]+")) {
            mostrarError("El nombre del Autor no puede contener números ni símbolos especiales.");
            return false;
        }
        return true;
    }

private boolean existeLibro(String id) {
        return Busqueda.buscar(BaseArchivos.listaLibros, l -> l.getidLibro().equals(id)) != null;
    }
public void buscarYLlenar(String texto) {
       String textoMin = texto.toLowerCase();
        // Busca por ID o por Título (ignorando mayúsculas)
       Libro encontrado = Busqueda.buscar(BaseArchivos.listaLibros, l -> 
           
            l.getidLibro().toLowerCase().contains(textoMin) || 
           
            l.getTitulo().toLowerCase().contains(textoMin) ||
           
            l.getAutor().toLowerCase().contains(textoMin));
       
               if (encontrado != null) {
            // Llenar campos
            txtId.setText(encontrado.getidLibro());
            txtTitulo.setText(encontrado.getTitulo());
            txtAutor.setText(encontrado.getAutor());
            cmbCategoria.setSelectedItem(encontrado.getGenero());
            spinStock.setValue(encontrado.getStock());
            
            // Filtrar tabla visualmente
            modeloTabla.setRowCount(0);
            modeloTabla.addRow(new Object[]{
                encontrado.getidLibro(), encontrado.getTitulo(), 
                encontrado.getAutor(), encontrado.getGenero(), encontrado.getStock()
            });
            tablaLibros.setRowSelectionInterval(0, 0);
        } else {
            mostrarError("Libro no encontrado.");
            llenarTabla(); // Restaurar tabla completa
        }
    }
    

    public void llenarTabla() {
        modeloTabla.setRowCount(0);
        ArrayList<Libro> lista = logicaLibros.obtenerLibros();
        if (lista != null) {
            for (Libro l : lista) {
                modeloTabla.addRow(new Object[]{l.getidLibro(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getStock()});
            }
        }
    }
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarFormulario() {
        txtId.setText(""); txtTitulo.setText(""); txtAutor.setText("");
        cmbCategoria.setSelectedIndex(0); spinStock.setValue(1);
    }
    
    // --- Estilos Visuales (Idénticos al anterior) ---
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
       
        String[] columnas = {"ID", "Título", "Autor", "Categoría", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaLibros = new JTable(modeloTabla);
        styleTable(tablaLibros);
        tablaLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaLibros.getSelectedRow();
                if (fila >= 0) {
                    // Pasar datos de la fila seleccionada a las cajas de texto
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtTitulo.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtAutor.setText(modeloTabla.getValueAt(fila, 2).toString());
                    cmbCategoria.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());
                 
                    spinStock.setValue(modeloTabla.getValueAt(fila, 4));
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tablaLibros); scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    private JLabel createLabel(String t) {
    JLabel l = new JLabel(t);
    l.setFont(new Font("Segoe UI", Font.BOLD, 14));
    l.setForeground(COLOR_TEXTO); return l; }
    private JTextField createStyledTextField() { 
    JTextField t = new JTextField(); 
    t.setPreferredSize(new Dimension(0, 30)); 
    t.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COLOR_BOTON, 1),
            new EmptyBorder(5, 5, 5, 5))); 
    return t; }
    private void styleComboBox(JComboBox<String> c) { 
    c.setPreferredSize(new Dimension(0, 20)); 
    c.setBackground(Color.WHITE); 
    ((JComponent)c.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5));
    }
    private void styleSpinner(JSpinner s) { 
        s.setPreferredSize(new Dimension(0, 50)); 
        s.setBorder(new LineBorder(COLOR_BOTON, 1));
        s.setFont( new Font("Segoe UI",Font.PLAIN,15));
        
    }
    private void styleButton(JButton b) { 
        b.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        b.setForeground(Color.WHITE); b.setBackground(COLOR_BOTON);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false); b.setPreferredSize(new Dimension(0, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
    private void styleTable(JTable t) {
    t.setRowHeight(30);
        // Fuente del contenido normal
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // ESTILO DEL ENCABEZADO (TITULOS)
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        t.getTableHeader().setBackground(new Color(0xE1D7C6)); 
        t.getTableHeader().setForeground(COLOR_TEXTO); 
        
        t.setSelectionBackground(new Color(0x795548)); 
        t.setSelectionForeground(Color.WHITE);
    }
}