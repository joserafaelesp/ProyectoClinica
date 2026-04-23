package Logical;

/**
 * Usuario.java — Secretaria y Administrador se manejan por el campo rol.
 * No hay clases separadas para cada subtipo.
 */
public class Usuario {

    private String idUsuario;
    private String nombreUser;
    private String password;
    private String rol;        // 'Administrador' | 'Secretaria' | 'Medico'

    public Usuario(String idUsuario, String nombreUser, String password, String rol) {
        this.idUsuario  = idUsuario;
        this.nombreUser = nombreUser;
        this.password   = password;
        this.rol        = rol;
    }

    public String getIdUsuario()           { return idUsuario; }
    public void   setIdUsuario(String id)  { this.idUsuario = id; }
    public String getNombreUser()          { return nombreUser; }
    public void   setNombreUser(String n)  { this.nombreUser = n; }
    public String getPassword()            { return password; }
    public void   setPassword(String p)    { this.password = p; }
    public String getRol()                 { return rol; }
    public void   setRol(String r)         { this.rol = r; }

    public boolean esAdministrador() { return "Administrador".equals(rol); }
    public boolean esSecretaria()    { return "Secretaria".equals(rol); }
    public boolean esMedico()        { return "Medico".equals(rol); }

    @Override
    public String toString() {
        return idUsuario + "," + nombreUser + "," + rol;
    }
}