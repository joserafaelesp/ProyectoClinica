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
import Logical.Vacuna;

public class ListarVacuna extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Vacuna            selected = null;

    // ── Sin main() para evitar que se abra sola ──────────────────

    public ListarVacuna() {
        setTitle("Ver Vacunas");
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

        // Columnas específicas de VACUNA
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(
            new String[]{"ID Vacuna", "Nombre Vacuna", "Descripción"});

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idVacuna = table.getValueAt(index, 0).toString();
                    selected = Clinica.getInstance().obtenervacuna(idVacuna);
                    System.out.println("Vacuna seleccionada: "
                        + selected.getNombreVacuna());
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

        JLabel lblTitulo = new JLabel("Lista de Vacunas");
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
                ListarVacuna.class.getResource(
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
                    RegistrarVacuna modificar =
                        new RegistrarVacuna(selected, 0);
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
                        "¿Seguro que desea borrar la vacuna '"
                        + selected.getNombreVacuna() + "'?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarVacuna(
                            selected.getIdVacuna());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Vacuna eliminada correctamente",
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
        ArrayList<Vacuna> lista = Clinica.getInstance().getMisVacunas();
        model.setRowCount(0);
        for (Vacuna v : lista)
            model.addRow(new Object[]{
                v.getIdVacuna(),
                v.getNombreVacuna(),
                v.getDescripcion() != null ? v.getDescripcion() : ""
            });
        System.out.println("Vacunas cargadas: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Vacuna v : Clinica.getInstance().getMisVacunas()) {
            if (v.getNombreVacuna().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    v.getIdVacuna(),
                    v.getNombreVacuna(),
                    v.getDescripcion() != null ? v.getDescripcion() : ""
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron vacunas con ese nombre",
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