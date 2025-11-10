package dao;

import conexion.ConexionSQL;
import models.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla Mesas
 */
public class MesaDAO {

    /**
     * Obtener todas las mesas
     */
    public List<Mesa> obtenerTodas() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT id_mesa, numero_mesa, estado FROM Mesas ORDER BY numero_mesa";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Mesa mesa = new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero_mesa"),
                    rs.getString("estado")
                );
                mesas.add(mesa);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesas: " + e.getMessage());
        }
        
        return mesas;
    }

    /**
     * Obtener una mesa por su número
     */
    public Mesa obtenerPorNumero(int numeroMesa) {
        String sql = "SELECT id_mesa, numero_mesa, estado FROM Mesas WHERE numero_mesa = ?";
        Mesa mesa = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, numeroMesa);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                mesa = new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero_mesa"),
                    rs.getString("estado")
                );
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesa: " + e.getMessage());
        }
        
        return mesa;
    }

    /**
     * Obtener una mesa por su ID
     */
    public Mesa obtenerPorId(int idMesa) {
        String sql = "SELECT id_mesa, numero_mesa, estado FROM Mesas WHERE id_mesa = ?";
        Mesa mesa = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idMesa);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                mesa = new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero_mesa"),
                    rs.getString("estado")
                );
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesa: " + e.getMessage());
        }
        
        return mesa;
    }

    /**
     * Actualizar el estado de una mesa
     */
    public boolean actualizarEstado(int numeroMesa, String nuevoEstado) {
        String sql = "UPDATE Mesas SET estado = ? WHERE numero_mesa = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setInt(2, numeroMesa);
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de mesa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marcar mesa como "En Proceso"
     */
    public boolean marcarEnProceso(int numeroMesa) {
        return actualizarEstado(numeroMesa, "En Proceso");
    }

    /**
     * Marcar mesa como "Disponible"
     */
    public boolean marcarDisponible(int numeroMesa) {
        return actualizarEstado(numeroMesa, "Disponible");
    }

    /**
     * Obtener mesas disponibles
     */
    public List<Mesa> obtenerDisponibles() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT id_mesa, numero_mesa, estado FROM Mesas WHERE estado = 'Disponible' ORDER BY numero_mesa";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Mesa mesa = new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero_mesa"),
                    rs.getString("estado")
                );
                mesas.add(mesa);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesas disponibles: " + e.getMessage());
        }
        
        return mesas;
    }

    /**
     * Obtener mesas en proceso
     */
    public List<Mesa> obtenerEnProceso() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT id_mesa, numero_mesa, estado FROM Mesas WHERE estado = 'En Proceso' ORDER BY numero_mesa";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Mesa mesa = new Mesa(
                    rs.getInt("id_mesa"),
                    rs.getInt("numero_mesa"),
                    rs.getString("estado")
                );
                mesas.add(mesa);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesas en proceso: " + e.getMessage());
        }
        
        return mesas;
    }
}