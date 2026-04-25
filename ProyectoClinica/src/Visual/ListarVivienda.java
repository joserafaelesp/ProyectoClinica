package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
import Logical.Vivienda;

public class ListarVivienda extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtBuscar;
    private DefaultTableModel model;        // ← ya no es static
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Vivienda          selected = null;

    public ListarVivienda() {
        setTitle("Ver Viviendas");
        setBounds(100, 100, 780, 460);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setBounds(10, 75, 744, 310);
        contentPanel.add(listPanel);
        listPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // ← Caracteres corregidos (sin símbolos rotos)
        model = new DefaultTableModel(0, 2) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(
            new String[]{"ID Vivienda", "Direccion"});

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(SystemColor.activeCaption);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(550);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idVivienda = model.getValueAt(index, 0).toString();
                    selected = Clinica.getInstance().obtenervivienda(idVivienda);
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setBackground(SystemColor.scrollbar);
        opcionesPanel.setBounds(10, 11, 744, 59);
        contentPanel.add(opcionesPanel);
        opcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Lista de Viviendas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setBounds(10, 3, 694, 14);
        opcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 45, 14);
        opcionesPanel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(58, 28, 640, 20);
        opcionesPanel.add(txtBuscar);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                ListarVivienda.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(706, 25, 38, 26);
        btnBuscar.addActionListener(e -> buscarPorId());
        opcionesPanel.add(btnBuscar);

        // ── Botones inferiores ───────────────────────────────────
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPane.setBackground(SystemColor.activeCaption);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    RegistrarVivienda modificar =
                        new RegistrarVivienda(selected, 0);
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
                        "Seguro que desea borrar la vivienda con ID: "
                        + selected.getIdVivienda() + "?",
                        "Confirmacion", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarVivienda(
                            selected.getIdVivienda());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Vivienda eliminada correctamente",
                            "Exito", JOptionPane.INFORMATION_MESSAGE);
                        Clinica.getInstance().actualizarGeneradores();
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
        ArrayList<Vivienda> lista = Clinica.getInstance().getMisViviendas();
        model.setRowCount(0);
        for (Vivienda v : lista)
            model.addRow(new Object[]{
                v.getIdVivienda(),
                v.getDireccion()
            });
        System.out.println("Viviendas cargadas: " + lista.size());
    }

    private void buscarPorId() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Vivienda v : Clinica.getInstance().getMisViviendas()) {
            if (v.getIdVivienda().toLowerCase().contains(texto)
                    || v.getDireccion().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    v.getIdVivienda(),
                    v.getDireccion()
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron viviendas con ese criterio",
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