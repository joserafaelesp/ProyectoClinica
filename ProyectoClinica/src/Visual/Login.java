package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Usuario;

import java.awt.SystemColor;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JPasswordField JpassContra;
	private JTextField txtUser;
	private Dimension dim;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 467, 260);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JpassContra = new JPasswordField();
		JpassContra.setBounds(227, 131, 214, 20);
		contentPane.add(JpassContra);

		JLabel lblNewLabel = new JLabel("Contrase\u00F1a:");
		lblNewLabel.setBounds(227, 118, 85, 14);
		contentPane.add(lblNewLabel);

		txtUser = new JTextField();
		txtUser.setBounds(227, 87, 214, 20);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Usuario:");
		lblNewLabel_1.setBounds(227, 68, 65, 14);
		contentPane.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setBounds(0, 0, 199, 261);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Login.class.getResource("/imagenes/edificio-del-hospital (3).png")));
		lblNewLabel_2.setBounds(36, 74, 128, 145);
		panel.add(lblNewLabel_2);

		JLabel label = new JLabel("Clinica S.R.L");
		label.setFont(new Font("Segoe UI", Font.BOLD, 28));
		label.setBounds(10, 11, 179, 48);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("LOGIN");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel_3.setBounds(278, 0, 96, 66);
		contentPane.add(lblNewLabel_3);

		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnSalir.setBounds(237, 174, 89, 23);
		contentPane.add(btnSalir);

		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Usuario> usuarios = Clinica.getInstance().getMisUsuarios();
				char[] clave = JpassContra.getPassword();
				String claveFinal = new String(clave);
				String nombreUsuario = txtUser.getText();
				Usuario usuarioEncontrado = null;

				for (Usuario usuario : usuarios) {
					if (usuario.getNombreUser().equals(nombreUsuario) && usuario.autenticar(claveFinal)) {
						usuarioEncontrado = usuario;
						break;
					}
				}

				if (usuarioEncontrado != null) {
					dispose();
					JOptionPane.showMessageDialog(null, "Bienvenido a Clinica S.R.L", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
					PrincipalVisual main = new PrincipalVisual();

					if (usuarioEncontrado.esAdministrador()) {
						main.mConsultas.setVisible(false);
						main.mCitas.setVisible(false);
					} else if (usuarioEncontrado.esSecretaria()) {
						main.mConsultas.setVisible(false);
						main.listadoEnfermedad.setVisible(false);
						main.listadoVacuna.setVisible(false);
						main.crearEnfermedad.setVisible(false);
						main.crearVacuna.setVisible(false);
						main.crearVivienda.setVisible(false);
						main.mRegistro.setVisible(true);
						main.listaVivienda.setVisible(false);
						main.mUSER.setVisible(false);
					} else if (usuarioEncontrado.esMedico()) {
						main.mCitas.setVisible(false);
						main.listaVivienda.setVisible(false);
						main.crearEnfermedad.setVisible(false);
						main.crearVacuna.setVisible(false);
						main.mUSER.setVisible(false);
					}

					main.lblUser.setText(usuarioEncontrado.getNombreUser());
					dim = getToolkit().getScreenSize();
					main.setResizable(false);
					main.setSize(dim.width, dim.height - 40);
					main.setLocationRelativeTo(null);
					main.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "DATOS ERRONEOS", "ERROR", JOptionPane.ERROR_MESSAGE);
					txtUser.setText("");
					JpassContra.setText("");
				}
			}
		});
		btnEntrar.setBounds(331, 174, 89, 23);
		contentPane.add(btnEntrar);
	}
}