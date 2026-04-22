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
    
    // NUEVO: Variable global para guardar al usuario que inició sesión
    private Logical.Usuario usuarioActual;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    // NUEVO: Se envía null para que no dé error si pruebas la ventana sola
                    PrincipalVisual frame = new PrincipalVisual(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // NUEVO: El constructor ahora recibe al Usuario
    public PrincipalVisual(Logical.Usuario user) {
        this.usuarioActual = user; // Guardamos el usuario
        
        dim = getToolkit().getScreenSize();
        // Para evitar el error de ruta si no tienes la imagen, usa un try-catch o coméntalo si falla
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(PrincipalVisual.class.getResource("/imagenes/edificio-del-hospital.png")));
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

        mCitas = new JMenu("CITAS");
        menuBar.add(mCitas);

        hacerCita = new JMenuItem("Hacer Cita");
        hacerCita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HacerCita hacerCita = new HacerCita();
                hacerCita.setModal(true);
                hacerCita.setVisible(true);
            }
        });
        stylizeMenuItem(hacerCita);
        mCitas.add(hacerCita);

        mConsultas = new JMenu("CONSULTAS");
        menuBar.add(mConsultas);

        HacerConsultas = new JMenuItem("Hacer Consulta");
        HacerConsultas.setBackground(SystemColor.activeCaption);
        HacerConsultas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // NUEVO: Le enviamos el usuarioActual a la ventana de Consultas
                HacerConsulta hacerCons = new HacerConsulta(usuarioActual);
                hacerCons.setModal(true);
                hacerCons.setVisible(true);
            }
        });
        stylizeMenuItem(HacerConsultas);
        mConsultas.add(HacerConsultas);

        mRegistro = new JMenu("REGISTROS");
        menuBar.add(mRegistro);

        mntmNewMenuItem_9 = new JMenuItem("Crear Persona");
        mntmNewMenuItem_9.setBackground(SystemColor.activeCaption);
        mntmNewMenuItem_9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarGeneral regisGen = new RegistrarGeneral(null, 0);
                regisGen.setModal(true);
                regisGen.setVisible(true);
            }
        });
        stylizeMenuItem(mntmNewMenuItem_9);
        mRegistro.add(mntmNewMenuItem_9);

        crearVacuna = new JMenuItem("Crear Vacuna");
        crearVacuna.setBackground(SystemColor.activeCaption);
        crearVacuna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegistrarVacuna regisVac = new RegistrarVacuna();
                regisVac.setModal(true);
                regisVac.setVisible(true);
            }
        });
        stylizeMenuItem(crearVacuna);
        mRegistro.add(crearVacuna);

        crearEnfermedad = new JMenuItem("Crear enfermedad");
        crearEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarEnfermedad enfer = new RegistrarEnfermedad(null, 0);
                enfer.setModal(true);
                enfer.setVisible(true);
            }
        });
        crearEnfermedad.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(crearEnfermedad);
        mRegistro.add(crearEnfermedad);

        crearVivienda = new JMenuItem("Crear Vivienda");
        crearVivienda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarVivienda vivienda = new RegistrarVivienda(null,0); // Actualizado a la opción recomendada
                vivienda.setModal(true);
                vivienda.setVisible(true);
            }
        });
        crearVivienda.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(crearVivienda);
        mRegistro.add(crearVivienda);

        mInvertario = new JMenu("INVENTARIO");
        menuBar.add(mInvertario);

        listaPaciente = new JMenuItem("Listado Paciente");
        listaPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListarPaciente listar1 = new ListarPaciente();
                listar1.setModal(true);
                listar1.setVisible(true);
            }
        });
        listaPaciente.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(listaPaciente);
        mInvertario.add(listaPaciente);

        listaMedico = new JMenuItem("Listado Medico");
        listaMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListarMedico lista2 = new ListarMedico();
                lista2.setModal(true);
                lista2.setVisible(true);
            }
        });
        listaMedico.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(listaMedico);
        mInvertario.add(listaMedico);

        listadoVacuna = new JMenuItem("Listado Vacunas");
        listadoVacuna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Función en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        listadoVacuna.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(listadoVacuna);
        mInvertario.add(listadoVacuna);

        listadoEnfermedad = new JMenuItem("Listado Enfermedades");
        listadoEnfermedad.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(listadoEnfermedad);
        listadoEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Función en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        mInvertario.add(listadoEnfermedad);

        listaVivienda = new JMenuItem("Listado Vivienda");
        listaVivienda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ListarVivienda listarV = new ListarVivienda();
                listarV.setModal(true);
                listarV.setVisible(true);
            }
        });
        listaVivienda.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(listaVivienda);
        mInvertario.add(listaVivienda);

        mUtilidades = new JMenu("UTILIDADES");
        menuBar.add(mUtilidades);

        JMenuItem mntmNewMenuItem_2 = new JMenuItem("WORKING");
        mntmNewMenuItem_2.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(mntmNewMenuItem_2);
        mUtilidades.add(mntmNewMenuItem_2);

        mUSER = new JMenu("USUARIO");
        menuBar.add(mUSER);

        crearUsuario = new JMenuItem("Crear Usuario");
        crearUsuario.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(crearUsuario);
        crearUsuario.addActionListener(e -> {
            CrearUser usuario = new CrearUser(null);
            usuario.setModal(true);
            usuario.setVisible(true);
        });
        mUSER.add(crearUsuario);

        borrarUsuario = new JMenuItem("Gestionar Usuarios");
        borrarUsuario.setBackground(SystemColor.activeCaption);
        stylizeMenuItem(borrarUsuario);
        borrarUsuario.addActionListener(e -> {
            VerMisUsuarios usuarios = new VerMisUsuarios();
            usuarios.setModal(true);
            usuarios.setVisible(true);
        });
        mUSER.add(borrarUsuario);

        lblSepa = new JLabel("--------------INFO-EXTRA---------------");
        menuBar.add(lblSepa);

        JLabel lblNewLabel = new JLabel(" USUARIO ACTIVO: - ");
        menuBar.add(lblNewLabel);

        // NUEVO: Muestra el nombre real del usuario en la barra
        String nombreLabel = (usuarioActual != null) ? usuarioActual.getNombreUser() : "Invitado";
        lblUser = new JLabel("  " + nombreLabel + "  ");
        lblUser.setForeground(new Color(0, 153, 0));
        menuBar.add(lblUser);
        
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.window);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.window);
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // Para evitar el error de ruta si no tienes la imagen
        try {
            JLabel lblNewLabel_1 = new JLabel("");
            lblNewLabel_1.setIcon(new ImageIcon(PrincipalVisual.class.getResource("/imagenes/caduceo.png")));
            lblNewLabel_1.setBounds(870, 367, 64, 104);
            panel.add(lblNewLabel_1);
        } catch (Exception e) {
            System.out.println("Imagen de fondo no encontrada");
        }
    }

    private void stylizeMenuItem(JMenuItem menuItem) {
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuItem.setForeground(Color.BLACK);
        menuItem.setBackground(SystemColor.activeCaption);
        menuItem.setPreferredSize(new Dimension(180, 40));
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}