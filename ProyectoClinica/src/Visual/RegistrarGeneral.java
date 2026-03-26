package Visual;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Medico;
import Logical.Paciente;
import Logical.Persona;
import Logical.Vivienda;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class RegistrarGeneral extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodeMed;
	private JTextField txtTelefono;
	private JTextField txtNombre;
	private JTextField txtCedula;
	private JTextField txtxEspecialidad;
	private JSpinner spnFecha;
	private JRadioButton rdbtnPaciente;
	private JRadioButton rdbtnMedico;
	private JPanel panel_Medico;
	private JTextField textCodigoPaciente;
	private JTextField textFieldInfoEmergencia;
	private JPanel panel_Paciente;
	private Persona miPersona;
	private JTextField txtVivienda;
	private Vivienda nuevaViv = null;
	private JComboBox<String> comboBoxGender;
	private JButton btnEditarVivienda;
	private boolean esModificacion = false;

	public static void main(String[] args) {
		try {
			RegistrarGeneral dialog = new RegistrarGeneral(null, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegistrarGeneral(Persona person, int index) {
		miPersona = person;
		esModificacion = (person != null);
		
		if (!esModificacion) {
			setTitle("Registrar Persona");
		} else {
			setTitle("Modificar Persona");
		}
		
		setBounds(100, 100, 705, 411);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBackground(SystemColor.info);
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);

			JPanel panel_DatosGenerales = new JPanel();
			panel_DatosGenerales.setBorder(new TitledBorder(null, "Datos generales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_DatosGenerales.setBackground(SystemColor.info);
			panel_DatosGenerales.setBounds(10, 11, 669, 203);
			panel.add(panel_DatosGenerales);
			panel_DatosGenerales.setLayout(null);

			JLabel lblApellido = new JLabel("Cedula:");
			lblApellido.setBounds(10, 26, 52, 14);
			panel_DatosGenerales.add(lblApellido);

			txtCedula = new JTextField();
			txtCedula.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char key = e.getKeyChar();
					if (!Character.isDigit(key))
						e.consume();
				}
			});
			txtCedula.setBounds(66, 23, 152, 20);
			txtCedula.setEnabled(!esModificacion);
			panel_DatosGenerales.add(txtCedula);
			txtCedula.setColumns(10);

			JLabel lbltelefon = new JLabel("Telefono:");
			lbltelefon.setBounds(10, 110, 52, 14);
			panel_DatosGenerales.add(lbltelefon);

			txtTelefono = new JTextField();
			txtTelefono.setBounds(74, 107, 144, 20);
			txtTelefono.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char key = e.getKeyChar();
					if (!Character.isDigit(key))
						e.consume();
				}
			});
			panel_DatosGenerales.add(txtTelefono);
			txtTelefono.setColumns(10);

			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setBounds(10, 69, 52, 14);
			panel_DatosGenerales.add(lblNombre);

			txtNombre = new JTextField();
			txtNombre.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char key = e.getKeyChar();
					if (!Character.isAlphabetic(key))
						e.consume();
				}
			});
			txtNombre.setBounds(66, 66, 152, 20);
			panel_DatosGenerales.add(txtNombre);
			txtNombre.setColumns(10);

			JLabel lblGenero = new JLabel("Genero:");
			lblGenero.setBounds(10, 156, 52, 14);
			panel_DatosGenerales.add(lblGenero);

			comboBoxGender = new JComboBox<>();
			comboBoxGender.setModel(new DefaultComboBoxModel<>(new String[] {"Elegir", "Masculino", "Femenino"}));
			comboBoxGender.setEditable(true);
			comboBoxGender.setMaximumRowCount(3);
			comboBoxGender.setBounds(82, 153, 94, 20);
			panel_DatosGenerales.add(comboBoxGender);

			rdbtnMedico = new JRadioButton("Medico");
			rdbtnMedico.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rdbtnPaciente.setSelected(false);
					panel_Medico.setVisible(true);
					panel_Paciente.setVisible(false);
				}
			});
			rdbtnMedico.setBackground(SystemColor.info);
			rdbtnMedico.setBounds(402, 152, 109, 23);
			panel_DatosGenerales.add(rdbtnMedico);

			rdbtnPaciente = new JRadioButton("Paciente");
			rdbtnPaciente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rdbtnMedico.setSelected(false);
					panel_Medico.setVisible(false);
					panel_Paciente.setVisible(true);
				}
			});
			rdbtnPaciente.setBackground(SystemColor.info);
			rdbtnPaciente.setBounds(402, 124, 109, 23);
			panel_DatosGenerales.add(rdbtnPaciente);

			JLabel lblNewLabel_2 = new JLabel("Rol:");
			lblNewLabel_2.setBounds(333, 128, 46, 14);
			panel_DatosGenerales.add(lblNewLabel_2);

			spnFecha = new JSpinner();
			spnFecha.setBounds(402, 23, 144, 28);
			spnFecha.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
			panel_DatosGenerales.add(spnFecha);

			JLabel lblNewLabel_3 = new JLabel("Fecha de Nacimiento:");
			lblNewLabel_3.setBounds(255, 26, 144, 14);
			panel_DatosGenerales.add(lblNewLabel_3);

			JLabel lblNewLabel_4 = new JLabel("Codigo de vivienda:");
			lblNewLabel_4.setBounds(276, 86, 116, 14);
			panel_DatosGenerales.add(lblNewLabel_4);

			txtVivienda = new JTextField();
			txtVivienda.setBounds(402, 83, 109, 20);
			panel_DatosGenerales.add(txtVivienda);
			txtVivienda.setColumns(10);

			JButton btnBuscaVivienda = new JButton("Buscar");
			btnBuscaVivienda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nuevaViv = Clinica.getInstance().obtenervivienda(txtVivienda.getText());
					if (nuevaViv != null) {
						txtVivienda.setText(nuevaViv.getIdVivienda());
						txtVivienda.setEnabled(false);
					}
				}
			});
			btnBuscaVivienda.setBounds(545, 65, 89, 23);
			panel_DatosGenerales.add(btnBuscaVivienda);

			JButton btnCrearVivienda = new JButton("Crear");
			btnCrearVivienda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegistrarVivienda nueva = new RegistrarVivienda(null, 0);
					nueva.setModal(true);
					nueva.setVisible(true);
					nuevaViv = nueva.getCasaReg();
					if (nuevaViv != null) {
						txtVivienda.setText(nuevaViv.getIdVivienda());
						txtVivienda.setEnabled(false);
						btnCrearVivienda.setVisible(false);
						btnEditarVivienda.setVisible(true);
					}
				}
			});
			btnCrearVivienda.setBounds(545, 101, 89, 23);
			panel_DatosGenerales.add(btnCrearVivienda);

			btnEditarVivienda = new JButton("Editar");
			btnEditarVivienda.setVisible(false);
			btnEditarVivienda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegistrarVivienda nueva = new RegistrarVivienda(nuevaViv, 0);
					nueva.setModal(true);
					nueva.setVisible(true);
				}
			});
			btnEditarVivienda.setEnabled(true);
			btnEditarVivienda.setBounds(545, 106, 89, 23);
			panel_DatosGenerales.add(btnEditarVivienda);

			panel_Medico = new JPanel();
			panel_Medico.setBorder(new TitledBorder(null, "Datos de Medico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_Medico.setBackground(SystemColor.info);
			panel_Medico.setBounds(10, 225, 598, 103);
			panel.add(panel_Medico);
			panel_Medico.setLayout(null);
			panel_Medico.setVisible(false);

			JLabel lblNewLabel_1 = new JLabel("Codigo:");
			lblNewLabel_1.setBounds(10, 25, 46, 14);
			panel_Medico.add(lblNewLabel_1);

			txtCodeMed = new JTextField();
			txtCodeMed.setBounds(65, 22, 77, 20);
			panel_Medico.add(txtCodeMed);
			txtCodeMed.setBackground(SystemColor.info);
			txtCodeMed.setEnabled(false);
			txtCodeMed.setText("Medico -" + Clinica.generadorCodigoidMedico);
			txtCodeMed.setColumns(10);

			JLabel lblEspecialidad = new JLabel("Especialidad:");
			lblEspecialidad.setBounds(10, 74, 97, 14);
			panel_Medico.add(lblEspecialidad);

			txtxEspecialidad = new JTextField();
			txtxEspecialidad.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char key = e.getKeyChar();
					if (!Character.isAlphabetic(key))
						e.consume();
				}
			});
			txtxEspecialidad.setBounds(86, 71, 185, 20);
			panel_Medico.add(txtxEspecialidad);
			txtxEspecialidad.setColumns(10);

			panel_Paciente = new JPanel();
			panel_Paciente.setBorder(new TitledBorder(null, "Datos Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_Paciente.setBackground(SystemColor.info);
			panel_Paciente.setBounds(10, 225, 592, 103);
			panel.add(panel_Paciente);
			panel_Paciente.setLayout(null);
			panel_Paciente.setVisible(false);
			
			JLabel lblNewLabel_5 = new JLabel("Codigo:");
			lblNewLabel_5.setBounds(10, 36, 46, 14);
			panel_Paciente.add(lblNewLabel_5);

			textCodigoPaciente = new JTextField("Paciente - " + Clinica.generadorCodigoPaciente);
			textCodigoPaciente.setBackground(SystemColor.info);
			textCodigoPaciente.setEnabled(false);
			textCodigoPaciente.setBounds(66, 33, 86, 20);
			panel_Paciente.add(textCodigoPaciente);
			textCodigoPaciente.setColumns(10);

			JLabel lblNewLabel_6 = new JLabel("Informacion de emergencia:");
			lblNewLabel_6.setBounds(10, 70, 184, 14);
			panel_Paciente.add(lblNewLabel_6);

			textFieldInfoEmergencia = new JTextField();
			textFieldInfoEmergencia.setBounds(204, 67, 281, 20);
			panel_Paciente.add(textFieldInfoEmergencia);
			textFieldInfoEmergencia.setColumns(10);

			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBounds(612, 255, 64, 73);
			panel.add(lblNewLabel);
			lblNewLabel.setIcon(new ImageIcon(RegistrarGeneral.class.getResource("/imagenes/edificio-del-hospital (2).png")));
		}
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(SystemColor.info);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cedula = txtCedula.getText().trim();
				String nombre = txtNombre.getText().trim();
				String genero = (String) comboBoxGender.getSelectedItem();
				Date fecha = (Date) spnFecha.getValue();
				String telefono = txtTelefono.getText().trim();
				Vivienda vivienda = Clinica.getInstance().obtenervivienda(txtVivienda.getText());
				
				if (cedula.isEmpty() || nombre.isEmpty() || genero.equals("Elegir") || telefono.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!esModificacion) {
					if (rdbtnMedico.isSelected()) {
						Medico aux = new Medico(cedula, nombre, genero, fecha, telefono, vivienda,
								txtCodeMed.getText(), txtxEspecialidad.getText());
						Clinica.getInstance().agregarMedico(aux);
					} else if (rdbtnPaciente.isSelected()) {
						Paciente aux = new Paciente(cedula, nombre, genero, fecha, telefono,
								textCodigoPaciente.getText(), vivienda,
								textFieldInfoEmergencia.getText(), null);
						Clinica.getInstance().agregarPaciente(aux);
					} else {
						JOptionPane.showMessageDialog(null, "Seleccione un rol (Médico o Paciente)", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					clean();
					JOptionPane.showMessageDialog(null, "Registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				} else {
					miPersona.setCedula(cedula);
					miPersona.setFechaNacimiento(fecha);
					miPersona.setGenero(genero);
					miPersona.setNombre(nombre);
					miPersona.setTelefono(telefono);
					miPersona.setViviend(vivienda);
					
					if (miPersona instanceof Paciente && rdbtnPaciente.isSelected()) {
						((Paciente) miPersona).setInfoEmergencia(textFieldInfoEmergencia.getText());
						Clinica.getInstance().modificarPaciente(cedula, (Paciente) miPersona);
					}
					if (miPersona instanceof Medico && rdbtnMedico.isSelected()) {
						((Medico) miPersona).setEspecialidad(txtxEspecialidad.getText());
						Clinica.getInstance().modificarMedico(((Medico) miPersona).getIdMedico(), (Medico) miPersona);
					}
					dispose();
					JOptionPane.showMessageDialog(null, "Actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				}
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
		
		if (miPersona != null) {
			loadPersona();
		}
	}

	public void clean() {
		txtCedula.setText("");
		if (miPersona instanceof Medico) {
			Clinica.generadorCodigoidMedico++;
			txtCodeMed.setText("Medico - " + Clinica.generadorCodigoidMedico);
		} else {
			Clinica.generadorCodigoPaciente++;
			textCodigoPaciente.setText("Paciente - " + Clinica.generadorCodigoPaciente);
		}
		textFieldInfoEmergencia.setText("");
		txtNombre.setText("");
		txtTelefono.setText("");
		txtVivienda.setText("");
		txtxEspecialidad.setText("");
		rdbtnMedico.setSelected(false);
		rdbtnPaciente.setSelected(false);
		panel_Medico.setVisible(false);
		panel_Paciente.setVisible(false);
		txtVivienda.setEnabled(true);
	}
	
	public void loadPersona() {
		txtCedula.setText(miPersona.getCedula());
		txtNombre.setText(miPersona.getNombre());
		txtTelefono.setText(miPersona.getTelefono());
		if (miPersona.getViviend() != null) {
			txtVivienda.setText(miPersona.getViviend().getIdVivienda());
		}
		comboBoxGender.setSelectedItem(miPersona.getGenero());
		
		if (miPersona instanceof Paciente) {
			rdbtnMedico.setSelected(false);
			rdbtnPaciente.setSelected(true);
			panel_Paciente.setVisible(true);
			textCodigoPaciente.setText(((Paciente) miPersona).getIdPaciente());
			textFieldInfoEmergencia.setText(((Paciente) miPersona).getInfoEmergencia());
		}
		if (miPersona instanceof Medico) {
			rdbtnMedico.setSelected(true);
			rdbtnPaciente.setSelected(false);
			panel_Medico.setVisible(true);
			txtCodeMed.setText(((Medico) miPersona).getIdMedico());
			txtxEspecialidad.setText(((Medico) miPersona).getEspecialidad());
		}
	}
}