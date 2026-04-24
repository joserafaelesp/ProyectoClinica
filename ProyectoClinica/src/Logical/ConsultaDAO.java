package Logical;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ConsultaDAO extends BaseDAO {
    public boolean insertarConEnfermedades(Consultas consulta,
                                           List<String> idsEnfermedades,
                                           String idHistorial) {
        try {
            Connection con = getConexion();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO CONSULTA (Id_Consulta,Fecha_Consulta,Diagnostico,Id_Historial,cedula) VALUES (?,?,?,?,?)")) {
                ps.setString(1, consulta.getIdConsulta());
                ps.setDate(2, new java.sql.Date(consulta.getFechaConsulta().getTime()));
                ps.setString(3, consulta.getDiagnostico());
                ps.setString(4, idHistorial);
                ps.setString(5, consulta.getDoctor()!=null?consulta.getDoctor().getCedula():null);
                ps.executeUpdate();
            }
            if (idsEnfermedades!=null && !idsEnfermedades.isEmpty()) {
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
            System.out.println("Error ConsultaDAO.insertar: " + e.getMessage());
            return false;
        }
    }
    public ArrayList<Consultas> listarTodos() {
        ArrayList<Consultas> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT Id_Consulta,Fecha_Consulta,Diagnostico,cedula FROM CONSULTA ORDER BY Fecha_Consulta DESC")) {
            MedicoDAO mD = new MedicoDAO();
            while (rs.next()) {
                Consultas c = new Consultas(rs.getString("Id_Consulta"),
                    rs.getDate("Fecha_Consulta"), rs.getString("Diagnostico"),
                    mD.buscarPorId(rs.getString("cedula")), null);
                lista.add(c);
            }
        } catch (SQLException e) { System.out.println("Error ConsultaDAO.listarTodos: " + e.getMessage()); }
        return lista;
    }
    public ArrayList<Consultas> listarPorPaciente(String cedula) {
        ArrayList<Consultas> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT c.Id_Consulta,c.Fecha_Consulta,c.Diagnostico,c.cedula"
            + " FROM CONSULTA c JOIN HISTORIAL_MEDICO h ON c.Id_Historial=h.Id_Historial"
            + " WHERE h.cedula=? ORDER BY c.Fecha_Consulta DESC")) {
            ps.setString(1, cedula);
            MedicoDAO mD = new MedicoDAO();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consultas c = new Consultas(rs.getString("Id_Consulta"),
                        rs.getDate("Fecha_Consulta"), rs.getString("Diagnostico"),
                        mD.buscarPorId(rs.getString("cedula")), null);
                    lista.add(c);
                }
            }
        } catch (SQLException e) { System.out.println("Error ConsultaDAO.listarPorPaciente: " + e.getMessage()); }
        return lista;
    }
    public boolean actualizar(Consultas c) {
        return ejecutar("UPDATE CONSULTA SET Fecha_Consulta=?,Diagnostico=? WHERE Id_Consulta=?",
            new java.sql.Date(c.getFechaConsulta().getTime()), c.getDiagnostico(), c.getIdConsulta());
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM CONSULTA WHERE Id_Consulta=?", id); }
    public List<Enfermedad> listarEnfermedadesDiagnosticadas() {
        List<Enfermedad> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT DISTINCT e.Id_Enfermedad,e.NombreEnfermedad,e.Sintomas"
                + " FROM CONSULTA_ENFERMEDAD ce JOIN ENFERMEDAD e ON ce.Id_Enfermedad=e.Id_Enfermedad")) {
            while (rs.next()) lista.add(new Enfermedad(rs.getString("Id_Enfermedad"),
                rs.getString("NombreEnfermedad"), rs.getString("Sintomas"), null));
        } catch (SQLException e) { System.out.println("Error ConsultaDAO.listarEnfs: " + e.getMessage()); }
        return lista;
    }
}