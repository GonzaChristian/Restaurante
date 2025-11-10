package models;

/**
 * Clase modelo que representa un Platillo del menú
 */
public class Platillo {
    private int idPlatillo;
    private String nombre;
    private int idCategoria;
    private String nombreCategoria; // Para mostrar en UI
    private double precio;
    private byte[] imagen;

    // Constructor vacío
    public Platillo() {
    }

    // Constructor completo
    public Platillo(int idPlatillo, String nombre, int idCategoria, String nombreCategoria, double precio, byte[] imagen) {
        this.idPlatillo = idPlatillo;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Constructor sin ID (para insertar)
    public Platillo(String nombre, int idCategoria, double precio, byte[] imagen) {
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Constructor simplificado (sin imagen)
    public Platillo(int idPlatillo, String nombre, String nombreCategoria, double precio) {
        this.idPlatillo = idPlatillo;
        this.nombre = nombre;
        this.nombreCategoria = nombreCategoria;
        this.precio = precio;
    }

    // Getters y Setters
    public int getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(int idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Platillo{" +
                "idPlatillo=" + idPlatillo +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + nombreCategoria + '\'' +
                ", precio=" + precio +
                '}';
    }
}