package controllers;

import dao.PlatilloDAO;
import models.Platillo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar platillos y el menú del día
 */
public class PlatillosController {
    
    // DAOs
    private PlatilloDAO platilloDAO;
    
    // Componentes de la vista
    private JTable tblMenuDia;
    private DefaultTableModel modeloMenuDia;
    
    // Lista de platillos seleccionados para el menú del día
    private List<Platillo> platillosEnMenu;
    
    // Referencia al PedidoController para actualizar el menú
    private Object pedidoControllerRef;
    
    public PlatillosController() {
        this.platilloDAO = new PlatilloDAO();
        this.platillosEnMenu = new ArrayList<>();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(JTable tblMenuDia) {
        this.tblMenuDia = tblMenuDia;
        configurarTabla();
        cargarPlatillosEnMenu();
    }
    
    /**
     * Establecer referencia al PedidoController
     */
    public void setPedidoControllerReferencia(Object pedidoController) {
        this.pedidoControllerRef = pedidoController;
    }
    
    /**
     * Configurar tabla del menú del día
     */
    private void configurarTabla() {
        modeloMenuDia = new DefaultTableModel(
            new Object[]{"Platillo", "Categoría", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMenuDia.setModel(modeloMenuDia);
        
        // Ajustar anchos
        tblMenuDia.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblMenuDia.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblMenuDia.getColumnModel().getColumn(2).setPreferredWidth(80);
    }
    
    /**
     * Cargar platillos en el menú del día (inicialmente todos)
     */
    public void cargarPlatillosEnMenu() {
        modeloMenuDia.setRowCount(0);
        platillosEnMenu = platilloDAO.obtenerTodos();
        
        for (Platillo p : platillosEnMenu) {
            modeloMenuDia.addRow(new Object[]{
                p.getNombre(),
                p.getNombreCategoria(),
                String.format("S/. %.2f", p.getPrecio())
            });
        }
        
        // Actualizar también en el panel de generar pedido
        actualizarMenuEnPedidos();
    }
    
    /**
     * Agregar platillo al menú del día desde un panel de platillo
     */
    public void agregarPlatilloAMenu(int idPlatillo, JComponent parent) {
        Platillo platillo = platilloDAO.obtenerPorId(idPlatillo);
        
        if (platillo == null) {
            JOptionPane.showMessageDialog(parent,
                "Error al cargar el platillo",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si ya está en el menú
        for (Platillo p : platillosEnMenu) {
            if (p.getIdPlatillo() == idPlatillo) {
                JOptionPane.showMessageDialog(parent,
                    "Este platillo ya está en el menú del día",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Agregar a la lista y tabla
        platillosEnMenu.add(platillo);
        modeloMenuDia.addRow(new Object[]{
            platillo.getNombre(),
            platillo.getNombreCategoria(),
            String.format("S/. %.2f", platillo.getPrecio())
        });
        
        actualizarMenuEnPedidos();
        
        JOptionPane.showMessageDialog(parent,
            platillo.getNombre() + " agregado al menú del día",
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Quitar platillo del menú del día
     */
    public void quitarPlatilloDeMenu(int idPlatillo, JComponent parent) {
        boolean encontrado = false;
        
        for (int i = 0; i < platillosEnMenu.size(); i++) {
            if (platillosEnMenu.get(i).getIdPlatillo() == idPlatillo) {
                platillosEnMenu.remove(i);
                modeloMenuDia.removeRow(i);
                encontrado = true;
                break;
            }
        }
        
        if (encontrado) {
            actualizarMenuEnPedidos();
            JOptionPane.showMessageDialog(parent,
                "Platillo quitado del menú del día",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent,
                "Este platillo no está en el menú del día",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Actualizar el menú en el panel de generar pedidos
     */
    private void actualizarMenuEnPedidos() {
        if (pedidoControllerRef != null) {
            try {
                pedidoControllerRef.getClass()
                    .getMethod("cargarPlatillosEnMenu")
                    .invoke(pedidoControllerRef);
            } catch (Exception e) {
                System.err.println("No se pudo actualizar menú en pedidos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Nuevo platillo (placeholder - implementar en siguiente fase)
     */
    public void nuevoPlatillo(JComponent parent) {
        JOptionPane.showMessageDialog(parent,
            "Función de nuevo platillo en desarrollo.\nPor ahora puedes agregar platillos directamente en la BD.",
            "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Modificar platillo (placeholder)
     */
    public void modificarPlatillo(JComponent parent) {
        JOptionPane.showMessageDialog(parent,
            "Función de modificar platillo en desarrollo",
            "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Eliminar platillo (placeholder)
     */
    public void eliminarPlatillo(JComponent parent) {
        JOptionPane.showMessageDialog(parent,
            "Función de eliminar platillo en desarrollo",
            "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}