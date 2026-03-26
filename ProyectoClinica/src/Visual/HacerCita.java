package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import Logical.Cita;
import Logical.Clinica;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.ImageIcon;

public class HacerCita extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodMed;
	private JTextField txtCodePaciente;
	private JSpinner spnFecha;
	private JTextField txtCita;

	public static void main(String[] args) {
		try {
			HacerCita dialog = new HacerCita();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HacerCita() {
		setTitle("Hacer Cita");
		setBounds(100, 100, 400, 280);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.info);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("ID Medico:");
		lblNewLabel_1.setBounds(10, 101, 80, 14);
		contentPanel.add(lblNewLabel_1);
		
		txtCodMed = new JTextField();
		txtCodMed.setBounds(100, 98, 100, 20);
		contentPanel.add(txtCodMed);
		txtCodMed.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("ID Paciente:");
		lblNewLabel_2.setBounds(10, 66, 80, 14);
		contentPanel.add(lblNewLabel_2);
		
		txtCodePaciente = new JTextField();
		txtCodePaciente.setBounds(100, 63, 100, 20);
		contentPanel.add(txtCodePaciente);
		txtCodePaciente.setColumns(10);
		
		spnFecha = new JSpinner();
		spnFecha.setBounds(100, 129, 123, 20);
		spnFecha.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		contentPanel.add(spnFecha);
		
		JLabel lblNewLabel_3 = new JLabel("Fecha:");
		lblNewLabel_3.setBounds(10, 132, 80, 14);
		contentPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Codigo Cita:");
		lblNewLabel_4.setBounds(10, 30, 80, 14);
		contentPanel.add(lblNewLabel_4);
		
		txtCita = new JTextField("Cita - " + Clinica.generadorCodigoCita);
		txtCita.setEnabled(false);
		txtCita.setBounds(100, 27, 100, 20);
		contentPanel.add(txtCita);
		txtCita.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(HacerCita.class.getResource("/imagenes/edificio-del-hospital (2).png")));
		lblNewLabel.setBounds(310, 147, 64, 70);
		contentPanel.add(lblNewLabel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(SystemColor.info);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idMedico = txtCodMed.getText().trim();
				String idPaciente = txtCodePaciente.getText().trim();
				Date fecha = (Date) spnFecha.getValue();
				
				if (idMedico.isEmpty() || idPaciente.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Cita citaReg = new Cita(txtCita.getText(),
						Clinica.getInstance().obtenerPacienteById(idPaciente),
						Clinica.getInstance().obtenerMedicoById(idMedico),
						fecha);
				Clinica.getInstance().agregarCita(citaReg);
				clean();
				JOptionPane.showMessageDialog(null, "Cita registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		buttonPane.add(btnRegistrar);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}
	
	public void clean() {
		txtCodePaciente.setText("");
		txtCodMed.setText("");
		Clinica.generadorCodigoCita++;
		txtCita.setText("Cita - " + Clinica.generadorCodigoCita);
	}
}