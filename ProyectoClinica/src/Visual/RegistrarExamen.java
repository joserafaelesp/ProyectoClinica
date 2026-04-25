package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Logical.Clinica;
import Logical.Examen;

public class RegistrarExamen extends JDialog {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private Examen     miExamen;
    private boolean    esModificacion = false;

    private static final Color FIXED_BG = new Color(230, 240, 255);
    private static final Color FIXED_FG = new Color(30, 60, 120);

    public RegistrarExamen() { this(null, 0); }

    public RegistrarExamen(Examen examen, int index) {
        miExamen       = examen;
        esModificacion = (examen != null);

        setTitle(esModificacion ? "Actualizar Examen" : "Registrar Examen");
        setBounds(100, 100, 460, 260);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            esModificacion ? "Datos del examen" : "Nuevo Examen"));
        panel.setLayout(null);
        getContentPane().add(panel, BorderLayout.CENTER);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Codigo
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setFont(labelFont);
        lblCodigo.setBounds(20, 35, 80, 22);
        panel.add(lblCodigo);

        txtCodigo = new JTextField("EXA-" + Clinica.generadorCodigoExamen);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(FIXED_BG);
        txtCodigo.setForeground(FIXED_FG);
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtCodigo.setBounds(110, 33, 120, 26);
        panel.add(txtCodigo);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(labelFont);
        lblNombre.setBounds(20, 78, 80, 22);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(fieldFont);
        txtNombre.setBounds(110, 76, 250, 26);
        panel.add(txtNombre);

        // Descripcion
        JLabel lblDesc = new JLabel("Descripcion:");
        lblDesc.setFont(labelFont);
        lblDesc.setBounds(20, 122, 90, 22);
        panel.add(lblDesc);

        txtDescripcion = new JTextField();
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setBounds(110, 120, 300, 26);
        panel.add(txtDescripcion);

        // Imagen decorativa
        JLabel lblImg = new JLabel("");
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarExamen.class.getResource(
                    "/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) {}
        lblImg.setBounds(380, 30, 55, 55);
        panel.add(lblImg);

        // Botones
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttonPane.setBackground(new Color(200, 220, 240));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnGuardar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnGuardar.setBackground(new Color(70, 130, 180));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String desc   = txtDescripcion.getText().trim();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "El nombre del examen no puede estar vacio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!esModificacion) {
                    Examen nuevo = new Examen(
                        txtCodigo.getText(), nombre, desc);
                    Clinica.getInstance().agregarExamen(nuevo);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Examen registrado correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miExamen.setNombreExamen(nombre);
                    miExamen.setDescripcion(desc);
                    Clinica.getInstance().modificarExamen(
                        miExamen.getIdExamen(), miExamen);
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Examen actualizado correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        getRootPane().setDefaultButton(btnGuardar);
        buttonPane.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        if (miExamen != null) loadExamen();
    }

    public void clean() {
        // No incrementar aqui — agregarExamen() ya lo incremento
        txtCodigo.setText("EXA-" + Clinica.generadorCodigoExamen);
        txtNombre.setText("");
        txtDescripcion.setText("");
    }

    public void loadExamen() {
        txtCodigo.setText(miExamen.getIdExamen());
        txtNombre.setText(miExamen.getNombreExamen());
        txtDescripcion.setText(
            miExamen.getDescripcion() != null ? miExamen.getDescripcion() : "");
    }
}