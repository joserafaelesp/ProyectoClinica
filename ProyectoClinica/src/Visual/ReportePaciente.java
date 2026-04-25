package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Paciente;
import Logical.Vacuna;

public class ReportePaciente extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private JTextField txtIdPaciente;
    private JTextArea  txtDescripcion;
    private JTextField txtCedula;
    private JTable     tableEnfermedades;
    private DefaultTableModel model;  // FIX: ya no es static
    private Paciente   pacienteActual = null;

    public ReportePaciente() {
        setTitle("Reporte de Paciente");
        setBounds(100, 100, 750, 650);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(224, 255, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Busqueda por ID ──────────────────────────────────────
        JLabel lblIdPaciente = new JLabel("ID Paciente:");
        lblIdPaciente.setBounds(10, 26, 80, 14);
        contentPanel.add(lblIdPaciente);

        txtIdPaciente = new JTextField();
        txtIdPaciente.setBounds(95, 23, 150, 20);
        contentPanel.add(txtIdPaciente);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnBuscar.setBounds(255, 23, 80, 20);
        contentPanel.add(btnBuscar);

        // ── Datos del paciente ───────────────────────────────────
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 58, 80, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        txtNombre.setEnabled(false);
        txtNombre.setBounds(95, 55, 200, 20);
        contentPanel.add(txtNombre);

        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setBounds(10, 88, 60, 14);
        contentPanel.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setEditable(false);
        txtCedula.setEnabled(false);
        txtCedula.setBounds(95, 85, 200, 20);
        contentPanel.add(txtCedula);

        // ── Descripcion general ──────────────────────────────────
        JLabel lblDesc = new JLabel("Informacion del Paciente:");
        lblDesc.setBounds(10, 118, 160, 14);
        contentPanel.add(lblDesc);

        JScrollPane scrollDesc = new JScrollPane();
        scrollDesc.setBounds(10, 136, 710, 90);
        contentPanel.add(scrollDesc);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcion.setMargin(new Insets(5, 8, 5, 8));
        scrollDesc.setViewportView(txtDescripcion);

        // ── Tabla de enfermedades diagnosticadas al paciente ─────
        // FIX: antes mostraba el catalogo completo de enfermedades del sistema.
        // Ahora solo muestra las enfermedades diagnosticadas al paciente buscado.
        JLabel lblEnfermedades = new JLabel("Enfermedades diagnosticadas al paciente:");
        lblEnfermedades.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEnfermedades.setBounds(10, 240, 300, 14);
        contentPanel.add(lblEnfermedades);

        // FIX: tildes y caracteres especiales corregidos (eran C?digo, S?ntomas)
        String[] headersEnfermedades = {"Codigo", "Nombre", "Sintomas", "Gravedad"};
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        model.setColumnIdentifiers(headersEnfermedades);

        tableEnfermedades = new JTable(model);
        tableEnfermedades.getTableHeader().setReorderingAllowed(false);
        tableEnfermedades.setRowHeight(22);

        JScrollPane scrollEnf = new JScrollPane(tableEnfermedades);
        scrollEnf.setBounds(10, 260, 710, 120);
        contentPanel.add(scrollEnf);

        // ── Tabla de consultas del paciente ──────────────────────
        // NUEVO: muestra el historial de consultas del paciente
        JLabel lblConsultas = new JLabel("Historial de consultas:");
        lblConsultas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConsultas.setBounds(10, 392, 200, 14);
        contentPanel.add(lblConsultas);

        String[] headersConsultas = {"ID Consulta", "Fecha", "Medico", "Diagnostico"};
        DefaultTableModel modelConsultas = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        modelConsultas.setColumnIdentifiers(headersConsultas);

        JTable tableConsultas = new JTable(modelConsultas);
        tableConsultas.getTableHeader().setReorderingAllowed(false);
        tableConsultas.setRowHeight(22);

        JScrollPane scrollCon = new JScrollPane(tableConsultas);
        scrollCon.setBounds(10, 412, 710, 130);
        contentPanel.add(scrollCon);

        // ── Actualizar tablas al buscar paciente ─────────────────
        // Se inyectan las referencias para que buscarPaciente() las actualice
        this.tablaConsultasModel = modelConsultas;

        // ── Boton cerrar ─────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        JButton cancelButton = new JButton("Salir");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    // Referencia al modelo de consultas para actualizarlo desde buscarPaciente()
    private DefaultTableModel tablaConsultasModel;

    private void buscarPaciente() {
        String idPaciente = txtIdPaciente.getText().trim();
        if (idPaciente.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese un ID de paciente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pacienteActual = Clinica.getInstance().obtenerPacienteById(idPaciente);
        if (pacienteActual == null) {
            JOptionPane.showMessageDialog(this,
                "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
            return;
        }

        // ── Datos generales ──────────────────────────────────────
        txtNombre.setText(pacienteActual.getNombre());
        txtCedula.setText(pacienteActual.getCedula());

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre:          ").append(pacienteActual.getNombre()).append("\n");
        sb.append("Cedula:          ").append(pacienteActual.getCedula()).append("\n");
        sb.append("Telefono:        ").append(nullSafe(pacienteActual.getTelefono())).append("\n");
        sb.append("Genero:          ").append(nullSafe(pacienteActual.getGenero())).append("\n");
        sb.append("Tipo de Sangre:  ").append(nullSafe(pacienteActual.getTipoSangre())).append("\n");
        sb.append("Info Emergencia: ").append(nullSafe(pacienteActual.getInfoEmergencia())).append("\n");
        if (pacienteActual.getViviend() != null)
            sb.append("Vivienda:        ").append(pacienteActual.getViviend().getDireccion());
        txtDescripcion.setText(sb.toString());

        // ── FIX: enfermedades diagnosticadas AL PACIENTE ─────────
        // Se obtienen via las consultas del paciente → CONSULTA_ENFERMEDAD
        model.setRowCount(0);
        List<Enfermedad> enfsDelPaciente =
            Clinica.getInstance().obtenerEnfermedadesDePaciente(pacienteActual.getCedula());
        for (Enfermedad enf : enfsDelPaciente) {
            String gravedad = (enf.getGravedad() != null)
                ? enf.getGravedad().getGravedad() : "—";
            model.addRow(new Object[]{
                enf.getIdEnfermedad(),
                enf.getNombreEnfermedad(),
                nullSafe(enf.getSintomas()),
                gravedad
            });
        }

        // ── NUEVO: historial de consultas del paciente ───────────
        tablaConsultasModel.setRowCount(0);
        List<Consultas> consultas =
            Clinica.getInstance().getMisConsultasDelPaciente(pacienteActual.getCedula());
        for (Consultas c : consultas) {
            String nombreMedico = (c.getDoctor() != null) ? c.getDoctor().getNombre() : "—";
            tablaConsultasModel.addRow(new Object[]{
                c.getIdConsulta(),
                c.getFechaConsulta(),
                nombreMedico,
                nullSafe(c.getDiagnostico())
            });
        }

        if (enfsDelPaciente.isEmpty() && consultas.isEmpty())
            JOptionPane.showMessageDialog(this,
                "Paciente encontrado. Aun no tiene consultas registradas.",
                "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtDescripcion.setText("");
        model.setRowCount(0);
        if (tablaConsultasModel != null) tablaConsultasModel.setRowCount(0);
    }

    private String nullSafe(String valor) {
        return (valor != null && !valor.trim().isEmpty()) ? valor : "—";
    }
}