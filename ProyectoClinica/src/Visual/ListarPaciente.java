package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

import Logical.Clinica;
import Logical.Paciente;

public class ListarPaciente extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtNombre;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Paciente          selected    = null;
    private boolean           soloLectura = false;

    public ListarPaciente() { this(false); }

    public ListarPaciente(boolean soloLectura) {
        this.soloLectura = soloLectura;
        setTitle("Ver Pacientes");
        setBounds(100, 100, 950, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel ListPanel = new JPanel();
        ListPanel.setBounds(10, 75, 914, 350);
        contentPanel.add(ListPanel);
        ListPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ListPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // ✅ Fecha de Nacimiento agregada como columna
        model.setColumnIdentifiers(new String[]{
            "Cedula", "Nombre", "Genero", "Fecha Nacimiento",
            "Telefono", "ID Paciente", "Tipo Sangre", "Info Emergencia"});

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idPaciente = model.getValueAt(index, 5).toString();
                    selected = Clinica.getInstance().obtenerPacienteById(idPaciente);
                    System.out.println("Paciente seleccionado: "
                        + selected.getNombre());
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel OpcionesPanel = new JPanel();
        OpcionesPanel.setBackground(SystemColor.scrollbar);
        OpcionesPanel.setBounds(10, 11, 914, 59);
        contentPanel.add(OpcionesPanel);
        OpcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Lista de Pacientes");
        lblTitulo.setBounds(10, 3, 694, 14);
        OpcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 38, 14);
        OpcionesPanel.add(lblBuscar);

        txtNombre = new JTextField();
        txtNombre.setBounds(53, 28, 803, 20);
        OpcionesPanel.add(txtNombre);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                ListarPaciente.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(866, 28, 38, 22);
        btnBuscar.addActionListener(e -> buscarPorNombre());
        OpcionesPanel.add(btnBuscar);

        // ── Botones inferiores ───────────────────────────────────
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(SystemColor.activeCaption);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    RegistrarGeneral modificar =
                        new RegistrarGeneral(selected, 0);
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
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    int conf = JOptionPane.showConfirmDialog(null,
                        "¿Seguro que desea borrar al paciente "
                        + selected.getNombre() + "?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarPaciente(
                            selected.getCedula());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Paciente eliminado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    Clinica.getInstance().actualizarGeneradores();
                    }
                }
            }
        });
        buttonPane.add(btnBorrar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        // Modo solo lectura — ocultar modificar y borrar
        if (soloLectura) {
            btnModificar.setVisible(false);
            btnBorrar.setVisible(false);
        }

        cargarDatos();
    }

    private void cargarDatos() {
        ArrayList<Paciente> lista = Clinica.getInstance().getMisPaciente();
        model.setRowCount(0);
        for (Paciente p : lista) {
            // Fecha formateada dd/MM/yyyy
            String fecha = "";
            if (p.getFechaNacimiento() != null)
                fecha = p.getFechaNacimiento().toString(); // LocalDate.toString() = yyyy-MM-dd
            model.addRow(new Object[]{
                p.getCedula(),
                p.getNombre(),
                p.getGenero(),
                fecha,
                p.getTelefono(),
                p.getIdPaciente(),
                p.getTipoSangre() != null ? p.getTipoSangre() : "",
                p.getInfoEmergencia() != null ? p.getInfoEmergencia() : ""
            });
        }
        System.out.println("Pacientes cargados: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtNombre.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Paciente p : Clinica.getInstance().getMisPaciente()) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                String fecha = p.getFechaNacimiento() != null
                    ? p.getFechaNacimiento().toString() : "";
                model.addRow(new Object[]{
                    p.getCedula(), p.getNombre(), p.getGenero(), fecha,
                    p.getTelefono(), p.getIdPaciente(),
                    p.getTipoSangre() != null ? p.getTipoSangre() : "",
                    p.getInfoEmergencia() != null ? p.getInfoEmergencia() : ""
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron pacientes con ese nombre",
                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        }
    }

    private void resetSeleccion() {
        selected = null;
        btnBorrar.setEnabled(false);
        btnModificar.setEnabled(false);
    }
}