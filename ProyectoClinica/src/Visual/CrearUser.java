package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Logical.Clinica;
import Logical.Usuario;

public class CrearUser extends JDialog {

    private final JPanel      contentPanel  = new JPanel();
    private JTextField        txtCodigo;
    private JTextField        txtUsername;
    private JTextField        txtPassword;
    private JComboBox<String> cbxRol;
    private JLabel            lblNotaMedico;
    private Usuario           usuarioExistente;
    private boolean           esModificacion  = false;
    private boolean           medicoCompletado = false; // control 2do paso médico

    public CrearUser() { this(null); }

    public CrearUser(Usuario usuario) {
        usuarioExistente = usuario;
        esModificacion   = (usuario != null);

        setTitle(esModificacion ? "Modificar Usuario" : "Crear Usuario");
        setBounds(100, 100, 390, 320);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.info);
        panel.setBorder(new TitledBorder(null,
            esModificacion ? "Datos del Usuario" : "Nuevo Usuario",
            TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // ── Rol ──────────────────────────────────────────────────
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(20, 30, 80, 14);
        panel.add(lblRol);

        cbxRol = new JComboBox<>(new String[]{
            "Seleccionar rol", "Administrador", "Medico", "Secretaria"});
        cbxRol.setBounds(110, 27, 200, 22);
        panel.add(cbxRol);

        // ── ID automático por rol ────────────────────────────────
        JLabel lblCodigo = new JLabel("ID Sistema:");
        lblCodigo.setBounds(20, 65, 80, 14);
        panel.add(lblCodigo);

        txtCodigo = new JTextField("(seleccione un rol)");
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(new Color(220, 220, 220));
        txtCodigo.setForeground(Color.DARK_GRAY);
        txtCodigo.setBounds(110, 62, 200, 20);
        panel.add(txtCodigo);

        // ── Username ─────────────────────────────────────────────
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 100, 80, 14);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(110, 97, 200, 20);
        txtUsername.setToolTipText("Nombre con el que iniciará sesión");
        panel.add(txtUsername);

        // ── Password ─────────────────────────────────────────────
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(20, 135, 80, 14);
        panel.add(lblPass);

        txtPassword = new JTextField();
        txtPassword.setBounds(110, 132, 200, 20);
        panel.add(txtPassword);

        // ── Nota para médico ─────────────────────────────────────
        lblNotaMedico = new JLabel(
            "<html><span style='color:#b05000'><b>⚠ Médico:</b> se pedirán datos<br>"
            + "&nbsp;&nbsp;&nbsp;adicionales en el siguiente paso.</span></html>");
        lblNotaMedico.setBounds(20, 162, 320, 35);
        lblNotaMedico.setVisible(false);
        panel.add(lblNotaMedico);

        JLabel lblNota = new JLabel(
            "<html><i>El ID se asigna automáticamente por rol</i></html>");
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(20, 205, 320, 14);
        panel.add(lblNota);

        // ── Cargar si es modificación ─────────────────────────────
        // IMPORTANTE: cargar datos ANTES de agregar el ActionListener
        // para que setSelectedItem no dispare actualizarSegunRol()
        if (esModificacion) {
            txtCodigo.setText(usuario.getIdUsuario());
            txtUsername.setText(usuario.getNombreUser());
            txtPassword.setText(usuario.getPassword());
            cbxRol.setSelectedItem(usuario.getRol());
            cbxRol.setEnabled(false);
            // NO agregar listener en modificación — ID no cambia
        } else {
            // Solo en creación: listener actualiza ID según rol
            cbxRol.addActionListener(e -> actualizarSegunRol());
        }

        // ── Botones ───────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton(
            esModificacion ? "Guardar cambios" : "Crear Usuario");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id       = txtCodigo.getText().trim();
                String username = txtUsername.getText().trim();
                String pass     = txtPassword.getText().trim();
                String rol      = cbxRol.getSelectedItem().toString();

                // ── Validaciones ─────────────────────────────────
                if (!esModificacion && rol.equals("Seleccionar rol")) {
                    JOptionPane.showMessageDialog(null,
                        "Seleccione un rol para el usuario",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "El Username no puede estar vacío",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "El Password no puede estar vacío",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario userObj = new Usuario(id, username, pass, rol);

                if (esModificacion) {
                    // ── MODIFICAR: actualiza directamente en BD ──
                    Clinica.getInstance().modificarUsuario(id, userObj);
                    JOptionPane.showMessageDialog(null,
                        "Usuario '" + username + "' modificado correctamente.",
                        "Modificación", JOptionPane.INFORMATION_MESSAGE);
                    medicoCompletado = true; // permitir cierre
                    dispose();

                } else if (rol.equals("Medico")) {
                    // ── MÉDICO: paso 1 guardar en USUARIO ────────
                    Clinica.getInstance().agregarUsuario(userObj);

                    JOptionPane.showMessageDialog(null,
                        "Credenciales creadas para el médico.\n"
                        + "Complete ahora los datos personales.",
                        "Paso 2 de 2", JOptionPane.INFORMATION_MESSAGE);

                    // Abrir RegistrarGeneral con médico preseleccionado
                    RegistrarGeneral regMedico = new RegistrarGeneral(null, 0);
                    regMedico.setModal(true);
                    regMedico.preseleccionarMedico(id);

                    // Interceptar cierre para verificar que completó el paso 2
                    regMedico.addWindowListener(new WindowAdapter() {
                        public void windowClosed(WindowEvent we) {
                            medicoCompletado = true;
                        }
                    });

                    dispose(); // cerrar CrearUser antes de abrir RegistrarGeneral
                    regMedico.setVisible(true);

                } else {
                    // ── ADMIN / SECRETARIA: solo en USUARIO ──────
                    Clinica.getInstance().agregarUsuario(userObj);
                    JOptionPane.showMessageDialog(null,
                        "Usuario '" + username + "' creado correctamente.\n"
                        + "Rol: " + rol + "   ID: " + id,
                        "Registro", JOptionPane.INFORMATION_MESSAGE);
                    medicoCompletado = true;
                    dispose();
                }
            }
        });
        getRootPane().setDefaultButton(okButton);
        buttonPane.add(okButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    // ── Actualiza el ID según el rol seleccionado ────────────────
    private void actualizarSegunRol() {
        String rol = cbxRol.getSelectedItem().toString();
        switch (rol) {
            case "Administrador":
                txtCodigo.setText("ADM-" + (Clinica.getInstance()
                    .obtenerMaxUsuarioPorPrefijo("ADM-") + 1));
                lblNotaMedico.setVisible(false);
                break;
            case "Medico":
                txtCodigo.setText("MED-" + (Clinica.getInstance()
                    .obtenerMaxUsuarioPorPrefijo("MED-") + 1));
                lblNotaMedico.setVisible(true);
                break;
            case "Secretaria":
                txtCodigo.setText("SEC-" + (Clinica.getInstance()
                    .obtenerMaxUsuarioPorPrefijo("SEC-") + 1));
                lblNotaMedico.setVisible(false);
                break;
            default:
                txtCodigo.setText("(seleccione un rol)");
                lblNotaMedico.setVisible(false);
                break;
        }
    }
}