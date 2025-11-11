package Views;

import models.Platillo;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel personalizado para mostrar un platillo
 */
public class PanelPlatillo extends JPanel {
    
    private Platillo platillo;
    private JLabel lblImagen;
    private JLabel lblNombre;
    private JLabel lblCategoria;
    private JLabel lblPrecio;
    private JButton btnAgregar;
    private JButton btnQuitar;
    
    // Interfaz para notificar selección
    public interface OnPlatilloSeleccionadoListener {
        void onPlatilloSeleccionado(PanelPlatillo panel);
    }
    
    private OnPlatilloSeleccionadoListener seleccionListener;
    
    // Interfaz para acciones de menú
    public interface OnMenuActionListener {
        void onAgregarAlMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar);
        void onQuitarDelMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar);
    }
    
    private OnMenuActionListener menuActionListener;
    
    private boolean seleccionado = false;
    private boolean estaEnMenu = false;
    
    public PanelPlatillo(Platillo platillo) {
        this.platillo = platillo;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setLayout(null);
        setPreferredSize(new Dimension(420, 150));
        setMaximumSize(new Dimension(420, 150));
        setMinimumSize(new Dimension(420, 150));
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        // Imagen del platillo
        lblImagen = new JLabel();
        lblImagen.setBounds(20, 20, 160, 108);
        lblImagen.setBorder(new LineBorder(Color.BLACK, 1));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImagen);
        
        // Label "Nombre:"
        JLabel lblTextoNombre = new JLabel("Nombre:");
        lblTextoNombre.setBounds(210, 20, 70, 20);
        lblTextoNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(lblTextoNombre);
        
        // Label "Categoría:"
        JLabel lblTextoCategoria = new JLabel("Categoría:");
        lblTextoCategoria.setBounds(210, 50, 70, 20);
        lblTextoCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(lblTextoCategoria);
        
        // Label "Precio:"
        JLabel lblTextoPrecio = new JLabel("Precio:");
        lblTextoPrecio.setBounds(210, 80, 70, 20);
        lblTextoPrecio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(lblTextoPrecio);
        
        // Valor Nombre
        lblNombre = new JLabel();
        lblNombre.setBounds(290, 20, 120, 20);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(lblNombre);
        
        // Valor Categoría
        lblCategoria = new JLabel();
        lblCategoria.setBounds(290, 50, 120, 20);
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(lblCategoria);
        
        // Valor Precio
        lblPrecio = new JLabel();
        lblPrecio.setBounds(290, 80, 120, 20);
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(lblPrecio);
        
        // Botón Agregar
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(210, 110, 80, 25);
        btnAgregar.setBackground(new Color(102, 153, 0));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarAlMenu());
        add(btnAgregar);
        
        // Botón Quitar
        btnQuitar = new JButton("Quitar");
        btnQuitar.setBounds(310, 110, 80, 25);
        btnQuitar.setBackground(new Color(204, 0, 0));
        btnQuitar.setForeground(Color.WHITE);
        btnQuitar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnQuitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuitar.setEnabled(false); // Deshabilitado por defecto
        btnQuitar.addActionListener(e -> quitarDelMenu());
        add(btnQuitar);
        
        // Listener para selección del panel
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (seleccionListener != null) {
                    seleccionListener.onPlatilloSeleccionado(PanelPlatillo.this);
                }
            }
        });
    }
    
    private void cargarDatos() {
        lblNombre.setText(platillo.getNombre());
        lblCategoria.setText(platillo.getNombreCategoria());
        lblPrecio.setText(String.format("S/. %.2f", platillo.getPrecio()));
        
        // Cargar imagen
        if (platillo.getImagen() != null && platillo.getImagen().length > 0) {
            try {
                ImageIcon icon = new ImageIcon(platillo.getImagen());
                Image img = icon.getImage().getScaledInstance(160, 108, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblImagen.setText("Sin imagen");
                lblImagen.setIcon(null);
            }
        } else {
            lblImagen.setText("🍽️");
            lblImagen.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            lblImagen.setIcon(null);
        }
    }
    
    private void agregarAlMenu() {
        if (menuActionListener != null) {
            menuActionListener.onAgregarAlMenu(platillo, btnAgregar, btnQuitar);
        }
    }
    
    private void quitarDelMenu() {
        if (menuActionListener != null) {
            menuActionListener.onQuitarDelMenu(platillo, btnAgregar, btnQuitar);
        }
    }
    
    public void marcarComoSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        if (seleccionado) {
            setBorder(new LineBorder(Color.BLUE, 3));
        } else {
            setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        }
    }
    
    public boolean isSeleccionado() {
        return seleccionado;
    }
    
    public Platillo getPlatillo() {
        return platillo;
    }
    
    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
        cargarDatos();
    }
    
    public void setOnPlatilloSeleccionadoListener(OnPlatilloSeleccionadoListener listener) {
        this.seleccionListener = listener;
    }
    
    public void setOnMenuActionListener(OnMenuActionListener listener) {
        this.menuActionListener = listener;
    }
    
    public void setEstaEnMenu(boolean estaEnMenu) {
        this.estaEnMenu = estaEnMenu;
        btnAgregar.setEnabled(!estaEnMenu);
        btnQuitar.setEnabled(estaEnMenu);
    }
    
    public boolean isEstaEnMenu() {
        return estaEnMenu;
    }
}