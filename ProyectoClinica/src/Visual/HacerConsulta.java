package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Clinica;
import Logical.Consultas;
import Logical.Enfermedad;
import Logical.Medico;
import Logical.Paciente;
import Logical.Usuario;

public class HacerConsulta extends JDialog {

    private final JPanel      contentPanel = new JPanel();

    private JTextField        txtCodigo;
    private JTextField        txtCedulaMed;
    private JTextField        txtCedulaPac;
    private JTextField        txtDiagnostico;

    private JComboBox<String> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<String> comboAnio;

    private DefaultTableModel modeloDisp;
    private DefaultTableModel modeloSel;
    private JTable            tablaDisp;
    private JTable            tablaSel;
    private JButton           btnAgregar;
    private JButton           btnQuitar;

    private ArrayList<Enfermedad> enfermedadesSelected = new ArrayList<Enfermedad>();
    private Usuario           usuarioActual;
    private Paciente          pacienteSeleccionado = null;

    private static final String[] MESES = {
        "(mes)", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public HacerConsulta(Usuario user) {
        this.usuarioActual = user;

        setTitle("Registrar Consulta Medica");
        setBounds(100, 100, 700, 560);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.info);
        contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // Panel datos generales
        JPanel pnlDatos = new JPanel();
        pnlDatos.setBackground(SystemColor.info);
        pnlDatos.setBorder(new TitledBorder("Datos de la consulta"));
        pnlDatos.setBounds(8, 8, 668, 140);
        pnlDatos.setLayout(null);
        contentPanel.add(pnlDatos);

        JLabel lblCod = new JLabel("Codigo:");
        lblCod.setBounds(10, 26, 70, 14);
        pnlDatos.add(lblCod);

        txtCodigo = new JTextField("CON-" + Clinica.generadorCodigoConsulta);
        txtCodigo.setEnabled(false);
        txtCodigo.setBackground(new Color(220, 220, 220));
        txtCodigo.setForeground(Color.DARK_GRAY);
        txtCodigo.setBounds(80, 23, 100, 22);
        pnlDatos.add(txtCodigo);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(195, 26, 45, 14);
        pnlDatos.add(lblFecha);

        String[] dias = new String[32];
        dias[0] = "(dia)";
        for (int i = 1; i <= 31; i++) dias[i] = String.valueOf(i);
        comboDia = new JComboBox<String>(dias);
        comboDia.setBounds(243, 23, 60, 22);
        pnlDatos.add(comboDia);

        comboMes = new JComboBox<String>(MESES);
        comboMes.setBounds(308, 23, 110, 22);
        pnlDatos.add(comboMes);

        int anioActual = LocalDate.now().getYear();
        String[] anios = new String[anioActual - 2019];
        anios[0] = "(anio)";
        for (int i = 1; i < anios.length; i++)
            anios[i] = String.valueOf(anioActual - i + 1);
        comboAnio = new JComboBox<String>(anios);
        comboAnio.setBounds(423, 23, 75, 22);
        pnlDatos.add(comboAnio);

        LocalDate hoy = LocalDate.now();
        comboDia.setSelectedIndex(hoy.getDayOfMonth());
        comboMes.setSelectedIndex(hoy.getMonthValue());
        for (int i = 0; i < comboAnio.getItemCount(); i++) {
            if (String.valueOf(hoy.getYear()).equals(comboAnio.getItemAt(i))) {
                comboAnio.setSelectedIndex(i);
                break;
            }
        }

        JLabel lblMed = new JLabel("Cedula medico:");
        lblMed.setBounds(10, 60, 100, 14);
        pnlDatos.add(lblMed);

        txtCedulaMed = new JTextField();
        txtCedulaMed.setBounds(115, 57, 150, 22);
        pnlDatos.add(txtCedulaMed);

        if (usuarioActual != null && usuarioActual.esMedico()) {
            Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            if (medActual != null) {
                txtCedulaMed.setText(medActual.getCedula());
                txtCedulaMed.setEnabled(false);
                txtCedulaMed.setBackground(new Color(220, 220, 220));
            }
        }

        JButton btnVerificarMed = new JButton("Verificar");
        btnVerificarMed.setBounds(273, 57, 80, 22);
        btnVerificarMed.addActionListener(e -> verificarMedico());
        pnlDatos.add(btnVerificarMed);

        JLabel lblPac = new JLabel("Paciente:");
        lblPac.setBounds(10, 95, 100, 14);
        pnlDatos.add(lblPac);

        txtCedulaPac = new JTextField();
        txtCedulaPac.setEditable(false);
        txtCedulaPac.setBackground(new Color(220, 220, 220));
        txtCedulaPac.setForeground(Color.DARK_GRAY);
        txtCedulaPac.setBounds(115, 92, 150, 22);
        pnlDatos.add(txtCedulaPac);

        JButton btnSelPac = new JButton("Seleccionar...");
        btnSelPac.setBounds(273, 92, 110, 22);
        btnSelPac.addActionListener(e -> abrirSelectorPaciente());
        pnlDatos.add(btnSelPac);

        JLabel lblDiag = new JLabel("Diagnostico:");
        lblDiag.setBounds(395, 95, 85, 14);
        pnlDatos.add(lblDiag);

        txtDiagnostico = new JTextField();
        txtDiagnostico.setBounds(480, 92, 178, 22);
        pnlDatos.add(txtDiagnostico);

        // Panel enfermedades
        JPanel pnlEnfs = new JPanel();
        pnlEnfs.setBackground(SystemColor.info);
        pnlEnfs.setBorder(new TitledBorder("Enfermedades diagnosticadas"));
        pnlEnfs.setBounds(8, 155, 668, 290);
        pnlEnfs.setLayout(null);
        contentPanel.add(pnlEnfs);

        JLabel lblDisp = new JLabel("Disponibles:");
        lblDisp.setBounds(10, 20, 90, 14);
        pnlEnfs.add(lblDisp);

        modeloDisp = new DefaultTableModel(0, 3);
        modeloDisp.setColumnIdentifiers(new String[]{"ID", "Nombre", "Gravedad"});
        tablaDisp = new JTable(modeloDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDisp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDisp.getTableHeader().setReorderingAllowed(false);
        tablaDisp.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaDisp.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaDisp.getColumnModel().getColumn(2).setPreferredWidth(80);
        JScrollPane spDisp = new JScrollPane(tablaDisp);
        spDisp.setBounds(10, 38, 280, 220);
        pnlEnfs.add(spDisp);

        btnAgregar = new JButton("Agregar >");
        btnAgregar.setEnabled(false);
        btnAgregar.setBounds(300, 110, 90, 28);
        btnAgregar.addActionListener(e -> agregarEnfermedad());
        pnlEnfs.add(btnAgregar);

        btnQuitar = new JButton("< Quitar");
        btnQuitar.setEnabled(false);
        btnQuitar.setBounds(300, 148, 90, 28);
        btnQuitar.addActionListener(e -> quitarEnfermedad());
        pnlEnfs.add(btnQuitar);

        JLabel lblSel = new JLabel("En esta consulta:");
        lblSel.setBounds(398, 20, 120, 14);
        pnlEnfs.add(lblSel);

        modeloSel = new DefaultTableModel(0, 2);
        modeloSel.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        tablaSel = new JTable(modeloSel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSel.getTableHeader().setReorderingAllowed(false);
        JScrollPane spSel = new JScrollPane(tablaSel);
        spSel.setBounds(398, 38, 260, 220);
        pnlEnfs.add(spSel);

        tablaDisp.getSelectionModel().addListSelectionListener(
            e -> btnAgregar.setEnabled(tablaDisp.getSelectedRow() >= 0));
        tablaSel.getSelectionModel().addListSelectionListener(
            e -> btnQuitar.setEnabled(tablaSel.getSelectedRow() >= 0));

        // Botones finales
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrar = new JButton("Registrar Consulta");
        btnRegistrar.setBackground(new Color(70, 130, 180));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrar());
        buttonPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        cargarTablaEnfermedades();
    }

    private void cargarTablaEnfermedades() {
        modeloDisp.setRowCount(0);
        for (Enfermedad e : Clinica.getInstance().getMisEnfermedades()) {
            String gravedad = (e.getGravedad() != null)
                ? e.getGravedad().getGravedad() : "(sin clasificar)";
            modeloDisp.addRow(new Object[]{
                e.getIdEnfermedad(), e.getNombreEnfermedad(), gravedad});
        }
    }

    private void agregarEnfermedad() {
        int fila = tablaDisp.getSelectedRow();
        if (fila < 0) return;
        String idEnf = (String) modeloDisp.getValueAt(fila, 0);
        Enfermedad enf = Clinica.getInstance().obtenerEnfermedadById(idEnf);
        if (enf != null && !enfermedadesSelected.contains(enf)) {
            enfermedadesSelected.add(enf);
            modeloSel.addRow(new Object[]{
                enf.getIdEnfermedad(), enf.getNombreEnfermedad()});
        }
    }

    private void quitarEnfermedad() {
        int fila = tablaSel.getSelectedRow();
        if (fila < 0) return;
        enfermedadesSelected.remove(fila);
        modeloSel.removeRow(fila);
        btnQuitar.setEnabled(false);
    }

    private void verificarMedico() {
        String ced = txtCedulaMed.getText().trim();
        if (ced.isEmpty()) return;
        Medico m = Clinica.getInstance().obtenerMedicoById(ced);
        if (m == null)
            JOptionPane.showMessageDialog(this,
                "Medico no encontrado con esa cedula",
                "Error", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(this,
                "Medico verificado: " + m.getNombre(),
                "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    private void registrar() {
        String cedulaMed   = txtCedulaMed.getText().trim();
        String cedulaPac   = txtCedulaPac.getText().trim();
        String diagnostico = txtDiagnostico.getText().trim();

        if (cedulaMed.isEmpty() || cedulaPac.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleccione el medico y el paciente antes de registrar",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Medico medico = Clinica.getInstance().obtenerMedicoById(cedulaMed);
        Paciente paciente = (pacienteSeleccionado != null)
            ? pacienteSeleccionado
            : Clinica.getInstance().obtenerPacienteById(cedulaPac);

        if (medico == null) {
            JOptionPane.showMessageDialog(this,
                "Medico no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (paciente == null) {
            JOptionPane.showMessageDialog(this,
                "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fecha = obtenerFecha();

        Consultas nueva = new Consultas(
            txtCodigo.getText(), fecha,
            enfermedadesSelected, medico, paciente);
        nueva.setDiagnostico(diagnostico);

        Clinica.getInstance().agregarConsulta(nueva);
        limpiar();

        JOptionPane.showMessageDialog(this,
            "Consulta registrada correctamente",
            "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private Date obtenerFecha() {
        try {
            int dia = comboDia.getSelectedIndex();
            int mes = comboMes.getSelectedIndex();
            String anioStr = (String) comboAnio.getSelectedItem();
            if (dia == 0 || mes == 0 || anioStr.equals("(anio)"))
                return new Date();
            int anio = Integer.parseInt(anioStr);
            return Date.from(LocalDate.of(anio, mes, dia)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            return new Date();
        }
    }

    private void abrirSelectorPaciente() {
        ArrayList<Paciente> lista = Clinica.getInstance().getMisPaciente();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay pacientes registrados en el sistema",
                "Sin pacientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog(this, "Seleccionar Paciente", true);
        dlg.setSize(580, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JLabel lblTit = new JLabel("  Selecciona un paciente:", JLabel.LEFT);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTit.setBorder(new EmptyBorder(8, 4, 4, 4));
        dlg.add(lblTit, BorderLayout.NORTH);

        DefaultTableModel mdl = new DefaultTableModel(0, 4);
        mdl.setColumnIdentifiers(new String[]{"Cedula", "Nombre", "Tipo Sangre", "ID Paciente"});
        for (Paciente p : lista) {
            String sangre = (p.getTipoSangre() != null) ? p.getTipoSangre() : "";
            mdl.addRow(new Object[]{
                p.getCedula(), p.getNombre(), sangre, p.getIdPaciente()
            });
        }

        JTable tbl = new JTable(mdl) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setRowHeight(24);
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tbl.getSelectedRow() >= 0)
                    seleccionarPaciente(tbl, lista, dlg);
            }
        });
        dlg.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSel = new JButton("Seleccionar");
        btnSel.addActionListener(ev -> seleccionarPaciente(tbl, lista, dlg));
        pnl.add(btnSel);
        JButton btnCan = new JButton("Cancelar");
        btnCan.addActionListener(ev -> dlg.dispose());
        pnl.add(btnCan);
        dlg.add(pnl, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void seleccionarPaciente(JTable tbl, ArrayList<Paciente> lista, JDialog dlg) {
        int fila = tbl.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(dlg,
                "Selecciona una fila primero", "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        pacienteSeleccionado = lista.get(fila);
        txtCedulaPac.setText(pacienteSeleccionado.getCedula()
            + "  -  " + pacienteSeleccionado.getNombre());
        dlg.dispose();
    }

    private void limpiar() {
        enfermedadesSelected.clear();
        modeloSel.setRowCount(0);
        txtDiagnostico.setText("");
        txtCedulaPac.setText("");
        pacienteSeleccionado = null;
        Clinica.generadorCodigoConsulta++;
        txtCodigo.setText("CON-" + Clinica.generadorCodigoConsulta);
        if (usuarioActual == null || !usuarioActual.esMedico())
            txtCedulaMed.setText("");
        btnAgregar.setEnabled(false);
        btnQuitar.setEnabled(false);
        LocalDate hoy = LocalDate.now();
        comboDia.setSelectedIndex(hoy.getDayOfMonth());
        comboMes.setSelectedIndex(hoy.getMonthValue());
    }
}