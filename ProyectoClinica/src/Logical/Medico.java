package Logical;
import java.time.LocalDate;

public class Medico extends Persona {
    private String idMedico;
    private String especialidad;
    private String idUsuario;   // FIX: campo agregado para vincular con USUARIO

    public Medico(String cedula, String nombre, String genero,
                  LocalDate fechaNacimiento, String telefono,
                  Vivienda vivienda, String idMedico, String especialidad) {
        super(cedula, nombre, genero, fechaNacimiento, telefono, vivienda);
        this.idMedico     = idMedico;
        this.especialidad = especialidad;
        this.idUsuario    = null;
    }

    public String getIdMedico()              { return idMedico; }
    public void   setIdMedico(String id)     { this.idMedico = id; }
    public String getEspecialidad()          { return especialidad; }
    public void   setEspecialidad(String e)  { this.especialidad = e; }
    public String getIdUsuario()             { return idUsuario; }
    public void   setIdUsuario(String id)    { this.idUsuario = id; }

    @Override
    public String toString() {
        return super.toString() + "," + idMedico + "," + especialidad;
    }
}