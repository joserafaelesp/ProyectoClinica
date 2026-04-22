package Logical;

import java.time.LocalDate;

public class Medico extends Persona {
    private String idMedico;
    private String especialidad;

    public Medico(String cedula, String nombre, String genero, LocalDate fechaNacimiento, String telefono,
                  Vivienda vivienda, String idMedico, String especialidad) {
        super(cedula, nombre, genero, fechaNacimiento, telefono, vivienda);
        this.idMedico = idMedico;
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        // Genera EXACTAMENTE 8 campos
        String idVivienda = (vivienda != null) ? vivienda.getIdVivienda() : "null";
        return String.join(",", cedula, nombre, genero, formatearFecha(), telefono, idVivienda, idMedico, especialidad);
    }

    public String getIdMedico() { return idMedico; }
    public void setIdMedico(String idMedico) { this.idMedico = idMedico; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}