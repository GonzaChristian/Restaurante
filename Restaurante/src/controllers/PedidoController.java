package controllers;

import dao.MesaDAO;
import dao.PlatilloDAO;
import dao.PedidoDAO;
import models.Mesa;
import models.Pedido;
import models.DetallePedido;
import models.Platillo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar la lógica de pedidos
 * Se integra con el diseño existente de SystemView
 */
public class PedidoController {
    
    // DAOs
    private MesaDAO mesaDAO;
    private PlatilloDAO platilloDAO;
    private PedidoDAO pedidoDAO;
    
    // Referencias a componentes de la vista
    private JComboBox<String> cmbMesas;
    private JTable tblPedidoActual;
    private JTable tblMenuDelDia;
    private JLabel lblSubtotal;
    private JLabel lblIGV;
    private JLabel lblTotal;
    private JTextField txtCantidad;
    
    // Modelos de tablas
    private DefaultTableModel modeloPedidoActual;
    private DefaultTableModel modeloMenuDelDia;
    
    // Gestión de pedidos temporales por mesa
    private Map<Integer, Pedido> pedidosPorMesa;
    private int mesaActual;
    
    // Referencia al panel de mesas para actualizar colores
    private Object panelMesasRef;
    
    public PedidoController() {
        this.mesaDAO = new MesaDAO();
        this.platilloDAO = new PlatilloDAO();
        this.pedidoDAO = new PedidoDAO();
        this.pedidosPorMesa = new HashMap<>();
        this.mesaActual = 1;
    }
    
    /**
     * Inicializar componentes desde SystemView
     */
    public void inicializarComponentes(
        JComboBox<String> cmbMesas,
        JTable tblPedidoActual,
        JTable tblMenuDelDia,
        JLabel lblSubtotal,
        JLabel lblIGV,
        JLabel lblTotal,
        JTextField txtCantidad
    ) {
        this.cmbMesas = cmbMesas;
        this.tblPedidoActual = tblPedidoActual;
        this.tblMenuDelDia = tblMenuDelDia;
        this.lblSubtotal = lblSubtotal;
        this.lblIGV = lblIGV;
        this.lblTotal = lblTotal;
        this.txtCantidad = txtCantidad;
        
        configurarTablas();
        cargarPlatillosEnMenu();
    }
    
    /**
     * Establecer referencia al panel de mesas
     */
    public void setPanelMesasReferencia(Object panelMesas) {
        this.panelMesasRef = panelMesas;
    }
    
    /**
     * Configurar modelos de tablas
     */
    private void configurarTablas() {
        // Configurar tabla Pedido Actual
        modeloPedidoActual = new DefaultTableModel(
            new Object[]{"Nombre", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPedidoActual.setModel(modeloPedidoActual);
        
        // Configurar tabla Menú del Día
        modeloMenuDelDia = new DefaultTableModel(
            new Object[]{"Nombre", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMenuDelDia.setModel(modeloMenuDelDia);
    }
    
    /**
     * Cargar platillos desde la BD al Menú del Día
     */
    public void cargarPlatillosEnMenu() {
        modeloMenuDelDia.setRowCount(0);
        List<Platillo> platillos = platilloDAO.obtenerTodos();
        
        for (Platillo p : platillos) {
            modeloMenuDelDia.addRow(new Object[]{
                p.getNombre(),
                String.format("S/. %.2f", p.getPrecio())
            });
        }
    }
    
    /**
     * Actualizar menú completo (llamado desde PlatillosController)
     */
    public void actualizarMenuCompleto(List<Platillo> platillosActualizados) {
        modeloMenuDelDia.setRowCount(0);
        
        for (Platillo p : platillosActualizados) {
            modeloMenuDelDia.addRow(new Object[]{
                p.getNombre(),
                String.format("S/. %.2f", p.getPrecio())
            });
        }
    }
    
    /**
     * Cambiar de mesa y cargar su pedido si existe
     */
    public void cambiarMesa() {
        // Guardar pedido actual antes de cambiar
        if (mesaActual > 0) {
            guardarPedidoTemporal();
        }
        
        // Cambiar a nueva mesa
        mesaActual = Integer.parseInt((String) cmbMesas.getSelectedItem());
        
        // Cargar pedido guardado de esta mesa
        cargarPedidoMesa(mesaActual);
    }
    
    /**
     * Guardar pedido temporal en memoria
     */
    private void guardarPedidoTemporal() {
        if (modeloPedidoActual.getRowCount() > 0) {
            Pedido pedido = obtenerPedidoDesdeTabla();
            pedidosPorMesa.put(mesaActual, pedido);
        } else {
            pedidosPorMesa.remove(mesaActual);
        }
    }
    
    /**
     * Cargar pedido de una mesa específica
     */
    public void cargarPedidoMesa(int numeroMesa) {
        modeloPedidoActual.setRowCount(0);
        
        // Primero buscar en pedidos temporales
        if (pedidosPorMesa.containsKey(numeroMesa)) {
            Pedido pedido = pedidosPorMesa.get(numeroMesa);
            cargarPedidoEnTabla(pedido);
        } else {
            // Buscar en BD si hay pedido activo
            Mesa mesa = mesaDAO.obtenerPorNumero(numeroMesa);
            if (mesa != null) {
                Pedido pedidoActivo = pedidoDAO.obtenerPedidoActivoPorMesa(mesa.getIdMesa());
                if (pedidoActivo != null) {
                    cargarPedidoEnTabla(pedidoActivo);
                }
            }
        }
        
        actualizarTotales();
    }
    
    /**
     * Cargar pedido en la tabla
     */
    private void cargarPedidoEnTabla(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            modeloPedidoActual.addRow(new Object[]{
                detalle.getNombrePlatillo(),
                detalle.getCantidad(),
                String.format("S/. %.2f", detalle.getSubtotal())
            });
        }
    }
    
    /**
     * Obtener el pedido desde la tabla actual
     */
    private Pedido obtenerPedidoDesdeTabla() {
        Mesa mesa = mesaDAO.obtenerPorNumero(mesaActual);
        Pedido pedido = new Pedido(mesa.getIdMesa());
        pedido.setNumeroMesa(mesaActual);
        
        List<Platillo> platillos = platilloDAO.obtenerTodos();
        
        for (int i = 0; i < modeloPedidoActual.getRowCount(); i++) {
            String nombrePlatillo = (String) modeloPedidoActual.getValueAt(i, 0);
            int cantidad = (int) modeloPedidoActual.getValueAt(i, 1);
            
            // Buscar platillo
            for (Platillo p : platillos) {
                if (p.getNombre().equals(nombrePlatillo)) {
                    DetallePedido detalle = new DetallePedido(p, cantidad);
                    pedido.agregarDetalle(detalle);
                    break;
                }
            }
        }
        
        return pedido;
    }
    
    /**
     * Agregar platillo al pedido actual
     */
    public void agregarPlatillo(JComponent parent) {
        int filaSeleccionada = tblMenuDelDia.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(parent, 
                "Por favor seleccione un platillo del menú", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombrePlatillo = (String) modeloMenuDelDia.getValueAt(filaSeleccionada, 0);
        String precioStr = (String) modeloMenuDelDia.getValueAt(filaSeleccionada, 1);
        double precio = Double.parseDouble(precioStr.replace("S/. ", ""));
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, 
                "Por favor ingrese una cantidad válida (mayor a 0)", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double subtotal = precio * cantidad;
        
        // Agregar a la tabla
        modeloPedidoActual.addRow(new Object[]{
            nombrePlatillo,
            cantidad,
            String.format("S/. %.2f", subtotal)
        });
        
        actualizarTotales();
        txtCantidad.setText("1"); // Resetear cantidad
    }
    
    /**
     * Actualizar labels de totales
     */
    private void actualizarTotales() {
        double subtotal = 0.0;
        
        for (int i = 0; i < modeloPedidoActual.getRowCount(); i++) {
            String subtotalStr = (String) modeloPedidoActual.getValueAt(i, 2);
            subtotal += Double.parseDouble(subtotalStr.replace("S/. ", ""));
        }
        
        double igv = subtotal * 0.18;
        double total = subtotal + igv;
        
        lblSubtotal.setText(String.format("S/. %.2f", subtotal));
        lblIGV.setText(String.format("S/. %.2f", igv));
        lblTotal.setText(String.format("S/. %.2f", total));
    }
    
    /**
     * Marcar pedido como "En Proceso" y guardarlo en BD
     */
    public void marcarPedidoEnProceso(JComponent parent) {
        if (modeloPedidoActual.getRowCount() == 0) {
            JOptionPane.showMessageDialog(parent, 
                "No hay platillos en el pedido", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Pedido pedido = obtenerPedidoDesdeTabla();
        
        if (pedidoDAO.insertarPedido(pedido)) {
            mesaDAO.marcarEnProceso(mesaActual);
            pedidosPorMesa.remove(mesaActual); // Limpiar temporal
            
            // Actualizar panel de mesas si existe referencia
            refrescarPanelMesas();
            
            JOptionPane.showMessageDialog(parent, 
                "Pedido registrado exitosamente en la Mesa " + mesaActual, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, 
                "Error al registrar pedido", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Marcar pedido como completado
     */
    public void marcarPedidoCompletado(JComponent parent) {
        Mesa mesa = mesaDAO.obtenerPorNumero(mesaActual);
        Pedido pedidoActivo = pedidoDAO.obtenerPedidoActivoPorMesa(mesa.getIdMesa());
        
        if (pedidoActivo == null) {
            JOptionPane.showMessageDialog(parent, 
                "No hay pedido activo en esta mesa", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (pedidoDAO.marcarCompletado(pedidoActivo.getIdPedido())) {
            mesaDAO.marcarDisponible(mesaActual);
            modeloPedidoActual.setRowCount(0);
            actualizarTotales();
            
            // Actualizar panel de mesas
            refrescarPanelMesas();
            
            JOptionPane.showMessageDialog(parent, 
                "Pedido completado - Mesa " + mesaActual + " disponible", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Refrescar panel de mesas usando reflexión
     */
    private void refrescarPanelMesas() {
        if (panelMesasRef != null) {
            try {
                // Llamar al método refrescar() del panelMesas
                panelMesasRef.getClass()
                    .getMethod("refrescar")
                    .invoke(panelMesasRef);
            } catch (Exception e) {
                System.err.println("No se pudo refrescar panel de mesas: " + e.getMessage());
            }
        }
    }
    
    /**
     * Establecer mesa seleccionada
     */
    public void setMesaSeleccionada(int numeroMesa) {
        if (cmbMesas != null && numeroMesa > 0 && numeroMesa <= cmbMesas.getItemCount()) {
            cmbMesas.setSelectedIndex(numeroMesa - 1);
        }
    }
    
    /**
 * Imprimir voucher del pedido actual
 */
public void imprimirVoucherPedidoActual(JComponent parent) {
    if (modeloPedidoActual.getRowCount() == 0) {
        JOptionPane.showMessageDialog(parent,
            "No hay pedido para imprimir",
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Pedido pedido = obtenerPedidoDesdeTabla();
    utils.ImpresoraPedidos.vistaPrevia(pedido, parent);
}

/**
 * Imprimir comanda para la cocina
 */
public void imprimirComandaCocina(JComponent parent) {
    if (modeloPedidoActual.getRowCount() == 0) {
        JOptionPane.showMessageDialog(parent,
            "No hay pedido para imprimir",
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Pedido pedido = obtenerPedidoDesdeTabla();
    
    int opcion = JOptionPane.showConfirmDialog(parent,
        "¿Desea imprimir la comanda para la cocina?",
        "Imprimir Comanda",
        JOptionPane.YES_NO_OPTION);
    
    if (opcion == JOptionPane.YES_OPTION) {
        utils.ImpresoraPedidos.imprimirComanda(pedido, parent);
    }
}

/**
 * Marcar pedido como "En Proceso" e imprimir comanda
 */
public void marcarPedidoEnProcesoConImpresion(JComponent parent) {
    if (modeloPedidoActual.getRowCount() == 0) {
        JOptionPane.showMessageDialog(parent, 
            "No hay platillos en el pedido", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Pedido pedido = obtenerPedidoDesdeTabla();
    
    // Preguntar si desea imprimir
    Object[] opciones = {"Guardar e Imprimir Comanda", "Solo Guardar", "Cancelar"};
    int opcion = JOptionPane.showOptionDialog(parent,
        "¿Desea imprimir la comanda para la cocina?",
        "Registrar Pedido",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opciones,
        opciones[0]);
    
    if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) {
        return; // Cancelar
    }
    
    // Guardar pedido
    if (pedidoDAO.insertarPedido(pedido)) {
        mesaDAO.marcarEnProceso(mesaActual);
        pedidosPorMesa.remove(mesaActual);
        
        // Actualizar panel de mesas
        refrescarPanelMesas();
        
        // Imprimir comanda si se seleccionó
        if (opcion == 0) {
            utils.ImpresoraPedidos.imprimirComanda(pedido, parent);
        }
        
        JOptionPane.showMessageDialog(parent, 
            "Pedido registrado exitosamente en la Mesa " + mesaActual, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(parent, 
            "Error al registrar pedido", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Marcar pedido como completado e imprimir voucher
 */
public void marcarPedidoCompletadoConImpresion(JComponent parent) {
    Mesa mesa = mesaDAO.obtenerPorNumero(mesaActual);
    Pedido pedidoActivo = pedidoDAO.obtenerPedidoActivoPorMesa(mesa.getIdMesa());
    
    if (pedidoActivo == null) {
        JOptionPane.showMessageDialog(parent, 
            "No hay pedido activo en esta mesa", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener pedido completo con detalles
    Pedido pedidoCompleto = pedidoDAO.obtenerPorId(pedidoActivo.getIdPedido());
    
    // Preguntar si desea imprimir voucher
    Object[] opciones = {"Completar e Imprimir", "Solo Completar", "Cancelar"};
    int opcion = JOptionPane.showOptionDialog(parent,
        "Pedido #" + pedidoCompleto.getIdPedido() + 
        " - Total: S/ " + String.format("%.2f", pedidoCompleto.getTotal()) +
        "\n¿Desea imprimir el voucher?",
        "Completar Pedido",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opciones,
        opciones[0]);
    
    if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) {
        return; // Cancelar
    }
    
    // Completar pedido
    if (pedidoDAO.marcarCompletado(pedidoCompleto.getIdPedido())) {
        mesaDAO.marcarDisponible(mesaActual);
        modeloPedidoActual.setRowCount(0);
        actualizarTotales();
        
        // Actualizar panel de mesas
        refrescarPanelMesas();
        
        // Imprimir voucher si se seleccionó
        if (opcion == 0) {
            utils.ImpresoraPedidos.vistaPrevia(pedidoCompleto, parent);
        }
        
        JOptionPane.showMessageDialog(parent, 
            "Pedido completado - Mesa " + mesaActual + " disponible", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}

/**
 * Reimprimir voucher de un pedido existente
 */
public void reimprimirVoucher(int idPedido, JComponent parent) {
    Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
    
    if (pedido == null) {
        JOptionPane.showMessageDialog(parent,
            "No se pudo cargar el pedido",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    utils.ImpresoraPedidos.vistaPrevia(pedido, parent);
}
    
    
    
    
}