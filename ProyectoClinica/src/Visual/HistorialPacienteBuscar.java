package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Paciente;

public class HistorialPacienteBuscar extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTextField        txtBuscar;
    private DefaultTableModel modelPaciente;
    private DefaultTableModel modelConsultas;
    private JTable            tablaPaciente;
    private JTable            tablaConsultas;
    private JButton           btnVerDetalle;
    private Paciente          pacienteSelected  = null;
    private Consultas         consultaSelected  = null;

    public HistorialPacienteBuscar() {
        setTitle("Historial Medico de Pacientes");
        setBounds(100, 100, 980, 580);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setBackground(SystemColor.scrollbar);
        opcionesPanel.setBounds(10, 11, 944, 59);
        contentPanel.add(opcionesPanel);
        opcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Buscar paciente para ver su historial medico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setBounds(10, 3, 700, 14);
        opcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 45, 14);
        opcionesPanel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(58, 28, 836, 20);
        opcionesPanel.add(txtBuscar);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                HistorialPacienteBuscar.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(904, 25, 38, 26);
        btnBuscar.addActionListener(e -> buscarPaciente());
        opcionesPanel.add(btnBuscar);

        // ── Panel izquierdo: lista de pacientes ──────────────────
        JPanel pnlPacientes = new JPanel(new BorderLayout());
        pnlPacientes.setBorder(new TitledBorder("Pacientes"));
        pnlPacientes.setBackground(SystemColor.window);
        pnlPacientes.setBounds(10, 75, 300, 410);
        contentPanel.add(pnlPacientes);

        modelPaciente = new DefaultTableModel(0, 3) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelPaciente.setColumnIdentifiers(
            new String[]{"ID", "Nombre", "Cedula"});

        tablaPaciente = new JTable(modelPaciente);
        tablaPaciente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPaciente.setRowHeight(24);
        tablaPaciente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPaciente.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        tablaPaciente.getTableHeader().setBackground(SystemColor.activeCaption);
        tablaPaciente.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaPaciente.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablaPaciente.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaPaciente.getTableHeader().setReorderingAllowed(false);

        tablaPaciente.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int idx = tablaPaciente.getSelectedRow();
                if (idx >= 0) {
                    String id = modelPaciente.getValueAt(idx, 0).toString();
                    pacienteSelected = Clinica.getInstance().obtenerPacienteById(id);
                    consultaSelected = null;
                    btnVerDetalle.setEnabled(false);
                    cargarConsultas();
                }
            }
        });
        pnlPacientes.add(new JScrollPane(tablaPaciente), BorderLayout.CENTER);

        // ── Panel derecho: historial del paciente ────────────────
        JPanel pnlHistorial = new JPanel(new BorderLayout());
        pnlHistorial.setBorder(new TitledBorder("Historial Medico"));
        pnlHistorial.setBackground(SystemColor.window);
        pnlHistorial.setBounds(320, 75, 634, 410);
        contentPanel.add(pnlHistorial);

        // Info paciente en la parte superior
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInfo.setBackground(new Color(235, 245, 255));
        pnlInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
            SystemColor.activeCaption));

        JLabel lblInfoPac = new JLabel(
            "Selecciona un paciente para ver su historial");
        lblInfoPac.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfoPac.setForeground(Color.GRAY);
        pnlInfo.add(lblInfoPac);
        pnlHistorial.add(pnlInfo, BorderLayout.NORTH);

        // Tabla de consultas
        modelConsultas = new DefaultTableModel(0, 4) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelConsultas.setColumnIdentifiers(
            new String[]{"ID Consulta", "Fecha", "Medico", "Diagnostico"});

        tablaConsultas = new JTable(modelConsultas);
        tablaConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConsultas.setRowHeight(24);
        tablaConsultas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaConsultas.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        tablaConsultas.getTableHeader().setBackground(SystemColor.activeCaption);
        tablaConsultas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaConsultas.getColumnModel().getColumn(1).setPreferredWidth(90);
        tablaConsultas.getColumnModel().getColumn(2).setPreferredWidth(140);
        tablaConsultas.getColumnModel().getColumn(3).setPreferredWidth(280);
        tablaConsultas.getTableHeader().setReorderingAllowed(false);

        tablaConsultas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int idx = tablaConsultas.getSelectedRow();
                if (idx >= 0) {
                    String idCon = modelConsultas.getValueAt(idx, 0).toString();
                    for (Consultas c : Clinica.getInstance().getMisConsultas()) {
                        if (c.getIdConsulta().equals(idCon)) {
                            consultaSelected = c;
                            break;
                        }
                    }
                    btnVerDetalle.setEnabled(true);
                }
            }
        });

        JScrollPane spConsultas = new JScrollPane(tablaConsultas);
        spConsultas.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlHistorial.add(spConsultas, BorderLayout.CENTER);

        // ── Botones ──────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnVerDetalle = new JButton("Ver Enfermedades");
        btnVerDetalle.setEnabled(false);
        btnVerDetalle.setBackground(new Color(70, 130, 180));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.addActionListener(e -> {
            if (consultaSelected != null) {
                List<Enfermedad> enfs = Clinica.getInstance()
                    .obtenerEnfermedadesConsulta(consultaSelected.getIdConsulta());
                StringBuilder sb = new StringBuilder();
                if (enfs == null || enfs.isEmpty()) {
                    sb.append("(ninguna registrada)");
                } else {
                    for (Enfermedad ef : enfs)
                        sb.append("• ").append(ef.getNombreEnfermedad())
                          .append(ef.getGravedad() != null
                              ? " [" + ef.getGravedad().getGravedad() + "]" : "")
                          .append("\n");
                }
                JOptionPane.showMessageDialog(this,
                    "Paciente:    " + (pacienteSelected != null
                        ? pacienteSelected.getNombre() : "") + "\n"
                    + "Consulta:    " + consultaSelected.getIdConsulta() + "\n"
                    + "Medico:      " + (consultaSelected.getDoctor() != null
                        ? consultaSelected.getDoctor().getNombre() : "(sin medico)") + "\n"
                    + "Fecha:       " + (consultaSelected.getFechaConsulta() != null
                        ? consultaSelected.getFechaConsulta().toString() : "") + "\n"
                    + "Diagnostico: " + (consultaSelected.getDiagnostico() != null
                        ? consultaSelected.getDiagnostico() : "(sin diagnostico)") + "\n\n"
                    + "Enfermedades diagnosticadas:\n" + sb.toString(),
                    "Detalle de Consulta", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPane.add(btnVerDetalle);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        cargarPacientes();
    }

    private void cargarPacientes() {
        ArrayList<Paciente> lista = Clinica.getInstance().getMisPaciente();
        modelPaciente.setRowCount(0);
        for (Paciente p : lista)
            modelPaciente.addRow(new Object[]{
                p.getIdPaciente(), p.getNombre(), p.getCedula()});
    }

    private void buscarPaciente() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modelPaciente.setRowCount(0);
        modelConsultas.setRowCount(0);
        pacienteSelected = null;
        consultaSelected = null;
        btnVerDetalle.setEnabled(false);
        boolean encontrado = false;
        for (Paciente p : Clinica.getInstance().getMisPaciente()) {
            if (p.getNombre().toLowerCase().contains(texto)
                    || p.getCedula().contains(texto)
                    || p.getIdPaciente().toLowerCase().contains(texto)) {
                modelPaciente.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getCedula()});
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron pacientes con ese criterio",
                "Busqueda", JOptionPane.INFORMATION_MESSAGE);
            cargarPacientes();
        }
    }

    private void cargarConsultas() {
        modelConsultas.setRowCount(0);
        if (pacienteSelected == null) return;
        ArrayList<Consultas> lista = Clinica.getInstance().getMisConsultas();
        boolean tieneConsultas = false;
        for (Consultas c : lista) {
            if (c.getPatient() != null
                    && c.getPatient().getCedula()
                        .equals(pacienteSelected.getCedula())) {
                modelConsultas.addRow(new Object[]{
                    c.getIdConsulta(),
                    c.getFechaConsulta() != null
                        ? c.getFechaConsulta().toString() : "",
                    c.getDoctor() != null
                        ? c.getDoctor().getNombre() : "(sin medico)",
                    c.getDiagnostico() != null
                        ? c.getDiagnostico() : "(sin diagnostico)"
                });
                tieneConsultas = true;
            }
        }
        if (!tieneConsultas)
            JOptionPane.showMessageDialog(this,
                "El paciente " + pacienteSelected.getNombre()
                + " no tiene consultas registradas.",
                "Sin historial", JOptionPane.INFORMATION_MESSAGE);
    }
}