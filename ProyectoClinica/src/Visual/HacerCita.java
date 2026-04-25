package Visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import Logical.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class HacerCita extends JDialog {

    private JComboBox<String>  cbxPacientes;
    private JComboBox<String>  cbxMedicos;
    private JSpinner            spnFecha;
    private JComboBox<String>  cbxHora;
    private JComboBox<String>  cbxMinuto;
    private JComboBox<String>  cbxAmPm;
    private ArrayList<Paciente> listaPacientes;
    private ArrayList<Medico>   listaMedicos;
    private Cita                citaExistente;
    private boolean             esModificacion = false;
    private boolean             soloFechaHora  = false;

    public HacerCita() { this(null); }

    public HacerCita(Cita cita, boolean soloFechaHora) {
        this(cita);
        this.soloFechaHora = soloFechaHora;
    }

    public HacerCita(Cita cita) {
        this.citaExistente  = cita;
        this.esModificacion = (cita != null);

        setTitle(esModificacion ? "Modificar Cita Medica" : "Agendar Cita Medica");
        setSize(520, 390);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // ── Panel principal ──────────────────────────────────────
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(new TitledBorder(
            esModificacion ? "Modificar datos de la cita" : "Nueva Cita Medica"));
        panel.setLayout(null);
        getContentPane().add(panel, BorderLayout.CENTER);

        int lx = 20, fx = 170, rh = 26, gap = 44;

        // Codigo
        addLabel(panel, "Codigo:", lx, 30);
        String codigo = esModificacion
            ? citaExistente.getIdCita()
            : "CITA-" + Clinica.generadorCodigoCita;
        JTextField txtCodigo = new JTextField(codigo);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(new Color(230, 240, 255));
        txtCodigo.setForeground(new Color(30, 60, 120));
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtCodigo.setBounds(fx, 28, 130, rh);
        panel.add(txtCodigo);

        // Paciente — muestra nombre + cedula + ID
        addLabel(panel, "Paciente:", lx, 30 + gap);
        cbxPacientes = new JComboBox<>();
        listaPacientes = Clinica.getInstance().getMisPaciente();
        for (Paciente p : listaPacientes)
            cbxPacientes.addItem(p.getIdPaciente()
                + " | " + p.getNombre()
                + " | CI: " + p.getCedula());
        cbxPacientes.setBounds(fx, 28 + gap, 310, rh);
        panel.add(cbxPacientes);

        // Medico — muestra nombre + cedula + ID
        addLabel(panel, "Medico:", lx, 30 + gap * 2);
        cbxMedicos = new JComboBox<>();
        listaMedicos = Clinica.getInstance().getMisMedico();
        for (Medico m : listaMedicos)
            cbxMedicos.addItem(m.getIdMedico()
                + " | " + m.getNombre()
                + " | " + m.getEspecialidad());
        cbxMedicos.setBounds(fx, 28 + gap * 2, 310, rh);
        panel.add(cbxMedicos);

        // Fecha
        addLabel(panel, "Fecha:", lx, 30 + gap * 3);
        spnFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
        spnFecha.setEditor(editor);
        spnFecha.setBounds(fx, 28 + gap * 3, 160, rh);
        panel.add(spnFecha);

        // Hora con AM/PM
        addLabel(panel, "Hora:", lx, 30 + gap * 4);

        String[] horas = new String[12];
        for (int i = 0; i < 12; i++) horas[i] = String.format("%02d", i + 1);
        cbxHora = new JComboBox<>(horas);
        cbxHora.setBounds(fx, 28 + gap * 4, 65, rh);
        panel.add(cbxHora);

        JLabel lblDos = new JLabel(":");
        lblDos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDos.setBounds(fx + 68, 26 + gap * 4, 12, rh);
        panel.add(lblDos);

        cbxMinuto = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        cbxMinuto.setBounds(fx + 82, 28 + gap * 4, 65, rh);
        panel.add(cbxMinuto);

        cbxAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        cbxAmPm.setBounds(fx + 155, 28 + gap * 4, 70, rh);
        panel.add(cbxAmPm);

        // Precargar si es modificacion
        if (esModificacion) {
            if (citaExistente.getPaciente() != null) {
                for (int i = 0; i < listaPacientes.size(); i++) {
                    if (listaPacientes.get(i).getCedula()
                            .equals(citaExistente.getPaciente().getCedula())) {
                        cbxPacientes.setSelectedIndex(i); break;
                    }
                }
            }
            if (citaExistente.getDoc() != null) {
                for (int i = 0; i < listaMedicos.size(); i++) {
                    if (listaMedicos.get(i).getCedula()
                            .equals(citaExistente.getDoc().getCedula())) {
                        cbxMedicos.setSelectedIndex(i); break;
                    }
                }
            }
            if (citaExistente.getFecha() != null)
                spnFecha.setValue(citaExistente.getFecha());
            if (citaExistente.getHoraCita() != null) {
                String[] p = citaExistente.getHoraCita().split("[:  ]+");
                if (p.length >= 3) {
                    cbxHora.setSelectedItem(p[0]);
                    cbxMinuto.setSelectedItem(p[1]);
                    cbxAmPm.setSelectedItem(p[2].toUpperCase());
                }
            }
        }

        // Si es modo medico: bloquear paciente y medico
        if (soloFechaHora) {
            cbxPacientes.setEnabled(false);
            cbxMedicos.setEnabled(false);
            setTitle("Modificar Fecha y Hora de Cita");
        }

        // ── Botones ──────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(200, 220, 240));
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        JButton btnGuardar = new JButton(
            esModificacion ? "Guardar cambios" : "Agendar Cita");
        btnGuardar.setBackground(new Color(70, 130, 180));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarCita(txtCodigo.getText()));
        panelBotones.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
    }

    private void addLabel(JPanel p, String txt, int x, int y) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBounds(x, y, 140, 26);
        p.add(lbl);
    }

    private void guardarCita(String idCita) {
        if (cbxPacientes.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay pacientes registrados", "Error",
                JOptionPane.ERROR_MESSAGE); return;
        }
        if (cbxMedicos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay medicos registrados", "Error",
                JOptionPane.ERROR_MESSAGE); return;
        }

        Paciente paciente = listaPacientes.get(cbxPacientes.getSelectedIndex());
        Medico   medico   = listaMedicos.get(cbxMedicos.getSelectedIndex());
        Date     fecha    = (Date) spnFecha.getValue();
        String   hora     = cbxHora.getSelectedItem()
            + ":" + cbxMinuto.getSelectedItem()
            + " " + cbxAmPm.getSelectedItem();

        Cita citaNueva = new Cita(idCita, paciente, medico, fecha);
        citaNueva.setHoraCita(hora);
        citaNueva.setCompletada(esModificacion && citaExistente.isCompletada());

        try {
            if (esModificacion) {
                Clinica.getInstance().modificarCita(idCita, citaNueva);
                JOptionPane.showMessageDialog(this,
                    "Cita modificada correctamente",
                    "Modificacion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Clinica.getInstance().agregarCita(citaNueva);
                JOptionPane.showMessageDialog(this,
                    "Cita agendada:\n"
                    + "Paciente: " + paciente.getNombre()
                        + " (" + paciente.getIdPaciente() + ")\n"
                    + "Medico:   " + medico.getNombre()
                        + " (" + medico.getIdMedico() + ")\n"
                    + "Fecha:    " + fecha + "\n"
                    + "Hora:     " + hora,
                    "Cita Registrada", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}