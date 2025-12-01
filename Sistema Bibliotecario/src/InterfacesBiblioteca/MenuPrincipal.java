package InterfacesBiblioteca;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Ventana principal de la aplicación (JFrame).
 * Gestiona la navegación entre los diferentes módulos y el buscador global.
 */

public class MenuPrincipal extends JFrame {

    // --- PALETA DE COLORES ---
    private final Color COLOR_FONDO_INICIO = new Color(0x3E2723); // Caoba
    private final Color COLOR_FONDO_FIN = new Color(0xE1D7C6);    // Pergamino
    private final Color COLOR_BARRA_SUP = new Color(0, 0, 0, 80); // Barra semi-transparente
    private final Color COLOR_BOTON = new Color(0x3E2723);        // Cuero
    private final Color COLOR_BOTON_HOVER = new Color(0x5D4037);
    private final Color COLOR_TEXTO = new Color(245, 245, 245);

    // Componentes globales
    private final JPanel panelContenido; // se usa para poder cambiar de panel y poner en contexto
    private JTextField txtBuscador;
    private JPanel panelBuscador; 

    public MenuPrincipal() {
        setTitle("BiblioControl - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 830); 
        setLocationRelativeTo(null);

        //  Panel de Fondo General degradado
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Construir la Zona Superior (Header + Navegacion)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.setOpaque(false);
        
        //  Barra de Título y Buscador
        JPanel headerPanel = createHeader();
        topContainer.add(headerPanel, BorderLayout.NORTH);

        // Menú de Opciones Horizontal
        JPanel menuPanel = createHorizontalMenu();
        topContainer.add(menuPanel, BorderLayout.CENTER);

        mainPanel.add(topContainer, BorderLayout.NORTH);

        //  Zona de Contenido (Donde se cargan los crud)
        panelContenido = new JPanel();
        panelContenido.setOpaque(false); 
        panelContenido.setLayout(new BorderLayout());
        
        
        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setOpaque(false);
        wrapperContent.setBorder(new EmptyBorder(20, 30, 30, 30));
        wrapperContent.add(panelContenido);
        mainPanel.add(wrapperContent, BorderLayout.CENTER);
        //Buscador inicia sin vista  solo visible en lugares  especificos
        panelBuscador.setVisible(false);
    }

    //  Gestion la visibilidad segun el modulo
    private void mostrarPanel(JPanel nuevoPanel) {
        panelContenido.removeAll(); 
        panelContenido.add(nuevoPanel, BorderLayout.CENTER); 
        
        //solo se muestra el buscador en estudiantes y libros
       
        if (nuevoPanel instanceof PanelEstudiantes || nuevoPanel instanceof PanelLibros) {
            panelBuscador.setVisible(true);
            resetearBuscador();
        } else {
            // En Inicio, Préstamos y Devoluciones se oculta
            panelBuscador.setVisible(false);
        }
       

        panelContenido.revalidate(); 
        panelContenido.repaint();
    }

    //Cabcera con el buscador y el titulo o logo
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_BARRA_SUP);
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Título a la izquierda
        JLabel lblLogo = new JLabel("BIBLIOCONTROL");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(COLOR_TEXTO);
        
       //inicializacion del panel de busqueda
        panelBuscador = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // <--- AGREGA ESTO
        panelBuscador.setOpaque(false);
      txtBuscador = new JTextField(20);
        styleSearchField(txtBuscador); 
        
        // Configuración inicial del texto fantasma
        String placeholder = "Buscador";
        txtBuscador.setText(placeholder);
        txtBuscador.setForeground(Color.GRAY);

        // Agregamos eventos para simular el efecto 
        txtBuscador.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
               
                if (txtBuscador.getText().equals(placeholder)) {
                    txtBuscador.setText("");
                    txtBuscador.setForeground(new Color(0x3E2723)); // Color normal (Marrón oscuro)
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                
                if (txtBuscador.getText().isEmpty()) {
                    txtBuscador.setForeground(Color.GRAY);
                    txtBuscador.setText(placeholder);
                }
            }
        });

        // Evento Enter ejecuta busqueda
        txtBuscador.addActionListener(e -> {
            // Evitar que busque el texto del placeholder
            if (!txtBuscador.getText().equals(placeholder)) {
                realizarBusquedaGlobal();
            }
        });
       
        panelBuscador.add(txtBuscador);
        header.add(lblLogo, BorderLayout.WEST);
        header.add(panelBuscador, BorderLayout.EAST); 
        
        return header;
    }


    // Barra de navegacion horizontal
    private JPanel createHorizontalMenu() {
        JPanel menuBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        menuBar.setOpaque(false);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 50)));

        // Botones del Menú (
         
        JButton btnEstudiantes = createMenuButton("ESTUDIANTES");
        JButton btnLibros = createMenuButton("LIBROS");
        JButton btnPrestamos = createMenuButton("PRÉSTAMOS");
        JButton btnDevoluciones = createMenuButton("DEVOLUCIONES"); 
        JButton btnSalir = createMenuButton("SALIR");

        // Acciones
 
        btnEstudiantes.addActionListener(e -> mostrarPanel(new PanelEstudiantes()));
        btnLibros.addActionListener(e -> mostrarPanel(new PanelLibros())); 
        btnPrestamos.addActionListener(e -> mostrarPanel(new PanelPrestamos()));
        btnDevoluciones.addActionListener(e -> mostrarPanel(new PanelDevoluciones()));
        btnSalir.addActionListener(e -> {
            
            new LoginScreen().setVisible(true); 
            this.dispose();
        });
        
    
        menuBar.add(btnEstudiantes);
        menuBar.add(btnLibros);
        menuBar.add(btnPrestamos);
        menuBar.add(btnDevoluciones); 
        menuBar.add(btnSalir);

        return menuBar;
    }
    //restaura el texto que se encuentra dentro del textfield
    private void resetearBuscador() {
        txtBuscador.setText("Buscador");
        txtBuscador.setForeground(Color.GRAY);
        this.requestFocusInWindow(); 
    }
   // logica para enviar el texto del buscador al panel que se encurntra activo 
    private void realizarBusquedaGlobal() {
        String texto = txtBuscador.getText().trim();
        String placeholder = "Buscador "; 

        
        if (texto.equals(placeholder)) {
            texto = "";
        }
        // Obtenemos el panel visible actualmente
        Component panelActual = null;
        if (panelContenido.getComponentCount() > 0) {
            panelActual = panelContenido.getComponent(0);
        }

        //si el texto esta vacio reseteamos las tablas
        if (texto.isEmpty()) {
            switch (panelActual) {
                case PanelEstudiantes panelEstudiantes -> {
                    panelEstudiantes.llenarTabla();
                    panelEstudiantes.limpiarFormulario();
                }
                case PanelLibros panelLibros -> {
                    panelLibros.llenarTabla();
                    panelLibros.limpiarFormulario();
                }
                default -> {
                }
            }
                return;
        }

       //si hay texto,filtramos el panel correspondiente
        switch (panelActual) {
            case PanelEstudiantes panelEstudiantes -> panelEstudiantes.buscarYLlenar(texto);
            case PanelLibros panelLibros -> panelLibros.buscarYLlenar(texto);
            default -> JOptionPane.showMessageDialog(this, "Esta pantalla no tiene búsqueda activa.");
        }
    }
   
//Estilo visuales 
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(170, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
///efecto hover pasar el mouse
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_BOTON_HOVER);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(255,255,255,100), 1),
                    new EmptyBorder(0,0,0,0) 
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_BOTON);
                btn.setBorder(null); 
            }
        });
        return btn;
    }


    private void styleSearchField(JTextField field) {
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(255, 255, 255, 245)); 
        field.setForeground(new Color(0x3E2723)); 
        field.setCaretColor(new Color(0x3E2723));
        
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0x5D4037), 1), 
            new EmptyBorder(5, 10, 5, 10) 
        ));
    }

    //  clase interna Panel con Degradado
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, COLOR_FONDO_INICIO, 
                                                 0, getHeight(), COLOR_FONDO_FIN);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
 
}