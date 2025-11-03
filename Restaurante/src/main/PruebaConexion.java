package main;

import conexion.ConexionSQL;
import java.sql.Connection;

public class PruebaConexion {
    public static void main(String[] args) {
        Connection con = ConexionSQL.conectar();
        if (con != null) {
            System.out.println("🎉 Conexión verificada correctamente.");
        } else {
            System.out.println("⚠️ No se pudo establecer conexión.");
        }
    }
}