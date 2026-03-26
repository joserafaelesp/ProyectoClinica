package Logical;

import java.sql.Date;
import java.util.ArrayList;

public class ResumenHistorial {
	private String codigo;
	private ArrayList<Medico> medicos;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Date> fecha;
	
	public ResumenHistorial(String codigo, ArrayList<Medico> medicos, 
			ArrayList<Enfermedad> enfermedades, ArrayList<Date> fecha) {
		super();
		this.codigo = codigo;
		this.medicos = medicos != null ? medicos : new ArrayList<>();
		this.enfermedades = enfermedades != null ? enfermedades : new ArrayList<>();
		this.fecha = fecha != null ? fecha : new ArrayList<>();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(codigo);
		for (Medico m : medicos) {
			sb.append(",").append(m.getIdMedico());
		}
		sb.append(";");
		for (Enfermedad e : enfermedades) {
			sb.append(",").append(e.getIdEnfermedad());
		}
		sb.append(";");
		for (Date d : fecha) {
			sb.append(",").append(d.toString());
		}
		return sb.toString();
	}
	
	// Getters y Setters...
	public String getCodigo() { return codigo; }
	public void setCodigo(String codigo) { this.codigo = codigo; }
	public ArrayList<Medico> getMedicos() { return medicos; }
	public void setMedicos(ArrayList<Medico> medicos) { this.medicos = medicos; }
	public ArrayList<Enfermedad> getEnfermedades() { return enfermedades; }
	public void setEnfermedades(ArrayList<Enfermedad> enfermedades) { this.enfermedades = enfermedades; }
	public ArrayList<Date> getFecha() { return fecha; }
	public void setFecha(ArrayList<Date> fecha) { this.fecha = fecha; }
}