package controllers;

import dao.InsumoDAO;
import models.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controlador para gestionar el panel de Inventario de Insumos
 */
public class InsumoController {
    
    private InsumoDAO insumoDAO;
    
    // Componentes de la vista
    private JTable tblInsumos;
    private DefaultTableModel modeloTabla;
    private JLabel lblID;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtCategoria;
    private JTextField txtStock;
    private JTextField txtAlmacen;
    private JTextField txtProveedor;
    
    private int idInsumoSeleccionado = -1;
    
    public InsumoController() {
        this.insumoDAO = new InsumoDAO();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblInsumos,
        JLabel lblID,
        JTextField txtCodigo,
        JTextField txtNombre,
        JTextField txtCategoria,
        JTextField txtStock,
        JTextField txtAlmacen,
        JTextField txtProveedor
    ) {
        this.tblInsumos = tblInsumos;
        this.lblID = lblID;
        this.txtCodigo = txtCodigo;
        this.txtNombre = txtNombre;
        this.txtCategoria = txtCategoria;
        this.txtStock = txtStock;
        this.txtAlmacen = txtAlmacen;
        this.txtProveedor = txtProveedor;
        
        configurarTabla();
        cargarInsumos();
        configurarListenerTabla();
    }
    
    /**
     * Configurar tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre", "Categoría", "Stock", "Almacén", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblInsumos.setModel(modeloTabla);
    }
    
    /**
     * Configurar listener de selección de tabla
     */
    private void configurarListenerTabla() {
        tblInsumos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarInsumoSeleccionado();
            }
        });
    }
    
    /**
     * Cargar todos los insumos
     */
    public void cargarInsumos() {
        modeloTabla.setRowCount(0);
        List<Insumo> insumos = insumoDAO.obtenerTodos();
        
        for (Insumo i : insumos) {
            modeloTabla.addRow(new Object[]{
                i.getIdInsumo(),
                i.getCodigo(),
                i.getNombre(),
                i.getNombreCategoria(),
                i.getStock(),
                i.getAlmacen(),
                i.getProveedor()
            });
        }
    }
    
    /**
     * Cargar insumo seleccionado en los campos
     */
    private void cargarInsumoSeleccionado() {
        int filaSeleccionada = tblInsumos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            return;
        }
        
        idInsumoSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        lblID.setText(String.valueOf(idInsumoSeleccionado));
        txtCodigo.setText((String) modeloTabla.getValueAt(filaSeleccionada, 1));
        txtNombre.setText((String) modeloTabla.getValueAt(filaSeleccionada, 2));
        txtCategoria.setText((String) modeloTabla.getValueAt(filaSeleccionada, 3));
        txtStock.setText(String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 4)));
        txtAlmacen.setText((String) modeloTabla.getValueAt(filaSeleccionada, 5));
        txtProveedor.setText((String) modeloTabla.getValueAt(filaSeleccionada, 6));
    }
    
    /**
     * Agregar nuevo insumo
     */
    public void agregarInsumo(JComponent parent) {
        if (!validarCampos(parent)) {
            return;
        }
        
        Insumo insumo = crearInsumoDesdeFormulario();
        
        if (insumoDAO.insertar(insumo)) {
            JOptionPane.showMessageDialog(parent,
                "Insumo agregado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarInsumos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al agregar insumo",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Modificar insumo existente
     */
    public void modificarInsumo(JComponent parent) {
        if (idInsumoSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un insumo de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos(parent)) {
            return;
        }
        
        Insumo insumo = crearInsumoDesdeFormulario();
        insumo.setIdInsumo(idInsumoSeleccionado);
        
        if (insumoDAO.actualizar(insumo)) {
            JOptionPane.showMessageDialog(parent,
                "Insumo actualizado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarInsumos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al actualizar insumo",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Eliminar insumo
     */
    public void eliminarInsumo(JComponent parent) {
        if (idInsumoSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un insumo de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(parent,
            "¿Está seguro de eliminar este insumo?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        if (insumoDAO.eliminar(idInsumoSeleccionado)) {
            JOptionPane.showMessageDialog(parent,
                "Insumo eliminado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarInsumos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al eliminar insumo",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validar campos del formulario
     */
    private boolean validarCampos(JComponent parent) {
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese código", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese nombre", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtStock.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese stock", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "El stock debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Crear objeto Insumo desde el formulario
     */
    private Insumo crearInsumoDesdeFormulario() {
        Insumo insumo = new Insumo();
        insumo.setCodigo(txtCodigo.getText().trim());
        insumo.setNombre(txtNombre.getText().trim());
        insumo.setStock(Integer.parseInt(txtStock.getText().trim()));
        insumo.setAlmacen(txtAlmacen.getText().trim());
        insumo.setIdCategoria(5); // ID de categoría "Insumos" por defecto
        insumo.setProveedor(txtProveedor.getText().trim());
        return insumo;
    }
    
    /**
     * Limpiar campos del formulario
     */
    private void limpiarCampos() {
        idInsumoSeleccionado = -1;
        lblID.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtStock.setText("");
        txtAlmacen.setText("");
        txtProveedor.setText("");
        tblInsumos.clearSelection();
    }
}