package Logical;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Persona {
    protected String cedula;
    protected String nombre;
    protected String genero;
    protected LocalDate fechaNacimiento;
    protected String telefono;
    protected Vivienda vivienda;

    public Persona(String cedula, String nombre, String genero, LocalDate fechaNacimiento, String telefono, Vivienda vivienda) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.vivienda = vivienda;
    }

    protected String formatearFecha() {
        return (fechaNacimiento != null) ? fechaNacimiento.format(DateTimeFormatter.ISO_LOCAL_DATE) : "null";
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Vivienda getViviend() { return vivienda; }
    public void setViviend(Vivienda vivienda) { this.vivienda = vivienda; }
}