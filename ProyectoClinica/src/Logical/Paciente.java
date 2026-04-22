package Logical;

import java.time.LocalDate;

public class Paciente extends Persona {
    private String idPaciente;
    private String infoEmergencia;
    // Quitamos 'historial' del constructor temporalmente para simplificar la escritura en texto plano
    // En un sistema real de Base de Datos, el historial se carga con una consulta aparte.

    public Paciente(String cedula, String nombre, String genero, LocalDate fechaNacimiento, String telefono,
                    String idPaciente, Vivienda vivienda, String infoEmergencia) {
        super(cedula, nombre, genero, fechaNacimiento, telefono, vivienda);
        this.idPaciente = idPaciente;
        this.infoEmergencia = infoEmergencia;
    }

    @Override
    public String toString() {
        // Genera EXACTAMENTE 8 campos
        String idVivienda = (vivienda != null) ? vivienda.getIdVivienda() : "null";
        return String.join(",", cedula, nombre, genero, formatearFecha(), telefono, idVivienda, idPaciente, infoEmergencia);
    }

    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String idPaciente) { this.idPaciente = idPaciente; }
    public String getInfoEmergencia() { return infoEmergencia; }
    public void setInfoEmergencia(String infoEmergencia) { this.infoEmergencia = infoEmergencia; }
}