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
    private JPanel            panel_Medico;
    private JTextField        textCodigoPaciente;
    private JTextField        textFieldInfoEmergencia;
    private JComboBox<String> comboTipoSangre;
    private JPanel            panel_Paciente;
    private Persona           miPersona;
    private JTextField        txtVivienda;
    private Vivienda          nuevaViv      = null;
    private JComboBox<String> comboBoxGender;
    private boolean           esModificacion = false;

    private static final String[] TIPOS_SANGRE =
        {"(seleccionar)", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    public RegistrarGeneral(Persona person, int index) {
        miPersona      = person;
        esModificacion = (person != null);

        setTitle(esModificacion ? "Modificar Persona" : "Registrar Persona");
        setBounds(100, 100, 710, 450);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.info);
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // ── Panel datos generales ─────────────────────────────────
        JPanel panel_DG = new JPanel();
        panel_DG.setBorder(new TitledBorder("Datos generales"));
        panel_DG.setBackground(SystemColor.info);
        panel_DG.setBounds(10, 11, 675, 210);
        panel.add(panel_DG);
        panel_DG.setLayout(null);

        // Cedula
        new JLabel("Cedula:") {{ setBounds(10, 26, 52, 14); panel_DG.add(this); }};
        txtCedula = new JTextField();
        txtCedula.setBounds(66, 23, 152, 20);
        txtCedula.setEnabled(!esModificacion);
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        panel_DG.add(txtCedula);

        // Nombre
        new JLabel("Nombre:") {{ setBounds(10, 60, 52, 14); panel_DG.add(this); }};
        txtNombre = new JTextField();
        txtNombre.setBounds(66, 57, 152, 20);
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        panel_DG.add(txtNombre);

        // Telefono
        new JLabel("Telefono:") {{ setBounds(10, 95, 60, 14); panel_DG.add(this); }};
        txtTelefono = new JTextField();
        txtTelefono.setBounds(74, 92, 144, 20);
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        panel_DG.add(txtTelefono);

        // Genero
        new JLabel("Genero:") {{ setBounds(10, 155, 52, 14); panel_DG.add(this); }};
        comboBoxGender = new JComboBox<>(new String[]{"Elegir", "Masculino", "Femenino"});
        comboBoxGender.setBounds(66, 152, 110, 22);
        panel_DG.add(comboBoxGender);

        // Fecha nacimiento
        new JLabel("Fecha Nacimiento:") {{ setBounds(255, 26, 120, 14); panel_DG.add(this); }};
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setBounds(378, 20, 150, 28);
        dateChooser.setMaxSelectableDate(new Date());
        panel_DG.add(dateChooser);

        // Vivienda
        new JLabel("Vivienda:") {{ setBounds(255, 65, 70, 14); panel_DG.add(this); }};
        txtVivienda = new JTextField("(ninguna seleccionada)");
        txtVivienda.setEditable(false);
        txtVivienda.setBackground(new Color(230, 230, 230));
        txtVivienda.setForeground(Color.GRAY);
        txtVivienda.setBounds(325, 62, 210, 20);
        panel_DG.add(txtVivienda);
        JButton btnSelViv = new JButton("Seleccionar...");
        btnSelViv.setBounds(540, 61, 110, 22);
        btnSelViv.addActionListener(e -> abrirSelectorVivienda());
        panel_DG.add(btnSelViv);

        // Rol — solo Paciente/Medico, NO Admin ni Secretaria
        new JLabel("Rol:") {{ setBounds(255, 105, 30, 14); panel_DG.add(this); }};

        rdbtnPaciente = new JRadioButton("Paciente");
        rdbtnPaciente.setBackground(SystemColor.info);
        rdbtnPaciente.setBounds(290, 100, 90, 23);
        rdbtnPaciente.addActionListener(e -> {
            rdbtnMedico.setSelected(false);
            panel_Medico.setVisible(false);
            panel_Paciente.setVisible(true);
        });
        panel_DG.add(rdbtnPaciente);

        rdbtnMedico = new JRadioButton("Medico");
        rdbtnMedico.setBackground(SystemColor.info);
        rdbtnMedico.setBounds(290, 126, 90, 23);
        rdbtnMedico.addActionListener(e -> {
            rdbtnPaciente.setSelected(false);
            panel_Medico.setVisible(true);
            panel_Paciente.setVisible(false);
        });
        panel_DG.add(rdbtnMedico);

        // Por defecto radio Medico oculto — solo visible desde CrearUser
        rdbtnMedico.setVisible(false);
        // Paciente preseleccionado por defecto
        rdbtnPaciente.setSelected(true);

        // ── Panel Medico ──────────────────────────────────────────
        panel_Medico = new JPanel();
        panel_Medico.setBorder(new TitledBorder("Datos de Medico"));
        panel_Medico.setBackground(SystemColor.info);
        panel_Medico.setBounds(10, 230, 600, 100);
        panel.add(panel_Medico);
        panel_Medico.setLayout(null);
        panel_Medico.setVisible(false);

        new JLabel("Codigo:") {{ setBounds(10, 25, 50, 14); panel_Medico.add(this); }};
        txtCodeMed = new JTextField("MED-" + Clinica.generadorCodigoidMedico);
        txtCodeMed.setEnabled(false);
        txtCodeMed.setBackground(SystemColor.info);
        txtCodeMed.setBounds(65, 22, 120, 20);
        panel_Medico.add(txtCodeMed);

        new JLabel("Especialidad:") {{ setBounds(10, 65, 90, 14); panel_Medico.add(this); }};
        txtxEspecialidad = new JTextField();
        txtxEspecialidad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c) && c != ' ') e.consume();
            }
        });
        txtxEspecialidad.setBounds(105, 62, 185, 20);
        panel_Medico.add(txtxEspecialidad);

        // ── Panel Paciente ────────────────────────────────────────
        panel_Paciente = new JPanel();
        panel_Paciente.setBorder(new TitledBorder("Datos Paciente"));
        panel_Paciente.setBackground(SystemColor.info);
        panel_Paciente.setBounds(10, 230, 600, 100);
        panel.add(panel_Paciente);
        panel_Paciente.setLayout(null);
        panel_Paciente.setVisible(true);

        new JLabel("Codigo:") {{ setBounds(10, 25, 50, 14); panel_Paciente.add(this); }};
        textCodigoPaciente = new JTextField("PAC-" + Clinica.generadorCodigoPaciente);
        textCodigoPaciente.setEnabled(false);
        textCodigoPaciente.setBackground(SystemColor.info);
        textCodigoPaciente.setBounds(66, 22, 100, 20);
        panel_Paciente.add(textCodigoPaciente);

        new JLabel("Info emergencia:") {{ setBounds(10, 58, 110, 14); panel_Paciente.add(this); }};
        textFieldInfoEmergencia = new JTextField();
        textFieldInfoEmergencia.setBounds(120, 55, 190, 20);
        panel_Paciente.add(textFieldInfoEmergencia);

        new JLabel("Tipo Sangre:") {{ setBounds(320, 25, 90, 14); panel_Paciente.add(this); }};
        comboTipoSangre = new JComboBox<>(TIPOS_SANGRE);
        comboTipoSangre.setBounds(415, 22, 90, 22);
        panel_Paciente.add(comboTipoSangre);

        // ── Botones ───────────────────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton(esModificacion ? "Actualizar" : "Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cedula   = txtCedula.getText().trim();
                String nombre   = txtNombre.getText().trim();
                String genero   = (String) comboBoxGender.getSelectedItem();
                String telefono = txtTelefono.getText().trim();

                if (cedula.isEmpty() || nombre.isEmpty()
                        || genero.equals("Elegir") || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fechaLocal = null;
                Date fecha = dateChooser.getDate();
                if (fecha != null)
                    fechaLocal = fecha.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                if (!esModificacion) {
                    if (rdbtnMedico.isSelected()) {
                        String esp = txtxEspecialidad.getText().trim();
                        if (esp.isEmpty()) {
                            JOptionPane.showMessageDialog(null,
                                "Ingrese la especialidad del medico",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Clinica.getInstance().agregarMedico(new Medico(
                            cedula, nombre, genero, fechaLocal, telefono,
                            nuevaViv, txtCodeMed.getText(), esp));
                    } else if (rdbtnPaciente.isSelected()) {
                        String sangre = (String) comboTipoSangre.getSelectedItem();
                        if (sangre.equals("(seleccionar)")) sangre = null;
                        Clinica.getInstance().agregarPaciente(new Paciente(
                            cedula, nombre, genero, fechaLocal, telefono,
                            nuevaViv, textCodigoPaciente.getText(),
                            textFieldInfoEmergencia.getText(), sangre));
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "Seleccione un rol (Medico o Paciente)",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    clean();
                    JOptionPane.showMessageDialog(null,
                        "Registrado correctamente", "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    miPersona.setNombre(nombre);
                    miPersona.setGenero(genero);
                    miPersona.setTelefono(telefono);
                    miPersona.setFechaNacimiento(fechaLocal);

                    if (miPersona instanceof Paciente) {
                        Paciente p = (Paciente) miPersona;
                        p.setInfoEmergencia(textFieldInfoEmergencia.getText());
                        String sangre = (String) comboTipoSangre.getSelectedItem();
                        p.setTipoSangre(sangre.equals("(seleccionar)") ? null : sangre);
                        p.setViviend(nuevaViv);
                        Clinica.getInstance().modificarPaciente(cedula, p);
                    } else if (miPersona instanceof Medico) {
                        Medico m = (Medico) miPersona;
                        m.setEspecialidad(txtxEspecialidad.getText().trim());
                        Clinica.getInstance().modificarMedico(cedula, m);
                    }
                    dispose();
                    JOptionPane.showMessageDialog(null,
                        "Actualizado correctamente", "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPane.add(btnRegistrar);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(ev -> dispose());
        buttonPane.add(cancelButton);

        if (miPersona != null) loadPersona();
    }

    // ── Selector de vivienda ──────────────────────────────────────
    private void abrirSelectorVivienda() {
        ArrayList<Vivienda> lista = Clinica.getInstance().getMisViviendas();
        if (lista.isEmpty()) {
            int resp = JOptionPane.showConfirmDialog(this,
                "No hay viviendas registradas.\n¿Desea crear una ahora?",
                "Sin viviendas", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                RegistrarVivienda dlgViv = new RegistrarVivienda(null, 0);
                dlgViv.setModal(true);
                dlgViv.setVisible(true);
                Clinica.getInstance().actualizarGeneradores();
                nuevaViv = dlgViv.getCasaReg();
                if (nuevaViv != null) {
                    txtVivienda.setText(nuevaViv.getIdVivienda()
                        + " — " + nuevaViv.getDireccion());
                    txtVivienda.setForeground(Color.BLACK);
                }
            }
            return;
        }

        JDialog dlg = new JDialog(this, "Seleccionar Vivienda", true);
        dlg.setSize(520, 340);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JLabel lblTit = new JLabel("  Selecciona una vivienda:", JLabel.LEFT);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTit.setBorder(new EmptyBorder(8, 4, 4, 4));
        dlg.add(lblTit, BorderLayout.NORTH);

        DefaultTableModel mdl = new DefaultTableModel(0, 2) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        mdl.setColumnIdentifiers(new String[]{"ID Vivienda", "Direccion"});
        for (Vivienda v : lista)
            mdl.addRow(new Object[]{v.getIdVivienda(), v.getDireccion()});

        JTable tbl = new JTable(mdl);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setRowHeight(24);
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tbl.getSelectedRow() >= 0)
                    seleccionarFila(tbl, lista, dlg);
            }
        });
        dlg.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnNueva = new JButton("+ Crear nueva");
        btnNueva.addActionListener(ev -> {
            dlg.dispose();
            RegistrarVivienda dlgViv = new RegistrarVivienda(null, 0);
            dlgViv.setModal(true);
            dlgViv.setVisible(true);
            Clinica.getInstance().actualizarGeneradores();
            nuevaViv = dlgViv.getCasaReg();
            if (nuevaViv != null) {
                txtVivienda.setText(nuevaViv.getIdVivienda()
                    + " — " + nuevaViv.getDireccion());
                txtVivienda.setForeground(Color.BLACK);
            }
        });
        pnl.add(btnNueva);
        JButton btnSel = new JButton("Seleccionar");
        btnSel.addActionListener(ev -> seleccionarFila(tbl, lista, dlg));
        pnl.add(btnSel);
        JButton btnCan = new JButton("Cancelar");
        btnCan.addActionListener(ev -> dlg.dispose());
        pnl.add(btnCan);
        dlg.add(pnl, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void seleccionarFila(JTable tbl, ArrayList<Vivienda> lista,
                                  JDialog dlg) {
        int fila = tbl.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(dlg,
                "Selecciona una fila primero", "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        nuevaViv = lista.get(fila);
        txtVivienda.setText(nuevaViv.getIdVivienda()
            + " — " + nuevaViv.getDireccion());
        txtVivienda.setForeground(Color.BLACK);
        dlg.dispose();
    }

    // ── Modo secretaria: solo puede registrar pacientes ───────────
    public void setSoloMostrarPaciente(boolean activo) {
        if (activo) {
            rdbtnMedico.setVisible(false);
            rdbtnPaciente.setSelected(true);
            panel_Paciente.setVisible(true);
            panel_Medico.setVisible(false);
        }
    }

    // ── Llamado desde CrearUser para médico ───────────────────────
    public void preseleccionarMedico(String idUsuario) {
        rdbtnMedico.setVisible(true);
        rdbtnMedico.setSelected(true);
        rdbtnPaciente.setSelected(false);
        panel_Medico.setVisible(true);
        panel_Paciente.setVisible(false);
        txtCodeMed.setText("MED-" + Clinica.generadorCodigoidMedico);
    }

    public void clean() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtVivienda.setText("(ninguna seleccionada)");
        txtVivienda.setForeground(Color.GRAY);
        txtxEspecialidad.setText("");
        textFieldInfoEmergencia.setText("");
        comboTipoSangre.setSelectedIndex(0);
        dateChooser.setDate(null);
        rdbtnMedico.setSelected(false);
        rdbtnPaciente.setSelected(true);
        panel_Medico.setVisible(false);
        panel_Paciente.setVisible(true);
        nuevaViv = null;
        txtCodeMed.setText("MED-" + Clinica.generadorCodigoidMedico);
        textCodigoPaciente.setText("PAC-" + Clinica.generadorCodigoPaciente);
    }

    // ── Carga datos al modificar ──────────────────────────────────
    public void loadPersona() {
        txtCedula.setText(miPersona.getCedula());
        txtNombre.setText(miPersona.getNombre());
        txtTelefono.setText(miPersona.getTelefono() != null
            ? miPersona.getTelefono() : "");
        comboBoxGender.setSelectedItem(miPersona.getGenero());

        // Fecha nacimiento
        if (miPersona.getFechaNacimiento() != null) {
            Date fecha = Date.from(miPersona.getFechaNacimiento()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(fecha);
        }

        // Vivienda
        if (miPersona.getViviend() != null) {
            nuevaViv = miPersona.getViviend();
            txtVivienda.setText(nuevaViv.getIdVivienda()
                + " — " + nuevaViv.getDireccion());
            txtVivienda.setForeground(Color.BLACK);
        }

        if (miPersona instanceof Paciente) {
            // Mostrar solo Paciente bloqueado
            rdbtnPaciente.setVisible(true);
            rdbtnPaciente.setSelected(true);
            rdbtnPaciente.setEnabled(false);
            rdbtnMedico.setVisible(false);
            panel_Paciente.setVisible(true);
            panel_Medico.setVisible(false);

            Paciente p = (Paciente) miPersona;
            // Poner el ID real del paciente, NO el generador
            textCodigoPaciente.setText(p.getIdPaciente());
            textFieldInfoEmergencia.setText(
                p.getInformacion() != null ? p.getInformacion() : "");
            String ts = p.getTipoSangre();
            if (ts != null && !ts.isEmpty())
                comboTipoSangre.setSelectedItem(ts);
            else
                comboTipoSangre.setSelectedIndex(0);
        }

        if (miPersona instanceof Medico) {
            // Mostrar solo Medico bloqueado
            rdbtnMedico.setVisible(true);
            rdbtnMedico.setSelected(true);
            rdbtnMedico.setEnabled(false);
            rdbtnPaciente.setVisible(false);
            panel_Medico.setVisible(true);
            panel_Paciente.setVisible(false);

            Medico m = (Medico) miPersona;
            txtCodeMed.setText(m.getIdMedico());
            txtxEspecialidad.setText(m.getEspecialidad());
        }
    }
}