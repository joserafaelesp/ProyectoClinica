package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Enfermedad;

public class RegistrarEnfermedad extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtSintomas;
    private JButton    btnRegistrar;
    private Enfermedad miEnfermedad;
    private boolean    esModificacion = false;

    public RegistrarEnfermedad(Enfermedad disease, int index) {
        miEnfermedad   = disease;
        esModificacion = (disease != null);

        setTitle(esModificacion ? "Actualizar Enfermedad" : "Registrar Enfermedad");
        setBounds(100, 100, 450, 300);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.info);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // imagen decorativa
        JLabel lblImg = new JLabel();
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarEnfermedad.class.getResource(
                    "/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) { /* opcional */ }
        lblImg.setBounds(356, 152, 68, 65);
        contentPanel.add(lblImg);

        // Código
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(10, 11, 90, 14);
        contentPanel.add(lblCodigo);

        txtCodigo = new JTextField(
            "Enfermedad-" + Clinica.generadorCodigoEnfermedad);
        txtCodigo.setBackground(SystemColor.info);
        txtCodigo.setEnabled(false);
        txtCodigo.setBounds(106, 8, 130, 20);
        contentPanel.add(txtCodigo);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 42, 90, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        txtNombre.setBounds(106, 39, 200, 20);
        contentPanel.add(txtNombre);

        // Síntomas
        JLabel lblSintomas = new JLabel("Sintomas:");
        lblSintomas.setBounds(10, 78, 90, 14);
        contentPanel.add(lblSintomas);

        txtSintomas = new JTextField();
        txtSintomas.setBounds(106, 75, 219, 65);
        contentPanel.add(txtSintomas);

        // ── Botones ──────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.info);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre   = txtNombre.getText().trim();
                String sintomas = txtSintomas.getText().trim();

                if (nombre.isEmpty() || sintomas.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!esModificacion) {
                    // Constructor correcto: (id, nombre, sintomas, gravedad)
                    Enfermedad aux = new Enfermedad(
                        txtCodigo.getText(), nombre, sintomas, null);
                    Clinica.getInstance().agregarEnfermedad(aux);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Enfermedad registrada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miEnfermedad.setNombreEnfermedad(nombre);
                    miEnfermedad.setSintomas(sintomas);
                    Clinica.getInstance().modificarEnfermedad(
                        miEnfermedad.getIdEnfermedad(), miEnfermedad);
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Enfermedad actualizada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPane.add(btnRegistrar);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        if (miEnfermedad != null) loadEnfermedad();
    }

    public void clean() {
        Clinica.generadorCodigoEnfermedad++;
        txtCodigo.setText("Enfermedad-" + Clinica.generadorCodigoEnfermedad);
        txtNombre.setText("");
        txtSintomas.setText("");
    }

    public void loadEnfermedad() {
        txtCodigo.setText(miEnfermedad.getIdEnfermedad());
        txtNombre.setText(miEnfermedad.getNombreEnfermedad());
        txtSintomas.setText(miEnfermedad.getSintomas());
    }
}