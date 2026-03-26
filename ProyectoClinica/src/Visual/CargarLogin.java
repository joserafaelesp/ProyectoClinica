package Visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;

public class CargarLogin extends JFrame {

	private JPanel contentPane;
	public JProgressBar Barra;
	public JLabel lblEsperar;

	Login verLogin = new Login();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CargarLogin frame = new CargarLogin();
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CargarLogin() {
		setAutoRequestFocus(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 461, 272);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		contentPane.add(panel);
		panel.setLayout(null);

		Barra = new JProgressBar();
		Barra.setStringPainted(true);
		Barra.setForeground(new Color(50, 205, 50));
		Barra.setBounds(10, 198, 425, 43);
		panel.add(Barra);

		JLabel lblNewLabel = new JLabel("Clinica S.R.L");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		lblNewLabel.setBounds(128, 11, 198, 48);
		panel.add(lblNewLabel);

		lblEsperar = new JLabel("Porfavor Espere");
		lblEsperar.setBounds(10, 182, 135, 14);
		panel.add(lblEsperar);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(CargarLogin.class.getResource("/imagenes/edificio-del-hospital (3).png")));
		lblNewLabel_1.setBounds(155, 53, 144, 136);
		panel.add(lblNewLabel_1);
	}
}