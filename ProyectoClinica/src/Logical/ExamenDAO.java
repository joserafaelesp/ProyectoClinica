package Logical;
import java.sql.*;
import java.util.ArrayList;
public class ExamenDAO extends BaseDAO {
    public boolean insertar(Examen e) {
        return ejecutar("INSERT INTO EXAMEN (Id_Examen,NombreExamen,Descripcion,Id_Consulta) VALUES (?,?,?,?)",
            e.getIdExamen(), e.getNombreExamen(), e.getDescripcion(),
            e.getConsulta()!=null?e.getConsulta().getIdConsulta():null);
    }
    public ArrayList<Examen> listarTodos() {
        ArrayList<Examen> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta FROM EXAMEN ORDER BY NombreExamen")) {
            while (rs.next()) lista.add(new Examen(rs.getString("Id_Examen"),
                rs.getString("NombreExamen"), rs.getString("Descripcion"), rs.getString("Id_Consulta")));
        } catch (SQLException e) { System.out.println("Error ExamenDAO: " + e.getMessage()); }
        return lista;
    }
    public ArrayList<Examen> listarPorConsulta(String idConsulta) {
        ArrayList<Examen> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta FROM EXAMEN WHERE Id_Consulta=? ORDER BY NombreExamen")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Examen(rs.getString("Id_Examen"),
                    rs.getString("NombreExamen"), rs.getString("Descripcion"), rs.getString("Id_Consulta")));
            }
        } catch (SQLException e) { System.out.println("Error ExamenDAO: " + e.getMessage()); }
        return lista;
    }
    public Examen buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta FROM EXAMEN WHERE Id_Examen=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Examen(rs.getString("Id_Examen"),
                    rs.getString("NombreExamen"), rs.getString("Descripcion"), rs.getString("Id_Consulta"));
            }
        } catch (SQLException e) { System.out.println("Error ExamenDAO: " + e.getMessage()); }
        return null;
    }
    public boolean actualizar(Examen e) {
        return ejecutar("UPDATE EXAMEN SET NombreExamen=?,Descripcion=? WHERE Id_Examen=?",
            e.getNombreExamen(), e.getDescripcion(), e.getIdExamen());
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM EXAMEN WHERE Id_Examen=?", id); }
}