package Logical;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cita {
	private String idCita;
	private Paciente paciente;
	private Medico doc;
	private Date fecha;

	public Cita(String idCita, Paciente paciente, Medico doc, Date fechaCita) {
		super();
		this.idCita = idCita;
		this.paciente = paciente;
		this.doc = doc;
		this.fecha = fechaCita;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return idCita + "," + 
			   (paciente != null ? paciente.getIdPaciente() : "null") + "," + 
			   (doc != null ? doc.getIdMedico() : "null") + "," + 
			   (fecha != null ? dateFormat.format(fecha) : "null");
	}

	// Getters y Setters...
	public String getIdCita() { return idCita; }
	public void setIdCita(String idCita) { this.idCita = idCita; }
	public Paciente getPaciente() { return paciente; }
	public void setPaciente(Paciente paciente) { this.paciente = paciente; }
	public Medico getDoc() { return doc; }
	public void setDoc(Medico doc) { this.doc = doc; }
	public Date getFecha() { return fecha; }
	public void setFecha(Date fecha) { this.fecha = fecha; }
}