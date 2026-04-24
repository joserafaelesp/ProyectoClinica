package Visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Logical.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

public class HacerCita extends JDialog {

    private JComboBox<String> cbxPacientes;
    private JComboBox<String> cbxMedicos;
    private JSpinner          spnFecha;
    private JTextField        txtHora;
    private ArrayList<Paciente> listaPacientes;
    private ArrayList<Medico>   listaMedicos;

    public HacerCita() {
        setTitle("Agendar Cita Médica");
        setSize(460, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // ── Panel principal ──────────────────────────────────────
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 12));
        panel.setBorder(new EmptyBorder(20, 20, 10, 20));
        panel.setBackground(new Color(240, 248, 255));
        getContentPane().add(panel, BorderLayout.CENTER);

        // Paciente
        panel.add(new JLabel("Paciente:"));
        cbxPacientes = new JComboBox<>();
        listaPacientes = Clinica.getInstance().getMisPaciente();
        for (Paciente p : listaPacientes)
            cbxPacientes.addItem(p.getNombre() + " (" + p.getCedula() + ")");
        panel.add(cbxPacientes);

        // Médico
        panel.add(new JLabel("Médico:"));
        cbxMedicos = new JComboBox<>();
        listaMedicos = Clinica.getInstance().getMisMedico();
        for (Medico m : listaMedicos)
            cbxMedicos.addItem(m.getNombre() + " (" + m.getCedula() + ")");
        panel.add(cbxMedicos);

        // Fecha
        panel.add(new JLabel("Fecha de la cita:"));
        spnFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
        spnFecha.setEditor(editor);
        panel.add(spnFecha);

        // Hora
        panel.add(new JLabel("Hora (HH:MM):"));
        txtHora = new JTextField("09:00");
        panel.add(txtHora);

        // Código de cita (auto)
        panel.add(new JLabel("Código de cita:"));
        JTextField txtCodigo = new JTextField(
            "CITA-" + Clinica.generadorCodigoCita);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(new Color(230, 230, 230));
        panel.add(txtCodigo);

        // ── Botones ──────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(240, 248, 255));
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        JButton btnAgendar = new JButton("Agendar Cita");
        btnAgendar.setBackground(new Color(70, 130, 180));
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.setFocusPainted(false);
        btnAgendar.addActionListener((ActionEvent e) -> registrarCita(
            txtCodigo.getText()));
        panelBotones.add(btnAgendar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
    }

    private void registrarCita(String idCita) {

        // Validar selección
        if (cbxPacientes.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay pacientes registrados", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbxMedicos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay médicos registrados", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener paciente y médico seleccionados
        int idxPac = cbxPacientes.getSelectedIndex();
        int idxMed = cbxMedicos.getSelectedIndex();
        Paciente paciente = listaPacientes.get(idxPac);
        Medico   medico   = listaMedicos.get(idxMed);
        Date     fecha    = (Date) spnFecha.getValue();
        String   hora     = txtHora.getText().trim();

        // Validar hora
        if (!hora.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "Ingrese la hora en formato HH:MM (ej: 09:30)",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear objeto Cita
        Cita nuevaCita = new Cita(idCita, paciente, medico, fecha);
        nuevaCita.setHoraCita(hora);
        nuevaCita.setCompletada(false);

        // Guardar en SQL Server
        boolean ok = false;
        try {
            Clinica.getInstance().agregarCita(nuevaCita);
            ok = true;
        } catch (Exception ex) {
            System.out.println("Error al guardar cita: " + ex.getMessage());
        }

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Cita agendada exitosamente\n"
                + "Paciente: " + paciente.getNombre() + "\n"
                + "Médico:   " + medico.getNombre()   + "\n"
                + "Fecha:    " + fecha                + "\n"
                + "Hora:     " + hora,
                "Cita Registrada",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la cita en la base de datos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}