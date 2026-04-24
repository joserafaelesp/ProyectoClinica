package Logical;
import java.sql.*;
import java.util.ArrayList;
public class ViviendaDAO extends BaseDAO {
    public boolean insertar(Vivienda v) {
        return ejecutar("INSERT INTO VIVIENDA (Id_Vivienda,Direccion) VALUES (?,?)",
            v.getIdVivienda(), v.getDireccion());
    }
    public ArrayList<Vivienda> listarTodos() {
        ArrayList<Vivienda> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Vivienda,Direccion FROM VIVIENDA")) {
            while (rs.next()) lista.add(new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion")));
        } catch (SQLException e) { System.out.println("Error ViviendaDAO: " + e.getMessage()); }
        return lista;
    }
    public Vivienda buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement("SELECT Id_Vivienda,Direccion FROM VIVIENDA WHERE Id_Vivienda=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion"));
            }
        } catch (SQLException e) { System.out.println("Error ViviendaDAO: " + e.getMessage()); }
        return null;
    }
    public boolean actualizar(Vivienda v) {
        return ejecutar("UPDATE VIVIENDA SET Direccion=? WHERE Id_Vivienda=?", v.getDireccion(), v.getIdVivienda());
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM VIVIENDA WHERE Id_Vivienda=?", id); }
}