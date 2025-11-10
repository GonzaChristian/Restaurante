package dao;

import conexion.ConexionSQL;
import models.Pedido;
import models.DetallePedido;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con las tablas Pedidos y DetallePedido
 */
public class PedidoDAO {

    /**
     * Insertar un nuevo pedido con sus detalles (transacción)
     */
    public boolean insertarPedido(Pedido pedido) {
        Connection con = null;
        PreparedStatement psPedido = null;
        PreparedStatement psDetalle = null;
        
        String sqlPedido = "INSERT INTO Pedidos (id_mesa, id_empleado, id_cliente, fecha, hora, subtotal, igv, total, estado) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DetallePedido (id_pedido, id_platillo, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        
        try {
            con = ConexionSQL.conectar();
            con.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar pedido
            psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, pedido.getIdMesa());
            psPedido.setObject(2, pedido.getIdEmpleado());
            psPedido.setObject(3, pedido.getIdCliente());
            psPedido.setDate(4, Date.valueOf(pedido.getFecha()));
            psPedido.setTime(5, Time.valueOf(pedido.getHora()));
            psPedido.setDouble(6, pedido.getSubtotal());
            psPedido.setDouble(7, pedido.getIgv());
            psPedido.setDouble(8, pedido.getTotal());
            psPedido.setString(9, pedido.getEstado());
            
            int filasInsertadas = psPedido.executeUpdate();
            
            if (filasInsertadas > 0) {
                // Obtener el ID del pedido generado
                ResultSet rs = psPedido.getGeneratedKeys();
                if (rs.next()) {
                    int idPedido = rs.getInt(1);
                    pedido.setIdPedido(idPedido);
                    
                    // 2. Insertar detalles del pedido
                    psDetalle = con.prepareStatement(sqlDetalle);
                    for (DetallePedido detalle : pedido.getDetalles()) {
                        psDetalle.setInt(1, idPedido);
                        psDetalle.setInt(2, detalle.getIdPlatillo());
                        psDetalle.setInt(3, detalle.getCantidad());
                        psDetalle.setDouble(4, detalle.getSubtotal());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                }
                rs.close();
            }
            
            con.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
            try {
                if (con != null) con.rollback(); // Revertir cambios
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
            
        } finally {
            try {
                if (psDetalle != null) psDetalle.close();
                if (psPedido != null) psPedido.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }

    /**
     * Obtener todos los pedidos
     */
    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.id_mesa, m.numero_mesa, p.id_empleado, p.id_cliente, " +
                     "p.fecha, p.hora, p.subtotal, p.igv, p.total, p.estado " +
                     "FROM Pedidos p " +
                     "INNER JOIN Mesas m ON p.id_mesa = m.id_mesa " +
                     "ORDER BY p.fecha DESC, p.hora DESC";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_mesa"),
                    (Integer) rs.getObject("id_empleado"),
                    (Integer) rs.getObject("id_cliente"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getTime("hora").toLocalTime(),
                    rs.getDouble("subtotal"),
                    rs.getDouble("igv"),
                    rs.getDouble("total"),
                    rs.getString("estado")
                );
                pedido.setNumeroMesa(rs.getInt("numero_mesa"));
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos: " + e.getMessage());
        }
        
        return pedidos;
    }

    /**
     * Obtener pedido por ID con sus detalles
     */
    public Pedido obtenerPorId(int idPedido) {
        String sqlPedido = "SELECT p.id_pedido, p.id_mesa, m.numero_mesa, p.id_empleado, p.id_cliente, " +
                          "p.fecha, p.hora, p.subtotal, p.igv, p.total, p.estado " +
                          "FROM Pedidos p " +
                          "INNER JOIN Mesas m ON p.id_mesa = m.id_mesa " +
                          "WHERE p.id_pedido = ?";
        
        String sqlDetalles = "SELECT d.id_detalle, d.id_pedido, d.id_platillo, pl.nombre AS nombre_platillo, " +
                            "d.cantidad, pl.precio, d.subtotal " +
                            "FROM DetallePedido d " +
                            "INNER JOIN Platillos pl ON d.id_platillo = pl.id_platillo " +
                            "WHERE d.id_pedido = ?";
        
        Pedido pedido = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement psPedido = con.prepareStatement(sqlPedido);
             PreparedStatement psDetalles = con.prepareStatement(sqlDetalles)) {
            
            // Obtener pedido
            psPedido.setInt(1, idPedido);
            ResultSet rsPedido = psPedido.executeQuery();
            
            if (rsPedido.next()) {
                pedido = new Pedido(
                    rsPedido.getInt("id_pedido"),
                    rsPedido.getInt("id_mesa"),
                    (Integer) rsPedido.getObject("id_empleado"),
                    (Integer) rsPedido.getObject("id_cliente"),
                    rsPedido.getDate("fecha").toLocalDate(),
                    rsPedido.getTime("hora").toLocalTime(),
                    rsPedido.getDouble("subtotal"),
                    rsPedido.getDouble("igv"),
                    rsPedido.getDouble("total"),
                    rsPedido.getString("estado")
                );
                pedido.setNumeroMesa(rsPedido.getInt("numero_mesa"));
                
                // Obtener detalles
                psDetalles.setInt(1, idPedido);
                ResultSet rsDetalles = psDetalles.executeQuery();
                
                List<DetallePedido> detalles = new ArrayList<>();
                while (rsDetalles.next()) {
                    DetallePedido detalle = new DetallePedido(
                        rsDetalles.getInt("id_detalle"),
                        rsDetalles.getInt("id_pedido"),
                        rsDetalles.getInt("id_platillo"),
                        rsDetalles.getString("nombre_platillo"),
                        rsDetalles.getInt("cantidad"),
                        rsDetalles.getDouble("precio")
                    );
                    detalles.add(detalle);
                }
                pedido.setDetalles(detalles);
                rsDetalles.close();
            }
            rsPedido.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pedido: " + e.getMessage());
        }
        
        return pedido;
    }

    /**
     * Obtener pedido activo de una mesa (estado "En Proceso")
     */
    public Pedido obtenerPedidoActivoPorMesa(int idMesa) {
        String sql = "SELECT TOP 1 id_pedido FROM Pedidos WHERE id_mesa = ? AND estado = 'En Proceso' ORDER BY fecha DESC, hora DESC";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idMesa);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                rs.close();
                return obtenerPorId(idPedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pedido activo: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Actualizar estado del pedido
     */
    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE Pedidos SET estado = ? WHERE id_pedido = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marcar pedido como completado
     */
    public boolean marcarCompletado(int idPedido) {
        return actualizarEstado(idPedido, "Completado");
    }

    /**
     * Obtener detalles de un pedido
     */
    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT d.id_detalle, d.id_pedido, d.id_platillo, pl.nombre AS nombre_platillo, " +
                    "d.cantidad, pl.precio, d.subtotal " +
                    "FROM DetallePedido d " +
                    "INNER JOIN Platillos pl ON d.id_platillo = pl.id_platillo " +
                    "WHERE d.id_pedido = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DetallePedido detalle = new DetallePedido(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_pedido"),
                    rs.getInt("id_platillo"),
                    rs.getString("nombre_platillo"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio")
                );
                detalles.add(detalle);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles: " + e.getMessage());
        }
        
        return detalles;
    }

    /**
     * Buscar pedidos por número de mesa
     */
    public List<Pedido> buscarPorMesa(int numeroMesa) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.id_mesa, m.numero_mesa, p.id_empleado, p.id_cliente, " +
                     "p.fecha, p.hora, p.subtotal, p.igv, p.total, p.estado " +
                     "FROM Pedidos p " +
                     "INNER JOIN Mesas m ON p.id_mesa = m.id_mesa " +
                     "WHERE m.numero_mesa = ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, numeroMesa);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_mesa"),
                    (Integer) rs.getObject("id_empleado"),
                    (Integer) rs.getObject("id_cliente"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getTime("hora").toLocalTime(),
                    rs.getDouble("subtotal"),
                    rs.getDouble("igv"),
                    rs.getDouble("total"),
                    rs.getString("estado")
                );
                pedido.setNumeroMesa(rs.getInt("numero_mesa"));
                pedidos.add(pedido);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedidos: " + e.getMessage());
        }
        
        return pedidos;
    }
}