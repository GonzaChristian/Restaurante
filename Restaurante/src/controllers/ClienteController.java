package controllers;

import dao.ClienteDAO;
import models.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controlador para gestionar el panel de Clientes
 */
public class ClienteController {
    
    private ClienteDAO clienteDAO;
    
    // Componentes de la vista
    private JTable tblClientes;
    private DefaultTableModel modeloTabla;
    private JLabel lblID;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDNI;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    
    private int idClienteSeleccionado = -1;
    
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblClientes,
        JLabel lblID,
        JTextField txtNombres,
        JTextField txtApellidos,
        JTextField txtDNI,
        JTextField txtDireccion,
        JTextField txtTelefono,
        JTextField txtCorreo
    ) {
        this.tblClientes = tblClientes;
        this.lblID = lblID;
        this.txtNombres = txtNombres;
        this.txtApellidos = txtApellidos;
        this.txtDNI = txtDNI;
        this.txtDireccion = txtDireccion;
        this.txtTelefono = txtTelefono;
        this.txtCorreo = txtCorreo;
        
        configurarTabla();
        cargarClientes();
        configurarListenerTabla();
    }
    
    /**
     * Configurar tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombres", "Apellidos", "DNI", "Dirección", "Teléfono", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblClientes.setModel(modeloTabla);
    }
    
    /**
     * Configurar listener de selección de tabla
     */
    private void configurarListenerTabla() {
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
    }
    
    /**
     * Cargar todos los clientes
     */
    public void cargarClientes() {
        modeloTabla.setRowCount(0);
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombres(),
                c.getApellidos(),
                c.getDni(),
                c.getDireccion(),
                c.getTelefono(),
                c.getCorreo()
            });
        }
    }
    
    /**
     * Cargar cliente seleccionado en los campos
     */
    private void cargarClienteSeleccionado() {
        int filaSeleccionada = tblClientes.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            return;
        }
        
        idClienteSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        lblID.setText(String.valueOf(idClienteSeleccionado));
        txtNombres.setText((String) modeloTabla.getValueAt(filaSeleccionada, 1));
        txtApellidos.setText((String) modeloTabla.getValueAt(filaSeleccionada, 2));
        txtDNI.setText((String) modeloTabla.getValueAt(filaSeleccionada, 3));
        txtDireccion.setText((String) modeloTabla.getValueAt(filaSeleccionada, 4));
        txtTelefono.setText((String) modeloTabla.getValueAt(filaSeleccionada, 5));
        txtCorreo.setText((String) modeloTabla.getValueAt(filaSeleccionada, 6));
    }
    
    /**
     * Agregar nuevo cliente
     */
    public void agregarCliente(JComponent parent) {
        if (!validarCampos(parent)) {
            return;
        }
        
        Cliente cliente = crearClienteDesdeFormulario();
        
        if (clienteDAO.insertar(cliente)) {
            JOptionPane.showMessageDialog(parent,
                "Cliente agregado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al agregar cliente",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Modificar cliente existente
     */
    public void modificarCliente(JComponent parent) {
        if (idClienteSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un cliente de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos(parent)) {
            return;
        }
        
        Cliente cliente = crearClienteDesdeFormulario();
        cliente.setIdCliente(idClienteSeleccionado);
        
        if (clienteDAO.actualizar(cliente)) {
            JOptionPane.showMessageDialog(parent,
                "Cliente actualizado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al actualizar cliente",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Eliminar cliente
     */
    public void eliminarCliente(JComponent parent) {
        if (idClienteSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un cliente de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(parent,
            "¿Está seguro de eliminar este cliente?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        if (clienteDAO.eliminar(idClienteSeleccionado)) {
            JOptionPane.showMessageDialog(parent,
                "Cliente eliminado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al eliminar cliente",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validar campos del formulario
     */
    private boolean validarCampos(JComponent parent) {
        if (txtNombres.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese nombres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese apellidos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtDNI.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese DNI", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Crear objeto Cliente desde el formulario
     */
    private Cliente crearClienteDesdeFormulario() {
        Cliente cliente = new Cliente();
        cliente.setNombres(txtNombres.getText().trim());
        cliente.setApellidos(txtApellidos.getText().trim());
        cliente.setDni(txtDNI.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreo(txtCorreo.getText().trim());
        return cliente;
    }
    
    /**
     * Limpiar campos del formulario
     */
    private void limpiarCampos() {
        idClienteSeleccionado = -1;
        lblID.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDNI.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tblClientes.clearSelection();
    }
}