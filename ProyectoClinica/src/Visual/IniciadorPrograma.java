package Visual;

public class IniciadorPrograma {

    public static void main(String[] args) {
        // Inicializar la BD en segundo plano mientras carga
        Logical.Clinica.getInstance();

        CargarLogin cargar = new CargarLogin();
        Login       login  = new Login();

        cargar.setLocationRelativeTo(null);
        cargar.setResizable(false);
        cargar.setUndecorated(true);
        cargar.setVisible(true);

        for (int i = 1; i <= 100; i++) {
            try {
                Thread.sleep(60);
                cargar.Barra.setValue(i);

                // Animar el texto de espera
                if (i % 2 == 0) {
                    cargar.lblEsperar.setText("Por favor Esperar..");
                } else {
                    cargar.lblEsperar.setText("Por favor Esperar...");
                }

                if (i == 100) {
                    cargar.dispose();
                    login.setLocationRelativeTo(null);
                    login.setResizable(false);
                    login.setUndecorated(true);
                    login.setVisible(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}