package dao;

import conexion.ConexionSQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar el menú del día (OPCIONAL - solo si quieres persistencia)
 */
public class MenuDelDiaDAO {

    /**
     * Obtener IDs de platillos que están en el menú del día
     */
    public List<Integer> obtenerPlatillosEnMenu() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_platillo FROM MenuDelDia";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ids.add(rs.getInt("id_platillo"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener menú del día: " + e.getMessage());
        }
        
        return ids;
    }

    /**
     * Agregar platillo al menú del día
     */
    public boolean agregar(int idPlatillo) {
        String sql = "INSERT INTO MenuDelDia (id_platillo) VALUES (?)";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlatillo);
            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar al menú: " + e.getMessage());
            return false;
        }
    }

    /**
     * Quitar platillo del menú del día
     */
    public boolean quitar(int idPlatillo) {
        String sql = "DELETE FROM MenuDelDia WHERE id_platillo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlatillo);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al quitar del menú: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verificar si un platillo está en el menú
     */
    public boolean estaEnMenu(int idPlatillo) {
        String sql = "SELECT COUNT(*) FROM MenuDelDia WHERE id_platillo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlatillo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar menú: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Limpiar todo el menú del día
     */
    public boolean limpiarMenu() {
        String sql = "DELETE FROM MenuDelDia";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al limpiar menú: " + e.getMessage());
            return false;
        }
    }
}
