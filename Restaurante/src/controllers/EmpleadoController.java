package controllers;

import dao.EmpleadoDAO;
import models.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador para gestionar el panel de Empleados
 */
public class EmpleadoController {
    
    private EmpleadoDAO empleadoDAO;
    
    // Componentes de la vista
    private JTable tblEmpleados;
    private DefaultTableModel modeloTabla;
    private JLabel lblID;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDNI;
    private JRadioButton rbMasculino;
    private JRadioButton rbFemenino;
    private JTextField txtFechaNacimiento;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtUsuario;
    private JTextField txtContrasena;
    private JTextField txtCargo;
    private JTextField txtHorario;
    
    private int idEmpleadoSeleccionado = -1;
    
    public EmpleadoController() {
        this.empleadoDAO = new EmpleadoDAO();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblEmpleados,
        JLabel lblID,
        JTextField txtNombres,
        JTextField txtApellidos,
        JTextField txtDNI,
        JRadioButton rbMasculino,
        JRadioButton rbFemenino,
        JTextField txtFechaNacimiento,
        JTextField txtDireccion,
        JTextField txtTelefono,
        JTextField txtCorreo,
        JTextField txtUsuario,
        JTextField txtContrasena,
        JTextField txtCargo,
        JTextField txtHorario
    ) {
        this.tblEmpleados = tblEmpleados;
        this.lblID = lblID;
        this.txtNombres = txtNombres;
        this.txtApellidos = txtApellidos;
        this.txtDNI = txtDNI;
        this.rbMasculino = rbMasculino;
        this.rbFemenino = rbFemenino;
        this.txtFechaNacimiento = txtFechaNacimiento;
        this.txtDireccion = txtDireccion;
        this.txtTelefono = txtTelefono;
        this.txtCorreo = txtCorreo;
        this.txtUsuario = txtUsuario;
        this.txtContrasena = txtContrasena;
        this.txtCargo = txtCargo;
        this.txtHorario = txtHorario;
        
        configurarTabla();
        cargarEmpleados();
        configurarListenerTabla();
    }
    
    /**
     * Configurar tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombres", "Apellidos", "DNI", "Sexo", "Fecha Nac.", 
                        "Cargo", "Dirección", "Teléfono", "Correo", "Usuario", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados.setModel(modeloTabla);
    }
    
    /**
     * Configurar listener de selección de tabla
     */
    private void configurarListenerTabla() {
        tblEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });
    }
    
    /**
     * Cargar todos los empleados
     */
    public void cargarEmpleados() {
        modeloTabla.setRowCount(0);
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Empleado e : empleados) {
            modeloTabla.addRow(new Object[]{
                e.getIdEmpleado(),
                e.getNombres(),
                e.getApellidos(),
                e.getDni(),
                e.getSexo(),
                e.getFechaNacimiento() != null ? e.getFechaNacimiento().format(formato) : "",
                e.getCargo(),
                e.getDireccion(),
                e.getTelefono(),
                e.getCorreo(),
                e.getUsuario(),
                e.getHorario()
            });
        }
    }
    
    /**
     * Cargar empleado seleccionado en los campos
     */
    private void cargarEmpleadoSeleccionado() {
        int filaSeleccionada = tblEmpleados.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            return;
        }
        
        idEmpleadoSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        lblID.setText(String.valueOf(idEmpleadoSeleccionado));
        txtNombres.setText((String) modeloTabla.getValueAt(filaSeleccionada, 1));
        txtApellidos.setText((String) modeloTabla.getValueAt(filaSeleccionada, 2));
        txtDNI.setText((String) modeloTabla.getValueAt(filaSeleccionada, 3));
        
        String sexo = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
        if ("Masculino".equals(sexo)) {
            rbMasculino.setSelected(true);
        } else {
            rbFemenino.setSelected(true);
        }
        
        txtFechaNacimiento.setText((String) modeloTabla.getValueAt(filaSeleccionada, 5));
        txtCargo.setText((String) modeloTabla.getValueAt(filaSeleccionada, 6));
        txtDireccion.setText((String) modeloTabla.getValueAt(filaSeleccionada, 7));
        txtTelefono.setText((String) modeloTabla.getValueAt(filaSeleccionada, 8));
        txtCorreo.setText((String) modeloTabla.getValueAt(filaSeleccionada, 9));
        txtUsuario.setText((String) modeloTabla.getValueAt(filaSeleccionada, 10));
        txtHorario.setText((String) modeloTabla.getValueAt(filaSeleccionada, 11));
        txtContrasena.setText(""); // No mostrar contraseña
    }
    
    /**
     * Agregar nuevo empleado
     */
    public void agregarEmpleado(JComponent parent) {
        if (!validarCampos(parent)) {
            return;
        }
        
        Empleado empleado = crearEmpleadoDesdeFormulario();
        
        if (empleadoDAO.insertar(empleado)) {
            JOptionPane.showMessageDialog(parent,
                "Empleado agregado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al agregar empleado",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Modificar empleado existente
     */
    public void modificarEmpleado(JComponent parent) {
        if (idEmpleadoSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un empleado de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos(parent)) {
            return;
        }
        
        Empleado empleado = crearEmpleadoDesdeFormulario();
        empleado.setIdEmpleado(idEmpleadoSeleccionado);
        
        if (empleadoDAO.actualizar(empleado)) {
            JOptionPane.showMessageDialog(parent,
                "Empleado actualizado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al actualizar empleado",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Eliminar empleado
     */
    public void eliminarEmpleado(JComponent parent) {
        if (idEmpleadoSeleccionado == -1) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un empleado de la tabla",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(parent,
            "¿Está seguro de eliminar este empleado?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        if (empleadoDAO.eliminar(idEmpleadoSeleccionado)) {
            JOptionPane.showMessageDialog(parent,
                "Empleado eliminado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al eliminar empleado",
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
     * Crear objeto Empleado desde el formulario
     */
    private Empleado crearEmpleadoDesdeFormulario() {
        Empleado empleado = new Empleado();
        empleado.setNombres(txtNombres.getText().trim());
        empleado.setApellidos(txtApellidos.getText().trim());
        empleado.setDni(txtDNI.getText().trim());
        empleado.setSexo(rbMasculino.isSelected() ? "Masculino" : "Femenino");
        
        // Parsear fecha
        try {
            String fechaStr = txtFechaNacimiento.getText().trim();
            if (!fechaStr.isEmpty()) {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                empleado.setFechaNacimiento(LocalDate.parse(fechaStr, formato));
            }
        } catch (DateTimeParseException e) {
            empleado.setFechaNacimiento(null);
        }
        
        empleado.setDireccion(txtDireccion.getText().trim());
        empleado.setTelefono(txtTelefono.getText().trim());
        empleado.setCorreo(txtCorreo.getText().trim());
        empleado.setUsuario(txtUsuario.getText().trim());
        empleado.setContrasena(txtContrasena.getText().trim());
        empleado.setCargo(txtCargo.getText().trim());
        empleado.setHorario(txtHorario.getText().trim());
        
        return empleado;
    }
    
    /**
     * Limpiar campos del formulario
     */
    private void limpiarCampos() {
        idEmpleadoSeleccionado = -1;
        lblID.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDNI.setText("");
        rbMasculino.setSelected(true);
        txtFechaNacimiento.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtCargo.setText("");
        txtHorario.setText("");
        tblEmpleados.clearSelection();
    }
}