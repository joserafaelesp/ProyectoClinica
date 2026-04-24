package Logical;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PacienteDAO extends BaseDAO {

    public boolean insertar(Paciente p) {
        try {
            Connection con = getConexion();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono,FechaNacimiento) VALUES (?,?,?,?,?)")) {
                ps.setString(1, p.getCedula()); ps.setString(2, p.getNombre());
                ps.setString(3, p.getGenero()); ps.setString(4, p.getTelefono());
                ps.setObject(5, p.getFechaNacimiento());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO PACIENTE (cedula,Id_Paciente,Informacion,TipoSangre,Id_Vivienda) VALUES (?,?,?,?,?)")) {
                ps.setString(1, p.getCedula()); ps.setString(2, p.getIdPaciente());
                ps.setString(3, p.getInformacion()); ps.setString(4, p.getTipoSangre());
                ps.setString(5, p.getViviend()!=null?p.getViviend().getIdVivienda():null);
                ps.executeUpdate();
            }
            con.commit(); con.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error PacienteDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Paciente> listarTodos() {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "pa.Id_Paciente,pa.Informacion,pa.TipoSangre,v.Id_Vivienda,v.Direccion"
            + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula=pa.cedula"
            + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda=v.Id_Vivienda ORDER BY p.Nombre";
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate fecha = rs.getDate("FechaNacimiento")!=null
                    ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                Vivienda viv = rs.getString("Id_Vivienda")!=null
                    ? new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion")) : null;
                lista.add(new Paciente(
                    rs.getString("cedula"), rs.getString("Nombre"),
                    rs.getString("Genero"), fecha,
                    rs.getString("Telefono"), viv,
                    rs.getString("Id_Paciente"), rs.getString("Informacion"),
                    rs.getString("TipoSangre")));
            }
        } catch (SQLException e) { System.out.println("Error PacienteDAO.listarTodos: " + e.getMessage()); }
        return lista;
    }

    public Paciente buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "pa.Id_Paciente,pa.Informacion,pa.TipoSangre,v.Id_Vivienda,v.Direccion"
            + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula=pa.cedula"
            + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda=v.Id_Vivienda"
            + " WHERE pa.Id_Paciente=? OR pa.cedula=?")) {
            ps.setString(1, id); ps.setString(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento")!=null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    String idViv = rs.getString("Id_Vivienda");
                    String dirViv = rs.getString("Direccion");
                    Vivienda viv = idViv != null
                        ? new Vivienda(idViv, dirViv) : null;
                    return new Paciente(
                        rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha,
                        rs.getString("Telefono"), viv,
                        rs.getString("Id_Paciente"), rs.getString("Informacion"),
                        rs.getString("TipoSangre"));
                }
            }
        } catch (SQLException e) { System.out.println("Error PacienteDAO.buscarPorId: " + e.getMessage()); }
        return null;
    }

    public Paciente buscarPorNombre(String nombre) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT p.cedula,p.Nombre,p.Genero,p.Telefono,p.FechaNacimiento,"
            + "pa.Id_Paciente,pa.Informacion,pa.TipoSangre,v.Id_Vivienda,v.Direccion"
            + " FROM PERSONA p JOIN PACIENTE pa ON p.cedula=pa.cedula"
            + " LEFT JOIN VIVIENDA v ON pa.Id_Vivienda=v.Id_Vivienda"
            + " WHERE p.Nombre=?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("FechaNacimiento")!=null
                        ? rs.getDate("FechaNacimiento").toLocalDate() : null;
                    Vivienda viv = rs.getString("Id_Vivienda")!=null
                        ? new Vivienda(rs.getString("Id_Vivienda"), rs.getString("Direccion")) : null;
                    return new Paciente(
                        rs.getString("cedula"), rs.getString("Nombre"),
                        rs.getString("Genero"), fecha,
                        rs.getString("Telefono"), viv,
                        rs.getString("Id_Paciente"), rs.getString("Informacion"),
                        rs.getString("TipoSangre"));
                }
            }
        } catch (SQLException e) { System.out.println("Error PacienteDAO.buscarPorNombre: " + e.getMessage()); }
        return null;
    }

    public boolean actualizar(Paciente p) {
        try {
            Connection con = getConexion();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(
                "UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=?,FechaNacimiento=? WHERE cedula=?")) {
                ps.setString(1, p.getNombre()); ps.setString(2, p.getGenero());
                ps.setString(3, p.getTelefono());
                ps.setObject(4, p.getFechaNacimiento());
                ps.setString(5, p.getCedula());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                "UPDATE PACIENTE SET Informacion=?,TipoSangre=?,Id_Vivienda=? WHERE cedula=?")) {
                ps.setString(1, p.getInformacion()); ps.setString(2, p.getTipoSangre());
                ps.setString(3, p.getViviend()!=null?p.getViviend().getIdVivienda():null);
                ps.setString(4, p.getCedula());
                ps.executeUpdate();
            }
            con.commit(); con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            System.out.println("Error PacienteDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula);
    }
}