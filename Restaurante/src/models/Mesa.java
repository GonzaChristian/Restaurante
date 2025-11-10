package models;

/**
 * Clase modelo que representa una Mesa del restaurante
 */
public class Mesa {
    private int idMesa;
    private int numeroMesa;
    private String estado; // "Disponible", "En Proceso", "Completado"

    // Constructor vacío
    public Mesa() {
    }

    // Constructor completo
    public Mesa(int idMesa, int numeroMesa, String estado) {
        this.idMesa = idMesa;
        this.numeroMesa = numeroMesa;
        this.estado = estado;
    }

    // Constructor sin ID (para insertar nuevos registros)
    public Mesa(int numeroMesa, String estado) {
        this.numeroMesa = numeroMesa;
        this.estado = estado;
    }

    // Getters y Setters
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para verificar si está disponible
    public boolean estaDisponible() {
        return "Disponible".equalsIgnoreCase(estado);
    }

    // Método toString para debugging
    @Override
    public String toString() {
        return "Mesa{" +
                "idMesa=" + idMesa +
                ", numeroMesa=" + numeroMesa +
                ", estado='" + estado + '\'' +
                '}';
    }
}