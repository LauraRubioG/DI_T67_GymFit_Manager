package com.gymfit.manager.model;

// Para que el recepcionista no tenga que meter las letras a mano creamos las opciones cerradas de los tipos de sexo
public enum Sexo {
    MUJER("Mujer"),
    HOMBRE("Hombre"),
    OTRO("Otro");

    private final String etiqueta;

    Sexo(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    public String getEtiqueta() {
        return etiqueta;
    }
    //Aqui lo sobrescribimos para que a la hora de salir se vea mas bonito
    @Override
    public String toString() {
        return etiqueta;
    }
}