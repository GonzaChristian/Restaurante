package controllers;

import dao.PedidoDAO;
import dao.PlatilloDAO;
import dao.InsumoDAO;
import models.Pedido;
import models.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import conexion.ConexionSQL;

/**
 * Controlador para gestionar el panel de Reportes
 */
public class ReportesController {
    
    private PedidoDAO pedidoDAO;
    private PlatilloDAO platilloDAO;
    private InsumoDAO insumoDAO;
    
    // Componentes de la vista
    private JTable tblReportes;
    private DefaultTableModel modeloTabla;
    
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
    public void inicializarComponentes(JTable tblReportes) {
        this.tblReportes = tblReportes;
        configurarTablaInicial();
        mostrarReportePlatillos(); // Mostrar platillos por defecto
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
        
        // Configurar columnas
        modeloTabla = new DefaultTableModel(
            new Object[]{"Posición", "Platillo", "Categoría", "Veces Vendido", "Total Vendido"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        // Obtener datos
        String sql = "SELECT p.nombre AS platillo, c.nombre AS categoria, " +
                    "COUNT(dp.id_detalle) AS veces_vendido, " +
                    "SUM(dp.cantidad) AS cantidad_total, " +
                    "SUM(dp.subtotal) AS total_vendido " +
                    "FROM DetallePedido dp " +
                    "INNER JOIN Platillos p ON dp.id_platillo = p.id_platillo " +
                    "LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                    "GROUP BY p.id_platillo, p.nombre, c.nombre " +
                    "ORDER BY veces_vendido DESC, total_vendido DESC";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
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
                modeloTabla.addRow(new Object[]{"Sin datos", "No hay ventas registradas", "-", "-", "-"});
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
        
        // Configurar columnas
        modeloTabla = new DefaultTableModel(
            new Object[]{"Código", "Insumo", "Categoría", "Stock", "Estado", "Almacén", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        // Obtener datos
        List<Insumo> insumos = insumoDAO.obtenerTodos();
        
        for (Insumo i : insumos) {
            String estado;
            if (i.getStock() == 0) {
                estado = "❌ SIN STOCK";
            } else if (i.getStock() < 10) {
                estado = "⚠️ STOCK BAJO";
            } else if (i.getStock() < 30) {
                estado = "🟡 STOCK MEDIO";
            } else {
                estado = "✅ STOCK OK";
            }
            
            modeloTabla.addRow(new Object[]{
                i.getCodigo(),
                i.getNombre(),
                i.getNombreCategoria(),
                i.getStock() + " unidades",
                estado,
                i.getAlmacen(),
                i.getProveedor()
            });
        }
        
        if (modeloTabla.getRowCount() == 0) {
            modeloTabla.addRow(new Object[]{"Sin datos", "No hay insumos registrados", "-", "-", "-", "-", "-"});
        }
        
        ajustarAnchoColumnas(new int[]{80, 200, 120, 100, 120, 120, 150});
    }
    
    /**
     * Mostrar reporte de caja (ventas/ganancias)
     */
    public void mostrarReporteCaja() {
        tipoActual = TipoReporte.CAJA;
        
        // Configurar columnas
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID Pedido", "Mesa", "Fecha", "Hora", "Subtotal", "IGV", "Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportes.setModel(modeloTabla);
        
        // Obtener datos
        List<Pedido> pedidos = pedidoDAO.obtenerTodos();
        
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        
        double totalSubtotal = 0;
        double totalIGV = 0;
        double totalGeneral = 0;
        
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.getIdPedido(),
                "Mesa " + p.getNumeroMesa(),
                p.getFecha().format(formatoFecha),
                p.getHora().format(formatoHora),
                String.format("S/. %.2f", p.getSubtotal()),
                String.format("S/. %.2f", p.getIgv()),
                String.format("S/. %.2f", p.getTotal()),
                p.getEstado()
            });
            
            totalSubtotal += p.getSubtotal();
            totalIGV += p.getIgv();
            totalGeneral += p.getTotal();
        }
        
        if (modeloTabla.getRowCount() > 0) {
            // Agregar fila de totales
            modeloTabla.addRow(new Object[]{
                "", "", "", "TOTALES:",
                String.format("S/. %.2f", totalSubtotal),
                String.format("S/. %.2f", totalIGV),
                String.format("S/. %.2f", totalGeneral),
                ""
            });
        } else {
            modeloTabla.addRow(new Object[]{"Sin datos", "No hay pedidos registrados", "-", "-", "-", "-", "-", "-"});
        }
        
        ajustarAnchoColumnas(new int[]{80, 100, 100, 80, 100, 100, 100, 100});
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
        
        // Generar texto del reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append("╔════════════════════════════════════════════════════╗\n");
        reporte.append("║          RESTAURANTE MERY - REPORTES              ║\n");
        reporte.append("╠════════════════════════════════════════════════════╣\n");
        reporte.append("║  Tipo: ").append(tipoReporte).append("\n");
        reporte.append("║  Fecha: ").append(java.time.LocalDate.now()).append("\n");
        reporte.append("╚════════════════════════════════════════════════════╝\n\n");
        
        // Agregar datos de la tabla
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
        
        // Mostrar en ventana
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
    
    /**
     * Obtener emoji según posición
     */
    private String obtenerEmoji(int posicion) {
        switch (posicion) {
            case 1: return "🥇";
            case 2: return "🥈";
            case 3: return "🥉";
            default: return "  ";
        }
    }
    
    /**
     * Ajustar ancho de columnas
     */
    private void ajustarAnchoColumnas(int[] anchos) {
        for (int i = 0; i < anchos.length && i < tblReportes.getColumnCount(); i++) {
            tblReportes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
    
    /**
     * Obtener resumen general
     */
    public String obtenerResumenGeneral() {
        StringBuilder resumen = new StringBuilder();
        
        try (Connection con = ConexionSQL.conectar()) {
            // Total de pedidos
            String sqlPedidos = "SELECT COUNT(*) AS total FROM Pedidos";
            PreparedStatement ps1 = con.prepareStatement(sqlPedidos);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                resumen.append("📊 Total Pedidos: ").append(rs1.getInt("total")).append("\n");
            }
            rs1.close();
            ps1.close();
            
            // Ventas totales
            String sqlVentas = "SELECT SUM(total) AS ventas FROM Pedidos WHERE estado = 'Completado'";
            PreparedStatement ps2 = con.prepareStatement(sqlVentas);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                resumen.append("💰 Ventas Totales: S/. ").append(String.format("%.2f", rs2.getDouble("ventas"))).append("\n");
            }
            rs2.close();
            ps2.close();
            
            // Platillo más vendido
            String sqlTop = "SELECT p.nombre, COUNT(*) AS veces FROM DetallePedido dp " +
                          "INNER JOIN Platillos p ON dp.id_platillo = p.id_platillo " +
                          "GROUP BY p.nombre ORDER BY veces DESC";
            PreparedStatement ps3 = con.prepareStatement(sqlTop);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                resumen.append("🏆 Más Vendido: ").append(rs3.getString("nombre")).append("\n");
            }
            rs3.close();
            ps3.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen: " + e.getMessage());
        }
        
        return resumen.toString();
    }
}