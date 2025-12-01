package utils;

import models.Pedido;
import models.DetallePedido;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.print.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Sistema completo de impresión para el restaurante
 * Incluye: Vouchers, Comandas, Tickets y Reportes
 */
public class ImpresoraPedidos {
    
    // Configuraciones de impresión
    private static final String NOMBRE_RESTAURANTE = "RESTAURANTE MERY";
    private static final String RUC = "20123456789";
    private static final String DIRECCION = "Av. Principal 123, Lima";
    private static final String TELEFONO = "(01) 234-5678";
    private static final double IGV_PORCENTAJE = 0.18;
    
    /**
     * Imprimir voucher completo del pedido (para el cliente)
     */
    public static void imprimirVoucher(Pedido pedido, Component parent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Voucher-Pedido-" + pedido.getIdPedido());
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) 
                    throws PrinterException {
                
                if (pageIndex > 0) return NO_SUCH_PAGE;
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                
                // Configurar renderizado de calidad
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                int y = 40;
                int x = 60;
                
                // ENCABEZADO
                dibujarEncabezado(g2d, x, y);
                y += 100;
                
                // INFORMACIÓN DEL PEDIDO
                y = dibujarInfoPedido(g2d, pedido, x, y);
                y += 20;
                
                // TABLA DE PRODUCTOS
                y = dibujarTablaProductos(g2d, pedido, x, y);
                y += 30;
                
                // TOTALES
                y = dibujarTotales(g2d, pedido, x, y);
                y += 40;
                
                // PIE DE PÁGINA
                dibujarPiePagina(g2d, x, y);
                
                return PAGE_EXISTS;
            }
        });
        
        ejecutarImpresion(job, parent, "Voucher");
    }
    
    /**
     * Imprimir comanda para la cocina (formato simplificado)
     */
    public static void imprimirComanda(Pedido pedido, Component parent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Comanda-Mesa-" + pedido.getNumeroMesa());
        
        // Configurar papel térmico 80mm si está disponible
        Paper paper = new Paper();
        paper.setSize(226.77, 841.89); // 80mm x 297mm en puntos
        paper.setImageableArea(5, 5, 216.77, 831.89);
        
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pf, int pageIndex) 
                    throws PrinterException {
                
                if (pageIndex > 0) return NO_SUCH_PAGE;
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pf.getImageableX(), pf.getImageableY());
                
                int y = 20;
                int x = 10;
                
                // Fuente para comanda
                Font fontTitulo = new Font("Arial", Font.BOLD, 18);
                Font fontInfo = new Font("Arial", Font.BOLD, 14);
                Font fontDetalle = new Font("Arial", Font.PLAIN, 12);
                
                // TÍTULO COMANDA
                g2d.setFont(fontTitulo);
                g2d.drawString("*** COMANDA - COCINA ***", x, y);
                y += 25;
                
                // Línea separadora
                g2d.drawLine(x, y, 200, y);
                y += 15;
                
                // INFORMACIÓN BÁSICA
                g2d.setFont(fontInfo);
                g2d.drawString("MESA: " + pedido.getNumeroMesa(), x, y);
                y += 20;
                
                DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
                g2d.drawString("HORA: " + pedido.getHora().format(formatoHora), x, y);
                y += 20;
                
                g2d.drawString("PEDIDO #" + pedido.getIdPedido(), x, y);
                y += 25;
                
                // Línea separadora
                g2d.drawLine(x, y, 200, y);
                y += 15;
                
                // PLATILLOS
                g2d.setFont(fontDetalle);
                for (DetallePedido detalle : pedido.getDetalles()) {
                    // Cantidad en grande
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    g2d.drawString("[" + detalle.getCantidad() + "x]", x, y);
                    
                    // Nombre del platillo
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    String nombre = detalle.getNombrePlatillo();
                    
                    // Dividir nombre si es muy largo
                    if (nombre.length() > 25) {
                        g2d.drawString(nombre.substring(0, 25), x + 50, y);
                        y += 18;
                        g2d.drawString(nombre.substring(25), x + 50, y);
                    } else {
                        g2d.drawString(nombre, x + 50, y);
                    }
                    
                    y += 25;
                }
                
                y += 10;
                g2d.drawLine(x, y, 200, y);
                y += 20;
                
                // TOTAL DE ITEMS
                g2d.setFont(fontInfo);
                int totalItems = pedido.getDetalles().stream()
                    .mapToInt(DetallePedido::getCantidad)
                    .sum();
                g2d.drawString("TOTAL ITEMS: " + totalItems, x, y);
                y += 30;
                
                // HORA DE IMPRESIÓN
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                DateTimeFormatter formatoCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                g2d.drawString("Impreso: " + LocalDateTime.now().format(formatoCompleto), x, y);
                
                return PAGE_EXISTS;
            }
        }, pageFormat);
        
        ejecutarImpresion(job, parent, "Comanda");
    }
    
    /**
     * Imprimir ticket (formato pequeño para caja)
     */
    public static void imprimirTicket(Pedido pedido, Component parent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Ticket-" + pedido.getIdPedido());
        
        // Configurar papel ticket 58mm
        Paper paper = new Paper();
        paper.setSize(164.41, 595.28); // 58mm x 210mm en puntos
        paper.setImageableArea(5, 5, 154.41, 585.28);
        
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pf, int pageIndex) 
                    throws PrinterException {
                
                if (pageIndex > 0) return NO_SUCH_PAGE;
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pf.getImageableX(), pf.getImageableY());
                
                int y = 15;
                int x = 5;
                int anchoTicket = 145;
                
                Font fontTitulo = new Font("Arial", Font.BOLD, 12);
                Font fontNormal = new Font("Arial", Font.PLAIN, 9);
                Font fontBold = new Font("Arial", Font.BOLD, 9);
                Font fontTotal = new Font("Arial", Font.BOLD, 11);
                
                // Encabezado centrado
                g2d.setFont(fontTitulo);
                centrarTexto(g2d, NOMBRE_RESTAURANTE, anchoTicket, y);
                y += 15;
                
                g2d.setFont(fontNormal);
                centrarTexto(g2d, "RUC: " + RUC, anchoTicket, y);
                y += 12;
                centrarTexto(g2d, DIRECCION, anchoTicket, y);
                y += 12;
                centrarTexto(g2d, "Tel: " + TELEFONO, anchoTicket, y);
                y += 15;
                
                // Línea
                dibujarLineaPunteada(g2d, x, y, anchoTicket);
                y += 10;
                
                // Info pedido
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
                g2d.setFont(fontNormal);
                g2d.drawString("Pedido: #" + pedido.getIdPedido(), x, y);
                y += 12;
                g2d.drawString("Mesa: " + pedido.getNumeroMesa(), x, y);
                y += 12;
                g2d.drawString("Fecha: " + LocalDateTime.of(pedido.getFecha(), 
                    pedido.getHora()).format(formato), x, y);
                y += 15;
                
                dibujarLineaPunteada(g2d, x, y, anchoTicket);
                y += 10;
                
                // Productos
                g2d.setFont(fontNormal);
                for (DetallePedido detalle : pedido.getDetalles()) {
                    String linea = detalle.getCantidad() + "x " + 
                        truncarTexto(detalle.getNombrePlatillo(), 15);
                    g2d.drawString(linea, x, y);
                    
                    String precio = String.format("%.2f", detalle.getSubtotal());
                    g2d.drawString(precio, anchoTicket - 30, y);
                    y += 12;
                }
                
                y += 5;
                dibujarLineaPunteada(g2d, x, y, anchoTicket);
                y += 12;
                
                // Totales
                g2d.setFont(fontBold);
                g2d.drawString("Subtotal:", x, y);
                g2d.drawString(String.format("%.2f", pedido.getSubtotal()), 
                    anchoTicket - 30, y);
                y += 12;
                
                g2d.drawString("IGV 18%:", x, y);
                g2d.drawString(String.format("%.2f", pedido.getIgv()), 
                    anchoTicket - 30, y);
                y += 15;
                
                g2d.setFont(fontTotal);
                g2d.drawString("TOTAL:", x, y);
                g2d.drawString("S/ " + String.format("%.2f", pedido.getTotal()), 
                    anchoTicket - 40, y);
                y += 15;
                
                dibujarLineaPunteada(g2d, x, y, anchoTicket);
                y += 12;
                
                // Pie
                g2d.setFont(fontNormal);
                centrarTexto(g2d, "Gracias por su visita", anchoTicket, y);
                y += 12;
                centrarTexto(g2d, "¡Vuelva pronto!", anchoTicket, y);
                
                return PAGE_EXISTS;
            }
        }, pageFormat);
        
        ejecutarImpresion(job, parent, "Ticket");
    }
    
    /**
     * Mostrar vista previa antes de imprimir
     */
    public static void vistaPrevia(Pedido pedido, Component parent) {
        StringBuilder preview = new StringBuilder();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        preview.append("╔════════════════════════════════════════════════════╗\n");
        preview.append("║           ").append(centrar(NOMBRE_RESTAURANTE, 38)).append("║\n");
        preview.append("║           ").append(centrar("RUC: " + RUC, 38)).append("║\n");
        preview.append("║      ").append(centrar(DIRECCION, 44)).append("║\n");
        preview.append("║         ").append(centrar("Tel: " + TELEFONO, 42)).append("║\n");
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append("║              VOUCHER DE PEDIDO                     ║\n");
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append(String.format("║ Pedido N°: %-39d ║\n", pedido.getIdPedido()));
        preview.append(String.format("║ Mesa: %-44d ║\n", pedido.getNumeroMesa()));
        preview.append(String.format("║ Fecha: %-43s ║\n", pedido.getFecha().format(formatoFecha)));
        preview.append(String.format("║ Hora: %-44s ║\n", pedido.getHora().format(formatoHora)));
        preview.append(String.format("║ Estado: %-42s ║\n", pedido.getEstado()));
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append("║ Cant │ Descripción              │ P.Unit │ Total  ║\n");
        preview.append("╠══════╪══════════════════════════╪════════╪════════╣\n");
        
        for (DetallePedido detalle : pedido.getDetalles()) {
            String nombre = truncarTexto(detalle.getNombrePlatillo(), 24);
            preview.append(String.format("║  %-3d │ %-24s │ %6.2f │ %6.2f ║\n",
                detalle.getCantidad(),
                nombre,
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
            ));
        }
        
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append(String.format("║                           Subtotal:     S/ %7.2f ║\n", 
            pedido.getSubtotal()));
        preview.append(String.format("║                           IGV (18%%):    S/ %7.2f ║\n", 
            pedido.getIgv()));
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append(String.format("║                       TOTAL A PAGAR:  S/ %7.2f ║\n", 
            pedido.getTotal()));
        preview.append("╠════════════════════════════════════════════════════╣\n");
        preview.append("║         ¡Gracias por su preferencia!               ║\n");
        preview.append("║    Restaurante Mery - Sabor que enamora           ║\n");
        preview.append("╚════════════════════════════════════════════════════╝\n");
        
        JTextArea textArea = new JTextArea(preview.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 700));
        
        Object[] opciones = {"Imprimir Voucher", "Imprimir Comanda", 
                            "Imprimir Ticket", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
            parent,
            scrollPane,
            "Vista Previa - Pedido #" + pedido.getIdPedido(),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        switch (opcion) {
            case 0: imprimirVoucher(pedido, parent); break;
            case 1: imprimirComanda(pedido, parent); break;
            case 2: imprimirTicket(pedido, parent); break;
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private static void dibujarEncabezado(Graphics2D g2d, int x, int y) {
        Font fontNombre = new Font("Arial", Font.BOLD, 20);
        Font fontInfo = new Font("Arial", Font.PLAIN, 11);
        
        g2d.setFont(fontNombre);
        g2d.drawString(NOMBRE_RESTAURANTE, x + 100, y);
        y += 25;
        
        g2d.setFont(fontInfo);
        g2d.drawString("RUC: " + RUC, x + 150, y);
        y += 15;
        g2d.drawString(DIRECCION, x + 120, y);
        y += 15;
        g2d.drawString("Teléfono: " + TELEFONO, x + 140, y);
    }
    
    private static int dibujarInfoPedido(Graphics2D g2d, Pedido pedido, int x, int y) {
        Font fontTitulo = new Font("Arial", Font.BOLD, 14);
        Font fontInfo = new Font("Arial", Font.PLAIN, 11);
        
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        g2d.drawLine(x, y, 520, y);
        y += 15;
        
        g2d.setFont(fontTitulo);
        g2d.drawString("COMPROBANTE DE PEDIDO", x + 130, y);
        y += 20;
        
        g2d.setFont(fontInfo);
        g2d.drawString("Pedido N°: " + pedido.getIdPedido(), x, y);
        g2d.drawString("Estado: " + pedido.getEstado(), x + 300, y);
        y += 18;
        
        g2d.drawString("Mesa: " + pedido.getNumeroMesa(), x, y);
        y += 18;
        
        g2d.drawString("Fecha: " + pedido.getFecha().format(formatoFecha), x, y);
        g2d.drawString("Hora: " + pedido.getHora().format(formatoHora), x + 300, y);
        y += 5;
        
        g2d.drawLine(x, y, 520, y);
        
        return y;
    }
    
    private static int dibujarTablaProductos(Graphics2D g2d, Pedido pedido, int x, int y) {
        Font fontHeader = new Font("Arial", Font.BOLD, 11);
        Font fontData = new Font("Arial", Font.PLAIN, 10);
        
        y += 15;
        
        // Headers
        g2d.setFont(fontHeader);
        g2d.drawString("CANT", x + 10, y);
        g2d.drawString("DESCRIPCIÓN", x + 80, y);
        g2d.drawString("P. UNITARIO", x + 320, y);
        g2d.drawString("SUBTOTAL", x + 430, y);
        y += 5;
        
        g2d.drawLine(x, y, 520, y);
        y += 15;
        
        // Datos
        g2d.setFont(fontData);
        for (DetallePedido detalle : pedido.getDetalles()) {
            g2d.drawString(String.valueOf(detalle.getCantidad()), x + 15, y);
            
            String nombre = detalle.getNombrePlatillo();
            if (nombre.length() > 30) {
                g2d.drawString(nombre.substring(0, 27) + "...", x + 80, y);
            } else {
                g2d.drawString(nombre, x + 80, y);
            }
            
            g2d.drawString(String.format("S/ %.2f", detalle.getPrecioUnitario()), 
                x + 330, y);
            g2d.drawString(String.format("S/ %.2f", detalle.getSubtotal()), 
                x + 435, y);
            
            y += 18;
        }
        
        y += 5;
        g2d.drawLine(x, y, 520, y);
        
        return y;
    }
    
    private static int dibujarTotales(Graphics2D g2d, Pedido pedido, int x, int y) {
        Font fontTotales = new Font("Arial", Font.BOLD, 12);
        Font fontTotal = new Font("Arial", Font.BOLD, 16);
        
        g2d.setFont(fontTotales);
        
        g2d.drawString("Subtotal:", x + 320, y);
        g2d.drawString(String.format("S/ %.2f", pedido.getSubtotal()), x + 430, y);
        y += 20;
        
        g2d.drawString("IGV (18%):", x + 320, y);
        g2d.drawString(String.format("S/ %.2f", pedido.getIgv()), x + 430, y);
        y += 25;
        
        g2d.setFont(fontTotal);
        g2d.drawString("TOTAL:", x + 320, y);
        g2d.drawString(String.format("S/ %.2f", pedido.getTotal()), x + 420, y);
        
        return y;
    }
    
    private static void dibujarPiePagina(Graphics2D g2d, int x, int y) {
        Font fontPie = new Font("Arial", Font.PLAIN, 10);
        Font fontItalic = new Font("Arial", Font.ITALIC, 9);
        
        g2d.drawLine(x, y, 520, y);
        y += 15;
        
        g2d.setFont(fontPie);
        g2d.drawString("¡Gracias por su preferencia!", x + 150, y);
        y += 15;
        
        g2d.setFont(fontItalic);
        g2d.drawString("Restaurante Mery - Sabor que enamora", x + 130, y);
        y += 12;
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        g2d.drawString("Impreso: " + LocalDateTime.now().format(formato), x + 150, y);
    }
    
    private static void dibujarLineaPunteada(Graphics2D g2d, int x, int y, int ancho) {
        for (int i = 0; i < ancho; i += 4) {
            g2d.drawLine(x + i, y, x + i + 2, y);
        }
    }
    
    private static void centrarTexto(Graphics2D g2d, String texto, int ancho, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(texto);
        int x = (ancho - textWidth) / 2;
        g2d.drawString(texto, x, y);
    }
    
    private static String centrar(String texto, int ancho) {
        int espacios = (ancho - texto.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < espacios; i++) sb.append(" ");
        sb.append(texto);
        while (sb.length() < ancho) sb.append(" ");
        return sb.toString();
    }
    
    private static String truncarTexto(String texto, int maxLength) {
        return texto.length() > maxLength ? 
            texto.substring(0, maxLength - 3) + "..." : texto;
    }
    
    private static void ejecutarImpresion(PrinterJob job, Component parent, 
                                         String tipoDocumento) {
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(parent,
                    tipoDocumento + " enviado a impresión correctamente",
                    "Impresión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(parent,
                    "Error al imprimir: " + e.getMessage(),
                    "Error de Impresión",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}