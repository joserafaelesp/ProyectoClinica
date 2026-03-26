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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Vivienda;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ListarVivienda extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField txtID;
	private static DefaultTableModel model;
	private JButton btnBorrar;
	private JButton btnModificar;
	private Vivienda selected = null;

	public static void main(String[] args) {
		try {
			ListarVivienda dialog = new ListarVivienda();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarVivienda() {
		setTitle("Ver Viviendas");
		setBounds(100, 100, 750, 433);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.window);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel ListPanel = new JPanel();
		ListPanel.setBounds(10, 75, 714, 275);
		contentPanel.add(ListPanel);
		ListPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ListPanel.add(scrollPane, BorderLayout.CENTER);

		String[] header = {"ID", "Teléfono", "Dirección"};
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
					String idVivienda = table.getValueAt(index, 0).toString();
					selected = Clinica.getInstance().obtenervivienda(idVivienda);
				}
			}
		});
		table.setModel(model);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		JPanel OpcionesPanel = new JPanel();
		OpcionesPanel.setBackground(SystemColor.scrollbar);
		OpcionesPanel.setBounds(10, 11, 714, 59);
		contentPanel.add(OpcionesPanel);
		OpcionesPanel.setLayout(null);

		txtID = new JTextField();
		txtID.setBounds(53, 28, 603, 20);
		OpcionesPanel.add(txtID);
		txtID.setColumns(10);

		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setBounds(0, 31, 53, 14);
		OpcionesPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Lista de Viviendas");
		lblNewLabel_1.setBounds(10, 3, 694, 14);
		OpcionesPanel.add(lblNewLabel_1);

		JButton btnBuscar = new JButton("");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarPorId();
			}
		});
		btnBuscar.setIcon(new ImageIcon(ListarVivienda.class.getResource("/imagenes/busqueda-de-lupa (1).png")));
		btnBuscar.setBounds(666, 28, 38, 22);
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
					RegistrarVivienda modificar = new RegistrarVivienda(selected, 0);
					modificar.setModal(true);
					modificar.setVisible(true);
					cargarDatos();
					btnBorrar.setEnabled(false);
					btnModificar.setEnabled(false);
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
							"¿Seguro que desea borrar la vivienda con ID: " + selected.getIdVivienda() + "?",
							"Confirmación", JOptionPane.YES_NO_OPTION);
					if (confirmacion == JOptionPane.YES_OPTION) {
						Clinica.getInstance().borrarVivienda(selected.getIdVivienda());
						cargarDatos();
						btnBorrar.setEnabled(false);
						btnModificar.setEnabled(false);
						selected = null;
						JOptionPane.showMessageDialog(null, "Vivienda eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
		ArrayList<Vivienda> listaVivienda = Clinica.getInstance().getMisViviendas();
		model.setRowCount(0);
		for (Vivienda vivienda : listaVivienda) {
			model.addRow(new Object[]{vivienda.getIdVivienda(), vivienda.getTelefono(), vivienda.getDireccion()});
		}
	}

	private void buscarPorId() {
		String idABuscar = txtID.getText().trim();
		boolean encontrado = false;
		model.setRowCount(0);
		ArrayList<Vivienda> listaVivienda = Clinica.getInstance().getMisViviendas();

		for (Vivienda vivienda : listaVivienda) {
			if (vivienda.getIdVivienda().toLowerCase().contains(idABuscar.toLowerCase())) {
				model.addRow(new Object[]{vivienda.getIdVivienda(), vivienda.getTelefono(), vivienda.getDireccion()});
				encontrado = true;
			}
		}
		if (!encontrado && !idABuscar.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontraron viviendas con ese ID", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			cargarDatos();
		}
	}
}