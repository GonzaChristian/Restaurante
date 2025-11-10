package models;

/**
 * Clase modelo que representa un detalle (ítem) de un pedido
 */
public class DetallePedido {
    private int idDetalle;
    private int idPedido;
    private int idPlatillo;
    private String nombrePlatillo; // Para mostrar en UI
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Constructor vacío
    public DetallePedido() {
    }

    // Constructor completo
    public DetallePedido(int idDetalle, int idPedido, int idPlatillo, 
                        String nombrePlatillo, int cantidad, double precioUnitario) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idPlatillo = idPlatillo;
        this.nombrePlatillo = nombrePlatillo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Constructor sin ID (para nuevos detalles)
    public DetallePedido(int idPlatillo, String nombrePlatillo, int cantidad, double precioUnitario) {
        this.idPlatillo = idPlatillo;
        this.nombrePlatillo = nombrePlatillo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Constructor desde Platillo
    public DetallePedido(Platillo platillo, int cantidad) {
        this.idPlatillo = platillo.getIdPlatillo();
        this.nombrePlatillo = platillo.getNombre();
        this.cantidad = cantidad;
        this.precioUnitario = platillo.getPrecio();
        this.subtotal = cantidad * platillo.getPrecio();
    }

    // Método para recalcular subtotal
    public void calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(int idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
        this.nombrePlatillo = nombrePlatillo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "nombrePlatillo='" + nombrePlatillo + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}