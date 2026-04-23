package Logical;
import java.sql.*;
import java.util.ArrayList;

public class GravedadenfermedadDAO extends BaseDAO {
    public boolean insertar(Gravedadenfermedad g) {
        return ejecutar("INSERT INTO GRAVEDAD_ENFERMEDAD (Id_Gravedad,Gravedad) VALUES (?,?)",
            g.getIdGravedad(), g.getGravedad());
    }
    public ArrayList<Gravedadenfermedad> listarTodos() {
        ArrayList<Gravedadenfermedad> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Gravedad,Gravedad FROM GRAVEDAD_ENFERMEDAD ORDER BY Gravedad")) {
            while (rs.next()) lista.add(new Gravedadenfermedad(rs.getString("Id_Gravedad"), rs.getString("Gravedad")));
        } catch (SQLException e) { System.out.println("Error GravedadDAO: " + e.getMessage()); }
        return lista;
    }
    public Gravedadenfermedad buscarPorId(String id) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Gravedad,Gravedad FROM GRAVEDAD_ENFERMEDAD WHERE Id_Gravedad=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Gravedadenfermedad(rs.getString("Id_Gravedad"), rs.getString("Gravedad"));
            }
        } catch (SQLException e) { System.out.println("Error GravedadDAO: " + e.getMessage()); }
        return null;
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM GRAVEDAD_ENFERMEDAD WHERE Id_Gravedad=?", id); }
}