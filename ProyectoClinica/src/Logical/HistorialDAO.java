package Logical;
import java.sql.*;

public class HistorialDAO extends BaseDAO {
    public boolean insertar(String idHistorial, String cedula) {
        return ejecutar("INSERT INTO HISTORIAL_MEDICO (Id_Historial,cedula) VALUES (?,?)",
            idHistorial, cedula);
    }
    public String buscarPorPaciente(String cedula) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Historial FROM HISTORIAL_MEDICO WHERE cedula=?")) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("Id_Historial");
            }
        } catch (SQLException e) { System.out.println("Error HistorialDAO: " + e.getMessage()); }
        return null;
    }
    public boolean eliminar(String idHistorial) {
        return ejecutar("DELETE FROM HISTORIAL_MEDICO WHERE Id_Historial=?", idHistorial);
    }
}