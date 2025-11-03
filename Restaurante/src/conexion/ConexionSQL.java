
package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionSQL {
    // Datos de conexión (ajusta según tu configuración)
    private static final String URL = "jdbc:sqlserver://DESKTOP-2U3S037\\SQLEXPRESS:1435;databaseName=RestauranteMery;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "gonza";       // Tu usuario SQL
    private static final String PASSWORD = "123456"; // Tu contraseña SQL

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa con la base de datos RestauranteMery");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "❌ Error al conectar con SQL Server:\n" + e.getMessage(), 
                    "Error de conexión", 
                    JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }
    
}
