package controllers;

import dao.PedidoDAO;
import dao.PlatilloDAO;
import dao.InsumoDAO;
import models.Pedido;
import models.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import conexion.ConexionSQL;
import com.toedter.calendar.JDateChooser;

/**
 * Controlador para gestionar el panel de Reportes con búsqueda y filtros
 */
public class ReportesController {
    
    private PedidoDAO pedidoDAO;
    private PlatilloDAO platilloDAO;
    private InsumoDAO insumoDAO;
    
    // Componentes de la vista
    private JTable tblReportes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltro;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFin;
    private JPanel pnlFiltroFechas; // Panel que contiene los dateChoosers
    
    // Tipo de reporte actual
    private enum TipoReporte {
        PLATILLOS, INVENTARIO, CAJA
    }
    private TipoReporte tipoActual;
    
    public ReportesController() {
        this.pedidoDAO = new PedidoDAO();
        this.platilloDAO = new PlatilloDAO();
        this.insumoDAO = new InsumoDAO();
    }
    
    /**
     * Inicializar componentes
     */
    public void inicializarComponentes(
        JTable tblReportes,
        JTextField txtBuscar,
        JComboBox<String> cmbFiltro
    ) {
        this.tblReportes = tblReportes;
        this.txtBuscar = txtBuscar;
        this.cmbFiltro = cmbFiltro;
        
        configurarTablaInicial();
        mostrarReportePlatillos();
    }
    
    /**
     * Configurar tabla inicial
     */
    private void configurarTablaInicial() {
        modeloTabla = new DefaultTableModel(new Object[]{"Cargando..."}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
    }
    
    /**
     * Mostrar reporte de platillos más vendidos
     */
    public void mostrarReportePlatillos() {
        tipoActual = TipoReporte.PLATILLOS;
        
        // Configurar combo de filtros
        cmbFiltro.removeAllItems();
        cmbFiltro.addItem("Todas las Categorías");
        cmbFiltro.addItem("Bebidas");
        cmbFiltro.addItem("Entradas");
        cmbFiltro.addItem("Platos Fuertes");
        cmbFiltro.addItem("Postres");
        
        // Limpiar búsqueda
        txtBuscar.setText("");
        
        cargarDatosPlatillos(null, null);
    }
    
    /**
     * Cargar datos de platillos con filtros
     */
    private void cargarDatosPlatillos(String busqueda, String categoria) {
        modeloTabla = new DefaultTableModel(
            new Object[]{"Posición", "Platillo", "Categoría", "Veces Vendido", "Total Vendido"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.nombre AS platillo, c.nombre AS categoria, ");
        sql.append("COUNT(dp.id_detalle) AS veces_vendido, ");
        sql.append("SUM(dp.cantidad) AS cantidad_total, ");
        sql.append("SUM(dp.subtotal) AS total_vendido ");
        sql.append("FROM DetallePedido dp ");
        sql.append("INNER JOIN Platillos p ON dp.id_platillo = p.id_platillo ");
        sql.append("LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria ");
        sql.append("WHERE 1=1 ");
        
        // Filtro por búsqueda
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append("AND p.nombre LIKE ? ");
        }
        
        // Filtro por categoría
        if (categoria != null && !categoria.equals("Todas las Categorías")) {
            sql.append("AND c.nombre = ? ");
        }
        
        sql.append("GROUP BY p.id_platillo, p.nombre, c.nombre ");
        sql.append("ORDER BY veces_vendido DESC, total_vendido DESC");
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + busqueda + "%");
            }
            if (categoria != null && !categoria.equals("Todas las Categorías")) {
                ps.setString(paramIndex++, categoria);
            }
            
            ResultSet rs = ps.executeQuery();
            int posicion = 1;
            
            while (rs.next()) {
                String emoji = obtenerEmoji(posicion);
                modeloTabla.addRow(new Object[]{
                    emoji + " " + posicion,
                    rs.getString("platillo"),
                    rs.getString("categoria"),
                    rs.getInt("veces_vendido") + " veces",
                    String.format("S/. %.2f", rs.getDouble("total_vendido"))
                });
                posicion++;
            }
            
            if (modeloTabla.getRowCount() == 0) {
                modeloTabla.addRow(new Object[]{"Sin resultados", "No se encontraron platillos", "-", "-", "-"});
            }
            
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de platillos: " + e.getMessage());
            modeloTabla.addRow(new Object[]{"Error", "No se pudo cargar el reporte", "-", "-", "-"});
        }
        
        ajustarAnchoColumnas(new int[]{80, 250, 150, 120, 120});
    }
    
    /**
     * Mostrar reporte de inventario
     */
    public void mostrarReporteInventario() {
        tipoActual = TipoReporte.INVENTARIO;
        
        // Configurar combo de filtros
        cmbFiltro.removeAllItems();
        cmbFiltro.addItem("Buscar por: Nombre");
        cmbFiltro.addItem("Buscar por: Categoría");
        cmbFiltro.addItem("Buscar por: Stock Máximo");
        cmbFiltro.addItem("Buscar por: Almacén");
        cmbFiltro.addItem("Buscar por: Proveedor");
        
        txtBuscar.setText("");
        
        cargarDatosInventario(null, "Nombre");
    }
    
    /**
     * Cargar datos de inventario con filtros
     */
    private void cargarDatosInventario(String busqueda, String tipoBusqueda) {
        modeloTabla = new DefaultTableModel(
            new Object[]{"Código", "Insumo", "Categoría", "Stock", "Estado", "Almacén", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.codigo, i.nombre, c.nombre AS categoria, i.stock, ");
        sql.append("i.almacen, i.proveedor ");
        sql.append("FROM InventarioInsumos i ");
        sql.append("LEFT JOIN Categorias c ON i.id_categoria = c.id_categoria ");
        sql.append("WHERE 1=1 ");
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            switch (tipoBusqueda) {
                case "Nombre":
                case "Buscar por: Nombre":
                    sql.append("AND i.nombre LIKE ? ");
                    break;
                case "Categoría":
                case "Buscar por: Categoría":
                    sql.append("AND c.nombre LIKE ? ");
                    break;
                case "Stock Máximo":
                case "Buscar por: Stock Máximo":
                    sql.append("AND i.stock <= ? ");
                    break;
                case "Almacén":
                case "Buscar por: Almacén":
                    sql.append("AND i.almacen LIKE ? ");
                    break;
                case "Proveedor":
                case "Buscar por: Proveedor":
                    sql.append("AND i.proveedor LIKE ? ");
                    break;
            }
        }
        
        sql.append("ORDER BY i.stock ASC, i.nombre");
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                if (tipoBusqueda.contains("Stock Máximo")) {
                    try {
                        ps.setInt(1, Integer.parseInt(busqueda));
                    } catch (NumberFormatException e) {
                        ps.setInt(1, 999999);
                    }
                } else {
                    ps.setString(1, "%" + busqueda + "%");
                }
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int stock = rs.getInt("stock");
                String estado;
                if (stock == 0) {
                    estado = "❌ SIN STOCK";
                } else if (stock < 10) {
                    estado = "⚠️ STOCK BAJO";
                } else if (stock < 30) {
                    estado = "🟡 STOCK MEDIO";
                } else {
                    estado = "✅ STOCK OK";
                }
                
                modeloTabla.addRow(new Object[]{
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    stock + " unidades",
                    estado,
                    rs.getString("almacen"),
                    rs.getString("proveedor")
                });
            }
            
            if (modeloTabla.getRowCount() == 0) {
                modeloTabla.addRow(new Object[]{"Sin resultados", "No se encontraron insumos", "-", "-", "-", "-", "-"});
            }
            
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de inventario: " + e.getMessage());
        }
        
        ajustarAnchoColumnas(new int[]{80, 200, 120, 100, 120, 120, 150});
    }
    
    /**
     * Mostrar reporte de caja
     */
    public void mostrarReporteCaja() {
        tipoActual = TipoReporte.CAJA;
        
        // Configurar combo de filtros
        cmbFiltro.removeAllItems();
        cmbFiltro.addItem("Filtrar por: Fecha");
        
        txtBuscar.setText("");
        txtBuscar.setToolTipText("Buscar por número de mesa");
        
        cargarDatosCaja(null, null, null);
    }
    
    /**
     * Cargar datos de caja con filtros
     */
    private void cargarDatosCaja(String busquedaMesa, LocalDate fechaInicio, LocalDate fechaFin) {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID Pedido", "Mesa", "Fecha", "Hora", "Subtotal", "IGV", "Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id_pedido, m.numero_mesa, p.fecha, p.hora, ");
        sql.append("p.subtotal, p.igv, p.total, p.estado ");
        sql.append("FROM Pedidos p ");
        sql.append("INNER JOIN Mesas m ON p.id_mesa = m.id_mesa ");
        sql.append("WHERE 1=1 ");
        
        // Filtro por mesa
        if (busquedaMesa != null && !busquedaMesa.trim().isEmpty()) {
            sql.append("AND m.numero_mesa = ? ");
        }
        
        // Filtro por fechas
        if (fechaInicio != null) {
            sql.append("AND p.fecha >= ? ");
        }
        if (fechaFin != null) {
            sql.append("AND p.fecha <= ? ");
        }
        
        sql.append("ORDER BY p.fecha DESC, p.hora DESC");
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (busquedaMesa != null && !busquedaMesa.trim().isEmpty()) {
                try {
                    ps.setInt(paramIndex++, Integer.parseInt(busquedaMesa));
                } catch (NumberFormatException e) {
                    ps.setInt(paramIndex++, -1);
                }
            }
            if (fechaInicio != null) {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(fechaInicio));
            }
            if (fechaFin != null) {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(fechaFin));
            }
            
            ResultSet rs = ps.executeQuery();
            
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            
            double totalSubtotal = 0;
            double totalIGV = 0;
            double totalGeneral = 0;
            
            while (rs.next()) {
                double subtotal = rs.getDouble("subtotal");
                double igv = rs.getDouble("igv");
                double total = rs.getDouble("total");
                
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id_pedido"),
                    "Mesa " + rs.getInt("numero_mesa"),
                    rs.getDate("fecha").toLocalDate().format(formatoFecha),
                    rs.getTime("hora").toLocalTime().format(formatoHora),
                    String.format("S/. %.2f", subtotal),
                    String.format("S/. %.2f", igv),
                    String.format("S/. %.2f", total),
                    rs.getString("estado")
                });
                
                totalSubtotal += subtotal;
                totalIGV += igv;
                totalGeneral += total;
            }
            
            if (modeloTabla.getRowCount() > 0) {
                modeloTabla.addRow(new Object[]{
                    "", "", "", "TOTALES:",
                    String.format("S/. %.2f", totalSubtotal),
                    String.format("S/. %.2f", totalIGV),
                    String.format("S/. %.2f", totalGeneral),
                    ""
                });
            } else {
                modeloTabla.addRow(new Object[]{"Sin resultados", "No se encontraron pedidos", "-", "-", "-", "-", "-", "-"});
            }
            
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de caja: " + e.getMessage());
        }
        
        ajustarAnchoColumnas(new int[]{80, 100, 100, 80, 100, 100, 100, 100});
    }
    
    /**
     * Buscar según tipo de reporte actual
     */
    public void buscar() {
        String textoBusqueda = txtBuscar.getText().trim();
        
        switch (tipoActual) {
            case PLATILLOS:
                String categoria = (String) cmbFiltro.getSelectedItem();
                cargarDatosPlatillos(textoBusqueda, categoria);
                break;
                
            case INVENTARIO:
                String tipoBusqueda = (String) cmbFiltro.getSelectedItem();
                cargarDatosInventario(textoBusqueda, tipoBusqueda);
                break;
                
            case CAJA:
                cargarDatosCaja(textoBusqueda, null, null);
                break;
        }
    }
    
    /**
     * Filtrar por fechas (solo para CAJA)
     */
    public void filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (tipoActual == TipoReporte.CAJA) {
            String busquedaMesa = txtBuscar.getText().trim();
            busquedaMesa = busquedaMesa.isEmpty() ? null : busquedaMesa;
            cargarDatosCaja(busquedaMesa, fechaInicio, fechaFin);
        }
    }
    
    /**
     * Imprimir reporte actual
     */
    public void imprimirReporte(JComponent parent) {
        String tipoReporte = "";
        switch (tipoActual) {
            case PLATILLOS:
                tipoReporte = "Platillos Más Vendidos";
                break;
            case INVENTARIO:
                tipoReporte = "Inventario de Insumos";
                break;
            case CAJA:
                tipoReporte = "Reporte de Caja";
                break;
        }
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("╔════════════════════════════════════════════════════╗\n");
        reporte.append("║          RESTAURANTE MERY - REPORTES              ║\n");
        reporte.append("╠════════════════════════════════════════════════════╣\n");
        reporte.append("║  Tipo: ").append(tipoReporte).append("\n");
        reporte.append("║  Fecha: ").append(LocalDate.now()).append("\n");
        reporte.append("╚════════════════════════════════════════════════════╝\n\n");
        
        for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
            reporte.append(modeloTabla.getColumnName(col)).append("\t");
        }
        reporte.append("\n");
        reporte.append("─".repeat(80)).append("\n");
        
        for (int row = 0; row < modeloTabla.getRowCount(); row++) {
            for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
                reporte.append(modeloTabla.getValueAt(row, col)).append("\t");
            }
            reporte.append("\n");
        }
        
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(700, 500));
        
        int opcion = JOptionPane.showConfirmDialog(parent, scrollPane,
            "Vista Previa - " + tipoReporte,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(parent,
                "Reporte listo para imprimir.\n(Función de impresión física en desarrollo)",
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private String obtenerEmoji(int posicion) {
        switch (posicion) {
            case 1: return "🥇";
            case 2: return "🥈";
            case 3: return "🥉";
            default: return "  ";
        }
    }
    
    private void ajustarAnchoColumnas(int[] anchos) {
        for (int i = 0; i < anchos.length && i < tblReportes.getColumnCount(); i++) {
            tblReportes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}