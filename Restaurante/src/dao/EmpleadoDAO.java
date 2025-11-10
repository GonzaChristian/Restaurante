package dao;

import conexion.ConexionSQL;
import models.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla Empleados
 */
public class EmpleadoDAO {

    /**
     * Obtener todos los empleados
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleados ORDER BY apellidos, nombres";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empleado emp = crearEmpleadoDesdeResultSet(rs);
                empleados.add(emp);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados: " + e.getMessage());
        }
        
        return empleados;
    }

    /**
     * Obtener empleado por ID
     */
    public Empleado obtenerPorId(int idEmpleado) {
        String sql = "SELECT * FROM Empleados WHERE id_empleado = ?";
        Empleado empleado = null;
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                empleado = crearEmpleadoDesdeResultSet(rs);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleado: " + e.getMessage());
        }
        
        return empleado;
    }

    /**
     * Insertar nuevo empleado
     */
    public boolean insertar(Empleado empleado) {
        String sql = "INSERT INTO Empleados (nombres, apellidos, dni, sexo, fecha_nacimiento, " +
                    "direccion, telefono, correo, usuario, contrasena, cargo, horario) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, empleado.getNombres());
            ps.setString(2, empleado.getApellidos());
            ps.setString(3, empleado.getDni());
            ps.setString(4, empleado.getSexo());
            ps.setDate(5, empleado.getFechaNacimiento() != null ? 
                       Date.valueOf(empleado.getFechaNacimiento()) : null);
            ps.setString(6, empleado.getDireccion());
            ps.setString(7, empleado.getTelefono());
            ps.setString(8, empleado.getCorreo());
            ps.setString(9, empleado.getUsuario());
            ps.setString(10, empleado.getContrasena());
            ps.setString(11, empleado.getCargo());
            ps.setString(12, empleado.getHorario());
            
            int filasInsertadas = ps.executeUpdate();
            
            if (filasInsertadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    empleado.setIdEmpleado(rs.getInt(1));
                }
                rs.close();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Actualizar empleado existente
     */
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE Empleados SET nombres = ?, apellidos = ?, dni = ?, sexo = ?, " +
                    "fecha_nacimiento = ?, direccion = ?, telefono = ?, correo = ?, " +
                    "usuario = ?, contrasena = ?, cargo = ?, horario = ? " +
                    "WHERE id_empleado = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, empleado.getNombres());
            ps.setString(2, empleado.getApellidos());
            ps.setString(3, empleado.getDni());
            ps.setString(4, empleado.getSexo());
            ps.setDate(5, empleado.getFechaNacimiento() != null ? 
                       Date.valueOf(empleado.getFechaNacimiento()) : null);
            ps.setString(6, empleado.getDireccion());
            ps.setString(7, empleado.getTelefono());
            ps.setString(8, empleado.getCorreo());
            ps.setString(9, empleado.getUsuario());
            ps.setString(10, empleado.getContrasena());
            ps.setString(11, empleado.getCargo());
            ps.setString(12, empleado.getHorario());
            ps.setInt(13, empleado.getIdEmpleado());
            
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar empleado
     */
    public boolean eliminar(int idEmpleado) {
        String sql = "DELETE FROM Empleados WHERE id_empleado = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEmpleado);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buscar empleados por DNI
     */
    public List<Empleado> buscarPorDNI(String dni) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleados WHERE dni LIKE ? ORDER BY apellidos, nombres";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + dni + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Empleado emp = crearEmpleadoDesdeResultSet(rs);
                empleados.add(emp);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar empleados: " + e.getMessage());
        }
        
        return empleados;
    }

    /**
     * Verificar si existe un usuario
     */
    public boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM Empleados WHERE usuario = ?";
        
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Crear objeto Empleado desde ResultSet
     */
    private Empleado crearEmpleadoDesdeResultSet(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        emp.setIdEmpleado(rs.getInt("id_empleado"));
        emp.setNombres(rs.getString("nombres"));
        emp.setApellidos(rs.getString("apellidos"));
        emp.setDni(rs.getString("dni"));
        emp.setSexo(rs.getString("sexo"));
        
        Date fecha = rs.getDate("fecha_nacimiento");
        if (fecha != null) {
            emp.setFechaNacimiento(fecha.toLocalDate());
        }
        
        emp.setDireccion(rs.getString("direccion"));
        emp.setTelefono(rs.getString("telefono"));
        emp.setCorreo(rs.getString("correo"));
        emp.setUsuario(rs.getString("usuario"));
        emp.setContrasena(rs.getString("contrasena"));
        emp.setCargo(rs.getString("cargo"));
        emp.setHorario(rs.getString("horario"));
        
        return emp;
    }
}