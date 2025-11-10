package controllers;

import dao.MesaDAO;
import dao.PedidoDAO;
import models.Pedido;
import models.DetallePedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador para gestionar el panel de Administrar Pedidos
 */
public class AdministrarPedidosController {
    
    // DAOs
    private PedidoDAO pedidoDAO;
    private MesaDAO mesaDAO;
    
    // Componentes de la vista
    private JTable tblAdministrarPedidos;
    private JTextField txtBuscar;
    private DefaultTableModel modeloTabla;
    
    // Referencia al panel de mesas
    private Object panelMesasRef;
    
    public AdministrarPedidosController() {
        this.pedidoDAO = new PedidoDAO();
        this.mesaDAO = new MesaDAO();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblAdministrarPedidos,
        JTextField txtBuscar
    ) {
        this.tblAdministrarPedidos = tblAdministrarPedidos;
        this.txtBuscar = txtBuscar;
        
        configurarTabla();
        cargarTodosPedidos();
    }
    
    /**
     * Establecer referencia al panel de mesas
     */
    public void setPanelMesasReferencia(Object panelMesas) {
        this.panelMesasRef = panelMesas;
    }
    
    /**
     * Configurar modelo de tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Mesa", "Total", "Fecha", "Hora", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAdministrarPedidos.setModel(modeloTabla);
        
        // Ajustar anchos de columnas
        tblAdministrarPedidos.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tblAdministrarPedidos.getColumnModel().getColumn(1).setPreferredWidth(70);  // Mesa
        tblAdministrarPedidos.getColumnModel().getColumn(2).setPreferredWidth(100); // Total
        tblAdministrarPedidos.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        tblAdministrarPedidos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Hora
        tblAdministrarPedidos.getColumnModel().getColumn(5).setPreferredWidth(120); // Estado
    }
    
    /**
     * Cargar todos los pedidos
     */
    public void cargarTodosPedidos() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.obtenerTodos();
        
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.getIdPedido(),
                "Mesa " + p.getNumeroMesa(),
                String.format("S/. %.2f", p.getTotal()),
                p.getFecha().format(formatoFecha),
                p.getHora().format(formatoHora),
                p.getEstado()
            });
        }
    }
    
    /**
     * Buscar pedidos por número de mesa
     */
    public void buscarPedidos(JComponent parent) {
        String textoBusqueda = txtBuscar.getText().trim();
        
        if (textoBusqueda.isEmpty()) {
            cargarTodosPedidos();
            return;
        }
        
        try {
            int numeroMesa = Integer.parseInt(textoBusqueda);
            
            modeloTabla.setRowCount(0);
            List<Pedido> pedidos = pedidoDAO.buscarPorMesa(numeroMesa);
            
            if (pedidos.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "No se encontraron pedidos para la mesa " + numeroMesa,
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            for (Pedido p : pedidos) {
                modeloTabla.addRow(new Object[]{
                    p.getIdPedido(),
                    "Mesa " + p.getNumeroMesa(),
                    String.format("S/. %.2f", p.getTotal()),
                    p.getFecha().format(formatoFecha),
                    p.getHora().format(formatoHora),
                    p.getEstado()
                });
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent,
                "Por favor ingrese un número de mesa válido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ver detalle del pedido seleccionado
     */
    public void verDetallePedido(JComponent parent) {
        int filaSeleccionada = tblAdministrarPedidos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un pedido",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPedido = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
        
        if (pedido == null) {
            JOptionPane.showMessageDialog(parent,
                "No se pudo cargar el pedido",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Construir mensaje con detalles
        StringBuilder detalles = new StringBuilder();
        detalles.append("═══════════════════════════════════\n");
        detalles.append("        DETALLE DEL PEDIDO\n");
        detalles.append("═══════════════════════════════════\n\n");
        detalles.append("ID Pedido: ").append(pedido.getIdPedido()).append("\n");
        detalles.append("Mesa: ").append(pedido.getNumeroMesa()).append("\n");
        detalles.append("Fecha: ").append(pedido.getFecha()).append("\n");
        detalles.append("Hora: ").append(pedido.getHora()).append("\n");
        detalles.append("Estado: ").append(pedido.getEstado()).append("\n\n");
        detalles.append("───────────────────────────────────\n");
        detalles.append("PLATILLOS:\n");
        detalles.append("───────────────────────────────────\n\n");
        
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalles.append(String.format("• %s\n", detalle.getNombrePlatillo()));
            detalles.append(String.format("  Cantidad: %d\n", detalle.getCantidad()));
            detalles.append(String.format("  Precio Unit.: S/. %.2f\n", detalle.getPrecioUnitario()));
            detalles.append(String.format("  Subtotal: S/. %.2f\n\n", detalle.getSubtotal()));
        }
        
        detalles.append("───────────────────────────────────\n");
        detalles.append(String.format("Subtotal:  S/. %.2f\n", pedido.getSubtotal()));
        detalles.append(String.format("IGV (18%%): S/. %.2f\n", pedido.getIgv()));
        detalles.append(String.format("TOTAL:     S/. %.2f\n", pedido.getTotal()));
        detalles.append("═══════════════════════════════════\n");
        
        // Mostrar en un JTextArea dentro de un JScrollPane
        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 500));
        
        JOptionPane.showMessageDialog(parent, scrollPane,
            "Detalle del Pedido #" + idPedido,
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Marcar pedido como completado
     */
    public void marcarCompletado(JComponent parent) {
        int filaSeleccionada = tblAdministrarPedidos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un pedido",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPedido = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
        
        if ("Completado".equals(estadoActual)) {
            JOptionPane.showMessageDialog(parent,
                "Este pedido ya está completado",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Confirmar acción
        int confirmacion = JOptionPane.showConfirmDialog(parent,
            "¿Está seguro de marcar este pedido como completado?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Obtener pedido para saber la mesa
        Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
        
        if (pedidoDAO.marcarCompletado(idPedido)) {
            // Actualizar estado de la mesa
            mesaDAO.marcarDisponible(pedido.getNumeroMesa());
            
            // Actualizar tabla
            modeloTabla.setValueAt("Completado", filaSeleccionada, 5);
            
            // Refrescar panel de mesas
            refrescarPanelMesas();
            
            JOptionPane.showMessageDialog(parent,
                "Pedido marcado como completado\nMesa " + pedido.getNumeroMesa() + " ahora está disponible",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al actualizar el pedido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Refrescar panel de mesas usando reflexión
     */
    private void refrescarPanelMesas() {
        if (panelMesasRef != null) {
            try {
                panelMesasRef.getClass()
                    .getMethod("refrescar")
                    .invoke(panelMesasRef);
            } catch (Exception e) {
                System.err.println("No se pudo refrescar panel de mesas: " + e.getMessage());
            }
        }
    }
    
    /**
     * Actualizar lista de pedidos
     */
    public void actualizar() {
        cargarTodosPedidos();
    }
}