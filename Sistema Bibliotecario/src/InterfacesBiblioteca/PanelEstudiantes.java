package InterfacesBiblioteca;

import AccesoDatosBiblioteca.BaseArchivos;
import AccesoDatosBiblioteca.Busqueda;
import AccesoDatosBiblioteca.EstudiantesCRUD;
import Entidadesbiblioteca.Estudiante;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Panel de Gestión CRUD para Estudiantes.
 * Incluye validaciones de formato, control de duplicados y búsqueda.
 */
public class PanelEstudiantes extends JPanel {

    // --- PALETA DE COLORES ---
    private final Color COLOR_BOTON = new Color(0x5D4037);
    private final Color COLOR_TEXTO = new Color(0x3E2723);

    // Componentes globales
    private JTextField txtCedula, txtNombre, txtTelefono, txtCorreo;
    private JComboBox<String> cmbCarrera; 
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    
    // Instancia del CRUD
    EstudiantesCRUD crud = new EstudiantesCRUD();

    public PanelEstudiantes() {
        BaseArchivos.cargarDatosIniciales(); //evitando cargar vacios
        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE ESTUDIANTES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(250, 250, 250, 245));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // paneles (Izquierda Formulario, Centro Tabla)
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        
        // Cargar datos iniciales en la tabla (si hay)
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

        // CAMPOS
        
        // 1. Cédula
        gbc.gridy = 0; panel.add(createLabel("Cédula:"), gbc);
        gbc.gridy = 1; txtCedula = createStyledTextField(); panel.add(txtCedula, gbc);

        // 2. Nombre
        gbc.gridy = 2; panel.add(createLabel("Nombre Completo:"), gbc);
        gbc.gridy = 3; txtNombre = createStyledTextField(); panel.add(txtNombre, gbc);

        // 3. Carrera
        gbc.gridy = 4; panel.add(createLabel("Carrera:"), gbc);
        gbc.gridy = 5;
        
        //usando la lista centralizada en la clase estudiante
        cmbCarrera = new JComboBox<>(Estudiante.CARRERAS);
        styleComboBox(cmbCarrera);
        panel.add(cmbCarrera, gbc);

        // 4. Teléfono
        gbc.gridy = 6; panel.add(createLabel("Teléfono:"), gbc);
        gbc.gridy = 7; txtTelefono = createStyledTextField(); panel.add(txtTelefono, gbc);

        // 5. Correo
        gbc.gridy = 8; panel.add(createLabel("Correo Electrónico:"), gbc);
        gbc.gridy = 9; txtCorreo = createStyledTextField(); panel.add(txtCorreo, gbc);

        // --- AQUÍ ESTÁ EL CAMBIO PARA QUE SE VEAN LOS BOTONES ---
        // Creamos un sub-panel que agrupa los botones pegaditos
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 8)); // 3 filas, 8px de separación
        panelBotones.setOpaque(false);
        
       //botones
        JButton btnRegistrar = new JButton("REGISTRAR");
        styleButton(btnRegistrar);
        
        JButton btnEditar = new JButton("EDITAR");
        styleButton(btnEditar);
        
        JButton btnEliminar = new JButton("ELIMINAR");
        styleButton(btnEliminar);
        
        JButton btnLimpiar = new JButton("LIMPIAR");
        styleButton(btnLimpiar);

  
   
        // Acciones
        
        //boton registrar
        btnRegistrar.addActionListener(e -> {
            if (!validarEntradas()) return; 

            // 2. VALIDAR DUPLICADOS (Usando el CRUD)
            if (crud.existeCedula(txtCedula.getText())) {
                mostrarError("La Cédula ya está registrada.");
                return;
            }
            if (crud.existeCorreo(txtCorreo.getText())) {
                mostrarError("El Correo ya está registrado.");
                return;
            }
            if (crud.existeTelefono(txtTelefono.getText())) {
                mostrarError("El Teléfono ya está registrado.");
                return;
            }
          
            Estudiante nuevo = new Estudiante(
                txtCedula.getText(), txtNombre.getText(), 
                cmbCarrera.getSelectedItem().toString(), 
                txtTelefono.getText(), txtCorreo.getText()
            );
            crud.agregarEstudiante(nuevo);
            JOptionPane.showMessageDialog(this, "Estudiante Registrado");
            
            llenarTabla();
            limpiarFormulario();
        });
  
        //boton editar
        btnEditar.addActionListener(e -> {
          if (txtCedula.getText().isEmpty()) { 
                mostrarError("Seleccione un estudiante de la tabla."); return; 
            }
            if (!validarEntradas()) return; 
            
            String miCedula = txtCedula.getText();
            String nuevoCorreo = txtCorreo.getText();
            String nuevoTelef = txtTelefono.getText();

            // validacion de duplicados especial para EDITAR 
            
            // A. Buscar si existe OTRO estudiante con este correo
            Estudiante otroConCorreo = Busqueda.buscar(BaseArchivos.listaEstudiantes, 
                est -> est.getCorreo().equalsIgnoreCase(nuevoCorreo) && !est.getCedula().equals(miCedula));
            
            if (otroConCorreo != null) {
                mostrarError("El correo ya pertenece a otro estudiante.");
                return;
            }

            // B. Buscar si existe OTRO estudiante con este teléfono
            Estudiante otroConTelef = Busqueda.buscar(BaseArchivos.listaEstudiantes, 
                est -> est.getTelefono().equals(nuevoTelef) && !est.getCedula().equals(miCedula));
            
            if (otroConTelef != null) {
                mostrarError("El teléfono ya pertenece a otro estudiante.");
                return;
            }
           
            boolean exito = crud.actualizarEstudiante(
                txtCedula.getText(), txtNombre.getText(), 
                cmbCarrera.getSelectedItem().toString(), 
                txtTelefono.getText(), txtCorreo.getText()
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Datos Actualizados");
                llenarTabla();
                limpiarFormulario();
            } else {
                mostrarError("Error: No se encontró la cédula (No se puede editar la cédula).");
            }
        });
        
        //boton eliminar
        btnEliminar.addActionListener(e -> {
          if (txtCedula.getText().isEmpty()) { 
                mostrarError("Seleccione un estudiante para eliminar."); return; 
            }
            
      
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar al estudiante " + txtNombre.getText() + "?",
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = crud.eliminarEstudiante(txtCedula.getText());
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Estudiante Eliminado");
                    llenarTabla();
                    limpiarFormulario();
                } else {
                    mostrarError("Error: Cédula no encontrada");
                }
            }
        });
        //boton limpiar
         btnLimpiar.addActionListener(e -> {
        limpiarFormulario(); 
        llenarTabla();
    });
        // Agregamos los botones al sub-panel
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        // Agregamos el sub-panel al formulario principal
        gbc.gridy = 10;
        gbc.insets = new Insets(15, 10, 10, 10); // Margen superior
        panel.add(panelBotones, gbc);

        // Empuje final hacia arriba para compactar todo
        gbc.gridy = 11;
        gbc.weighty = 1.0; 
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }
    //metodo tipo boolenao para validar entradas en general
private boolean validarEntradas() {
        // 1. Campos Vacíos
        if (txtCedula.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || 
            txtTelefono.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return false;
        }
        if (cmbCarrera.getSelectedIndex() == 0) {
            mostrarError("Seleccione una carrera.");
            return false;
        }

        // 2. Validación CÉDULA (Solo números, longitud exacta o máxima)
        String cedula = txtCedula.getText().trim();
        if (!cedula.matches("[0-9]+")) {
            mostrarError("La Cédula debe contener solo números.");
            return false;
        }
        if (cedula.length() > 10||cedula.length()<7) { // Límite de caracteres
            mostrarError("La Cédula no puede tener más de 10 dígitos.");
            return false;
        }

        // 3. Validación NOMBRE (Solo letras y espacios)
        String nombre = txtNombre.getText().trim();
        if (nombre.length() < 3) {
            mostrarError("El nombre es muy corto.");
            return false;
        }
        // [a-zA-ZáéíóúÁÉÍÓÚñÑ ] incluye letras, tildes y espacios
        if (!txtNombre.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("El Nombre solo puede contener letras.");
            return false;
        }

        // 4. Validación TELÉFONO (Solo números, longitud)
        String telefono = txtTelefono.getText().trim();
        if (!telefono.matches("[0-9]+")) {
            mostrarError("El Teléfono debe contener solo números.");
            return false;
        }
        if (telefono.length() != 11) {
            mostrarError("El Teléfono no puede exceder los 11 dígitos.");
            return false;
        }

        // 5. Validación CORREO (Formato básico)
        String correo = txtCorreo.getText().trim();
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarError("Formato de correo inválido (ej: usuario@email.com).");
            return false;
        }

        return true;
    }
 //metodo auxiliar para armar un mensaje de error
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de Validación", JOptionPane.WARNING_MESSAGE);
    }
    
    //Estilos visuales
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR_BOTON, 1, true)));

        String[] columnas = {"Cédula", "Nombre", "Carrera", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEstudiantes = new JTable(modeloTabla);
        styleTable(tablaEstudiantes);
        
        // Evento: Al hacer clic en la tabla, llenar el formulario
        tablaEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaEstudiantes.getSelectedRow();
                if (fila >= 0) {
                    // Tomamos el valor de la tabla y llenamos las cajas
                    String cedulaTabla = modeloTabla.getValueAt(fila, 0).toString();
                    
                    // Buscamos el objeto completo en la memoria para obtener todos los datos (incluyendo correo)
                    Estudiante est = Busqueda.buscar(BaseArchivos.listaEstudiantes, e -> e.getCedula().equals(cedulaTabla));
                    
                    if (est != null) {
                        txtCedula.setText(est.getCedula());
                        txtNombre.setText(est.getNombre());
                        cmbCarrera.setSelectedItem(est.getCarrera());
                        txtTelefono.setText(est.getTelefono());
                        txtCorreo.setText(est.getCorreo());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaEstudiantes);
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    
    public void llenarTabla() {
        modeloTabla.setRowCount(0); 
        for (Estudiante e : BaseArchivos.listaEstudiantes) {
            Object[] fila = { e.getCedula(), e.getNombre(), e.getCarrera(), e.getTelefono() };
            modeloTabla.addRow(fila);
        }
    }

    //metodo publico (conectado al buscador)
    public void buscarYLlenar(String textoBusqueda) {
        String textoMin = textoBusqueda.toLowerCase();

        Estudiante encontrado = Busqueda.buscar(BaseArchivos.listaEstudiantes, est -> 
          
            est.getCedula().contains(textoMin) || 
            
            est.getNombre().toLowerCase().contains(textoMin)||
             est.getCarrera().toLowerCase().contains(textoMin)
        );
        if (encontrado != null) {
            // 2. LLENAR LOS CAMPOS DEL FORMULARIO (Como antes)
            txtCedula.setText(encontrado.getCedula());
            txtNombre.setText(encontrado.getNombre());
            txtTelefono.setText(encontrado.getTelefono());
            txtCorreo.setText(encontrado.getCorreo());
            cmbCarrera.setSelectedItem(encontrado.getCarrera());

            // 3. ACTUALIZAR LA TABLA PARA MOSTRAR SOLO ESTE RESULTADO
            modeloTabla.setRowCount(0); // Borramos todo visualmente
         // Llenar formulario
            txtCedula.setText(encontrado.getCedula());
            txtNombre.setText(encontrado.getNombre());
            txtTelefono.setText(encontrado.getTelefono());
            txtCorreo.setText(encontrado.getCorreo());
            cmbCarrera.setSelectedItem(encontrado.getCarrera());

            // Filtrar tabla visualmente
            modeloTabla.setRowCount(0);
            modeloTabla.addRow(new Object[]{ 
                encontrado.getCedula(), encontrado.getNombre(), 
                encontrado.getCarrera(), encontrado.getTelefono() 
            });
            tablaEstudiantes.setRowSelectionInterval(0, 0);
        } else {
            // Si no encuentra nada, avisamos y restauramos la tabla completa
            JOptionPane.showMessageDialog(this, "No se encontró ningún estudiante con ese dato.");
            llenarTabla(); // Vuelve a mostrar todos
            limpiarFormulario();
        }
    }

    public void limpiarFormulario() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cmbCarrera.setSelectedIndex(0);
    }

    // --- ESTILOS VISUALES ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }
    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 30));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COLOR_BOTON, 1), 
                new EmptyBorder(5, 5, 5, 5)));
        return txt;
    }
    private void styleComboBox(JComboBox<String> cmb) {
        cmb.setPreferredSize(new Dimension(0, 25)); // Ajustado a 25 para ahorrar espacio
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmb.setBackground(Color.WHITE);
        ((JComponent) cmb.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5));
    }
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_BOTON);
        btn.setOpaque(true);
        btn.setBorderPainted(false); 
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 35)); // Altura ajustada
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0xE1D7C6));
        table.getTableHeader().setForeground(COLOR_TEXTO);
        table.setSelectionBackground(new Color(0x795548));
        table.setSelectionForeground(Color.WHITE);
    }
}