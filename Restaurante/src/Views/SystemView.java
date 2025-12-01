
package Views;
import controllers.PedidoController;
import Views.PanelMesas;
import controllers.AdministrarPedidosController;
import controllers.EmpleadoController;
import controllers.ClienteController;
import controllers.InsumoController;
import controllers.PlatillosController;
import controllers.ReportesController;

public class SystemView extends javax.swing.JFrame {
    
    private PanelMesas panelMesas;
    private PlatillosController platillosController;
    private PedidoController pedidoController;
    private AdministrarPedidosController adminPedidosController;
    private PanelGenerarPedido panelGenerarPedido;
    private EmpleadoController empleadoController;
    private ClienteController clienteController;
    private InsumoController insumoController;
    private ReportesController reportesController;
    public SystemView(){
        initComponents(); // Mantener esto primero
    
        // ========== INICIALIZAR CONTROLADOR ==========
        pedidoController = new PedidoController();

        // ========== INICIALIZAR COMPONENTES DEL CONTROLADOR ==========
        pedidoController.inicializarComponentes(
            cmbMesas,                   // ComboBox de mesas
            jTablePedidoActual,         // Tabla de pedido actual
            jTableMenuDelDia,           // Tabla de menú del día
            lblSubtotal,                // Label subtotal
            lblIGV,                     // Label IGV
            jLabel9,                    // Label Total (usa jLabel9 según tu diseño)
            jTextFieldCantidad          // TextField de cantidad
        );
        
        // ========== CONFIGURAR PANEL DE MESAS ==========
        panelMesas = new PanelMesas();
        pnlMesas.removeAll();
        pnlMesas.setLayout(new java.awt.BorderLayout());
        pnlMesas.add(panelMesas, java.awt.BorderLayout.CENTER);
        pnlMesas.revalidate();
        pnlMesas.repaint();

        // Conectar referencia de panel mesas al controlador
        pedidoController.setPanelMesasReferencia(panelMesas);

        // ========== CONFIGURAR LISTENER DE SELECCIÓN DE MESA ==========
        panelMesas.setOnMesaSeleccionadaListener(numeroMesa -> {
            // Cambiar a la pestaña de Generar Pedido
            jTabbedPane1.setSelectedComponent(pnlGenerarPedido);

            // Actualizar el combo y cargar el pedido de esa mesa
            pedidoController.setMesaSeleccionada(numeroMesa);
        });
        configurarListeners();
    
        
        adminPedidosController = new AdministrarPedidosController();
        adminPedidosController.inicializarComponentes(
            jTableAdministrarPedidos,      // Tabla de pedidos
            jTextFieldBuscarPedido         // TextField de búsqueda
        );
        adminPedidosController.setPanelMesasReferencia(panelMesas);
        
        
        // ========== CONFIGURAR PANEL PLATILLOS ==========
        platillosController = new PlatillosController();
        platillosController.inicializarComponentes(
            jTableMenuDia,
            jPanelContenedorPlatillos,
            jScrollPanePlatillos
        );
        platillosController.setPedidoControllerReferencia(pedidoController);
        
        
        // ========== CONFIGURAR PANEL EMPLEADOS ==========
    empleadoController = new EmpleadoController();
    empleadoController.inicializarComponentes(
        jTableEmpleados,
        jLabelIDEmpleado,
        jTextFieldNombreEmpledo,
        jTextFieldApellidoEmpleado,
        jTextFieldDNIEmpleado,
        jRadioButtonMasculinoEmpleado,
        jRadioButtonFemeninoEmpleado,
        jTextFieldFechaNacimientoEmpleado,
        jTextFieldDireccionEmpleado,
        jTextFieldTelefonoEmpleado,
        jTextFieldCorreoEmpleado,
        jTextFieldUsuarioEmpleado,
        jTextFieldContraseñaEmpleado,
        jTextFieldCargoEmpleado,
        jTextFieldHorarioEmpleado
    );
    
    // ========== CONFIGURAR PANEL CLIENTES ==========
    clienteController = new ClienteController();
    clienteController.inicializarComponentes(
        jTableClientes,
        jLabelIDCliente,
        jTextFieldNombreCliente,
        jTextFieldApellidoCliente,
        jTextFieldDNICliente,
        jTextFieldDireccionCliente,
        jTextFieldTelefonoCliente,
        jTextFieldCorreoCliente
    );
    
    // ========== CONFIGURAR PANEL INVENTARIO ==========
    insumoController = new InsumoController();
    insumoController.inicializarComponentes(
        jTableInsumos,
        jLabelIDInsumos,
        jTextFieldCodigoInsumo,
        jTextFieldNombreInsumo,
        jTextFieldCategoriaInsumo,
        jTextFieldStockInsumo,
        jTextFieldAlmacenInsumo,
        jTextFieldProveedorInsumo
    );
        
    // ========== CONFIGURAR PANEL REPORTES ==========
       reportesController = new ReportesController();
       reportesController.inicializarComponentes(
           jTableReportes,
           jTextFieldBuscarReporte,  
           jComboBoxFiltroReporte    
    );
       
    }
    private void configurarListeners() {
        // Listener del ComboBox de mesas
        cmbMesas.addActionListener(evt -> {
            pedidoController.cambiarMesa();
        });
        

        // Listener del botón Agregar
        jButtonAgregar.addActionListener(evt -> {
            pedidoController.agregarPlatillo(pnlGenerarPedido);
        });

        // Listener del botón Pedido en Proceso
        btnPedidoEnProceso.addActionListener(evt -> {
            pedidoController.marcarPedidoEnProceso(pnlGenerarPedido);
        });

        // Listener del botón Pedido Completado
        btnPedidoCompletado.addActionListener(evt -> {
            pedidoController.marcarPedidoCompletado(pnlGenerarPedido);
        });

        // Listener del botón Imprimir (placeholder por ahora)
        btnImprimirVoucher.addActionListener(evt -> {
            adminPedidosController.imprimirVoucherPedido(pnlGenerarPedido);
        });
        
        jButtonBuscar.addActionListener(evt -> {
            adminPedidosController.buscarPedidos(pnlAdministrarPedido);
        });

        jButtonActualizar.addActionListener(evt -> {
            adminPedidosController.actualizar();
        });

        jButtonVerDetalle.addActionListener(evt -> {
            adminPedidosController.verDetallePedido(pnlAdministrarPedido);
        });

        jButton8.addActionListener(evt -> {  // Botón "Marcar Completado"
            adminPedidosController.marcarCompletado(pnlAdministrarPedido);
        });
        

        // ========== LISTENERS PANEL PLATILLOS ==========
        jButtonNuevoPlatillo.addActionListener(evt -> {
            platillosController.nuevoPlatillo(SystemView.this);
        });

        jButtonModificarPlatillo.addActionListener(evt -> {
            platillosController.modificarPlatillo(SystemView.this); 
        });

        jButtonEliminarPlatillo.addActionListener(evt -> {
            platillosController.eliminarPlatillo(pnlPlatillos);  
        });

        jButtonNuevoEmpleado.addActionListener(evt -> {
        empleadoController.agregarEmpleado(pnlEmpleados);
        });

        jButtonModificarEmpleado.addActionListener(evt -> {
            empleadoController.modificarEmpleado(pnlEmpleados);
        });

        jButtonEliminarEmpleado.addActionListener(evt -> {
            empleadoController.eliminarEmpleado(pnlEmpleados);
        });

        // ========== LISTENERS PANEL CLIENTES ==========
        jButtonNuevoCliente.addActionListener(evt -> {
            clienteController.agregarCliente(pnlClientes);
        });

        jButtonModificarCliente.addActionListener(evt -> {
            clienteController.modificarCliente(pnlClientes);
        });

        jButtonEliminarCliente.addActionListener(evt -> {
            clienteController.eliminarCliente(pnlClientes);
        });

        // ========== LISTENERS PANEL INVENTARIO ==========
        jButtonNuevoInsumo.addActionListener(evt -> {
            insumoController.agregarInsumo(pnlInventario);
        });

        jButtonModificarInsumo.addActionListener(evt -> {
            insumoController.modificarInsumo(pnlInventario);
        });

        jButtonEliminarInsumo.addActionListener(evt -> {
            insumoController.eliminarInsumo(pnlInventario);
        });
        
        // ========== LISTENERS PANEL REPORTES ==========
    
        // Botón "PLATILLOS" - Mostrar platillos más vendidos
        jPanelButtonPlatillos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportesController.mostrarReportePlatillos();
            }
        });

        // Botón "INVENTARIO" - Mostrar estado del inventario
        jPanelButtonInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportesController.mostrarReporteInventario();
            }
        });

        // Botón "CAJA" - Mostrar reporte financiero
        jPanelButtonCaja.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportesController.mostrarReporteCaja();
            }
        });

        // Botón "IMPRIMIR"
        jPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportesController.imprimirReporte(pnlReportes);
            }
        });
        // TextField búsqueda - Enter para buscar
        jTextFieldBuscarReporte.addActionListener(evt -> {
            reportesController.buscar();
        });
    }
    

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlMesas = new javax.swing.JPanel();
        pnlContenedorMesas = new javax.swing.JPanel();
        pnlGenerarPedido = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbMesas = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMenuDelDia = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jButtonAgregar = new javax.swing.JButton();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePedidoActual = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblIGV = new javax.swing.JLabel();
        btnImprimirVoucher = new javax.swing.JButton();
        btnPedidoEnProceso = new javax.swing.JButton();
        btnPedidoCompletado = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        pnlAdministrarPedido = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableAdministrarPedidos = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldBuscarPedido = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jButtonActualizar = new javax.swing.JButton();
        jButtonVerDetalle = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        pnlPlatillos = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jButtonNuevoPlatillo = new javax.swing.JButton();
        jButtonEliminarPlatillo = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jTable5 = new javax.swing.JTable();
        jScrollPanePlatillos = new javax.swing.JScrollPane();
        jPanelContenedorPlatillos = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jPanelEstofadoDePollo = new javax.swing.JPanel();
        jLabelImagenPlatillo = new javax.swing.JLabel();
        jButtonAgregarPlatilloAMenu = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        lblNombrePlatillo = new javax.swing.JLabel();
        lblCategoriaPlatillo = new javax.swing.JLabel();
        lblPrecioPlatillo = new javax.swing.JLabel();
        jButtonQuitarPlatilloDeMenu = new javax.swing.JButton();
        jPanelLomoSaltado = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButtonQuitarLomoSaltadoDeMenu = new javax.swing.JButton();
        jLabelPrecio = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabelCategoria = new javax.swing.JLabel();
        jButtonAgregarLomoSaltadoAMenu = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jPanel51 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jButton32 = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jButton36 = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jButton37 = new javax.swing.JButton();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jButton38 = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jLabel148 = new javax.swing.JLabel();
        jButton39 = new javax.swing.JButton();
        jLabel149 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        jButton40 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableMenuDia = new javax.swing.JTable();
        jLabel94 = new javax.swing.JLabel();
        jButtonModificarPlatillo = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        pnlEmpleados = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jTextFieldNombreEmpledo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldApellidoEmpleado = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldDNIEmpleado = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldFechaNacimientoEmpleado = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jRadioButtonMasculinoEmpleado = new javax.swing.JRadioButton();
        jRadioButtonFemeninoEmpleado = new javax.swing.JRadioButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextFieldCargoEmpleado = new javax.swing.JTextField();
        jLabelIDEmpleado = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextFieldHorarioEmpleado = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextFieldTelefonoEmpleado = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextFieldUsuarioEmpleado = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextFieldCorreoEmpleado = new javax.swing.JTextField();
        jLabel155 = new javax.swing.JLabel();
        jTextFieldContraseñaEmpleado = new javax.swing.JTextField();
        jLabel156 = new javax.swing.JLabel();
        jTextFieldDireccionEmpleado = new javax.swing.JTextField();
        jButtonNuevoEmpleado = new javax.swing.JButton();
        jButtonModificarEmpleado = new javax.swing.JButton();
        jButtonEliminarEmpleado = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableEmpleados = new javax.swing.JTable();
        pnlClientes = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jTextFieldNombreCliente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        jTextFieldApellidoCliente = new javax.swing.JTextField();
        jLabel158 = new javax.swing.JLabel();
        jTextFieldDNICliente = new javax.swing.JTextField();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        jLabelIDCliente = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jTextFieldTelefonoCliente = new javax.swing.JTextField();
        jLabel168 = new javax.swing.JLabel();
        jTextFieldCorreoCliente = new javax.swing.JTextField();
        jLabel170 = new javax.swing.JLabel();
        jTextFieldDireccionCliente = new javax.swing.JTextField();
        jButtonNuevoCliente = new javax.swing.JButton();
        jButtonModificarCliente = new javax.swing.JButton();
        jButtonEliminarCliente = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        pnlInventario = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jTextFieldCodigoInsumo = new javax.swing.JTextField();
        jLabel160 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jTextFieldNombreInsumo = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        jTextFieldCategoriaInsumo = new javax.swing.JTextField();
        jLabel169 = new javax.swing.JLabel();
        jLabel171 = new javax.swing.JLabel();
        jLabelIDInsumos = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        jTextFieldAlmacenInsumo = new javax.swing.JTextField();
        jLabel174 = new javax.swing.JLabel();
        jTextFieldProveedorInsumo = new javax.swing.JTextField();
        jLabel175 = new javax.swing.JLabel();
        jTextFieldStockInsumo = new javax.swing.JTextField();
        jLabel176 = new javax.swing.JLabel();
        jButtonEliminarInsumo = new javax.swing.JButton();
        jButtonModificarInsumo = new javax.swing.JButton();
        jButtonNuevoInsumo = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableInsumos = new javax.swing.JTable();
        pnlReportes = new javax.swing.JPanel();
        jPanelButtonCaja = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableReportes = new javax.swing.JTable();
        jTextFieldBuscarReporte = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jComboBoxFiltroReporte = new javax.swing.JComboBox<>();
        jPanelButtonPlatillos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanelButtonInventario = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(210, 166, 129));

        pnlMesas.setBackground(new java.awt.Color(255, 240, 228));
        pnlMesas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlContenedorMesas.setName("pnlContenedorMesas"); // NOI18N
        pnlContenedorMesas.setLayout(new java.awt.GridLayout(1, 0));
        pnlMesas.add(pnlContenedorMesas, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 1000, 720));

        jTabbedPane1.addTab("Mesas", pnlMesas);

        pnlGenerarPedido.setBackground(new java.awt.Color(255, 240, 228));
        pnlGenerarPedido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("MESA:");
        pnlGenerarPedido.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 70, 20));

        cmbMesas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        pnlGenerarPedido.add(cmbMesas, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jPanel1.setBackground(new java.awt.Color(209, 188, 172));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableMenuDelDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Precio"
            }
        ));
        jScrollPane2.setViewportView(jTableMenuDelDia);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 420, 470));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setText("MENÚ DEL DÍA");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 260, 40));

        jButtonAgregar.setText("Agregar");
        jPanel1.add(jButtonAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 610, 150, 50));
        jPanel1.add(jTextFieldCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 620, 80, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Cantidad:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 620, -1, -1));

        pnlGenerarPedido.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 480, 690));

        jTablePedidoActual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Cantidad", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(jTablePedidoActual);

        pnlGenerarPedido.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 500, 320));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("0");
        pnlGenerarPedido.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 590, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Pedido actual:");
        pnlGenerarPedido.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSubtotal.setText("0");
        pnlGenerarPedido.add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 490, -1, -1));

        lblIGV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblIGV.setText("0");
        pnlGenerarPedido.add(lblIGV, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 540, -1, -1));

        btnImprimirVoucher.setText("🖨 Imprimir");
        btnImprimirVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirVoucherActionPerformed(evt);
            }
        });
        pnlGenerarPedido.add(btnImprimirVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 650, 150, 50));

        btnPedidoEnProceso.setText("Pedido en Proceso");
        btnPedidoEnProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedidoEnProcesoActionPerformed(evt);
            }
        });
        pnlGenerarPedido.add(btnPedidoEnProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 650, 150, 50));

        btnPedidoCompletado.setText("Pedido Completado");
        pnlGenerarPedido.add(btnPedidoCompletado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 650, 150, 50));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Total:");
        pnlGenerarPedido.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 590, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Subtotal:");
        pnlGenerarPedido.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("IGV:");
        pnlGenerarPedido.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 540, -1, -1));

        jTabbedPane1.addTab("Generar pedido", pnlGenerarPedido);

        pnlAdministrarPedido.setBackground(new java.awt.Color(204, 204, 204));
        pnlAdministrarPedido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(255, 240, 228));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableAdministrarPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mesa", "Total", "Fecha", "Hora", "Estado"
            }
        ));
        jScrollPane3.setViewportView(jTableAdministrarPedidos);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 1010, 410));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Buscar:");
        jPanel11.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        jTextFieldBuscarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBuscarPedidoActionPerformed(evt);
            }
        });
        jPanel11.add(jTextFieldBuscarPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 350, 30));

        jButtonBuscar.setText("Buscar");
        jPanel11.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 150, -1, 30));

        jButtonActualizar.setText("Actualizar");
        jPanel11.add(jButtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 150, -1, 30));

        jButtonVerDetalle.setText("Ver Detalle");
        jPanel11.add(jButtonVerDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 660, 180, 40));

        jButton8.setText("✔ Marcar Completado");
        jPanel11.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 660, 180, 40));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 68)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(136, 92, 49));
        jLabel15.setText("ADMINISTRAR PEDIDOS");
        jLabel15.setToolTipText("");
        jPanel11.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 790, 60));

        pnlAdministrarPedido.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 750));

        jTabbedPane1.addTab("Administrar pedidos", pnlAdministrarPedido);

        pnlPlatillos.setBackground(new java.awt.Color(255, 240, 228));
        pnlPlatillos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(255, 240, 228));
        jPanel17.setPreferredSize(new java.awt.Dimension(1366, 790));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonNuevoPlatillo.setBackground(new java.awt.Color(102, 153, 0));
        jButtonNuevoPlatillo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonNuevoPlatillo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNuevoPlatillo.setText("Nuevo...");
        jButtonNuevoPlatillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoPlatilloActionPerformed(evt);
            }
        });
        jPanel17.add(jButtonNuevoPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 120, 40));

        jButtonEliminarPlatillo.setBackground(new java.awt.Color(153, 51, 0));
        jButtonEliminarPlatillo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEliminarPlatillo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarPlatillo.setText("Eliminar");
        jPanel17.add(jButtonEliminarPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 120, 40));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel17.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mesa", "Total", "Fecha", "Hora", "Estado"
            }
        ));
        jPanel17.add(jTable5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanelContenedorPlatillos.setBackground(new java.awt.Color(203, 166, 131));
        jPanelContenedorPlatillos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelContenedorPlatillos.setMinimumSize(new java.awt.Dimension(440, 2500));
        jPanelContenedorPlatillos.setPreferredSize(new java.awt.Dimension(440, 2000));
        jPanelContenedorPlatillos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_polloalaplancha.jpg"))); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel25.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton27.setText("Agregar");
        jPanel25.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel112.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel112.setText("Precio:");
        jPanel25.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel113.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel113.setText("Categoría:");
        jPanel25.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel114.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel114.setText("Nombre:");
        jPanel25.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel115.setText("Pollo a la plancha");
        jPanel25.add(jLabel115, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel116.setText("Plato especial");
        jPanel25.add(jLabel116, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel117.setText("S/.17.00");
        jPanel25.add(jLabel117, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton28.setText("Quitar");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel25.add(jButton28, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1820, 420, 150));

        jPanelEstofadoDePollo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelEstofadoDePollo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelImagenPlatillo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_estofadodepollo.jpg"))); // NOI18N
        jLabelImagenPlatillo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelEstofadoDePollo.add(jLabelImagenPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButtonAgregarPlatilloAMenu.setText("Agregar");
        jPanelEstofadoDePollo.add(jButtonAgregarPlatilloAMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Precio:");
        jPanelEstofadoDePollo.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel95.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel95.setText("Categoría:");
        jPanelEstofadoDePollo.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel96.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel96.setText("Nombre:");
        jPanelEstofadoDePollo.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        lblNombrePlatillo.setText("Estofado de Pollo");
        jPanelEstofadoDePollo.add(lblNombrePlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        lblCategoriaPlatillo.setText("Plato principal");
        jPanelEstofadoDePollo.add(lblCategoriaPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        lblPrecioPlatillo.setText("S/.12.00");
        jPanelEstofadoDePollo.add(lblPrecioPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButtonQuitarPlatilloDeMenu.setText("Quitar");
        jButtonQuitarPlatilloDeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarPlatilloDeMenuActionPerformed(evt);
            }
        });
        jPanelEstofadoDePollo.add(jButtonQuitarPlatilloDeMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanelEstofadoDePollo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 420, 150));

        jPanelLomoSaltado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_lomosaltado.jpg"))); // NOI18N
        jLabel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Precio:");

        jButtonQuitarLomoSaltadoDeMenu.setText("Quitar");
        jButtonQuitarLomoSaltadoDeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarLomoSaltadoDeMenuActionPerformed(evt);
            }
        });

        jLabelPrecio.setText("S/.12.00");

        jLabel90.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel90.setText("Nombre:");

        jLabelNombre.setText("Lomo Saltado");

        jLabel92.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel92.setText("Categoría:");

        jLabelCategoria.setText("Plato principal");

        jButtonAgregarLomoSaltadoAMenu.setText("Agregar");

        javax.swing.GroupLayout jPanelLomoSaltadoLayout = new javax.swing.GroupLayout(jPanelLomoSaltado);
        jPanelLomoSaltado.setLayout(jPanelLomoSaltadoLayout);
        jPanelLomoSaltadoLayout.setHorizontalGroup(
            jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                        .addGroup(jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                        .addComponent(jButtonAgregarLomoSaltadoAMenu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonQuitarLomoSaltadoDeMenu)))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        jPanelLomoSaltadoLayout.setVerticalGroup(
            jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanelLomoSaltadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButtonAgregarLomoSaltadoAMenu))
                    .addGroup(jPanelLomoSaltadoLayout.createSequentialGroup()
                        .addComponent(jLabelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabelCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabelPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonQuitarLomoSaltadoDeMenu))))
        );

        jPanelContenedorPlatillos.add(jPanelLomoSaltado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 420, 150));

        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_sopaalaminuta.jpg"))); // NOI18N
        jLabel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel29.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton23.setText("Agregar");
        jPanel29.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel100.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel100.setText("Precio:");
        jPanel29.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel101.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel101.setText("Categoría:");
        jPanel29.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel102.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel102.setText("Nombre:");
        jPanel29.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel103.setText("Sopa a la minuta");
        jPanel29.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel104.setText("Entrada");
        jPanel29.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel105.setText("S/.0.00");
        jPanel29.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton24.setText("Quitar");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel29.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 420, 150));

        jPanel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_causa.jpg"))); // NOI18N
        jLabel40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel30.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton25.setText("Agregar");
        jPanel30.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel106.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel106.setText("Precio:");
        jPanel30.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel107.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel107.setText("Categoría:");
        jPanel30.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel108.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel108.setText("Nombre:");
        jPanel30.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel109.setText("Causa");
        jPanel30.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel110.setText("Entrada");
        jPanel30.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel111.setText("S/.0.00");
        jPanel30.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton26.setText("Quitar");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 420, 150));

        jPanel31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel31.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel46.setText("Nombre:");
        jPanel31.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 60, 20));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel47.setText("Precio:");
        jPanel31.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 60, 20));

        jButton19.setText("Agregar  a Menú");
        jPanel31.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, -1, -1));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel48.setText("Lomo Saltado");
        jPanel31.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 100, 20));

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setText("S/.12.00");
        jPanel31.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 60, 20));

        jPanelContenedorPlatillos.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 420, 150));

        jPanel50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillos_tallarinesverdes.jpg"))); // NOI18N
        jLabel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel50.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton29.setText("Agregar");
        jPanel50.add(jButton29, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel118.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel118.setText("Precio:");
        jPanel50.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel119.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel119.setText("Categoría:");
        jPanel50.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel120.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel120.setText("Nombre:");
        jPanel50.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel121.setText("Tallarines verdes");
        jPanel50.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel122.setText("Plato principal");
        jPanel50.add(jLabel122, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel123.setText("S/.12.00");
        jPanel50.add(jLabel123, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton30.setText("Quitar");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel50.add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 740, 420, 150));

        jPanel51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel51.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_frejolconseco.jpg"))); // NOI18N
        jLabel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel51.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton31.setText("Agregar");
        jPanel51.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel124.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel124.setText("Precio:");
        jPanel51.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel125.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel125.setText("Categoría:");
        jPanel51.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel126.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel126.setText("Nombre:");
        jPanel51.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel127.setText("Frejoles con Seco");
        jPanel51.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel128.setText("Plato principal");
        jPanel51.add(jLabel128, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel129.setText("S/.12.00");
        jPanel51.add(jLabel129, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton32.setText("Quitar");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel51.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 920, 420, 150));

        jPanel52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_tallarinsaltado.jpg"))); // NOI18N
        jLabel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel52.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton33.setText("Agregar");
        jPanel52.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel130.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel130.setText("Precio:");
        jPanel52.add(jLabel130, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel131.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel131.setText("Categoría:");
        jPanel52.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel132.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel132.setText("Nombre:");
        jPanel52.add(jLabel132, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel133.setText("Tallarín saltado");
        jPanel52.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel134.setText("Plato principal");
        jPanel52.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel135.setText("S/.12.00");
        jPanel52.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton34.setText("Quitar");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel52.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1100, 420, 150));

        jPanel53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel53.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_papaalahuancaina.jpg"))); // NOI18N
        jLabel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel53.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton35.setText("Agregar");
        jPanel53.add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel136.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel136.setText("Precio:");
        jPanel53.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel137.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel137.setText("Categoría:");
        jPanel53.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel138.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel138.setText("Nombre:");
        jPanel53.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel139.setText("Papa a la huancaína");
        jPanel53.add(jLabel139, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel140.setText("Entrada");
        jPanel53.add(jLabel140, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel141.setText("S/.0.00");
        jPanel53.add(jLabel141, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton36.setText("Quitar");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel53.add(jButton36, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1280, 420, 150));

        jPanel54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel54.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_ensaladarusa.jpg"))); // NOI18N
        jLabel31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel54.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton37.setText("Agregar");
        jPanel54.add(jButton37, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel142.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel142.setText("Precio:");
        jPanel54.add(jLabel142, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel143.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel143.setText("Categoría:");
        jPanel54.add(jLabel143, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel144.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel144.setText("Nombre:");
        jPanel54.add(jLabel144, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel145.setText("Ensalada rusa con Pollo al Horno");
        jPanel54.add(jLabel145, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel146.setText("Plato principal");
        jPanel54.add(jLabel146, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel147.setText("S/.12.00");
        jPanel54.add(jLabel147, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton38.setText("Quitar");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton38, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1460, 420, 150));

        jPanel55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel55.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel148.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/platillos/platillo_costillardorado.jpg"))); // NOI18N
        jLabel148.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel55.add(jLabel148, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 108));

        jButton39.setText("Agregar");
        jPanel55.add(jButton39, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jLabel149.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel149.setText("Precio:");
        jPanel55.add(jLabel149, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 60, 20));

        jLabel150.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel150.setText("Categoría:");
        jPanel55.add(jLabel150, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 80, 20));

        jLabel151.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel151.setText("Nombre:");
        jPanel55.add(jLabel151, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 60, 20));

        jLabel152.setText("Costillar dorado");
        jPanel55.add(jLabel152, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 100, 20));

        jLabel153.setText("Plato especial");
        jPanel55.add(jLabel153, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 100, 20));

        jLabel154.setText("S/.17.00");
        jPanel55.add(jLabel154, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 60, 20));

        jButton40.setText("Quitar");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel55.add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, -1, -1));

        jPanelContenedorPlatillos.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1640, 420, 150));

        jScrollPanePlatillos.setViewportView(jPanelContenedorPlatillos);

        jPanel17.add(jScrollPanePlatillos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 470, 510));

        jTableMenuDia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTableMenuDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Platillo", "Categoría", "Precio"
            }
        ));
        jScrollPane4.setViewportView(jTableMenuDia);

        jPanel17.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, -1, 510));

        jLabel94.setBackground(new java.awt.Color(255, 255, 255));
        jLabel94.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(151, 114, 78));
        jLabel94.setText("MENÚ DEL DÍA");
        jPanel17.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 130, 360, 50));

        jButtonModificarPlatillo.setBackground(new java.awt.Color(153, 153, 0));
        jButtonModificarPlatillo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonModificarPlatillo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificarPlatillo.setText("Modificar");
        jButtonModificarPlatillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarPlatilloActionPerformed(evt);
            }
        });
        jPanel17.add(jButtonModificarPlatillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 120, 40));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 68)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(136, 92, 49));
        jLabel17.setText("GESTIÓN DE PLATILLOS");
        jLabel17.setToolTipText("");
        jPanel17.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 770, 60));

        pnlPlatillos.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 760));

        jTabbedPane1.addTab("Platillos", pnlPlatillos);

        pnlEmpleados.setBackground(new java.awt.Color(255, 240, 228));

        jPanel56.setBackground(new java.awt.Color(209, 188, 172));
        jPanel56.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldNombreEmpledo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldNombreEmpledo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreEmpledoActionPerformed(evt);
            }
        });
        jPanel56.add(jTextFieldNombreEmpledo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 133, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Nombres:");
        jPanel56.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel22.setText("Apellidos:");
        jPanel56.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jTextFieldApellidoEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldApellidoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 133, -1));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel23.setText("DNI:");
        jPanel56.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        jTextFieldDNIEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldDNIEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 133, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel24.setText("Sexo:");
        jPanel56.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 60, -1));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setText("Fecha Nac.:");
        jPanel56.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jTextFieldFechaNacimientoEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldFechaNacimientoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaNacimientoEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jTextFieldFechaNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 133, -1));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(136, 92, 49));
        jLabel36.setText("REGISTRO DE EMPLEADOS");
        jPanel56.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 616, 65));

        jRadioButtonMasculinoEmpleado.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jRadioButtonMasculinoEmpleado.setText("Masculino");
        jRadioButtonMasculinoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMasculinoEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jRadioButtonMasculinoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, -1, -1));

        jRadioButtonFemeninoEmpleado.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jRadioButtonFemeninoEmpleado.setText("Femenino");
        jRadioButtonFemeninoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonFemeninoEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jRadioButtonFemeninoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, -1, -1));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/empleados_restaurante.png"))); // NOI18N
        jPanel56.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 470, 318));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setText("Cargo:");
        jPanel56.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        jTextFieldCargoEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldCargoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCargoEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jTextFieldCargoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 133, -1));

        jLabelIDEmpleado.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelIDEmpleado.setText("ID:");
        jPanel56.add(jLabelIDEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setText("Horario:");
        jPanel56.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, -1, -1));

        jTextFieldHorarioEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldHorarioEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHorarioEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jTextFieldHorarioEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 320, 133, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel42.setText("Teléfono:");
        jPanel56.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, -1, -1));

        jTextFieldTelefonoEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldTelefonoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 133, -1));

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setText("Usario:");
        jPanel56.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, -1, -1));

        jTextFieldUsuarioEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldUsuarioEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 240, 133, -1));

        jLabel44.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel44.setText("Correo:");
        jPanel56.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, -1));

        jTextFieldCorreoEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldCorreoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 200, 133, -1));

        jLabel155.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel155.setText("Contraseña:");
        jPanel56.add(jLabel155, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 280, -1, -1));

        jTextFieldContraseñaEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel56.add(jTextFieldContraseñaEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 133, -1));

        jLabel156.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel156.setText("Dirección:");
        jPanel56.add(jLabel156, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, -1, -1));

        jTextFieldDireccionEmpleado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldDireccionEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDireccionEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jTextFieldDireccionEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 133, -1));

        jButtonNuevoEmpleado.setBackground(new java.awt.Color(102, 153, 0));
        jButtonNuevoEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonNuevoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNuevoEmpleado.setText("Nuevo...");
        jPanel56.add(jButtonNuevoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 120, 40));

        jButtonModificarEmpleado.setBackground(new java.awt.Color(153, 153, 0));
        jButtonModificarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonModificarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificarEmpleado.setText("Modificar");
        jButtonModificarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarEmpleadoActionPerformed(evt);
            }
        });
        jPanel56.add(jButtonModificarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 120, 40));

        jButtonEliminarEmpleado.setBackground(new java.awt.Color(153, 51, 0));
        jButtonEliminarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEliminarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarEmpleado.setText("Eliminar");
        jPanel56.add(jButtonEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 380, 120, 40));

        jTableEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombres", "Apellidos", "DNI", "Sexo", "Fecha de nacimiento", "Cargo", "Dirección", "Teléfono", "Correo", "Usuario", "Contraseña", "Horario"
            }
        ));
        jScrollPane5.setViewportView(jTableEmpleados);
        if (jTableEmpleados.getColumnModel().getColumnCount() > 0) {
            jTableEmpleados.getColumnModel().getColumn(0).setHeaderValue("Nombres");
            jTableEmpleados.getColumnModel().getColumn(1).setHeaderValue("Apellidos");
            jTableEmpleados.getColumnModel().getColumn(2).setResizable(false);
            jTableEmpleados.getColumnModel().getColumn(2).setHeaderValue("DNI");
            jTableEmpleados.getColumnModel().getColumn(3).setHeaderValue("Sexo");
            jTableEmpleados.getColumnModel().getColumn(4).setHeaderValue("Fecha de nacimiento");
            jTableEmpleados.getColumnModel().getColumn(5).setHeaderValue("Cargo");
            jTableEmpleados.getColumnModel().getColumn(6).setHeaderValue("Dirección");
            jTableEmpleados.getColumnModel().getColumn(7).setHeaderValue("Teléfono");
            jTableEmpleados.getColumnModel().getColumn(8).setHeaderValue("Correo");
            jTableEmpleados.getColumnModel().getColumn(9).setHeaderValue("Usuario");
            jTableEmpleados.getColumnModel().getColumn(10).setHeaderValue("Contraseña");
            jTableEmpleados.getColumnModel().getColumn(11).setHeaderValue("Horario");
        }

        javax.swing.GroupLayout pnlEmpleadosLayout = new javax.swing.GroupLayout(pnlEmpleados);
        pnlEmpleados.setLayout(pnlEmpleadosLayout);
        pnlEmpleadosLayout.setHorizontalGroup(
            pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5)
                    .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        pnlEmpleadosLayout.setVerticalGroup(
            pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Empleados", pnlEmpleados);

        pnlClientes.setBackground(new java.awt.Color(255, 240, 228));
        pnlClientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel57.setBackground(new java.awt.Color(209, 188, 172));
        jPanel57.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldNombreCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreClienteActionPerformed(evt);
            }
        });
        jPanel57.add(jTextFieldNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 140, -1));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel19.setText("Nombres:");
        jPanel57.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel157.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel157.setText("Apellidos:");
        jPanel57.add(jLabel157, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        jTextFieldApellidoCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldApellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldApellidoClienteActionPerformed(evt);
            }
        });
        jPanel57.add(jTextFieldApellidoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 140, -1));

        jLabel158.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel158.setText("DNI:");
        jPanel57.add(jLabel158, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jTextFieldDNICliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel57.add(jTextFieldDNICliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 140, -1));

        jLabel161.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(136, 92, 49));
        jLabel161.setText("REGISTRO DE CLIENTES");
        jPanel57.add(jLabel161, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 616, 65));

        jLabel162.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cliente_restaurante.png"))); // NOI18N
        jPanel57.add(jLabel162, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 80, 400, 318));

        jLabelIDCliente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelIDCliente.setText("ID:");
        jPanel57.add(jLabelIDCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, -1, -1));

        jLabel166.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel166.setText("Teléfono:");
        jPanel57.add(jLabel166, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, -1));

        jTextFieldTelefonoCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel57.add(jTextFieldTelefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 220, 133, -1));

        jLabel168.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel168.setText("Correo:");
        jPanel57.add(jLabel168, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 280, -1, -1));

        jTextFieldCorreoCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel57.add(jTextFieldCorreoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 133, -1));

        jLabel170.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel170.setText("Dirección:");
        jPanel57.add(jLabel170, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, -1, -1));

        jTextFieldDireccionCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDireccionClienteActionPerformed(evt);
            }
        });
        jPanel57.add(jTextFieldDireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, 133, -1));

        jButtonNuevoCliente.setBackground(new java.awt.Color(102, 153, 0));
        jButtonNuevoCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonNuevoCliente.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNuevoCliente.setText("Nuevo...");
        jPanel57.add(jButtonNuevoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 120, 40));

        jButtonModificarCliente.setBackground(new java.awt.Color(153, 153, 0));
        jButtonModificarCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonModificarCliente.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificarCliente.setText("Modificar");
        jButtonModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarClienteActionPerformed(evt);
            }
        });
        jPanel57.add(jButtonModificarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, 120, 40));

        jButtonEliminarCliente.setBackground(new java.awt.Color(153, 51, 0));
        jButtonEliminarCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEliminarCliente.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarCliente.setText("Eliminar");
        jPanel57.add(jButtonEliminarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 350, 120, 40));

        pnlClientes.add(jPanel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1060, 420));

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombres", "Apellidos", "Dirección", "Teléfono", "Correo", "Sexo"
            }
        ));
        jScrollPane8.setViewportView(jTableClientes);

        pnlClientes.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 1060, 300));

        jTabbedPane1.addTab("Clientes", pnlClientes);

        pnlInventario.setBackground(new java.awt.Color(255, 240, 228));
        pnlInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel58.setBackground(new java.awt.Color(209, 188, 172));
        jPanel58.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldCodigoInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldCodigoInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoInsumoActionPerformed(evt);
            }
        });
        jPanel58.add(jTextFieldCodigoInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 140, -1));

        jLabel160.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel160.setText("Código:");
        jPanel58.add(jLabel160, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel163.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel163.setText("Nombre:");
        jPanel58.add(jLabel163, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        jTextFieldNombreInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldNombreInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreInsumoActionPerformed(evt);
            }
        });
        jPanel58.add(jTextFieldNombreInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 140, -1));

        jLabel165.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel165.setText("Categoría:");
        jPanel58.add(jLabel165, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, -1, -1));

        jTextFieldCategoriaInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel58.add(jTextFieldCategoriaInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 140, -1));

        jLabel169.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel169.setForeground(new java.awt.Color(136, 92, 49));
        jLabel169.setText("INVENTARIO DE INSUMOS");
        jPanel58.add(jLabel169, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 616, 65));

        jLabel171.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/insumos_restaurante.png"))); // NOI18N
        jPanel58.add(jLabel171, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 400, 260));

        jLabelIDInsumos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelIDInsumos.setText("ID:");
        jPanel58.add(jLabelIDInsumos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, -1, -1));

        jLabel173.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jPanel58.add(jLabel173, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, -1));

        jTextFieldAlmacenInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel58.add(jTextFieldAlmacenInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 230, 133, -1));

        jLabel174.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel174.setText("Proveedor:");
        jPanel58.add(jLabel174, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, -1, -1));

        jTextFieldProveedorInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel58.add(jTextFieldProveedorInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 300, 133, -1));

        jLabel175.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel175.setText("Almacén:");
        jPanel58.add(jLabel175, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, -1, -1));

        jTextFieldStockInsumo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextFieldStockInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldStockInsumoActionPerformed(evt);
            }
        });
        jPanel58.add(jTextFieldStockInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, 133, -1));

        jLabel176.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel176.setText("Stock:");
        jPanel58.add(jLabel176, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, -1, -1));

        jButtonEliminarInsumo.setBackground(new java.awt.Color(153, 51, 0));
        jButtonEliminarInsumo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEliminarInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarInsumo.setText("Eliminar");
        jPanel58.add(jButtonEliminarInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 360, 120, 40));

        jButtonModificarInsumo.setBackground(new java.awt.Color(153, 153, 0));
        jButtonModificarInsumo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonModificarInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificarInsumo.setText("Modificar");
        jButtonModificarInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarInsumoActionPerformed(evt);
            }
        });
        jPanel58.add(jButtonModificarInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 120, 40));

        jButtonNuevoInsumo.setBackground(new java.awt.Color(102, 153, 0));
        jButtonNuevoInsumo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonNuevoInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNuevoInsumo.setText("Nuevo...");
        jPanel58.add(jButtonNuevoInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, 120, 40));

        pnlInventario.add(jPanel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1060, 430));

        jTableInsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Código", "Nombre", "Categoría", "Stock", "Almacén", "Proveedor"
            }
        ));
        jScrollPane9.setViewportView(jTableInsumos);

        pnlInventario.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 1060, 280));

        jTabbedPane1.addTab("Inventario", pnlInventario);

        pnlReportes.setBackground(new java.awt.Color(255, 240, 228));
        pnlReportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelButtonCaja.setBackground(new java.awt.Color(204, 102, 0));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icos/ventas.png"))); // NOI18N
        jLabel5.setText("CAJA");

        javax.swing.GroupLayout jPanelButtonCajaLayout = new javax.swing.GroupLayout(jPanelButtonCaja);
        jPanelButtonCaja.setLayout(jPanelButtonCajaLayout);
        jPanelButtonCajaLayout.setHorizontalGroup(
            jPanelButtonCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonCajaLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanelButtonCajaLayout.setVerticalGroup(
            jPanelButtonCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonCajaLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pnlReportes.add(jPanelButtonCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, -1, -1));

        jPanel12.setBackground(new java.awt.Color(209, 188, 172));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTableReportes);

        jPanel12.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 930, 430));
        jPanel12.add(jTextFieldBuscarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 400, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Buscar:");
        jPanel12.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 37, -1, -1));

        jPanel12.add(jComboBoxFiltroReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 100, 30));

        pnlReportes.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 1020, 540));

        jPanelButtonPlatillos.setBackground(new java.awt.Color(204, 102, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icos/reportes (1).png"))); // NOI18N
        jLabel3.setText("PLATILLOS");

        javax.swing.GroupLayout jPanelButtonPlatillosLayout = new javax.swing.GroupLayout(jPanelButtonPlatillos);
        jPanelButtonPlatillos.setLayout(jPanelButtonPlatillosLayout);
        jPanelButtonPlatillosLayout.setHorizontalGroup(
            jPanelButtonPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonPlatillosLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanelButtonPlatillosLayout.setVerticalGroup(
            jPanelButtonPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonPlatillosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlReportes.add(jPanelButtonPlatillos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jPanelButtonInventario.setBackground(new java.awt.Color(204, 102, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icos/clientes.png"))); // NOI18N
        jLabel4.setText("INVENTARIO");

        javax.swing.GroupLayout jPanelButtonInventarioLayout = new javax.swing.GroupLayout(jPanelButtonInventario);
        jPanelButtonInventario.setLayout(jPanelButtonInventarioLayout);
        jPanelButtonInventarioLayout.setHorizontalGroup(
            jPanelButtonInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonInventarioLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanelButtonInventarioLayout.setVerticalGroup(
            jPanelButtonInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonInventarioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlReportes.add(jPanelButtonInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        jPanel15.setBackground(new java.awt.Color(204, 102, 0));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("IMPRIMIR");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlReportes.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 680, 180, 50));

        jTabbedPane1.addTab("Reportes", pnlReportes);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 1090, 790));

        jPanel2.setBackground(new java.awt.Color(210, 166, 129));
        jPanel2.setForeground(new java.awt.Color(153, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(175, 73, 16));

        jLabel20.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("SALIR");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jLabel20)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 270, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo_restaurante.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 350));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 790));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldStockInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldStockInsumoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldStockInsumoActionPerformed

    private void jTextFieldNombreInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreInsumoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNombreInsumoActionPerformed

    private void jTextFieldCodigoInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoInsumoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCodigoInsumoActionPerformed

    private void jTextFieldDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDireccionClienteActionPerformed

    private void jTextFieldApellidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldApellidoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldApellidoClienteActionPerformed

    private void jTextFieldNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNombreClienteActionPerformed

    private void jButtonModificarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonModificarEmpleadoActionPerformed

    private void jTextFieldDireccionEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDireccionEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDireccionEmpleadoActionPerformed

    private void jTextFieldHorarioEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHorarioEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldHorarioEmpleadoActionPerformed

    private void jTextFieldCargoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCargoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCargoEmpleadoActionPerformed

    private void jRadioButtonFemeninoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonFemeninoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonFemeninoEmpleadoActionPerformed

    private void jRadioButtonMasculinoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMasculinoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonMasculinoEmpleadoActionPerformed

    private void jTextFieldFechaNacimientoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaNacimientoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaNacimientoEmpleadoActionPerformed

    private void jTextFieldNombreEmpledoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreEmpledoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNombreEmpledoActionPerformed

    private void jButtonModificarPlatilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarPlatilloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonModificarPlatilloActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButtonQuitarLomoSaltadoDeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarLomoSaltadoDeMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonQuitarLomoSaltadoDeMenuActionPerformed

    private void jButtonQuitarPlatilloDeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarPlatilloDeMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonQuitarPlatilloDeMenuActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jTextFieldBuscarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBuscarPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBuscarPedidoActionPerformed

    private void btnPedidoEnProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedidoEnProcesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPedidoEnProcesoActionPerformed

    private void jButtonModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonModificarClienteActionPerformed

    private void jButtonModificarInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarInsumoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonModificarInsumoActionPerformed

    private void jButtonNuevoPlatilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoPlatilloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNuevoPlatilloActionPerformed

    private void btnImprimirVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirVoucherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirVoucherActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SystemView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImprimirVoucher;
    private javax.swing.JButton btnPedidoCompletado;
    private javax.swing.JButton btnPedidoEnProceso;
    public javax.swing.JComboBox<String> cmbMesas;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonAgregarLomoSaltadoAMenu;
    private javax.swing.JButton jButtonAgregarPlatilloAMenu;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonEliminarCliente;
    private javax.swing.JButton jButtonEliminarEmpleado;
    private javax.swing.JButton jButtonEliminarInsumo;
    private javax.swing.JButton jButtonEliminarPlatillo;
    private javax.swing.JButton jButtonModificarCliente;
    private javax.swing.JButton jButtonModificarEmpleado;
    private javax.swing.JButton jButtonModificarInsumo;
    private javax.swing.JButton jButtonModificarPlatillo;
    private javax.swing.JButton jButtonNuevoCliente;
    private javax.swing.JButton jButtonNuevoEmpleado;
    private javax.swing.JButton jButtonNuevoInsumo;
    private javax.swing.JButton jButtonNuevoPlatillo;
    private javax.swing.JButton jButtonQuitarLomoSaltadoDeMenu;
    private javax.swing.JButton jButtonQuitarPlatilloDeMenu;
    private javax.swing.JButton jButtonVerDetalle;
    private javax.swing.JComboBox<String> jComboBoxFiltroReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelIDCliente;
    private javax.swing.JLabel jLabelIDEmpleado;
    private javax.swing.JLabel jLabelIDInsumos;
    private javax.swing.JLabel jLabelImagenPlatillo;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanelButtonCaja;
    private javax.swing.JPanel jPanelButtonInventario;
    private javax.swing.JPanel jPanelButtonPlatillos;
    private javax.swing.JPanel jPanelContenedorPlatillos;
    private javax.swing.JPanel jPanelEstofadoDePollo;
    private javax.swing.JPanel jPanelLomoSaltado;
    private javax.swing.JRadioButton jRadioButtonFemeninoEmpleado;
    private javax.swing.JRadioButton jRadioButtonMasculinoEmpleado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPanePlatillos;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTableAdministrarPedidos;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableEmpleados;
    private javax.swing.JTable jTableInsumos;
    private javax.swing.JTable jTableMenuDelDia;
    private javax.swing.JTable jTableMenuDia;
    private javax.swing.JTable jTablePedidoActual;
    private javax.swing.JTable jTableReportes;
    private javax.swing.JTextField jTextFieldAlmacenInsumo;
    private javax.swing.JTextField jTextFieldApellidoCliente;
    private javax.swing.JTextField jTextFieldApellidoEmpleado;
    private javax.swing.JTextField jTextFieldBuscarPedido;
    private javax.swing.JTextField jTextFieldBuscarReporte;
    private javax.swing.JTextField jTextFieldCantidad;
    private javax.swing.JTextField jTextFieldCargoEmpleado;
    private javax.swing.JTextField jTextFieldCategoriaInsumo;
    private javax.swing.JTextField jTextFieldCodigoInsumo;
    private javax.swing.JTextField jTextFieldContraseñaEmpleado;
    private javax.swing.JTextField jTextFieldCorreoCliente;
    private javax.swing.JTextField jTextFieldCorreoEmpleado;
    private javax.swing.JTextField jTextFieldDNICliente;
    private javax.swing.JTextField jTextFieldDNIEmpleado;
    private javax.swing.JTextField jTextFieldDireccionCliente;
    private javax.swing.JTextField jTextFieldDireccionEmpleado;
    private javax.swing.JTextField jTextFieldFechaNacimientoEmpleado;
    private javax.swing.JTextField jTextFieldHorarioEmpleado;
    private javax.swing.JTextField jTextFieldNombreCliente;
    private javax.swing.JTextField jTextFieldNombreEmpledo;
    private javax.swing.JTextField jTextFieldNombreInsumo;
    private javax.swing.JTextField jTextFieldProveedorInsumo;
    private javax.swing.JTextField jTextFieldStockInsumo;
    private javax.swing.JTextField jTextFieldTelefonoCliente;
    private javax.swing.JTextField jTextFieldTelefonoEmpleado;
    private javax.swing.JTextField jTextFieldUsuarioEmpleado;
    private javax.swing.JLabel lblCategoriaPlatillo;
    private javax.swing.JLabel lblIGV;
    private javax.swing.JLabel lblNombrePlatillo;
    private javax.swing.JLabel lblPrecioPlatillo;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JPanel pnlAdministrarPedido;
    private javax.swing.JPanel pnlClientes;
    private javax.swing.JPanel pnlContenedorMesas;
    private javax.swing.JPanel pnlEmpleados;
    public javax.swing.JPanel pnlGenerarPedido;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlMesas;
    private javax.swing.JPanel pnlPlatillos;
    private javax.swing.JPanel pnlReportes;
    // End of variables declaration//GEN-END:variables
}
