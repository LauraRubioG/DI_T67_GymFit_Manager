package com.gymfit.manager.validation;

import com.gymfit.manager.model.Sexo;
import com.gymfit.manager.model.CuotaTipo;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

// Esta clase la creamos para validar que todos y cada uno de los apartados del formulario
// En caso de que haya un error de formato, se indicara que es lo que está mal y como lo solucionamso
// Asi usamos el criterio de usabilidad de mensajes claros
public class SocioValidador {

    // Devuelve Optional.empty() si  esta todo bien, o Optional si no lo esta con un mensaje de error indicando que hay que corregir
    public static Optional<String> validar(String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, String provincia, CuotaTipo cuota, String iban) {

        // Usamos trim() para evitar que el usuario cuele solo espacios en blanco como nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.of("Falta el nombre del socio.\nEscribe el nombre en el primer campo (ejemplo: Laura).");
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            return Optional.of("Faltan los apellidos del socio.\nIntroduce al menos un apellido (ejemplo: Rubio Gallardo).");
        }

        // Si el DatePicker está vacío o la fecha escrita no se pudo interpretar la detecta como null
        if (fechaNacimiento == null) {
            return Optional.of("Falta la fecha de nacimiento.\nSelecciónala en el calendario o escríbela con formato dd/mm/aaaa (ejemplo: 17/03/2002).");
        }

        // Comprobamos que no sea una fecha futura ni de un menor de 14 años
        LocalDate hoy = LocalDate.now();
        if (fechaNacimiento.isAfter(hoy)) {
            return Optional.of("La fecha de nacimiento no puede ser futura.\nRevisa el año introducido y selecciona una fecha anterior a hoy.");
        }
        if (Period.between(fechaNacimiento, hoy).getYears() < 14) {
            return Optional.of("La edad mínima para inscribirse es de 14 años.\nComprueba la fecha de nacimiento o consulta las cuotas infantiles en recepción.");
        }

        if (sexo == null) {
            return Optional.of("No se ha seleccionado el sexo.\nMarca una de las opciones disponibles (Mujer, Hombre u Otro).");
        }

        if (provincia == null || provincia.trim().isEmpty()) {
            return Optional.of("Falta seleccionar la provincia.\nElige una provincia de la lista desplegable (ejemplo: Sevilla).");
        }

        if (cuota == null) {
            return Optional.of("Falta elegir el tipo de cuota.\nSelecciona una cuota (Básica, Plus, Premium, Joven o Senior).");
        }

        // El IBAN lo dejamos opcional para agilizar altas rápidas, pero si se escribe, validamos el siguiente formato
        if (iban != null && !iban.trim().isEmpty()) {
            String limpio = iban.replaceAll("\\s+", "").toUpperCase();
            if (!limpio.matches("^ES\\d{22}$")) {
                return Optional.of("El formato del IBAN no es válido.\nDebe empezar por ES seguido de 22 dígitos (ejemplo: ES91 2100 0418 4502 0005 1332).");
            }
        }

        //Si se pasa entero sin errores devolvemos el Optional.empty indicando que esta todo correcto
        return Optional.empty();
    }
}