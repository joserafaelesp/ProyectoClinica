package Logical;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Persona {

	protected String cedula;
	protected String nombre;
	protected String genero;
	protected Date fechaNacimiento;
	protected String telefono;
	protected Vivienda viviend;

	public Persona(String cedula, String nombre, String genero, Date fechaNacimiento, String telefono, Vivienda viviend) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.viviend = viviend;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return cedula + "," + nombre + "," + genero + "," + 
			   (fechaNacimiento != null ? dateFormat.format(fechaNacimiento) : "null") + 
			   "," + telefono + "," + (viviend != null ? viviend.getIdVivienda() : "null");
	}

	// Getters y Setters...
	public String getCedula() { return cedula; }
	public void setCedula(String cedula) { this.cedula = cedula; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getGenero() { return genero; }
	public void setGenero(String genero) { this.genero = genero; }
	public Date getFechaNacimiento() { return fechaNacimiento; }
	public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public Vivienda getViviend() { return viviend; }
	public void setViviend(Vivienda viviend) { this.viviend = viviend; }
}