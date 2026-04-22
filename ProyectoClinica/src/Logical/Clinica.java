package Logical;

import java.util.ArrayList;
import java.util.List;

public class Clinica {
    
    // TODAS las listas de la aplicación
    private ArrayList<Medico> misMedico;
    private ArrayList<Paciente> misPaciente;
    private ArrayList<Usuario> misUsuarios;
    private ArrayList<Vivienda> misViviendas;
    private ArrayList<Vacuna> misVacunas;
    private ArrayList<Enfermedad> misEnfermedades;
    private ArrayList<Cita> misCitas;
    private ArrayList<Consultas> misConsultas;
    
    private static Clinica clinica = null;

    // Generadores de códigos automáticos
    public static int generadorCodigoidMedico = 1;
    public static int generadorCodigoPaciente = 1;
    public static int generadorCodigoVivienda = 1;
    public static int generadorCodigoUser = 1;
    public static int generadorCodigoVacuna = 1;
    public static int generadorCodigoEnfermedad = 1;
    public static int generadorCodigoCita = 1;
    public static int generadorCodigoConsulta = 1;
    
    private Clinica() {
        this.misMedico = new ArrayList<>();
        this.misPaciente = new ArrayList<>();
        this.misUsuarios = new ArrayList<>();
        this.misViviendas = new ArrayList<>();
        this.misVacunas = new ArrayList<>();
        this.misEnfermedades = new ArrayList<>();
        this.misCitas = new ArrayList<>();
        this.misConsultas = new ArrayList<>();
        cargarTodosLosDatos();
    }
    
    public static synchronized Clinica getInstance(){
        if(clinica == null){
            clinica = new Clinica();
        }		
        return clinica;
    }
    
    private void cargarTodosLosDatos() {
        // El orden es importante (Ej: Los pacientes necesitan que las viviendas ya estén cargadas)
        misUsuarios = ArchivoManager.leerUsuarios();
        misViviendas = ArchivoManager.leerViviendas();
        misMedico = ArchivoManager.leerMedicos(this);
        misPaciente = ArchivoManager.leerPacientes(this);
        misVacunas = ArchivoManager.leerVacunas(this);
        misEnfermedades = ArchivoManager.leerEnfermedades(this);
        misCitas = ArchivoManager.leerCitas(this);
        misConsultas = ArchivoManager.leerConsultas(this);
        
        actualizarGeneradores();
    }
    
    private void actualizarGeneradores() {
        // Lee el número de ID de los txt cargados para continuar la secuencia correctamente
        generadorCodigoUser = misUsuarios.stream().mapToInt(u -> extraerNumero(u.getIdUsuario())).max().orElse(0) + 1;
        generadorCodigoVivienda = misViviendas.stream().mapToInt(v -> extraerNumero(v.getIdVivienda())).max().orElse(0) + 1;
        generadorCodigoidMedico = misMedico.stream().mapToInt(m -> extraerNumero(m.getIdMedico())).max().orElse(0) + 1;
        generadorCodigoPaciente = misPaciente.stream().mapToInt(p -> extraerNumero(p.getIdPaciente())).max().orElse(0) + 1;
        generadorCodigoVacuna = misVacunas.stream().mapToInt(v -> extraerNumero(v.getIdVacuna())).max().orElse(0) + 1;
        generadorCodigoEnfermedad = misEnfermedades.stream().mapToInt(e -> extraerNumero(e.getIdEnfermedad())).max().orElse(0) + 1;
        generadorCodigoCita = misCitas.stream().mapToInt(c -> extraerNumero(c.getIdCita())).max().orElse(0) + 1;
        generadorCodigoConsulta = misConsultas.stream().mapToInt(c -> extraerNumero(c.getIdConsulta())).max().orElse(0) + 1;
    }

    private int extraerNumero(String id) {
        try { return Integer.parseInt(id.replaceAll("\\D+", "")); } 
        catch (Exception e) { return 0; }
    }
    
    // ================= CRUD EN MEMORIA Y GUARDADO =================
    
    // Usuarios
    public void agregarUsuario(Usuario u) { misUsuarios.add(u); ArchivoManager.guardarUsuarios(misUsuarios); }
    public ArrayList<Usuario> getMisUsuarios() { return misUsuarios; }
    public Usuario buscarUsuarioPorCodigo(String id) { return misUsuarios.stream().filter(u -> u.getIdUsuario().equals(id)).findFirst().orElse(null); }
    public void modificarUsuario(String id, Usuario u) { ArchivoManager.guardarUsuarios(misUsuarios); }
    public void borrarUsuario(String id) { misUsuarios.removeIf(u -> u.getIdUsuario().equals(id)); ArchivoManager.guardarUsuarios(misUsuarios); }

    // Viviendas
    public void agregarVivienda(Vivienda v) { misViviendas.add(v); ArchivoManager.guardarViviendas(misViviendas); }
    public ArrayList<Vivienda> getMisViviendas() { return misViviendas; }
    public Vivienda obtenervivienda(String id) { return misViviendas.stream().filter(v -> v.getIdVivienda().equals(id)).findFirst().orElse(null); }
    public void modificarVivienda(String id, Vivienda v) { ArchivoManager.guardarViviendas(misViviendas); }
    public void borrarVivienda(String id) { misViviendas.removeIf(v -> v.getIdVivienda().equals(id)); ArchivoManager.guardarViviendas(misViviendas); }

    // Médicos
    public void agregarMedico(Medico m) { misMedico.add(m); ArchivoManager.guardarMedicos(misMedico); }
    public ArrayList<Medico> getMisMedico() { return misMedico; }
    public Medico obtenerMedicoById(String id) { return misMedico.stream().filter(m -> m.getIdMedico().equals(id)).findFirst().orElse(null); }
    public Medico obtenerMedicoXnombre(String nombre) { return misMedico.stream().filter(m -> m.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null); }
    public void modificarMedico(String id, Medico m) { ArchivoManager.guardarMedicos(misMedico); }
    public void borrarMedico(String id) { misMedico.removeIf(m -> m.getIdMedico().equals(id)); ArchivoManager.guardarMedicos(misMedico); }

    // Pacientes
    public void agregarPaciente(Paciente p) { misPaciente.add(p); ArchivoManager.guardarPacientes(misPaciente); }
    public ArrayList<Paciente> getMisPaciente() { return misPaciente; }
    public Paciente obtenerPacienteById(String id) { return misPaciente.stream().filter(p -> p.getIdPaciente().equals(id)).findFirst().orElse(null); }
    public void modificarPaciente(String id, Paciente p) { ArchivoManager.guardarPacientes(misPaciente); }
    public void borrarPaciente(String cedula) { misPaciente.removeIf(p -> p.getCedula().equals(cedula)); ArchivoManager.guardarPacientes(misPaciente); }

    // Vacunas
    public void agregarVacuna(Vacuna v) { misVacunas.add(v); ArchivoManager.guardarVacunas(misVacunas); }
    public ArrayList<Vacuna> getMisVacunas() { return misVacunas; }
    public Vacuna obtenervacuna(String id) { return misVacunas.stream().filter(v -> v.getIdVacuna().equals(id)).findFirst().orElse(null); }
    public void modificarVacuna(String id, Vacuna v) { ArchivoManager.guardarVacunas(misVacunas); }
    public void borrarVacuna(String id) { misVacunas.removeIf(v -> v.getIdVacuna().equals(id)); ArchivoManager.guardarVacunas(misVacunas); }

    // Enfermedades
    public void agregarEnfermedad(Enfermedad e) { misEnfermedades.add(e); ArchivoManager.guardarEnfermedades(misEnfermedades); }
    public ArrayList<Enfermedad> getMisEnfermedades() { return misEnfermedades; }
    public Enfermedad obtenerEnfermedadById(String id) { return misEnfermedades.stream().filter(e -> e.getIdEnfermedad().equals(id)).findFirst().orElse(null); }
    public void modificarEnfermedad(String id, Enfermedad e) { ArchivoManager.guardarEnfermedades(misEnfermedades); }
    public void borrarEnfermedad(String id) { misEnfermedades.removeIf(e -> e.getIdEnfermedad().equals(id)); ArchivoManager.guardarEnfermedades(misEnfermedades); }

    // Citas
    public void agregarCita(Cita c) { misCitas.add(c); ArchivoManager.guardarCitas(misCitas); }
    public ArrayList<Cita> getMisCitas() { return misCitas; }
    public void modificarCita(String id, Cita c) { ArchivoManager.guardarCitas(misCitas); }
    public void borrarCita(String id) { misCitas.removeIf(c -> c.getIdCita().equals(id)); ArchivoManager.guardarCitas(misCitas); }

    // Consultas
    public void agregarConsulta(Consultas c) { misConsultas.add(c); ArchivoManager.guardarConsultas(misConsultas); }
    public ArrayList<Consultas> getMisConsultas() { return misConsultas; }
    public void modificarConsulta(String id, Consultas c) { ArchivoManager.guardarConsultas(misConsultas); }
    public void borrarConsulta(String id) { misConsultas.removeIf(c -> c.getIdConsulta().equals(id)); ArchivoManager.guardarConsultas(misConsultas); }
    
    // Método auxiliar histórico
    public List<Enfermedad> getEnfermedadesDiagnosticadas() {
        List<Enfermedad> enfermedadesDiagnosticadas = new ArrayList<>();
        for (Consultas consulta : misConsultas) {
            List<Enfermedad> enfConsulta = consulta.getSintomas();
            if (enfConsulta != null) enfermedadesDiagnosticadas.addAll(enfConsulta);
        }
        return enfermedadesDiagnosticadas;
    }
}