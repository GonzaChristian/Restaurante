package dao;

import conexion.ConexionSQL;
import models.Insumo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla InventarioInsumos
 */
public class InsumoDAO {

    /**
     * Obtener todos los insumos
     */
    public List<Insumo> obtenerTodos() {
        List<Insumo> insumos = new ArrayList<>();
        String sql = "SELECT i.id_insumo, i.codigo, i.nombre, i.stock, i.almacen, " +
                    "i.id_categoria, c.nombre AS categoria, i.proveedor " +
                    "FROM InventarioInsumos i " +
                    "LEFT JOIN Categorias c ON i.id_categoria = c.id_categoria " +
                    "ORDER BY i.nombre";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Insumo insumo = crearInsumoDesdeResultSet(rs);
                insumos.add(insumo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener insumos: " + e.getMessage());
        }
        
        return insumos;
    }

    /**
     * Obtener insumo por ID
     */
    public Insumo obtenerPorId(int idInsumo) {
        String sql = "SELECT i.id_insumo, i.codigo, i.nombre, i.stock, i.almacen, " +
                    "i.id_categoria, c.nombre AS categoria, i.proveedor " +
                    "FROM InventarioInsumos i " +
                    "LEFT JOIN Categorias c ON i.id_categoria = c.id_categoria " +
                    "WHERE i.id_insumo = ?";
        Insumo insumo = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idInsumo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                insumo = crearInsumoDesdeResultSet(rs);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener insumo: " + e.getMessage());
        }
        
        return insumo;
    }

    /**
     * Insertar nuevo insumo
     */
    public boolean insertar(Insumo insumo) {
        String sql = "INSERT INTO InventarioInsumos (codigo, nombre, stock, almacen, id_categoria, proveedor) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, insumo.getCodigo());
            ps.setString(2, insumo.getNombre());
            ps.setInt(3, insumo.getStock());
            ps.setString(4, insumo.getAlmacen());
            ps.setInt(5, insumo.getIdCategoria());
            ps.setString(6, insumo.getProveedor());
            
            int filasInsertadas = ps.executeUpdate();
            
            if (filasInsertadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    insumo.setIdInsumo(rs.getInt(1));
                }
                rs.close();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar insumo: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Actualizar insumo existente
     */
    public boolean actualizar(Insumo insumo) {
        String sql = "UPDATE InventarioInsumos SET codigo = ?, nombre = ?, stock = ?, " +
                    "almacen = ?, id_categoria = ?, proveedor = ? WHERE id_insumo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, insumo.getCodigo());
            ps.setString(2, insumo.getNombre());
            ps.setInt(3, insumo.getStock());
            ps.setString(4, insumo.getAlmacen());
            ps.setInt(5, insumo.getIdCategoria());
            ps.setString(6, insumo.getProveedor());
            ps.setInt(7, insumo.getIdInsumo());
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar insumo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar insumo
     */
    public boolean eliminar(int idInsumo) {
        String sql = "DELETE FROM InventarioInsumos WHERE id_insumo = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idInsumo);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar insumo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buscar insumos por código
     */
    public List<Insumo> buscarPorCodigo(String codigo) {
        List<Insumo> insumos = new ArrayList<>();
        String sql = "SELECT i.id_insumo, i.codigo, i.nombre, i.stock, i.almacen, " +
                    "i.id_categoria, c.nombre AS categoria, i.proveedor " +
                    "FROM InventarioInsumos i " +
                    "LEFT JOIN Categorias c ON i.id_categoria = c.id_categoria " +
                    "WHERE i.codigo LIKE ? ORDER BY i.nombre";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + codigo + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Insumo insumo = crearInsumoDesdeResultSet(rs);
                insumos.add(insumo);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar insumos: " + e.getMessage());
        }
        
        return insumos;
    }

    /**
     * Obtener insumos con stock bajo
     */
    public List<Insumo> obtenerConStockBajo(int umbral) {
        List<Insumo> insumos = new ArrayList<>();
        String sql = "SELECT i.id_insumo, i.codigo, i.nombre, i.stock, i.almacen, " +
                    "i.id_categoria, c.nombre AS categoria, i.proveedor " +
                    "FROM InventarioInsumos i " +
                    "LEFT JOIN Categorias c ON i.id_categoria = c.id_categoria " +
                    "WHERE i.stock < ? ORDER BY i.stock ASC";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, umbral);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Insumo insumo = crearInsumoDesdeResultSet(rs);
                insumos.add(insumo);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener insumos con stock bajo: " + e.getMessage());
        }
        
        return insumos;
    }

    /**
     * Crear objeto Insumo desde ResultSet
     */
    private Insumo crearInsumoDesdeResultSet(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setIdInsumo(rs.getInt("id_insumo"));
        insumo.setCodigo(rs.getString("codigo"));
        insumo.setNombre(rs.getString("nombre"));
        insumo.setStock(rs.getInt("stock"));
        insumo.setAlmacen(rs.getString("almacen"));
        insumo.setIdCategoria(rs.getInt("id_categoria"));
        insumo.setNombreCategoria(rs.getString("categoria"));
        insumo.setProveedor(rs.getString("proveedor"));
        return insumo;
    }
}