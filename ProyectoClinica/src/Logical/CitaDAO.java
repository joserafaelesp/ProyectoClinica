package Logical;

import java.sql.*;
import java.util.ArrayList;

public class CitaDAO extends BaseDAO {

    public boolean insertar(Cita c) {
        String sql = "INSERT INTO CITA (Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita) VALUES (?,?,?,?,?,?)";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getIdCita());
            ps.setDate(2, new java.sql.Date(c.getFecha().getTime()));
            ps.setBoolean(3, c.isCompletada());

            if (c.getDoc() != null)
                ps.setString(4, c.getDoc().getCedula());
            else
                ps.setNull(4, java.sql.Types.VARCHAR);

            if (c.getPaciente() != null)
                ps.setString(5, c.getPaciente().getCedula());
            else
                ps.setNull(5, java.sql.Types.VARCHAR);

            if (c.getHoraCita() != null && !c.getHoraCita().isEmpty())
                ps.setString(6, c.getHoraCita());
            else
                ps.setNull(6, java.sql.Types.VARCHAR);

            int filas = ps.executeUpdate();
            System.out.println("[CitaDAO.insertar] filas=" + filas
                + " pac=" + (c.getPaciente() != null ? c.getPaciente().getCedula() : "NULL")
                + " hora=" + c.getHoraCita());
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Cita> listarTodos() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita"
                   + " FROM CITA ORDER BY Fecha_Cita";
        try (Connection con = getConexion();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            MedicoDAO   mDao = new MedicoDAO();
            PacienteDAO pDao = new PacienteDAO();
            while (rs.next()) {
                Medico   med = mDao.buscarPorId(rs.getString("cedula"));
                Paciente pac = pDao.buscarPorId(rs.getString("cedula_paciente"));
                Cita c = new Cita(
                    rs.getString("Id_Cita"),
                    pac, med,
                    rs.getDate("Fecha_Cita"));
                c.setCompletada(rs.getBoolean("Completada"));
                c.setHoraCita(rs.getString("Hora_Cita"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Cita> listarPorMedico(String cedulaMedico) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita"
                   + " FROM CITA WHERE cedula=? ORDER BY Fecha_Cita";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedulaMedico);
            try (ResultSet rs = ps.executeQuery()) {
                MedicoDAO   mDao = new MedicoDAO();
                PacienteDAO pDao = new PacienteDAO();
                while (rs.next()) {
                    Medico   med = mDao.buscarPorId(rs.getString("cedula"));
                    Paciente pac = pDao.buscarPorId(rs.getString("cedula_paciente"));
                    Cita c = new Cita(rs.getString("Id_Cita"), pac, med,
                        rs.getDate("Fecha_Cita"));
                    c.setCompletada(rs.getBoolean("Completada"));
                    c.setHoraCita(rs.getString("Hora_Cita"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.listarPorMedico: " + e.getMessage());
        }
        return lista;
    }

    public Cita buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita"
            + " FROM CITA WHERE Id_Cita=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medico   med = new MedicoDAO().buscarPorId(rs.getString("cedula"));
                    Paciente pac = new PacienteDAO().buscarPorId(rs.getString("cedula_paciente"));
                    Cita c = new Cita(
                        rs.getString("Id_Cita"),
                        pac, med,
                        rs.getDate("Fecha_Cita"));
                    c.setCompletada(rs.getBoolean("Completada"));
                    c.setHoraCita(rs.getString("Hora_Cita"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Cita c) {
        String sql = "UPDATE CITA SET Fecha_Cita=?,Completada=?,cedula=?,cedula_paciente=?,Hora_Cita=? WHERE Id_Cita=?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(c.getFecha().getTime()));
            ps.setBoolean(2, c.isCompletada());

            if (c.getDoc() != null)
                ps.setString(3, c.getDoc().getCedula());
            else
                ps.setNull(3, java.sql.Types.VARCHAR);

            if (c.getPaciente() != null)
                ps.setString(4, c.getPaciente().getCedula());
            else
                ps.setNull(4, java.sql.Types.VARCHAR);

            if (c.getHoraCita() != null && !c.getHoraCita().isEmpty())
                ps.setString(5, c.getHoraCita());
            else
                ps.setNull(5, java.sql.Types.VARCHAR);

            ps.setString(6, c.getIdCita());

            int filas = ps.executeUpdate();

            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarCompletada(String id) {
        return ejecutar("UPDATE CITA SET Completada=1 WHERE Id_Cita=?", id);
    }

    public boolean eliminar(String id) {
        return ejecutar("DELETE FROM CITA WHERE Id_Cita=?", id);
    }
}