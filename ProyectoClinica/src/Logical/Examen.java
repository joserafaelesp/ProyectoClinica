package Logical;

/**
 * Examen.java — CLASE NUEVA
 *
 * Representa un examen médico ordenado dentro de una consulta.
 * Relación: CONSULTA 1:N EXAMEN (Ordena)
 *
 * Tabla SQL Server:
 *   EXAMEN (Id_Examen PK, NombreExamen, Descripcion, Id_Consulta FK)
 */
public class Examen {

    private String    idExamen;
    private String    nombreExamen;
    private String    descripcion;
    private Consultas consulta;    // FK Id_Consulta → CONSULTA

    public Examen(String idExamen, String nombreExamen, String descripcion,
                  Consultas consulta) {
        this.idExamen     = idExamen;
        this.nombreExamen = nombreExamen;
        this.descripcion  = descripcion;
        this.consulta     = consulta;
    }

    // Constructor sin objeto Consultas (para cuando solo se necesita el ID)
    public Examen(String idExamen, String nombreExamen, String descripcion,
                  String idConsulta) {
        this.idExamen     = idExamen;
        this.nombreExamen = nombreExamen;
        this.descripcion  = descripcion;
        this.consulta     = null;
    }

    public String    getIdExamen()              { return idExamen; }
    public void      setIdExamen(String id)     { this.idExamen = id; }
    public String    getNombreExamen()           { return nombreExamen; }
    public void      setNombreExamen(String n)  { this.nombreExamen = n; }
    public String    getDescripcion()           { return descripcion; }
    public void      setDescripcion(String d)   { this.descripcion = d; }
    public Consultas getConsulta()              { return consulta; }
    public void      setConsulta(Consultas c)   { this.consulta = c; }

   
}

