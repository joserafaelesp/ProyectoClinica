package Logical;

import java.util.ArrayList;

public class Vivienda {

	private String idVivienda;
	private String Direccion; 
	private String telefono;
	private ArrayList<Persona> misPersonas;

	public Vivienda(String idVivienda, String direccion, String telefono, ArrayList<Persona> misPersonas) {
		super();
		this.idVivienda = idVivienda;
		this.Direccion = direccion;
		this.telefono = telefono;
		this.misPersonas = misPersonas != null ? misPersonas : new ArrayList<>();
	}

	@Override
	public String toString() {
		return idVivienda + "," + Direccion + "," + telefono;
	}

	// Getters y Setters...
	public String getIdVivienda() { return idVivienda; }
	public void setIdVivienda(String idVivienda) { this.idVivienda = idVivienda; }
	public String getDireccion() { return Direccion; }
	public void setDireccion(String direccion) { Direccion = direccion; }
	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public ArrayList<Persona> getMisPersonas() { return misPersonas; }
	public void setMisPersonas(ArrayList<Persona> misPersonas) { this.misPersonas = misPersonas; }
}