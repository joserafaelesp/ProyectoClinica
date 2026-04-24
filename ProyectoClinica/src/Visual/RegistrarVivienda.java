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
import javax.swing.border.TitledBorder;

import Logical.Clinica;
import Logical.Vivienda;

public class RegistrarVivienda extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField   txtCodeVivienda;
    private JTextField   txtTelefono;
    private JTextField   txtDireccion;
    private Vivienda     miVivienda;
    private Vivienda     casaReg;
    private boolean      esModificacion = false;

    public Vivienda getCasaReg() {
        return casaReg;
    }

    public RegistrarVivienda(Vivienda house, int index) {
        miVivienda     = house;
        esModificacion = (house != null);

        setTitle(esModificacion ? "Modificar Vivienda" : "Registrar Vivienda");
        setBounds(100, 100, 420, 260);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.info);
        panel.setBorder(new TitledBorder(
            esModificacion ? "Datos de la vivienda" : "Nueva vivienda"));
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // ── Código (auto, bloqueado) ──────────────────────────────
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(20, 30, 60, 14);
        panel.add(lblCodigo);

        // ✅ CORREGIDO: prefijo VIV- en lugar de "Vivienda - "
        txtCodeVivienda = new JTextField("VIV-" + Clinica.generadorCodigoVivienda);
        txtCodeVivienda.setBackground(SystemColor.info);
        txtCodeVivienda.setEnabled(false);
        txtCodeVivienda.setBounds(85, 27, 110, 22);
        panel.add(txtCodeVivienda);

        // ── Teléfono ─────────────────────────────────────────────
        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(20, 65, 60, 14);
        panel.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        txtTelefono.setBounds(85, 62, 130, 22);
        panel.add(txtTelefono);

        // ── Dirección ────────────────────────────────────────────
        JLabel lblDireccion = new JLabel("Direccion:");
        lblDireccion.setBounds(20, 100, 62, 14);
        panel.add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(85, 97, 220, 22);
        panel.add(txtDireccion);

        // Imagen decorativa
        JLabel lblImg = new JLabel("");
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarVivienda.class.getResource(
                    "/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) {}
        lblImg.setBounds(320, 100, 64, 73);
        panel.add(lblImg);

        // ── Botones ───────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String telefono  = txtTelefono.getText().trim();
                String direccion = txtDireccion.getText().trim();

                if (direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "La direccion no puede estar vacia",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!esModificacion) {
                    // ✅ Usar el código VIV-N del campo (ya es correcto)
                    casaReg = new Vivienda(
                        txtCodeVivienda.getText(), direccion, telefono, null);
                    Clinica.getInstance().agregarVivienda(casaReg);
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Vivienda registrada correctamente",
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
        // ✅ Incrementar DESPUÉS de registrar y actualizar el campo con VIV-
        Clinica.generadorCodigoVivienda++;
        txtCodeVivienda.setText("VIV-" + Clinica.generadorCodigoVivienda);
        txtDireccion.setText("");
        txtTelefono.setText("");
        casaReg = null;
    }

    public void loadVivienda() {
        txtCodeVivienda.setText(miVivienda.getIdVivienda());
        txtDireccion.setText(miVivienda.getDireccion());
        if (miVivienda.getTelefono() != null)
            txtTelefono.setText(miVivienda.getTelefono());
    }
}