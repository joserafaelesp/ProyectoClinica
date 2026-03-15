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
import Logical.Paciente;
import Logical.archivoManager;

public class ListarPaciente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField txtNombre;
	private static DefaultTableModel model;
	private static Object[] row;
	private JButton borrar;
	private Paciente selected = null;

	public static void main(String[] args) {
		try {
			ListarPaciente dialog = new ListarPaciente();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListarPaciente() {
		setTitle("Ver Pacientes");
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


		String[] headear = {"Cedula","Nombre", "Genero"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(headear);
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if(index >= 0) {
					borrar.setEnabled(true);

					String codigoPaciente = table.getValueAt(index, 0).toString();
					selected = Clinica.getInstance().obtenerPacienteById(codigoPaciente);
					System.out.println("Paciente seleccionado para borrar: " + selected);
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

		JLabel lblNewLabel_1 = new JLabel("Lista de Pacientes");
		lblNewLabel_1.setBounds(10, 3, 694, 14);
		OpcionesPanel.add(lblNewLabel_1);

		JButton btnBuscar = new JButton("");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarPorNombre();
			}
		});
		btnBuscar.setIcon(new ImageIcon(ListarMedico.class.getResource("/imagenes/busqueda-de-lupa (1).png")));
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
				borrar.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        int index = table.getSelectedRow();
				        
				        if (index >= 0) {
				            String codigoPaciente = table.getValueAt(index, 0).toString(); // En este caso, la Cédula
				            selected = Clinica.getInstance().obtenerPacienteById(codigoPaciente);

				            if (selected != null) {
				                int confirmacion = JOptionPane.showConfirmDialog(null, 
				                        "¿Seguro que desea borrar al paciente con Cédula: " + codigoPaciente + "?",
				                        "Confirmación", JOptionPane.YES_NO_OPTION);

				                if (confirmacion == JOptionPane.YES_OPTION) {
				                    
				                    // 1. Llamamos al método que vamos a crear en archivoManager
				                    archivoManager.borrarPaciente(selected);
				                    
				                    // 2. ¡Corregido! Ahora recarga el archivo correcto
				                    cargarDatosDesdeArchivo("Paciente.txt"); 
				                    
				                    // 3. Reiniciamos la interfaz
				                    borrar.setEnabled(false);
				                    selected = null; 
				                    
				                    JOptionPane.showMessageDialog(null, "Paciente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				                }
				            }
				        }
				    }
				});		
			}
		}
	}
		
	private void cargarDatosDesdeArchivo(String archivo) {
		ArrayList<Paciente> listaPaciente = archivoManager.leerPacientes();
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.setRowCount(0);

		for (Paciente paciente : listaPaciente) {
			model.addRow(new Object[]{ paciente.getCedula(),paciente.getNombre(),paciente.getGenero()});
		}
	}

	private void buscarPorNombre() {
		String nombreABuscar = txtNombre.getText().trim();
		boolean pacienteEncontrado = false;

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);

		ArrayList<Paciente> listaPaciente = archivoManager.leerPacientes();

		for (Paciente paciente : listaPaciente) {
			if (paciente.getNombre().equalsIgnoreCase(nombreABuscar)) {
				model.addRow(new Object[]{ paciente.getCedula(),paciente.getNombre(),paciente.getGenero()});
				pacienteEncontrado = true;
			}
		}
		if (!pacienteEncontrado) {
			JOptionPane.showMessageDialog(null, "Error No existe", "Busqueda", JOptionPane.ERROR_MESSAGE);
		}
	}
}