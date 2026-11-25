package com.gymfit.manager.model;

// Creamos los tipos de cuotas con sus propios precios que le saldran al recepcionista de forma clara
public enum CuotaTipo {
    BASICA("Básica", 29.90),
    PLUS("Plus", 39.90),
    PREMIUM("Premium", 49.90),
    JOVEN("Joven", 24.90),
    SENIOR("Senior", 22.90);

    //Atributos para cada cuota personalizada, ya que se pueden seleccionar varias
    private final String nombre;
    private final double precioMensual;

    //Constructor
    CuotaTipo(String nombre, double precioMensual) {
        this.nombre = nombre;
        this.precioMensual = precioMensual;
    }

    public String getNombre() {
        return nombre;
    }
    public double getPrecioMensual() {
        return precioMensual;
    }
    // Forma en la que veremos la couta en la pantalla
    @Override
    public String toString() {
        return nombre + " (" + String.format("%.2f €", precioMensual) + ")";
    }
}