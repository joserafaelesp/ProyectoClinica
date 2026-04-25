package Logical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenDAO extends BaseDAO {

    // Insertar examen en catálogo
    public boolean insertar(Examen e) {
        return ejecutar(
            "INSERT INTO EXAMEN (Id_Examen,NombreExamen,Descripcion) VALUES (?,?,?)",
            e.getIdExamen(), e.getNombreExamen(), e.getDescripcion());
    }

    // Listar todos los examenes del catálogo
    public ArrayList<Examen> listarTodos() {
        ArrayList<Examen> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT Id_Examen,NombreExamen,Descripcion FROM EXAMEN ORDER BY NombreExamen")) {
            while (rs.next())
                lista.add(new Examen(
                    rs.getString("Id_Examen"),
                    rs.getString("NombreExamen"),
                    rs.getString("Descripcion")));
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // Buscar examen por ID
    public Examen buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Examen,NombreExamen,Descripcion FROM EXAMEN WHERE Id_Examen=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Examen(
                        rs.getString("Id_Examen"),
                        rs.getString("NombreExamen"),
                        rs.getString("Descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    // Listar examenes de una consulta via CONSULTA_EXAMEN
    public List<Examen> listarPorConsulta(String idConsulta) {
        List<Examen> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT e.Id_Examen, e.NombreExamen, e.Descripcion"
            + " FROM CONSULTA_EXAMEN ce"
            + " JOIN EXAMEN e ON ce.Id_Examen = e.Id_Examen"
            + " WHERE ce.Id_Consulta = ?"
            + " ORDER BY e.NombreExamen")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(new Examen(
                        rs.getString("Id_Examen"),
                        rs.getString("NombreExamen"),
                        rs.getString("Descripcion"), null));
            }
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.listarPorConsulta: " + e.getMessage());
        }
        return lista;
    }

    // Vincular examen a consulta en CONSULTA_EXAMEN
    public boolean vincularAConsulta(String idConsulta, String idExamen) {
        return ejecutar(
            "INSERT INTO CONSULTA_EXAMEN (Id_Consulta, Id_Examen) VALUES (?,?)",
            idConsulta, idExamen);
    }

    // Borrar vínculos al eliminar una consulta
    public boolean borrarVinculosConsulta(String idConsulta) {
        return ejecutar(
            "DELETE FROM CONSULTA_EXAMEN WHERE Id_Consulta=?", idConsulta);
    }

    public boolean actualizar(Examen e) {
        return ejecutar(
            "UPDATE EXAMEN SET NombreExamen=?,Descripcion=? WHERE Id_Examen=?",
            e.getNombreExamen(), e.getDescripcion(), e.getIdExamen());
    }

    public boolean eliminar(String id) {
        ejecutar("DELETE FROM CONSULTA_EXAMEN WHERE Id_Examen=?", id);
        return ejecutar("DELETE FROM EXAMEN WHERE Id_Examen=?", id);
    }
}