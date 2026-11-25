package com.gymfit.manager.service;

import com.gymfit.manager.model.Sexo;
import com.gymfit.manager.model.Socio;
import com.gymfit.manager.model.CuotaTipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

// Gestiona en memoria la lista de socios y los catálogos de provincias y clases
public class ServiceSocio {

    // ObservableList avisa automáticamente a la TableView cada vez que añadimos, editamos o borramos un socio
    private final ObservableList<Socio> socios = FXCollections.observableArrayList();
    private int siguienteId = 1;

    // Lista cerrada y ordenada de provincias (adiós a recordar códigos INE como 41 o 28)
    public static final List<String> PROVINCIAS = List.of(
            "Álava", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz",
            "Barcelona", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ciudad Real",
            "Córdoba", "Cuenca", "Girona", "Granada", "Guadalajara", "Guipúzcoa", "Huelva",
            "Huesca", "Islas Baleares", "Jaén", "La Coruña", "La Rioja", "Las Palmas", "León",
            "Lleida", "Lugo", "Madrid", "Málaga", "Murcia", "Navarra", "Ourense", "Palencia",
            "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria",
            "Tarragona", "Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza"
    );

    // Reducimos las 34 opciones caóticas originales a las 10 clases principales del gimnasio
    public static final List<String> CLASES_DISPONIBLES = List.of(
            "Spinning", "Pilates", "Yoga", "Entrenamiento funcional", "Musculación",
            "BodyPump", "Zumba", "Boxeo", "Natación", "CrossTraining"
    );

    public ServiceSocio() {
        cargarDatosIniciales();
    }

    // Cargamos 4 socios de ejemplo para que la tabla tenga contenido desde el primer segundo
    private void cargarDatosIniciales() {
        agregarSocio("Marta", "Gonzalez Gonzalez", LocalDate.of(1999, 3, 12), Sexo.MUJER,
                "Sevilla", CuotaTipo.PREMIUM, List.of("Pilates", "Yoga"), "ES91 2100 0418 4502 0005 1332");
        agregarSocio("Ander", "Olivos Gamarra", LocalDate.of(2001, 7, 21), Sexo.HOMBRE,
                "Sevilla", CuotaTipo.BASICA, List.of("Musculación"), "ES79 2100 0813 6101 2345 6789");
        agregarSocio("Elena", "Saez Torres", LocalDate.of(1988, 11, 30), Sexo.MUJER,
                "Cádiz", CuotaTipo.PLUS, List.of("Spinning", "BodyPump"), "ES12 0049 1500 0512 3456 7892");
        agregarSocio("David", "Alonso Aguilar", LocalDate.of(2005, 2, 2), Sexo.HOMBRE,
                "Málaga", CuotaTipo.JOVEN, List.of("Boxeo", "CrossTraining"), "");
    }

    public ObservableList<Socio> getSocios() {
        return socios;
    }

    public Socio agregarSocio(String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, String provincia, CuotaTipo cuota, List<String> clases, String iban) {
        Socio nuevo = new Socio(siguienteId++, nombre.trim(), apellidos.trim(),
                fechaNacimiento, sexo, provincia, cuota, clases, iban != null ? iban.trim() : "");
        socios.add(nuevo);
        return nuevo;
    }

    public void actualizarSocio(Socio socio, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, String provincia, CuotaTipo cuota, List<String> clases, String iban) {

        socio.setNombre(nombre.trim());
        socio.setApellidos(apellidos.trim());
        socio.setFechaNacimiento(fechaNacimiento);
        socio.setSexo(sexo);
        socio.setProvincia(provincia);
        socio.setCuota(cuota);
        socio.setClases(clases);
        socio.setIban(iban != null ? iban.trim() : "");
        // Forzamos el refresco del elemento en la ObservableList para que la tabla se actualice al instante
        int index = socios.indexOf(socio);
        if (index >= 0) {
            socios.set(index, socio);
        }
    }

    public void eliminarSocio(Socio socio) {
        socios.remove(socio);
    }

    public void cambiarEstado(Socio socio) {
        socio.setActivo(!socio.isActivo());
        int index = socios.indexOf(socio);
        if (index >= 0) {
            socios.set(index, socio);
        }
    }

    public Socio duplicarSocio(Socio original) {
        return agregarSocio(
                original.getNombre() + " (copia)",
                original.getApellidos(),
                original.getFechaNacimiento(),
                original.getSexo(),
                original.getProvincia(),
                original.getCuota(),
                original.getClases(),
                original.getIban()
        );
    }
}