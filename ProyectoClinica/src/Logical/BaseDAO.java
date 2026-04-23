package Logical;

import DataBase.ConexionSQL;
import java.sql.*;

public abstract class BaseDAO {

    protected final Connection con = ConexionSQL.getConexion();

    protected boolean ejecutar(String sql, Object... params) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ponerParametros(ps, params);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar SQL: " + e.getMessage());
            return false;
        }
    }

    protected void ponerParametros(PreparedStatement ps, Object... params)
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p == null)
                ps.setNull(i + 1, Types.NULL);
            else if (p instanceof String)
                ps.setString(i + 1, (String) p);
            else if (p instanceof Integer)
                ps.setInt(i + 1, (Integer) p);
            else if (p instanceof Double)
                ps.setDouble(i + 1, (Double) p);
            else if (p instanceof Boolean)
                ps.setBoolean(i + 1, (Boolean) p);
            else if (p instanceof java.sql.Date)
                ps.setDate(i + 1, (java.sql.Date) p);
            else if (p instanceof java.util.Date)
                ps.setDate(i + 1, new java.sql.Date(((java.util.Date) p).getTime()));
            else
                ps.setObject(i + 1, p);
        }
    }

    public int obtenerMaxNumero(String tabla, String columnaId, String prefijo) {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(" + columnaId
                   + ", LEN(?) + 1, LEN(" + columnaId + ")) AS INT)), 0)"
                   + " FROM " + tabla + " WHERE " + columnaId + " LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prefijo);
            ps.setString(2, prefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { /* tabla vacía */ }
        return 0;
    }
}