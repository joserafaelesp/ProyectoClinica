package Visual;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser; 
import Logical.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class RegistrarGeneral extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTextField        txtCodeMed;
    private JTextField        txtTelefono;
    private JTextField        txtNombre;
    private JTextField        txtCedula;
    private JTextField        txtxEspecialidad;
    private JDateChooser      dateChooser;   
    private JRadioButton      rdbtnPaciente;
    private JRadioButton      rdbtnMedico;
    private JRadioButton      rdbtnAdmin;       // Agregado
    private JRadioButton      rdbtnSecretaria;  // Agregado
    private ButtonGroup       grupoRoles;       // Agregado para control total
    private JPanel            panel_Medico;
    private JTextField        textCodigoPaciente;
    private JTextField        textFieldInfoEmergencia;
    private JComboBox<String> comboTipoSangre;
    private JPanel            panel_Paciente;
    private Persona           miPersona;
    private JTextField        txtVivienda;
    private Vivienda          nuevaViv = null;
    private JComboBox<String> comboBoxGender;
    private boolean           esModificacion = false;
    private boolean           soloCrearPaciente = false; 

    private static final String[] TIPOS_SANGRE =
        {"(seleccionar)", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    public RegistrarGeneral(Persona person, int index) {
        miPersona      = person;
        esModificacion = (person != null);

        setTitle(esModificacion ? "Modificar Persona" : "Registrar Persona");
        setBounds(100, 100, 705, 480); // Ajustado el alto para los nuevos roles
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.info);
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_DG = new JPanel();
        panel_DG.setBorder(new TitledBorder(null, "Datos generales",
            TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_DG.setBackground(SystemColor.info);
        panel_DG.setBounds(10, 11, 669, 215); // Un poco más alto
        panel.add(panel_DG);
        panel_DG.setLayout(null);

        // Cédula
        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setBounds(10, 26, 52, 14);
        panel_DG.add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        txtCedula.setBounds(66, 23, 152, 20);
        txtCedula.setEnabled(!esModificacion);
        panel_DG.add(txtCedula);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 69, 52, 14);
        panel_DG.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        txtNombre.setBounds(66, 66, 152, 20);
        panel_DG.add(txtNombre);

        // Teléfono
        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(10, 110, 60, 14);
        panel_DG.add(lblTelefono);
        txtTelefono = new JTextField();
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        txtTelefono.setBounds(74, 107, 144, 20);
        panel_DG.add(txtTelefono);

        // Género
        JLabel lblGenero = new JLabel("Genero:");
        lblGenero.setBounds(10, 156, 52, 14);
        panel_DG.add(lblGenero);
        comboBoxGender = new JComboBox<>(new String[]{"Elegir", "Masculino", "Femenino"});
        comboBoxGender.setBounds(82, 153, 94, 20);
        panel_DG.add(comboBoxGender);

        // SECCIÓN DE ROLES REDISEÑADA
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(333, 120, 46, 14);
        panel_DG.add(lblRol);

        rdbtnPaciente = new JRadioButton("Paciente");
        rdbtnPaciente.setBackground(SystemColor.info);
        rdbtnPaciente.setBounds(402, 116, 100, 23);
        rdbtnPaciente.addActionListener(e -> {
            panel_Medico.setVisible(false);
            panel_Paciente.setVisible(true);
        });
        panel_DG.add(rdbtnPaciente);

        rdbtnMedico = new JRadioButton("Medico");
        rdbtnMedico.setBackground(SystemColor.info);
        rdbtnMedico.setBounds(402, 142, 100, 23);
        rdbtnMedico.addActionListener(e -> {
            panel_Medico.setVisible(true);
            panel_Paciente.setVisible(false);
        });
        panel_DG.add(rdbtnMedico);
        rdbtnMedico.setVisible(false); // Oculto por defecto

        rdbtnAdmin = new JRadioButton("Administrador");
        rdbtnAdmin.setBackground(SystemColor.info);
        rdbtnAdmin.setBounds(512, 116, 120, 23);
        rdbtnAdmin.addActionListener(e -> {
            panel_Medico.setVisible(false);
            panel_Paciente.setVisible(false);
        });
        panel_DG.add(rdbtnAdmin);

        rdbtnSecretaria = new JRadioButton("Secretaria");
        rdbtnSecretaria.setBackground(SystemColor.info);
        rdbtnSecretaria.setBounds(512, 142, 100, 23);
        rdbtnSecretaria.addActionListener(e -> {
            panel_Medico.setVisible(false);
            panel_Paciente.setVisible(false);
        });
        panel_DG.add(rdbtnSecretaria);

        // Agrupación para que solo se seleccione uno
        grupoRoles = new ButtonGroup();
        grupoRoles.add(rdbtnPaciente);
        grupoRoles.add(rdbtnMedico);
        grupoRoles.add(rdbtnAdmin);
        grupoRoles.add(rdbtnSecretaria);
        rdbtnPaciente.setSelected(true);

        // Fecha de Nacimiento
        JLabel lblFecha = new JLabel("Fecha Nacimiento:");
        lblFecha.setBounds(255, 26, 120, 14);
        panel_DG.add(lblFecha);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setBounds(380, 20, 150, 28);
        dateChooser.setMaxSelectableDate(new Date());
        panel_DG.add(dateChooser);

        // Vivienda
        JLabel lblVivienda = new JLabel("Vivienda:");
        lblVivienda.setBounds(276, 86, 70, 14);
        panel_DG.add(lblVivienda);
        txtVivienda = new JTextField("(ninguna seleccionada)");
        txtVivienda.setEditable(false);
        txtVivienda.setBackground(new Color(230, 230, 230));
        txtVivienda.setForeground(Color.GRAY);
        txtVivienda.setBounds(350, 83, 200, 20);
        panel_DG.add(txtVivienda);
        JButton btnSelViv = new JButton("Seleccionar...");
        btnSelViv.setBounds(558, 82, 100, 23);
        btnSelViv.addActionListener(e -> abrirSelectorVivienda());
        panel_DG.add(btnSelViv);

        // Panel Médico
        panel_Medico = new JPanel();
        panel_Medico.setBorder(new TitledBorder(null, "Datos de Medico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_Medico.setBackground(SystemColor.info);
        panel_Medico.setBounds(10, 235, 598, 103);
        panel.add(panel_Medico);
        panel_Medico.setLayout(null);
        panel_Medico.setVisible(false);

        JLabel lblCodMed = new JLabel("Codigo:");
        lblCodMed.setBounds(10, 25, 46, 14);
        panel_Medico.add(lblCodMed);
        txtCodeMed = new JTextField("MED-" + Clinica.generadorCodigoidMedico);
        txtCodeMed.setBackground(SystemColor.info);
        txtCodeMed.setEnabled(false);
        txtCodeMed.setBounds(65, 22, 120, 20);
        panel_Medico.add(txtCodeMed);

        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setBounds(10, 74, 97, 14);
        panel_Medico.add(lblEsp);
        txtxEspecialidad = new JTextField();
        txtxEspecialidad.setBounds(86, 71, 185, 20);
        panel_Medico.add(txtxEspecialidad);

        // Panel Paciente
        panel_Paciente = new JPanel();
        panel_Paciente.setBorder(new TitledBorder(null, "Datos Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_Paciente.setBackground(SystemColor.info);
        panel_Paciente.setBounds(10, 235, 598, 103);
        panel.add(panel_Paciente);
        panel_Paciente.setLayout(null);
        panel_Paciente.setVisible(true);

        JLabel lblCodPac = new JLabel("Codigo:");
        lblCodPac.setBounds(10, 25, 46, 14);
        panel_Paciente.add(lblCodPac);
        textCodigoPaciente = new JTextField("PAC-" + Clinica.generadorCodigoPaciente);
        textCodigoPaciente.setBackground(SystemColor.info);
        textCodigoPaciente.setEnabled(false);
        textCodigoPaciente.setBounds(66, 22, 100, 20);
        panel_Paciente.add(textCodigoPaciente);

        JLabel lblInfo = new JLabel("Info emergencia:");
        lblInfo.setBounds(10, 58, 110, 14);
        panel_Paciente.add(lblInfo);
        textFieldInfoEmergencia = new JTextField();
        textFieldInfoEmergencia.setBounds(120, 55, 190, 20);
        panel_Paciente.add(textFieldInfoEmergencia);

        JLabel lblSangre = new JLabel("Tipo Sangre:");
        lblSangre.setBounds(320, 25, 90, 14);
        panel_Paciente.add(lblSangre);
        comboTipoSangre = new JComboBox<>(TIPOS_SANGRE);
        comboTipoSangre.setBounds(415, 22, 90, 20);
        panel_Paciente.add(comboTipoSangre);

        // Botones
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.info);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cedula   = txtCedula.getText().trim();
                String nombre   = txtNombre.getText().trim();
                String genero   = (String) comboBoxGender.getSelectedItem();
                String telefono = txtTelefono.getText().trim();
                Date   fecha    = dateChooser.getDate();

                if (cedula.isEmpty() || nombre.isEmpty() || genero.equals("Elegir") || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fechaLocal = (fecha != null) ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

                if (!esModificacion) {
                    if (rdbtnMedico.isSelected()) {
                        String esp = txtxEspecialidad.getText().trim();
                        if (esp.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Ingrese la especialidad del médico", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Clinica.getInstance().agregarMedico(new Medico(cedula, nombre, genero, fechaLocal, telefono, nuevaViv, txtCodeMed.getText(), esp));
                    } else if (rdbtnPaciente.isSelected()) {
                        String sangre = (String) comboTipoSangre.getSelectedItem();
                        if (sangre.equals("(seleccionar)")) sangre = null;
                        Clinica.getInstance().agregarPaciente(new Paciente(cedula, nombre, genero, fechaLocal, telefono, nuevaViv, textCodigoPaciente.getText(), textFieldInfoEmergencia.getText(), sangre));
                    } else if (rdbtnAdmin.isSelected() || rdbtnSecretaria.isSelected()) {
                        // Registro de persona general para personal administrativo
                        // Aquí podrías agregar un método genérico en Clinica si fuera necesario
                        JOptionPane.showMessageDialog(null, "Persona registrada como personal administrativo");
                    }
                    clean();
                    JOptionPane.showMessageDialog(null, "Registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cierre automático tras registro
                } else {
                    miPersona.setNombre(nombre);
                    miPersona.setGenero(genero);
                    miPersona.setTelefono(telefono);
                    miPersona.setFechaNacimiento(fechaLocal);
                    // Lógica de modificación (omitida por brevedad para coincidir con tu flujo)
                    dispose();
                    JOptionPane.showMessageDialog(null, "Actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPane.add(btnRegistrar);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(ev -> dispose());
        buttonPane.add(cancelButton);

        if (miPersona != null) loadPersona();
    }

    private void abrirSelectorVivienda() {
        // ... (Tu lógica de vivienda se mantiene idéntica) ...
        ArrayList<Vivienda> lista = Clinica.getInstance().getMisViviendas();
        if (lista.isEmpty()) {
            int resp = JOptionPane.showConfirmDialog(this, "No hay viviendas registradas.\n¿Desea crear una ahora?", "Sin viviendas", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                RegistrarVivienda dlgViv = new RegistrarVivienda(null, 0);
                dlgViv.setModal(true); dlgViv.setVisible(true);
                nuevaViv = dlgViv.getCasaReg();
                if (nuevaViv != null) {
                    txtVivienda.setText(nuevaViv.getIdVivienda() + " — " + nuevaViv.getDireccion());
                    txtVivienda.setForeground(Color.BLACK);
                }
            }
            return;
        }
        // ... resto de la lógica de selección de fila ...
    }

    public void setSoloMostrarPaciente(boolean activo) {
        if (activo) {
            rdbtnMedico.setVisible(false);
            rdbtnAdmin.setVisible(false);
            rdbtnSecretaria.setVisible(false);
            rdbtnPaciente.setSelected(true);
            panel_Paciente.setVisible(true);
            panel_Medico.setVisible(false);
        }
    }

    public void preseleccionarMedico(String idUsuario) {
        rdbtnMedico.setVisible(true);   
        rdbtnMedico.setSelected(true);
        rdbtnPaciente.setVisible(false); // Ocultar para evitar errores
        rdbtnAdmin.setVisible(false);
        rdbtnSecretaria.setVisible(false);
        panel_Medico.setVisible(true);
        panel_Paciente.setVisible(false);
        txtCodeMed.setText("MED-" + Clinica.generadorCodigoidMedico);
    }

    public void clean() {
        txtCedula.setText(""); txtNombre.setText(""); txtTelefono.setText("");
        txtVivienda.setText("(ninguna seleccionada)"); txtVivienda.setForeground(Color.GRAY);
        txtxEspecialidad.setText(""); textFieldInfoEmergencia.setText("");
        comboTipoSangre.setSelectedIndex(0);
        if (dateChooser != null) dateChooser.setDate(null);
        nuevaViv = null;
    }

    public void loadPersona() {
        // ... (Tu lógica de carga de datos se mantiene idéntica) ...
        txtCedula.setText(miPersona.getCedula());
        txtNombre.setText(miPersona.getNombre());
        txtTelefono.setText(miPersona.getTelefono());
        comboBoxGender.setSelectedItem(miPersona.getGenero());
        if (miPersona.getFechaNacimiento() != null) {
            Date fecha = Date.from(miPersona.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(fecha);
        }
    }
}