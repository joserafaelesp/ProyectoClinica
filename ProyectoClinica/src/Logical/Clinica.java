package Logical;

import java.util.ArrayList;
import java.util.List;

public class Clinica {

    private final MedicoDAO              medicoDAO     = new MedicoDAO();
    private final PacienteDAO            pacienteDAO   = new PacienteDAO();
    private final CitaDAO                citaDAO       = new CitaDAO();
    private final ConsultaDAO            consultaDAO   = new ConsultaDAO();
    private final VacunaDAO              vacunaDAO     = new VacunaDAO();
    private final EnfermedadDAO          enfermedadDAO = new EnfermedadDAO();
    private final ViviendaDAO            viviendaDAO   = new ViviendaDAO();
    private final UsuarioDAO             usuarioDAO    = new UsuarioDAO();
    private final HistorialDAO           historialDAO  = new HistorialDAO();
    private final ExamenDAO              examenDAO     = new ExamenDAO();
    private final GravedadenfermedadDAO  gravedadDAO   = new GravedadenfermedadDAO();

    private static Clinica clinica = null;

    public static int generadorCodigoCita       = 1;
    public static int generadorCodigoConsulta   = 1;
    public static int generadorCodigoEnfermedad = 1;
    public static int generadorCodigoidMedico   = 1;
    public static int generadorCodigoVacuna     = 1;
    public static int generadorCodigoVivienda   = 1;
    public static int generadorCodigoUser       = 1;
    public static int generadorCodigoPaciente   = 1;
    public static int generadorCodigoExamen     = 1;

    private Clinica() { actualizarGeneradores(); }

    public static Clinica getInstance() {
        if (clinica == null) clinica = new Clinica();
        return clinica;
    }

    public void actualizarGeneradores() {
        int maxMedico = medicoDAO.obtenerMaxNumero("MEDICO",   "Id_Medico",  "MED-");
        int maxUsuMed = usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", "MED-");
        generadorCodigoidMedico   = Math.max(maxMedico, maxUsuMed) + 1;
        generadorCodigoPaciente   = pacienteDAO.obtenerMaxNumero("PACIENTE",    "Id_Paciente",  "PAC-")  + 1;
        generadorCodigoCita       = citaDAO.obtenerMaxNumero("CITA",            "Id_Cita",      "CITA-") + 1;
        generadorCodigoConsulta   = consultaDAO.obtenerMaxNumero("CONSULTA",    "Id_Consulta",  "CON-")  + 1;
        int maxAdm = usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", "ADM-");
        int maxMed = usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", "MED-");
        int maxSec = usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", "SEC-");
        generadorCodigoUser       = Math.max(Math.max(maxAdm, maxMed), maxSec) + 1;
        generadorCodigoVacuna     = vacunaDAO.obtenerMaxNumero("VACUNA",        "Id_Vacuna",    "VAC-")  + 1;
        generadorCodigoVivienda   = viviendaDAO.obtenerMaxNumero("VIVIENDA",    "Id_Vivienda",  "VIV-")  + 1;
        generadorCodigoEnfermedad = enfermedadDAO.obtenerMaxNumero("ENFERMEDAD","Id_Enfermedad","ENF-")  + 1;
        generadorCodigoExamen     = examenDAO.obtenerMaxNumero("EXAMEN",        "Id_Examen",    "EXA-")  + 1;
    }

    // ── MEDICO ───────────────────────────────────────────────────
    public void agregarMedico(Medico medico)                     { medicoDAO.insertar(medico); generadorCodigoidMedico++; }
    public void modificarMedico(String cedula, Medico medico)    { medicoDAO.actualizar(medico); }
    public void borrarMedico(String cedula)                      { medicoDAO.eliminar(cedula); }
    public ArrayList<Medico> getMisMedico()                      { return medicoDAO.listarTodos(); }
    public Medico obtenerMedicoById(String id)                   { return medicoDAO.buscarPorId(id); }
    public Medico obtenerMedicoXnombre(String nombre)            { return medicoDAO.buscarPorNombre(nombre); }

    // ── PACIENTE ─────────────────────────────────────────────────
    public void agregarPaciente(Paciente paciente) {
        pacienteDAO.insertar(paciente);
        generadorCodigoPaciente++;
        historialDAO.insertar("HIST-" + paciente.getIdPaciente(), paciente.getCedula());
    }
    public void modificarPaciente(String cedula, Paciente p)     { pacienteDAO.actualizar(p); }
    public void borrarPaciente(String cedula)                    { pacienteDAO.eliminar(cedula); }
    public ArrayList<Paciente> getMisPaciente()                  { return pacienteDAO.listarTodos(); }
    public Paciente obtenerPacienteById(String id)               { return pacienteDAO.buscarPorId(id); }
    public Paciente obtenerPacienteXnombre(String nombre)        { return pacienteDAO.buscarPorNombre(nombre); }

    // ── VACUNA ───────────────────────────────────────────────────
    public void agregarVacuna(Vacuna v)                          { vacunaDAO.insertar(v); generadorCodigoVacuna++; }
    public void modificarVacuna(String id, Vacuna v)             { vacunaDAO.actualizar(v); }
    public void borrarVacuna(String id)                          { vacunaDAO.eliminar(id); }
    public ArrayList<Vacuna> getMisVacunas()                     { return vacunaDAO.listarTodos(); }
    public Vacuna obtenerVacunaById(String id)                   { return vacunaDAO.buscarPorId(id); }
    public Vacuna obtenervacuna(String id)                       { return vacunaDAO.buscarPorId(id); }

    // ── ENFERMEDAD ───────────────────────────────────────────────
    public void agregarEnfermedad(Enfermedad e)                  { enfermedadDAO.insertar(e); generadorCodigoEnfermedad++; }
    public void modificarEnfermedad(String id, Enfermedad e)     { enfermedadDAO.actualizar(e); }
    public void borrarEnfermedad(String id)                      { enfermedadDAO.eliminar(id); }
    public ArrayList<Enfermedad> getMisEnfermedades()            { return enfermedadDAO.listarTodos(); }
    public Enfermedad obtenerEnfermedadById(String id)           { return enfermedadDAO.buscarPorId(id); }

    // ── VIVIENDA ─────────────────────────────────────────────────
    public void agregarVivienda(Vivienda v)                      { viviendaDAO.insertar(v); generadorCodigoVivienda++; }
    public void modificarVivienda(String id, Vivienda v)         { viviendaDAO.actualizar(v); }
    public void borrarVivienda(String id)                        { viviendaDAO.eliminar(id); }
    public ArrayList<Vivienda> getMisViviendas()                 { return viviendaDAO.listarTodos(); }
    public Vivienda obtenervivienda(String id)                   { return viviendaDAO.buscarPorId(id); }

    // ── CITA ─────────────────────────────────────────────────────
    public void agregarCita(Cita cita)                           { citaDAO.insertar(cita); generadorCodigoCita++; }
    public void modificarCita(String id, Cita c)                 { citaDAO.actualizar(c); }
    public void borrarCita(String id)                            { citaDAO.eliminar(id); }
    public Cita obtenerCitaById(String id)                       { return citaDAO.buscarPorId(id); }
    public ArrayList<Cita> getMisCitas()                         { return citaDAO.listarTodos(); }
    public ArrayList<Cita> getCitasPorMedico(String cedula)      { return citaDAO.listarPorMedico(cedula); }

    // ── CONSULTA ─────────────────────────────────────────────────
    public void agregarConsulta(Consultas consulta) {
        String idHistorial = historialDAO.buscarPorPaciente(
            consulta.getPatient() != null ? consulta.getPatient().getCedula() : null);
        List<String> idsEnfs = new ArrayList<>();
        if (consulta.getEnfermedades() != null)
            for (Enfermedad e : consulta.getEnfermedades())
                idsEnfs.add(e.getIdEnfermedad());
        consultaDAO.insertarConEnfermedades(consulta, idsEnfs, idHistorial);
        generadorCodigoConsulta++;
    }
    public void insertarConsulta(Consultas c)                    { agregarConsulta(c); }
    public void modificarConsulta(String id, Consultas c)        { consultaDAO.actualizar(c); }
    public void borrarConsulta(String id)                        { consultaDAO.eliminar(id); }
    public ArrayList<Consultas> getMisConsultas()                { return consultaDAO.listarTodos(); }
    public ArrayList<Consultas> getConsultasPorMedico(String ced){ return consultaDAO.listarPorMedico(ced); }
    public List<Enfermedad> obtenerEnfermedadesConsulta(String id){ return consultaDAO.leerEnfermedades(id); }
    public List<Vacuna> obtenerVacunasConsulta(String id)         { return consultaDAO.leerVacunas(id); }

    // ── EXAMEN ───────────────────────────────────────────────────
    public void agregarExamen(Examen examen)                     { examenDAO.insertar(examen); generadorCodigoExamen++; }
    public void modificarExamen(String id, Examen e)             { examenDAO.actualizar(e); }
    public void borrarExamen(String id)                          { examenDAO.eliminar(id); }
    public ArrayList<Examen> getMisExamenes()                    { return examenDAO.listarTodos(); }
    public List<Examen> getExamenesDe(String idConsulta)         { return examenDAO.listarPorConsulta(idConsulta); }
    public Examen obtenerExamenById(String id)                   { return examenDAO.buscarPorId(id); }
    public boolean vincularExamenConsulta(String idCon, String idEx){ return examenDAO.vincularAConsulta(idCon, idEx); }

    // ── USUARIO ──────────────────────────────────────────────────
    public void agregarUsuario(Usuario u)                        { usuarioDAO.insertar(u); generadorCodigoUser++; }
    public void modificarUsuario(String id, Usuario u)           { usuarioDAO.actualizar(u); }
    public void borrarUsuario(String id)                         { usuarioDAO.eliminar(id); }
    public ArrayList<Usuario> getMisUsuarios()                   { return usuarioDAO.listarTodos(); }
    public Usuario buscarUsuarioPorCodigo(String id)             { return usuarioDAO.buscarPorId(id); }
    public Usuario autenticarUsuario(String user, String pass)   { return usuarioDAO.autenticar(user, pass); }

    // ── GRAVEDAD ─────────────────────────────────────────────────
    public ArrayList<Gravedadenfermedad> getMisGravedades()      { return gravedadDAO.listarTodos(); }
    public void agregarGravedad(Gravedadenfermedad g)            { gravedadDAO.insertar(g); }

    // ── AUXILIARES ───────────────────────────────────────────────
    public int obtenerMaxUsuarioPorPrefijo(String prefijo) {
        return usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", prefijo);
    }
    public void asignarPacienteMedico(String paciente) {}
    public List<Enfermedad> getEnfermedadesDiagnosticadas() {
        return consultaDAO.listarEnfermedadesDiagnosticadas();
    }
 // ── Agrega estos dos métodos al final de la sección CONSULTA en Clinica.java ──

 // Devuelve todas las consultas de un paciente por su cédula
 public ArrayList<Consultas> getMisConsultasDelPaciente(String cedula) {
     return consultaDAO.listarPorPaciente(cedula);
 }

 // Devuelve todas las enfermedades diagnosticadas a un paciente (via sus consultas)
 public List<Enfermedad> obtenerEnfermedadesDePaciente(String cedula) {
     List<Enfermedad> todas = new ArrayList<>();
     ArrayList<Consultas> consultas = consultaDAO.listarPorPaciente(cedula);
     for (Consultas c : consultas) {
         List<Enfermedad> enfs = consultaDAO.leerEnfermedades(c.getIdConsulta());
         for (Enfermedad e : enfs) {
             // Evitar duplicados por si la misma enfermedad aparece en varias consultas
             boolean existe = todas.stream()
                 .anyMatch(ex -> ex.getIdEnfermedad().equals(e.getIdEnfermedad()));
             if (!existe) todas.add(e);
         }
     }
     return todas;
 }
}