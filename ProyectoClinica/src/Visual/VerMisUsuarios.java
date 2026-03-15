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
import Logical.archivoManager;

import javax.swing.JScrollBar;
import javax.swing.BoxLayout;
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
	private static Object[] row;
	private JButton borrar;
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

	/**
	 * Create the dialog.
	 */
	public VerMisUsuarios() {
		setTitle("Ver usuarios");
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


		String[] headear = {"ID","Usuario","Password", "Rol"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(headear);
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if(index >= 0) {
					borrar.setEnabled(true);

					String codigoUsuario = table.getValueAt(index, 0).toString();
					selected = Clinica.getInstance().buscarUsuarioPorCodigo(codigoUsuario);
					System.out.println("Usuario seleccionado para borrar: " + selected);
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

		txtNombre = new JTextField();
		txtNombre.setBounds(53, 28, 603, 20);
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
		btnBuscar.setBounds(666, 28, 38, 22);
		OpcionesPanel.add(btnBuscar);
		{
			
				JPanel buttonPane = new JPanel();
				buttonPane.setBackground(SystemColor.activeCaption);
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					borrar = new JButton("Borrar");
					borrar.setEnabled(false);
					
					// Lógica principal de borrado
					borrar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int index = table.getSelectedRow();
							
							if (index >= 0) {
								// 1. Obtener el ID de la tabla
								String codigoUsuario = table.getValueAt(index, 0).toString();
								
								// 2. Buscar el objeto en la lista lógica
								selected = Clinica.getInstance().buscarUsuarioPorCodigo(codigoUsuario);

								if (selected != null) {
									int confirmacion = JOptionPane.showConfirmDialog(null, 
											"¿Seguro que desea borrar al usuario con ID: " + codigoUsuario + "?",
											"Confirmación", JOptionPane.YES_NO_OPTION);

									if (confirmacion == JOptionPane.YES_OPTION) {
										
										// 3. Borrar del archivo (y asegúrate de que también se borre de Clinica.getInstance() si tienes una lista en memoria)
										archivoManager.borrarUsuario(selected);
										
										// 4. Recargar los datos visuales
										cargarDatosDesdeArchivo("usuarios.txt");
										
										// 5. Reiniciar la UI y la variable
										borrar.setEnabled(false);
										selected = null; 
										
										JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
									}
								} else {
									JOptionPane.showMessageDialog(null, "Error: No se encontró el usuario en la base de datos lógica.", "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					});
					borrar.setActionCommand("OK");
					buttonPane.add(borrar);
					getRootPane().setDefaultButton(borrar);
				}
				{
					JButton cancelButton = new JButton("Salir");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}

			cargarDatosDesdeArchivo("usuarios.txt");
		}


	private void cargarDatosDesdeArchivo(String archivo) {
		ArrayList<Usuario> listaUsuarios = archivoManager.LeerUsuario();
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.setRowCount(0);

		for (Usuario usuario : listaUsuarios) {
			model.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombreUser(), usuario.getPassword(), usuario.getRol()});
		}
	}

	private void buscarPorNombre() {
		String nombreABuscar = txtNombre.getText().trim();
		boolean usuarioEncontrado = false;

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);

		ArrayList<Usuario> listaUsuarios = archivoManager.LeerUsuario();

		for (Usuario usuario : listaUsuarios) {
			if (usuario.getNombreUser().equalsIgnoreCase(nombreABuscar)) {
				model.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombreUser(), usuario.getPassword(),usuario.getRol()});
				usuarioEncontrado = true;
			}
		}
		if (!usuarioEncontrado) {
			JOptionPane.showMessageDialog(null, "Error No existe", "Busqueda", JOptionPane.ERROR_MESSAGE);
		}
	}
}
