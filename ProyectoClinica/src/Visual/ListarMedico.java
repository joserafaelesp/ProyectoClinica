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
import Logical.Medico;

public class ListarMedico extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtNombre;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Medico            selected     = null;
    private boolean           soloLectura  = false; // ← modo secretaria

    // Constructor normal — con permisos completos
    public ListarMedico() {
        this(false);
    }

    // Constructor con modo solo lectura para secretaria
    public ListarMedico(boolean soloLectura) {
        this.soloLectura = soloLectura;

        setTitle("Ver Médicos");
        setBounds(100, 100, 850, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel ListPanel = new JPanel();
        ListPanel.setBounds(10, 75, 814, 350);
        contentPanel.add(ListPanel);
        ListPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ListPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "Cedula", "Nombre", "Genero", "Telefono", "ID Médico", "Especialidad"});

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0 && !soloLectura) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idMedico = model.getValueAt(index, 4).toString();
                    selected = Clinica.getInstance().obtenerMedicoById(idMedico);
                    System.out.println("Médico seleccionado: " + selected.getNombre());
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel OpcionesPanel = new JPanel();
        OpcionesPanel.setBackground(SystemColor.scrollbar);
        OpcionesPanel.setBounds(10, 11, 814, 59);
        contentPanel.add(OpcionesPanel);
        OpcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel(soloLectura
            ? "Lista de Médicos (solo lectura)"
            : "Lista de Médicos");
        lblTitulo.setBounds(10, 3, 694, 14);
        OpcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 38, 14);
        OpcionesPanel.add(lblBuscar);

        txtNombre = new JTextField();
        txtNombre.setBounds(53, 28, 703, 20);
        OpcionesPanel.add(txtNombre);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                ListarMedico.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(766, 28, 38, 22);
        btnBuscar.addActionListener(e -> buscarPorNombre());
        OpcionesPanel.add(btnBuscar);

        // ── Botones inferiores ───────────────────────────────────
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(SystemColor.activeCaption);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        // Solo visible si NO es solo lectura
        btnModificar.setVisible(!soloLectura);
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
        // Solo visible si NO es solo lectura
        btnBorrar.setVisible(!soloLectura);
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    int conf = JOptionPane.showConfirmDialog(null,
                        "¿Seguro que desea borrar al médico '"
                        + selected.getNombre() + "'?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarMedico(selected.getCedula());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Médico eliminado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        buttonPane.add(btnBorrar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());
        buttonPane.add(btnSalir);

        cargarDatos();
    }

    private void cargarDatos() {
        ArrayList<Medico> lista = Clinica.getInstance().getMisMedico();
        model.setRowCount(0);
        for (Medico m : lista)
            model.addRow(new Object[]{
                m.getCedula(),
                m.getNombre(),
                m.getGenero() != null ? m.getGenero() : "",
                m.getTelefono() != null ? m.getTelefono() : "",
                m.getIdMedico(),
                m.getEspecialidad()
            });
        System.out.println("Médicos cargados: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtNombre.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Medico m : Clinica.getInstance().getMisMedico()) {
            if (m.getNombre().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    m.getCedula(), m.getNombre(),
                    m.getGenero() != null ? m.getGenero() : "",
                    m.getTelefono() != null ? m.getTelefono() : "",
                    m.getIdMedico(), m.getEspecialidad()
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron médicos con ese nombre",
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