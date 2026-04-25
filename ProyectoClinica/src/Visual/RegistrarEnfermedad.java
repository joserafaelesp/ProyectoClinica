package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Enfermedad;
import Logical.Gravedadenfermedad;

public class RegistrarEnfermedad extends JDialog {

    private final JPanel                  contentPanel = new JPanel();
    private JTextField                    txtCodigo;
    private JTextField                    txtNombre;
    private JTextField                    txtSintomas;
    private JComboBox<String>             comboGravedad;
    private JButton                       btnRegistrar;
    private Enfermedad                    miEnfermedad;
    private boolean                       esModificacion = false;
    private ArrayList<Gravedadenfermedad> listaGravedad;

    private static final Color FIXED_BG = new Color(230, 240, 255);
    private static final Color FIXED_FG = new Color(30, 60, 120);

    public RegistrarEnfermedad(Enfermedad disease, int index) {
        miEnfermedad   = disease;
        esModificacion = (disease != null);

        setTitle(esModificacion ? "Actualizar Enfermedad" : "Registrar Enfermedad");
        setBounds(100, 100, 450, 340);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.info);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // Imagen decorativa
        JLabel lblImg = new JLabel();
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarEnfermedad.class.getResource(
                    "/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) {}
        lblImg.setBounds(356, 180, 68, 65);
        contentPanel.add(lblImg);

        // Codigo
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(10, 11, 90, 14);
        contentPanel.add(lblCodigo);

        txtCodigo = new JTextField("ENF-" + Clinica.generadorCodigoEnfermedad);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(FIXED_BG);
        txtCodigo.setForeground(FIXED_FG);
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtCodigo.setBounds(106, 8, 130, 22);
        contentPanel.add(txtCodigo);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 42, 90, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Permite letras, numeros, espacios y guion
                if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-') e.consume();
            }
        });
        txtNombre.setBounds(106, 39, 200, 22);
        contentPanel.add(txtNombre);

        // Sintomas
        JLabel lblSintomas = new JLabel("Sintomas:");
        lblSintomas.setBounds(10, 78, 90, 14);
        contentPanel.add(lblSintomas);

        txtSintomas = new JTextField();
        txtSintomas.setBounds(106, 75, 219, 65);
        contentPanel.add(txtSintomas);

        // Gravedad
        JLabel lblGravedad = new JLabel("Gravedad:");
        lblGravedad.setBounds(10, 158, 90, 14);
        contentPanel.add(lblGravedad);

        comboGravedad = new JComboBox<>();
        comboGravedad.setBounds(106, 155, 200, 22);
        contentPanel.add(comboGravedad);

        cargarGravedades();

        // Botones
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre   = txtNombre.getText().trim();
                String sintomas = txtSintomas.getText().trim();

                if (nombre.isEmpty() || sintomas.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Gravedadenfermedad gravedad = obtenerGravedadSeleccionada();

                if (!esModificacion) {
                    Enfermedad aux = new Enfermedad(
                        txtCodigo.getText(), nombre, sintomas, gravedad);
                    Clinica.getInstance().agregarEnfermedad(aux);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Enfermedad registrada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miEnfermedad.setNombreEnfermedad(nombre);
                    miEnfermedad.setSintomas(sintomas);
                    miEnfermedad.setGravedad(gravedad);
                    Clinica.getInstance().modificarEnfermedad(
                        miEnfermedad.getIdEnfermedad(), miEnfermedad);
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Enfermedad actualizada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPane.add(btnRegistrar);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        if (miEnfermedad != null) loadEnfermedad();
    }

    private void cargarGravedades() {
        listaGravedad = Clinica.getInstance().getMisGravedades();
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("(sin clasificar)");
        for (Gravedadenfermedad g : listaGravedad)
            modelo.addElement(g.getIdGravedad() + " — " + g.getGravedad());
        comboGravedad.setModel(modelo);
    }

    private Gravedadenfermedad obtenerGravedadSeleccionada() {
        int idx = comboGravedad.getSelectedIndex();
        if (idx <= 0 || listaGravedad == null || listaGravedad.isEmpty())
            return null;
        return listaGravedad.get(idx - 1);
    }

    public void clean() {
        // No incrementar aqui — agregarEnfermedad() ya lo hizo
        txtCodigo.setText("ENF-" + Clinica.generadorCodigoEnfermedad);
        txtNombre.setText("");
        txtSintomas.setText("");
        comboGravedad.setSelectedIndex(0);
    }

    public void loadEnfermedad() {
        txtCodigo.setText(miEnfermedad.getIdEnfermedad());
        txtNombre.setText(miEnfermedad.getNombreEnfermedad());
        txtSintomas.setText(miEnfermedad.getSintomas());
        if (miEnfermedad.getGravedad() != null) {
            String idGrav = miEnfermedad.getGravedad().getIdGravedad();
            for (int i = 0; i < comboGravedad.getItemCount(); i++) {
                if (comboGravedad.getItemAt(i).startsWith(idGrav)) {
                    comboGravedad.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            comboGravedad.setSelectedIndex(0);
        }
    }
}