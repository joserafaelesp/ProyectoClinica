package Logical;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicoDAO extends BaseDAO {

    public boolean insertar(Medico m) {
        try {
            Connection con = getConexion();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono,FechaNacimiento) VALUES (?,?,?,?,?)")) {
                ps.setString(1, m.getCedula());
                ps.setString(2, m.getNombre());
                ps.setString(3, m.getGenero());
                ps.setString(4, m.getTelefono());
                ps.setObject(5, m.getFechaNacimiento()); // LocalDate directo
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO MEDICO (cedula,Id_Medico,Especialidad) VALUES (?,?,?)")) {
                ps.setString(1, m.getCedula());
                ps.setString(2, m.getIdMedico());
                ps.setString(3, m.getEspecialidad());
                ps.executeUpdate();
            }
            con.commit(); con.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Medico> listarTodos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad"
            + " FROM PERSONA p JOIN MEDICO m ON p.cedula=m.cedula ORDER BY p.Nombre";
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate fecha = rs.getDate("FechaNacimiento") != null
                    ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                lista.add(new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                    rs.getString("Genero"), fecha, rs.getString("Telefono"), null,
                    rs.getString("Id_Medico"), rs.getString("Especialidad")));
            }
        } catch (SQLException e) { System.out.println("Error MedicoDAO.listarTodos: " + e.getMessage()); }
        return lista;
    }

    public Medico buscarPorId(String idMedico) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad FROM PERSONA p JOIN MEDICO m ON p.cedula=m.cedula"
            + " WHERE m.Id_Medico=? OR m.cedula=?")) {
            ps.setString(1, idMedico); ps.setString(2, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento") != null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    return new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha, rs.getString("Telefono"), null,
                        rs.getString("Id_Medico"), rs.getString("Especialidad"));
                }
            }
        } catch (SQLException e) { System.out.println("Error MedicoDAO.buscarPorId: " + e.getMessage()); }
        return null;
    }

    public Medico buscarPorNombre(String nombre) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "m.Id_Medico,m.Especialidad FROM PERSONA p JOIN MEDICO m ON p.cedula=m.cedula"
            + " WHERE p.Nombre=?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento") != null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    return new Medico(rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha, rs.getString("Telefono"), null,
                        rs.getString("Id_Medico"), rs.getString("Especialidad"));
                }
            }
        } catch (SQLException e) { System.out.println("Error MedicoDAO.buscarPorNombre: " + e.getMessage()); }
        return null;
    }

    public boolean actualizar(Medico m) {
        try {
            Connection con = getConexion();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(
                "UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=?,FechaNacimiento=? WHERE cedula=?")) {
                ps.setString(1, m.getNombre()); ps.setString(2, m.getGenero());
                ps.setString(3, m.getTelefono());
                ps.setObject(4, m.getFechaNacimiento());
                ps.setString(5, m.getCedula());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                "UPDATE MEDICO SET Especialidad=? WHERE cedula=?")) {
                ps.setString(1, m.getEspecialidad()); ps.setString(2, m.getCedula());
                ps.executeUpdate();
            }
            con.commit(); con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            System.out.println("Error MedicoDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula);
    }
}