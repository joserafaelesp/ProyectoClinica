package Logical;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO para manejar las operaciones de la entidad Cita.
 * Hereda de BaseDAO para reutilizar métodos comunes.
 */
public class CitaDAO extends BaseDAO {

    /**
     * Inserta una nueva cita en la base de datos.
     */
    public boolean insertar(Cita c) {

        String sql = "INSERT INTO CITA (Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita) VALUES (?,?,?,?,?,?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // ID de la cita
            ps.setString(1, c.getIdCita());

            // Fecha (convertida de util.Date a sql.Date)
            ps.setDate(2, new java.sql.Date(c.getFecha().getTime()));

            // Estado de la cita
            ps.setBoolean(3, c.isCompletada());

            // Médico (puede ser null)
            if (c.getDoc() != null) {
                ps.setString(4, c.getDoc().getCedula());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            // Paciente (puede ser null)
            if (c.getPaciente() != null) {
                ps.setString(5, c.getPaciente().getCedula());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }

            // Hora (puede ser null o vacía)
            if (c.getHoraCita() != null && !c.getHoraCita().isEmpty()) {
                ps.setString(6, c.getHoraCita());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            int filas = ps.executeUpdate();

            // Log para depuración
            System.out.println("[CitaDAO.insertar] filas=" + filas
                    + " pac=" + (c.getPaciente() != null ? c.getPaciente().getCedula() : "NULL")
                    + " hora=" + c.getHoraCita());

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error CitaDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las citas ordenadas por fecha.
     */
    public ArrayList<Cita> listarTodos() {

        ArrayList<Cita> lista = new ArrayList<>();

        String sql = "SELECT Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita"
                   + " FROM CITA ORDER BY Fecha_Cita";

        try (Connection con = getConexion();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            // DAO auxiliares para reconstruir objetos completos
            MedicoDAO   mDao = new MedicoDAO();
            PacienteDAO pDao = new PacienteDAO();

            while (rs.next()) {

                // Buscar relaciones (médico y paciente)
                Medico   med = mDao.buscarPorId(rs.getString("cedula"));
                Paciente pac = pDao.buscarPorId(rs.getString("cedula_paciente"));

                // Crear objeto Cita
                Cita c = new Cita(
                        rs.getString("Id_Cita"),
                        pac,
                        med,
                        rs.getDate("Fecha_Cita")
                );

                // Setear campos adicionales
                c.setCompletada(rs.getBoolean("Completada"));
                c.setHoraCita(rs.getString("Hora_Cita"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error CitaDAO.listarTodos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Lista citas filtradas por médico.
     */
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

                    Cita c = new Cita(
                            rs.getString("Id_Cita"),
                            pac,
                            med,
                            rs.getDate("Fecha_Cita")
                    );

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

    /**
     * Busca una cita por su ID.
     */
    public Cita buscarPorId(String id) {

        String sql = "SELECT Id_Cita,Fecha_Cita,Completada,cedula,cedula_paciente,Hora_Cita"
                   + " FROM CITA WHERE Id_Cita=?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    // Obtener relaciones
                    Medico   med = new MedicoDAO().buscarPorId(rs.getString("cedula"));
                    Paciente pac = new PacienteDAO().buscarPorId(rs.getString("cedula_paciente"));

                    // Crear objeto
                    Cita c = new Cita(
                            rs.getString("Id_Cita"),
                            pac,
                            med,
                            rs.getDate("Fecha_Cita")
                    );

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

    /**
     * Actualiza una cita existente.
     */
    public boolean actualizar(Cita c) {

        String sql = "UPDATE CITA SET Fecha_Cita=?,Completada=?,cedula=?,cedula_paciente=?,Hora_Cita=? WHERE Id_Cita=?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(c.getFecha().getTime()));
            ps.setBoolean(2, c.isCompletada());

            // Médico
            if (c.getDoc() != null) {
                ps.setString(3, c.getDoc().getCedula());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            // Paciente
            if (c.getPaciente() != null) {
                ps.setString(4, c.getPaciente().getCedula());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            // Hora
            if (c.getHoraCita() != null && !c.getHoraCita().isEmpty()) {
                ps.setString(5, c.getHoraCita());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }

            // ID para el WHERE
            ps.setString(6, c.getIdCita());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error CitaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marca una cita como completada (usa método heredado).
     */
    public boolean marcarCompletada(String id) {
        return ejecutar("UPDATE CITA SET Completada=1 WHERE Id_Cita=?", id);
    }

    /**
     * Elimina una cita por ID.
     */
    public boolean eliminar(String id) {
        return ejecutar("DELETE FROM CITA WHERE Id_Cita=?", id);
    }
}