package Visual;

import javax.swing.*;
import Logical.*;
import java.awt.*;
import java.util.ArrayList;

public class HacerCita extends JDialog {
    private JComboBox<String> cbxPacientes;
    private JComboBox<String> cbxMedicos;
    private JSpinner spnFecha;

    public HacerCita() {
        setTitle("Agendar Cita Médica");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("  Seleccione Paciente:"));
        cbxPacientes = new JComboBox<>();
        llenarPacientes();
        add(cbxPacientes);

        add(new JLabel("  Seleccione Médico:"));
        cbxMedicos = new JComboBox<>();
        llenarMedicos();
        add(cbxMedicos);

        add(new JLabel("  Fecha y Hora:"));
        spnFecha = new JSpinner(new SpinnerDateModel());
        add(spnFecha);

        JButton btnCita = new JButton("Agendar Cita");
        btnCita.addActionListener(e -> registrarCita());
        add(new JLabel(""));
        add(btnCita);
    }

    private void llenarPacientes() {
        for (Paciente p : Clinica.getInstance().getMisPaciente()) {
            cbxPacientes.addItem(p.getNombre() + " (" + p.getIdPaciente() + ")");
        }
    }

    private void llenarMedicos() {
        for (Medico m : Clinica.getInstance().getMisMedico()) {
            cbxMedicos.addItem(m.getNombre() + " (" + m.getIdMedico() + ")");
        }
    }

    private void registrarCita() {
        // Lógica para extraer el ID del String seleccionado y guardar
        JOptionPane.showMessageDialog(this, "Cita agendada con éxito");
        dispose();
    }
}