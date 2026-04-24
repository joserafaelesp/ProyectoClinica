package Logical;
import java.sql.*;
import java.util.ArrayList;
public class CitaDAO extends BaseDAO {
    public boolean insertar(Cita c) {
        return ejecutar("INSERT INTO CITA (Id_Cita,Fecha_Cita,Completada,cedula,Id_Consulta) VALUES (?,?,?,?,?)",
            c.getIdCita(), new java.sql.Date(c.getFecha().getTime()),
            c.isCompletada(), c.getMedico()!=null?c.getMedico().getCedula():null,
            c.getConsulta()!=null?c.getConsulta().getIdConsulta():null);
    }
    public ArrayList<Cita> listarTodos() {
        ArrayList<Cita> lista = new ArrayList<>();
        try (Connection con = getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Id_Cita,Fecha_Cita,Completada,cedula FROM CITA ORDER BY Fecha_Cita")) {
            MedicoDAO mDao = new MedicoDAO();
            while (rs.next()) {
                Medico med = mDao.buscarPorId(rs.getString("cedula"));
                Cita c = new Cita(rs.getString("Id_Cita"), rs.getDate("Fecha_Cita"), med);
                c.setCompletada(rs.getBoolean("Completada"));
                lista.add(c);
            }
        } catch (SQLException e) { System.out.println("Error CitaDAO: " + e.getMessage()); }
        return lista;
    }
    public Cita buscarPorId(String id) {
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Cita,Fecha_Cita,Completada,cedula FROM CITA WHERE Id_Cita=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita c = new Cita(rs.getString("Id_Cita"), rs.getDate("Fecha_Cita"),
                        new MedicoDAO().buscarPorId(rs.getString("cedula")));
                    c.setCompletada(rs.getBoolean("Completada"));
                    return c;
                }
            }
        } catch (SQLException e) { System.out.println("Error CitaDAO: " + e.getMessage()); }
        return null;
    }
    public boolean actualizar(Cita c) {
        return ejecutar("UPDATE CITA SET Fecha_Cita=?,Completada=? WHERE Id_Cita=?",
            new java.sql.Date(c.getFecha().getTime()), c.isCompletada(), c.getIdCita());
    }
    public boolean marcarCompletada(String id) { return ejecutar("UPDATE CITA SET Completada=1 WHERE Id_Cita=?", id); }
    public boolean eliminar(String id) { return ejecutar("DELETE FROM CITA WHERE Id_Cita=?", id); }
}