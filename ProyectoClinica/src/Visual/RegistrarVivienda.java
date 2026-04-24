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
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Datos de la Vivienda", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBackground(SystemColor.window);
        panel.setBounds(10, 11, 414, 206);
        contentPanel.add(panel);
        panel.setLayout(null);

        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(10, 30, 46, 14);
        panel.add(lblCodigo);

        txtCodeVivienda = new JTextField("VIV-" + Clinica.generadorCodigoVivienda);
        txtCodeVivienda.setEditable(false);
        txtCodeVivienda.setBounds(66, 27, 86, 20);
        panel.add(txtCodeVivienda);
        txtCodeVivienda.setColumns(10);

        JLabel lblDireccion = new JLabel("Direccion:");
        lblDireccion.setBounds(10, 70, 60, 14);
        panel.add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(80, 67, 200, 20);
        panel.add(txtDireccion);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(10, 110, 60, 14);
        panel.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(80, 107, 150, 20);
        panel.add(txtTelefono);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String direccion = txtDireccion.getText().trim();
                String telefono  = txtTelefono.getText().trim();

                if (direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar la dirección", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!esModificacion) {
                    casaReg = new Vivienda(txtCodeVivienda.getText(), direccion, telefono, null);
                    Clinica.getInstance().agregarVivienda(casaReg);
                    clean();
                    JOptionPane.showMessageDialog(null, "Vivienda registrada correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miVivienda.setDireccion(direccion);
                    Clinica.getInstance().modificarVivienda(miVivienda.getIdVivienda(), miVivienda);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Vivienda actualizada correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
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
        // ARREGLADO: Ya no sumamos Clinica.generadorCodigoVivienda++ aquí porque lo suma la base de datos, 
        // evitando que salte números en la vista.
        txtCodeVivienda.setText("VIV-" + Clinica.generadorCodigoVivienda);
        txtDireccion.setText("");
        txtTelefono.setText("");
        casaReg = null;
    }

    public void loadVivienda() {
        txtCodeVivienda.setText(miVivienda.getIdVivienda());
        txtDireccion.setText(miVivienda.getDireccion());
        if (miVivienda.getTelefono() != null) txtTelefono.setText(miVivienda.getTelefono());
    }
}