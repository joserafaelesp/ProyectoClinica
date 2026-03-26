package Logical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class archivoManager {

	private static final String FILE_PATH = "usuarios.txt";	
	private static final String FILE_PATH1 = "Medico.txt";
	private static final String FILE_PATH2 = "Paciente.txt";
	private static final String FILE_PATH3 = "vacunas.txt";
	private static final String FILE_PATH4 = "enfermedad.txt";
	private static final String FILE_PATH5 = "vivienda.txt";
	private static final String FILE_PATH6 = "consultas.txt";
	private static final String FILE_PATH7 = "citas.txt";
	private static final String FILE_PATH8 = "historial.txt";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	// ==================== USUARIOS CRUD ====================
	
	public static void crearUsuario(Usuario usuario) {
		ArrayList<Usuario> usuarios = leerUsuarios();
		usuarios.add(usuario);
		guardarUsuarios(usuarios);
	}
	
	public static ArrayList<Usuario> leerUsuarios() {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 4) {
					usuarios.add(new Usuario(parts[0], parts[1], parts[2], parts[3]));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return usuarios;
	}
	
	public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, false)))) {
			for (Usuario usuario : usuarios) {
				writer.println(usuario.getIdUsuario() + "," + usuario.getNombreUser() + "," 
						+ usuario.getPassword() + "," + usuario.getRol());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarUsuario(String idUsuario, Usuario usuarioModificado) {
		ArrayList<Usuario> usuarios = leerUsuarios();
		for (int i = 0; i < usuarios.size(); i++) {
			if (usuarios.get(i).getIdUsuario().equals(idUsuario)) {
				usuarios.set(i, usuarioModificado);
				break;
			}
		}
		guardarUsuarios(usuarios);
	}
	
	public static void borrarUsuario(String idUsuario) {
		ArrayList<Usuario> usuarios = leerUsuarios();
		usuarios.removeIf(u -> u.getIdUsuario().equals(idUsuario));
		guardarUsuarios(usuarios);
	}
	
	// ==================== MEDICOS CRUD ====================
	
	public static void crearMedico(Medico medico) {
		ArrayList<Medico> medicos = leerMedicos();
		medicos.add(medico);
		guardarMedicos(medicos);
	}
	
	public static ArrayList<Medico> leerMedicos() {
		ArrayList<Medico> medicos = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH1))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 9) { 
					Vivienda vivienda = obtenerViviendaPorId(parts[5]);
					medicos.add(new Medico(parts[0], parts[1], parts[2], 
							parseDate(parts[3]), parts[4], vivienda, parts[6], parts[7]));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return medicos;
	}
	
	public static void guardarMedicos(ArrayList<Medico> medicos) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH1, false)))) {
			for (Medico m : medicos) {
				writer.println(m.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarMedico(String idMedico, Medico medicoModificado) {
		ArrayList<Medico> medicos = leerMedicos();
		for (int i = 0; i < medicos.size(); i++) {
			if (medicos.get(i).getIdMedico().equals(idMedico)) {
				medicos.set(i, medicoModificado);
				break;
			}
		}
		guardarMedicos(medicos);
	}
	
	public static void borrarMedico(String idMedico) {
		ArrayList<Medico> medicos = leerMedicos();
		medicos.removeIf(m -> m.getIdMedico().equals(idMedico));
		guardarMedicos(medicos);
	}
	
	// ==================== PACIENTES CRUD ====================
	
	public static void crearPaciente(Paciente paciente) {
		ArrayList<Paciente> pacientes = leerPacientes();
		pacientes.add(paciente);
		guardarPacientes(pacientes);
	}
	
	public static ArrayList<Paciente> leerPacientes() {
		ArrayList<Paciente> pacientes = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH2))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 10) {
					Vivienda vivienda = obtenerViviendaPorId(parts[5]);
					HistoriaMedica historia = new HistoriaMedica(new ArrayList<>());
					pacientes.add(new Paciente(parts[0], parts[1], parts[2], parseDate(parts[3]), 
							parts[4], parts[6], vivienda, parts[7], historia));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return pacientes;
	}
	
	public static void guardarPacientes(ArrayList<Paciente> pacientes) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH2, false)))) {
			for (Paciente p : pacientes) {
				writer.println(p.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarPaciente(String cedula, Paciente pacienteModificado) {
		ArrayList<Paciente> pacientes = leerPacientes();
		for (int i = 0; i < pacientes.size(); i++) {
			if (pacientes.get(i).getCedula().equals(cedula)) {
				pacientes.set(i, pacienteModificado);
				break;
			}
		}
		guardarPacientes(pacientes);
	}
	
	public static void borrarPaciente(String cedula) {
		ArrayList<Paciente> pacientes = leerPacientes();
		pacientes.removeIf(p -> p.getCedula().equals(cedula));
		guardarPacientes(pacientes);
	}
	
	// ==================== VACUNAS CRUD ====================
	
	public static void crearVacuna(Vacuna vacuna) {
		ArrayList<Vacuna> vacunas = leerVacunas();
		vacunas.add(vacuna);
		guardarVacunas(vacunas);
	}
	
	public static ArrayList<Vacuna> leerVacunas() {
		ArrayList<Vacuna> vacunas = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH3))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 5) { 
					Paciente paciente = obtenerPacientePorId(parts[2]);
					vacunas.add(new Vacuna(parts[0], parts[1], paciente, 
							Integer.parseInt(parts[3]), parts[4]));
				}
			}
		} catch (IOException | NumberFormatException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return vacunas;
	}
	
	public static void guardarVacunas(ArrayList<Vacuna> vacunas) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH3, false)))) {
			for (Vacuna v : vacunas) {
				writer.println(v.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarVacuna(String idVacuna, Vacuna vacunaModificada) {
		ArrayList<Vacuna> vacunas = leerVacunas();
		for (int i = 0; i < vacunas.size(); i++) {
			if (vacunas.get(i).getIdVacuna().equals(idVacuna)) {
				vacunas.set(i, vacunaModificada);
				break;
			}
		}
		guardarVacunas(vacunas);
	}
	
	public static void borrarVacuna(String idVacuna) {
		ArrayList<Vacuna> vacunas = leerVacunas();
		vacunas.removeIf(v -> v.getIdVacuna().equals(idVacuna));
		guardarVacunas(vacunas);
	}
	
	// ==================== ENFERMEDADES CRUD ====================
	
	public static void crearEnfermedad(Enfermedad enfermedad) {
		ArrayList<Enfermedad> enfermedades = leerEnfermedades();
		enfermedades.add(enfermedad);
		guardarEnfermedades(enfermedades);
	}
	
	public static ArrayList<Enfermedad> leerEnfermedades() {
		ArrayList<Enfermedad> enfermedades = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH4))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 6) { 
					Vacuna vacuna = obtenerVacunaPorId(parts[4]);
					enfermedades.add(new Enfermedad(parts[0], parts[1], parts[2], parts[3], 
							vacuna, Boolean.parseBoolean(parts[5])));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return enfermedades;
	}
	
	public static void guardarEnfermedades(ArrayList<Enfermedad> enfermedades) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH4, false)))) {
			for (Enfermedad e : enfermedades) {
				writer.println(e.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarEnfermedad(String idEnfermedad, Enfermedad enfermedadModificada) {
		ArrayList<Enfermedad> enfermedades = leerEnfermedades();
		for (int i = 0; i < enfermedades.size(); i++) {
			if (enfermedades.get(i).getIdEnfermedad().equals(idEnfermedad)) {
				enfermedades.set(i, enfermedadModificada);
				break;
			}
		}
		guardarEnfermedades(enfermedades);
	}
	
	public static void borrarEnfermedad(String idEnfermedad) {
		ArrayList<Enfermedad> enfermedades = leerEnfermedades();
		enfermedades.removeIf(e -> e.getIdEnfermedad().equals(idEnfermedad));
		guardarEnfermedades(enfermedades);
	}
	
	// ==================== VIVIENDAS CRUD ====================
	
	public static void crearVivienda(Vivienda vivienda) {
		ArrayList<Vivienda> viviendas = leerViviendas();
		viviendas.add(vivienda);
		guardarViviendas(viviendas);
	}
	
	public static ArrayList<Vivienda> leerViviendas() {
		ArrayList<Vivienda> viviendas = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH5))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) { 
					viviendas.add(new Vivienda(parts[0], parts[1], parts[2], new ArrayList<>()));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return viviendas;
	}
	
	public static void guardarViviendas(ArrayList<Vivienda> viviendas) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH5, false)))) {
			for (Vivienda v : viviendas) {
				writer.println(v.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarVivienda(String idVivienda, Vivienda viviendaModificada) {
		ArrayList<Vivienda> viviendas = leerViviendas();
		for (int i = 0; i < viviendas.size(); i++) {
			if (viviendas.get(i).getIdVivienda().equals(idVivienda)) {
				viviendas.set(i, viviendaModificada);
				break;
			}
		}
		guardarViviendas(viviendas);
	}
	
	public static void borrarVivienda(String idVivienda) {
		ArrayList<Vivienda> viviendas = leerViviendas();
		viviendas.removeIf(v -> v.getIdVivienda().equals(idVivienda));
		guardarViviendas(viviendas);
	}
	
	// ==================== CONSULTAS CRUD ====================
	
	public static void crearConsulta(Consultas consulta) {
		ArrayList<Consultas> consultas = leerConsultas();
		consultas.add(consulta);
		guardarConsultas(consultas);
	}
	
	public static ArrayList<Consultas> leerConsultas() {
		ArrayList<Consultas> consultas = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH6))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 5) {
					Date fechaConsulta = parseDate(parts[1]);
					
					List<Enfermedad> sintomas = new ArrayList<>();
					String[] sintomasArray = parts[2].split(";");
					for (String sintomaId : sintomasArray) {
						if (!sintomaId.isEmpty()) {
							Enfermedad enfermedad = obtenerEnfermedadPorId(sintomaId);
							if (enfermedad != null) {
								sintomas.add(enfermedad);
							}
						}
					}
					
					Medico doctor = obtenerMedicoPorId(parts[3]);
					Paciente patient = obtenerPacientePorId(parts[4]);
					
					consultas.add(new Consultas(parts[0], fechaConsulta, sintomas, doctor, patient));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return consultas;
	}
	
	public static void guardarConsultas(ArrayList<Consultas> consultas) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH6, false)))) {
			for (Consultas c : consultas) {
				writer.println(c.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarConsulta(String idConsulta, Consultas consultaModificada) {
		ArrayList<Consultas> consultas = leerConsultas();
		for (int i = 0; i < consultas.size(); i++) {
			if (consultas.get(i).getIdConsulta().equals(idConsulta)) {
				consultas.set(i, consultaModificada);
				break;
			}
		}
		guardarConsultas(consultas);
	}
	
	public static void borrarConsulta(String idConsulta) {
		ArrayList<Consultas> consultas = leerConsultas();
		consultas.removeIf(c -> c.getIdConsulta().equals(idConsulta));
		guardarConsultas(consultas);
	}
	
	// ==================== CITAS CRUD ====================
	
	public static void crearCita(Cita cita) {
		ArrayList<Cita> citas = leerCitas();
		citas.add(cita);
		guardarCitas(citas);
	}
	
	public static ArrayList<Cita> leerCitas() {
		ArrayList<Cita> citas = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH7))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 4) { 
					Date fechaCita = parseDate(parts[3]);
					Paciente paciente = obtenerPacientePorId(parts[1]);
					Medico medico = obtenerMedicoPorId(parts[2]);
					
					citas.add(new Cita(parts[0], paciente, medico, fechaCita));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return citas;
	}
	
	public static void guardarCitas(ArrayList<Cita> citas) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH7, false)))) {
			for (Cita c : citas) {
				writer.println(c.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarCita(String idCita, Cita citaModificada) {
		ArrayList<Cita> citas = leerCitas();
		for (int i = 0; i < citas.size(); i++) {
			if (citas.get(i).getIdCita().equals(idCita)) {
				citas.set(i, citaModificada);
				break;
			}
		}
		guardarCitas(citas);
	}
	
	public static void borrarCita(String idCita) {
		ArrayList<Cita> citas = leerCitas();
		citas.removeIf(c -> c.getIdCita().equals(idCita));
		guardarCitas(citas);
	}
	
	// ==================== HISTORIAL CRUD ====================
	
	public static void crearHistoria(HistoriaMedica historia) {
		ArrayList<HistoriaMedica> historias = leerHistorias();
		historias.add(historia);
		guardarHistorias(historias);
	}
	
	public static ArrayList<HistoriaMedica> leerHistorias() {
		ArrayList<HistoriaMedica> historias = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length > 0) { 
					ArrayList<Consultas> consultas = new ArrayList<>();
					for (int i = 1; i < parts.length; i++) {
						Consultas c = obtenerConsultaPorId(parts[i]);
						if (c != null) {
							consultas.add(c);
						}
					}
					historias.add(new HistoriaMedica(consultas));
				}
			}
		} catch (IOException e) {
			// Si el archivo no existe, retornar lista vacía
		}
		return historias;
	}
	
	public static void guardarHistorias(ArrayList<HistoriaMedica> historias) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH8, false)))) {
			for (HistoriaMedica h : historias) {
				writer.println(h.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	private static Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) return null;
		try {
			return new Date(dateFormat.parse(dateStr).getTime());
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Vivienda obtenerViviendaPorId(String idVivienda) {
		if (idVivienda == null || idVivienda.isEmpty()) return null;
		ArrayList<Vivienda> viviendas = leerViviendas();
		for (Vivienda v : viviendas) {
			if (v.getIdVivienda().equals(idVivienda)) {
				return v;
			}
		}
		return null;
	}
	
	public static Paciente obtenerPacientePorId(String idPaciente) {
		if (idPaciente == null || idPaciente.isEmpty()) return null;
		ArrayList<Paciente> pacientes = leerPacientes();
		for (Paciente p : pacientes) {
			if (p.getIdPaciente().equals(idPaciente)) {
				return p;
			}
		}
		return null;
	}
	
	public static Medico obtenerMedicoPorId(String idMedico) {
		if (idMedico == null || idMedico.isEmpty()) return null;
		ArrayList<Medico> medicos = leerMedicos();
		for (Medico m : medicos) {
			if (m.getIdMedico().equals(idMedico)) {
				return m;
			}
		}
		return null;
	}
	
	public static Vacuna obtenerVacunaPorId(String idVacuna) {
		if (idVacuna == null || idVacuna.isEmpty()) return null;
		ArrayList<Vacuna> vacunas = leerVacunas();
		for (Vacuna v : vacunas) {
			if (v.getIdVacuna().equals(idVacuna)) {
				return v;
			}
		}
		return null;
	}
	
	public static Enfermedad obtenerEnfermedadPorId(String idEnfermedad) {
		if (idEnfermedad == null || idEnfermedad.isEmpty()) return null;
		ArrayList<Enfermedad> enfermedades = leerEnfermedades();
		for (Enfermedad e : enfermedades) {
			if (e.getIdEnfermedad().equals(idEnfermedad)) {
				return e;
			}
		}
		return null;
	}
	
	public static Consultas obtenerConsultaPorId(String idConsulta) {
		if (idConsulta == null || idConsulta.isEmpty()) return null;
		ArrayList<Consultas> consultas = leerConsultas();
		for (Consultas c : consultas) {
			if (c.getIdConsulta().equals(idConsulta)) {
				return c;
			}
		}
		return null;
	}
}