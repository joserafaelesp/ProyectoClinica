package Logical;
import java.sql.*;
import java.util.ArrayList;

public class EnfermedadDAO extends BaseDAO {

    public boolean insertar(Enfermedad e) {
        return ejecutar(
            "INSERT INTO ENFERMEDAD (Id_Enfermedad,NombreEnfermedad,Sintomas,Id_Gravedad) VALUES (?,?,?,?)",
            e.getIdEnfermedad(), e.getNombreEnfermedad(), e.getSintomas(),
            e.getGravedad() != null ? e.getGravedad().getIdGravedad() : null);
    }

    public ArrayList<Enfermedad> listarTodos() {
        ArrayList<Enfermedad> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT e.Id_Enfermedad, e.NombreEnfermedad, e.Sintomas,"
                + " g.Id_Gravedad, g.Gravedad"
                + " FROM ENFERMEDAD e"
                + " LEFT JOIN GRAVEDAD_ENFERMEDAD g ON e.Id_Gravedad = g.Id_Gravedad"
                + " ORDER BY e.NombreEnfermedad")) {
            while (rs.next()) {
                Gravedadenfermedad grav = rs.getString("Id_Gravedad") != null
                    ? new Gravedadenfermedad(
                        rs.getString("Id_Gravedad"),
                        rs.getString("Gravedad")) : null;
                lista.add(new Enfermedad(
                    rs.getString("Id_Enfermedad"),
                    rs.getString("NombreEnfermedad"),
                    rs.getString("Sintomas"), grav));
            }
        } catch (SQLException e) {
            System.out.println("Error EnfermedadDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // FIX: ahora hace JOIN con GRAVEDAD_ENFERMEDAD para traer Id_Gravedad y Gravedad
    // Antes retornaba el objeto con gravedad = null, por eso el combo no se preseleccionaba
    public Enfermedad buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT e.Id_Enfermedad, e.NombreEnfermedad, e.Sintomas,"
            + " g.Id_Gravedad, g.Gravedad"
            + " FROM ENFERMEDAD e"
            + " LEFT JOIN GRAVEDAD_ENFERMEDAD g ON e.Id_Gravedad = g.Id_Gravedad"
            + " WHERE e.Id_Enfermedad = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Gravedadenfermedad grav = rs.getString("Id_Gravedad") != null
                        ? new Gravedadenfermedad(
                            rs.getString("Id_Gravedad"),
                            rs.getString("Gravedad")) : null;
                    return new Enfermedad(
                        rs.getString("Id_Enfermedad"),
                        rs.getString("NombreEnfermedad"),
                        rs.getString("Sintomas"), grav);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error EnfermedadDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Enfermedad e) {
        return ejecutar(
            "UPDATE ENFERMEDAD SET NombreEnfermedad=?,Sintomas=?,Id_Gravedad=? WHERE Id_Enfermedad=?",
            e.getNombreEnfermedad(), e.getSintomas(),
            e.getGravedad() != null ? e.getGravedad().getIdGravedad() : null,
            e.getIdEnfermedad());
    }

    public boolean eliminar(String id) {
        return ejecutar("DELETE FROM ENFERMEDAD WHERE Id_Enfermedad=?", id);
    }
}