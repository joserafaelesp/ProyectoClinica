package Visual;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import Logical.*;

public class MenuInformes extends JDialog {

    private static final Color BG = Color.WHITE;
    private static final Color PRIMARY = new Color(100, 120, 150);

    private final InformesDAO dao = new InformesDAO();

    public MenuInformes() {
        setTitle("Informes - Clinica");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Header simple
        JLabel titulo = new JLabel("Panel de Informes");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(10, 15, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Centro
        JPanel center = new JPanel(new GridLayout(2, 2, 10, 10));
        center.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(center, BorderLayout.CENTER);

        center.add(crearTarjeta("Reporte de Paciente", e -> abrirReportePaciente()));
        center.add(crearTarjeta("Consultas por Fecha", e -> abrirConsultasPorFecha()));
        center.add(crearTarjeta("Medicos Activos", e -> abrirMedicosMasActivos()));
        center.add(crearTarjeta("Enfermedades", e -> abrirEnfermedades()));

        // Botón cerrar
        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(cerrar);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel crearTarjeta(String titulo, ActionListener accion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBorder(new EmptyBorder(10, 5, 10, 5));

        JButton btn = new JButton("Abrir");
        btn.addActionListener(accion);

        panel.add(lbl, BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);

        return panel;
    }

    // ============================
    // REPORTE PACIENTE (simplificado)
    // ============================
    private void abrirReportePaciente() {
        JDialog dlg = new JDialog(this, "Reporte Paciente", true);
        dlg.setSize(600, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JComboBox<String> cbx = new JComboBox<>();
        ArrayList<Paciente> lista = Clinica.getInstance().getMisPaciente();

        for (Paciente p : lista) {
            cbx.addItem(p.getNombre());
        }

        JButton btn = new JButton("Generar");

        JPanel top = new JPanel();
        top.add(new JLabel("Paciente:"));
        top.add(cbx);
        top.add(btn);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        dlg.add(top, BorderLayout.NORTH);
        dlg.add(new JScrollPane(area), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            int i = cbx.getSelectedIndex();
            if (i < 0) return;

            Paciente p = lista.get(i);

            area.setText(
                "Nombre: " + p.getNombre() + "\n" +
                "Cedula: " + p.getCedula()
            );
        });

        dlg.setVisible(true);
    }

    // ============================
    private void abrirConsultasPorFecha() {
        JDialog dlg = new JDialog(this, "Consultas", true);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(this);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Medico", "Total"});

        JTable tabla = new JTable(model);

        JButton btn = new JButton("Cargar");

        btn.addActionListener(e -> {
            model.setRowCount(0);
            ArrayList<String[]> datos = dao.consultasPorFecha("2024-01-01", "2026-12-31");

            for (String[] d : datos) {
                model.addRow(new Object[]{d[0], d[2]});
            }
        });

        dlg.add(new JScrollPane(tabla), BorderLayout.CENTER);
        dlg.add(btn, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // ============================
    private void abrirMedicosMasActivos() {
        JDialog dlg = new JDialog(this, "Medicos", true);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(this);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Medico", "Consultas"});

        JTable tabla = new JTable(model);

        ArrayList<String[]> datos = dao.medicosMasActivos();

        for (String[] d : datos) {
            model.addRow(new Object[]{d[0], d[2]});
        }

        dlg.add(new JScrollPane(tabla));
        dlg.setVisible(true);
    }

    // ============================
    private void abrirEnfermedades() {
        JDialog dlg = new JDialog(this, "Enfermedades", true);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(this);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Enfermedad", "Veces"});

        JTable tabla = new JTable(model);

        ArrayList<String[]> datos = dao.enfermedadesMasDiagnosticadas();

        for (String[] d : datos) {
            model.addRow(new Object[]{d[0], d[2]});
        }

        dlg.add(new JScrollPane(tabla));
        dlg.setVisible(true);
    }
}