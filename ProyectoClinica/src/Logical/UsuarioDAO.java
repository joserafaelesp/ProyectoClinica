package Logical;
import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO extends BaseDAO {
    public boolean insertar(Usuario u) {
        return ejecutar("INSERT INTO USUARIO (Id_Usuario,NombreUsuario,Contrasena,rol) VALUES (?,?,?,?)",
            u.getIdUsuario(), u.getNombreUser(), u.getPassword(), u.getRol());
    }
    public ArrayList<Usuario> listarTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO ORDER BY rol")) {
            while (rs.next()) lista.add(new Usuario(rs.getString("Id_Usuario"),
                rs.getString("NombreUsuario"), rs.getString("Contrasena"), rs.getString("rol")));
        } catch (SQLException e) { System.out.println("Error UsuarioDAO: " + e.getMessage()); }
        return lista;
    }
    public Usuario buscarPorId(String id) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO WHERE Id_Usuario=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Usuario(rs.getString("Id_Usuario"),
                    rs.getString("NombreUsuario"), rs.getString("Contrasena"), rs.getString("rol"));
            }
        } catch (SQLException e) { System.out.println("Error UsuarioDAO: " + e.getMessage()); }
        return null;
    }
    public Usuario autenticar(String nombreUser, String password) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO WHERE NombreUsuario=? AND Contrasena=?")) {
            ps.setString(1, nombreUser); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Usuario(rs.getString("Id_Usuario"),
                    rs.getString("NombreUsuario"), rs.getString("Contrasena"), rs.getString("rol"));
            }
        } catch (SQLException e) { System.out.println("Error UsuarioDAO: " + e.getMessage()); }
        return null;
    }
    public boolean actualizar(Usuario u) {
        return ejecutar("UPDATE USUARIO SET NombreUsuario=?,Contrasena=?,rol=? WHERE Id_Usuario=?",
            u.getNombreUser(), u.getPassword(), u.getRol(), u.getIdUsuario());
    }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM USUARIO WHERE Id_Usuario=?", id); }
}