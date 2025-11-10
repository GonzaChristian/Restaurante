package Views;

import dao.MesaDAO;
import models.Mesa;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Panel que muestra las 20 mesas del restaurante
 * REFACTORIZADO: Ahora usa MesaDAO en lugar de consultas directas
 */
public class PanelMesas extends JPanel {
    
    // Interfaz para notificar cuando se selecciona una mesa
    public interface OnMesaSeleccionadaListener {
        void onMesaSeleccionada(int numeroMesa);
    }

    private OnMesaSeleccionadaListener listener;
    private JPanel pnlContenedorMesas;
    private MesaDAO mesaDAO;

    public PanelMesas() {
        this.mesaDAO = new MesaDAO();
        initComponents();
        cargarMesas();
    }

    public void setOnMesaSeleccionadaListener(OnMesaSeleccionadaListener listener) {
        this.listener = listener;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        pnlContenedorMesas = new JPanel(new GridLayout(4, 5, 10, 10)); // 4 filas x 5 columnas
        pnlContenedorMesas.setPreferredSize(new Dimension(1000, 720));
        pnlContenedorMesas.setBackground(new Color(255, 240, 228));
        add(pnlContenedorMesas, BorderLayout.CENTER);
    }

    /**
     * Cargar mesas desde la base de datos usando MesaDAO
     */
    public void cargarMesas() {
        pnlContenedorMesas.removeAll(); // Limpiar paneles anteriores
        
        List<Mesa> mesas = mesaDAO.obtenerTodas();
        
        if (mesas.isEmpty()) {
            JLabel lblError = new JLabel("No se pudieron cargar las mesas");
            lblError.setHorizontalAlignment(SwingConstants.CENTER);
            pnlContenedorMesas.add(lblError);
        } else {
            for (Mesa mesa : mesas) {
                pnlContenedorMesas.add(crearPanelMesa(mesa));
            }
        }
        
        pnlContenedorMesas.revalidate();
        pnlContenedorMesas.repaint();
    }

    /**
     * Crear el panel visual para una mesa individual
     */
    private JPanel crearPanelMesa(Mesa mesa) {
        JPanel panelMesa = new JPanel(new BorderLayout());
        panelMesa.setPreferredSize(new Dimension(190, 170));
        panelMesa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelMesa.setOpaque(true);

        // Color según el estado
        Color colorFondo;
        if (mesa.estaDisponible()) {
            colorFondo = new Color(153, 255, 102); // Verde claro
        } else {
            colorFondo = new Color(255, 153, 153); // Rojo claro
        }
        panelMesa.setBackground(colorFondo);

        // Imagen de la mesa
        JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Images/mesa_restaurante.png"));
            Image img = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImagen.setText("🪑"); // Emoji alternativo si no hay imagen
            lblImagen.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        }

        // Texto con número de mesa
        JLabel lblTexto = new JLabel("MESA " + mesa.getNumeroMesa(), SwingConstants.CENTER);
        lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTexto.setForeground(new Color(51, 51, 51));

        // Label de estado
        JLabel lblEstado = new JLabel(mesa.getEstado(), SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(51, 51, 51));

        // Panel para texto y estado
        JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
        pnlInfo.setOpaque(false);
        pnlInfo.add(lblTexto);
        pnlInfo.add(lblEstado);

        panelMesa.add(lblImagen, BorderLayout.CENTER);
        panelMesa.add(pnlInfo, BorderLayout.SOUTH);

        // Efectos hover y clic
        panelMesa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.onMesaSeleccionada(mesa.getNumeroMesa());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panelMesa.setBorder(new LineBorder(new Color(204, 102, 0), 3, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panelMesa.setBorder(null);
            }
        });

        return panelMesa;
    }

    /**
     * Actualizar el color de una mesa específica según su nuevo estado
     */
    public void actualizarEstadoMesa(int numeroMesa, String nuevoEstado) {
        // Actualizar en la base de datos
        mesaDAO.actualizarEstado(numeroMesa, nuevoEstado);
        
        // Recargar las mesas visualmente
        cargarMesas();
    }

    /**
     * Refrescar la vista de todas las mesas
     */
    public void refrescar() {
        cargarMesas();
    }
}