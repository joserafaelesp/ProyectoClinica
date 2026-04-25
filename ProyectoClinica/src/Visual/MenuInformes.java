package Visual;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import Logical.*;

public class MenuInformes extends JDialog {

    private static final Color BG        = new Color(245, 248, 252);
    private static final Color ACCENT    = new Color(70, 130, 180);
    private static final Color ACCENT2   = new Color(60, 160, 80);
    private static final Color ACCENT3   = new Color(160, 80, 180);
    private static final Color ACCENT4   = new Color(200, 100, 50);
    private static final Color FIXED_BG  = new Color(230, 240, 255);
    private static final Color FIXED_FG  = new Color(30, 60, 120);

    private final InformesDAO dao = new InformesDAO();

    public MenuInformes() {
        setTitle("Informes y Estadisticas — Clinica SAM");
        setBounds(100, 100, 900, 640);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // ── Encabezado ───────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ACCENT);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("  Informes y Estadisticas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Clinica SAM — Panel de Administrador  ");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSub.setForeground(new Color(200, 220, 255));
        header.add(lblSub, BorderLayout.EAST);
        getContentPane().add(header, BorderLayout.NORTH);

        // ── Panel central con 4 botones ───────────────────────────
        JPanel center = new JPanel(new GridLayout(2, 2, 15, 15));
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(20, 20, 10, 20));
        getContentPane().add(center, BorderLayout.CENTER);

        // Botón 1 — Reporte de Paciente
        center.add(crearBotonInforme(
            "1. Reporte de Paciente",
            "Historial clínico completo:\nconsultas, diagnósticos,\nenfermedades, vacunas y exámenes.",
            ACCENT,
            e -> abrirReportePaciente()
        ));

        // Botón 2 — Consultas por Rango de Fechas
        center.add(crearBotonInforme(
            "2. Consultas por Fechas",
            "Total de consultas realizadas\nentre dos fechas, agrupadas\npor médico.",
            ACCENT2,
            e -> abrirConsultasPorFecha()
        ));

        // Botón 3 — Médico más Activo
        center.add(crearBotonInforme(
            "3. Medico mas Activo",
            "Ranking de médicos según\nnúmero de consultas realizadas\ny pacientes atendidos.",
            ACCENT3,
            e -> abrirMedicosMasActivos()
        ));

        // Botón 4 — Enfermedades más Diagnosticadas
        center.add(crearBotonInforme(
            "4. Enfermedades Frecuentes",
            "Ranking de enfermedades más\ndiagnosticadas en el sistema\ncon su nivel de gravedad.",
            ACCENT4,
            e -> abrirEnfermedades()
        ));

        // ── Botón cerrar ──────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottom.setBackground(new Color(200, 220, 240));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        bottom.add(btnCerrar);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    // ── Fabrica de botones ────────────────────────────────────────
    private JPanel crearBotonInforme(String titulo, String descripcion,
                                      Color color, ActionListener accion) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            new EmptyBorder(15, 18, 15, 18)));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTit.setForeground(color);
        card.add(lblTit, BorderLayout.NORTH);

        JLabel lblDesc = new JLabel("<html>" + descripcion.replace("\n", "<br>") + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(80, 80, 80));
        card.add(lblDesc, BorderLayout.CENTER);

        JButton btn = new JButton("Ver Informe →");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.addActionListener(accion);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    // ── Informe 1: Reporte de Paciente ────────────────────────────
    private void abrirReportePaciente() {
        JDialog dlg = new JDialog(this, "Reporte de Paciente", true);
        dlg.setSize(750, 600);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Busqueda
        JPanel pnlBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBuscar.setBackground(FIXED_BG);
        pnlBuscar.setBorder(new EmptyBorder(5, 10, 5, 10));

        pnlBuscar.add(new JLabel("Buscar paciente:") {{
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }});

        // Combo con pacientes
        JComboBox<String> cbxPacientes = new JComboBox<>();
        ArrayList<Paciente> listaPac = Clinica.getInstance().getMisPaciente();
        for (Paciente p : listaPac)
            cbxPacientes.addItem(p.getIdPaciente() + " | " + p.getNombre()
                + " | CI: " + p.getCedula());
        cbxPacientes.setPreferredSize(new Dimension(380, 30));
        pnlBuscar.add(cbxPacientes);

        JButton btnBuscar = new JButton("Generar Reporte");
        btnBuscar.setBackground(ACCENT);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlBuscar.add(btnBuscar);
        dlg.add(pnlBuscar, BorderLayout.NORTH);

        // Area de reporte
        JTextArea txtReporte = new JTextArea();
        txtReporte.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtReporte.setEditable(false);
        txtReporte.setBackground(new Color(250, 252, 255));
        txtReporte.setMargin(new Insets(10, 15, 10, 15));
        txtReporte.setText("Selecciona un paciente y presiona 'Generar Reporte'.");
        dlg.add(new JScrollPane(txtReporte), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            int idx = cbxPacientes.getSelectedIndex();
            if (idx < 0 || listaPac.isEmpty()) return;
            Paciente pac = listaPac.get(idx);
            String cedula = pac.getCedula();
            String sep = "══════════════════════════════════════════\n";
            String reporte = sep
                + "   REPORTE MÉDICO — CLÍNICA SAM\n"
                + sep
                + "DATOS PERSONALES\n"
                + "──────────────────────────────────────────\n"
                + dao.reportePaciente(cedula) + "\n"
                + sep
                + "Generado por el sistema de Clínica SAM\n"
                + sep;
            txtReporte.setText(reporte);
            txtReporte.setCaretPosition(0);
        });

        // Botones inferiores
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlBot.setBackground(new Color(200, 220, 240));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Informe 2: Consultas por Rango de Fechas ─────────────────
    private void abrirConsultasPorFecha() {
        JDialog dlg = new JDialog(this, "Consultas por Rango de Fechas", true);
        dlg.setSize(750, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(FIXED_BG);
        pnlTop.setBorder(new EmptyBorder(5, 10, 5, 10));

        pnlTop.add(new JLabel("Desde:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); }});
        JTextField txtDesde = new JTextField("2026-01-01");
        txtDesde.setPreferredSize(new Dimension(110, 28));
        pnlTop.add(txtDesde);

        pnlTop.add(new JLabel("Hasta:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); }});
        JTextField txtHasta = new JTextField("2026-12-31");
        txtHasta.setPreferredSize(new Dimension(110, 28));
        pnlTop.add(txtHasta);

        JButton btnBuscar = new JButton("Generar");
        btnBuscar.setBackground(ACCENT2);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlTop.add(btnBuscar);
        dlg.add(pnlTop, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(0, 5) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "Medico", "Especialidad", "Total Consultas",
            "Primera Consulta", "Ultima Consulta"});
        JTable tabla = new JTable(model);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(ACCENT2);
        tabla.getTableHeader().setForeground(Color.WHITE);
        dlg.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Resumen
        JLabel lblResumen = new JLabel("  Total de consultas: -");
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResumen.setForeground(ACCENT2);

        JPanel pnlBot = new JPanel(new BorderLayout());
        pnlBot.setBackground(new Color(200, 220, 240));
        pnlBot.setBorder(new EmptyBorder(5, 10, 5, 10));
        pnlBot.add(lblResumen, BorderLayout.WEST);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(new Color(200, 220, 240));
        right.add(btnCerrar);
        pnlBot.add(right, BorderLayout.EAST);
        dlg.add(pnlBot, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            model.setRowCount(0);
            ArrayList<String[]> datos = dao.consultasPorFecha(
                txtDesde.getText().trim(), txtHasta.getText().trim());
            int total = 0;
            for (String[] row : datos) {
                model.addRow(row);
                total += Integer.parseInt(row[2]);
            }
            lblResumen.setText("  Total de consultas en el período: " + total
                + "  |  Médicos activos: " + datos.size());
            if (datos.isEmpty())
                JOptionPane.showMessageDialog(dlg,
                    "No hay consultas en ese rango de fechas.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        });

        dlg.setVisible(true);
    }

    // ── Informe 3: Médico más Activo ──────────────────────────────
    private void abrirMedicosMasActivos() {
        JDialog dlg = new JDialog(this, "Ranking — Medicos mas Activos", true);
        dlg.setSize(700, 450);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(new Color(240, 230, 255));
        pnlTop.setBorder(new EmptyBorder(5, 15, 5, 15));
        JLabel lbl = new JLabel("Ranking de médicos según consultas realizadas");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(ACCENT3);
        pnlTop.add(lbl);
        dlg.add(pnlTop, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(0, 4) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "#", "Medico", "Especialidad", "Consultas", "Pacientes Atendidos"});
        // Agregar columna de posicion
        model.setColumnCount(5);
        model.setColumnIdentifiers(new String[]{
            "Posicion", "Medico", "Especialidad",
            "Total Consultas", "Pacientes Atendidos"});

        JTable tabla = new JTable(model);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(ACCENT3);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(130);
        dlg.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Cargar datos
        ArrayList<String[]> datos = dao.medicosMasActivos();
        int pos = 1;
        for (String[] row : datos) {
            String medalla = pos == 1 ? "🥇 1°" : pos == 2 ? "🥈 2°" : pos == 3 ? "🥉 3°"
                : pos + "°";
            model.addRow(new Object[]{medalla, row[0], row[1], row[2], row[3]});
            pos++;
        }

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlBot.setBackground(new Color(200, 220, 240));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Informe 4: Enfermedades más Diagnosticadas ────────────────
    private void abrirEnfermedades() {
        JDialog dlg = new JDialog(this, "Enfermedades mas Diagnosticadas", true);
        dlg.setSize(650, 450);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(new Color(255, 240, 230));
        pnlTop.setBorder(new EmptyBorder(5, 15, 5, 15));
        JLabel lbl = new JLabel("Ranking de enfermedades mas frecuentes en el sistema");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(ACCENT4);
        pnlTop.add(lbl);
        dlg.add(pnlTop, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(0, 4) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "Posicion", "Enfermedad", "Gravedad", "Veces Diagnosticada"});

        JTable tabla = new JTable(model);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(ACCENT4);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        dlg.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Cargar datos
        ArrayList<String[]> datos = dao.enfermedadesMasDiagnosticadas();
        int pos = 1;
        for (String[] row : datos) {
            String posStr = pos == 1 ? "🥇 1°" : pos == 2 ? "🥈 2°"
                : pos == 3 ? "🥉 3°" : pos + "°";
            // Color por gravedad
            model.addRow(new Object[]{posStr, row[0], row[1], row[2]});
            pos++;
        }

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlBot.setBackground(new Color(200, 220, 240));
        JLabel lblTotal = new JLabel("Total enfermedades en catalogo: " + datos.size());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotal.setForeground(ACCENT4);
        pnlBot.add(lblTotal);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
}