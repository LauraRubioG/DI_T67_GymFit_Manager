package com.gymfit.manager;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        //Cambiamos el que viene por defecto HelloApplication por el nombre de nuestra clase
        Application.launch(GymFitApp.class, args);
    }
}
