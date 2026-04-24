package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Medico;
import Logical.Paciente;
import Logical.Usuario;

public class HacerConsulta extends JDialog {

    private final JPanel       contentPanel     = new JPanel();
    private JTextField         txtCedulaMed;
    private JTextField         txtCedulaPaciente;
    private JTextField         txtDiagnostico;
    private JSpinner           spnFecha;
    private JTextField         txtConsulta;
    private JTable             tableEnfermedades;
    private DefaultTableModel  modeloTabla;
    private JButton            btnAgregar;
    private ArrayList<Enfermedad> enfermedadesSelected;
    private JTextField         txtEnfermedad;
    private Enfermedad         consultEnfermedad = null;
    private Usuario            usuarioActual;

    public HacerConsulta(Usuario user) {
        this.usuarioActual    = user;
        this.enfermedadesSelected = new ArrayList<>();

        setTitle("Registrar Consulta Médica");
        setBounds(100, 100, 620, 520);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.info);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Código de consulta ───────────────────────────────────
        JLabel lblCodConsulta = new JLabel("Código Consulta:");
        lblCodConsulta.setBounds(10, 15, 110, 14);
        contentPanel.add(lblCodConsulta);

        txtConsulta = new JTextField("CON-" + Clinica.generadorCodigoConsulta);
        txtConsulta.setEnabled(false);
        txtConsulta.setBackground(new Color(230, 230, 230));
        txtConsulta.setBounds(125, 12, 120, 20);
        contentPanel.add(txtConsulta);

        // ── Cédula del Médico ────────────────────────────────────
        JLabel lblMed = new JLabel("Cédula Médico:");
        lblMed.setBounds(10, 45, 110, 14);
        contentPanel.add(lblMed);

        txtCedulaMed = new JTextField();
        txtCedulaMed.setBounds(125, 42, 120, 20);
        contentPanel.add(txtCedulaMed);

        // Si el usuario es médico, autocompleta su cédula
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            if (medActual != null) {
                txtCedulaMed.setText(medActual.getCedula());
                txtCedulaMed.setEnabled(false);
            }
        }

        // ── Cédula del Paciente ──────────────────────────────────
        JLabel lblPac = new JLabel("Cédula Paciente:");
        lblPac.setBounds(270, 45, 110, 14);
        contentPanel.add(lblPac);

        txtCedulaPaciente = new JTextField();
        txtCedulaPaciente.setBounds(385, 42, 120, 20);
        contentPanel.add(txtCedulaPaciente);

        // ── Fecha ────────────────────────────────────────────────
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(10, 75, 60, 14);
        contentPanel.add(lblFecha);

        spnFecha = new JSpinner();
        spnFecha.setBounds(125, 72, 120, 20);
        spnFecha.setModel(new SpinnerDateModel(
            new Date(), null, null, Calendar.DAY_OF_YEAR));
        contentPanel.add(spnFecha);

        // ── Diagnóstico ──────────────────────────────────────────
        JLabel lblDiag = new JLabel("Diagnóstico:");
        lblDiag.setBounds(270, 75, 90, 14);
        contentPanel.add(lblDiag);

        txtDiagnostico = new JTextField();
        txtDiagnostico.setBounds(365, 72, 220, 20);
        contentPanel.add(txtDiagnostico);

        // ── Tabla de enfermedades disponibles ────────────────────
        JPanel panelEnfs = new JPanel(new BorderLayout());
        panelEnfs.setBorder(new TitledBorder(
            UIManager.getBorder("TitledBorder.border"),
            "Enfermedades disponibles",
            TitledBorder.LEADING, TitledBorder.TOP,
            null, Color.BLACK));
        panelEnfs.setBounds(10, 110, 250, 220);
        contentPanel.add(panelEnfs);

        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Código"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableEnfermedades = new JTable(modeloTabla);
        tableEnfermedades.getTableHeader().setReorderingAllowed(false);
        panelEnfs.add(new JScrollPane(tableEnfermedades), BorderLayout.CENTER);

        // ── Enfermedades seleccionadas ───────────────────────────
        JPanel panelSel = new JPanel(new BorderLayout());
        panelSel.setBorder(new TitledBorder(
            UIManager.getBorder("TitledBorder.border"),
            "Enfermedades en consulta",
            TitledBorder.LEADING, TitledBorder.TOP,
            null, Color.BLACK));
        panelSel.setBounds(360, 110, 220, 220);
        contentPanel.add(panelSel);

        DefaultTableModel modeloSel = new DefaultTableModel(
            new String[]{"Nombre", "Código"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaSel = new JTable(modeloSel);
        panelSel.add(new JScrollPane(tablaSel), BorderLayout.CENTER);

        // ── Botón Agregar ────────────────────────────────────────
        btnAgregar = new JButton("Agregar >");
        btnAgregar.setEnabled(false);
        btnAgregar.setBounds(268, 200, 88, 23);
        contentPanel.add(btnAgregar);

        btnAgregar.addActionListener(e -> {
            int fila = tableEnfermedades.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this,
                    "Seleccione una enfermedad de la tabla",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Obtener enfermedad seleccionada de la tabla
            String idEnf = (String) modeloTabla.getValueAt(fila, 1);
            Enfermedad enf = Clinica.getInstance().obtenerEnfermedadById(idEnf);
            if (enf != null && !enfermedadesSelected.contains(enf)) {
                enfermedadesSelected.add(enf);
                modeloSel.addRow(new Object[]{
                    enf.getNombreEnfermedad(), enf.getIdEnfermedad()});
            }
        });

        // Habilitar botón al seleccionar fila
        tableEnfermedades.getSelectionModel().addListSelectionListener(
            e -> btnAgregar.setEnabled(tableEnfermedades.getSelectedRow() >= 0));

        // ── Buscar enfermedad por ID ─────────────────────────────
        JLabel lblBuscar = new JLabel("Buscar por ID:");
        lblBuscar.setBounds(10, 340, 90, 14);
        contentPanel.add(lblBuscar);

        txtEnfermedad = new JTextField();
        txtEnfermedad.setBounds(105, 337, 100, 20);
        contentPanel.add(txtEnfermedad);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(215, 336, 80, 23);
        btnBuscar.addActionListener(e -> {
            String idBuscar = txtEnfermedad.getText().trim();
            if (idBuscar.isEmpty()) return;
            consultEnfermedad = Clinica.getInstance().obtenerEnfermedadById(idBuscar);
            if (consultEnfermedad != null) {
                // Seleccionar en la tabla
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if (modeloTabla.getValueAt(i, 1).equals(idBuscar)) {
                        tableEnfermedades.setRowSelectionInterval(i, i);
                        break;
                    }
                }
                btnAgregar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Enfermedad no encontrada", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPanel.add(btnBuscar);

        // ── Botones finales ──────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.info);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton("Registrar Consulta");
        btnRegistrar.setBackground(new Color(70, 130, 180));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> {
            String cedulaMed = txtCedulaMed.getText().trim();
            String cedulaPac = txtCedulaPaciente.getText().trim();
            String diagnostico = txtDiagnostico.getText().trim();

            if (cedulaMed.isEmpty() || cedulaPac.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Complete la cédula del médico y del paciente",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar médico y paciente por cédula
            Medico   medico   = Clinica.getInstance().obtenerMedicoById(cedulaMed);
            Paciente paciente = Clinica.getInstance().obtenerPacienteById(cedulaPac);

            if (medico == null) {
                JOptionPane.showMessageDialog(this,
                    "Médico no encontrado con esa cédula",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (paciente == null) {
                JOptionPane.showMessageDialog(this,
                    "Paciente no encontrado con esa cédula",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear consulta
            Consultas nuevaConsulta = new Consultas(
                txtConsulta.getText(),
                (Date) spnFecha.getValue(),
                enfermedadesSelected,
                medico,
                paciente);
            nuevaConsulta.setDiagnostico(diagnostico);

            Clinica.getInstance().agregarConsulta(nuevaConsulta);
            clean(modeloSel);

            JOptionPane.showMessageDialog(this,
                "Consulta registrada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        loadTable();
    }

    public void loadTable() {
        modeloTabla.setRowCount(0);
        for (Enfermedad e : Clinica.getInstance().getMisEnfermedades())
            modeloTabla.addRow(new Object[]{
                e.getNombreEnfermedad(), e.getIdEnfermedad()});
    }

    public void clean(DefaultTableModel modeloSel) {
        txtCedulaPaciente.setText("");
        txtEnfermedad.setText("");
        txtDiagnostico.setText("");
        enfermedadesSelected.clear();
        modeloSel.setRowCount(0);
        Clinica.generadorCodigoConsulta++;
        txtConsulta.setText("Consulta-" + Clinica.generadorCodigoConsulta);
        if (usuarioActual == null || !usuarioActual.esMedico())
            txtCedulaMed.setText("");
    }
}