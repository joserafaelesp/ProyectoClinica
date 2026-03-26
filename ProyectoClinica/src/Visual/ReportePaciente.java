package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
import Logical.Enfermedad;
import Logical.Paciente;

import java.awt.Color;

public class ReportePaciente extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private JTextField txtIdPaciente;
    private JTextArea txtDescripcion;
    private JTextField txtCedula;
    private JTable tableEnfermedades;
    private static DefaultTableModel model;
    private Paciente pacienteActual = null;

    public static void main(String[] args) {
        try {
            ReportePaciente dialog = new ReportePaciente();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReportePaciente() {
        setTitle("Reporte de Paciente");
        setBounds(100, 100, 705, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(224, 255, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Nombre:");
        lblNewLabel.setBounds(10, 65, 54, 14);
        contentPanel.add(lblNewLabel);

        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        txtNombre.setEnabled(false);
        txtNombre.setBounds(74, 62, 166, 20);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("ID Paciente:");
        lblNewLabel_1.setBounds(10, 26, 70, 14);
        contentPanel.add(lblNewLabel_1);

        txtIdPaciente = new JTextField();
        txtIdPaciente.setBounds(90, 23, 150, 20);
        contentPanel.add(txtIdPaciente);
        txtIdPaciente.setColumns(10);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });
        btnBuscar.setBounds(250, 23, 80, 20);
        contentPanel.add(btnBuscar);

        JLabel lblNewLabel_4 = new JLabel("Descripcion de Paciente:");
        lblNewLabel_4.setBounds(10, 120, 147, 14);
        contentPanel.add(lblNewLabel_4);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 145, 400, 150);
        contentPanel.add(scrollPane);

        txtDescripcion = new JTextArea();
        scrollPane.setViewportView(txtDescripcion);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);

        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setBounds(10, 95, 46, 14);
        contentPanel.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setEditable(false);
        txtCedula.setEnabled(false);
        txtCedula.setBounds(74, 92, 166, 20);
        contentPanel.add(txtCedula);
        txtCedula.setColumns(10);

        JLabel lblEnfermedades = new JLabel("Enfermedades registradas:");
        lblEnfermedades.setBounds(10, 310, 200, 14);
        contentPanel.add(lblEnfermedades);

        JScrollPane scrollPaneEnfermedades = new JScrollPane();
        scrollPaneEnfermedades.setBounds(10, 335, 669, 180);
        contentPanel.add(scrollPaneEnfermedades);

        String headersEnfermedades[] = {"Código", "Nombre", "Síntomas"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(headersEnfermedades);

        tableEnfermedades = new JTable();
        tableEnfermedades.getTableHeader().setReorderingAllowed(false);
        tableEnfermedades.setModel(model);
        scrollPaneEnfermedades.setViewportView(tableEnfermedades);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Salir");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        buttonPane.add(cancelButton);
        
        cargarEnfermedades();
    }

    private void buscarPaciente() {
        String idPaciente = txtIdPaciente.getText().trim();
        if (idPaciente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un ID de paciente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        pacienteActual = Clinica.getInstance().obtenerPacienteById(idPaciente);
        if (pacienteActual != null) {
            txtNombre.setText(pacienteActual.getNombre());
            txtCedula.setText(pacienteActual.getCedula());
            
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(pacienteActual.getNombre()).append("\n");
            sb.append("Cédula: ").append(pacienteActual.getCedula()).append("\n");
            sb.append("Teléfono: ").append(pacienteActual.getTelefono()).append("\n");
            sb.append("Género: ").append(pacienteActual.getGenero()).append("\n");
            sb.append("Info Emergencia: ").append(pacienteActual.getInfoEmergencia()).append("\n");
            if (pacienteActual.getViviend() != null) {
                sb.append("Vivienda: ").append(pacienteActual.getViviend().getDireccion());
            }
            txtDescripcion.setText(sb.toString());
            
            JOptionPane.showMessageDialog(null, "Paciente encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.setText("");
            txtCedula.setText("");
            txtDescripcion.setText("");
        }
    }

    private void cargarEnfermedades() {
        ArrayList<Enfermedad> listaEnf = Clinica.getInstance().getMisEnfermedades();
        model.setRowCount(0);
        for (Enfermedad enfermedad : listaEnf) {
            model.addRow(new Object[]{enfermedad.getIdEnfermedad(), enfermedad.getNombreEnfermedad(), enfermedad.getSintomas()});
        }
    }
}