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
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Examen;
import Logical.Usuario;
import Logical.Vacuna;

public class ListarConsulta extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;
    private JButton           btnVerDetalle;
    private JButton           btnVerCompleto;
    private JButton           btnEliminar;
    private Consultas         selected = null;
    private Usuario           usuarioActual;

    public ListarConsulta(Usuario usuario) {
        this.usuarioActual = usuario;
        boolean esMedico = usuario != null && usuario.esMedico();

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
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
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
                    btnVerCompleto.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    String idConsulta = model.getValueAt(index, 0).toString();
                    for (Consultas c : obtenerLista()) {
                        if (c.getIdConsulta().equals(idConsulta)) {
                            selected = c; break;
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

        // Botón: Ver resumen rápido (enfermedades + vacunas)
        btnVerDetalle = new JButton("Ver Enfermedades");
        btnVerDetalle.setEnabled(false);
        btnVerDetalle.addActionListener(e -> {
            if (selected == null) return;
            List<Enfermedad> enfs = Clinica.getInstance()
                .obtenerEnfermedadesConsulta(selected.getIdConsulta());
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
            List<Vacuna> vacs = Clinica.getInstance()
                .obtenerVacunasConsulta(selected.getIdConsulta());
            StringBuilder sbVac = new StringBuilder();
            if (vacs == null || vacs.isEmpty()) {
                sbVac.append("(ninguna)");
            } else {
                for (Vacuna v : vacs)
                    sbVac.append("• ").append(v.getNombreVacuna()).append("\n");
            }
            JOptionPane.showMessageDialog(this,
                "Consulta:    " + selected.getIdConsulta() + "\n"
                + "Medico:      " + (selected.getDoctor()  != null
                    ? selected.getDoctor().getNombre()  : "(sin medico)") + "\n"
                + "Paciente:    " + (selected.getPatient() != null
                    ? selected.getPatient().getNombre() : "(sin paciente)") + "\n"
                + "Diagnostico: " + (selected.getDiagnostico() != null
                    ? selected.getDiagnostico() : "(sin diagnostico)") + "\n\n"
                + "Enfermedades diagnosticadas:\n" + sb + "\n"
                + "Vacunas aplicadas:\n" + sbVac,
                "Detalle de Consulta", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPane.add(btnVerDetalle);

        // FIX: "Ver Detalle Completo" reemplaza a "Modificar"
        // Muestra enfermedades + vacunas + exámenes en una ventana organizada
        btnVerCompleto = new JButton("Ver Detalle Completo");
        btnVerCompleto.setEnabled(false);
        btnVerCompleto.setBackground(new Color(70, 130, 180));
        btnVerCompleto.setForeground(Color.WHITE);
        btnVerCompleto.setFocusPainted(false);
        btnVerCompleto.addActionListener(e -> {
            if (selected == null) return;
            abrirDetalleCompleto(selected);
        });
        buttonPane.add(btnVerCompleto);

        // Eliminar — oculto para médico
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.setBackground(new Color(200, 60, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setVisible(!esMedico);
        btnEliminar.addActionListener(e -> {
            if (selected == null) return;
            int conf = JOptionPane.showConfirmDialog(this,
                "Seguro que desea eliminar la consulta "
                + selected.getIdConsulta() + "?\n"
                + "Esto tambien eliminara las enfermedades asociadas.",
                "Confirmacion", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                Clinica.getInstance().borrarConsulta(selected.getIdConsulta());
                cargarDatos();
                resetSeleccion();
                JOptionPane.showMessageDialog(this,
                    "Consulta eliminada correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                Clinica.getInstance().actualizarGeneradores();
            }
        });
        buttonPane.add(btnEliminar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        cargarDatos();
    }

    // ── Ventana de detalle completo ───────────────────────────────
    private void abrirDetalleCompleto(Consultas c) {
        JDialog dlg = new JDialog(this, "Detalle Completo — " + c.getIdConsulta(), true);
        dlg.setSize(700, 560);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(0, 8));

        // ── Encabezado reorganizado en dos columnas equilibradas ──
        JPanel pnlHeader = new JPanel(null);
        pnlHeader.setBackground(new Color(230, 240, 255));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
            new Color(180, 200, 230)));
        pnlHeader.setPreferredSize(new java.awt.Dimension(700, 120));

        // Columna izquierda: Consulta, Fecha, Medico, Paciente
        int lx = 16, vx = 110, gap = 24;
        addDetalleLbl(pnlHeader, "Consulta:",  lx, 12,       true);
        addDetalleLbl(pnlHeader, c.getIdConsulta(), vx, 12,  false);

        addDetalleLbl(pnlHeader, "Fecha:",     lx, 12+gap,   true);
        String fecha = c.getFechaConsulta() != null
            ? c.getFechaConsulta().toString() : "(sin fecha)";
        addDetalleLbl(pnlHeader, fecha,        vx, 12+gap,   false);

        addDetalleLbl(pnlHeader, "Medico:",    lx, 12+gap*2, true);
        addDetalleLbl(pnlHeader,
            c.getDoctor() != null ? c.getDoctor().getNombre() : "(sin medico)",
            vx, 12+gap*2, false);

        addDetalleLbl(pnlHeader, "Paciente:",  lx, 12+gap*3, true);
        addDetalleLbl(pnlHeader,
            c.getPatient() != null ? c.getPatient().getNombre() : "(sin paciente)",
            vx, 12+gap*3, false);

        // Columna derecha: Diagnostico con JTextArea para texto largo
        addDetalleLbl(pnlHeader, "Diagnostico:", 360, 12, true);
        javax.swing.JTextArea areaDiag = new javax.swing.JTextArea(
            c.getDiagnostico() != null ? c.getDiagnostico() : "(sin diagnostico)");
        areaDiag.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaDiag.setLineWrap(true);
        areaDiag.setWrapStyleWord(true);
        areaDiag.setEditable(false);
        areaDiag.setFocusable(false);
        areaDiag.setBackground(new Color(230, 240, 255));
        areaDiag.setBorder(null);
        areaDiag.setBounds(360, 34, 310, 78);
        pnlHeader.add(areaDiag);

        dlg.add(pnlHeader, BorderLayout.NORTH);

        // ── Tres tablas: Enfermedades | Vacunas | Exámenes ───────
        JPanel pnlTablas = new JPanel(new java.awt.GridLayout(1, 3, 8, 0));
        pnlTablas.setBorder(new EmptyBorder(8, 8, 8, 8));
        pnlTablas.setBackground(Color.WHITE);

        // Enfermedades
        List<Enfermedad> enfs = Clinica.getInstance()
            .obtenerEnfermedadesConsulta(c.getIdConsulta());
        DefaultTableModel mEnf = new DefaultTableModel(0, 3) {
            public boolean isCellEditable(int r, int col) { return false; }
        };
        mEnf.setColumnIdentifiers(new String[]{"ID", "Nombre", "Gravedad"});
        for (Enfermedad ef : enfs)
            mEnf.addRow(new Object[]{
                ef.getIdEnfermedad(),
                ef.getNombreEnfermedad(),
                ef.getGravedad() != null ? ef.getGravedad().getGravedad() : "—"});
        JTable tEnf = new JTable(mEnf);
        tEnf.setRowHeight(24);
        tEnf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tEnf.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tEnf.getTableHeader().setBackground(new Color(200, 60, 60));
        tEnf.getTableHeader().setForeground(Color.WHITE);
        JPanel pEnf = new JPanel(new BorderLayout());
        pEnf.setBorder(new TitledBorder("Enfermedades (" + enfs.size() + ")"));
        pEnf.add(new JScrollPane(tEnf));
        pnlTablas.add(pEnf);

        // Vacunas
        List<Vacuna> vacs = Clinica.getInstance()
            .obtenerVacunasConsulta(c.getIdConsulta());
        DefaultTableModel mVac = new DefaultTableModel(0, 2) {
            public boolean isCellEditable(int r, int col) { return false; }
        };
        mVac.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        for (Vacuna v : vacs)
            mVac.addRow(new Object[]{v.getIdVacuna(), v.getNombreVacuna()});
        JTable tVac = new JTable(mVac);
        tVac.setRowHeight(24);
        tVac.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tVac.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tVac.getTableHeader().setBackground(new Color(60, 160, 80));
        tVac.getTableHeader().setForeground(Color.WHITE);
        JPanel pVac = new JPanel(new BorderLayout());
        pVac.setBorder(new TitledBorder("Vacunas (" + vacs.size() + ")"));
        pVac.add(new JScrollPane(tVac));
        pnlTablas.add(pVac);

        // Exámenes
        List<Examen> exams = Clinica.getInstance().getExamenesDe(c.getIdConsulta());
        DefaultTableModel mExam = new DefaultTableModel(0, 2) {
            public boolean isCellEditable(int r, int col) { return false; }
        };
        mExam.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        for (Examen ex : exams)
            mExam.addRow(new Object[]{ex.getIdExamen(), ex.getNombreExamen()});
        JTable tExam = new JTable(mExam);
        tExam.setRowHeight(24);
        tExam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tExam.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tExam.getTableHeader().setBackground(new Color(70, 130, 180));
        tExam.getTableHeader().setForeground(Color.WHITE);
        JPanel pExam = new JPanel(new BorderLayout());
        pExam.setBorder(new TitledBorder("Examenes (" + exams.size() + ")"));
        pExam.add(new JScrollPane(tExam));
        pnlTablas.add(pExam);

        dlg.add(pnlTablas, BorderLayout.CENTER);

        // ── Botón cerrar ─────────────────────────────────────────
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlBot.setBackground(new Color(215, 228, 242));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(ev -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void addDetalleLbl(JPanel p, String txt, int x, int y, boolean bold) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 12));
        lbl.setBounds(x, y, 200, 20);
        p.add(lbl);
    }

    private ArrayList<Consultas> obtenerLista() {
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            return medActual != null
                ? Clinica.getInstance().getConsultasPorMedico(medActual.getCedula())
                : new ArrayList<>();
        }
        return Clinica.getInstance().getMisConsultas();
    }

    private void cargarDatos() {
        model.setRowCount(0);
        for (Consultas c : obtenerLista()) {
            model.addRow(new Object[]{
                c.getIdConsulta(),
                c.getFechaConsulta() != null ? c.getFechaConsulta().toString() : "",
                c.getDoctor()  != null ? c.getDoctor().getNombre()  : "(sin medico)",
                c.getPatient() != null ? c.getPatient().getNombre() : "(sin paciente)",
                c.getDiagnostico() != null ? c.getDiagnostico() : ""
            });
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Consultas c : obtenerLista()) {
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
                    c.getFechaConsulta() != null ? c.getFechaConsulta().toString() : "",
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
        btnVerCompleto.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}