package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Usuario;

public class ListarConsulta extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;
    private JButton           btnVerDetalle;
    private JButton           btnModificar;
    private JButton           btnEliminar;
    private Consultas         selected = null;
    private Usuario           usuarioActual;

    public ListarConsulta(Usuario usuario) {
        this.usuarioActual = usuario;

        setTitle("Consultas Medicas Registradas");
        setBounds(100, 100, 980, 540);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setBounds(10, 75, 944, 390);
        contentPanel.add(listPanel);
        listPanel.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel(0, 5) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "ID Consulta", "Fecha", "Medico", "Paciente", "Diagnostico"});

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(SystemColor.activeCaption);
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(380);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnVerDetalle.setEnabled(true);
                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    String idConsulta = model.getValueAt(index, 0).toString();
                    ArrayList<Consultas> listaBuscar;
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            listaBuscar = medActual != null
                ? Clinica.getInstance().getConsultasPorMedico(medActual.getCedula())
                : new ArrayList<>();
        } else {
            listaBuscar = Clinica.getInstance().getMisConsultas();
        }
        for (Consultas c : listaBuscar) {
                        if (c.getIdConsulta().equals(idConsulta)) {
                            selected = c;
                            break;
                        }
                    }
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setBackground(SystemColor.scrollbar);
        opcionesPanel.setBounds(10, 11, 944, 59);
        contentPanel.add(opcionesPanel);
        opcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Listado de Consultas Medicas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setBounds(10, 3, 694, 14);
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
                ListarConsulta.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(904, 25, 38, 26);
        btnBuscar.addActionListener(e -> buscar());
        opcionesPanel.add(btnBuscar);

        // ── Botones ──────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Ver detalle con enfermedades
        btnVerDetalle = new JButton("Ver Enfermedades");
        btnVerDetalle.setEnabled(false);
        btnVerDetalle.addActionListener(e -> {
            if (selected != null) {
                List<Enfermedad> enfs =
                    Clinica.getInstance().obtenerEnfermedadesConsulta(
                        selected.getIdConsulta());
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
                // Cargar vacunas aplicadas
                java.util.List<Logical.Vacuna> vacs =
                    Clinica.getInstance().obtenerVacunasConsulta(
                        selected.getIdConsulta());
                StringBuilder sbVac = new StringBuilder();
                if (vacs == null || vacs.isEmpty()) {
                    sbVac.append("(ninguna)");
                } else {
                    for (Logical.Vacuna v : vacs)
                        sbVac.append("• ").append(v.getNombreVacuna()).append("\n");
                }

                JOptionPane.showMessageDialog(this,
                    "Consulta:    " + selected.getIdConsulta() + "\n"
                    + "Medico:      " + (selected.getDoctor() != null
                        ? selected.getDoctor().getNombre() : "(sin medico)") + "\n"
                    + "Paciente:    " + (selected.getPatient() != null
                        ? selected.getPatient().getNombre() : "(sin paciente)") + "\n"
                    + "Diagnostico: " + (selected.getDiagnostico() != null
                        ? selected.getDiagnostico() : "(sin diagnostico)") + "\n\n"
                    + "Enfermedades diagnosticadas:\n" + sb.toString() + "\n"
                    + "Vacunas aplicadas:\n" + sbVac.toString(),
                    "Detalle de Consulta", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPane.add(btnVerDetalle);

        // Modificar diagnostico
        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> {
            if (selected != null) {
                String diagActual = selected.getDiagnostico() != null
                    ? selected.getDiagnostico() : "";
                String nuevoDiag = JOptionPane.showInputDialog(this,
                    "Modificar diagnostico de la consulta "
                    + selected.getIdConsulta() + ":",
                    diagActual);
                if (nuevoDiag != null && !nuevoDiag.trim().isEmpty()) {
                    selected.setDiagnostico(nuevoDiag.trim());
                    Clinica.getInstance().modificarConsulta(
                        selected.getIdConsulta(), selected);
                    cargarDatos();
                    resetSeleccion();
                    JOptionPane.showMessageDialog(this,
                        "Consulta actualizada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                    Clinica.getInstance().actualizarGeneradores();
                }
            }
        });
        buttonPane.add(btnModificar);

        // Eliminar consulta
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.setBackground(new Color(200, 60, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            if (selected != null) {
                int conf = JOptionPane.showConfirmDialog(this,
                    "Seguro que desea eliminar la consulta "
                    + selected.getIdConsulta() + "?\n"
                    + "Esto tambien eliminara las enfermedades asociadas.",
                    "Confirmacion", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (conf == JOptionPane.YES_OPTION) {
                    Clinica.getInstance().borrarConsulta(
                        selected.getIdConsulta());
                    cargarDatos();
                    resetSeleccion();
                    JOptionPane.showMessageDialog(this,
                        "Consulta eliminada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                    Clinica.getInstance().actualizarGeneradores();
                }
            }
        });
        buttonPane.add(btnEliminar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        cargarDatos();
    }

    private void cargarDatos() {
        ArrayList<Consultas> lista;
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            lista = medActual != null
                ? Clinica.getInstance().getConsultasPorMedico(medActual.getCedula())
                : new ArrayList<>();
        } else {
            lista = Clinica.getInstance().getMisConsultas();
        }
        model.setRowCount(0);
        for (Consultas c : lista) {
            String medico   = c.getDoctor()  != null
                ? c.getDoctor().getNombre()  : "(sin medico)";
            String paciente = c.getPatient() != null
                ? c.getPatient().getNombre() : "(sin paciente)";
            String fecha    = c.getFechaConsulta() != null
                ? c.getFechaConsulta().toString() : "";
            String diag     = c.getDiagnostico() != null
                ? c.getDiagnostico() : "";
            model.addRow(new Object[]{
                c.getIdConsulta(), fecha, medico, paciente, diag});
        }
        System.out.println("Consultas cargadas: " + lista.size());
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        ArrayList<Consultas> listaBuscar;
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            listaBuscar = medActual != null
                ? Clinica.getInstance().getConsultasPorMedico(medActual.getCedula())
                : new ArrayList<>();
        } else {
            listaBuscar = Clinica.getInstance().getMisConsultas();
        }
        for (Consultas c : listaBuscar) {
            String medico   = c.getDoctor()  != null
                ? c.getDoctor().getNombre().toLowerCase()  : "";
            String paciente = c.getPatient() != null
                ? c.getPatient().getNombre().toLowerCase() : "";
            String diag     = c.getDiagnostico() != null
                ? c.getDiagnostico().toLowerCase() : "";
            if (medico.contains(texto) || paciente.contains(texto)
                    || diag.contains(texto)
                    || c.getIdConsulta().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    c.getIdConsulta(),
                    c.getFechaConsulta() != null
                        ? c.getFechaConsulta().toString() : "",
                    c.getDoctor()  != null ? c.getDoctor().getNombre()  : "",
                    c.getPatient() != null ? c.getPatient().getNombre() : "",
                    c.getDiagnostico() != null ? c.getDiagnostico() : ""
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron consultas con ese criterio",
                "Busqueda", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        }
    }

    private void resetSeleccion() {
        selected = null;
        btnVerDetalle.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}