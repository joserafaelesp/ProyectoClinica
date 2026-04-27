package Logical;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO extends BaseDAO {

    // Inserta un nuevo usuario en la base de datos
    public boolean insertar(Usuario u) {
        return ejecutar(
            "INSERT INTO USUARIO (Id_Usuario,NombreUsuario,Contrasena,rol) VALUES (?,?,?,?)",
            u.getIdUsuario(),
            u.getNombreUser(),
            u.getPassword(),
            u.getRol()
        );
    }

    // Lista todos los usuarios ordenados por rol
    public ArrayList<Usuario> listarTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();

        try (
            Connection con = getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO ORDER BY rol"
            )
        ) {
            // Recorre cada fila y la convierte en objeto Usuario
            while (rs.next()) {
                lista.add(
                    new Usuario(
                        rs.getString("Id_Usuario"),
                        rs.getString("NombreUsuario"),
                        rs.getString("Contrasena"),
                        rs.getString("rol")
                    )
                );
            }

        } catch (SQLException e) {
            // Manejo de error en consulta
            System.out.println("Error UsuarioDAO: " + e.getMessage());
        }

        return lista;
    }

    // Busca un usuario por su id
    public Usuario buscarPorId(String id) {
        try (
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(
                "SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO WHERE Id_Usuario=?"
            )
        ) {
            // Asigna el parametro al query
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                // Si encuentra resultado, lo retorna como objeto
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("Id_Usuario"),
                        rs.getString("NombreUsuario"),
                        rs.getString("Contrasena"),
                        rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error UsuarioDAO: " + e.getMessage());
        }

        return null;
    }

    // Autentica usuario usando nombre y contrasena
    public Usuario autenticar(String nombreUser, String password) {
        try (
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(
                "SELECT Id_Usuario,NombreUsuario,Contrasena,rol FROM USUARIO WHERE NombreUsuario=? AND Contrasena=?"
            )
        ) {
            // Asigna valores al query
            ps.setString(1, nombreUser);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                // Si coincide usuario y contrasena, retorna el usuario
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("Id_Usuario"),
                        rs.getString("NombreUsuario"),
                        rs.getString("Contrasena"),
                        rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error UsuarioDAO: " + e.getMessage());
        }

        return null;
    }

    // Actualiza los datos de un usuario existente
    public boolean actualizar(Usuario u) {
        // Mensaje de debug para ver que usuario se actualiza
        System.out.println("[DEBUG] UPDATE USUARIO id=" + u.getIdUsuario());

        return ejecutar(
            "UPDATE USUARIO SET NombreUsuario=?,Contrasena=?,rol=? WHERE Id_Usuario=?",
            u.getNombreUser(),
            u.getPassword(),
            u.getRol(),
            u.getIdUsuario()
        );
    }

    // Elimina un usuario por id
    public boolean eliminar(String id) {
        return ejecutar(
            "DELETE FROM USUARIO WHERE Id_Usuario=?",
            id
        );
    }
}