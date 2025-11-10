package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase modelo que representa un Pedido
 */
public class Pedido {
    private int idPedido;
    private int idMesa;
    private int numeroMesa; // Para mostrar en UI
    private Integer idEmpleado; // Puede ser null
    private Integer idCliente;  // Puede ser null
    private LocalDate fecha;
    private LocalTime hora;
    private double subtotal;
    private double igv;
    private double total;
    private String estado; // "En Proceso", "Completado"
    
    // Lista de detalles del pedido (platillos)
    private List<DetallePedido> detalles;

    // Constructor vacío
    public Pedido() {
        this.detalles = new ArrayList<>();
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.estado = "En Proceso";
    }

    // Constructor completo
    public Pedido(int idPedido, int idMesa, Integer idEmpleado, Integer idCliente, 
                  LocalDate fecha, LocalTime hora, double subtotal, double igv, 
                  double total, String estado) {
        this.idPedido = idPedido;
        this.idMesa = idMesa;
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.hora = hora;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    // Constructor para nuevo pedido
    public Pedido(int idMesa) {
        this();
        this.idMesa = idMesa;
    }

    // Método para agregar un detalle al pedido
    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
        calcularTotales();
    }

    // Método para eliminar un detalle
    public void eliminarDetalle(int index) {
        if (index >= 0 && index < detalles.size()) {
            detalles.remove(index);
            calcularTotales();
        }
    }

    // Calcular subtotal, IGV y total
    public void calcularTotales() {
        this.subtotal = 0;
        for (DetallePedido detalle : detalles) {
            this.subtotal += detalle.getSubtotal();
        }
        this.igv = this.subtotal * 0.18; // 18% IGV
        this.total = this.subtotal + this.igv;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        calcularTotales();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", mesa=" + numeroMesa +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}