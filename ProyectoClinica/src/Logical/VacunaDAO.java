package Logical;
import java.sql.*;
import java.util.ArrayList;
public class VacunaDAO extends BaseDAO {
    public boolean insertar(Vacuna v) {
        return ejecutar("INSERT INTO VACUNA (Id_Vacuna,NombreVacuna,Descripcion) VALUES (?,?,?)",
            v.getIdVacuna(), v.getNombreVacuna(), v.getDescripcion());
    }
    public ArrayList<Vacuna> listarTodos() {
        ArrayList<Vacuna> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Vacuna,NombreVacuna,Descripcion FROM VACUNA ORDER BY NombreVacuna")) {
            while (rs.next())
                lista.add(new Vacuna(rs.getString("Id_Vacuna"), rs.getString("NombreVacuna"), rs.getString("Descripcion")));
        } catch (SQLException e) { System.out.println("Error VacunaDAO: " + e.getMessage()); }
        return lista;
    }
    public Vacuna buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Vacuna,NombreVacuna,Descripcion FROM VACUNA WHERE Id_Vacuna=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Vacuna(rs.getString("Id_Vacuna"), rs.getString("NombreVacuna"), rs.getString("Descripcion"));
            }
        } catch (SQLException e) { System.out.println("Error VacunaDAO: " + e.getMessage()); }
        return null;
    }
    public boolean actualizar(Vacuna v) {
        return ejecutar("UPDATE VACUNA SET NombreVacuna=?,Descripcion=? WHERE Id_Vacuna=?",
            v.getNombreVacuna(), v.getDescripcion(), v.getIdVacuna());
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM VACUNA WHERE Id_Vacuna=?", id); }
}