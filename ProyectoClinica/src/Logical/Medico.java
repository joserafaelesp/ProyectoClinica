package Logical;

import java.util.Date;

public class Medico extends Persona {

	private String idMedico;
	private String especialidad;

	public Medico(String cedula, String nombre, String genero, Date fechaNacimiento, String telefono,
			Vivienda viviend, String idMedico, String especialidad) {
		super(cedula, nombre, genero, fechaNacimiento, telefono, viviend);
		this.idMedico = idMedico;
		this.especialidad = especialidad;
	}

	@Override
	public String toString() {
		return super.toString() + "," + idMedico + "," + especialidad;
	}

	// Getters y Setters...
	public String getIdMedico() { return idMedico; }
	public void setIdMedico(String idMedico) { this.idMedico = idMedico; }
	public String getEspecialidad() { return especialidad; }
	public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}