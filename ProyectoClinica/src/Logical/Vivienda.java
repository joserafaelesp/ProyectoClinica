package Logical;

import java.util.ArrayList;

/**
 * Vivienda.java — sin cambios de estructura, solo Direccion (sin telefono
 * ya que el modelo relacional de la imagen no lo incluye).
 */
public class Vivienda {

    private String idVivienda;
    private String direccion;
    private ArrayList<Persona> residentes;

    public Vivienda(String idVivienda, String direccion) {
        this.idVivienda  = idVivienda;
        this.direccion   = direccion;
        this.residentes  = new ArrayList<>();
    }

    // Constructor de compatibilidad con código existente
    public Vivienda(String idVivienda, String direccion, String telefonoIgnorado,
                    ArrayList<?> lista) {
        this(idVivienda, direccion);
    }

    public String getIdVivienda()           { return idVivienda; }
    public void   setIdVivienda(String id)  { this.idVivienda = id; }
    public String getDireccion()            { return direccion; }
    public void   setDireccion(String d)    { this.direccion = d; }
    // getTelefono por compatibilidad
    public String getTelefono()             { return ""; }

    @Override
    public String toString() {
        return idVivienda + "," + direccion;
    }
}