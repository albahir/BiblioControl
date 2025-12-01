package InterfacesBiblioteca;

import Entidadesbiblioteca.Admin;
import AccesoDatosBiblioteca.BaseArchivos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen extends JFrame {

    // --- PALETA DE COLORES ---
    private final Color COLOR_FONDO_INICIO = new Color(0x3E2723);
    private final Color COLOR_FONDO_FIN = new Color(0xe7e9bb);
    
    // Color de la tarjeta central (Negro transparente)
    private final Color COLOR_TARJETA = new Color(0, 0, 0, 110);
    
    // Color del botón
    private final Color COLOR_BOTON = new Color(0x5D4037);
    private final Color COLOR_BOTON_HOVER = new Color(0x795548);
    
    // Colores de texto
    private final Color COLOR_TEXTO_TITULO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO_LABEL = new Color(245, 245, 245);

    // Variables de interfaz
    private JTextField txtUser;
    private JPasswordField txtPass;
    
    public LoginScreen() {
        setTitle("Bibliotecario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar en pantalla

        // 1. Panel principal con fondo degradado
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout()); // Para centrar la tarjeta
        setContentPane(mainPanel);

        // 2. Crear la tarjeta de login
        JPanel loginCard = createLoginCard();
        mainPanel.add(loginCard);
    }

    private JPanel createLoginCard() {
        // Tarjeta con bordes rectos (Radio 0)
        RoundedPanel card = new RoundedPanel(0, COLOR_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 70, 80, 70)); 

        // --- Título: Bibliotecario ---
        JLabel lblTitle = new JLabel("BIBLIOCONTROL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(COLOR_TEXTO_TITULO);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Label y Campo: Usuario ---
        JLabel lblUser = new JLabel("USUARIO");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUser.setForeground(COLOR_TEXTO_LABEL);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT); // Mejor a la izquierda
        
        txtUser = new JTextField(15);
        styleTextField(txtUser);

        // --- Label y Campo: Contraseña ---
        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPass.setForeground(COLOR_TEXTO_LABEL);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPass = new JPasswordField(15);
        styleTextField(txtPass);

        // --- Botón: Iniciar Sesión ---
        JButton btnLogin = new JButton("INICIAR SESIÓN");
        styleButton(btnLogin); // Usamos el método de estilo para el botón
        
        // --- LÓGICA DEL LOGIN ---
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarCredenciales();
            }
        });

        // --- Añadir componentes con espaciado ---
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(40)); 
        
        // Panel auxiliar para alinear inputs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false); 
        
        formPanel.add(lblUser);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(txtUser);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblPass);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(txtPass);
        
        card.add(formPanel);
        card.add(Box.createVerticalStrut(35)); 
        card.add(btnLogin);

        return card;
    }

    // --- MÉTODO DE VALIDACIÓN SIMPLIFICADO ---
    private void verificarCredenciales() {
        String usuarioIngresado = txtUser.getText();
        String passwordIngresado = new String(txtPass.getPassword());
        
        boolean encontrado = false;

        // 1. Verificar en la lista de administradores si existe
        if (BaseArchivos.listaAdmins != null) {
            for (Admin admin : BaseArchivos.listaAdmins) {
                if (admin.getUsuario().equals(usuarioIngresado) && 
                    admin.getPassword().equals(passwordIngresado)) {
                    encontrado = true;
                    break;
                }
            }
        }

        // 2. Respaldo: Verificar usuario por defecto (admin/123) si no se encontró en la lista
        if (!encontrado && usuarioIngresado.equals("admin") && passwordIngresado.equals("123")) {
            encontrado = true;
        }

        if (encontrado) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido al Sistema!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
            
            new MenuPrincipal().setVisible(true);
             this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para dar estilo a las cajas de texto (VERSIÓN FINAL)
    private void styleTextField(JTextField field) {
        // 1. Más alto (40px)
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // 2. Fuente más moderna
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // 3. Colores explícitos
        field.setBackground(Color.WHITE);
        field.setForeground(Color.DARK_GRAY);
        field.setCaretColor(Color.GRAY); 
        
        // 4. Borde invisible con relleno interno (Padding)
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255), 0), 
            new EmptyBorder(5, 10, 5, 10) 
        ));
    }

    // Método para estilo del botón
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COLOR_TEXTO_TITULO); 
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Un poco más alto
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_BOTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_BOTON);
            }
        });
    }

    // --- CLASE INTERNA: Fondo degradado ---
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

    // --- CLASE INTERNA: Tarjeta redondeada ---
    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
            catch (Exception ignored) {}
            new BaseArchivos(); 
            new LoginScreen().setVisible(true);
        });
    }
}