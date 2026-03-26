package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Usuario;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VerMisUsuarios extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField txtNombre;
	private static DefaultTableModel model;
	private JButton btnBorrar;
	private JButton btnModificar;
	private Usuario selected = null;

	public static void main(String[] args) {
		try {
			VerMisUsuarios dialog = new VerMisUsuarios();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VerMisUsuarios() {
		setTitle("Gestionar Usuarios");
		setBounds(100, 100, 800, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.window);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel ListPanel = new JPanel();
		ListPanel.setBounds(10, 75, 764, 350);
		contentPanel.add(ListPanel);
		ListPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ListPanel.add(scrollPane, BorderLayout.CENTER);

		String[] header = {"ID", "Usuario", "Password", "Rol"};
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
					String idUsuario = table.getValueAt(index, 0).toString();
					selected = Clinica.getInstance().buscarUsuarioPorCodigo(idUsuario);
					System.out.println("Usuario seleccionado: " + selected.getNombreUser() + " - ID: " + idUsuario);
				}
			}
		});
		table.setModel(model);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		JPanel OpcionesPanel = new JPanel();
		OpcionesPanel.setBackground(SystemColor.scrollbar);
		OpcionesPanel.setBounds(10, 11, 764, 59);
		contentPanel.add(OpcionesPanel);
		OpcionesPanel.setLayout(null);

		txtNombre = new JTextField();
		txtNombre.setBounds(53, 28, 653, 20);
		OpcionesPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setBounds(10, 31, 38, 14);
		OpcionesPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Lista de usuarios");
		lblNewLabel_1.setBounds(10, 3, 694, 14);
		OpcionesPanel.add(lblNewLabel_1);

		JButton btnBuscar = new JButton("");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarPorNombre();
			}
		});
		btnBuscar.setIcon(new ImageIcon(VerMisUsuarios.class.getResource("/imagenes/busqueda-de-lupa (1).png")));
		btnBuscar.setBounds(716, 28, 38, 22);
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
					CrearUser modificar = new CrearUser(selected);
					modificar.setModal(true);
					modificar.setVisible(true);
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
							"¿Seguro que desea borrar al usuario " + selected.getNombreUser() + " con ID " + selected.getIdUsuario() + "?",
							"Confirmación", JOptionPane.YES_NO_OPTION);
					if (confirmacion == JOptionPane.YES_OPTION) {
						Clinica.getInstance().borrarUsuario(selected.getIdUsuario());
						cargarDatos();
						btnBorrar.setEnabled(false);
						btnModificar.setEnabled(false);
						selected = null;
						JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
		ArrayList<Usuario> listaUsuarios = Clinica.getInstance().getMisUsuarios();
		model.setRowCount(0);
		for (Usuario usuario : listaUsuarios) {
			model.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombreUser(), usuario.getPassword(), usuario.getRol()});
		}
		System.out.println("Usuarios cargados: " + listaUsuarios.size());
	}

	private void buscarPorNombre() {
		String nombreABuscar = txtNombre.getText().trim();
		boolean encontrado = false;
		model.setRowCount(0);
		ArrayList<Usuario> listaUsuarios = Clinica.getInstance().getMisUsuarios();

		for (Usuario usuario : listaUsuarios) {
			if (usuario.getNombreUser().toLowerCase().contains(nombreABuscar.toLowerCase())) {
				model.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombreUser(), usuario.getPassword(), usuario.getRol()});
				encontrado = true;
			}
		}
		if (!encontrado && !nombreABuscar.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontraron usuarios con ese nombre", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			cargarDatos();
		}
	}
}