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
                "INSERT INTO CONSULTA (Id_Consulta,Fecha_Consulta,Diagnostico,Id_Historial,cedula,cedula_paciente) VALUES (?,?,?,?,?,?)")) {
                ps.setString(1, consulta.getIdConsulta());
                ps.setDate(2, new java.sql.Date(consulta.getFechaConsulta().getTime()));
                ps.setString(3, consulta.getDiagnostico());
                ps.setString(4, idHistorial);
                ps.setString(5, consulta.getDoctor()  != null ? consulta.getDoctor().getCedula()  : null);
                ps.setString(6, consulta.getPatient() != null ? consulta.getPatient().getCedula() : null);
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
            // Insertar vacunas aplicadas en CONSULTA_VACUNA
            List<String> idsVacunas = consulta.getVacunasAplicadas();
            if (idsVacunas != null && !idsVacunas.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO CONSULTA_VACUNA (Id_Consulta,Id_Vacuna) VALUES (?,?)")) {
                    for (String idVac : idsVacunas) {
                        ps.setString(1, consulta.getIdConsulta());
                        ps.setString(2, idVac);
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
                "SELECT Id_Consulta,Fecha_Consulta,Diagnostico,cedula,cedula_paciente FROM CONSULTA ORDER BY Fecha_Consulta DESC")) {
            MedicoDAO   mD = new MedicoDAO();
            PacienteDAO pD = new PacienteDAO();
            while (rs.next()) {
                Medico   med = mD.buscarPorId(rs.getString("cedula"));
                Paciente pac = pD.buscarPorId(rs.getString("cedula_paciente"));
                String idCon = rs.getString("Id_Consulta");
                Consultas c = new Consultas(
                    idCon,
                    rs.getDate("Fecha_Consulta"),
                    rs.getString("Diagnostico"),
                    med, pac);
                lista.add(c);
            }
        } catch (SQLException e) { System.out.println("Error ConsultaDAO.listarTodos: " + e.getMessage()); }
        return lista;
    }
    public ArrayList<Consultas> listarPorPaciente(String cedula) {
        ArrayList<Consultas> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT c.Id_Consulta,c.Fecha_Consulta,c.Diagnostico,c.cedula,c.cedula_paciente"
            + " FROM CONSULTA c JOIN HISTORIAL_MEDICO h ON c.Id_Historial=h.Id_Historial"
            + " WHERE h.cedula=? ORDER BY c.Fecha_Consulta DESC")) {
            ps.setString(1, cedula);
            MedicoDAO   mD = new MedicoDAO();
            PacienteDAO pD = new PacienteDAO();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medico   medLP = mD.buscarPorId(rs.getString("cedula"));
                    Paciente pacLP = pD.buscarPorId(rs.getString("cedula_paciente"));
                    Consultas c = new Consultas(
                        rs.getString("Id_Consulta"),
                        rs.getDate("Fecha_Consulta"),
                        rs.getString("Diagnostico"),
                        medLP, pacLP);
                    lista.add(c);
                }
            }
        } catch (SQLException e) { System.out.println("Error ConsultaDAO.listarPorPaciente: " + e.getMessage()); }
        return lista;
    }
    // Lee las vacunas aplicadas en CONSULTA_VACUNA
    public List<Vacuna> leerVacunas(String idConsulta) {
        List<Vacuna> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT v.Id_Vacuna, v.NombreVacuna, v.Descripcion"
            + " FROM CONSULTA_VACUNA cv"
            + " JOIN VACUNA v ON cv.Id_Vacuna = v.Id_Vacuna"
            + " WHERE cv.Id_Consulta = ?")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(new Vacuna(rs.getString("Id_Vacuna"),
                        rs.getString("NombreVacuna"),
                        rs.getString("Descripcion")));
            }
        } catch (SQLException e) {
            System.out.println("Error leerVacunas: " + e.getMessage());
        }
        return lista;
    }

    // Lee las enfermedades registradas en CONSULTA_ENFERMEDAD
    public List<Enfermedad> leerEnfermedades(String idConsulta) {
        List<Enfermedad> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT e.Id_Enfermedad, e.NombreEnfermedad, e.Sintomas, g.Gravedad"
            + " FROM CONSULTA_ENFERMEDAD ce"
            + " JOIN ENFERMEDAD e ON ce.Id_Enfermedad = e.Id_Enfermedad"
            + " LEFT JOIN GRAVEDAD_ENFERMEDAD g ON e.Id_Gravedad = g.Id_Gravedad"
            + " WHERE ce.Id_Consulta = ?")) {
            ps.setString(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Gravedadenfermedad grav = rs.getString("Gravedad") != null
                        ? new Gravedadenfermedad("", rs.getString("Gravedad")) : null;
                    lista.add(new Enfermedad(
                        rs.getString("Id_Enfermedad"),
                        rs.getString("NombreEnfermedad"),
                        rs.getString("Sintomas"),
                        grav));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error leerEnfermedades: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Consultas> listarPorMedico(String cedulaMedico) {
        ArrayList<Consultas> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Consulta,Fecha_Consulta,Diagnostico,cedula,cedula_paciente"
            + " FROM CONSULTA WHERE cedula=? ORDER BY Fecha_Consulta DESC")) {
            ps.setString(1, cedulaMedico);
            MedicoDAO   mD = new MedicoDAO();
            PacienteDAO pD = new PacienteDAO();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medico   med = mD.buscarPorId(rs.getString("cedula"));
                    Paciente pac = pD.buscarPorId(rs.getString("cedula_paciente"));
                    Consultas c = new Consultas(
                        rs.getString("Id_Consulta"),
                        rs.getDate("Fecha_Consulta"),
                        rs.getString("Diagnostico"),
                        med, pac);
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error ConsultaDAO.listarPorMedico: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Consultas c) {
        return ejecutar("UPDATE CONSULTA SET Fecha_Consulta=?,Diagnostico=? WHERE Id_Consulta=?",
            new java.sql.Date(c.getFechaConsulta().getTime()), c.getDiagnostico(), c.getIdConsulta());
    }
    public boolean eliminar(String id) {
        // Borrar relaciones antes de borrar la consulta
        ejecutar("DELETE FROM CONSULTA_ENFERMEDAD WHERE Id_Consulta=?", id);
        ejecutar("DELETE FROM CONSULTA_VACUNA WHERE Id_Consulta=?", id);
        return ejecutar("DELETE FROM CONSULTA WHERE Id_Consulta=?", id);
    }
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