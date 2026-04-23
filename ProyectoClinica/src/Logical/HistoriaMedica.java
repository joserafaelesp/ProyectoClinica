package Logical;

import java.util.ArrayList;
import java.util.List;

/**
 * HistoriaMedica.java — actualizada.
 * En BD: HISTORIAL_MEDICO (Id_Historial PK, cedula FK→PACIENTE)
 */
public class HistoriaMedica {

    private String          idHistorial;
    private Paciente        paciente;
    private List<Consultas> consultas;

    public HistoriaMedica(String idHistorial, Paciente paciente) {
        this.idHistorial = idHistorial;
        this.paciente    = paciente;
        this.consultas   = new ArrayList<>();
    }

    // Constructor compatibilidad
    public HistoriaMedica(ArrayList<?> lista) {
        this.idHistorial = "";
        this.paciente    = null;
        this.consultas   = new ArrayList<>();
    }

    public String          getIdHistorial()             { return idHistorial; }
    public void            setIdHistorial(String id)   { this.idHistorial = id; }
    public Paciente        getPaciente()               { return paciente; }
    public void            setPaciente(Paciente p)     { this.paciente = p; }
    public List<Consultas> getConsultas()              { return consultas; }
    public void            setConsultas(List<Consultas> c){ this.consultas = c; }

    @Override
    public String toString() {
        return idHistorial + ","
            + (paciente != null ? paciente.getCedula() : "null");
    }
}