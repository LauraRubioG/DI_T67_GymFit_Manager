module com.gymfit.manager {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.gymfit.manager;
    // Permitimos a JavaFX leer las propiedades de Socio para rellenar la TableView
    opens com.gymfit.manager.model to javafx.base;
    // Permitimos a FXMLLoader inyectar los componentes @FXML en el controlador
    opens com.gymfit.manager.controller to javafx.fxml;
}