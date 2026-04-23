package Logical;

import java.sql.*;
import java.util.ArrayList;

public class MedicoDAO extends BaseDAO {

    public boolean insertar(Medico m) {
        try {
            con.setAutoCommit(false);

            // 1. Insertar en PERSONA
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono) VALUES (?,?,?,?)")) {
                ps.setString(1, m.getCedula());
                ps.setString(2, m.getNombre());
                ps.setString(3, m.getGenero());
                ps.setString(4, m.getTelefono());
                ps.executeUpdate();
            }

            // 2. Insertar en MEDICO
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO MEDICO (cedula,Id_Medico,Especialidad) VALUES (?,?,?)")) {
                ps.setString(1, m.getCedula());
                ps.setString(2, m.getIdMedico());
                ps.setString(3, m.getEspecialidad());
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) {}
            System.out.println("Error MedicoDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Medico> listarTodos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql =
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " m.Id_Medico, m.Especialidad"
            + " FROM PERSONA p"
            + " JOIN MEDICO m ON p.cedula = m.cedula"
            + " ORDER BY p.Nombre";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                lista.add(new Medico(
                    rs.getString("cedula"),
                    rs.getString("Nombre"),
                    rs.getString("Genero"),
                    null,   // fechaNacimiento
                    rs.getString("Telefono"),
                    null,   // vivienda
                    rs.getString("Id_Medico"),
                    rs.getString("Especialidad")
                ));
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public Medico buscarPorId(String idMedico) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " m.Id_Medico, m.Especialidad"
            + " FROM PERSONA p JOIN MEDICO m ON p.cedula = m.cedula"
            + " WHERE m.Id_Medico = ? OR m.cedula = ?")) {
            ps.setString(1, idMedico);
            ps.setString(2, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Medico(
                        rs.getString("cedula"),
                        rs.getString("Nombre"),
                        rs.getString("Genero"),
                        null,
                        rs.getString("Telefono"),
                        null,
                        rs.getString("Id_Medico"),
                        rs.getString("Especialidad")
                    );
            }
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public Medico buscarPorNombre(String nombre) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " m.Id_Medico, m.Especialidad"
            + " FROM PERSONA p JOIN MEDICO m ON p.cedula = m.cedula"
            + " WHERE p.Nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Medico(
                        rs.getString("cedula"),
                        rs.getString("Nombre"),
                        rs.getString("Genero"),
                        null,
                        rs.getString("Telefono"),
                        null,
                        rs.getString("Id_Medico"),
                        rs.getString("Especialidad")
                    );
            }
        } catch (SQLException e) {
            System.out.println("Error MedicoDAO.buscarPorNombre: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Medico m) {
        try {
            con.setAutoCommit(false);
            ejecutar(
                "UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=? WHERE cedula=?",
                m.getNombre(), m.getGenero(), m.getTelefono(), m.getCedula());
            ejecutar(
                "UPDATE MEDICO SET Especialidad=? WHERE cedula=?",
                m.getEspecialidad(), m.getCedula());
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (Exception ex) {}
            System.out.println("Error MedicoDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula);
    }
}