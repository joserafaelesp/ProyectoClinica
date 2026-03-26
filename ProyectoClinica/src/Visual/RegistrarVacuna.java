package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Paciente;
import Logical.Vacuna;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;

public class RegistrarVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombreVacuna;
	private JTextField txtIdVacuna;
	private JTextField txtDescripcion;
	private JTextField txtIdPaciente;
	private JSpinner spnCantidad;

	public static void main(String[] args) {
		try {
			RegistrarVacuna dialog = new RegistrarVacuna();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegistrarVacuna() {
		setTitle("Registrar Vacuna");
		setBounds(100, 100, 500, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBackground(new Color(255, 192, 203));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);

			JLabel lblNewLabel = new JLabel("Nombre:");
			lblNewLabel.setBounds(30, 65, 54, 14);
			panel.add(lblNewLabel);

			txtNombreVacuna = new JTextField();
			txtNombreVacuna.setBounds(94, 62, 166, 20);
			panel.add(txtNombreVacuna);
			txtNombreVacuna.setColumns(10);

			JLabel lblNewLabel_1 = new JLabel("ID:");
			lblNewLabel_1.setBounds(30, 26, 46, 14);
			panel.add(lblNewLabel_1);

			txtIdVacuna = new JTextField();
			txtIdVacuna.setEditable(false);
			txtIdVacuna.setEnabled(false);
			txtIdVacuna.setText("Vacuna - " + Clinica.generadorCodigoVacuna);
			txtIdVacuna.setBounds(94, 23, 86, 20);
			panel.add(txtIdVacuna);
			txtIdVacuna.setColumns(10);

			JLabel lblNewLabel_2 = new JLabel("Cantidad:");
			lblNewLabel_2.setBounds(30, 96, 54, 14);
			panel.add(lblNewLabel_2);

			spnCantidad = new JSpinner();
			spnCantidad.setModel(new SpinnerNumberModel(1, 1, 100, 1));
			spnCantidad.setBounds(94, 93, 86, 20);
			panel.add(spnCantidad);

			JLabel lblNewLabel_3 = new JLabel("ID Paciente:");
			lblNewLabel_3.setBounds(30, 127, 80, 14);
			panel.add(lblNewLabel_3);

			txtIdPaciente = new JTextField();
			txtIdPaciente.setBounds(120, 124, 100, 20);
			panel.add(txtIdPaciente);
			txtIdPaciente.setColumns(10);

			JLabel lblNewLabel_4 = new JLabel("Descripcion:");
			lblNewLabel_4.setBounds(30, 158, 72, 14);
			panel.add(lblNewLabel_4);

			txtDescripcion = new JTextField();
			txtDescripcion.setBounds(112, 155, 300, 100);
			panel.add(txtDescripcion);
			txtDescripcion.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Agregar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String idVacuna = txtIdVacuna.getText();
						String nombre = txtNombreVacuna.getText().trim();
						String idPaciente = txtIdPaciente.getText().trim();
						int cantidad = (int) spnCantidad.getValue();
						String descripcion = txtDescripcion.getText().trim();
						
						if (nombre.isEmpty() || idPaciente.isEmpty() || descripcion.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						Paciente paciente = Clinica.getInstance().obtenerPacienteById(idPaciente);
						if (paciente == null) {
							JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						Vacuna vacuna = new Vacuna(idVacuna, nombre, paciente, cantidad, descripcion);
						Clinica.getInstance().agregarVacuna(vacuna);
						clean();
						JOptionPane.showMessageDialog(null, "Vacuna registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void clean() {
		Clinica.generadorCodigoVacuna++;
		txtIdVacuna.setText("Vacuna - " + Clinica.generadorCodigoVacuna);
		txtNombreVacuna.setText("");
		txtIdPaciente.setText("");
		spnCantidad.setValue(1);
		txtDescripcion.setText("");
	}
}