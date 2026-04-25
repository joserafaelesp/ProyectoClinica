package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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
import Logical.Vivienda;

public class RegistrarVivienda extends JDialog {

    private JTextField txtCodeVivienda;
    private JTextField txtDireccion;
    private Vivienda   miVivienda;
    private Vivienda   casaReg;
    private boolean    esModificacion = false;

    public Vivienda getCasaReg() { return casaReg; }

    public RegistrarVivienda(Vivienda house, int index) {
        miVivienda     = house;
        esModificacion = (house != null);

        setTitle(esModificacion ? "Modificar Vivienda" : "Registrar Vivienda");
        setBounds(100, 100, 420, 220);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            esModificacion ? "Datos de la vivienda" : "Nueva Vivienda"));
        panel.setLayout(null);
        getContentPane().add(panel, BorderLayout.CENTER);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color editBg   = new Color(255, 255, 255);
        Color fixedBg  = new Color(230, 240, 255); // azul claro para campos fijos
        Color fixedFg  = new Color(30, 60, 120);   // azul oscuro visible

        // Codigo
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setFont(labelFont);
        lblCodigo.setBounds(20, 35, 80, 24);
        panel.add(lblCodigo);

        txtCodeVivienda = new JTextField("VIV-" + Clinica.generadorCodigoVivienda);
        txtCodeVivienda.setEnabled(false);
        txtCodeVivienda.setBackground(fixedBg);
        txtCodeVivienda.setForeground(fixedFg);
        txtCodeVivienda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtCodeVivienda.setBounds(110, 33, 120, 28);
        panel.add(txtCodeVivienda);

        // Direccion
        JLabel lblDir = new JLabel("Direccion:");
        lblDir.setFont(labelFont);
        lblDir.setBounds(20, 80, 80, 24);
        panel.add(lblDir);

        txtDireccion = new JTextField();
        txtDireccion.setBackground(editBg);
        txtDireccion.setFont(fieldFont);
        txtDireccion.setBounds(110, 78, 260, 28);
        panel.add(txtDireccion);

        // Imagen
        JLabel lblImg = new JLabel("");
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarVivienda.class.getResource(
                    "/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) {}
        lblImg.setBounds(335, 25, 60, 60);
        panel.add(lblImg);

        // Botones
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttonPane.setBackground(new Color(200, 220, 240));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.setBackground(new Color(70, 130, 180));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String direccion = txtDireccion.getText().trim();
                if (direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "La direccion no puede estar vacia",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!esModificacion) {
                    casaReg = new Vivienda(txtCodeVivienda.getText(), direccion);
                    Clinica.getInstance().agregarVivienda(casaReg);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Vivienda registrada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miVivienda.setDireccion(direccion);
                    Clinica.getInstance().modificarVivienda(
                        miVivienda.getIdVivienda(), miVivienda);
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Vivienda actualizada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        getRootPane().setDefaultButton(btnRegistrar);
        buttonPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        if (miVivienda != null) loadVivienda();
    }

    public void clean() {
        Clinica.generadorCodigoVivienda++;
        txtCodeVivienda.setText("VIV-" + Clinica.generadorCodigoVivienda);
        txtDireccion.setText("");
        casaReg = null;
    }

    public void loadVivienda() {
        txtCodeVivienda.setText(miVivienda.getIdVivienda());
        txtDireccion.setText(miVivienda.getDireccion());
    }
}