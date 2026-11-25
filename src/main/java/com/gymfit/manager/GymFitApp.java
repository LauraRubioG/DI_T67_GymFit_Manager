// Declaramos el paquete al que pertenece esta clase para que coincida con la estructura de carpetas
package com.gymfit.manager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 * Hereda de Application para que JavaFX pueda inicializar el hilo gráfico y lanzar la ventana.
 */
public class GymFitApp extends Application {

    /**
     * Punto de entrada gráfico de JavaFX: el framework llama automáticamente a start()
     * cuando ejecutamos Application.launch() desde Launcher.
     *
     * @param stage La ventana principal (escenario) que JavaFX crea por defecto para nosotros.
     */
    @Override
    public void start(Stage stage) {
        // Creamos el contenedor raíz (root) usando StackPane para que el texto de prueba salga centrado en la ventana
        StackPane root = new StackPane(new Label("GymFit Manager - Arranque correcto"));

        // Creamos la escena pasando el contenedor raíz y las dimensiones iniciales (1280x800, las pedidas en el enunciado)
        Scene scene = new Scene(root, 1280, 800);

        // Ponemos el título limpio en la barra superior de la ventana (sin versiones ni nombres de archivo raros)
        stage.setTitle("GymFit Manager");

        // Asociamos la escena que acabamos de crear a la ventana principal
        stage.setScene(scene);

        // Fijamos un ancho mínimo de 1024px para evitar que la interfaz se aplaste o se rompa si el usuario reduce la ventana
        stage.setMinWidth(1024);

        // Fijamos un alto mínimo de 700px por el mismo motivo de usabilidad y legibilidad
        stage.setMinHeight(700);

        // Hacemos visible la ventana en pantalla; sin esta llamada la app arrancaría en segundo plano sin mostrar nada
        stage.show();
    }
}