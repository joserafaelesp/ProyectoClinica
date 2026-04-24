package Visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PrincipalVisual extends JFrame {

    private JPanel contentPane;
    public JLabel lblSepa;
    private JMenuBar menuBar;
    public JLabel lblUser;
    public JMenuItem hacerCita;
    public JMenuItem HacerConsultas;
    public JMenu mRegistro;
    public JMenuItem mntmNewMenuItem_9;
    public JMenuItem crearVacuna;
    public JMenu mUSER;
    public JMenu mUtilidades;
    public JMenu mInvertario;
    public JMenu mConsultas;
    public JMenu mCitas;
    public JMenuItem crearUsuario;
    public JMenuItem borrarUsuario;
    public JMenuItem listaPaciente;
    public JMenuItem listaMedico;
    public JMenuItem listadoVacuna;
    public JMenuItem listadoEnfermedad;
    public JMenuItem listaVivienda;
    private Dimension dim;
    public JMenuItem crearEnfermedad;
    public JMenuItem crearVivienda;
    private Logical.Usuario usuarioActual;
    private boolean modoSecretaria = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    PrincipalVisual frame = new PrincipalVisual(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PrincipalVisual(Logical.Usuario user) {
        this.usuarioActual = user;

        dim = getToolkit().getScreenSize();
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                PrincipalVisual.class.getResource(
                    "/imagenes/edificio-del-hospital.png")));
        } catch (Exception e) {
            System.out.println("Icono no encontrado");
        }

        setTitle("MENU CLINICA");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setSize(dim.width, dim.height - 40);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menuBar.setBackground(SystemColor.activeCaption);
        setJMenuBar(menuBar);

        // ── CITAS ────────────────────────────────────────────────
        mCitas = new JMenu("CITAS");
        menuBar.add(mCitas);
        hacerCita = new JMenuItem("Hacer Cita");
        hacerCita.addActionListener(e -> {
            HacerCita hc = new HacerCita();
            hc.setModal(true); hc.setVisible(true);
        });
        stylizeMenuItem(hacerCita);
        mCitas.add(hacerCita);

        // ── CONSULTAS ────────────────────────────────────────────
        mConsultas = new JMenu("CONSULTAS");
        menuBar.add(mConsultas);
        HacerConsultas = new JMenuItem("Hacer Consulta");
        HacerConsultas.addActionListener(e -> {
            HacerConsulta hcons = new HacerConsulta(usuarioActual);
            hcons.setModal(true); hcons.setVisible(true);
        });
        stylizeMenuItem(HacerConsultas);
        mConsultas.add(HacerConsultas);

        // ── REGISTROS ────────────────────────────────────────────
        mRegistro = new JMenu("REGISTROS");
        menuBar.add(mRegistro);

        // Crear Persona — en modo secretaria solo permite pacientes
        mntmNewMenuItem_9 = new JMenuItem("Crear Persona");
        mntmNewMenuItem_9.addActionListener(e -> {
            RegistrarGeneral rg = new RegistrarGeneral(null, 0);
            if (modoSecretaria) rg.setSoloMostrarPaciente(true);
            rg.setModal(true); rg.setVisible(true);
        });
        stylizeMenuItem(mntmNewMenuItem_9);
        mRegistro.add(mntmNewMenuItem_9);

        crearVacuna = new JMenuItem("Crear Vacuna");
        crearVacuna.addActionListener(e -> {
            RegistrarVacuna rv = new RegistrarVacuna();
            rv.setModal(true); rv.setVisible(true);
        });
        stylizeMenuItem(crearVacuna);
        mRegistro.add(crearVacuna);

        crearEnfermedad = new JMenuItem("Crear Enfermedad");
        crearEnfermedad.addActionListener(e -> {
            RegistrarEnfermedad re = new RegistrarEnfermedad(null, 0);
            re.setModal(true); re.setVisible(true);
        });
        stylizeMenuItem(crearEnfermedad);
        mRegistro.add(crearEnfermedad);

        crearVivienda = new JMenuItem("Crear Vivienda");
        crearVivienda.addActionListener(e -> {
            RegistrarVivienda vivienda = new RegistrarVivienda(null, 0);
            vivienda.setModal(true); vivienda.setVisible(true);
        });
        stylizeMenuItem(crearVivienda);
        mRegistro.add(crearVivienda);

        // ── INVENTARIO ───────────────────────────────────────────
        mInvertario = new JMenu("INVENTARIO");
        menuBar.add(mInvertario);

        listaPaciente = new JMenuItem("Listado Paciente");
        listaPaciente.addActionListener(e -> {
            ListarPaciente lp = new ListarPaciente();
            lp.setModal(true); lp.setVisible(true);
        });
        stylizeMenuItem(listaPaciente);
        mInvertario.add(listaPaciente);

        // Listado Médico — pasa modoSecretaria para solo lectura
        listaMedico = new JMenuItem("Listado Medico");
        listaMedico.addActionListener(e -> {
            ListarMedico lm = new ListarMedico(modoSecretaria);
            lm.setModal(true); lm.setVisible(true);
        });
        stylizeMenuItem(listaMedico);
        mInvertario.add(listaMedico);

        listadoVacuna = new JMenuItem("Listado Vacunas");
        listadoVacuna.addActionListener(e -> {
            ListarVacuna lv = new ListarVacuna();
            lv.setModal(true); lv.setVisible(true);
        });
        stylizeMenuItem(listadoVacuna);
        mInvertario.add(listadoVacuna);

        listadoEnfermedad = new JMenuItem("Listado Enfermedades");
        listadoEnfermedad.addActionListener(e -> {
            ListarEnfermedad le = new ListarEnfermedad();
            le.setModal(true); le.setVisible(true);
        });
        stylizeMenuItem(listadoEnfermedad);
        mInvertario.add(listadoEnfermedad);

        listaVivienda = new JMenuItem("Listado Vivienda");
        listaVivienda.addActionListener(e -> {
            ListarVivienda listarV = new ListarVivienda();
            listarV.setModal(true); listarV.setVisible(true);
        });
        stylizeMenuItem(listaVivienda);
        mInvertario.add(listaVivienda);

        // ── UTILIDADES ───────────────────────────────────────────
        mUtilidades = new JMenu("UTILIDADES");
        menuBar.add(mUtilidades);
        JMenuItem mntmWorking = new JMenuItem("WORKING");
        stylizeMenuItem(mntmWorking);
        mUtilidades.add(mntmWorking);

        // ── USUARIO ──────────────────────────────────────────────
        mUSER = new JMenu("USUARIO");
        menuBar.add(mUSER);
        crearUsuario = new JMenuItem("Crear Usuario");
        stylizeMenuItem(crearUsuario);
        crearUsuario.addActionListener(e -> {
            CrearUser usuario = new CrearUser(null);
            usuario.setModal(true); usuario.setVisible(true);
        });
        mUSER.add(crearUsuario);

        borrarUsuario = new JMenuItem("Gestionar Usuarios");
        stylizeMenuItem(borrarUsuario);
        borrarUsuario.addActionListener(e -> {
            VerMisUsuarios usuarios = new VerMisUsuarios();
            usuarios.setModal(true); usuarios.setVisible(true);
        });
        mUSER.add(borrarUsuario);

        // ── INFO EN BARRA ────────────────────────────────────────
        lblSepa = new JLabel("--------------INFO-EXTRA---------------");
        menuBar.add(lblSepa);
        JLabel lblNewLabel = new JLabel(" USUARIO ACTIVO: - ");
        menuBar.add(lblNewLabel);
        String nombreLabel = (usuarioActual != null)
            ? usuarioActual.getNombreUser() : "Invitado";
        lblUser = new JLabel("  " + nombreLabel + "  ");
        lblUser.setForeground(new Color(0, 153, 0));
        menuBar.add(lblUser);

        // ── CONTENIDO PRINCIPAL ──────────────────────────────────
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.window);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.window);
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        try {
            JLabel lblImagen = new JLabel("");
            lblImagen.setIcon(new ImageIcon(
                PrincipalVisual.class.getResource("/imagenes/caduceo.png")));
            lblImagen.setBounds(870, 367, 64, 104);
            panel.add(lblImagen);
        } catch (Exception e) {
            System.out.println("Imagen de fondo no encontrada");
        }
    }

    /**
     * Activa el modo secretaria:
     * - ListarMedico abre en solo lectura
     * - RegistrarGeneral solo muestra opción Paciente
     */
    public void setModoSecretaria(boolean activo) {
        this.modoSecretaria = activo;
    }

    private void stylizeMenuItem(JMenuItem menuItem) {
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuItem.setForeground(Color.BLACK);
        menuItem.setBackground(SystemColor.activeCaption);
        menuItem.setPreferredSize(new Dimension(180, 40));
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}