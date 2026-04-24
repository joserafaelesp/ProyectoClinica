package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class CargarLogin extends JFrame {

    private JPanel       contentPane;
    public  JProgressBar Barra;
    public  JLabel       lblEsperar;
    // ← eliminado: Login verLogin = new Login() — creaba Login innecesariamente

    public CargarLogin() {
        setAutoRequestFocus(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 461, 272);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.activeCaption);
        contentPane.add(panel);
        panel.setLayout(null);

        // Barra de progreso
        Barra = new JProgressBar();
        Barra.setStringPainted(true);
        Barra.setForeground(new Color(50, 205, 50));
        Barra.setBounds(10, 198, 425, 43);
        panel.add(Barra);

        // Título
        JLabel lblTitulo = new JLabel("Clinica S.R.L");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setBounds(128, 11, 198, 48);
        panel.add(lblTitulo);

        // Label de espera
        lblEsperar = new JLabel("Por favor Espere");
        lblEsperar.setBounds(10, 182, 150, 14);
        panel.add(lblEsperar);

        // Imagen
        JLabel lblImagen = new JLabel("");
        try {
            lblImagen.setIcon(new ImageIcon(
                CargarLogin.class.getResource(
                    "/imagenes/edificio-del-hospital (3).png")));
        } catch (Exception ex) {
            System.out.println("Imagen de carga no encontrada");
        }
        lblImagen.setBounds(155, 53, 144, 136);
        panel.add(lblImagen);
    }
}