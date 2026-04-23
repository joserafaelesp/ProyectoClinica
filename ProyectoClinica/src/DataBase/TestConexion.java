package DataBase;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        System.out.println("Intentando conectar a SQL Server...");

        Connection con = ConexionSQL.getConexion();

        if (con != null) {
            System.out.println("CONEXION EXITOSA con clinica_sam");
            System.out.println("El programa puede leer y escribir en la BD");
        } else {
            System.out.println("FALLO la conexion");
            System.out.println("Verifica:");
            System.out.println("  1. SQL Server esta corriendo");
            System.out.println("  2. La BD clinica_sam existe en SSMS");
            System.out.println("  3. Usuario clinica y contrasena 12345 son correctos");
            System.out.println("  4. El .jar del driver esta en el Build Path");
        }
    }
}