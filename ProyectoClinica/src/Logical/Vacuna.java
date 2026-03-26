package Logical;

public class Vacuna {

	private String idVacuna;
	private String nombreVacuna;
	private Paciente idPaciente;
	private int cantidad;
	private String Descripcion;

	public Vacuna(String idVacuna, String nombreVacuna, Paciente idPaciente, int cantidad, String Descripcion) {
		super();
		this.idVacuna = idVacuna;
		this.nombreVacuna = nombreVacuna;
		this.idPaciente = idPaciente;
		this.cantidad = cantidad;
		this.Descripcion = Descripcion;
	}

	@Override
	public String toString() {
		return idVacuna + "," + nombreVacuna + "," + 
			   (idPaciente != null ? idPaciente.getIdPaciente() : "null") + "," + 
			   cantidad + "," + Descripcion;
	}

	// Getters y Setters...
	public String getIdVacuna() { return idVacuna; }
	public void setIdVacuna(String idVacuna) { this.idVacuna = idVacuna; }
	public String getNombreVacuna() { return nombreVacuna; }
	public void setNombreVacuna(String nombreVacuna) { this.nombreVacuna = nombreVacuna; }
	public Paciente getIdPaciente() { return idPaciente; }
	public void setIdPaciente(Paciente idPaciente) { this.idPaciente = idPaciente; }
	public int getCantidad() { return cantidad; }
	public void setCantidad(int cantidad) { this.cantidad = cantidad; }
	public String getDescripcion() { return Descripcion; }
	public void setDescripcion(String descripcion) { Descripcion = descripcion; }
}