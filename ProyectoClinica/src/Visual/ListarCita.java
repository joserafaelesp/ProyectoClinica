package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Logical.Cita;
import Logical.Usuario;
import Logical.Clinica;

public class ListarCita extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Cita              selected = null;
    private Usuario          usuarioActual;

    public ListarCita(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Gestion de Citas");
        setBounds(100, 100, 950, 520);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setBounds(10, 75, 914, 370);
        contentPanel.add(listPanel);
        listPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel(0, 7) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "ID Cita", "ID Paciente", "Paciente", "ID Medico",
            "Medico", "Especialidad", "Fecha", "Hora", "Completada"});

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(SystemColor.activeCaption);

        // Ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(65);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(85);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    String idCita = model.getValueAt(index, 0).toString();
                    selected = Clinica.getInstance().obtenerCitaById(idCita);
                    btnBorrar.setEnabled(true);
                    // Modificar solo si la cita NO está completada
                    btnModificar.setEnabled(selected != null && !selected.isCompletada());
                    if (selected != null && selected.isCompletada()) {
                        btnModificar.setToolTipText("No se puede modificar una cita completada");
                    } else {
                        btnModificar.setToolTipText(null);
                    }
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setBackground(SystemColor.scrollbar);
        opcionesPanel.setBounds(10, 11, 914, 59);
        contentPanel.add(opcionesPanel);
        opcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Lista de Citas Medicas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setBounds(10, 3, 694, 14);
        opcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 45, 14);
        opcionesPanel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(58, 28, 806, 20);
        opcionesPanel.add(txtBuscar);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                ListarCita.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(866, 25, 38, 26);
        btnBuscar.addActionListener(e -> buscarPorNombre());
        opcionesPanel.add(btnBuscar);

        // ── Botones inferiores ───────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        // FIX: el médico no puede modificar ni borrar citas, solo completarlas
        btnModificar.setVisible(usuarioActual == null || !usuarioActual.esMedico());
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    // Abrir HacerCita en modo modificación
                    HacerCita modificar = new HacerCita(selected);
                    modificar.setModal(true);
                    modificar.setVisible(true);
                    cargarDatos();
                    resetSeleccion();
                }
            }
        });
        buttonPane.add(btnModificar);

        btnBorrar = new JButton("Borrar");
        btnBorrar.setEnabled(false);
        // FIX: el médico no puede borrar citas
        btnBorrar.setVisible(usuarioActual == null || !usuarioActual.esMedico());
        btnBorrar.setBackground(new Color(200, 60, 60));
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    int conf = JOptionPane.showConfirmDialog(null,
                        "Seguro que desea eliminar la cita "
                        + selected.getIdCita() + "?",
                        "Confirmacion", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarCita(selected.getIdCita());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Cita eliminada correctamente",
                            "Exito", JOptionPane.INFORMATION_MESSAGE);
                        Clinica.getInstance().actualizarGeneradores();
                    }
                }
            }
        });
        buttonPane.add(btnBorrar);

        // Boton Marcar Completada — solo visible para el medico
        JButton btnCompletar = new JButton("Marcar Completada");
        btnCompletar.setBackground(new Color(60, 160, 80));
        btnCompletar.setForeground(Color.WHITE);
        btnCompletar.setFocusPainted(false);
        btnCompletar.setVisible(usuarioActual != null && usuarioActual.esMedico());
        btnCompletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    if (selected.isCompletada()) {
                        JOptionPane.showMessageDialog(null,
                            "Esta cita ya fue completada anteriormente.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String msgConf = "Marcar la cita como completada y registrar la consulta?"
                        + "\nPaciente: " + (selected.getPaciente() != null
                            ? selected.getPaciente().getNombre() : "(sin paciente)")
                        + "\nMedico:   " + (selected.getDoc() != null
                            ? selected.getDoc().getNombre() : "(sin medico)");
                    int conf = JOptionPane.showConfirmDialog(null,
                        msgConf, "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        // NO marcar como completada todavía
                        // Se marcará solo si el médico confirma la consulta
                        HacerConsulta hc = new HacerConsulta(
                            usuarioActual, selected);
                        hc.setModal(true);
                        hc.setVisible(true);
                        // Recargar después — si se registró la consulta
                        // HacerConsulta ya marcó la cita como completada
                        cargarDatos();
                        resetSeleccion();
                    }
                }
            }
        });
        buttonPane.add(btnCompletar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        cargarDatos();
    }

    private void cargarDatos() {
        ArrayList<Cita> lista;
        // Si el usuario es médico, solo mostrar sus citas
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            if (medActual != null) {
                lista = Clinica.getInstance()
                    .getCitasPorMedico(medActual.getCedula());
            } else {
                lista = new ArrayList<>();
            }
        } else {
            // Secretaria y admin ven todas las citas
            lista = Clinica.getInstance().getMisCitas();
        }
        model.setRowCount(0);
        for (Cita c : lista) {
            String paciente    = c.getPaciente() != null
                ? c.getPaciente().getNombre() : "(sin paciente)";
            String medico      = c.getMedico() != null
                ? c.getMedico().getNombre() : "(sin medico)";
            String especialidad = c.getMedico() != null
                ? c.getMedico().getEspecialidad() : "";
            String fecha       = c.getFecha() != null
                ? c.getFecha().toString() : "";
            String hora        = c.getHoraCita() != null
                ? c.getHoraCita() : "";
            String completada  = c.isCompletada() ? "Si" : "Pendiente";

            String idPac = c.getPaciente() != null
                ? c.getPaciente().getIdPaciente() : "";
            String idMed = c.getDoc() != null
                ? c.getDoc().getIdMedico() : "";
            model.addRow(new Object[]{
                c.getIdCita(), idPac, paciente,
                idMed, medico, especialidad, fecha, hora, completada
            });
        }
        System.out.println("Citas cargadas: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        ArrayList<Cita> listaBuscar;
        if (usuarioActual != null && usuarioActual.esMedico()) {
            Logical.Medico medActual = Clinica.getInstance()
                .obtenerMedicoById(usuarioActual.getIdUsuario());
            listaBuscar = medActual != null
                ? Clinica.getInstance().getCitasPorMedico(medActual.getCedula())
                : new ArrayList<>();
        } else {
            listaBuscar = Clinica.getInstance().getMisCitas();
        }
        for (Cita c : listaBuscar) {
            String paciente = c.getPaciente() != null
                ? c.getPaciente().getNombre().toLowerCase() : "";
            String medico   = c.getMedico() != null
                ? c.getMedico().getNombre().toLowerCase() : "";
            if (paciente.contains(texto) || medico.contains(texto)
                    || c.getIdCita().toLowerCase().contains(texto)) {
                String completada = c.isCompletada() ? "Si" : "Pendiente";
                model.addRow(new Object[]{
                    c.getIdCita(),
                    c.getPaciente() != null ? c.getPaciente().getNombre() : "",
                    c.getMedico()   != null ? c.getMedico().getNombre()   : "",
                    c.getMedico()   != null ? c.getMedico().getEspecialidad() : "",
                    c.getFecha()    != null ? c.getFecha().toString() : "",
                    c.getHoraCita() != null ? c.getHoraCita() : "",
                    completada
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron citas con ese criterio",
                "Busqueda", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        }
    }

    private void resetSeleccion() {
        selected = null;
        btnBorrar.setEnabled(false);
        btnModificar.setEnabled(false);
    }
}