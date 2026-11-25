package com.gymfit.manager.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Clase para guardas los datos de cada uno de los socios, "Plantilla de subscripcion"
public class Socio {

    // Formato para guardar las fechas
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    //ATRIBUTOS
    //el id lo guardamos como final porque una vez que lo tenga no puede cambiar nunca
    private final int id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private String provincia;
    private CuotaTipo cuota;
    //Esta lista la usamos para que el socio se pueda apuntar a varias clases
    private List<String> clases;
    private String iban;
    //Y aqui controlamos si el socio sigue activo o no con el gym
    private boolean activo;

    //Constructor
    public Socio(int id, String nombre, String apellidos, LocalDate fechaNacimiento,
                 Sexo sexo, String provincia, CuotaTipo cuota, List<String> clases, String iban) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.provincia = provincia;
        this.cuota = cuota;
        // Esto es una copia de la lista para que por si hay cambios en las clases no afecten
        this.clases = new ArrayList<>(clases);
        this.iban = iban;
        //Todo socio que creemos empezara como activo siempre, lo estamos creando por lo mismo
        this.activo = true;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos; }


    // Aqui mostramos juntos en una sola columna de la tabla
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Convertimos las fechas en texto(ej. 14/05/2003)
    public String getFechaNacimientoFormateada() {
        return fechaNacimiento != null ? fechaNacimiento.format(FORMATO_FECHA) : "";
    }

    public Sexo getSexo() {
        return sexo;
    }
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public CuotaTipo getCuota() {
        return cuota;
    }
    public void setCuota(CuotaTipo cuota) {
        this.cuota = cuota;
    }
    // Para protegerlo damos y guardamos la copia de la lista
    public List<String> getClases() {
        return new ArrayList<>(clases);
    }
    public void setClases(List<String> clases) {
        this.clases = new ArrayList<>(clases);
    }

    // Junta las clases separadas por comas para que se lean bien en la tabla
    public String getClasesTexto() {
        return clases.isEmpty() ? "Sin clases" : String.join(", ", clases);
    }

    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
     //Convertimos el booleano en texto leible para el recepcionits
    public String getEstadoTexto() {
        return activo ? "Activo" : "Baja";
    }

    //Usamos tantos getter y setter para poder mostrar la informacion del socio
}