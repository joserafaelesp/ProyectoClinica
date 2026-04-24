package Visual;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Usuario;

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

        JLabel lblContra = new JLabel("Contraseña:");
        lblContra.setBounds(227, 118, 85, 14);
        contentPane.add(lblContra);

        txtUser = new JTextField();
        txtUser.setBounds(227, 87, 214, 20);
        contentPane.add(txtUser);
        txtUser.setColumns(10);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(227, 68, 65, 14);
        contentPane.add(lblUser);

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.activeCaption);
        panel.setBounds(0, 0, 199, 261);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblImagen = new JLabel("");
        try {
            lblImagen.setIcon(new ImageIcon(
                Login.class.getResource("/imagenes/edificio-del-hospital (3).png")));
        } catch (Exception ex) {
            System.out.println("Imagen no encontrada");
        }
        lblImagen.setBounds(36, 74, 128, 145);
        panel.add(lblImagen);

        JLabel lblClinica = new JLabel("Clinica S.R.L");
        lblClinica.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblClinica.setBounds(10, 11, 179, 48);
        panel.add(lblClinica);

        JLabel lblLogin = new JLabel("LOGIN");
        lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 28));
        lblLogin.setBounds(278, 0, 96, 66);
        contentPane.add(lblLogin);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        btnSalir.setBounds(237, 174, 89, 23);
        contentPane.add(btnSalir);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String nombreUsuario = txtUser.getText().trim();
                String claveFinal    = new String(JpassContra.getPassword()).trim();

                if (nombreUsuario.isEmpty() || claveFinal.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Por favor ingresa usuario y contraseña",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario usuarioEncontrado =
                    Clinica.getInstance().autenticarUsuario(nombreUsuario, claveFinal);

                if (usuarioEncontrado != null) {
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Bienvenido " + usuarioEncontrado.getNombreUser(),
                        "Bienvenido", JOptionPane.INFORMATION_MESSAGE);

                    PrincipalVisual main = new PrincipalVisual(usuarioEncontrado);

                    // ════════════════════════════════════════════════
                    // PERMISOS POR ROL
                    // ════════════════════════════════════════════════

                    if (usuarioEncontrado.esAdministrador()) {
                        // ── ADMINISTRADOR ────────────────────────────
                        // Solo gestiona usuarios y configuración general
                        // No hace consultas ni citas (eso es del médico)
                        main.mConsultas.setVisible(false);
                        main.mCitas.setVisible(false);

                    } else if (usuarioEncontrado.esSecretaria()) {
                        // ── SECRETARIA ───────────────────────────────
                        // Solo puede:
                        //   - Registrar pacientes
                        //   - Ver/modificar/borrar pacientes
                        //   - Ver médicos (solo lectura)
                        // NO puede: consultas, citas, vacunas,
                        //           enfermedades, viviendas, usuarios

                        main.mConsultas.setVisible(false);
                        main.mCitas.setVisible(false);
                        main.mUSER.setVisible(false);

                        // En REGISTROS: solo "Crear Persona"
                        // pero RegistrarGeneral solo mostrará opción Paciente
                        main.crearVacuna.setVisible(false);
                        main.crearEnfermedad.setVisible(false);
                        main.crearVivienda.setVisible(false);

                        // En INVENTARIO: solo lista pacientes y lista médicos
                        main.listadoVacuna.setVisible(false);
                        main.listadoEnfermedad.setVisible(false);
                        main.listaVivienda.setVisible(false);

                        // Pasar modo secretaria al listado de médicos
                        // para que no tenga botones de modificar/borrar
                        main.setModoSecretaria(true);

                    } else if (usuarioEncontrado.esMedico()) {
                        // ── MÉDICO ───────────────────────────────────
                        // Puede: consultas, ver pacientes, ver enfermedades/vacunas
                        // NO puede: citas, viviendas, usuarios
                        main.mCitas.setVisible(false);
                        main.listaVivienda.setVisible(false);
                        main.crearEnfermedad.setVisible(false);
                        main.crearVacuna.setVisible(false);
                        main.mUSER.setVisible(false);
                    }

                    main.lblUser.setText("  " + usuarioEncontrado.getNombreUser()
                        + " [" + usuarioEncontrado.getRol() + "]  ");

                    dim = getToolkit().getScreenSize();
                    main.setResizable(false);
                    main.setSize(dim.width, dim.height - 40);
                    main.setLocationRelativeTo(null);
                    main.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null,
                        "Usuario o contraseña incorrectos",
                        "Error de acceso", JOptionPane.ERROR_MESSAGE);
                    txtUser.setText("");
                    JpassContra.setText("");
                    txtUser.requestFocus();
                }
            }
        });
        btnEntrar.setBounds(331, 174, 89, 23);
        contentPane.add(btnEntrar);
    }
}