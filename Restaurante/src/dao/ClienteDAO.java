package dao;

import conexion.ConexionSQL;
import models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla Clientes
 */
public class ClienteDAO {

    /**
     * Obtener todos los clientes
     */
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY apellidos, nombres";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = crearClienteDesdeResultSet(rs);
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        
        return clientes;
    }

    /**
     * Obtener cliente por ID
     */
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        Cliente cliente = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cliente = crearClienteDesdeResultSet(rs);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        
        return cliente;
    }

    /**
     * Insertar nuevo cliente
     */
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nombres, apellidos, dni, direccion, telefono, correo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getCorreo());
            
            int filasInsertadas = ps.executeUpdate();
            
            if (filasInsertadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
                rs.close();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Actualizar cliente existente
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Clientes SET nombres = ?, apellidos = ?, dni = ?, " +
                    "direccion = ?, telefono = ?, correo = ? WHERE id_cliente = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getCorreo());
            ps.setInt(7, cliente.getIdCliente());
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar cliente
     */
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idCliente);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buscar clientes por DNI
     */
    public List<Cliente> buscarPorDNI(String dni) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE dni LIKE ? ORDER BY apellidos, nombres";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + dni + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = crearClienteDesdeResultSet(rs);
                clientes.add(cliente);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        
        return clientes;
    }

    /**
     * Buscar clientes por nombre
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE nombres LIKE ? OR apellidos LIKE ? " +
                    "ORDER BY apellidos, nombres";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String patron = "%" + nombre + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = crearClienteDesdeResultSet(rs);
                clientes.add(cliente);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes por nombre: " + e.getMessage());
        }
        
        return clientes;
    }

    /**
     * Crear objeto Cliente desde ResultSet
     */
    private Cliente crearClienteDesdeResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDni(rs.getString("dni"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        return cliente;
    }
}