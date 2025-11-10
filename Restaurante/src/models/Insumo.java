package models;

/**
 * Clase modelo que representa un Insumo del inventario
 */
public class Insumo {
    private int idInsumo;
    private String codigo;
    private String nombre;
    private int stock;
    private String almacen;
    private int idCategoria;
    private String nombreCategoria;
    private String proveedor;

    // Constructor vacío
    public Insumo() {
    }

    // Constructor completo
    public Insumo(int idInsumo, String codigo, String nombre, int stock, 
                 String almacen, int idCategoria, String nombreCategoria, 
                 String proveedor) {
        this.idInsumo = idInsumo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.almacen = almacen;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.proveedor = proveedor;
    }

    // Constructor sin ID (para insertar)
    public Insumo(String codigo, String nombre, int stock, String almacen, 
                 int idCategoria, String proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.almacen = almacen;
        this.idCategoria = idCategoria;
        this.proveedor = proveedor;
    }

    // Getters y Setters
    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
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

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public boolean tieneStockBajo() {
        return stock < 10; // Umbral de stock bajo
    }

    @Override
    public String toString() {
        return "Insumo{" +
                "idInsumo=" + idInsumo +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                ", proveedor='" + proveedor + '\'' +
                '}';
    }
}