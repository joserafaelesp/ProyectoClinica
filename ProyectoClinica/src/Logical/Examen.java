package Logical;

public class Examen {

    private String idExamen;
    private String nombreExamen;
    private String descripcion;

    // Constructor simplificado — ya no tiene FK a Consulta (es N:M via CONSULTA_EXAMEN)
    public Examen(String idExamen, String nombreExamen, String descripcion) {
        this.idExamen     = idExamen;
        this.nombreExamen = nombreExamen;
        this.descripcion  = descripcion;
    }

    // Constructor de compatibilidad — ignora el 4to parametro
    public Examen(String idExamen, String nombreExamen, String descripcion,
                  Object ignorado) {
        this(idExamen, nombreExamen, descripcion);
    }

    public String getIdExamen()             { return idExamen; }
    public void   setIdExamen(String id)    { this.idExamen = id; }
    public String getNombreExamen()          { return nombreExamen; }
    public void   setNombreExamen(String n) { this.nombreExamen = n; }
    public String getDescripcion()          { return descripcion; }
    public void   setDescripcion(String d)  { this.descripcion = d; }

    @Override
    public String toString() {
        return idExamen + "," + nombreExamen + "," + descripcion;
    }
}