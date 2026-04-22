package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Usuario; // <-- NUEVO: Importamos la clase Usuario

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.ImageIcon;

public class HacerConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodMed;
	private JTextField txtCodePaciente;
	private JSpinner spnFecha;
	private JTextField txtConsulta;
	private JTable tableEnfermedadNoSelected;
	private static DefaultTableModel modeloNoSelected;
	private JButton btnAgregar;
	private ArrayList<Enfermedad> enfermedadesSelected;
	private JTextField txtEnfermedad;
	private Enfermedad consultEnfermedad = null;
	
	// <-- NUEVO: Variable para recibir al usuario desde el menú
	private Usuario usuarioActual; 

	public static void main(String[] args) {
		try {
			// <-- NUEVO: Se envía null para pruebas
			HacerConsulta dialog = new HacerConsulta(null); 
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// <-- NUEVO: El constructor ahora recibe al Usuario
	public HacerConsulta(Usuario user) {
		this.usuarioActual = user; // Guardamos el usuario
		enfermedadesSelected = new ArrayList<>();
		setTitle("Hacer Consulta");
		setBounds(100, 100, 600, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.info);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("ID Medico:");
		lblNewLabel_1.setBounds(269, 33, 80, 14);
		contentPanel.add(lblNewLabel_1);
		
		txtCodMed = new JTextField();
		txtCodMed.setBounds(359, 30, 100, 20);
		contentPanel.add(txtCodMed);
		txtCodMed.setColumns(10);
		
		// <-- NUEVO: Si el usuario es médico, autocompleta su ID y bloquea el campo
		if (usuarioActual != null && usuarioActual.esMedico()) {
			txtCodMed.setText(usuarioActual.getIdUsuario());
			txtCodMed.setEnabled(false);
		}
		
		JLabel lblNewLabel_2 = new JLabel("ID Paciente:");
		lblNewLabel_2.setBounds(10, 66, 80, 14);
		contentPanel.add(lblNewLabel_2);
		
		txtCodePaciente = new JTextField();
		txtCodePaciente.setBounds(100, 63, 100, 20);
		contentPanel.add(txtCodePaciente);
		txtCodePaciente.setColumns(10);
		
		spnFecha = new JSpinner();
		spnFecha.setBounds(359, 61, 123, 20);
		spnFecha.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		contentPanel.add(spnFecha);
		
		JLabel lblNewLabel_3 = new JLabel("Fecha:");
		lblNewLabel_3.setBounds(269, 66, 80, 14);
		contentPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Codigo Consulta:");
		lblNewLabel_4.setBounds(10, 30, 100, 14);
		contentPanel.add(lblNewLabel_4);
		
		txtConsulta = new JTextField("Consulta - " + Clinica.generadorCodigoConsulta);
		txtConsulta.setEnabled(false);
		txtConsulta.setBounds(100, 27, 100, 20);
		contentPanel.add(txtConsulta);
		txtConsulta.setColumns(10);
		
		JPanel panel_EnfermedadNoSelected = new JPanel();
		panel_EnfermedadNoSelected.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Enfermedades", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_EnfermedadNoSelected.setBounds(10, 117, 250, 200);
		contentPanel.add(panel_EnfermedadNoSelected);
		panel_EnfermedadNoSelected.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_EnfermedadNoSelected.add(scrollPane, BorderLayout.CENTER);
		
		modeloNoSelected = new DefaultTableModel();
		String headers[] = {"Nombre", "Codigo"};
		modeloNoSelected.setColumnIdentifiers(headers);
		tableEnfermedadNoSelected = new JTable();
		tableEnfermedadNoSelected.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(tableEnfermedadNoSelected);
		tableEnfermedadNoSelected.setModel(modeloNoSelected);
		
		btnAgregar = new JButton("Agregar >");
		btnAgregar.setEnabled(false);
		btnAgregar.setBounds(270, 180, 80, 23);
		contentPanel.add(btnAgregar);
		
		JLabel lblNewLabel_5 = new JLabel("ID Enfermedad:");
		lblNewLabel_5.setBounds(10, 330, 90, 14);
		contentPanel.add(lblNewLabel_5);
		
		txtEnfermedad = new JTextField();
		txtEnfermedad.setBounds(100, 327, 100, 20);
		contentPanel.add(txtEnfermedad);
		txtEnfermedad.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultEnfermedad = Clinica.getInstance().obtenerEnfermedadById(txtEnfermedad.getText());
				if (consultEnfermedad != null) {
					btnAgregar.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Enfermedad encontrada: " + consultEnfermedad.getNombreEnfermedad());
				} else {
					JOptionPane.showMessageDialog(null, "Enfermedad no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBuscar.setBounds(210, 326, 80, 23);
		contentPanel.add(btnBuscar);
		
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (consultEnfermedad != null && !enfermedadesSelected.contains(consultEnfermedad)) {
					enfermedadesSelected.add(consultEnfermedad);
					btnAgregar.setEnabled(false);
					txtEnfermedad.setText("");
					JOptionPane.showMessageDialog(null, "Enfermedad agregada a la consulta");
				}
			}
		});
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(SystemColor.info);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idMedico = txtCodMed.getText().trim();
				String idPaciente = txtCodePaciente.getText().trim();
				
				if (idMedico.isEmpty() || idPaciente.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Consultas consultaReg = new Consultas(txtConsulta.getText(), (Date) spnFecha.getValue(),
						enfermedadesSelected,
						Clinica.getInstance().obtenerMedicoById(idMedico),
						Clinica.getInstance().obtenerPacienteById(idPaciente));
				Clinica.getInstance().agregarConsulta(consultaReg);
				clean();
				JOptionPane.showMessageDialog(null, "Consulta registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
		
		loadTable();
	}
	
	public void loadTable() {
		modeloNoSelected.setRowCount(0);
		for (Enfermedad enfermedad : Clinica.getInstance().getMisEnfermedades()) {
			modeloNoSelected.addRow(new Object[]{enfermedad.getNombreEnfermedad(), enfermedad.getIdEnfermedad()});
		}
	}
	
	public void clean() {
		txtCodePaciente.setText("");
		txtEnfermedad.setText("");
		enfermedadesSelected.clear();
		Clinica.generadorCodigoConsulta++;
		txtConsulta.setText("Consulta - " + Clinica.generadorCodigoConsulta);
		
		// Limpiamos el médico solo si NO es un médico usando el sistema
		if (usuarioActual == null || !usuarioActual.esMedico()) {
			txtCodMed.setText("");
		}
	}
}