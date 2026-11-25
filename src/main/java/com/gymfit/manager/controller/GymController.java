package com.gymfit.manager.controller;

import com.gymfit.manager.model.Sexo;
import com.gymfit.manager.model.Socio;
import com.gymfit.manager.model.CuotaTipo;
import com.gymfit.manager.service.ServiceSocio;
import com.gymfit.manager.validation.SocioValidador;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

// Controlador principal enlazado con gym.fxml: gestiona eventos, validaciones y actualiza la tabla
public class GymController {

    // Controles del formulario inyectados desde gym.fxml
    @FXML private Label lblTituloFormulario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private HBox cajaSexo;
    @FXML private ComboBox<String> cbProvincia;
    @FXML private FlowPane cajaCuotas;
    @FXML private FlowPane cajaClases;
    @FXML private TextField txtIban;
    @FXML private Button btnGuardar;

    // Controles de la tabla, búsqueda y barra de estado
    @FXML private Label lblContador;
    @FXML private TextField txtBuscar;
    @FXML private TableView<Socio> tablaSocios;
    @FXML private TableColumn<Socio, String> colNombre;
    @FXML private TableColumn<Socio, String> colFecha;
    @FXML private TableColumn<Socio, String> colProvincia;
    @FXML private TableColumn<Socio, CuotaTipo> colCuota;
    @FXML private TableColumn<Socio, String> colClases;
    @FXML private TableColumn<Socio, String> colEstado;
    @FXML private Label lblEstado;

    // Servicio en memoria que guarda los socios y los catálogos de provincias y clases
    private final ServiceSocio socioService = new ServiceSocio();
    private FilteredList<Socio> sociosFiltrados;

    // Grupos y mapas para controlar fácilmente los RadioButtons y CheckBoxes generados
    private final ToggleGroup grupoSexo = new ToggleGroup();
    private final Map<Sexo, RadioButton> radiosSexo = new LinkedHashMap<>();
    private final ToggleGroup grupoCuota = new ToggleGroup();
    private final Map<CuotaTipo, RadioButton> radiosCuota = new LinkedHashMap<>();
    private final List<CheckBox> checksClases = new ArrayList<>();

    // Si es null estamos dando de alta un socio nuevo; si tiene valor estamos editando ese socio
    private Socio socioEnEdicion = null;

    // JavaFX llama a initialize() automáticamente justo después de cargar gym.fxml
    @FXML
    public void initialize() {
        cargarControlesFormulario();
        configurarFormatoFecha();
        configurarTablaYBusqueda();
        actualizarContador();
    }

    // Genera los RadioButtons de sexo y cuota, las casillas de clases y rellena el desplegable de provincias
    private void cargarControlesFormulario() {
        for (Sexo sexo : Sexo.values()) {
            RadioButton rb = new RadioButton(sexo.getEtiqueta());
            rb.setToggleGroup(grupoSexo);
            rb.setUserData(sexo);
            radiosSexo.put(sexo, rb);
            cajaSexo.getChildren().add(rb);
        }

        cbProvincia.getItems().addAll(ServiceSocio.PROVINCIAS);

        for (CuotaTipo cuota : CuotaTipo.values()) {
            RadioButton rb = new RadioButton(cuota.toString());
            rb.setToggleGroup(grupoCuota);
            rb.setUserData(cuota);
            radiosCuota.put(cuota, rb);
            cajaCuotas.getChildren().add(rb);
        }

        for (String clase : ServiceSocio.CLASES_DISPONIBLES) {
            CheckBox cb = new CheckBox(clase);
            checksClases.add(cb);
            cajaClases.getChildren().add(cb);
        }
    }

    // Configura el DatePicker para aceptar y mostrar fechas en formato español dd/MM/yyyy
    private void configurarFormatoFecha() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dpFechaNacimiento.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate fecha) {
                return fecha != null ? formato.format(fecha) : "";
            }

            @Override
            public LocalDate fromString(String texto) {
                if (texto == null || texto.trim().isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(texto.trim(), formato);
                } catch (DateTimeParseException e) {
                    // Si el formato escrito no es válido devolvemos null para que el validador avise con claridad
                    return null;
                }
            }
        });
    }

    // Enlaza cada columna con su getter de Socio y conecta el filtro de búsqueda en tiempo real
    private void configurarTablaYBusqueda() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimientoFormateada"));
        colProvincia.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        colCuota.setCellValueFactory(new PropertyValueFactory<>("cuota"));
        colClases.setCellValueFactory(new PropertyValueFactory<>("clasesTexto"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));

        // Ajustamos las columnas para que aprovechen todo el ancho de la tabla sin huecos vacíos
        tablaSocios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // FilteredList permite filtrar sin borrar datos reales; SortedList mantiene la ordenación por columnas
        sociosFiltrados = new FilteredList<>(socioService.getSocios(), s -> true);
        SortedList<Socio> sociosOrdenados = new SortedList<>(sociosFiltrados);
        sociosOrdenados.comparatorProperty().bind(tablaSocios.comparatorProperty());
        tablaSocios.setItems(sociosOrdenados);

        // Cada vez que el usuario escribe en el buscador actualizamos el filtro
        txtBuscar.textProperty().addListener((obs, anterior, nuevo) -> {
            String filtro = nuevo == null ? "" : nuevo.trim().toLowerCase();
            sociosFiltrados.setPredicate(socio -> {
                if (filtro.isEmpty()) return true;
                return socio.getNombreCompleto().toLowerCase().contains(filtro)
                        || socio.getProvincia().toLowerCase().contains(filtro)
                        || socio.getCuota().getNombre().toLowerCase().contains(filtro)
                        || socio.getClasesTexto().toLowerCase().contains(filtro);
            });
            actualizarContador();
        });

        // Actualizamos el contador automáticamente cuando se añade o elimina un socio
        socioService.getSocios().addListener((ListChangeListener<Socio>) c -> actualizarContador());
    }

    private void actualizarContador() {
        int visibles = sociosFiltrados.size();
        int total = socioService.getSocios().size();
        lblContador.setText(visibles == total
                ? total + " socios registrados"
                : visibles + " de " + total + " socios");
    }

    // ==================== ACCIONES DE MENÚ, TOOLBAR Y BOTONES ====================

    @FXML
    private void onNuevoSocio() {
        limpiarFormulario();
        txtNombre.requestFocus();
        lblEstado.setText("Nuevo alta · Rellena los campos obligatorios y pulsa Guardar socio.");
    }

    @FXML
    private void onGuardarSocio() {
        // Leemos los valores actuales de todos los controles del formulario
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        LocalDate fecha = dpFechaNacimiento.getValue();
        Sexo sexo = grupoSexo.getSelectedToggle() != null ? (Sexo) grupoSexo.getSelectedToggle().getUserData() : null;
        String provincia = cbProvincia.getValue();
        CuotaTipo cuota = grupoCuota.getSelectedToggle() != null ? (CuotaTipo) grupoCuota.getSelectedToggle().getUserData() : null;
        String iban = txtIban.getText();

        List<String> clasesSeleccionadas = new ArrayList<>();
        for (CheckBox cb : checksClases) {
            if (cb.isSelected()) {
                clasesSeleccionadas.add(cb.getText());
            }
        }

        // Validamos antes de guardar; si hay algún fallo mostramos un diálogo claro con solución
        Optional<String> error = SocioValidador.validar(nombre, apellidos, fecha, sexo, provincia, cuota, iban);
        if (error.isPresent()) {
            mostrarErrorValidacion(error.get());
            return;
        }

        if (socioEnEdicion == null) {
            Socio creado = socioService.agregarSocio(nombre, apellidos, fecha, sexo, provincia, cuota, clasesSeleccionadas, iban);
            tablaSocios.getSelectionModel().select(creado);
            lblEstado.setText("Socio dado de alta correctamente: " + creado.getNombreCompleto());
        } else {
            socioService.actualizarSocio(socioEnEdicion, nombre, apellidos, fecha, sexo, provincia, cuota, clasesSeleccionadas, iban);
            lblEstado.setText("Cambios guardados para: " + socioEnEdicion.getNombreCompleto());
        }

        limpiarFormulario();
    }

    @FXML
    private void onLimpiarFormulario() {
        limpiarFormulario();
        lblEstado.setText("Formulario limpio.");
    }

    @FXML
    private void onEnfocarBuscador() {
        txtBuscar.requestFocus();
        txtBuscar.selectAll();
    }

    @FXML
    private void onEditarSeleccionado() {
        Socio seleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAvisoSeleccion("editar");
            return;
        }

        // Cargamos todos los datos del socio seleccionado en el formulario y cambiamos a modo edición
        socioEnEdicion = seleccionado;
        lblTituloFormulario.setText("Editar socio");
        btnGuardar.setText("Guardar cambios");

        txtNombre.setText(seleccionado.getNombre());
        txtApellidos.setText(seleccionado.getApellidos());
        dpFechaNacimiento.setValue(seleccionado.getFechaNacimiento());
        if (radiosSexo.containsKey(seleccionado.getSexo())) {
            radiosSexo.get(seleccionado.getSexo()).setSelected(true);
        }
        cbProvincia.setValue(seleccionado.getProvincia());
        if (radiosCuota.containsKey(seleccionado.getCuota())) {
            radiosCuota.get(seleccionado.getCuota()).setSelected(true);
        }
        for (CheckBox cb : checksClases) {
            cb.setSelected(seleccionado.getClases().contains(cb.getText()));
        }
        txtIban.setText(seleccionado.getIban());

        txtNombre.requestFocus();
        lblEstado.setText("Editando ficha de: " + seleccionado.getNombreCompleto());
    }

    @FXML
    private void onDuplicarSeleccionado() {
        Socio seleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAvisoSeleccion("duplicar");
            return;
        }
        Socio copia = socioService.duplicarSocio(seleccionado);
        tablaSocios.getSelectionModel().select(copia);
        lblEstado.setText("Ficha duplicada: " + copia.getNombreCompleto());
    }

    @FXML
    private void onCambiarEstadoSeleccionado() {
        Socio seleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAvisoSeleccion("cambiar el estado");
            return;
        }
        socioService.cambiarEstado(seleccionado);
        lblEstado.setText("Estado actualizado: " + seleccionado.getNombreCompleto() + " (" + seleccionado.getEstadoTexto() + ")");
    }

    @FXML
    private void onEliminarSeleccionado() {
        Socio seleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAvisoSeleccion("eliminar");
            return;
        }

        // Diálogo de confirmación explícito con verbos claros para evitar borrados accidentales
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar la ficha de " + seleccionado.getNombreCompleto() + "?");
        confirmacion.setContentText("Esta acción quitará al socio del listado actual y no se puede deshacer.");

        ButtonType btnEliminar = new ButtonType("Eliminar socio", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(btnEliminar, btnCancelar);

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == btnEliminar) {
            if (seleccionado.equals(socioEnEdicion)) {
                limpiarFormulario();
            }
            socioService.eliminarSocio(seleccionado);
            lblEstado.setText("Socio eliminado: " + seleccionado.getNombreCompleto());
        }
    }

    @FXML
    private void onMostrarAtajos() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Atajos de teclado");
        info.setHeaderText("Atajos rápidos de GymFit Manager");
        info.setContentText(
                "Ctrl + N  →  Nuevo socio\n" +
                        "Ctrl + S  →  Guardar socio\n" +
                        "Ctrl + F  →  Buscar socio\n" +
                        "Ctrl + E  →  Editar socio seleccionado\n" +
                        "Ctrl + L  →  Limpiar formulario\n" +
                        "Supr      →  Eliminar socio seleccionado\n" +
                        "Ctrl + Q  →  Salir de la aplicación"
        );
        info.showAndWait();
    }

    @FXML
    private void onAcercaDe() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Acerca de GymFit Manager");
        info.setHeaderText("GymFit Manager · Gestión de recepción");
        info.setContentText("Aplicación rediseñada siguiendo criterios de usabilidad (Temas 6 y 7 · Desarrollo de Interfaces).");
        info.showAndWait();
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void limpiarFormulario() {
        socioEnEdicion = null;
        lblTituloFormulario.setText("Alta de nuevo socio");
        btnGuardar.setText("Guardar socio");
        txtNombre.clear();
        txtApellidos.clear();
        dpFechaNacimiento.setValue(null);
        dpFechaNacimiento.getEditor().clear();
        grupoSexo.selectToggle(null);
        cbProvincia.setValue(null);
        grupoCuota.selectToggle(null);
        checksClases.forEach(cb -> cb.setSelected(false));
        txtIban.clear();
    }

    // Muestra un mensaje de error comprensible, positivo y con botón de acción ("Corregir")
    private void mostrarErrorValidacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Revisa los datos del formulario");
        alerta.setHeaderText("No se pudo guardar el socio");
        alerta.setContentText(mensaje);

        ButtonType btnCorregir = new ButtonType("Corregir", ButtonBar.ButtonData.OK_DONE);
        alerta.getButtonTypes().setAll(btnCorregir);
        alerta.showAndWait();
    }

    private void mostrarAvisoSeleccion(String accion) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ningún socio seleccionado");
        alerta.setHeaderText("Selecciona primero un socio en la tabla");
        alerta.setContentText("Haz clic sobre la fila del socio que quieres " + accion + " y vuelve a intentarlo.");
        alerta.showAndWait();
    }
}