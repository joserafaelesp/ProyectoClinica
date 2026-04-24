package Logical;

import java.time.LocalDate;

public class Persona {

    private String    cedula;
    private String    nombre;
    private String    genero;
    private LocalDate fechaNacimiento;
    private String    telefono;
    protected Vivienda  vivienda;   // protected para que Paciente acceda directo

    // Constructor completo — usado por Paciente y Medico
    public Persona(String cedula, String nombre, String genero,
                   LocalDate fechaNacimiento, String telefono,
                   Vivienda vivienda) {
        this.cedula          = cedula;
        this.nombre          = nombre;
        this.genero          = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono        = telefono;
        this.vivienda        = vivienda;
    }

    // Constructor sin fecha/vivienda — compatibilidad
    public Persona(String cedula, String nombre, String genero, String telefono) {
        this(cedula, nombre, genero, null, telefono, null);
    }

    public String    getCedula()                    { return cedula; }
    public void      setCedula(String c)            { this.cedula = c; }
    public String    getNombre()                    { return nombre; }
    public void      setNombre(String n)            { this.nombre = n; }
    public String    getGenero()                    { return genero; }
    public void      setGenero(String g)            { this.genero = g; }
    public String    getTelefono()                  { return telefono; }
    public void      setTelefono(String t)          { this.telefono = t; }
    public LocalDate getFechaNacimiento()           { return fechaNacimiento; }
    public void      setFechaNacimiento(LocalDate f){ this.fechaNacimiento = f; }
    public Vivienda  getViviend()                   { return vivienda; }
    public void      setViviend(Vivienda v)         { this.vivienda = v; }

    @Override
    public String toString() {
        return cedula + "," + nombre + "," + genero + "," + telefono;
    }
}