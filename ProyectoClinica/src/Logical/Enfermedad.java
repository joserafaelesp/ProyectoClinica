package Logical;

public class Enfermedad {

    private String             idEnfermedad;
    private String             nombreEnfermedad;
    private String             sintomas;
    private Gravedadenfermedad gravedad;

    // ── Constructor principal (4 parámetros) ─────────────────────
    public Enfermedad(String idEnfermedad, String nombreEnfermedad,
                      String sintomas, Gravedadenfermedad gravedad) {
        this.idEnfermedad     = idEnfermedad;
        this.nombreEnfermedad = nombreEnfermedad;
        this.sintomas         = sintomas;
        this.gravedad         = gravedad;
    }

    // ── Constructor compatibilidad con código anterior ────────────
    // Cubre llamadas como: new Enfermedad(id, nombre, descripcion, sintomas, vacuna, vigilancia)
    public Enfermedad(String idEnfermedad, String nombreEnfermedad,
                      String descripcion, String sintomas,
                      Object vacunaIgnorada, boolean vigilanciaIgnorada) {
        this(idEnfermedad, nombreEnfermedad, sintomas, null);
    }

    // ── Constructor de 3 parámetros que usa ConsultaDAO ──────────
    // Cubre: new Enfermedad(id, nombre, sintomas, null)
    // null aquí es GravedadEnfermedad → ya cubierto por constructor principal

    // ── Getters y Setters ────────────────────────────────────────
    public String             getIdEnfermedad()               { return idEnfermedad; }
    public void               setIdEnfermedad(String id)      { this.idEnfermedad = id; }
    public String             getNombreEnfermedad()           { return nombreEnfermedad; }
    public void               setNombreEnfermedad(String n)   { this.nombreEnfermedad = n; }
    public String             getSintomas()                   { return sintomas; }
    public void               setSintomas(String s)           { this.sintomas = s; }
    public Gravedadenfermedad getGravedad()                   { return gravedad; }
    public void               setGravedad(Gravedadenfermedad g){ this.gravedad = g; }

    // ── Métodos de compatibilidad con código anterior ─────────────
    public String  getDescripcion()           { return sintomas; }
    public boolean isEnfermedadVigilancia()   { return false; }
    public Object  getVacunaDispo()           { return null; }

    @Override
    public String toString() {
        return idEnfermedad + "," + nombreEnfermedad + "," + sintomas;
    }
}