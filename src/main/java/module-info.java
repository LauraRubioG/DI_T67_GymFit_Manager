module com.gymfit.manager {
    requires javafx.controls;
    exports com.gymfit.manager;
    // Permitimos a JavaFX leer las propiedades de Socio para rellenar la TableView
    opens com.gymfit.manager.model to javafx.base;
}