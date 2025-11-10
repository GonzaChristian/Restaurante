package dao;

import conexion.ConexionSQL;
import models.Platillo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla Platillos
 */
public class PlatilloDAO {

    /**
     * Obtener todos los platillos
     */
    public List<Platillo> obtenerTodos() {
        List<Platillo> platillos = new ArrayList<>();
        String sql = "SELECT p.id_platillo, p.nombre, p.id_categoria, c.nombre AS categoria, p.precio, p.imagen " +
                     "FROM Platillos p " +
                     "LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                     "ORDER BY p.nombre";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Platillo platillo = new Platillo(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getInt("id_categoria"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getBytes("imagen")
                );
                platillos.add(platillo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener platillos: " + e.getMessage());
        }
        
        return platillos;
    }

    /**
     * Obtener platillo por ID
     */
    public Platillo obtenerPorId(int idPlatillo) {
        String sql = "SELECT p.id_platillo, p.nombre, p.id_categoria, c.nombre AS categoria, p.precio, p.imagen " +
                     "FROM Platillos p " +
                     "LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.id_platillo = ?";
        Platillo platillo = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlatillo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                platillo = new Platillo(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getInt("id_categoria"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getBytes("imagen")
                );
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener platillo: " + e.getMessage());
        }
        
        return platillo;
    }

    /**
     * Insertar nuevo platillo
     */
    public boolean insertar(Platillo platillo) {
        String sql = "INSERT INTO Platillos (nombre, id_categoria, precio, imagen) VALUES (?, ?, ?, ?)";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, platillo.getNombre());
            ps.setInt(2, platillo.getIdCategoria());
            ps.setDouble(3, platillo.getPrecio());
            ps.setBytes(4, platillo.getImagen());
            
            int filasInsertadas = ps.executeUpdate();
            
            if (filasInsertadas > 0) {
                // Obtener el ID generado
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    platillo.setIdPlatillo(rs.getInt(1));
                }
                rs.close();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar platillo: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Actualizar platillo existente
     */
    public boolean actualizar(Platillo platillo) {
        String sql = "UPDATE Platillos SET nombre = ?, id_categoria = ?, precio = ?, imagen = ? WHERE id_platillo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, platillo.getNombre());
            ps.setInt(2, platillo.getIdCategoria());
            ps.setDouble(3, platillo.getPrecio());
            ps.setBytes(4, platillo.getImagen());
            ps.setInt(5, platillo.getIdPlatillo());
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar platillo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar platillo
     */
    public boolean eliminar(int idPlatillo) {
        String sql = "DELETE FROM Platillos WHERE id_platillo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlatillo);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar platillo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtener platillos por categoría
     */
    public List<Platillo> obtenerPorCategoria(int idCategoria) {
        List<Platillo> platillos = new ArrayList<>();
        String sql = "SELECT p.id_platillo, p.nombre, p.id_categoria, c.nombre AS categoria, p.precio, p.imagen " +
                     "FROM Platillos p " +
                     "LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.id_categoria = ? " +
                     "ORDER BY p.nombre";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Platillo platillo = new Platillo(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getInt("id_categoria"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getBytes("imagen")
                );
                platillos.add(platillo);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener platillos por categoría: " + e.getMessage());
        }
        
        return platillos;
    }

    /**
     * Buscar platillos por nombre (búsqueda parcial)
     */
    public List<Platillo> buscarPorNombre(String nombre) {
        List<Platillo> platillos = new ArrayList<>();
        String sql = "SELECT p.id_platillo, p.nombre, p.id_categoria, c.nombre AS categoria, p.precio, p.imagen " +
                     "FROM Platillos p " +
                     "LEFT JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.nombre LIKE ? " +
                     "ORDER BY p.nombre";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Platillo platillo = new Platillo(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getInt("id_categoria"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getBytes("imagen")
                );
                platillos.add(platillo);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar platillos: " + e.getMessage());
        }
        
        return platillos;
    }
}