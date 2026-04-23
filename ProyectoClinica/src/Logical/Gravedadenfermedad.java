package Logical;

public class Gravedadenfermedad {

    private String idGravedad;
    private String gravedad;

    public Gravedadenfermedad(String idGravedad, String gravedad) {
        this.idGravedad = idGravedad;
        this.gravedad   = gravedad;
    }

    public String getIdGravedad()           { return idGravedad; }
    public void   setIdGravedad(String id)  { this.idGravedad = id; }
    public String getGravedad()             { return gravedad; }
    public void   setGravedad(String g)     { this.gravedad = g; }


}
