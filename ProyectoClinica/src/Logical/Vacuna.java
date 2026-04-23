package Logical;

/**
 * Vacuna.java — actualizada según modelo relacional.
 * Campos: Id_Vacuna, NombreVacuna, Descripcion
 */
public class Vacuna {

    private String idVacuna;
    private String nombreVacuna;
    private String descripcion;

    public Vacuna(String idVacuna, String nombreVacuna, String descripcion) {
        this.idVacuna     = idVacuna;
        this.nombreVacuna = nombreVacuna;
        this.descripcion  = descripcion;
    }

    // Constructor compatibilidad con código anterior
    public Vacuna(String idVacuna, String nombreVacuna, Object ignorado,
                  int cantidadIgnorada, String descripcion) {
        this(idVacuna, nombreVacuna, descripcion);
    }

    public String getIdVacuna()              { return idVacuna; }
    public void   setIdVacuna(String id)     { this.idVacuna = id; }
    public String getNombreVacuna()          { return nombreVacuna; }
    public void   setNombreVacuna(String n)  { this.nombreVacuna = n; }
    public String getDescripcion()           { return descripcion; }
    public void   setDescripcion(String d)   { this.descripcion = d; }
    // compatibilidad
    public int    getCantidad()              { return 0; }

    @Override
    public String toString() {
        return idVacuna + "," + nombreVacuna + "," + descripcion;
    }
}