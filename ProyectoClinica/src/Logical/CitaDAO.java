package Logical;

import java.sql.*;
import java.util.ArrayList;

public class CitaDAO extends BaseDAO {

    public boolean insertar(Cita c) {
        return ejecutar(
            "INSERT INTO CITA (Id_Cita,Fecha_Cita,Completada,cedula,Id_Consulta)"
            + " VALUES (?,?,?,?,?)",
            c.getIdCita(),
            new java.sql.Date(c.getFecha().getTime()),
            c.isCompletada(),
            c.getMedico() != null ? c.getMedico().getCedula() : null,
            c.getConsulta() != null ? c.getConsulta().getIdConsulta() : null
        );
    }

    public ArrayList<Cita> listarTodos() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT Id_Cita,Fecha_Cita,Completada,cedula,Id_Consulta"
                   + " FROM CITA ORDER BY Fecha_Cita";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            MedicoDAO mDao = new MedicoDAO();
            while (rs.next()) {
                Medico med = mDao.buscarPorId(rs.getString("cedula"));
                Cita cita = new Cita(rs.getString("Id_Cita"),
                    rs.getDate("Fecha_Cita"), med);
                cita.setCompletada(rs.getBoolean("Completada"));
                lista.add(cita);
            }
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public Cita buscarPorId(String idCita) {
        try (PreparedStatement ps = con.prepareStatement(
            "SELECT Id_Cita,Fecha_Cita,Completada,cedula,Id_Consulta"
            + " FROM CITA WHERE Id_Cita=?")) {
            ps.setString(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medico med = new MedicoDAO().buscarPorId(rs.getString("cedula"));
                    Cita c = new Cita(rs.getString("Id_Cita"),
                        rs.getDate("Fecha_Cita"), med);
                    c.setCompletada(rs.getBoolean("Completada"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error CitaDAO.buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Cita c) {
        return ejecutar(
            "UPDATE CITA SET Fecha_Cita=?,Completada=?,Id_Consulta=? WHERE Id_Cita=?",
            new java.sql.Date(c.getFecha().getTime()),
            c.isCompletada(),
            c.getConsulta() != null ? c.getConsulta().getIdConsulta() : null,
            c.getIdCita()
        );
    }

    public boolean marcarCompletada(String idCita) {
        return ejecutar("UPDATE CITA SET Completada=1 WHERE Id_Cita=?", idCita);
    }

    public boolean eliminar(String idCita) {
        return ejecutar("DELETE FROM CITA WHERE Id_Cita=?", idCita);
    }
}