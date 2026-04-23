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
import Logical.Enfermedad;

public class ListarEnfermedad extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Enfermedad        selected = null;

    // ── Sin main() para evitar que se abra sola ──────────────────

    public ListarEnfermedad() {
        setTitle("Ver Enfermedades");
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

        // Columnas específicas de ENFERMEDAD
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(
            new String[]{"ID Enfermedad", "Nombre Enfermedad",
                         "Síntomas", "Gravedad"});

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idEnf = table.getValueAt(index, 0).toString();
                    selected = Clinica.getInstance()
                        .obtenerEnfermedadById(idEnf);
                    System.out.println("Enfermedad seleccionada: "
                        + selected.getNombreEnfermedad());
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Panel de búsqueda ────────────────────────────────────
        JPanel OpcionesPanel = new JPanel();
        OpcionesPanel.setBackground(SystemColor.scrollbar);
        OpcionesPanel.setBounds(10, 11, 814, 59);
        contentPanel.add(OpcionesPanel);
        OpcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Lista de Enfermedades");
        lblTitulo.setBounds(10, 3, 694, 14);
        OpcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 38, 14);
        OpcionesPanel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(53, 28, 703, 20);
        OpcionesPanel.add(txtBuscar);
        txtBuscar.setColumns(10);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                ListarEnfermedad.class.getResource(
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
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    RegistrarEnfermedad modificar =
                        new RegistrarEnfermedad(selected, 0);
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
                        "¿Seguro que desea borrar la enfermedad '"
                        + selected.getNombreEnfermedad() + "'?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarEnfermedad(
                            selected.getIdEnfermedad());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Enfermedad eliminada correctamente",
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
        ArrayList<Enfermedad> lista =
            Clinica.getInstance().getMisEnfermedades();
        model.setRowCount(0);
        for (Enfermedad enf : lista)
            model.addRow(new Object[]{
                enf.getIdEnfermedad(),
                enf.getNombreEnfermedad(),
                enf.getSintomas() != null ? enf.getSintomas() : "",
                enf.getGravedad() != null
                    ? enf.getGravedad().getGravedad()
                    : "Sin clasificar"
            });
        System.out.println("Enfermedades cargadas: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Enfermedad enf : Clinica.getInstance().getMisEnfermedades()) {
            if (enf.getNombreEnfermedad().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    enf.getIdEnfermedad(),
                    enf.getNombreEnfermedad(),
                    enf.getSintomas() != null ? enf.getSintomas() : "",
                    enf.getGravedad() != null
                        ? enf.getGravedad().getGravedad()
                        : "Sin clasificar"
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron enfermedades con ese nombre",
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