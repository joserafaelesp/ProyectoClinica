package Logical;

import java.time.LocalDate;

public class Paciente extends Persona {

    private String        idPaciente;
    private String        informacion;
    private String        tipoSangre;
    private HistoriaMedica historial;

    public Paciente(String cedula, String nombre, String genero,
                    LocalDate fechaNacimiento, String telefono,
                    Vivienda vivienda, String idPaciente,
                    String informacion, String tipoSangre) {
        super(cedula, nombre, genero, fechaNacimiento, telefono, vivienda);
        this.idPaciente  = idPaciente;
        this.informacion = informacion;
        this.tipoSangre  = tipoSangre;
        this.historial   = null;
    }

    public String        getIdPaciente()               { return idPaciente; }
    public void          setIdPaciente(String id)      { this.idPaciente = id; }
    public String        getInformacion()              { return informacion; }
    public void          setInformacion(String i)      { this.informacion = i; }
    public String        getInfoEmergencia()           { return informacion; }
    public void          setInfoEmergencia(String i)   { this.informacion = i; }
    public String        getTipoSangre()               { return tipoSangre; }
    public void          setTipoSangre(String ts)      { this.tipoSangre = ts; }
    public Vivienda      getViviend()                  { return (Vivienda) vivienda; }
    public void          setViviend(Vivienda v)        { this.vivienda = v; }
    public HistoriaMedica getHistorial()               { return historial; }
    public void          setHistorial(HistoriaMedica h){ this.historial = h; }

    @Override
    public String toString() {
        return super.toString() + "," + idPaciente + "," + informacion + "," + tipoSangre;
    }
}