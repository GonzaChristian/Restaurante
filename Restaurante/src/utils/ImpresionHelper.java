package utils;

import models.Pedido;
import models.DetallePedido;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.print.*;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria para manejar impresiones
 */
public class ImpresionHelper {
    
    /**
     * Imprimir voucher de pedido
     */
    public static void imprimirVoucherPedido(Pedido pedido, Component parent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                
                // Configurar fuentes
                Font fontTitulo = new Font("Arial", Font.BOLD, 16);
                Font fontNormal = new Font("Arial", Font.PLAIN, 10);
                Font fontBold = new Font("Arial", Font.BOLD, 10);
                Font fontTotal = new Font("Arial", Font.BOLD, 14);
                
                int y = 30;
                int lineHeight = 15;
                int x = 50;
                
                // Encabezado
                g2d.setFont(fontTitulo);
                g2d.drawString("RESTAURANTE MERY", x, y);
                y += 20;
                
                g2d.setFont(fontNormal);
                g2d.drawString("RUC: 20123456789", x, y);
                y += lineHeight;
                g2d.drawString("Dirección: Av. Principal 123, Lima", x, y);
                y += lineHeight;
                g2d.drawString("Teléfono: (01) 234-5678", x, y);
                y += lineHeight + 10;
                
                // Línea separadora
                g2d.drawLine(x, y, 500, y);
                y += 15;
                
                // Información del pedido
                g2d.setFont(fontBold);
                g2d.drawString("VOUCHER DE PEDIDO", x, y);
                y += lineHeight + 5;
                
                g2d.setFont(fontNormal);
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                g2d.drawString("Pedido N°: " + pedido.getIdPedido(), x, y);
                y += lineHeight;
                g2d.drawString("Mesa: " + pedido.getNumeroMesa(), x, y);
                y += lineHeight;
                g2d.drawString("Fecha: " + pedido.getFecha().format(formatoFecha), x, y);
                y += lineHeight;
                g2d.drawString("Hora: " + pedido.getHora().format(formatoHora), x, y);
                y += lineHeight;
                g2d.drawString("Estado: " + pedido.getEstado(), x, y);
                y += lineHeight + 10;
                
                // Línea separadora
                g2d.drawLine(x, y, 500, y);
                y += 15;
                
                // Encabezados de tabla
                g2d.setFont(fontBold);
                g2d.drawString("CANT", x, y);
                g2d.drawString("DESCRIPCIÓN", x + 50, y);
                g2d.drawString("P.UNIT", x + 280, y);
                g2d.drawString("SUBTOTAL", x + 380, y);
                y += lineHeight;
                
                g2d.drawLine(x, y, 500, y);
                y += 10;
                
                // Detalles del pedido
                g2d.setFont(fontNormal);
                for (DetallePedido detalle : pedido.getDetalles()) {
                    g2d.drawString(String.valueOf(detalle.getCantidad()), x + 5, y);
                    
                    String nombre = detalle.getNombrePlatillo();
                    if (nombre.length() > 30) {
                        nombre = nombre.substring(0, 27) + "...";
                    }
                    g2d.drawString(nombre, x + 50, y);
                    
                    g2d.drawString(String.format("S/. %.2f", detalle.getPrecioUnitario()), x + 280, y);
                    g2d.drawString(String.format("S/. %.2f", detalle.getSubtotal()), x + 380, y);
                    y += lineHeight;
                }
                
                y += 10;
                g2d.drawLine(x, y, 500, y);
                y += 15;
                
                // Totales
                g2d.setFont(fontBold);
                g2d.drawString("SUBTOTAL:", x + 280, y);
                g2d.drawString(String.format("S/. %.2f", pedido.getSubtotal()), x + 380, y);
                y += lineHeight;
                
                g2d.drawString("IGV (18%):", x + 280, y);
                g2d.drawString(String.format("S/. %.2f", pedido.getIgv()), x + 380, y);
                y += lineHeight + 5;
                
                g2d.setFont(fontTotal);
                g2d.drawString("TOTAL:", x + 280, y);
                g2d.drawString(String.format("S/. %.2f", pedido.getTotal()), x + 380, y);
                y += lineHeight + 20;
                
                // Línea separadora
                g2d.drawLine(x, y, 500, y);
                y += 15;
                
                // Pie de página
                g2d.setFont(fontNormal);
                g2d.drawString("¡Gracias por su preferencia!", x + 150, y);
                y += lineHeight;
                g2d.drawString("Restaurante Mery - La mejor comida de la ciudad", x + 80, y);
                
                return PAGE_EXISTS;
            }
        });
        
        // Mostrar diálogo de impresión
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(parent,
                    "Voucher enviado a impresión",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(parent,
                    "Error al imprimir: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Imprimir reporte desde tabla
     */
    public static void imprimirReporte(String tituloReporte, TableModel modeloTabla, Component parent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                // Calcular páginas necesarias
                int rowsPerPage = 35; // Filas por página
                int totalPages = (int) Math.ceil((double) modeloTabla.getRowCount() / rowsPerPage);
                
                if (pageIndex >= totalPages) {
                    return NO_SUCH_PAGE;
                }
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                
                // Configurar fuentes
                Font fontTitulo = new Font("Arial", Font.BOLD, 16);
                Font fontSubtitulo = new Font("Arial", Font.BOLD, 12);
                Font fontNormal = new Font("Arial", Font.PLAIN, 9);
                Font fontBold = new Font("Arial", Font.BOLD, 9);
                
                int y = 30;
                int lineHeight = 12;
                int x = 20;
                
                // Encabezado (en todas las páginas)
                g2d.setFont(fontTitulo);
                g2d.drawString("RESTAURANTE MERY", x, y);
                y += 20;
                
                g2d.setFont(fontSubtitulo);
                g2d.drawString(tituloReporte, x, y);
                y += 15;
                
                g2d.setFont(fontNormal);
                g2d.drawString("Fecha: " + java.time.LocalDate.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), x, y);
                g2d.drawString("Página: " + (pageIndex + 1) + " de " + totalPages, 450, y);
                y += lineHeight + 5;
                
                // Línea separadora
                g2d.drawLine(x, y, 550, y);
                y += 10;
                
                // Encabezados de columnas
                g2d.setFont(fontBold);
                int columnWidth = 550 / modeloTabla.getColumnCount();
                
                for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
                    String columnName = modeloTabla.getColumnName(col);
                    if (columnName.length() > 12) {
                        columnName = columnName.substring(0, 10) + "..";
                    }
                    g2d.drawString(columnName, x + (col * columnWidth), y);
                }
                y += lineHeight;
                
                g2d.drawLine(x, y, 550, y);
                y += 10;
                
                // Datos de la tabla
                g2d.setFont(fontNormal);
                int startRow = pageIndex * rowsPerPage;
                int endRow = Math.min(startRow + rowsPerPage, modeloTabla.getRowCount());
                
                for (int row = startRow; row < endRow; row++) {
                    for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
                        Object value = modeloTabla.getValueAt(row, col);
                        String text = value != null ? value.toString() : "";
                        
                        if (text.length() > 15) {
                            text = text.substring(0, 13) + "..";
                        }
                        
                        g2d.drawString(text, x + (col * columnWidth), y);
                    }
                    y += lineHeight;
                    
                    // Evitar que el texto salga de la página
                    if (y > pageFormat.getImageableHeight() - 50) {
                        break;
                    }
                }
                
                // Pie de página
                y = (int) pageFormat.getImageableHeight() - 20;
                g2d.drawLine(x, y - 10, 550, y - 10);
                g2d.setFont(fontNormal);
                g2d.drawString("Restaurante Mery - Sistema de Gestión", x, y);
                
                return PAGE_EXISTS;
            }
        });
        
        // Mostrar diálogo de impresión
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(parent,
                    "Reporte enviado a impresión",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(parent,
                    "Error al imprimir: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Vista previa de voucher (en JDialog)
     */
    public static void vistaPreviaVoucher(Pedido pedido, Component parent) {
        StringBuilder voucher = new StringBuilder();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        voucher.append("═══════════════════════════════════════════════════\n");
        voucher.append("               RESTAURANTE MERY\n");
        voucher.append("           RUC: 20123456789\n");
        voucher.append("    Dirección: Av. Principal 123, Lima\n");
        voucher.append("         Teléfono: (01) 234-5678\n");
        voucher.append("═══════════════════════════════════════════════════\n\n");
        
        voucher.append("              VOUCHER DE PEDIDO\n\n");
        voucher.append("Pedido N°: ").append(pedido.getIdPedido()).append("\n");
        voucher.append("Mesa: ").append(pedido.getNumeroMesa()).append("\n");
        voucher.append("Fecha: ").append(pedido.getFecha().format(formatoFecha)).append("\n");
        voucher.append("Hora: ").append(pedido.getHora().format(formatoHora)).append("\n");
        voucher.append("Estado: ").append(pedido.getEstado()).append("\n\n");
        
        voucher.append("───────────────────────────────────────────────────\n");
        voucher.append(String.format("%-5s %-25s %8s %10s\n", "CANT", "DESCRIPCIÓN", "P.UNIT", "SUBTOTAL"));
        voucher.append("───────────────────────────────────────────────────\n");
        
        for (DetallePedido detalle : pedido.getDetalles()) {
            voucher.append(String.format("%-5d %-25s %8.2f %10.2f\n",
                detalle.getCantidad(),
                detalle.getNombrePlatillo().length() > 25 ? 
                    detalle.getNombrePlatillo().substring(0, 22) + "..." : 
                    detalle.getNombrePlatillo(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
            ));
        }
        
        voucher.append("───────────────────────────────────────────────────\n\n");
        voucher.append(String.format("%35s %8s %10.2f\n", "SUBTOTAL:", "S/.", pedido.getSubtotal()));
        voucher.append(String.format("%35s %8s %10.2f\n", "IGV (18%):", "S/.", pedido.getIgv()));
        voucher.append(String.format("%35s %8s %10.2f\n", "TOTAL:", "S/.", pedido.getTotal()));
        
        voucher.append("\n═══════════════════════════════════════════════════\n");
        voucher.append("          ¡Gracias por su preferencia!\n");
        voucher.append("   Restaurante Mery - La mejor comida de la ciudad\n");
        voucher.append("═══════════════════════════════════════════════════\n");
        
        // Mostrar en JTextArea
        JTextArea textArea = new JTextArea(voucher.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        
        int opcion = JOptionPane.showConfirmDialog(parent, scrollPane,
            "Vista Previa - Voucher Pedido #" + pedido.getIdPedido(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            imprimirVoucherPedido(pedido, parent);
        }
    }
}