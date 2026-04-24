package Logical;
import java.sql.*;
public class PersonaDAO extends BaseDAO {
    public boolean insertar(Persona p) {
        return ejecutar("INSERT INTO PERSONA (cedula,Nombre,Genero,Telefono) VALUES (?,?,?,?)",
            p.getCedula(), p.getNombre(), p.getGenero(), p.getTelefono());
    }
    public boolean actualizar(Persona p) {
        return ejecutar("UPDATE PERSONA SET Nombre=?,Genero=?,Telefono=? WHERE cedula=?",
            p.getNombre(), p.getGenero(), p.getTelefono(), p.getCedula());
    }
    public boolean eliminar(String cedula) { return ejecutar("DELETE FROM PERSONA WHERE cedula=?", cedula); }
    public boolean existeCedula(String cedula) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement("SELECT cedula FROM PERSONA WHERE cedula=?")) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { System.out.println("Error PersonaDAO: " + e.getMessage()); }
        return false;
    }
}