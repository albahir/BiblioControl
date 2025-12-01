package InterfacesBiblioteca;

import AccesoDatosBiblioteca.BaseArchivos;
import AccesoDatosBiblioteca.PrestamosCRUD;
import Entidadesbiblioteca.Prestamo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel para gestionar la devolución de libros.
 * Incluye lógica de filtrado y ordenamiento automático de préstamos.
 */

public class PanelDevoluciones extends JPanel {

    private final Color COLOR_BOTON = new Color(0x5D4037); // Caoba
    private final Color COLOR_BLANCO_SUAVE = new Color(245, 245, 245); // Blanco suave
    private final Color COLOR_TEXTO_NORMAL = new Color(0x3E2723); // Marrón oscuro

    //componenetes de la interfaz
    private JCheckBox chkSoloPendientes;
    private JComboBox<String> cmbPrestamosPendientes;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    
    //instancia logica
    private final PrestamosCRUD logicaPrestamos = new PrestamosCRUD();

    public PanelDevoluciones() {
        BaseArchivos.cargarDatosIniciales();//carga de datos
        
        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("DEVOLUCIÓN DE LIBROS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(COLOR_BLANCO_SUAVE); // Título blanco suave
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        
       actualizarTodo();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(350, 0));
        // Borde Limpio
        panel.setBorder(new LineBorder(COLOR_BOTON, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1.0;
      
        //combo de seleccion
        gbc.gridy = 0; panel.add(createLabel("Seleccione Préstamo Pendiente:"), gbc);
        gbc.gridy = 1; cmbPrestamosPendientes = new JComboBox<>();
        styleComboBox(cmbPrestamosPendientes);
        panel.add(cmbPrestamosPendientes, gbc);
       
        //  Checkbox de Filtro
        gbc.gridy = 2; 
        chkSoloPendientes = new JCheckBox("Ver solo Pendientes en tabla");
        chkSoloPendientes.setOpaque(false);
        chkSoloPendientes.setForeground(COLOR_TEXTO_NORMAL);
        chkSoloPendientes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkSoloPendientes.setSelected(false); // Por defecto ver todo
        
        // Evento: Al marcar/desmarcar, actualiza la tabla
        chkSoloPendientes.addActionListener(e -> cargarDatosTabla());
        panel.add(chkSoloPendientes, gbc);
        
        //boton de accion
        gbc.gridy = 3; gbc.insets = new Insets(30, 10, 10, 10);
        JButton btnDevolver = new JButton("REGISTRAR DEVOLUCIÓN");
        styleButton(btnDevolver);
        
        btnDevolver.addActionListener(e -> procesarDevolucion());
        
        panel.add(btnDevolver, gbc);
        gbc.gridy = 4; gbc.weighty = 1.0; panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }
   
    //aqui ocurre lo importante validamos la seleccion,busca el objeto y cambia false su estado 
    private void procesarDevolucion() {
        if (cmbPrestamosPendientes.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para devolver.");
            return;
        }
        
        // Extraer ID del string del combo: "Titulo - Nombre (ID)"
        String seleccion = (String) cmbPrestamosPendientes.getSelectedItem();
        String idPrestamo = seleccion.substring(seleccion.lastIndexOf("(") + 1, seleccion.lastIndexOf(")"));

        // Buscar el objeto Préstamo real en memoria
        Prestamo prestamoEncontrado = logicaPrestamos.buscarPrestamoPorId(idPrestamo);
        if (prestamoEncontrado != null) {
            logicaPrestamos.devolverLibro(prestamoEncontrado);
            JOptionPane.showMessageDialog(this, "Libro devuelto correctamente.");
            actualizarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "Error al encontrar el préstamo.");
        }
    }
    private void actualizarTodo() {
        cargarComboPendientes();
        cargarDatosTabla();
    }
    //cargar el combo box con los prestamos que estan activos
    private void cargarComboPendientes() {
        cmbPrestamosPendientes.removeAllItems();
        cmbPrestamosPendientes.addItem("Seleccione un préstamo...");
        
        for (Prestamo p : BaseArchivos.listaPrestamos) {
            if (p.isActivo()) {
                // Formato para que el usuario sepa qué selecciona
                String item = p.getLibro().getTitulo() + " - " + p.getEstudiante().getNombre() + " (" + p.getIdPrestamo() + ")";
                cmbPrestamosPendientes.addItem(item);
            }
        }
    }
    //carga la tabla aplicando filtros 
   private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        
        // Copia de la lista para no modificar la original al filtrar
        List<Prestamo> listaMostrar = new ArrayList<>(BaseArchivos.listaPrestamos);
        
        // 1. Filtro si esta marcado el checkbox
        if (chkSoloPendientes.isSelected()) {
            listaMostrar = listaMostrar.stream()
                .filter(Prestamo::isActivo)
                .collect(Collectors.toList());
        }
        
        // 2. Pendientes arriba,devueltos abajo
        listaMostrar.sort((p1, p2) -> {
            int estado = Boolean.compare(p2.isActivo(), p1.isActivo());
            if (estado != 0) return estado;
            return p1.getIdPrestamo().compareTo(p2.getIdPrestamo());
        });

        // 3. Llenar filas
        for (Prestamo p : listaMostrar) {
           
            modeloTabla.addRow(new Object[]{
                p.getIdPrestamo(),              
                p.getLibro().getTitulo(),       
                p.getEstudiante().getNombre(),  
                p.getFechaLimpia(),
                p.getFechaLimiteLimpia(),
                p.getEstado()
            });
        }
    }
    // --- Visuales ---
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        panel.setBorder(new LineBorder(COLOR_BOTON, 1, true));
       
        //columnas
        String[] columnas = {"ID","Libro", "Estudiante","Fecha Prestamo","Fecha Entrega", "Estado"};
        
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(modeloTabla);
        styleTable(tablaHistorial);
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(80); // Inicio
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(80);
      
        //Evento: Al hacer clic en una fila pendiente, se selecciona en el combo automáticamente
        tablaHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaHistorial.getSelectedRow();
                if (fila >= 0) {
                    // Obtener ID de la columna 0
                    String idPrestamo = modeloTabla.getValueAt(fila, 0).toString();
                    String estado = modeloTabla.getValueAt(fila, 5).toString(); 
                    
                    if (estado.equals("Retrasado")||estado.equals("Pendiente")) {
                        seleccionarEnCombo(idPrestamo);
                    } else {
                        // Si está devuelto, reseteamos el combo
                        cmbPrestamosPendientes.setSelectedIndex(0);
                    }
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tablaHistorial); scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    // Busca el ID en el combo y lo selecciona
    private void seleccionarEnCombo(String idTarget) {
        for (int i = 0; i < cmbPrestamosPendientes.getItemCount(); i++) {
            String item = cmbPrestamosPendientes.getItemAt(i);
            // El item termina en " (P-1)", buscamos si contiene el ID entre paréntesis
            if (item.contains("(" + idTarget + ")")) {
                cmbPrestamosPendientes.setSelectedIndex(i);
                return;
            }
        }
    }
    //estilos visuales
    private JLabel createLabel(String t) { 
        JLabel l = new JLabel(t); 
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(COLOR_TEXTO_NORMAL); return l;
    }
    private void styleComboBox(JComboBox<String> c) {
        c.setPreferredSize(new Dimension(0, 35));
         c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        c.setBackground(Color.WHITE); 
        ((JComponent)c.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5)); 
    }
    
    // CORRECCIÓN AQUÍ: Se añade setOpaque(true) y setBorderPainted(false) para forzar el color de fondo
    private void styleButton(JButton b) { 
        b.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        b.setForeground(COLOR_BLANCO_SUAVE); 
        b.setBackground(COLOR_BOTON); 
        b.setOpaque(true); 
        b.setBorderPainted(false);
        b.setFocusPainted(false); 
        b.setPreferredSize(new Dimension(0, 45)); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
    }
    
    private void styleTable(JTable t) { 
     t.setRowHeight(30);
        // Fuente del contenido normal
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // ESTILO DEL ENCABEZADO (TITULOS)
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        t.getTableHeader().setBackground(new Color(0xE1D7C6)); 
        t.getTableHeader().setForeground(COLOR_TEXTO_NORMAL); 
        
        t.setSelectionBackground(new Color(0x795548)); 
        t.setSelectionForeground(Color.WHITE); }
}