package Logical;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cita {

    private String    idCita;
    private Date      fechaCita;
    private boolean   completada;
    private Medico    medico;
    private Paciente  paciente;   // ← AGREGADO
    private String    horaCita;   // ← AGREGADO
    private Consultas consulta;

    // Constructor principal
    public Cita(String idCita, Date fechaCita, Medico medico) {
        this.idCita     = idCita;
        this.fechaCita  = fechaCita;
        this.medico     = medico;
        this.completada = false;
        this.consulta   = null;
        this.paciente   = null;
        this.horaCita   = null;
    }

    // Constructor con paciente y fecha — usado por HacerCita y CitaDAO
    public Cita(String idCita, Paciente paciente, Medico medico, Date fechaCita) {
        this.idCita     = idCita;
        this.fechaCita  = fechaCita;
        this.medico     = medico;
        this.paciente   = paciente;   // ← ya NO se ignora
        this.completada = false;
        this.consulta   = null;
        this.horaCita   = null;
    }

    // ── Getters / Setters ────────────────────────────────────────
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
    public Medico    getDoc()                 { return medico; }
    public void      setDoc(Medico m)         { this.medico = m; }

    public Paciente  getPaciente()            { return paciente; }
    public void      setPaciente(Paciente p)  { this.paciente = p; }

    public String    getHoraCita()            { return horaCita; }
    public void      setHoraCita(String h)    { this.horaCita = h; }

    public Consultas getConsulta()            { return consulta; }
    public void      setConsulta(Consultas c) { this.consulta = c; }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return idCita + ","
            + (fechaCita != null ? df.format(fechaCita) : "null") + ","
            + completada + ","
            + (medico   != null ? medico.getCedula()   : "null") + ","
            + (paciente != null ? paciente.getCedula() : "null") + ","
            + (horaCita != null ? horaCita             : "null");
    }
}