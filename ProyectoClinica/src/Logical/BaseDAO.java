package Logical;

import DataBase.ConexionSQL;
import java.sql.*;

/**
 * Clase base para todos los DAO (Data Access Object).
 * Centraliza la ejecución de consultas SQL y manejo de parámetros.
 */
public abstract class BaseDAO {

    /**
     * Ejecuta operaciones INSERT, UPDATE o DELETE.
     * 
     * Cada operación abre su propia conexión, ejecuta la consulta
     * y se asegura de cerrarla automáticamente con try-with-resources.
     * Esto ayuda a evitar problemas de confirmación (commit).
     * 
     * @param sql Consulta SQL a ejecutar
     * @param params Parámetros para la consulta
     * @return true si se afectó al menos una fila
     */
    protected boolean ejecutar(String sql, Object... params) {

        // Obtener conexión desde la clase de conexión
        Connection con = ConexionSQL.getConexion();

        if (con == null) {
            System.out.println("Error: conexión null en ejecutar()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignar parámetros dinámicamente
            ponerParametros(ps, params);

            // Ejecutar la consulta
            int filas = ps.executeUpdate();

            // Log simple de la operación (INSERT, UPDATE, DELETE)
            System.out.println("[SQL] " + sql.split(" ")[0]
                    + " → " + filas + " fila(s) afectada(s)");

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al ejecutar SQL: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }

    /**
     * Asigna los parámetros al PreparedStatement de forma dinámica
     * según el tipo de dato.
     * 
     * @param ps PreparedStatement
     * @param params Lista de parámetros
     * @throws SQLException si ocurre un error al asignar
     */
    protected void ponerParametros(PreparedStatement ps, Object... params)
            throws SQLException {

        for (int i = 0; i < params.length; i++) {

            Object p = params[i];

            // Manejo de valores null
            if (p == null) {
                ps.setNull(i + 1, Types.VARCHAR);

            // Diferentes tipos de datos soportados
            } else if (p instanceof String) {
                ps.setString(i + 1, (String) p);

            } else if (p instanceof Integer) {
                ps.setInt(i + 1, (Integer) p);

            } else if (p instanceof Double) {
                ps.setDouble(i + 1, (Double) p);

            } else if (p instanceof Boolean) {
                ps.setBoolean(i + 1, (Boolean) p);

            } else if (p instanceof java.sql.Date) {
                ps.setDate(i + 1, (java.sql.Date) p);

            } else if (p instanceof java.util.Date) {
                // Conversión de util.Date a sql.Date
                ps.setDate(i + 1,
                        new java.sql.Date(((java.util.Date) p).getTime()));

            } else {
                // Tipo genérico (fallback)
                ps.setObject(i + 1, p);
            }
        }
    }

    /**
     * Obtiene el número máximo de un ID con prefijo.
     * 
     * Ejemplo:
     * Si tienes IDs tipo "CLI001", "CLI002", devuelve el mayor número (2).
     * 
     * @param tabla Nombre de la tabla
     * @param columnaId Nombre de la columna ID
     * @param prefijo Prefijo del ID (ej: "CLI")
     * @return número máximo encontrado o 0 si no hay registros
     */
    public int obtenerMaxNumero(String tabla, String columnaId, String prefijo) {

        Connection con = ConexionSQL.getConexion();
        if (con == null) return 0;

        // Consulta que extrae la parte numérica del ID
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(" + columnaId
                + ", LEN(?) + 1, LEN(" + columnaId + ")) AS INT)), 0)"
                + " FROM " + tabla
                + " WHERE " + columnaId + " LIKE ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            // Prefijo para calcular longitud y filtro LIKE
            ps.setString(1, prefijo);
            ps.setString(2, prefijo + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            // Si la tabla está vacía o hay error, retorna 0
        }

        return 0;
    }

    /**
     * Devuelve una conexión para consultas SELECT.
     * 
     * IMPORTANTE:
     * El que llama este método es responsable de cerrar
     * el PreparedStatement y ResultSet.
     */
    protected Connection getConexion() {
        return ConexionSQL.getConexion();
    }
}