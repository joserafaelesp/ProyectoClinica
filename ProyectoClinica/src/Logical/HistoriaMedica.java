package Logical;

import java.sql.Date;
import java.util.ArrayList;

public class HistoriaMedica {

	private ArrayList<Consultas> historialConsultas;
	private ResumenHistorial resumenPaciente;

	public HistoriaMedica(ArrayList<Consultas> historialConsultas) {
		super();
		this.historialConsultas = historialConsultas != null ? historialConsultas : new ArrayList<>();
		this.resumenPaciente = new ResumenHistorial("", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	public ArrayList<Consultas> getHistorialConsultas() {
		return historialConsultas;
	}

	public void setHistorialConsultas(ArrayList<Consultas> historialConsultas) {
		this.historialConsultas = historialConsultas;
	}

	public void agregarConsulta(Consultas consult) {
		if (historialConsultas == null) {
			historialConsultas = new ArrayList<>();
		}
		historialConsultas.add(consult);
	}

	public ResumenHistorial generarResumen() {
		ResumenHistorial resumen = new ResumenHistorial("", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		for (Consultas consulta : historialConsultas) {
			if (consulta.getDoctor() != null) {
				resumen.getMedicos().add(consulta.getDoctor());
			}
			if (consulta.getSintomas() != null) {
				resumen.getEnfermedades().addAll(consulta.getSintomas());
			}
			if (consulta.getFechaConsulta() != null) {
				resumen.getFecha().add(new Date(consulta.getFechaConsulta().getTime()));
			}
		}
		return resumen;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HIST_" + System.currentTimeMillis());
		if (historialConsultas != null) {
			for (Consultas c : historialConsultas) {
				sb.append(",").append(c.getIdConsulta());
			}
		}
		return sb.toString();
	}

	public ResumenHistorial getResumenPaciente() {
		return resumenPaciente;
	}

	public void setResumenPaciente(ResumenHistorial resumenPaciente) {
		this.resumenPaciente = resumenPaciente;
	}
}