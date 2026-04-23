package Logical;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cita.java — actualizada según el modelo relacional:
 * FK cedula (médico) y FK Id_Consulta (provoca 1:1).
 * Se mantiene el campo Completada.
 */
public class Cita {

    private String  idCita;
    private Date    fechaCita;
    private boolean completada;
    private Medico  medico;      // cedula FK → MEDICO
    private Consultas consulta;  // Id_Consulta FK → CONSULTA (puede ser null)

    public Cita(String idCita, Date fechaCita, Medico medico) {
        this.idCita     = idCita;
        this.fechaCita  = fechaCita;
        this.medico     = medico;
        this.completada = false;
        this.consulta   = null;
    }

    // Constructor de compatibilidad con la GUI existente
    public Cita(String idCita, Paciente pacienteIgnorado, Medico medico, Date fechaCita) {
        this(idCita, fechaCita, medico);
    }

    public String    getIdCita()              { return idCita; }
    public void      setIdCita(String id)     { this.idCita = id; }
    public Date      getFecha()               { return fechaCita; }
    public void      setFecha(Date f)         { this.fechaCita = f; }
    public Date      getFechaCita()           { return fechaCita; }
    public void      setFechaCita(Date f)     { this.fechaCita = f; }
    public boolean   isCompletada()           { return completada; }
    public void      setCompletada(boolean c) { this.completada = c; }
    public Medico    getMedico()              { return medico; }
    public void      setMedico(Medico m)      { this.medico = m; }
    // compatibilidad con getDoc() que usaba el código anterior
    public Medico    getDoc()                 { return medico; }
    public void      setDoc(Medico m)         { this.medico = m; }
    public Consultas getConsulta()            { return consulta; }
    public void      setConsulta(Consultas c) { this.consulta = c; }
    // getHoraCita por compatibilidad (no está en el modelo pero puede estar en GUI)
    public String    getHoraCita()            { return ""; }
    public void      setHoraCita(String h)    {}
    // getPaciente por compatibilidad con GUI
    public Paciente  getPaciente()            { return null; }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return idCita + ","
            + (fechaCita != null ? df.format(fechaCita) : "null") + ","
            + completada + ","
            + (medico != null ? medico.getCedula() : "null");
    }
}