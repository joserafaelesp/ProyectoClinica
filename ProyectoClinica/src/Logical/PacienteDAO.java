package Logical;

import java.sql.*;
import java.util.ArrayList;

public class PacienteDAO extends BaseDAO {

    public boolean insertar(Paciente p) {
        try {
            con.setAutoCommit(false);

            // 1. Insertar en PERSONA
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono) VALUES (?,?,?,?)")) {
                ps.setString(1, p.getCedula());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getGenero());
                ps.setString(4, p.getTelefono());
                ps.executeUpdate();
            }

            // 2. Insertar en PACIENTE
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PACIENTE (cedula,Id_Paciente,Informacion,TipoSangre,Id_Vivienda)"
                + " VALUES (?,?,?,?,?)")) {
                ps.setString(1, p.getCedula());
                ps.setString(2, p.getIdPaciente());
                ps.setString(3, p.getInformacion());
                ps.setString(4, p.getTipoSangre());
                ps.setString(5, p.getViviend() != null ? p.getViviend().getIdVivienda() : null);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) {}
            System.out.println("Error PacienteDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Paciente> listarTodos() {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql =
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " pa.Id_Paciente, pa.Informacion, pa.TipoSangre,"
            + " v.Id_Vivienda, v.Direccion"
            + " FROM PERSONA p"
            + " JOIN PACIENTE pa ON p.cedula = pa.cedula"
            + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda = v.Id_Vivienda"
            + " ORDER BY p.Nombre";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Vivienda viv = null;
                if (rs.getString("Id_Vivienda") != null)
                    viv = new Vivienda(rs.getString("Id_Vivienda"),
                                       rs.getString("Direccion"));
                lista.add(new Paciente(
                    rs.getString("cedula"),
                    rs.getString("Nombre"),
                    rs.getString("Genero"),
                    null,                          // fechaNacimiento
                    rs.getString("Telefono"),
                    viv,                           // vivienda
                    rs.getString("Id_Paciente"),
                    rs.getString("Informacion"),
                    rs.getString("TipoSangre")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error PacienteDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public Paciente buscarPorId(String idPaciente) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " pa.Id_Paciente, pa.Informacion, pa.TipoSangre,"
            + " v.Id_Vivienda, v.Direccion"
            + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula = pa.cedula"
            + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda = v.Id_Vivienda"
            + " WHERE pa.Id_Paciente = ? OR pa.cedula = ?")) {
            ps.setString(1, idPaciente);
            ps.setString(2, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vivienda viv = null;
                    if (rs.getString("Id_Vivienda") != null)
                        viv = new Vivienda(rs.getString("Id_Vivienda"),
                                           rs.getString("Direccion"));
                    return new Paciente(
                        rs.getString("cedula"),
                        rs.getString("Nombre"),
                        rs.getString("Genero"),
                        null,
                        rs.getString("Telefono"),
                        viv,
                        rs.getString("Id_Paciente"),
                        rs.getString("Informacion"),
                        rs.getString("TipoSangre")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error PacienteDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public Paciente buscarPorNombre(String nombre) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula, p.Nombre, p.Genero, p.Telefono,"
            + " pa.Id_Paciente, pa.Informacion, pa.TipoSangre, pa.Id_Vivienda"
            + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula = pa.cedula"
            + " WHERE p.Nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Paciente(
                        rs.getString("cedula"),
                        rs.getString("Nombre"),
                        rs.getString("Genero"),
                        null,
                        rs.getString("Telefono"),
                        null,
                        rs.getString("Id_Paciente"),
                        rs.getString("Informacion"),
                        rs.getString("TipoSangre")
                    );
            }
        } catch (SQLException e) {
            System.out.println("Error PacienteDAO.buscarPorNombre: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Paciente p) {
        try {
            con.setAutoCommit(false);
            ejecutar(
                "UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=? WHERE cedula=?",
                p.getNombre(), p.getGenero(), p.getTelefono(), p.getCedula());
            ejecutar(
                "UPDATE PACIENTE SET Informacion=?,TipoSangre=?,Id_Vivienda=? WHERE cedula=?",
                p.getInformacion(), p.getTipoSangre(),
                p.getViviend() != null ? p.getViviend().getIdVivienda() : null,
                p.getCedula());
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (Exception ex) {}
            System.out.println("Error PacienteDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula);
    }
}