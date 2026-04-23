package Visual;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

import Logical.Clinica;
import Logical.Medico;
import Logical.Paciente;
import Logical.Persona;
import Logical.Vivienda;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class RegistrarGeneral extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodeMed;
    private JTextField txtTelefono;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtxEspecialidad;
    private JSpinner spnFecha;
    private JRadioButton rdbtnPaciente;
    private JRadioButton rdbtnMedico;
    private JPanel panel_Medico;
    private JTextField textCodigoPaciente;
    private JTextField textFieldInfoEmergencia;
    private JTextField txtTipoSangre;
    private JPanel panel_Paciente;
    private Persona miPersona;
    private JTextField txtVivienda;
    private Vivienda nuevaViv = null;
    private JComboBox<String> comboBoxGender;
    private JButton btnEditarVivienda;
    private boolean esModificacion = false;

    public RegistrarGeneral(Persona person, int index) {
        miPersona = person;
        esModificacion = (person != null);

        setTitle(esModificacion ? "Modificar Persona" : "Registrar Persona");
        setBounds(100, 100, 705, 430);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.info);
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // ── Panel datos generales ────────────────────────────────
        JPanel panel_DatosGenerales = new JPanel();
        panel_DatosGenerales.setBorder(new TitledBorder(null, "Datos generales",
            TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_DatosGenerales.setBackground(SystemColor.info);
        panel_DatosGenerales.setBounds(10, 11, 669, 203);
        panel.add(panel_DatosGenerales);
        panel_DatosGenerales.setLayout(null);

        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setBounds(10, 26, 52, 14);
        panel_DatosGenerales.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        txtCedula.setBounds(66, 23, 152, 20);
        txtCedula.setEnabled(!esModificacion);
        panel_DatosGenerales.add(txtCedula);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(10, 110, 60, 14);
        panel_DatosGenerales.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        txtTelefono.setBounds(74, 107, 144, 20);
        panel_DatosGenerales.add(txtTelefono);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 69, 52, 14);
        panel_DatosGenerales.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        txtNombre.setBounds(66, 66, 152, 20);
        panel_DatosGenerales.add(txtNombre);

        JLabel lblGenero = new JLabel("Genero:");
        lblGenero.setBounds(10, 156, 52, 14);
        panel_DatosGenerales.add(lblGenero);

        comboBoxGender = new JComboBox<>();
        comboBoxGender.setModel(new DefaultComboBoxModel<>(
            new String[]{"Elegir", "Masculino", "Femenino"}));
        comboBoxGender.setBounds(82, 153, 94, 20);
        panel_DatosGenerales.add(comboBoxGender);

        rdbtnMedico = new JRadioButton("Medico");
        rdbtnMedico.addActionListener(e -> {
            rdbtnPaciente.setSelected(false);
            panel_Medico.setVisible(true);
            panel_Paciente.setVisible(false);
        });
        rdbtnMedico.setBackground(SystemColor.info);
        rdbtnMedico.setBounds(402, 152, 109, 23);
        panel_DatosGenerales.add(rdbtnMedico);

        rdbtnPaciente = new JRadioButton("Paciente");
        rdbtnPaciente.addActionListener(e -> {
            rdbtnMedico.setSelected(false);
            panel_Medico.setVisible(false);
            panel_Paciente.setVisible(true);
        });
        rdbtnPaciente.setBackground(SystemColor.info);
        rdbtnPaciente.setBounds(402, 124, 109, 23);
        panel_DatosGenerales.add(rdbtnPaciente);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(333, 128, 46, 14);
        panel_DatosGenerales.add(lblRol);

        spnFecha = new JSpinner();
        spnFecha.setBounds(402, 23, 144, 28);
        spnFecha.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
        panel_DatosGenerales.add(spnFecha);

        JLabel lblFecha = new JLabel("Fecha de Nacimiento:");
        lblFecha.setBounds(255, 26, 144, 14);
        panel_DatosGenerales.add(lblFecha);

        JLabel lblVivienda = new JLabel("Codigo de vivienda:");
        lblVivienda.setBounds(276, 86, 116, 14);
        panel_DatosGenerales.add(lblVivienda);

        txtVivienda = new JTextField();
        txtVivienda.setBounds(402, 83, 109, 20);
        panel_DatosGenerales.add(txtVivienda);

        JButton btnBuscaVivienda = new JButton("Buscar");
        btnBuscaVivienda.addActionListener(e -> {
            nuevaViv = Clinica.getInstance().obtenervivienda(txtVivienda.getText());
            if (nuevaViv != null) {
                txtVivienda.setText(nuevaViv.getIdVivienda());
                txtVivienda.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Vivienda no encontrada", "Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        btnBuscaVivienda.setBounds(545, 65, 89, 23);
        panel_DatosGenerales.add(btnBuscaVivienda);

        JButton btnCrearVivienda = new JButton("Crear");
        btnCrearVivienda.addActionListener(e -> {
            RegistrarVivienda nueva = new RegistrarVivienda(null, 0);
            nueva.setModal(true);
            nueva.setVisible(true);
            nuevaViv = nueva.getCasaReg();
            if (nuevaViv != null) {
                txtVivienda.setText(nuevaViv.getIdVivienda());
                txtVivienda.setEnabled(false);
                btnCrearVivienda.setVisible(false);
                btnEditarVivienda.setVisible(true);
            }
        });
        btnCrearVivienda.setBounds(545, 101, 89, 23);
        panel_DatosGenerales.add(btnCrearVivienda);

        btnEditarVivienda = new JButton("Editar");
        btnEditarVivienda.setVisible(false);
        btnEditarVivienda.addActionListener(e -> {
            RegistrarVivienda nueva = new RegistrarVivienda(nuevaViv, 0);
            nueva.setModal(true);
            nueva.setVisible(true);
        });
        btnEditarVivienda.setBounds(545, 106, 89, 23);
        panel_DatosGenerales.add(btnEditarVivienda);

        // ── Panel Médico ─────────────────────────────────────────
        panel_Medico = new JPanel();
        panel_Medico.setBorder(new TitledBorder(null, "Datos de Medico",
            TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_Medico.setBackground(SystemColor.info);
        panel_Medico.setBounds(10, 225, 598, 103);
        panel.add(panel_Medico);
        panel_Medico.setLayout(null);
        panel_Medico.setVisible(false);

        JLabel lblCodMed = new JLabel("Codigo:");
        lblCodMed.setBounds(10, 25, 46, 14);
        panel_Medico.add(lblCodMed);

        txtCodeMed = new JTextField();
        txtCodeMed.setBounds(65, 22, 120, 20);
        txtCodeMed.setBackground(SystemColor.info);
        txtCodeMed.setEnabled(false);
        txtCodeMed.setText("Medico-" + Clinica.generadorCodigoidMedico);
        panel_Medico.add(txtCodeMed);

        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setBounds(10, 74, 97, 14);
        panel_Medico.add(lblEspecialidad);

        txtxEspecialidad = new JTextField();
        txtxEspecialidad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        txtxEspecialidad.setBounds(86, 71, 185, 20);
        panel_Medico.add(txtxEspecialidad);

        // ── Panel Paciente ───────────────────────────────────────
        panel_Paciente = new JPanel();
        panel_Paciente.setBorder(new TitledBorder(null, "Datos Paciente",
            TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_Paciente.setBackground(SystemColor.info);
        panel_Paciente.setBounds(10, 225, 598, 103);
        panel.add(panel_Paciente);
        panel_Paciente.setLayout(null);
        panel_Paciente.setVisible(false);

        JLabel lblCodPac = new JLabel("Codigo:");
        lblCodPac.setBounds(10, 25, 46, 14);
        panel_Paciente.add(lblCodPac);

        textCodigoPaciente = new JTextField("Paciente-" + Clinica.generadorCodigoPaciente);
        textCodigoPaciente.setBackground(SystemColor.info);
        textCodigoPaciente.setEnabled(false);
        textCodigoPaciente.setBounds(66, 22, 100, 20);
        panel_Paciente.add(textCodigoPaciente);

        JLabel lblInfo = new JLabel("Info emergencia:");
        lblInfo.setBounds(10, 58, 110, 14);
        panel_Paciente.add(lblInfo);

        textFieldInfoEmergencia = new JTextField();
        textFieldInfoEmergencia.setBounds(120, 55, 200, 20);
        panel_Paciente.add(textFieldInfoEmergencia);

        JLabel lblSangre = new JLabel("Tipo de Sangre:");
        lblSangre.setBounds(330, 25, 100, 14);
        panel_Paciente.add(lblSangre);

        txtTipoSangre = new JTextField();
        txtTipoSangre.setBounds(435, 22, 80, 20);
        txtTipoSangre.setToolTipText("Ej: A+, B-, O+, AB-");
        panel_Paciente.add(txtTipoSangre);

        // imagen decorativa
        JLabel lblImg = new JLabel("");
        try {
            lblImg.setIcon(new ImageIcon(
                RegistrarGeneral.class.getResource("/imagenes/edificio-del-hospital (2).png")));
        } catch (Exception ex) { /* imagen opcional */ }
        lblImg.setBounds(612, 255, 64, 73);
        panel.add(lblImg);

        // ── Botones ──────────────────────────────────────────────
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(SystemColor.info);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String cedula   = txtCedula.getText().trim();
                String nombre   = txtNombre.getText().trim();
                String genero   = (String) comboBoxGender.getSelectedItem();
                String telefono = txtTelefono.getText().trim();
                Date   fecha    = (Date) spnFecha.getValue();

                if (cedula.isEmpty() || nombre.isEmpty()
                        || genero.equals("Elegir") || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Convertir Date → LocalDate
                LocalDate fechaLocal = fecha.toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                if (!esModificacion) {
                    // ── REGISTRAR ────────────────────────────────
                    if (rdbtnMedico.isSelected()) {
                        String especialidad = txtxEspecialidad.getText().trim();
                        if (especialidad.isEmpty()) {
                            JOptionPane.showMessageDialog(null,
                                "Ingrese la especialidad del médico",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Medico aux = new Medico(
                            cedula, nombre, genero, fechaLocal, telefono,
                            nuevaViv, txtCodeMed.getText(), especialidad);
                        Clinica.getInstance().agregarMedico(aux);

                    } else if (rdbtnPaciente.isSelected()) {
                        Paciente aux = new Paciente(
                            cedula, nombre, genero, fechaLocal, telefono,
                            nuevaViv, textCodigoPaciente.getText(),
                            textFieldInfoEmergencia.getText(),
                            txtTipoSangre.getText().trim());
                        Clinica.getInstance().agregarPaciente(aux);

                    } else {
                        JOptionPane.showMessageDialog(null,
                            "Seleccione un rol (Médico o Paciente)",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Registrado correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                } else {
                    // ── ACTUALIZAR ───────────────────────────────
                    miPersona.setNombre(nombre);
                    miPersona.setGenero(genero);
                    miPersona.setTelefono(telefono);
                    miPersona.setFechaNacimiento(fechaLocal);

                    if (miPersona instanceof Paciente && rdbtnPaciente.isSelected()) {
                        Paciente p = (Paciente) miPersona;
                        p.setInfoEmergencia(textFieldInfoEmergencia.getText());
                        p.setTipoSangre(txtTipoSangre.getText().trim());
                        p.setViviend(nuevaViv);
                        Clinica.getInstance().modificarPaciente(cedula, p);
                    }
                    if (miPersona instanceof Medico && rdbtnMedico.isSelected()) {
                        Medico m = (Medico) miPersona;
                        m.setEspecialidad(txtxEspecialidad.getText());
                        Clinica.getInstance().modificarMedico(cedula, m);
                    }
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Actualizado correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPane.add(btnRegistrar);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        if (miPersona != null) loadPersona();
    }

    public void clean() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtVivienda.setText("");
        txtxEspecialidad.setText("");
        textFieldInfoEmergencia.setText("");
        txtTipoSangre.setText("");
        rdbtnMedico.setSelected(false);
        rdbtnPaciente.setSelected(false);
        panel_Medico.setVisible(false);
        panel_Paciente.setVisible(false);
        txtVivienda.setEnabled(true);
        nuevaViv = null;
        txtCodeMed.setText("Medico-" + Clinica.generadorCodigoidMedico);
        textCodigoPaciente.setText("Paciente-" + Clinica.generadorCodigoPaciente);
    }

    public void loadPersona() {
        txtCedula.setText(miPersona.getCedula());
        txtNombre.setText(miPersona.getNombre());
        txtTelefono.setText(miPersona.getTelefono());
        comboBoxGender.setSelectedItem(miPersona.getGenero());
        if (miPersona.getViviend() != null)
            txtVivienda.setText(miPersona.getViviend().getIdVivienda());

        if (miPersona instanceof Paciente) {
            rdbtnPaciente.setSelected(true);
            panel_Paciente.setVisible(true);
            Paciente p = (Paciente) miPersona;
            textCodigoPaciente.setText(p.getIdPaciente());
            textFieldInfoEmergencia.setText(p.getInfoEmergencia());
            txtTipoSangre.setText(p.getTipoSangre() != null ? p.getTipoSangre() : "");
        }
        if (miPersona instanceof Medico) {
            rdbtnMedico.setSelected(true);
            panel_Medico.setVisible(true);
            Medico m = (Medico) miPersona;
            txtCodeMed.setText(m.getIdMedico());
            txtxEspecialidad.setText(m.getEspecialidad());
        }
    }
}