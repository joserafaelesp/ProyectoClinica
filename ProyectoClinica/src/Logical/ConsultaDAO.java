package Logical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO extends BaseDAO {

    /**
     * Inserta consulta + enfermedades en una sola transacción.
     */
    public boolean insertarConEnfermedades(Consultas consulta,
                                           List<String> idsEnfermedades,
                                           String idHistorial) {
        try {
            con.setAutoCommit(false);
            // Insertar CONSULTA
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO CONSULTA (Id_Consulta,Fecha_Consulta,Diagnostico,"
                + "Id_Historial,cedula) VALUES (?,?,?,?,?)")) {
                ps.setString(1, consulta.getIdConsulta());
                ps.setDate(2, new java.sql.Date(consulta.getFechaConsulta().getTime()));
                ps.setString(3, consulta.getDiagnostico());
                ps.setString(4, idHistorial);
                ps.setString(5, consulta.getDoctor() != null
                    ? consulta.getDoctor().getCedula() : null);
                ps.executeUpdate();
            }
            // Insertar enfermedades N:M
            if (idsEnfermedades != null && !idsEnfermedades.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO CONSULTA_ENFERMEDAD (Id_Consulta,Id_Enfermedad) VALUES (?,?)")) {
                    for (String idEnf : idsEnfermedades) {
                        ps.setString(1, consulta.getIdConsulta());
                        ps.setString(2, idEnf);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            con.commit(); con.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) {}
            System.out.println("Error ConsultaDAO.insertar (rollback): " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Consultas> listarTodos() {
        ArrayList<Consultas> lista = new ArrayList<>();
        String sql = "SELECT Id_Consulta,Fecha_Consulta,Diagnostico,Id_Historial,cedula"
                   + " FROM CONSULTA ORDER BY Fecha_Consulta DESC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            MedicoDAO mD = new MedicoDAO();
            while (rs.next()) {
                Medico med = mD.buscarPorId(rs.getString("cedula"));
                List<Enfermedad> enfs = obtenerEnfermedadesDe(rs.getString("Id_Consulta"));
                Consultas c = new Consultas(rs.getString("Id_Consulta"),
                    rs.getDate("Fecha_Consulta"), rs.getString("Diagnostico"), med, null);
                c.setEnfermedades(enfs);
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error ConsultaDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Consultas> listarPorPaciente(String cedula) {
        ArrayList<Consultas> lista = new ArrayList<>();
        String sql =
            "SELECT c.Id_Consulta,c.Fecha_Consulta,c.Diagnostico,c.cedula"
            + " FROM CONSULTA c"
            + " JOIN HISTORIAL_MEDICO h ON c.Id_Historial=h.Id_Historial"
            + " WHERE h.cedula=? ORDER BY c.Fecha_Consulta DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            MedicoDAO mD = new MedicoDAO();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medico med = mD.buscarPorId(rs.getString("cedula"));
                    List<Enfermedad> enfs = obtenerEnfermedadesDe(rs.getString("Id_Consulta"));
                    Consultas c = new Consultas(rs.getString("Id_Consulta"),
                        rs.getDate("Fecha_Consulta"), rs.getString("Diagnostico"), med, null);
                    c.setEnfermedades(enfs);
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error ConsultaDAO.listarPorPaciente: " + e.getMessage());
        }
        return lista;
    }

    private List<Enfermedad> obtenerEnfermedadesDe(String idConsulta) {
        List<Enfermedad> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT e.Id_Enfermedad,e.NombreEnfermedad,e.Sintomas"
            + " FROM CONSULTA_ENFERMEDAD ce"
            + " JOIN ENFERMEDAD e ON ce.Id_Enfermedad=e.Id_Enfermedad"
            + " WHERE ce.Id_Consulta=?")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(new Enfermedad(rs.getString("Id_Enfermedad"),
                        rs.getString("NombreEnfermedad"), rs.getString("Sintomas"), null));
            }
        } catch (SQLException e) {
            System.out.println("Error ConsultaDAO.getEnfs: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Consultas c) {
        return ejecutar(
            "UPDATE CONSULTA SET Fecha_Consulta=?,Diagnostico=? WHERE Id_Consulta=?",
            new java.sql.Date(c.getFechaConsulta().getTime()),
            c.getDiagnostico(), c.getIdConsulta());
    }

    public boolean eliminar(String idConsulta) {
        return ejecutar("DELETE FROM CONSULTA WHERE Id_Consulta=?", idConsulta);
    }

    public List<Enfermedad> listarEnfermedadesDiagnosticadas() {
        List<Enfermedad> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT DISTINCT e.Id_Enfermedad,e.NombreEnfermedad,e.Sintomas"
                + " FROM CONSULTA_ENFERMEDAD ce"
                + " JOIN ENFERMEDAD e ON ce.Id_Enfermedad=e.Id_Enfermedad")) {
            while (rs.next())
                lista.add(new Enfermedad(rs.getString("Id_Enfermedad"),
                    rs.getString("NombreEnfermedad"), rs.getString("Sintomas"), null));
        } catch (SQLException e) {
            System.out.println("Error ConsultaDAO.listarEnfs: " + e.getMessage());
        }
        return lista;
    }
}