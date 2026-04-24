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
import Logical.Usuario;

public class VerMisUsuarios extends JDialog {

    private final JPanel      contentPanel = new JPanel();
    private JTable            table;
    private JTextField        txtNombre;
    private DefaultTableModel model;
    private JButton           btnBorrar;
    private JButton           btnModificar;
    private Usuario           selected = null;

    // ── Sin main() para evitar que se abra sola ──────────────────

    public VerMisUsuarios() {
        setTitle("Gestionar Usuarios");
        setBounds(100, 100, 800, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.window);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Tabla ────────────────────────────────────────────────
        JPanel ListPanel = new JPanel();
        ListPanel.setBounds(10, 75, 764, 350);
        contentPanel.add(ListPanel);
        ListPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ListPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(
            new String[]{"ID", "Username", "Password", "Rol"});

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    btnBorrar.setEnabled(true);
                    btnModificar.setEnabled(true);
                    String idUsuario = model.getValueAt(index, 0).toString();
                    selected = Clinica.getInstance()
                        .buscarUsuarioPorCodigo(idUsuario);
                    System.out.println("Usuario seleccionado: "
                        + selected.getNombreUser());
                }
            }
        });
        scrollPane.setViewportView(table);

        // ── Barra de búsqueda ────────────────────────────────────
        JPanel OpcionesPanel = new JPanel();
        OpcionesPanel.setBackground(SystemColor.scrollbar);
        OpcionesPanel.setBounds(10, 11, 764, 59);
        contentPanel.add(OpcionesPanel);
        OpcionesPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Lista de usuarios");
        lblTitulo.setBounds(10, 3, 694, 14);
        OpcionesPanel.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 31, 38, 14);
        OpcionesPanel.add(lblBuscar);

        txtNombre = new JTextField();
        txtNombre.setBounds(53, 28, 653, 20);
        OpcionesPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JButton btnBuscar = new JButton("");
        try {
            btnBuscar.setIcon(new ImageIcon(
                VerMisUsuarios.class.getResource(
                    "/imagenes/busqueda-de-lupa (1).png")));
        } catch (Exception ex) { btnBuscar.setText("Buscar"); }
        btnBuscar.setBounds(716, 28, 38, 22);
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
                    // Pasar el objeto Usuario completo a CrearUser
                    CrearUser modificar = new CrearUser(selected);
                    modificar.setModal(true);
                    modificar.setVisible(true);
                    // Recargar tabla después de cerrar el diálogo
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
                        "¿Seguro que desea borrar al usuario '"
                        + selected.getNombreUser() + "' con ID "
                        + selected.getIdUsuario() + "?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        Clinica.getInstance().borrarUsuario(
                            selected.getIdUsuario());
                        cargarDatos();
                        resetSeleccion();
                        JOptionPane.showMessageDialog(null,
                            "Usuario eliminado correctamente",
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

        cargarDatos();
    }

    private void cargarDatos() {
        ArrayList<Usuario> lista = Clinica.getInstance().getMisUsuarios();
        model.setRowCount(0);
        for (Usuario u : lista)
            model.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombreUser(),
                u.getPassword(),
                u.getRol()
            });
        System.out.println("Usuarios cargados: " + lista.size());
    }

    private void buscarPorNombre() {
        String texto = txtNombre.getText().trim().toLowerCase();
        model.setRowCount(0);
        boolean encontrado = false;
        for (Usuario u : Clinica.getInstance().getMisUsuarios()) {
            if (u.getNombreUser().toLowerCase().contains(texto)) {
                model.addRow(new Object[]{
                    u.getIdUsuario(), u.getNombreUser(),
                    u.getPassword(), u.getRol()
                });
                encontrado = true;
            }
        }
        if (!encontrado && !texto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron usuarios con ese nombre",
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