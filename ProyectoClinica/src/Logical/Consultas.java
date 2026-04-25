package Logical;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Consultas {

    private String           idConsulta;
    private Date             fechaConsulta;
    private String           diagnostico;
    private Medico           doctor;
    private Paciente         patient;
    private List<Enfermedad> enfermedades;
    private List<Examen>     examenes;
    private List<String>     vacunasAplicadas;
    private String           idCita;

    // Constructor principal
    public Consultas(String idConsulta, Date fechaConsulta, String diagnostico,
                     Medico doctor, Paciente patient) {
        this.idConsulta       = idConsulta;
        this.fechaConsulta    = fechaConsulta;
        this.diagnostico      = diagnostico;
        this.doctor           = doctor;
        this.patient          = patient;
        this.enfermedades     = new ArrayList<>();
        this.examenes         = new ArrayList<>();
        this.vacunasAplicadas = new ArrayList<>();
        this.idCita           = null;
    }

    // Constructor compatibilidad con GUI — recibe List<Enfermedad>
    public Consultas(String idConsulta, Date fechaConsulta,
                     List<Enfermedad> sintomas, Medico doctor, Paciente patient) {
        this.idConsulta       = idConsulta;
        this.fechaConsulta    = fechaConsulta;
        this.diagnostico      = null;
        this.doctor           = doctor;
        this.patient          = patient;
        this.enfermedades     = (sintomas != null) ? sintomas : new ArrayList<>();
        this.examenes         = new ArrayList<>();
        this.vacunasAplicadas = new ArrayList<>();
        this.idCita           = null;
    }

    // ── Getters y Setters ────────────────────────────────────────
    public String           getIdConsulta()                     { return idConsulta; }
    public void             setIdConsulta(String id)            { this.idConsulta = id; }
    public Date             getFechaConsulta()                  { return fechaConsulta; }
    public void             setFechaConsulta(Date d)            { this.fechaConsulta = d; }
    public String           getDiagnostico()                    { return diagnostico; }
    public void             setDiagnostico(String d)            { this.diagnostico = d; }
    public Medico           getDoctor()                         { return doctor; }
    public void             setDoctor(Medico m)                 { this.doctor = m; }
    public Paciente         getPatient()                        { return patient; }
    public void             setPatient(Paciente p)              { this.patient = p; }
    public List<Enfermedad> getEnfermedades()                   { return enfermedades; }
    public void             setEnfermedades(List<Enfermedad> e) { this.enfermedades = e; }
    public List<Enfermedad> getSintomas()                       { return enfermedades; }
    public void             setSintomas(List<Enfermedad> s)     { this.enfermedades = s; }
    public List<Examen>     getExamenes()                       { return examenes; }
    public void             setExamenes(List<Examen> e)         { this.examenes = e; }
    public List<String>     getVacunasAplicadas()               { return vacunasAplicadas; }
    public void             setVacunasAplicadas(List<String> v) { this.vacunasAplicadas = v; }
    public String           getIdCita()                         { return idCita; }
    public void             setIdCita(String id)                { this.idCita = id; }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return idConsulta + ","
            + (fechaConsulta != null ? df.format(fechaConsulta) : "null") + ","
            + (doctor  != null ? doctor.getCedula()  : "null") + ","
            + (patient != null ? patient.getCedula() : "null") + ","
            + diagnostico + ","
            + (idCita != null ? idCita : "null");
    }
}