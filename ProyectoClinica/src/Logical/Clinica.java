package Logical;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Clinica {
	
	private ArrayList<Medico> misMedico;
	private ArrayList<Paciente> misPaciente;
	private ArrayList<Consultas> misConsultas;
	private ArrayList<Vacuna> misVacunas;
	private ArrayList<Cita> misCitas;
	private ArrayList<Usuario> misUsuarios;
	private ArrayList<Enfermedad> misEnfermedades;
	private ArrayList<Vivienda> misViviendas;
	List<String> listaPacientesAsignados;
	public static int generadorCodigoCita = 1;
	private static Clinica clinica = null;
	public static int generadorCodigoConsulta = 1;
	public static int generadorCodigoEnfermedad = 1;
	public static int generadorCodigoHistorialMed = 1;
	public static int generadorCodigoidMedico = 1;
	public static int generadorCodigoVacuna = 1;
	public static int generadorCodigoVivienda = 1;
	public static int generadorCodigoUser = 1;
	public static int generadorCodigoPaciente = 1;
	
	public Clinica() {
		super();
		// Inicializar listas
		this.misMedico = new ArrayList<>();
		this.misPaciente = new ArrayList<>();
		this.misConsultas = new ArrayList<>();
		this.misVacunas = new ArrayList<>();
		this.misCitas = new ArrayList<>();
		this.misUsuarios = new ArrayList<>();
		this.listaPacientesAsignados = new ArrayList<>();
		this.misEnfermedades = new ArrayList<>();
		this.misViviendas = new ArrayList<>();
		
		// Cargar TODOS los datos desde los archivos al iniciar
		cargarTodosLosDatos();
	}
	
	public static Clinica getInstance(){
		if(clinica == null){
			clinica = new Clinica();
		}		
		return clinica;
	}
	
	// ==================== CARGAR TODOS LOS DATOS ====================
	
	private void cargarTodosLosDatos() {
		misUsuarios = archivoManager.leerUsuarios();
		misMedico = archivoManager.leerMedicos();
		misPaciente = archivoManager.leerPacientes();
		misVacunas = archivoManager.leerVacunas();
		misEnfermedades = archivoManager.leerEnfermedades();
		misViviendas = archivoManager.leerViviendas();
		misConsultas = archivoManager.leerConsultas();
		misCitas = archivoManager.leerCitas();
		
		// Actualizar generadores de código según los datos existentes
		actualizarGeneradores();
	}
	
	private void actualizarGeneradores() {
		// Usuarios
		int maxUser = 0;
		for (Usuario u : misUsuarios) {
			try {
				String numStr = u.getIdUsuario().replace("User-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxUser) maxUser = num;
			} catch (Exception e) {}
		}
		if (maxUser > 0) generadorCodigoUser = maxUser + 1;
		
		// Medicos
		int maxMed = 0;
		for (Medico m : misMedico) {
			try {
				String numStr = m.getIdMedico().replace("Medico-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxMed) maxMed = num;
			} catch (Exception e) {}
		}
		if (maxMed > 0) generadorCodigoidMedico = maxMed + 1;
		
		// Pacientes
		int maxPac = 0;
		for (Paciente p : misPaciente) {
			try {
				String numStr = p.getIdPaciente().replace("Paciente-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxPac) maxPac = num;
			} catch (Exception e) {}
		}
		if (maxPac > 0) generadorCodigoPaciente = maxPac + 1;
		
		// Enfermedades
		int maxEnf = 0;
		for (Enfermedad e : misEnfermedades) {
			try {
				String numStr = e.getIdEnfermedad().replace("Enfermedad-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxEnf) maxEnf = num;
			} catch (Exception e1) {}
		}
		if (maxEnf > 0) generadorCodigoEnfermedad = maxEnf + 1;
		
		// Vacunas
		int maxVac = 0;
		for (Vacuna v : misVacunas) {
			try {
				String numStr = v.getIdVacuna().replace("Vacuna-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxVac) maxVac = num;
			} catch (Exception e) {}
		}
		if (maxVac > 0) generadorCodigoVacuna = maxVac + 1;
		
		// Viviendas
		int maxViv = 0;
		for (Vivienda v : misViviendas) {
			try {
				String numStr = v.getIdVivienda().replace("Vivienda-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxViv) maxViv = num;
			} catch (Exception e) {}
		}
		if (maxViv > 0) generadorCodigoVivienda = maxViv + 1;
		
		// Consultas
		int maxCons = 0;
		for (Consultas c : misConsultas) {
			try {
				String numStr = c.getIdConsulta().replace("Consulta-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxCons) maxCons = num;
			} catch (Exception e) {}
		}
		if (maxCons > 0) generadorCodigoConsulta = maxCons + 1;
		
		// Citas
		int maxCit = 0;
		for (Cita c : misCitas) {
			try {
				String numStr = c.getIdCita().replace("Cita-", "");
				int num = Integer.parseInt(numStr);
				if (num > maxCit) maxCit = num;
			} catch (Exception e) {}
		}
		if (maxCit > 0) generadorCodigoCita = maxCit + 1;
	}
	
	// ==================== USUARIOS CRUD ====================
	
	public void agregarUsuario(Usuario usuario) {
		misUsuarios.add(usuario);
		archivoManager.crearUsuario(usuario);
		generadorCodigoUser++;
	}
	
	public void modificarUsuario(String idUsuario, Usuario usuario) {
		archivoManager.modificarUsuario(idUsuario, usuario);
		cargarTodosLosDatos(); // Recargar todo después de modificar
	}
	
	public void borrarUsuario(String idUsuario) {
		archivoManager.borrarUsuario(idUsuario);
		cargarTodosLosDatos(); // Recargar todo después de borrar
	}
	
	public ArrayList<Usuario> getMisUsuarios() {
		return misUsuarios; // Devolver la lista en memoria, NO leer de nuevo
	}
	
	public Usuario buscarUsuarioPorCodigo(String codigoUsuario) {
		for (Usuario usuario : misUsuarios) {
			if (usuario.getIdUsuario().equals(codigoUsuario)) {
				return usuario;
			}
		}
		return null;
	}
	
	// ==================== MEDICOS CRUD ====================
	
	public void agregarMedico(Medico medico) {
		misMedico.add(medico);
		archivoManager.crearMedico(medico);
		generadorCodigoidMedico++;
	}
	
	public void modificarMedico(String idMedico, Medico medico) {
		archivoManager.modificarMedico(idMedico, medico);
		cargarTodosLosDatos(); // Recargar todo después de modificar
	}
	
	public void borrarMedico(String idMedico) {
		archivoManager.borrarMedico(idMedico);
		cargarTodosLosDatos(); // Recargar todo después de borrar
	}
	
	public ArrayList<Medico> getMisMedico() {
		return misMedico; // Devolver la lista en memoria, NO leer de nuevo
	}
	
	public Medico obtenerMedicoById(String id) {
		for (Medico medico : misMedico) {
			if (medico.getIdMedico().equals(id)) {
				return medico;
			}
		}
		return null;
	}
	
	public Medico obtenerMedicoXnombre(String nombre) {
		for (Medico medico : misMedico) {
			if (medico.getNombre().equalsIgnoreCase(nombre)) {
				return medico;
			}
		}
		return null;
	}
	
	// ==================== PACIENTES CRUD ====================
	
	public void agregarPaciente(Paciente paciente) {
		misPaciente.add(paciente);
		archivoManager.crearPaciente(paciente);
		generadorCodigoPaciente++;
	}
	
	public void modificarPaciente(String cedula, Paciente paciente) {
		archivoManager.modificarPaciente(cedula, paciente);
		cargarTodosLosDatos(); // Recargar todo después de modificar
	}
	
	public void borrarPaciente(String cedula) {
		archivoManager.borrarPaciente(cedula);
		cargarTodosLosDatos(); // Recargar todo después de borrar
	}
	
	public ArrayList<Paciente> getMisPaciente() {
		return misPaciente; // Devolver la lista en memoria, NO leer de nuevo
	}
	
	public Paciente obtenerPacienteById(String id) {
		for (Paciente paciente : misPaciente) {
			if (paciente.getIdPaciente().equals(id)) {
				return paciente;
			}
		}
		return null;
	}
	
	public Paciente obtenerPacienteXnombre(String nombre) {
		for (Paciente paciente : misPaciente) {
			if (paciente.getNombre().equalsIgnoreCase(nombre)) {
				return paciente;
			}
		}
		return null;
	}
	
	// ==================== VACUNAS CRUD ====================
	
	public void agregarVacuna(Vacuna vacuna) {
		misVacunas.add(vacuna);
		archivoManager.crearVacuna(vacuna);
		generadorCodigoVacuna++;
	}
	
	public void modificarVacuna(String idVacuna, Vacuna vacuna) {
		archivoManager.modificarVacuna(idVacuna, vacuna);
		cargarTodosLosDatos();
	}
	
	public void borrarVacuna(String idVacuna) {
		archivoManager.borrarVacuna(idVacuna);
		cargarTodosLosDatos();
	}
	
	public ArrayList<Vacuna> getMisVacunas() {
		return misVacunas;
	}
	
	public Vacuna obtenervacuna(String idVacuna) {
		for (Vacuna vacuna : misVacunas) {
			if (vacuna.getIdVacuna().equalsIgnoreCase(idVacuna)) {
				return vacuna;
			}
		}
		return null;
	}
	
	// ==================== ENFERMEDADES CRUD ====================
	
	public void agregarEnfermedad(Enfermedad enfermedad) {
		misEnfermedades.add(enfermedad);
		archivoManager.crearEnfermedad(enfermedad);
		generadorCodigoEnfermedad++;
	}
	
	public void modificarEnfermedad(String idEnfermedad, Enfermedad enfermedad) {
		archivoManager.modificarEnfermedad(idEnfermedad, enfermedad);
		cargarTodosLosDatos();
	}
	
	public void borrarEnfermedad(String idEnfermedad) {
		archivoManager.borrarEnfermedad(idEnfermedad);
		cargarTodosLosDatos();
	}
	
	public ArrayList<Enfermedad> getMisEnfermedades() {
		return misEnfermedades;
	}
	
	public Enfermedad obtenerEnfermedadById(String id) {
		for (Enfermedad enfermedad : misEnfermedades) {
			if (enfermedad.getIdEnfermedad().equals(id)) {
				return enfermedad;
			}
		}
		return null;
	}
	
	// ==================== VIVIENDAS CRUD ====================
	
	public void agregarVivienda(Vivienda vivienda) {
		misViviendas.add(vivienda);
		archivoManager.crearVivienda(vivienda);
		generadorCodigoVivienda++;
	}
	
	public void modificarVivienda(String idVivienda, Vivienda vivienda) {
		archivoManager.modificarVivienda(idVivienda, vivienda);
		cargarTodosLosDatos();
	}
	
	public void borrarVivienda(String idVivienda) {
		archivoManager.borrarVivienda(idVivienda);
		cargarTodosLosDatos();
	}
	
	public ArrayList<Vivienda> getMisViviendas() {
		return misViviendas;
	}
	
	public Vivienda obtenervivienda(String idVivienda) {
		for (Vivienda vivienda : misViviendas) {
			if (vivienda.getIdVivienda().equals(idVivienda)) {
				return vivienda;
			}
		}
		return null;
	}
	
	// ==================== CONSULTAS CRUD ====================
	
	public void agregarConsulta(Consultas consulta) {
		misConsultas.add(consulta);
		archivoManager.crearConsulta(consulta);
		generadorCodigoConsulta++;
	}
	
	public void modificarConsulta(String idConsulta, Consultas consulta) {
		archivoManager.modificarConsulta(idConsulta, consulta);
		cargarTodosLosDatos();
	}
	
	public void borrarConsulta(String idConsulta) {
		archivoManager.borrarConsulta(idConsulta);
		cargarTodosLosDatos();
	}
	
	public ArrayList<Consultas> getMisConsultas() {
		return misConsultas;
	}
	
	// ==================== CITAS CRUD ====================
	
	public void agregarCita(Cita cita) {
		misCitas.add(cita);
		archivoManager.crearCita(cita);
		generadorCodigoCita++;
	}
	
	public void modificarCita(String idCita, Cita cita) {
		archivoManager.modificarCita(idCita, cita);
		cargarTodosLosDatos();
	}
	
	public void borrarCita(String idCita) {
		archivoManager.borrarCita(idCita);
		cargarTodosLosDatos();
	}
	
	public ArrayList<Cita> getMisCitas() {
		return misCitas;
	}
	
	// ==================== MÉTODOS EXISTENTES ====================
	
	public void crearCita(String idCita, Paciente paciente, Medico medico, Date fecha) {
		Cita nuevaCita = new Cita(idCita, paciente, medico, fecha);
		agregarCita(nuevaCita);
	}
	
	public void asignarPacienteMedico(String paciente) {
		listaPacientesAsignados.add(paciente);
	}
	
	public void insertarConsulta(Consultas consultas) {
		agregarConsulta(consultas);
	}
	
	public List<Enfermedad> getEnfermedadesDiagnosticadas() {
		List<Enfermedad> enfermedadesDiagnosticadas = new ArrayList<>();
		for (Consultas consulta : misConsultas) {
			List<Enfermedad> enfermedadesConsulta = consulta.getSintomas();
			if (enfermedadesConsulta != null) {
				enfermedadesDiagnosticadas.addAll(enfermedadesConsulta);
			}
		}
		return enfermedadesDiagnosticadas;
	}
	
	// Getters y Setters
	public void setMisUsuarios(ArrayList<Usuario> misUsuarios) { this.misUsuarios = misUsuarios; }
	public void setMisMedico(ArrayList<Medico> misMedico) { this.misMedico = misMedico; }
	public void setMisPaciente(ArrayList<Paciente> misPaciente) { this.misPaciente = misPaciente; }
	public void setMisConsultas(ArrayList<Consultas> misConsultas) { this.misConsultas = misConsultas; }
	public void setMisVacunas(ArrayList<Vacuna> misVacunas) { this.misVacunas = misVacunas; }
	public void setMisCitas(ArrayList<Cita> misCitas) { this.misCitas = misCitas; }
	public void setMisEnfermedades(ArrayList<Enfermedad> misEnfermedades) { this.misEnfermedades = misEnfermedades; }
	public void setMisViviendas(ArrayList<Vivienda> misViviendas) { this.misViviendas = misViviendas; }
}