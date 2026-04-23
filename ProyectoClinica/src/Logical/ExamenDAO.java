package Logical;

import java.sql.*;
import java.util.ArrayList;

/**
 * ExamenDAO — CRUD para la tabla EXAMEN.
 * Relación: CONSULTA 1:N EXAMEN (Ordena)
 */
public class ExamenDAO extends BaseDAO {

    public boolean insertar(Examen e) {
        return ejecutar(
            "INSERT INTO EXAMEN (Id_Examen,NombreExamen,Descripcion,Id_Consulta)"
            + " VALUES (?,?,?,?)",
            e.getIdExamen(), e.getNombreExamen(), e.getDescripcion(),
            e.getConsulta() != null ? e.getConsulta().getIdConsulta() : null
        );
    }

    public ArrayList<Examen> listarTodos() {
        ArrayList<Examen> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta"
                + " FROM EXAMEN ORDER BY NombreExamen")) {
            while (rs.next())
                lista.add(new Examen(rs.getString("Id_Examen"),
                    rs.getString("NombreExamen"), rs.getString("Descripcion"),
                    rs.getString("Id_Consulta")));
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene todos los exámenes de una consulta específica.
     */
    public ArrayList<Examen> listarPorConsulta(String idConsulta) {
        ArrayList<Examen> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta"
            + " FROM EXAMEN WHERE Id_Consulta=? ORDER BY NombreExamen")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(new Examen(rs.getString("Id_Examen"),
                        rs.getString("NombreExamen"), rs.getString("Descripcion"),
                        rs.getString("Id_Consulta")));
            }
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.listarPorConsulta: " + e.getMessage());
        }
        return lista;
    }

    public Examen buscarPorId(String idExamen) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Examen,NombreExamen,Descripcion,Id_Consulta"
            + " FROM EXAMEN WHERE Id_Examen=?")) {
            ps.setString(1, idExamen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Examen(rs.getString("Id_Examen"),
                        rs.getString("NombreExamen"), rs.getString("Descripcion"),
                        rs.getString("Id_Consulta"));
            }
        } catch (SQLException e) {
            System.out.println("Error ExamenDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Examen e) {
        return ejecutar(
            "UPDATE EXAMEN SET NombreExamen=?,Descripcion=? WHERE Id_Examen=?",
            e.getNombreExamen(), e.getDescripcion(), e.getIdExamen());
    }

    public boolean eliminar(String idExamen) {
        return ejecutar("DELETE FROM EXAMEN WHERE Id_Examen=?", idExamen);
    }
}