
package Views;

import conexion.ConexionSQL;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PanelMesas extends JPanel {
    
    // INTERFAZ y campo para el listener
    public interface OnMesaSeleccionadaListener {
        void onMesaSeleccionada(int numeroMesa);
    }

    private OnMesaSeleccionadaListener listener;

    public void setOnMesaSeleccionadaListener(OnMesaSeleccionadaListener listener) {
        this.listener = listener;
    }
    
    
    

    private JPanel pnlContenedorMesas;

    public PanelMesas() {
        initComponents();
        cargarMesasDesdeBD();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        pnlContenedorMesas = new JPanel(new GridLayout(4, 5, 0, 0)); // sin separación
        pnlContenedorMesas.setPreferredSize(new Dimension(1000, 720));
        pnlContenedorMesas.setBackground(Color.WHITE);
        add(pnlContenedorMesas, BorderLayout.CENTER);
    }

    private void cargarMesasDesdeBD() {
        Connection con = ConexionSQL.conectar();

        if (con == null) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos");
            return;
        }

        try {
            String sql = "SELECT numero_mesa, estado FROM Mesas ORDER BY numero_mesa";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int numero = rs.getInt("numero_mesa");
                String estado = rs.getString("estado");

                // Panel individual
                JPanel panelMesa = new JPanel(new BorderLayout());
                panelMesa.setPreferredSize(new Dimension(200, 180));
                panelMesa.setCursor(new Cursor(Cursor.HAND_CURSOR));
                panelMesa.setOpaque(true);

                // Color por estado
                if (estado.equalsIgnoreCase("Disponible")) {
                    panelMesa.setBackground(new Color(153,255,102)); // verde claro
                } else {
                    panelMesa.setBackground(new Color(255, 153, 153)); // rojo claro
                }

                // Imagen centrada
                JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
                ImageIcon icono = new ImageIcon(getClass().getResource("/Images/mesa_restaurante.png"));
                Image img = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));

                // Texto debajo
                JLabel lblTexto = new JLabel("MESA " + numero, SwingConstants.CENTER);
                lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 24));
                lblTexto.setForeground(Color.DARK_GRAY);

                panelMesa.add(lblImagen, BorderLayout.CENTER);
                panelMesa.add(lblTexto, BorderLayout.SOUTH);

                // Efecto de clic
                panelMesa.addMouseListener(new MouseAdapter() {
                    @Override
                public void mouseClicked(MouseEvent e) {
                    // Si hay listener registrado, notificamos la selección
                    if (listener != null) {
                        listener.onMesaSeleccionada(numero);
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    panelMesa.setBorder(new LineBorder(Color.BLUE, 2, true));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    panelMesa.setBorder(null);
                }
                });

                pnlContenedorMesas.add(panelMesa);
            }

            con.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar mesas: " + e.getMessage());
        }
    }
    

    
    
}


