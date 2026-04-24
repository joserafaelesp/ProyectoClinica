package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConexionSQL {

    private static final String URL  = "jdbc:sqlserver://localhost:1433;databaseName=ProyectoClinica;encrypt=true;trustServerCertificate=true";
    private static final String USER = "clinica";
    private static final String PASS = "12345";


    private static Connection conexion = null;
    private ConexionSQL() {}
    
    /**
     * Crea una conexión nueva cada vez.
     * Simple y confiable — sin Singleton que cause problemas.
     */
    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}