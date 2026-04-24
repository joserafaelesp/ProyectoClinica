package Logical;

/**
 * Vacuna.java — actualizada según modelo relacional.
 * Campos: Id_Vacuna, NombreVacuna, Descripcion, EnfermedadAsociada
 */
public class Vacuna {

    private String idVacuna;
    private String nombreVacuna;
    private String descripcion;
    private Enfermedad enfermedadAsociada; // ARREGLADO: Relación con Enfermedad

    public Vacuna(String idVacuna, String nombreVacuna, String descripcion, Enfermedad enfermedad) {
        this.idVacuna     = idVacuna;
        this.nombreVacuna = nombreVacuna;
        this.descripcion  = descripcion;
        this.enfermedadAsociada = enfermedad;
    }

    // Constructor compatibilidad con código anterior (por si tu GUI antigua no manda enfermedad aún)
    public Vacuna(String idVacuna, String nombreVacuna, Object ignorado,
                  int cantidadIgnorada, String descripcion) {
        this(idVacuna, nombreVacuna, descripcion, null);
    }
    
    // Constructor compatibilidad básico
    public Vacuna(String idVacuna, String nombreVacuna, String descripcion) {
        this(idVacuna, nombreVacuna, descripcion, null);
    }

    public String getIdVacuna()              { return idVacuna; }
    public void   setIdVacuna(String id)     { this.idVacuna = id; }
    public String getNombreVacuna()          { return nombreVacuna; }
    public void   setNombreVacuna(String n)  { this.nombreVacuna = n; }
    public String getDescripcion()           { return descripcion; }
    public void   setDescripcion(String d)   { this.descripcion = d; }
    public Enfermedad getEnfermedadAsociada() { return enfermedadAsociada; }
    public void   setEnfermedadAsociada(Enfermedad e) { this.enfermedadAsociada = e; }
    
    // compatibilidad
    public int    getCantidad()              { return 0; }

    @Override
    public String toString() {
        return idVacuna + "," + nombreVacuna + "," + descripcion;
    }
}