package Logical;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchivoManager { // Nombre con minúscula para no romper tus referencias

    private static final String FILE_USUARIOS = "usuarios.txt";
    private static final String FILE_MEDICOS = "Medico.txt";
    private static final String FILE_PACIENTES = "Paciente.txt";
    private static final String FILE_VACUNAS = "vacunas.txt";
    private static final String FILE_ENFERMEDAD = "enfermedad.txt";
    private static final String FILE_VIVIENDAS = "vivienda.txt";
    private static final String FILE_CONSULTAS = "consultas.txt";
    private static final String FILE_CITAS = "citas.txt";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // ================= MÉTODO GENÉRICO PARA GUARDAR =================
    public static <T> void guardarDatos(String filePath, List<T> entidades) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            for (T entidad : entidades) {
                writer.println(entidad.toString());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar en: " + filePath);
        }
    }

    // ================= PARSERS DE FECHAS =================
    private static LocalDate parseLocalDate(String dateStr) {
        return (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) ? null : LocalDate.parse(dateStr);
    }
    private static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) return null;
        try { return new Date(dateFormat.parse(dateStr).getTime()); } catch (ParseException e) { return null; }
    }

    // ================= USUARIOS =================
    public static void guardarUsuarios(List<Usuario> lista) { guardarDatos(FILE_USUARIOS, lista); }
    public static ArrayList<Usuario> leerUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        File file = new File(FILE_USUARIOS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 4) lista.add(new Usuario(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= VIVIENDAS =================
    public static void guardarViviendas(List<Vivienda> lista) { guardarDatos(FILE_VIVIENDAS, lista); }
    public static ArrayList<Vivienda> leerViviendas() {
        ArrayList<Vivienda> lista = new ArrayList<>();
        File file = new File(FILE_VIVIENDAS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) lista.add(new Vivienda(parts[0], parts[1], parts[2], new ArrayList<>()));
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= MEDICOS =================
    public static void guardarMedicos(List<Medico> lista) { guardarDatos(FILE_MEDICOS, lista); }
    public static ArrayList<Medico> leerMedicos(Clinica c) {
        ArrayList<Medico> lista = new ArrayList<>();
        File file = new File(FILE_MEDICOS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 8) { 
                    Vivienda v = c.obtenervivienda(parts[5]);
                    lista.add(new Medico(parts[0], parts[1], parts[2], parseLocalDate(parts[3]), parts[4], v, parts[6], parts[7]));
                }
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= PACIENTES =================
    public static void guardarPacientes(List<Paciente> lista) { guardarDatos(FILE_PACIENTES, lista); }
    public static ArrayList<Paciente> leerPacientes(Clinica c) {
        ArrayList<Paciente> lista = new ArrayList<>();
        File file = new File(FILE_PACIENTES);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 8) {
                    Vivienda v = c.obtenervivienda(parts[5]);
                    lista.add(new Paciente(parts[0], parts[1], parts[2], parseLocalDate(parts[3]), parts[4], parts[6], v, parts[7]));
                }
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= VACUNAS =================
    public static void guardarVacunas(List<Vacuna> lista) { guardarDatos(FILE_VACUNAS, lista); }
    public static ArrayList<Vacuna> leerVacunas(Clinica c) {
        ArrayList<Vacuna> lista = new ArrayList<>();
        File file = new File(FILE_VACUNAS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    Paciente p = c.obtenerPacienteById(parts[2]);
                    lista.add(new Vacuna(parts[0], parts[1], p, Integer.parseInt(parts[3]), parts[4]));
                }
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= ENFERMEDADES =================
    public static void guardarEnfermedades(List<Enfermedad> lista) { guardarDatos(FILE_ENFERMEDAD, lista); }
    public static ArrayList<Enfermedad> leerEnfermedades(Clinica c) {
        ArrayList<Enfermedad> lista = new ArrayList<>();
        File file = new File(FILE_ENFERMEDAD);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    Vacuna v = c.obtenervacuna(parts[4]);
                    lista.add(new Enfermedad(parts[0], parts[1], parts[2], parts[3], v, Boolean.parseBoolean(parts[5])));
                }
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= CITAS =================
    public static void guardarCitas(List<Cita> lista) { guardarDatos(FILE_CITAS, lista); }
    public static ArrayList<Cita> leerCitas(Clinica c) {
        ArrayList<Cita> lista = new ArrayList<>();
        File file = new File(FILE_CITAS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    Paciente p = c.obtenerPacienteById(parts[1]);
                    Medico m = c.obtenerMedicoById(parts[2]);
                    lista.add(new Cita(parts[0], p, m, parseDate(parts[3])));
                }
            }
        } catch (IOException e) {}
        return lista;
    }

    // ================= CONSULTAS =================
    public static void guardarConsultas(List<Consultas> lista) { guardarDatos(FILE_CONSULTAS, lista); }
    public static ArrayList<Consultas> leerConsultas(Clinica c) {
        ArrayList<Consultas> lista = new ArrayList<>();
        File file = new File(FILE_CONSULTAS);
        if (!file.exists()) return lista;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    List<Enfermedad> sintomas = new ArrayList<>();
                    if (!parts[2].isEmpty() && !parts[2].equals("null")) {
                        for (String sId : parts[2].split(";")) {
                            Enfermedad e = c.obtenerEnfermedadById(sId);
                            if (e != null) sintomas.add(e);
                        }
                    }
                    Medico m = c.obtenerMedicoById(parts[3]);
                    Paciente p = c.obtenerPacienteById(parts[4]);
                    lista.add(new Consultas(parts[0], parseDate(parts[1]), sintomas, m, p));
                }
            }
        } catch (IOException e) {}
        return lista;
    }
}