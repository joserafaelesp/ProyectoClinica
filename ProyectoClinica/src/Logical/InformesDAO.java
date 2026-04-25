package Logical;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class InformesDAO extends BaseDAO {

    // 1. Reporte completo de un paciente por cedula
    public String reportePaciente(String cedula) {
        StringBuilder sb = new StringBuilder();
        try (Connection con = getConexion()) {

            // Datos personales
            PreparedStatement ps = con.prepareStatement(
                "SELECT p.Nombre, p.cedula, p.Genero, p.Telefono, p.FechaNacimiento,"
                + " pa.Id_Paciente, pa.TipoSangre, pa.Informacion, v.Direccion"
                + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula=pa.cedula"
                + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda=v.Id_Vivienda"
                + " WHERE p.cedula=?");
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sb.append("NOMBRE:          ").append(rs.getString("Nombre")).append("\n");
                sb.append("CEDULA:          ").append(rs.getString("cedula")).append("\n");
                sb.append("ID PACIENTE:     ").append(rs.getString("Id_Paciente")).append("\n");
                sb.append("GENERO:          ").append(rs.getString("Genero")).append("\n");
                sb.append("TELEFONO:        ").append(rs.getString("Telefono")).append("\n");
                sb.append("FECHA NAC:       ").append(rs.getString("FechaNacimiento") != null
                    ? rs.getString("FechaNacimiento") : "N/A").append("\n");
                sb.append("TIPO SANGRE:     ").append(rs.getString("TipoSangre") != null
                    ? rs.getString("TipoSangre") : "N/A").append("\n");
                sb.append("INFO EMERGENCIA: ").append(rs.getString("Informacion") != null
                    ? rs.getString("Informacion") : "N/A").append("\n");
                sb.append("VIVIENDA:        ").append(rs.getString("Direccion") != null
                    ? rs.getString("Direccion") : "N/A").append("\n");
            } else {
                return "Paciente no encontrado.";
            }

            // Historial de consultas
            sb.append("\n──────────────────────────────────────────\n");
            sb.append("HISTORIAL DE CONSULTAS\n");
            sb.append("──────────────────────────────────────────\n");

            PreparedStatement ps2 = con.prepareStatement(
                "SELECT c.Id_Consulta, c.Fecha_Consulta, c.Diagnostico,"
                + " p2.Nombre AS NombreMedico, m.Especialidad"
                + " FROM CONSULTA c"
                + " JOIN HISTORIAL_MEDICO h ON c.Id_Historial=h.Id_Historial"
                + " JOIN MEDICO m ON c.cedula=m.cedula"
                + " JOIN PERSONA p2 ON m.cedula=p2.cedula"
                + " WHERE h.cedula=?"
                + " ORDER BY c.Fecha_Consulta DESC");
            ps2.setString(1, cedula);
            ResultSet rs2 = ps2.executeQuery();

            int numConsulta = 1;
            while (rs2.next()) {
                String idCon = rs2.getString("Id_Consulta");
                sb.append("\n  #").append(numConsulta++).append(" ").append(idCon)
                  .append(" | ").append(rs2.getString("Fecha_Consulta")).append("\n");
                sb.append("  Medico:      ").append(rs2.getString("NombreMedico"))
                  .append(" (").append(rs2.getString("Especialidad")).append(")\n");
                sb.append("  Diagnostico: ").append(rs2.getString("Diagnostico") != null
                    ? rs2.getString("Diagnostico") : "N/A").append("\n");

                // Enfermedades de esa consulta
                PreparedStatement ps3 = con.prepareStatement(
                    "SELECT e.NombreEnfermedad, g.Gravedad"
                    + " FROM CONSULTA_ENFERMEDAD ce"
                    + " JOIN ENFERMEDAD e ON ce.Id_Enfermedad=e.Id_Enfermedad"
                    + " LEFT JOIN GRAVEDAD_ENFERMEDAD g ON e.Id_Gravedad=g.Id_Gravedad"
                    + " WHERE ce.Id_Consulta=?");
                ps3.setString(1, idCon);
                ResultSet rs3 = ps3.executeQuery();
                sb.append("  Enfermedades: ");
                boolean hayEnf = false;
                while (rs3.next()) {
                    sb.append(rs3.getString("NombreEnfermedad"))
                      .append(" [").append(rs3.getString("Gravedad") != null
                          ? rs3.getString("Gravedad") : "sin clasificar").append("] ");
                    hayEnf = true;
                }
                if (!hayEnf) sb.append("ninguna");
                sb.append("\n");

                // Vacunas de esa consulta
                PreparedStatement ps4 = con.prepareStatement(
                    "SELECT v.NombreVacuna FROM CONSULTA_VACUNA cv"
                    + " JOIN VACUNA v ON cv.Id_Vacuna=v.Id_Vacuna"
                    + " WHERE cv.Id_Consulta=?");
                ps4.setString(1, idCon);
                ResultSet rs4 = ps4.executeQuery();
                sb.append("  Vacunas:      ");
                boolean hayVac = false;
                while (rs4.next()) {
                    sb.append(rs4.getString("NombreVacuna")).append(" ");
                    hayVac = true;
                }
                if (!hayVac) sb.append("ninguna");
                sb.append("\n");

                // Examenes de esa consulta
                PreparedStatement ps5 = con.prepareStatement(
                    "SELECT e.NombreExamen FROM CONSULTA_EXAMEN ce"
                    + " JOIN EXAMEN e ON ce.Id_Examen=e.Id_Examen"
                    + " WHERE ce.Id_Consulta=?");
                ps5.setString(1, idCon);
                ResultSet rs5 = ps5.executeQuery();
                sb.append("  Examenes:     ");
                boolean hayEx = false;
                while (rs5.next()) {
                    sb.append(rs5.getString("NombreExamen")).append(" ");
                    hayEx = true;
                }
                if (!hayEx) sb.append("ninguno");
                sb.append("\n");
            }
            if (numConsulta == 1) sb.append("  Sin consultas registradas.\n");

        } catch (SQLException e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    // 2. Consultas por rango de fechas agrupadas por medico
    public ArrayList<String[]> consultasPorFecha(String fechaInicio, String fechaFin) {
        ArrayList<String[]> lista = new ArrayList<>();
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.Nombre AS NombreMedico, m.Especialidad,"
            + " COUNT(c.Id_Consulta) AS TotalConsultas,"
            + " MIN(c.Fecha_Consulta) AS PrimeraConsulta,"
            + " MAX(c.Fecha_Consulta) AS UltimaConsulta"
            + " FROM CONSULTA c"
            + " JOIN MEDICO m ON c.cedula=m.cedula"
            + " JOIN PERSONA p ON m.cedula=p.cedula"
            + " WHERE c.Fecha_Consulta BETWEEN ? AND ?"
            + " GROUP BY p.Nombre, m.Especialidad"
            + " ORDER BY TotalConsultas DESC")) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("NombreMedico"),
                    rs.getString("Especialidad"),
                    String.valueOf(rs.getInt("TotalConsultas")),
                    rs.getString("PrimeraConsulta"),
                    rs.getString("UltimaConsulta")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error InformesDAO.consultasPorFecha: " + e.getMessage());
        }
        return lista;
    }

    // 3. Medico mas activo — ranking por numero de consultas
    public ArrayList<String[]> medicosMasActivos() {
        ArrayList<String[]> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
            "SELECT p.Nombre AS NombreMedico, m.Especialidad,"
            + " COUNT(c.Id_Consulta) AS TotalConsultas,"
            + " COUNT(DISTINCT c.cedula_paciente) AS PacientesAtendidos"
            + " FROM MEDICO m"
            + " JOIN PERSONA p ON m.cedula=p.cedula"
            + " LEFT JOIN CONSULTA c ON m.cedula=c.cedula"
            + " GROUP BY p.Nombre, m.Especialidad"
            + " ORDER BY TotalConsultas DESC")) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("NombreMedico"),
                    rs.getString("Especialidad"),
                    String.valueOf(rs.getInt("TotalConsultas")),
                    String.valueOf(rs.getInt("PacientesAtendidos"))
                });
            }
        } catch (SQLException e) {
            System.out.println("Error InformesDAO.medicosMasActivos: " + e.getMessage());
        }
        return lista;
    }

    // 4. Enfermedades mas diagnosticadas
    public ArrayList<String[]> enfermedadesMasDiagnosticadas() {
        ArrayList<String[]> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
            "SELECT e.NombreEnfermedad, g.Gravedad,"
            + " COUNT(ce.Id_Consulta) AS VecesDiagnosticada"
            + " FROM ENFERMEDAD e"
            + " LEFT JOIN CONSULTA_ENFERMEDAD ce ON e.Id_Enfermedad=ce.Id_Enfermedad"
            + " LEFT JOIN GRAVEDAD_ENFERMEDAD g ON e.Id_Gravedad=g.Id_Gravedad"
            + " GROUP BY e.NombreEnfermedad, g.Gravedad"
            + " ORDER BY VecesDiagnosticada DESC")) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("NombreEnfermedad"),
                    rs.getString("Gravedad") != null ? rs.getString("Gravedad") : "Sin clasificar",
                    String.valueOf(rs.getInt("VecesDiagnosticada"))
                });
            }
        } catch (SQLException e) {
            System.out.println("Error InformesDAO.enfermedadesMasDiagnosticadas: " + e.getMessage());
        }
        return lista;
    }
}