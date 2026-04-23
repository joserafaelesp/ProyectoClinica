package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Vivienda;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrarVivienda extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodeVivienda;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private Vivienda miVivienda;
	private Vivienda casaReg;
	private boolean esModificacion = false;

	public static void main(String[] args) {
		try {
			RegistrarVivienda dialog = new RegistrarVivienda(null, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vivienda getCasaReg() {
		return casaReg;
	}

	public RegistrarVivienda(Vivienda house, int index) {
		miVivienda = house;
		esModificacion = (house != null);
		
		if (!esModificacion) {
			setTitle("Ingresar Vivienda");
		} else {
			setTitle("Modificar Vivienda");
		}

		setBounds(100, 100, 400, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBackground(SystemColor.info);
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);

			JLabel lblNewLabel_1 = new JLabel("Codigo:");
			lblNewLabel_1.setBounds(23, 24, 46, 14);
			panel.add(lblNewLabel_1);

			txtCodeVivienda = new JTextField();
			txtCodeVivienda.setBackground(SystemColor.info);
			txtCodeVivienda.setEnabled(false);
			txtCodeVivienda.setText("Vivienda - " + Clinica.generadorCodigoVivienda);
			txtCodeVivienda.setBounds(90, 21, 86, 20);
			panel.add(txtCodeVivienda);
			txtCodeVivienda.setColumns(10);

			JLabel lblNewLabel_2 = new JLabel("Telefono:");
			lblNewLabel_2.setBounds(23, 73, 52, 14);
			panel.add(lblNewLabel_2);

			txtTelefono = new JTextField();
			txtTelefono.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char key = e.getKeyChar();
					if (!Character.isDigit(key))
						e.consume();
				}
			});
			txtTelefono.setBounds(90, 70, 86, 20);
			panel.add(txtTelefono);
			txtTelefono.setColumns(10);

			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(RegistrarVivienda.class.getResource("/imagenes/edificio-del-hospital (2).png")));
			lblNewLabel.setBounds(310, 95, 64, 73);
			panel.add(lblNewLabel);

			JLabel lblNewLabel_3 = new JLabel("Direccion:");
			lblNewLabel_3.setBounds(23, 118, 57, 14);
			panel.add(lblNewLabel_3);

			txtDireccion = new JTextField();
			txtDireccion.setBounds(90, 115, 164, 20);
			panel.add(txtDireccion);
			txtDireccion.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(SystemColor.info);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String telefono = txtTelefono.getText().trim();
						String direccion = txtDireccion.getText().trim();
						
						if (telefono.isEmpty() || direccion.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (!esModificacion) {
							casaReg = new Vivienda(txtCodeVivienda.getText(), direccion, telefono, null);
							Clinica.getInstance().agregarVivienda(casaReg);
							clean();
							JOptionPane.showMessageDialog(null, "Vivienda registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						} else {
							miVivienda.setDireccion(direccion);
							Clinica.getInstance().modificarVivienda(miVivienda.getIdVivienda(), miVivienda);
							dispose();
							JOptionPane.showMessageDialog(null, "Vivienda actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				btnRegistrar.setActionCommand("OK");
				buttonPane.add(btnRegistrar);
				getRootPane().setDefaultButton(btnRegistrar);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		if (miVivienda != null)
			loadVivienda();
	}

	public void clean() {
		Clinica.generadorCodigoVivienda++;
		txtCodeVivienda.setText("Vivienda - " + Clinica.generadorCodigoVivienda);
		txtDireccion.setText("");
		txtTelefono.setText("");
	}
	
	public void loadVivienda() {
		txtCodeVivienda.setText(miVivienda.getIdVivienda());
		txtDireccion.setText(miVivienda.getDireccion());
		txtTelefono.setText(miVivienda.getTelefono());
	}
}