package Logical;

import java.util.ArrayList;
import java.util.List;

public class Clinica {

    private final MedicoDAO            medicoDAO     = new MedicoDAO();
    private final PacienteDAO          pacienteDAO   = new PacienteDAO();
    private final CitaDAO              citaDAO       = new CitaDAO();
    private final ConsultaDAO          consultaDAO   = new ConsultaDAO();
    private final VacunaDAO            vacunaDAO     = new VacunaDAO();
    private final EnfermedadDAO        enfermedadDAO = new EnfermedadDAO();
    private final ViviendaDAO          viviendaDAO   = new ViviendaDAO();
    private final UsuarioDAO           usuarioDAO    = new UsuarioDAO();
    private final HistorialDAO         historialDAO  = new HistorialDAO();
    private final ExamenDAO            examenDAO     = new ExamenDAO();
    private final GravedadenfermedadDAO gravedadDAO  = new GravedadenfermedadDAO();

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

    private void actualizarGeneradores() {
        generadorCodigoidMedico   = medicoDAO.obtenerMaxNumero("MEDICO",      "Id_Medico",      "MED-")      + 1;
        generadorCodigoPaciente   = pacienteDAO.obtenerMaxNumero("PACIENTE",  "Id_Paciente",    "PAC-")      + 1;
        generadorCodigoCita       = citaDAO.obtenerMaxNumero("CITA",          "Id_Cita",        "CITA-")     + 1;
        generadorCodigoConsulta   = consultaDAO.obtenerMaxNumero("CONSULTA",  "Id_Consulta",    "CON-")      + 1;
        generadorCodigoUser       = usuarioDAO.obtenerMaxNumero("USUARIO",    "Id_Usuario",     "USR-")      + 1;
        generadorCodigoVacuna     = vacunaDAO.obtenerMaxNumero("VACUNA",      "Id_Vacuna",      "VAC-")      + 1;
        generadorCodigoVivienda   = viviendaDAO.obtenerMaxNumero("VIVIENDA",  "Id_Vivienda",    "VIV-")      + 1;
        generadorCodigoEnfermedad = enfermedadDAO.obtenerMaxNumero("ENFERMEDAD","Id_Enfermedad","ENF-")      + 1;
        generadorCodigoExamen     = examenDAO.obtenerMaxNumero("EXAMEN",      "Id_Examen",      "EXA-")      + 1;
    }

    // ── MÉDICO ──────────────────────────────────────────────────
    public void agregarMedico(Medico medico) {
        medicoDAO.insertar(medico);
        generadorCodigoidMedico++;
    }
    public void modificarMedico(String cedula, Medico medico) { medicoDAO.actualizar(medico); }
    public void borrarMedico(String cedula)                    { medicoDAO.eliminar(cedula); }
    public ArrayList<Medico> getMisMedico()                    { return medicoDAO.listarTodos(); }
    public Medico obtenerMedicoById(String id)                 { return medicoDAO.buscarPorId(id); }
    public Medico obtenerMedicoXnombre(String nombre)          { return medicoDAO.buscarPorNombre(nombre); }

    // ── PACIENTE ─────────────────────────────────────────────────
    public void agregarPaciente(Paciente paciente) {
        pacienteDAO.insertar(paciente);
        generadorCodigoPaciente++;
        String idHistorial = "HIST-" + paciente.getIdPaciente();
        historialDAO.insertar(idHistorial, paciente.getCedula());
    }
    public void modificarPaciente(String cedula, Paciente paciente) { pacienteDAO.actualizar(paciente); }
    public void borrarPaciente(String cedula)                        { pacienteDAO.eliminar(cedula); }
    public ArrayList<Paciente> getMisPaciente()                      { return pacienteDAO.listarTodos(); }
    public Paciente obtenerPacienteById(String id)                   { return pacienteDAO.buscarPorId(id); }
    public Paciente obtenerPacienteXnombre(String nombre)            { return pacienteDAO.buscarPorNombre(nombre); }

    // ── VACUNA ───────────────────────────────────────────────────
    public void agregarVacuna(Vacuna vacuna) { vacunaDAO.insertar(vacuna); generadorCodigoVacuna++; }
    public void modificarVacuna(String id, Vacuna v) { vacunaDAO.actualizar(v); }
    public void borrarVacuna(String id)              { vacunaDAO.eliminar(id); }
    public ArrayList<Vacuna> getMisVacunas()         { return vacunaDAO.listarTodos(); }
    public Vacuna obtenervacuna(String id)           { return vacunaDAO.buscarPorId(id); }

    // ── ENFERMEDAD ───────────────────────────────────────────────
    public void agregarEnfermedad(Enfermedad e) { enfermedadDAO.insertar(e); generadorCodigoEnfermedad++; }
    public void modificarEnfermedad(String id, Enfermedad e) { enfermedadDAO.actualizar(e); }
    public void borrarEnfermedad(String id)                  { enfermedadDAO.eliminar(id); }
    public ArrayList<Enfermedad> getMisEnfermedades()        { return enfermedadDAO.listarTodos(); }
    public Enfermedad obtenerEnfermedadById(String id)       { return enfermedadDAO.buscarPorId(id); }

    // ── VIVIENDA ─────────────────────────────────────────────────
    public void agregarVivienda(Vivienda v) { viviendaDAO.insertar(v); generadorCodigoVivienda++; }
    public void modificarVivienda(String id, Vivienda v) { viviendaDAO.actualizar(v); }
    public void borrarVivienda(String id)                { viviendaDAO.eliminar(id); }
    public ArrayList<Vivienda> getMisViviendas()         { return viviendaDAO.listarTodos(); }
    public Vivienda obtenervivienda(String id)           { return viviendaDAO.buscarPorId(id); }

    // ── CITA ─────────────────────────────────────────────────────
    public void agregarCita(Cita cita) { citaDAO.insertar(cita); generadorCodigoCita++; }
    public void modificarCita(String id, Cita c) { citaDAO.actualizar(c); }
    public void borrarCita(String id)             { citaDAO.eliminar(id); }
    public ArrayList<Cita> getMisCitas()          { return citaDAO.listarTodos(); }

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
    public void insertarConsulta(Consultas c) { agregarConsulta(c); }
    public void modificarConsulta(String id, Consultas c) { consultaDAO.actualizar(c); }
    public void borrarConsulta(String id)                 { consultaDAO.eliminar(id); }
    public ArrayList<Consultas> getMisConsultas()         { return consultaDAO.listarTodos(); }

    // ── EXAMEN ───────────────────────────────────────────────────
    public void agregarExamen(Examen examen) { examenDAO.insertar(examen); generadorCodigoExamen++; }
    public void modificarExamen(String id, Examen e) { examenDAO.actualizar(e); }
    public void borrarExamen(String id)               { examenDAO.eliminar(id); }
    public ArrayList<Examen> getMisExamenes()         { return examenDAO.listarTodos(); }
    public ArrayList<Examen> getExamenesDe(String idConsulta) {
        return examenDAO.listarPorConsulta(idConsulta);
    }
    public Examen obtenerExamenById(String id) { return examenDAO.buscarPorId(id); }

    // ── USUARIO ──────────────────────────────────────────────────
    public void agregarUsuario(Usuario u) { usuarioDAO.insertar(u); generadorCodigoUser++; }
    public void modificarUsuario(String id, Usuario u) { usuarioDAO.actualizar(u); }
    public void borrarUsuario(String id)               { usuarioDAO.eliminar(id); }
    public ArrayList<Usuario> getMisUsuarios()         { return usuarioDAO.listarTodos(); }
    public Usuario buscarUsuarioPorCodigo(String id)   { return usuarioDAO.buscarPorId(id); }
    public Usuario autenticarUsuario(String user, String pass) {
        return usuarioDAO.autenticar(user, pass);
    }

    // ── GRAVEDAD ─────────────────────────────────────────────────
    public ArrayList<Gravedadenfermedad> getMisGravedades() { return gravedadDAO.listarTodos(); }
    public void agregarGravedad(Gravedadenfermedad g)       { gravedadDAO.insertar(g); }

    // ── AUXILIARES ───────────────────────────────────────────────
    // Usado por CrearUser para generar ADM-N, MED-N, SEC-N
    public int obtenerMaxUsuarioPorPrefijo(String prefijo) {
        return usuarioDAO.obtenerMaxNumero("USUARIO", "Id_Usuario", prefijo);
    }

    public void asignarPacienteMedico(String paciente) {}
    public List<Enfermedad> getEnfermedadesDiagnosticadas() {
        return consultaDAO.listarEnfermedadesDiagnosticadas();
    }
}