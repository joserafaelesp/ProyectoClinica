package Logical;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicoDAO extends BaseDAO {

    public boolean insertar(Medico m) {
        // FIX: try-with-resources garantiza que la conexión siempre se cierre
        try (Connection con = getConexion()) {
            if (con == null) return false;
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono,FechaNacimiento) VALUES (?,?,?,?,?)")) {
                    ps.setString(1, m.getCedula());
                    ps.setString(2, m.getNombre());
                    ps.setString(3, m.getGenero());
                    ps.setString(4, m.getTelefono());
                    ps.setObject(5, m.getFechaNacimiento());
                    ps.executeUpdate();
                }
                // FIX: se agrega Id_Usuario al INSERT para vincularlo al usuario creado en paso 1
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO MEDICO (cedula,Id_Medico,Especialidad,Id_Usuario) VALUES (?,?,?,?)")) {
                    ps.setString(1, m.getCedula());
                    ps.setString(2, m.getIdMedico());
                    ps.setString(3, m.getEspecialidad());
                    if (m.getIdUsuario() != null)
                        ps.setString(4, m.getIdUsuario());
                    else
                        ps.setNull(4, java.sql.Types.VARCHAR);
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                try { con.rollback(); } catch (SQLException ex) { /* ignorar */ }
                System.out.println("Error MedicoDAO.insertar: " + e.getMessage());
                return false;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException ex) { /* ignorar */ }
            }
        } catch (SQLException e) {
            System.out.println("Error conexion MedicoDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Medico> listarTodos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad,m.Id_Usuario,"
            + "v.Id_Vivienda,v.Direccion"
            + " FROM PERSONA p"
            + " JOIN MEDICO m ON p.cedula=m.cedula"
            + " LEFT JOIN PACIENTE pac ON p.cedula=pac.cedula"
            + " LEFT JOIN VIVIENDA v ON pac.Id_Vivienda=v.Id_Vivienda"
            + " ORDER BY p.Nombre";
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate fecha = rs.getDate("FechaNacimiento") != null
                    ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                // FIX: cargar vivienda si existe
                Vivienda viv = rs.getString("Id_Vivienda") != null
                    ? new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion"))
                    : null;
                Medico med = new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                    rs.getString("Genero"), fecha, rs.getString("Telefono"), viv,
                    rs.getString("Id_Medico"), rs.getString("Especialidad"));
                med.setIdUsuario(rs.getString("Id_Usuario"));
                lista.add(med);
            }
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public Medico buscarPorId(String idMedico) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad,m.Id_Usuario,"
            + "v.Id_Vivienda,v.Direccion"
            + " FROM PERSONA p"
            + " JOIN MEDICO m ON p.cedula=m.cedula"
            + " LEFT JOIN PACIENTE pac ON p.cedula=pac.cedula"
            + " LEFT JOIN VIVIENDA v ON pac.Id_Vivienda=v.Id_Vivienda"
            + " WHERE m.Id_Medico=? OR m.cedula=?")) {
            ps.setString(1, idMedico);
            ps.setString(2, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento") != null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    // FIX: cargar vivienda si existe
                    Vivienda viv = rs.getString("Id_Vivienda") != null
                        ? new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion"))
                        : null;
                    Medico med = new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha, rs.getString("Telefono"), viv,
                        rs.getString("Id_Medico"), rs.getString("Especialidad"));
                    med.setIdUsuario(rs.getString("Id_Usuario"));
                    return med;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public Medico buscarPorNombre(String nombre) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad,m.Id_Usuario FROM PERSONA p JOIN MEDICO m ON p.cedula=m.cedula"
            + " WHERE p.Nombre=?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento") != null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    Medico med = new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha, rs.getString("Telefono"), null,
                        rs.getString("Id_Medico"), rs.getString("Especialidad"));
                    med.setIdUsuario(rs.getString("Id_Usuario"));
                    return med;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.buscarPorNombre: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Medico m) {
        try (Connection con = getConexion()) {
            if (con == null) return false;
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=?,FechaNacimiento=? WHERE cedula=?")) {
                    ps.setString(1, m.getNombre());
                    ps.setString(2, m.getGenero());
                    ps.setString(3, m.getTelefono());
                    ps.setObject(4, m.getFechaNacimiento());
                    ps.setString(5, m.getCedula());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE MEDICO SET Especialidad=? WHERE cedula=?")) {
                    ps.setString(1, m.getEspecialidad());
                    ps.setString(2, m.getCedula());
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (Exception e) {
                try { con.rollback(); } catch (SQLException ex) { /* ignorar */ }
                System.out.println("Error MedicoDAO.actualizar: " + e.getMessage());
                return false;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException ex) { /* ignorar */ }
            }
        } catch (SQLException e) {
            System.out.println("Error conexion MedicoDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula);
    }
}