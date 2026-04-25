package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Cita;
import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Medico;
import Logical.Paciente;
import Logical.Usuario;
import Logical.Vacuna;
import Logical.Examen;

public class HacerConsulta extends JDialog {

    private final JPanel      contentPanel = new JPanel();

    private JTextField        txtCodigo;
    private JTextField        txtCedulaMed;
    private JTextField        txtCedulaPac;
    private JTextField        txtDiagnostico;

    private JComboBox<String> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<String> comboAnio;

    // Tablas enfermedades
    private DefaultTableModel modeloEnfDisp;
    private DefaultTableModel modeloEnfSel;
    private JTable            tablaEnfDisp;
    private JTable            tablaEnfSel;
    private JButton           btnAgregarEnf;
    private JButton           btnQuitarEnf;

    // Tablas vacunas
    private DefaultTableModel modeloVacDisp;
    private DefaultTableModel modeloVacSel;
    private JTable            tablaVacDisp;
    private JTable            tablaVacSel;
    private JButton           btnAgregarVac;
    private JButton           btnQuitarVac;

    private ArrayList<Enfermedad> enfermedadesSelected = new ArrayList<Enfermedad>();
    private ArrayList<Vacuna>     vacunasSelected       = new ArrayList<Vacuna>();
    private ArrayList<Examen>    examenesOrdenados     = new ArrayList<Examen>();

    // Tabla examenes
    private DefaultTableModel    modeloExamen;
    private JTable               tablaExamen;
    private Usuario               usuarioActual;
    private Paciente              pacienteSeleccionado  = null;
    private boolean               vieneDeUnaCita        = false;
    private Cita                  citaOrigen            = null; // cita que originó esta consulta

    private static final String[] MESES = {
        "(mes)", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public HacerConsulta(Usuario user) { this(user, null); }

    public HacerConsulta(Usuario user, Cita cita) {
        this.usuarioActual = user;
        vieneDeUnaCita     = (cita != null);
        citaOrigen         = cita;

        setTitle("Registrar Consulta Medica");
        setBounds(100, 100, 750, 620);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.info);
        contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ══════════════════════════════════════════════════════════
        // PANEL DATOS GENERALES
        // ══════════════════════════════════════════════════════════
        JPanel pnlDatos = new JPanel();
        pnlDatos.setBackground(SystemColor.info);
        pnlDatos.setBorder(new TitledBorder("Datos de la consulta"));
        pnlDatos.setBounds(8, 8, 720, 145);
        pnlDatos.setLayout(null);
        contentPanel.add(pnlDatos);

        // Codigo
        addLabel(pnlDatos, "Codigo:", 10, 28);
        txtCodigo = new JTextField("CON-" + Clinica.generadorCodigoConsulta);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(new Color(230, 240, 255));
        txtCodigo.setForeground(new Color(30, 60, 120));
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtCodigo.setBounds(80, 25, 110, 22);
        pnlDatos.add(txtCodigo);

        // Fecha
        addLabel(pnlDatos, "Fecha:", 205, 28);
        String[] dias = new String[32];
        dias[0] = "(dia)";
        for (int i = 1; i <= 31; i++) dias[i] = String.valueOf(i);
        comboDia = new JComboBox<String>(dias);
        comboDia.setBounds(250, 25, 60, 22);
        pnlDatos.add(comboDia);

        comboMes = new JComboBox<String>(MESES);
        comboMes.setBounds(315, 25, 110, 22);
        pnlDatos.add(comboMes);

        int anioActual = LocalDate.now().getYear();
        String[] anios = new String[anioActual - 2019];
        anios[0] = "(anio)";
        for (int i = 1; i < anios.length; i++)
            anios[i] = String.valueOf(anioActual - i + 1);
        comboAnio = new JComboBox<String>(anios);
        comboAnio.setBounds(430, 25, 75, 22);
        pnlDatos.add(comboAnio);

        // Fecha de hoy por defecto
        LocalDate hoy = LocalDate.now();
        comboDia.setSelectedIndex(hoy.getDayOfMonth());
        comboMes.setSelectedIndex(hoy.getMonthValue());
        for (int i = 0; i < comboAnio.getItemCount(); i++) {
            if (String.valueOf(hoy.getYear()).equals(comboAnio.getItemAt(i))) {
                comboAnio.setSelectedIndex(i); break;
            }
        }

        // Cedula medico
        addLabel(pnlDatos, "Cedula medico:", 10, 62);
        txtCedulaMed = new JTextField();
        txtCedulaMed.setBounds(110, 59, 140, 22);
        pnlDatos.add(txtCedulaMed);

        if (usuarioActual != null && usuarioActual.esMedico()) {
            Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            if (medActual != null) {
                txtCedulaMed.setText(medActual.getCedula());
                txtCedulaMed.setEnabled(false);
                txtCedulaMed.setBackground(new Color(230, 240, 255));
            }
        }

        JButton btnVerMed = new JButton("Verificar");
        btnVerMed.setBounds(258, 59, 80, 22);
        btnVerMed.addActionListener(e -> verificarMedico());
        pnlDatos.add(btnVerMed);

        // Paciente
        addLabel(pnlDatos, "Paciente:", 10, 98);
        txtCedulaPac = new JTextField();
        txtCedulaPac.setEditable(false);
        txtCedulaPac.setBackground(new Color(230, 240, 255));
        txtCedulaPac.setForeground(new Color(30, 60, 120));
        txtCedulaPac.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtCedulaPac.setBounds(80, 95, 180, 22);
        pnlDatos.add(txtCedulaPac);

        JButton btnSelPac = new JButton("Seleccionar...");
        btnSelPac.setBounds(268, 95, 110, 22);
        btnSelPac.addActionListener(e -> abrirSelectorPaciente());
        // Si viene de una cita el paciente ya está asignado — ocultar botón
        btnSelPac.setVisible(!vieneDeUnaCita);
        pnlDatos.add(btnSelPac);

        // Diagnostico
        addLabel(pnlDatos, "Diagnostico:", 395, 62);
        txtDiagnostico = new JTextField();
        txtDiagnostico.setBounds(480, 59, 230, 22);
        pnlDatos.add(txtDiagnostico);

        // ══════════════════════════════════════════════════════════
        // PESTAÑAS: ENFERMEDADES | VACUNAS
        // ══════════════════════════════════════════════════════════
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBounds(8, 160, 720, 340);
        contentPanel.add(tabs);

        // ── Pestaña Enfermedades ─────────────────────────────────
        JPanel pnlEnf = new JPanel(null);
        pnlEnf.setBackground(SystemColor.info);
        tabs.addTab("  Enfermedades diagnosticadas  ", pnlEnf);

        addLabel(pnlEnf, "Disponibles:", 10, 15);
        modeloEnfDisp = new DefaultTableModel(0, 3);
        modeloEnfDisp.setColumnIdentifiers(new String[]{"ID", "Nombre", "Gravedad"});
        tablaEnfDisp = new JTable(modeloEnfDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEnfDisp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEnfDisp.setRowHeight(24);
        tablaEnfDisp.getTableHeader().setReorderingAllowed(false);
        tablaEnfDisp.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaEnfDisp.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaEnfDisp.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaEnfDisp.getTableHeader().setBackground(SystemColor.activeCaption);
        JScrollPane spEnfDisp = new JScrollPane(tablaEnfDisp);
        spEnfDisp.setBounds(10, 35, 300, 250);
        pnlEnf.add(spEnfDisp);

        btnAgregarEnf = new JButton("Agregar >");
        btnAgregarEnf.setEnabled(false);
        btnAgregarEnf.setBackground(new Color(70, 130, 180));
        btnAgregarEnf.setForeground(Color.WHITE);
        btnAgregarEnf.setFocusPainted(false);
        btnAgregarEnf.setBounds(320, 120, 100, 30);
        btnAgregarEnf.addActionListener(e -> agregarEnfermedad());
        pnlEnf.add(btnAgregarEnf);

        btnQuitarEnf = new JButton("< Quitar");
        btnQuitarEnf.setEnabled(false);
        btnQuitarEnf.setBounds(320, 160, 100, 30);
        btnQuitarEnf.addActionListener(e -> quitarEnfermedad());
        pnlEnf.add(btnQuitarEnf);

        addLabel(pnlEnf, "Diagnosticadas:", 430, 15);
        modeloEnfSel = new DefaultTableModel(0, 2);
        modeloEnfSel.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        tablaEnfSel = new JTable(modeloEnfSel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEnfSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEnfSel.setRowHeight(24);
        tablaEnfSel.getTableHeader().setReorderingAllowed(false);
        tablaEnfSel.getTableHeader().setBackground(SystemColor.activeCaption);
        JScrollPane spEnfSel = new JScrollPane(tablaEnfSel);
        spEnfSel.setBounds(430, 35, 270, 250);
        pnlEnf.add(spEnfSel);

        tablaEnfDisp.getSelectionModel().addListSelectionListener(
            e -> btnAgregarEnf.setEnabled(tablaEnfDisp.getSelectedRow() >= 0));
        tablaEnfSel.getSelectionModel().addListSelectionListener(
            e -> btnQuitarEnf.setEnabled(tablaEnfSel.getSelectedRow() >= 0));

        // ── Pestaña Vacunas ──────────────────────────────────────
        JPanel pnlVac = new JPanel(null);
        pnlVac.setBackground(SystemColor.info);
        tabs.addTab("  Vacunas aplicadas  ", pnlVac);

        addLabel(pnlVac, "Disponibles:", 10, 15);
        modeloVacDisp = new DefaultTableModel(0, 3);
        modeloVacDisp.setColumnIdentifiers(new String[]{"ID", "Nombre", "Descripcion"});
        tablaVacDisp = new JTable(modeloVacDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaVacDisp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVacDisp.setRowHeight(24);
        tablaVacDisp.getTableHeader().setReorderingAllowed(false);
        tablaVacDisp.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaVacDisp.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaVacDisp.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaVacDisp.getTableHeader().setBackground(SystemColor.activeCaption);
        JScrollPane spVacDisp = new JScrollPane(tablaVacDisp);
        spVacDisp.setBounds(10, 35, 300, 250);
        pnlVac.add(spVacDisp);

        btnAgregarVac = new JButton("Agregar >");
        btnAgregarVac.setEnabled(false);
        btnAgregarVac.setBackground(new Color(60, 160, 80));
        btnAgregarVac.setForeground(Color.WHITE);
        btnAgregarVac.setFocusPainted(false);
        btnAgregarVac.setBounds(320, 120, 100, 30);
        btnAgregarVac.addActionListener(e -> agregarVacuna());
        pnlVac.add(btnAgregarVac);

        btnQuitarVac = new JButton("< Quitar");
        btnQuitarVac.setEnabled(false);
        btnQuitarVac.setBounds(320, 160, 100, 30);
        btnQuitarVac.addActionListener(e -> quitarVacuna());
        pnlVac.add(btnQuitarVac);

        addLabel(pnlVac, "Aplicadas:", 430, 15);
        modeloVacSel = new DefaultTableModel(0, 2);
        modeloVacSel.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        tablaVacSel = new JTable(modeloVacSel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaVacSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVacSel.setRowHeight(24);
        tablaVacSel.getTableHeader().setReorderingAllowed(false);
        tablaVacSel.getTableHeader().setBackground(SystemColor.activeCaption);
        JScrollPane spVacSel = new JScrollPane(tablaVacSel);
        spVacSel.setBounds(430, 35, 270, 250);
        pnlVac.add(spVacSel);

        tablaVacDisp.getSelectionModel().addListSelectionListener(
            e -> btnAgregarVac.setEnabled(tablaVacDisp.getSelectedRow() >= 0));
        tablaVacSel.getSelectionModel().addListSelectionListener(
            e -> btnQuitarVac.setEnabled(tablaVacSel.getSelectedRow() >= 0));

        // ── Pestaña Examenes — catálogo N:M ─────────────────────
        JPanel pnlExam = new JPanel(null);
        pnlExam.setBackground(SystemColor.info);
        tabs.addTab("  Examenes ordenados  ", pnlExam);

        // Tabla catálogo disponibles
        JLabel lblExamDisp = new JLabel("Catalogo de examenes:");
        lblExamDisp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblExamDisp.setBounds(10, 15, 200, 20);
        pnlExam.add(lblExamDisp);

        DefaultTableModel modeloExamDisp = new DefaultTableModel(0, 2);
        modeloExamDisp.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        JTable tablaExamDisp = new JTable(modeloExamDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaExamDisp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaExamDisp.setRowHeight(24);
        tablaExamDisp.getTableHeader().setReorderingAllowed(false);
        tablaExamDisp.getTableHeader().setBackground(SystemColor.activeCaption);
        tablaExamDisp.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaExamDisp.getColumnModel().getColumn(1).setPreferredWidth(200);
        JScrollPane spExamDisp = new JScrollPane(tablaExamDisp);
        spExamDisp.setBounds(10, 38, 280, 250);
        pnlExam.add(spExamDisp);

        // Botones centro
        JButton btnAgregarExam = new JButton("Ordenar >");
        btnAgregarExam.setEnabled(false);
        btnAgregarExam.setBackground(new Color(70, 130, 180));
        btnAgregarExam.setForeground(Color.WHITE);
        btnAgregarExam.setFocusPainted(false);
        btnAgregarExam.setBounds(300, 120, 100, 30);
        pnlExam.add(btnAgregarExam);

        JButton btnQuitarExam = new JButton("< Quitar");
        btnQuitarExam.setEnabled(false);
        btnQuitarExam.setBounds(300, 160, 100, 30);
        pnlExam.add(btnQuitarExam);

        // Tabla examenes ordenados
        JLabel lblExamSel = new JLabel("Ordenados en esta consulta:");
        lblExamSel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblExamSel.setBounds(410, 15, 250, 20);
        pnlExam.add(lblExamSel);

        modeloExamen = new DefaultTableModel(0, 3);
        modeloExamen.setColumnIdentifiers(new String[]{"ID", "Nombre", "Descripcion"});
        tablaExamen = new JTable(modeloExamen) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaExamen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaExamen.setRowHeight(24);
        tablaExamen.getTableHeader().setReorderingAllowed(false);
        tablaExamen.getTableHeader().setBackground(SystemColor.activeCaption);
        tablaExamen.getColumnModel().getColumn(0).setPreferredWidth(55);
        tablaExamen.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaExamen.getColumnModel().getColumn(2).setPreferredWidth(120);
        JScrollPane spExamSel = new JScrollPane(tablaExamen);
        spExamSel.setBounds(410, 38, 295, 250);
        pnlExam.add(spExamSel);

        // Cargar catálogo
        for (Examen ex : Clinica.getInstance().getMisExamenes())
            modeloExamDisp.addRow(new Object[]{ex.getIdExamen(), ex.getNombreExamen()});

        // Listeners
        tablaExamDisp.getSelectionModel().addListSelectionListener(
            e -> btnAgregarExam.setEnabled(tablaExamDisp.getSelectedRow() >= 0));
        tablaExamen.getSelectionModel().addListSelectionListener(
            e -> btnQuitarExam.setEnabled(tablaExamen.getSelectedRow() >= 0));

        btnAgregarExam.addActionListener(e -> {
            int fila = tablaExamDisp.getSelectedRow();
            if (fila < 0) return;
            String idEx = (String) modeloExamDisp.getValueAt(fila, 0);
            Examen ex = Clinica.getInstance().obtenerExamenById(idEx);
            if (ex != null && !examenesOrdenados.contains(ex)) {
                examenesOrdenados.add(ex);
                modeloExamen.addRow(new Object[]{
                    ex.getIdExamen(), ex.getNombreExamen(),
                    ex.getDescripcion() != null ? ex.getDescripcion() : ""});
            }
        });

        btnQuitarExam.addActionListener(e -> {
            int fila = tablaExamen.getSelectedRow();
            if (fila < 0) return;
            examenesOrdenados.remove(fila);
            modeloExamen.removeRow(fila);
            btnQuitarExam.setEnabled(false);
        });

        // ══════════════════════════════════════════════════════════
        // BOTONES
        // ══════════════════════════════════════════════════════════
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton("Registrar Consulta");
        btnRegistrar.setBackground(new Color(70, 130, 180));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrar());
        buttonPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        // Precargar desde cita si viene de una
        if (cita != null) {
            if (cita.getDoc() != null) {
                txtCedulaMed.setText(cita.getDoc().getCedula());
                txtCedulaMed.setEnabled(false);
                txtCedulaMed.setBackground(new Color(230, 240, 255));
            }
            if (cita.getPaciente() != null) {
                pacienteSeleccionado = cita.getPaciente();
                txtCedulaPac.setText(cita.getPaciente().getCedula()
                    + "  -  " + cita.getPaciente().getNombre());
            }
            comboDia.setSelectedIndex(LocalDate.now().getDayOfMonth());
            comboMes.setSelectedIndex(LocalDate.now().getMonthValue());
        }

        cargarTablas();
    }

    private void addLabel(JPanel p, String txt, int x, int y) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setBounds(x, y, 130, 20);
        p.add(lbl);
    }

    private void cargarTablas() {
        // Enfermedades
        modeloEnfDisp.setRowCount(0);
        for (Enfermedad e : Clinica.getInstance().getMisEnfermedades()) {
            String grav = e.getGravedad() != null
                ? e.getGravedad().getGravedad() : "(sin clasificar)";
            modeloEnfDisp.addRow(new Object[]{
                e.getIdEnfermedad(), e.getNombreEnfermedad(), grav});
        }
        // Vacunas
        modeloVacDisp.setRowCount(0);
        for (Vacuna v : Clinica.getInstance().getMisVacunas()) {
            modeloVacDisp.addRow(new Object[]{
                v.getIdVacuna(), v.getNombreVacuna(),
                v.getDescripcion() != null ? v.getDescripcion() : ""});
        }
    }

    private void agregarEnfermedad() {
        int fila = tablaEnfDisp.getSelectedRow();
        if (fila < 0) return;
        String idEnf = (String) modeloEnfDisp.getValueAt(fila, 0);
        Enfermedad enf = Clinica.getInstance().obtenerEnfermedadById(idEnf);
        if (enf != null && !enfermedadesSelected.contains(enf)) {
            enfermedadesSelected.add(enf);
            modeloEnfSel.addRow(new Object[]{
                enf.getIdEnfermedad(), enf.getNombreEnfermedad()});
        }
    }

    private void quitarEnfermedad() {
        int fila = tablaEnfSel.getSelectedRow();
        if (fila < 0) return;
        enfermedadesSelected.remove(fila);
        modeloEnfSel.removeRow(fila);
        btnQuitarEnf.setEnabled(false);
    }

    private void agregarVacuna() {
        int fila = tablaVacDisp.getSelectedRow();
        if (fila < 0) return;
        String idVac = (String) modeloVacDisp.getValueAt(fila, 0);
        Vacuna vac = Clinica.getInstance().obtenerVacunaById(idVac);
        if (vac != null && !vacunasSelected.contains(vac)) {
            vacunasSelected.add(vac);
            modeloVacSel.addRow(new Object[]{
                vac.getIdVacuna(), vac.getNombreVacuna()});
        }
    }

    private void quitarVacuna() {
        int fila = tablaVacSel.getSelectedRow();
        if (fila < 0) return;
        vacunasSelected.remove(fila);
        modeloVacSel.removeRow(fila);
        btnQuitarVac.setEnabled(false);
    }

    private void verificarMedico() {
        String ced = txtCedulaMed.getText().trim();
        if (ced.isEmpty()) return;
        Medico m = Clinica.getInstance().obtenerMedicoById(ced);
        if (m == null)
            JOptionPane.showMessageDialog(this,
                "Medico no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(this,
                "Medico: " + m.getNombre() + " — " + m.getEspecialidad(),
                "Verificado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void registrar() {
        if (usuarioActual != null && usuarioActual.esMedico() && !vieneDeUnaCita) {
            JOptionPane.showMessageDialog(this,
                "Para registrar una consulta primero debe\n"
                + "marcar una cita como completada desde\n"
                + "UTILIDADES → Listado de Citas.",
                "Accion no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedulaMed   = txtCedulaMed.getText().trim();
        String cedulaPac   = txtCedulaPac.getText().trim();
        String diagnostico = txtDiagnostico.getText().trim();

        if (cedulaMed.isEmpty() || cedulaPac.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleccione el medico y el paciente",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Medico medico = Clinica.getInstance().obtenerMedicoById(cedulaMed);
        Paciente paciente = (pacienteSeleccionado != null)
            ? pacienteSeleccionado
            : Clinica.getInstance().obtenerPacienteById(cedulaPac);

        if (medico == null) {
            JOptionPane.showMessageDialog(this,
                "Medico no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (paciente == null) {
            JOptionPane.showMessageDialog(this,
                "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fecha = obtenerFecha();
        Consultas nueva = new Consultas(
            txtCodigo.getText(), fecha,
            enfermedadesSelected, medico, paciente);
        nueva.setDiagnostico(diagnostico);

        // Pasar vacunas seleccionadas
        ArrayList<String> idsVacunas = new ArrayList<String>();
        for (Vacuna v : vacunasSelected)
            idsVacunas.add(v.getIdVacuna());
        nueva.setVacunasAplicadas(idsVacunas);

        // Vincular la cita si viene de una
        if (citaOrigen != null)
            nueva.setIdCita(citaOrigen.getIdCita());

        Clinica.getInstance().agregarConsulta(nueva);

        // Vincular examenes del catálogo a esta consulta via CONSULTA_EXAMEN
        for (Examen ex : examenesOrdenados) {
            Clinica.getInstance().vincularExamenConsulta(
                nueva.getIdConsulta(), ex.getIdExamen());
        }

        // Marcar la cita como completada SOLO si la consulta se registró bien
        if (citaOrigen != null) {
            citaOrigen.setCompletada(true);
            Clinica.getInstance().modificarCita(
                citaOrigen.getIdCita(), citaOrigen);
        }

        limpiar();

        // Construir resumen de enfermedades
        StringBuilder resEnf = new StringBuilder();
        if (enfermedadesSelected.isEmpty()) {
            resEnf.append("   (ninguna)");
        } else {
            for (Enfermedad ef : enfermedadesSelected)
                resEnf.append("   • ").append(ef.getNombreEnfermedad()).append("\n");
        }
        // Construir resumen de vacunas
        StringBuilder resVac = new StringBuilder();
        if (vacunasSelected.isEmpty()) {
            resVac.append("   (ninguna)");
        } else {
            for (Vacuna v : vacunasSelected)
                resVac.append("   • ").append(v.getNombreVacuna()).append("\n");
        }

        JOptionPane.showMessageDialog(this,
            "Consulta registrada correctamente\n\n"
            + "Medico:   " + (medico != null ? medico.getNombre() : "") + "\n"
            + "Paciente: " + (paciente != null ? paciente.getNombre() : "") + "\n"
            + "Fecha:    " + obtenerFecha().toString() + "\n\n"
            + "Enfermedades diagnosticadas (" + enfermedadesSelected.size() + "):\n"
            + resEnf.toString() + "\n"
            + "Vacunas aplicadas (" + vacunasSelected.size() + "):\n"
            + resVac.toString() + "\n"
            + "Examenes ordenados: " + examenesOrdenados.size(),
            "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private Date obtenerFecha() {
        try {
            int dia = comboDia.getSelectedIndex();
            int mes = comboMes.getSelectedIndex();
            String anioStr = (String) comboAnio.getSelectedItem();
            if (dia == 0 || mes == 0 || anioStr.equals("(anio)"))
                return new Date();
            return Date.from(LocalDate.of(Integer.parseInt(anioStr), mes, dia)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) { return new Date(); }
    }

    private void abrirSelectorPaciente() {
        ArrayList<Paciente> lista = Clinica.getInstance().getMisPaciente();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay pacientes registrados",
                "Sin pacientes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        javax.swing.JDialog dlg = new javax.swing.JDialog(
            this, "Seleccionar Paciente", true);
        dlg.setSize(580, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JLabel lbl = new JLabel("  Selecciona un paciente:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(new EmptyBorder(8, 4, 4, 4));
        dlg.add(lbl, BorderLayout.NORTH);

        DefaultTableModel mdl = new DefaultTableModel(0, 4);
        mdl.setColumnIdentifiers(
            new String[]{"Cedula", "Nombre", "Tipo Sangre", "ID Paciente"});
        for (Paciente p : lista)
            mdl.addRow(new Object[]{p.getCedula(), p.getNombre(),
                p.getTipoSangre() != null ? p.getTipoSangre() : "",
                p.getIdPaciente()});

        JTable tbl = new JTable(mdl) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setRowHeight(24);
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tbl.getSelectedRow() >= 0)
                    seleccionarPaciente(tbl, lista, dlg);
            }
        });
        dlg.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSel = new JButton("Seleccionar");
        btnSel.addActionListener(ev -> seleccionarPaciente(tbl, lista, dlg));
        pnl.add(btnSel);
        JButton btnCan = new JButton("Cancelar");
        btnCan.addActionListener(ev -> dlg.dispose());
        pnl.add(btnCan);
        dlg.add(pnl, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void seleccionarPaciente(JTable tbl,
            ArrayList<Paciente> lista, javax.swing.JDialog dlg) {
        int fila = tbl.getSelectedRow();
        if (fila < 0) return;
        pacienteSeleccionado = lista.get(fila);
        txtCedulaPac.setText(pacienteSeleccionado.getCedula()
            + "  -  " + pacienteSeleccionado.getNombre());
        dlg.dispose();
    }

    private void limpiar() {
        enfermedadesSelected.clear();
        vacunasSelected.clear();
        modeloEnfSel.setRowCount(0);
        modeloVacSel.setRowCount(0);
        txtDiagnostico.setText("");
        txtCedulaPac.setText("");
        pacienteSeleccionado = null;
        Clinica.generadorCodigoConsulta++;
        txtCodigo.setText("CON-" + Clinica.generadorCodigoConsulta);
        if (usuarioActual == null || !usuarioActual.esMedico())
            txtCedulaMed.setText("");
        btnAgregarEnf.setEnabled(false);
        btnQuitarEnf.setEnabled(false);
        btnAgregarVac.setEnabled(false);
        btnQuitarVac.setEnabled(false);
        examenesOrdenados.clear();
        if (modeloExamen != null) modeloExamen.setRowCount(0);
        comboDia.setSelectedIndex(LocalDate.now().getDayOfMonth());
        comboMes.setSelectedIndex(LocalDate.now().getMonthValue());
    }
}