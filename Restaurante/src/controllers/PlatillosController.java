package controllers;

import dao.PlatilloDAO;
import dao.MenuDelDiaDAO;
import models.Platillo;
import Views.PanelPlatillo;
import Views.DialogPlatillosAñadir;
import Views.DialogPlatillosModificar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar platillos y el menú del día
 */
public class PlatillosController {
    
    private PlatilloDAO platilloDAO;
    private MenuDelDiaDAO menuDelDiaDAO;
    
    // Componentes de la vista
    private JTable tblMenuDia;
    private DefaultTableModel modeloMenuDia;
    private JPanel pnlContenedorPlatillos;
    private JScrollPane scrollPanePlatillos;
    
    // Lista de paneles de platillos
    private List<PanelPlatillo> panelesPlatillos;
    private PanelPlatillo panelSeleccionado;
    
    // Lista de platillos en el menú del día
    private List<Platillo> platillosEnMenu;
    
    // Referencia al PedidoController
    private Object pedidoControllerRef;
    
    public PlatillosController() {
        this.platilloDAO = new PlatilloDAO();
        this.menuDelDiaDAO = new MenuDelDiaDAO();
        this.panelesPlatillos = new ArrayList<>();
        this.platillosEnMenu = new ArrayList<>();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblMenuDia,
        JPanel pnlContenedorPlatillos,
        JScrollPane scrollPanePlatillos
    ) {
        this.tblMenuDia = tblMenuDia;
        this.pnlContenedorPlatillos = pnlContenedorPlatillos;
        this.scrollPanePlatillos = scrollPanePlatillos;
        
        configurarTabla();
        configurarPanelContenedor();
        cargarTodosPlatillos();
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
        
        tblMenuDia.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblMenuDia.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblMenuDia.getColumnModel().getColumn(2).setPreferredWidth(80);
    }
    
    /**
     * Configurar panel contenedor de platillos
     */
    private void configurarPanelContenedor() {
        pnlContenedorPlatillos.setLayout(null);
        pnlContenedorPlatillos.setBackground(new Color(203, 166, 131));
    }
    
    /**
     * Cargar todos los platillos desde la BD
     */
    public void cargarTodosPlatillos() {
        panelesPlatillos.clear();
        pnlContenedorPlatillos.removeAll();
        
        List<Platillo> platillos = platilloDAO.obtenerTodos();
        
        int yPosition = 20;
        
        for (Platillo p : platillos) {
            PanelPlatillo panelPlatillo = new PanelPlatillo(p);
            panelPlatillo.setBounds(20, yPosition, 420, 150);
            
            // Configurar listeners
            panelPlatillo.setOnPlatilloSeleccionadoListener(panel -> {
                seleccionarPanel(panel);
            });
            
            panelPlatillo.setOnMenuActionListener(new PanelPlatillo.OnMenuActionListener() {
                @Override
                public void onAgregarAlMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar) {
                    agregarPlatilloAMenu(platillo, btnAgregar, btnQuitar);
                }
                
                @Override
                public void onQuitarDelMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar) {
                    quitarPlatilloDeMenu(platillo, btnAgregar, btnQuitar);
                }
            });
            
            // Verificar si ya está en el menú
            boolean estaEnMenu = platillosEnMenu.stream()
                .anyMatch(pl -> pl.getIdPlatillo() == p.getIdPlatillo());
            panelPlatillo.setEstaEnMenu(estaEnMenu);
            
            pnlContenedorPlatillos.add(panelPlatillo);
            panelesPlatillos.add(panelPlatillo);
            
            yPosition += 170; // 150 de altura + 20 de separación
        }
        
        // Ajustar tamaño del panel contenedor
        pnlContenedorPlatillos.setPreferredSize(new Dimension(460, yPosition + 20));
        pnlContenedorPlatillos.revalidate();
        pnlContenedorPlatillos.repaint();
        scrollPanePlatillos.revalidate();
    }
    
    /**
     * Seleccionar un panel de platillo
     */
    private void seleccionarPanel(PanelPlatillo panel) {
        // Deseleccionar todos
        for (PanelPlatillo p : panelesPlatillos) {
            p.marcarComoSeleccionado(false);
        }
        
        // Seleccionar el clickeado
        panel.marcarComoSeleccionado(true);
        panelSeleccionado = panel;
    }
    
    /**
     * Cargar platillos en el menú del día (inicialmente vacío)
     */
    public void cargarPlatillosEnMenu() {
        modeloMenuDia.setRowCount(0);
        platillosEnMenu.clear(); // Iniciar vacío
        
        // Si quieres cargar todos al inicio, descomenta estas líneas:
        // platillosEnMenu = platilloDAO.obtenerTodos();
        // for (Platillo p : platillosEnMenu) {
        //     modeloMenuDia.addRow(new Object[]{
        //         p.getNombre(),
        //         p.getNombreCategoria(),
        //         String.format("S/. %.2f", p.getPrecio())
        //     });
        // }
        
        actualizarMenuEnPedidos();
    }
    
    /**
     * Agregar platillo al menú del día
     */
    private void agregarPlatilloAMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar) {
        // Verificar si ya está
        boolean yaEsta = platillosEnMenu.stream()
            .anyMatch(p -> p.getIdPlatillo() == platillo.getIdPlatillo());
        
        if (yaEsta) {
            return;
        }
        
        // Agregar a la lista y tabla
        platillosEnMenu.add(platillo);
        modeloMenuDia.addRow(new Object[]{
            platillo.getNombre(),
            platillo.getNombreCategoria(),
            String.format("S/. %.2f", platillo.getPrecio())
        });
        
        // Actualizar botones
        btnAgregar.setEnabled(false);
        btnQuitar.setEnabled(true);
        
        actualizarMenuEnPedidos();
    }
    
    /**
     * Quitar platillo del menú del día
     */
    private void quitarPlatilloDeMenu(Platillo platillo, JButton btnAgregar, JButton btnQuitar) {
        // Buscar y eliminar
        for (int i = 0; i < platillosEnMenu.size(); i++) {
            if (platillosEnMenu.get(i).getIdPlatillo() == platillo.getIdPlatillo()) {
                platillosEnMenu.remove(i);
                modeloMenuDia.removeRow(i);
                break;
            }
        }
        
        // Actualizar botones
        btnAgregar.setEnabled(true);
        btnQuitar.setEnabled(false);
        
        actualizarMenuEnPedidos();
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
     * Nuevo platillo - Abrir diálogo
     */
    public void nuevoPlatillo(JFrame parent) {
        DialogPlatillosAñadir dialog = new DialogPlatillosAñadir(parent, true);
        
        // Cargar categorías en el combo
        cargarCategoriasEnCombo(dialog.jComboBoxCategoria);
        
        // Configurar botón seleccionar imagen
        dialog.btnSeleccionarImagen.addActionListener(e -> {
            seleccionarImagen(dialog.lblImagen, dialog);
        });
        
        // Configurar botón añadir
        dialog.jButtonAñadir.addActionListener(e -> {
            if (validarCamposAñadir(dialog)) {
                Platillo nuevoPlatillo = crearPlatilloDesdeDialogo(dialog);
                
                if (platilloDAO.insertar(nuevoPlatillo)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Platillo agregado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    cargarTodosPlatillos();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Error al agregar platillo",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Configurar botón cancelar
        dialog.jButtonCancelar.addActionListener(e -> dialog.dispose());
        
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Modificar platillo - Abrir diálogo
     */
    public void modificarPlatillo(JFrame parent) {
        if (panelSeleccionado == null) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un platillo para modificar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DialogPlatillosModificar dialog = new DialogPlatillosModificar(parent, true);
        
        // Cargar categorías
        cargarCategoriasEnCombo(dialog.jComboBoxCategoria);
        
        // Cargar datos del platillo seleccionado
        Platillo platillo = panelSeleccionado.getPlatillo();
        dialog.jTextFieldNombrePlatillo.setText(platillo.getNombre());
        dialog.jTextFieldPrecioPlatillo.setText(String.valueOf(platillo.getPrecio()));
        dialog.jComboBoxCategoria.setSelectedItem(platillo.getNombreCategoria());
        
        // Cargar imagen
        if (platillo.getImagen() != null && platillo.getImagen().length > 0) {
            ImageIcon icon = new ImageIcon(platillo.getImagen());
            Image img = icon.getImage().getScaledInstance(
                dialog.lblImagen.getWidth(), 
                dialog.lblImagen.getHeight(), 
                Image.SCALE_SMOOTH
            );
            dialog.lblImagen.setIcon(new ImageIcon(img));
        }
        
        // Configurar botón seleccionar imagen
        dialog.btnSeleccionarImagen.addActionListener(e -> {
            seleccionarImagen(dialog.lblImagen, dialog);
        });
        
        // Configurar botón modificar
        dialog.jButtonModificar.addActionListener(e -> {
            if (validarCamposModificar(dialog)) {
                Platillo platilloModificado = crearPlatilloDesdeDialogoModificar(dialog);
                platilloModificado.setIdPlatillo(platillo.getIdPlatillo());
                
                if (platilloDAO.actualizar(platilloModificado)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Platillo modificado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    cargarTodosPlatillos();
                    cargarPlatillosEnMenu();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Error al modificar platillo",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Configurar botón cancelar
        dialog.jButtonCancelar.addActionListener(e -> dialog.dispose());
        
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Eliminar platillo
     */
    public void eliminarPlatillo(JComponent parent) {
        if (panelSeleccionado == null) {
            JOptionPane.showMessageDialog(parent,
                "Por favor seleccione un platillo para eliminar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(parent,
            "¿Está seguro de eliminar el platillo '" + panelSeleccionado.getPlatillo().getNombre() + "'?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        if (platilloDAO.eliminar(panelSeleccionado.getPlatillo().getIdPlatillo())) {
            JOptionPane.showMessageDialog(parent,
                "Platillo eliminado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            panelSeleccionado = null;
            cargarTodosPlatillos();
            cargarPlatillosEnMenu();
        } else {
            JOptionPane.showMessageDialog(parent,
                "Error al eliminar platillo",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cargar categorías en combo
     */
    private void cargarCategoriasEnCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        combo.addItem("Bebidas");
        combo.addItem("Entradas");
        combo.addItem("Platos Fuertes");
        combo.addItem("Postres");
    }
    
    /**
     * Seleccionar imagen con FileChooser
     */
    private byte[] imagenSeleccionada = null;
    
    private void seleccionarImagen(JLabel lblImagen, JDialog dialog) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen del platillo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(dialog);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                
                // Leer imagen como bytes
                java.io.FileInputStream fis = new java.io.FileInputStream(file);
                imagenSeleccionada = new byte[(int) file.length()];
                fis.read(imagenSeleccionada);
                fis.close();
                
                // Mostrar preview
                ImageIcon icon = new ImageIcon(imagenSeleccionada);
                Image img = icon.getImage().getScaledInstance(
                    lblImagen.getWidth(), 
                    lblImagen.getHeight(), 
                    Image.SCALE_SMOOTH
                );
                lblImagen.setIcon(new ImageIcon(img));
                lblImagen.setText("");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error al cargar imagen: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Validar campos del diálogo añadir
     */
    private boolean validarCamposAñadir(DialogPlatillosAñadir dialog) {
        if (dialog.jTextFieldNombrePlatillo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Ingrese nombre del platillo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dialog.jTextFieldPrecioPlatillo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Ingrese precio del platillo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(dialog.jTextFieldPrecioPlatillo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Validar campos del diálogo modificar
     */
    private boolean validarCamposModificar(DialogPlatillosModificar dialog) {
        if (dialog.jTextFieldNombrePlatillo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Ingrese nombre del platillo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dialog.jTextFieldPrecioPlatillo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Ingrese precio del platillo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(dialog.jTextFieldPrecioPlatillo.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Crear platillo desde diálogo añadir
     */
    private Platillo crearPlatilloDesdeDialogo(DialogPlatillosAñadir dialog) {
        String nombre = dialog.jTextFieldNombrePlatillo.getText().trim();
        double precio = Double.parseDouble(dialog.jTextFieldPrecioPlatillo.getText().trim());
        int idCategoria = dialog.jComboBoxCategoria.getSelectedIndex() + 1;
        
        Platillo platillo = new Platillo(nombre, idCategoria, precio, imagenSeleccionada);
        imagenSeleccionada = null; // Limpiar
        
        return platillo;
    }
    
    /**
     * Crear platillo desde diálogo modificar
     */
    private Platillo crearPlatilloDesdeDialogoModificar(DialogPlatillosModificar dialog) {
        String nombre = dialog.jTextFieldNombrePlatillo.getText().trim();
        double precio = Double.parseDouble(dialog.jTextFieldPrecioPlatillo.getText().trim());
        int idCategoria = dialog.jComboBoxCategoria.getSelectedIndex() + 1;
        
        // Si no se seleccionó nueva imagen, usar la actual
        byte[] imagen = imagenSeleccionada != null ? imagenSeleccionada : panelSeleccionado.getPlatillo().getImagen();
        
        Platillo platillo = new Platillo(nombre, idCategoria, precio, imagen);
        imagenSeleccionada = null; // Limpiar
        
        return platillo;
    }
}