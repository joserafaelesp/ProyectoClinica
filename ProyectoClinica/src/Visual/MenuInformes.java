package Visual;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import Logical.*;

public class MenuInformes extends JDialog {

    private static final Color BG       = new Color(245, 248, 252);
    private static final Color ACCENT   = new Color(70, 130, 180);
    private static final Color ACCENT2  = new Color(60, 160, 80);
    private static final Color ACCENT3  = new Color(160, 80, 180);
    private static final Color ACCENT4  = new Color(200, 100, 50);
    private static final Color FIXED_BG = new Color(230, 240, 255);

    private final InformesDAO dao = new InformesDAO();

    public MenuInformes() {
        setTitle("Informes — Clinica S.R.L");
        setBounds(100, 100, 900, 640);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // ── Encabezado rediseñado ─────────────────────────────────
        // Una sola franja con icono + título + subtítulo apilados
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(new Color(40, 90, 140));
        header.setBorder(new EmptyBorder(16, 24, 16, 24));

        // Icono decorativo izquierda
        JLabel lblIcono = new JLabel("");
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblIcono.setForeground(Color.WHITE);
        header.add(lblIcono, BorderLayout.WEST);

        // Título + subtítulo apilados en el centro
        JPanel pnlTexto = new JPanel();
        pnlTexto.setOpaque(false);
        pnlTexto.setLayout(new BoxLayout(pnlTexto, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Panel de Informes y Estadisticas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        pnlTexto.add(lblTitulo);

        JLabel lblSub = new JLabel("Clinica S.R.L  •  Solo visible para Administrador");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(180, 210, 240));
        pnlTexto.add(lblSub);

        header.add(pnlTexto, BorderLayout.CENTER);
        getContentPane().add(header, BorderLayout.NORTH);

        // ── Panel central con 4 tarjetas ─────────────────────────
        JPanel center = new JPanel(new GridLayout(2, 2, 15, 15));
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(20, 20, 10, 20));
        getContentPane().add(center, BorderLayout.CENTER);

        center.add(crearTarjeta(
            "Reporte de Paciente",
            "Historial clinico completo:\nconsultas, diagnosticos,\nenfermedades, vacunas y examenes.",
            ACCENT, e -> abrirReportePaciente()
        ));
        center.add(crearTarjeta(
            "Consultas por Fechas",
            "Total de consultas realizadas\nentre dos fechas, agrupadas\npor medico.",
            ACCENT2, e -> abrirConsultasPorFecha()
        ));
        center.add(crearTarjeta(
            "Medico mas Activo",
            "Ranking de medicos segun\nnumero de consultas realizadas\ny pacientes atendidos.",
            ACCENT3, e -> abrirMedicosMasActivos()
        ));
        center.add(crearTarjeta(
            "Enfermedades Frecuentes",
            "Ranking de enfermedades mas\ndiagnosticadas en el sistema\ncon su nivel de gravedad.",
            ACCENT4, e -> abrirEnfermedades()
        ));

        // ── Barra inferior ────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottom.setBackground(new Color(215, 228, 242));
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 200, 225)));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());
        bottom.add(btnCerrar);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    // ── Fábrica de tarjetas ───────────────────────────────────────
    private JPanel crearTarjeta(String titulo, String descripcion,
                                 Color color, ActionListener accion) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            new EmptyBorder(16, 18, 16, 18)));

        // Título con icono integrado
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTit.setForeground(color);
        card.add(lblTit, BorderLayout.NORTH);

        // Descripción
        JLabel lblDesc = new JLabel(
            "<html><body style='width:160px'>"
            + descripcion.replace("\n", "<br>")
            + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(90, 90, 90));
        card.add(lblDesc, BorderLayout.CENTER);

        // Botón
        JButton btn = new JButton("Ver Informe \u2192");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(accion);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    // ── Informe 1: Reporte de Paciente ────────────────────────────
    private void abrirReportePaciente() {
        JDialog dlg = new JDialog(this, "Reporte de Paciente", true);
        dlg.setSize(750, 620);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBuscar.setBackground(FIXED_BG);
        pnlBuscar.setBorder(new EmptyBorder(5, 10, 5, 10));
        pnlBuscar.add(new JLabel("Buscar paciente:") {{
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }});

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
            String sep = "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550"
                       + "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550"
                       + "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\n";

            StringBuilder reporte = new StringBuilder();
            reporte.append(sep);
            reporte.append("   REPORTE MEDICO \u2014 CLINICA S.R.L\n");
            reporte.append(sep);
            reporte.append("Paciente : ").append(pac.getNombre()).append("\n");
            reporte.append("Cedula   : ").append(pac.getCedula()).append("\n");
            reporte.append("ID       : ").append(pac.getIdPaciente()).append("\n");
            reporte.append("Telefono : ").append(nullSafe(pac.getTelefono())).append("\n");
            reporte.append("Genero   : ").append(nullSafe(pac.getGenero())).append("\n");
            if (pac.getViviend() != null)
                reporte.append("Direccion: ").append(pac.getViviend().getDireccion()).append("\n");
            reporte.append(sep);

            java.util.List<Consultas> consultas =
                Clinica.getInstance().getMisConsultasDelPaciente(cedula);
            reporte.append("HISTORIAL DE CONSULTAS (").append(consultas.size()).append(")\n");
            reporte.append(sep);
            if (consultas.isEmpty()) {
                reporte.append("  Sin consultas registradas.\n");
            } else {
                for (Consultas c : consultas) {
                    reporte.append("  Consulta    : ").append(c.getIdConsulta()).append("\n");
                    reporte.append("  Fecha       : ").append(c.getFechaConsulta()).append("\n");
                    reporte.append("  Medico      : ")
                        .append(c.getDoctor() != null ? c.getDoctor().getNombre() : "\u2014").append("\n");
                    reporte.append("  Diagnostico : ").append(nullSafe(c.getDiagnostico())).append("\n");
                    reporte.append("  \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
                }
            }

            java.util.List<Enfermedad> enfs =
                Clinica.getInstance().obtenerEnfermedadesDePaciente(cedula);
            reporte.append("\nENFERMEDADES DIAGNOSTICADAS (").append(enfs.size()).append(")\n");
            reporte.append(sep);
            if (enfs.isEmpty()) {
                reporte.append("  Ninguna registrada.\n");
            } else {
                for (Enfermedad enf : enfs) {
                    reporte.append("  \u2022 ").append(enf.getNombreEnfermedad());
                    if (enf.getGravedad() != null)
                        reporte.append(" [").append(enf.getGravedad().getGravedad()).append("]");
                    reporte.append("\n");
                }
            }
            reporte.append(sep);
            txtReporte.setText(reporte.toString());
            txtReporte.setCaretPosition(0);
        });

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBot.setBackground(new Color(200, 220, 240));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e2 -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Informe 2: Consultas por Rango de Fechas ─────────────────
    private void abrirConsultasPorFecha() {
        JDialog dlg = new JDialog(this, "Consultas por Rango de Fechas", true);
        dlg.setSize(750, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(new Color(220, 245, 220));
        pnlTop.setBorder(new EmptyBorder(5, 15, 5, 15));
        pnlTop.add(new JLabel("Desde:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); }});
        JTextField txtDesde = new JTextField("2024-01-01");
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
                try { total += Integer.parseInt(row[2]); } catch (NumberFormatException ex) {}
            }
            lblResumen.setText("  Total: " + total + " consultas  |  Medicos activos: " + datos.size());
            if (datos.isEmpty())
                JOptionPane.showMessageDialog(dlg,
                    "No hay consultas en ese rango de fechas.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        });
        dlg.setVisible(true);
    }

    // ── Informe 3: Medico mas Activo ──────────────────────────────
    private void abrirMedicosMasActivos() {
        JDialog dlg = new JDialog(this, "Ranking \u2014 Medicos mas Activos", true);
        dlg.setSize(700, 450);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(new Color(240, 230, 255));
        pnlTop.setBorder(new EmptyBorder(5, 15, 5, 15));
        JLabel lbl = new JLabel("Ranking de medicos segun consultas realizadas");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(ACCENT3);
        pnlTop.add(lbl);
        dlg.add(pnlTop, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(0, 5) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
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

        ArrayList<String[]> datos = dao.medicosMasActivos();
        int pos = 1;
        for (String[] row : datos) {
            String medalla = pos == 1 ? "\uD83E\uDD47 1\u00B0"
                           : pos == 2 ? "\uD83E\uDD48 2\u00B0"
                           : pos == 3 ? "\uD83E\uDD49 3\u00B0"
                           : pos + "\u00B0";
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

    // ── Informe 4: Enfermedades mas Diagnosticadas ────────────────
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

        ArrayList<String[]> datos = dao.enfermedadesMasDiagnosticadas();
        int pos = 1;
        for (String[] row : datos) {
            String posStr = pos == 1 ? "\uD83E\uDD47 1\u00B0"
                          : pos == 2 ? "\uD83E\uDD48 2\u00B0"
                          : pos == 3 ? "\uD83E\uDD49 3\u00B0"
                          : pos + "\u00B0";
            model.addRow(new Object[]{posStr, row[0], row[1], row[2]});
            pos++;
        }

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlBot.setBackground(new Color(200, 220, 240));
        JLabel lblTotal = new JLabel("Enfermedades en catalogo: " + datos.size());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotal.setForeground(ACCENT4);
        pnlBot.add(lblTotal);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlBot.add(btnCerrar);
        dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private String nullSafe(String valor) {
        return (valor != null && !valor.trim().isEmpty()) ? valor : "\u2014";
    }
}