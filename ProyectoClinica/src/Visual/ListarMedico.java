package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Medico;

public class ListarMedico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField txtNombre;
	private static DefaultTableModel model;
	private JButton btnBorrar;
	private JButton btnModificar;
	private Medico selected = null;

	public static void main(String[] args) {
		try {
			ListarMedico dialog = new ListarMedico();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarMedico() {
		setTitle("Ver Medicos");
		setBounds(100, 100, 850, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.window);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel ListPanel = new JPanel();
		ListPanel.setBounds(10, 75, 814, 350);
		contentPanel.add(ListPanel);
		ListPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ListPanel.add(scrollPane, BorderLayout.CENTER);

		String[] header = {"Cedula", "Nombre", "Genero", "Fecha Nac", "Telefono", "ID Medico", "Especialidad"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(header);
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					btnBorrar.setEnabled(true);
					btnModificar.setEnabled(true);
					// OBTENER EL ID MEDICO (columna 5)
					String idMedico = table.getValueAt(index, 5).toString();
					selected = Clinica.getInstance().obtenerMedicoById(idMedico);
					System.out.println("Médico seleccionado: " + selected.getNombre() + " - ID: " + idMedico);
				}
			}
		});
		table.setModel(model);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		JPanel OpcionesPanel = new JPanel();
		OpcionesPanel.setBackground(SystemColor.scrollbar);
		OpcionesPanel.setBounds(10, 11, 814, 59);
		contentPanel.add(OpcionesPanel);
		OpcionesPanel.setLayout(null);

		txtNombre = new JTextField();
		txtNombre.setBounds(53, 28, 703, 20);
		OpcionesPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setBounds(10, 31, 38, 14);
		OpcionesPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Lista de Medicos");
		lblNewLabel_1.setBounds(10, 3, 694, 14);
		OpcionesPanel.add(lblNewLabel_1);

		JButton btnBuscar = new JButton("");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarPorNombre();
			}
		});
		btnBuscar.setIcon(new ImageIcon(ListarMedico.class.getResource("/imagenes/busqueda-de-lupa (1).png")));
		btnBuscar.setBounds(766, 28, 38, 22);
		OpcionesPanel.add(btnBuscar);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(SystemColor.activeCaption);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null) {
					RegistrarGeneral modificar = new RegistrarGeneral(selected, 0);
					modificar.setModal(true);
					modificar.setVisible(true);
					// Recargar datos después de modificar
					cargarDatos();
					btnBorrar.setEnabled(false);
					btnModificar.setEnabled(false);
					selected = null;
				}
			}
		});
		buttonPane.add(btnModificar);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setEnabled(false);
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null) {
					int confirmacion = JOptionPane.showConfirmDialog(null, 
							"¿Seguro que desea borrar al médico " + selected.getNombre() + " con ID " + selected.getIdMedico() + "?",
							"Confirmación", JOptionPane.YES_NO_OPTION);
					if (confirmacion == JOptionPane.YES_OPTION) {
						Clinica.getInstance().borrarMedico(selected.getIdMedico());
						// Recargar datos después de borrar
						cargarDatos();
						btnBorrar.setEnabled(false);
						btnModificar.setEnabled(false);
						selected = null;
						JOptionPane.showMessageDialog(null, "Médico eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		buttonPane.add(btnBorrar);
		
		JButton cancelButton = new JButton("Salir");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);

		cargarDatos();
	}

	private void cargarDatos() {
		ArrayList<Medico> listaMedicos = Clinica.getInstance().getMisMedico();
		model.setRowCount(0);
		for (Medico medico : listaMedicos) {
			model.addRow(new Object[]{
				medico.getCedula(), 
				medico.getNombre(), 
				medico.getGenero(), 
				medico.getFechaNacimiento(), 
				medico.getTelefono(), 
				medico.getIdMedico(), 
				medico.getEspecialidad()
			});
		}
		System.out.println("Médicos cargados: " + listaMedicos.size());
	}

	private void buscarPorNombre() {
		String nombreABuscar = txtNombre.getText().trim();
		boolean encontrado = false;
		model.setRowCount(0);
		ArrayList<Medico> listaMedicos = Clinica.getInstance().getMisMedico();

		for (Medico medico : listaMedicos) {
			if (medico.getNombre().toLowerCase().contains(nombreABuscar.toLowerCase())) {
				model.addRow(new Object[]{
					medico.getCedula(), medico.getNombre(), medico.getGenero(), 
					medico.getFechaNacimiento(), medico.getTelefono(), 
					medico.getIdMedico(), medico.getEspecialidad()
				});
				encontrado = true;
			}
		}
		if (!encontrado && !nombreABuscar.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontraron médicos con ese nombre", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			cargarDatos();
		}
	}
}