package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Vacuna;

public class RegistrarVacuna extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField   txtIdVacuna;
    private JTextField   txtNombreVacuna;
    private JTextField   txtDescripcion;
    private JButton      btnAccion;
    private Vacuna       miVacuna;
    private boolean      esModificacion = false;

    public RegistrarVacuna() {
        this(null, 0);
    }

    public RegistrarVacuna(Vacuna vacuna, int index) {
        miVacuna       = vacuna;
        esModificacion = (vacuna != null);

        setTitle(esModificacion ? "Actualizar Vacuna" : "Registrar Vacuna");
        setBounds(100, 100, 460, 300);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 192, 203));
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(30, 26, 46, 14);
        panel.add(lblId);

        // ✅ Formato VAC-N
        txtIdVacuna = new JTextField("VAC-" + Clinica.generadorCodigoVacuna);
        txtIdVacuna.setEditable(false);
        txtIdVacuna.setEnabled(false);
        txtIdVacuna.setBackground(SystemColor.info);
        txtIdVacuna.setBounds(94, 23, 120, 20);
        panel.add(txtIdVacuna);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 65, 54, 14);
        panel.add(lblNombre);

        txtNombreVacuna = new JTextField();
        txtNombreVacuna.setBounds(94, 62, 200, 20);
        panel.add(txtNombreVacuna);

        JLabel lblDesc = new JLabel("Descripcion:");
        lblDesc.setBounds(30, 105, 80, 14);
        panel.add(lblDesc);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(115, 102, 300, 80);
        panel.add(txtDescripcion);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnAccion = new JButton(esModificacion ? "Actualizar" : "Agregar");
        btnAccion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre      = txtNombreVacuna.getText().trim();
                String descripcion = txtDescripcion.getText().trim();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "El nombre es obligatorio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!esModificacion) {
                    Vacuna nueva = new Vacuna(
                        txtIdVacuna.getText(), nombre, descripcion);
                    Clinica.getInstance().agregarVacuna(nueva);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Vacuna registrada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miVacuna.setNombreVacuna(nombre);
                    miVacuna.setDescripcion(descripcion);
                    Clinica.getInstance().modificarVacuna(
                        miVacuna.getIdVacuna(), miVacuna);
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Vacuna actualizada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnAccion.setActionCommand("OK");
        buttonPane.add(btnAccion);
        getRootPane().setDefaultButton(btnAccion);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        if (miVacuna != null) loadVacuna();
    }

    public void clean() {
        // No incrementar aqui — agregarVacuna() ya lo hizo
        // ✅ Formato VAC-N
        txtIdVacuna.setText("VAC-" + Clinica.generadorCodigoVacuna);
        txtNombreVacuna.setText("");
        txtDescripcion.setText("");
    }

    public void loadVacuna() {
        txtIdVacuna.setText(miVacuna.getIdVacuna());
        txtNombreVacuna.setText(miVacuna.getNombreVacuna());
        txtDescripcion.setText(
            miVacuna.getDescripcion() != null ? miVacuna.getDescripcion() : "");
    }
}