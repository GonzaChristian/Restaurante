package Views;
import javax.swing.*;
import java.awt.*;

public class PanelGenerarPedido extends JPanel {
    public JComboBox<String> cmbMesas;
    private JTable tblPedidoActual;
    private JLabel lblSubtotal, lblIGV, lblTotal;
    
    
     public void setMesaSeleccionada(int numeroMesa) {
        if (cmbMesas != null && numeroMesa > 0 && numeroMesa <= cmbMesas.getItemCount()) {
            cmbMesas.setSelectedIndex(numeroMesa - 1);
        }
    }
    
    
   
    
}